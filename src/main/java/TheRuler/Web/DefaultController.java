package TheRuler.Web;

import TheRuler.Common.BaseXClient;
import TheRuler.Common.Config;
import TheRuler.Common.Utils;
import TheRuler.Exceptions.GenericException;
import TheRuler.Exceptions.NotFoundException;
import TheRuler.Model.*;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for handling standard requests.
 *
 * @author Peter Gren
 */
@Controller
public class DefaultController {

    /**
     * Displays home page - grammar listing.
     *
     * @param model Empty ModelMap.
     * @return The grammar page view.
     */
    @RequestMapping("/")
    public String index(ModelMap model) {
//        try {
//            if (!Boolean.TRUE.toString().equals(Config.getValue(Config.C_DB_INST))) {
//                return "redirect:/install";
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(DefaultController.class.getName()).log(Level.SEVERE, null, ex);
//        }

        BaseXClient baseXClient = null;

        try {
            baseXClient = Utils.connectToBaseX();

            GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
            grammarManager.setBaseXClient(baseXClient);

            List<GrammarMeta> grammarMetas = grammarManager.findAllGrammarMetas();

            model.addAttribute("grammarMetas", grammarMetas);
        } catch (Exception e) {
            throw new GenericException("Pri priojeni k databaze doslo k chybe"); // =========================
        } finally {
            if (baseXClient != null) {
                try {
                    baseXClient.close();
                } catch (IOException ex) {
                    Logger.getLogger(DefaultController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return "index";
    }

    /**
     * Displays grammar page - rule listing.
     *
     * @param model Empty ModelMap.
     * @param id ID of grammar.
     * @param request Request for accesing url params.
     * @return The grammar page view.
     */
    @RequestMapping(value = "/grammar/{id}", method = RequestMethod.GET)
    public String grammar(ModelMap model, @PathVariable String id, HttpServletRequest request) {

        if (id == null || id.equals("")) {
            throw new IllegalArgumentException();
        }

        boolean search = false;

        if (request.getParameter("search") != null) {
            search = true;
            if (request.getParameter("name").equals("")) {
                //throw new IllegalArgumentException();
                return "redirect:/grammar/" + id;
            }
        }

        try {
            BaseXClient baseXClient = Utils.connectToBaseX();
            GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
            grammarManager.setBaseXClient(baseXClient);

            GrammarMeta gm = grammarManager.findGrammarMeta(Long.parseLong(id));

            model.addAttribute("gm", gm);

            RuleManagerBaseXImpl ruleManager = new RuleManagerBaseXImpl();
            ruleManager.setBaseXClient(baseXClient);

            List<Rule> rules;

            if (search) {
                rules = ruleManager.findAllRulesById(request.getParameter("name").toLowerCase(), gm);
            } else {
                rules = ruleManager.findAllRules(gm);
            }

            model.addAttribute("rules", rules);
            model.addAttribute("search", search);
            model.addAttribute("searchString", request.getParameter("name"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "grammar";
    }

    /**
     * Displays grammar meta edit page.
     *
     * @param model Empty ModelMap.
     * @param id ID of grammar.
     * @return The grammar meta edit view.
     */
    @RequestMapping(value = "/edit-grammar/{id}")
    public String grammarEdit(ModelMap model, @PathVariable String id) {

        if (id.equals("") || id == null) {
            throw new IllegalArgumentException();
        }

        try {
            BaseXClient baseXClient = Utils.connectToBaseX();
            GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
            grammarManager.setBaseXClient(baseXClient);

            Grammar grammar = grammarManager.findGrammar(Long.parseLong(id));

            RuleManagerBaseXImpl ruleManager = new RuleManagerBaseXImpl();
            ruleManager.setBaseXClient(baseXClient);
            List<Rule> rules = ruleManager.findAllRules(grammar.getMeta());

            model.addAttribute("grammar", grammar);
            model.addAttribute("rules", rules);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "grammarMetaEdit";
    }

    /**
     * Saves posted grammar and redirects to grammar page.
     *
     * @param grammar A grammar from HTML form.
     * @return Redirects to the grammar page view.
     */
    @RequestMapping(value = "/save-grammar", method = RequestMethod.POST)
    public String doSaveGrammar(Grammar grammar) {

        if (grammar == null) {
            throw new IllegalArgumentException();
        }

        BaseXClient baseXClient = null;

        try {
            baseXClient = Utils.connectToBaseX();
            GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
            grammarManager.setBaseXClient(baseXClient);

            grammarManager.updateGrammar(grammar);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (baseXClient != null) {
                try {
                    baseXClient.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        //model.addAttribute("cv", initedCvDocument.getCv());
        return "redirect:/grammar/" + grammar.getId();
        //return "redirect:/edit-grammar/" + grammar.getMeta().getId();
    }
    
    /**
     * Saves posted rule and redirects to grammar page.
     *
     * @param rule A rule from HTML form.
     * @return Redirects to the grammar page view.
     */
    @RequestMapping(value = "/save-rule", method = RequestMethod.POST)
    public String doSaveRule(Rule rule) {

        if (rule == null) {
            throw new IllegalArgumentException();
        }

        BaseXClient baseXClient = null;

        try {
            baseXClient = Utils.connectToBaseX();
            RuleManagerBaseXImpl ruleManager = new RuleManagerBaseXImpl();
            ruleManager.setBaseXClient(baseXClient);

            // TODO
            GrammarMeta gm = new GrammarMeta();
            gm.setId(rule.getGrammarId());
            
            ruleManager.updateRule(rule, gm);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (baseXClient != null) {
                try {
                    baseXClient.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return "redirect:/grammar/" + rule.getGrammarId().toString();
    }

    /**
     * Creates new grammar with posted name and redirects to grammar meta edit
     * page of created grammar.
     *
     * @param request HTTP Request with posted data - grammar name.
     * @return Redirects to the grammar meta edit view.
     */
    @RequestMapping(value = "/create-grammar", method = RequestMethod.POST)
    public String doCreateGrammar(HttpServletRequest request) {
        if (request.getParameter("name").equals("")) {
            throw new IllegalArgumentException();
        }

//            try {
//                request.setCharacterEncoding("UTF-8");
//            } catch (UnsupportedEncodingException ex) {
//                Logger.getLogger(DefaultController.class.getName()).log(Level.SEVERE, null, ex);
//            }

        String s = request.getParameter("name");

        BaseXClient baseXClient = null;
        GrammarMeta gm = new GrammarMeta();
        gm.setName(request.getParameter("name"));

        try {
            baseXClient = Utils.connectToBaseX();
            GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
            grammarManager.setBaseXClient(baseXClient);

            gm = grammarManager.createGrammar(gm);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (baseXClient != null) {
                try {
                    baseXClient.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return "redirect:/grammar/" + gm.getId();
    }

    /**
     * Deletes grammar according to ID.
     *
     * @param id ID of grammar.
     * @return The grammar page view.
     */
    @RequestMapping(value = "/delete-grammar/{id}")
    public String doDeleteGrammar(@PathVariable String id) {

        if (id.equals("") || id == null) {
            throw new IllegalArgumentException();
        }

        BaseXClient baseXClient = null;
        GrammarMeta gm = new GrammarMeta();
        gm.setId(Long.parseLong(id));

        try {
            baseXClient = Utils.connectToBaseX();
            GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
            grammarManager.setBaseXClient(baseXClient);

            grammarManager.deletaGrammar(gm);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (baseXClient != null) {
                try {
                    baseXClient.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return "redirect:/";
    }

    /**
     * Adds new rule with posted name and redirects to grammar page with rule
     * listing.
     *
     * @param request Request with posted rule name.
     * @return Redirects to the grammar page view.
     */
    @RequestMapping(value = "/rule-add", method = RequestMethod.POST)
    public String doCreateRule(HttpServletRequest request) {

        if (request.getParameter("ruleId") == null || request.getParameter("ruleId").equals("")
                || request.getParameter("grammarId") == null || request.getParameter("grammarId").equals("")) {
            throw new IllegalArgumentException();
        }

        BaseXClient baseXClient = null;
        GrammarMeta gm = new GrammarMeta();
        gm.setId(Long.parseLong(request.getParameter("grammarId")));

        try {
            baseXClient = Utils.connectToBaseX();
            RuleManagerBaseXImpl ruleManager = new RuleManagerBaseXImpl();
            ruleManager.setBaseXClient(baseXClient);

            Rule rule = new Rule();
            rule.setId(request.getParameter("ruleId"));

            ruleManager.addRule(rule, gm);


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (baseXClient != null) {
                try {
                    baseXClient.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return "redirect:/grammar/" + gm.getId();
    }

    /**
     * Displays page for editing given rule.
     *
     * @param model Empty ModelMap.
     * @param grammarId ID of grammar containg rule.
     * @param ruleId ID of rule.
     * @return The rule edit view.
     */
    @RequestMapping(value = "/grammar/{grammarId}/rule/{ruleId}")
    public String ruleEdit(ModelMap model, @PathVariable String grammarId, @PathVariable String ruleId, HttpServletRequest request) {

        if (grammarId.equals("") || grammarId == null || ruleId.equals("") || ruleId == null) {
            throw new IllegalArgumentException();
        }
        
        try {
            BaseXClient baseXClient = Utils.connectToBaseX();
            GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
            grammarManager.setBaseXClient(baseXClient);
            RuleManagerBaseXImpl ruleManager = new RuleManagerBaseXImpl();
            ruleManager.setBaseXClient(baseXClient);
            
            GrammarMeta gm = grammarManager.findGrammarMeta(Long.parseLong(grammarId));
            Rule rule = ruleManager.findRuleById(ruleId, gm);
            if (rule == null) {
                throw new GenericException("Rule not found."); // ========================================== Lokalizacie chybocyh hlasok
            }
            List<Rule> rules = ruleManager.findAllRules(gm);

            model.addAttribute("gm", gm);
            model.addAttribute("rule", rule);
            model.addAttribute("rules", rules);
        } catch (GenericException ge) {
            throw new GenericException(ge.getMessage());  
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "ruleEdit";
    }

    /**
     * Deletes rule from grammar nad redirects to grammar page with rule
     * listing.
     *
     * @param grammarId ID of grammar containg rule.
     * @param ruleId ID of rule.
     * @return Redirects to the grammar page view.
     */
    @RequestMapping(value = "/delete-rule/{grammarId}/{ruleId}")
    public String doDeleteRule(@PathVariable Long grammarId, @PathVariable String ruleId) {
        if (grammarId == null || grammarId == 0 || ruleId == null || ruleId.equals("")) {
            throw new IllegalArgumentException();
        }

        BaseXClient baseXClient = null;
        GrammarMeta gm = new GrammarMeta();
        gm.setId(grammarId);
        Rule rule = new Rule();
        rule.setId(ruleId);

        try {
            baseXClient = Utils.connectToBaseX();
            RuleManagerBaseXImpl ruleManager = new RuleManagerBaseXImpl();
            ruleManager.setBaseXClient(baseXClient);

            ruleManager.deleteRule(rule, gm);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (baseXClient != null) {
                try {
                    baseXClient.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return "redirect:/grammar/" + gm.getId();
    }

    /**
     * Installs database and redirects to grammar page.
     *
     * @param request Request with connection data.
     * @return Redirects to the grammar page view.
     */
    @RequestMapping(value = "/doInstall", method = RequestMethod.POST)
    public String doInstall(HttpServletRequest request) {
//            if (request.getParameter("ruleId") == null || request.getParameter("ruleId").equals("") 
//             || request.getParameter("grammarId") == null || request.getParameter("grammarId").equals("")) {
//                throw new IllegalArgumentException();
//            }

        try {
            if (Boolean.TRUE.toString().equals(Config.getValue(Config.C_DB_INST))) {
                throw new NotFoundException();
            }
        } catch (IOException ioe) {
        }

        String host = request.getParameter("inputHost");
        String user = request.getParameter("inputUser");
        String pass = request.getParameter("inputPass");
        String name = request.getParameter("inputName");
        String port = request.getParameter("inputPort");
        try {
            // If DB is already seted as installed - do not process
            if (Boolean.parseBoolean(Config.getValue(Config.C_DB_INST))) {
                throw new NotFoundException();
            }

            Config.setValue(Config.C_DB_HOST, host);
            Config.setValue(Config.C_DB_USER, user);
            Config.setValue(Config.C_DB_PASS, pass);
            Config.setValue(Config.C_DB_NAME, name);
            Config.setValue(Config.C_DB_PORT, port);


        } catch (IOException ex) {
            Logger.getLogger(DefaultController.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            Utils.installDB();
            Config.setValue(Config.C_DB_INST, Boolean.TRUE.toString());
        } catch (Exception e) {
            String s = e.getMessage();
        }
        try {
            // Set DB asi installed into properties file

            Config.setValue(Config.C_DB_INST, Boolean.TRUE.toString());
        } catch (IOException ex) {
            Logger.getLogger(DefaultController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "redirect:/";
    }

    /**
     * Displays installation page.
     *
     * @param model Empty ModelMap.
     * @return The installation page view.
     */
    @RequestMapping(value = "/install", method = RequestMethod.GET)
    public String install(ModelMap model) {
        try {
            if (Boolean.TRUE.toString().equals(Config.getValue(Config.C_DB_INST))) {
                throw new NotFoundException();
            }
        } catch (IOException ioe) {
        }

        return "install";
    }

    /**
     * Export given grammar into text/plain file named export.xml.
     *
     * @param response HTTP response used for HTTP header manipulation.
     * @param grammarId ID of grammar stated for export.
     * @return Response body - UTF-8 encoded text/plain file named export.xml.
     */
    @RequestMapping(value = "/export/{grammarId}", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String doExport(HttpServletResponse response, @PathVariable Long grammarId) {
        response.setHeader("Content-Disposition", "attachment;filename=\"export.xml\"");
        if (grammarId == null || grammarId == 0) {
            throw new IllegalArgumentException();
        }

        BaseXClient baseXClient = null;
        try {
            baseXClient = Utils.connectToBaseX();
            GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
            grammarManager.setBaseXClient(baseXClient);

            Grammar grammar = grammarManager.findGrammar(grammarId);

            return grammar.getContent();

        } catch (Exception e) {
            return "ERROR";
        } finally {
            if (baseXClient != null) {
                try {
                    baseXClient.close();
                } catch (IOException e) {
                    return "ERROR";
                }
            }
        }
    }
}
