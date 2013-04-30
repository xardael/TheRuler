package TheRuler.Web;

import TheRuler.Common.BaseXClient;
import TheRuler.Common.Config;
import TheRuler.Common.Utils;
import TheRuler.Exceptions.*;
import TheRuler.Model.*;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling standard requests.
 *
 * @author Peter Gren
 */
@Controller
public class DefaultController {
    
    private static final Logger LOGGER = Logger.getLogger(DefaultController.class);
    private ResourceBundle rb = ResourceBundle.getBundle("locale/messages");
    private BaseXClient baseXClient = null;
        
    /**
     * Displays home page - grammar listing.
     *
     * @param model Empty ModelMap.
     * @return The grammar page view.
     */
    @RequestMapping("/")
    public String index(ModelMap model) {
        try {
            baseXClient = Utils.connectToBaseX();

            GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
            grammarManager.setBaseXClient(baseXClient);

            List<GrammarMeta> grammarMetas = grammarManager.findAllGrammarMetas();

            model.addAttribute("grammarMetas", grammarMetas);
        } catch (DatabaseException e) {
            throw new GenericException(rb.getString("dbError"), e);
        } finally {
            try {baseXClient.close();} catch (Exception e) {LOGGER.log(Level.ERROR, e);}
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
    public String grammar(ModelMap model, @PathVariable Long id, @RequestParam(required = false) String search, @RequestParam(required = false) String name) {
        boolean isSearch = false;

        if (search != null) {
            isSearch = true;
            if (name.trim().equals("")) {
                return "redirect:/grammar/" + id;
            }
        }
        
        try {
            baseXClient = Utils.connectToBaseX();
            GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
            grammarManager.setBaseXClient(baseXClient);

            GrammarMeta gm = grammarManager.findGrammarMeta(id);
            
            if (gm == null) {
                throw new NotFoundException();
            }

            model.addAttribute("gm", gm);

            RuleManagerBaseXImpl ruleManager = new RuleManagerBaseXImpl();
            ruleManager.setBaseXClient(baseXClient);

            List<Rule> rules;

            if (isSearch) {
                rules = ruleManager.findAllRulesById(name.toLowerCase(), gm);
            } else {
                rules = ruleManager.findAllRules(gm);
            }

            model.addAttribute("rules", rules);
            model.addAttribute("search", isSearch);
            model.addAttribute("searchString", name);

            return "grammar";
        } catch (DatabaseException e) {
            throw new GenericException(rb.getString("dbError"), e);
        } finally {
            try {baseXClient.close();} catch (Exception e) {LOGGER.log(Level.ERROR, e);}
        }
    }

    /**
     * Displays grammar meta edit page.
     *
     * @param model Empty ModelMap.
     * @param id ID of grammar.
     * @return The grammar meta edit view.
     */
    @RequestMapping(value = "/edit-grammar/{id}")
    public String grammarEdit(ModelMap model, @PathVariable Long id) {
        try {
            baseXClient = Utils.connectToBaseX();
            GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
            grammarManager.setBaseXClient(baseXClient);

            Grammar grammar = grammarManager.findGrammar(id);
            if (grammar == null) {
                throw new NotFoundException();
            }

            RuleManagerBaseXImpl ruleManager = new RuleManagerBaseXImpl();
            ruleManager.setBaseXClient(baseXClient);
            List<Rule> rules = ruleManager.findAllRules(grammar.getMeta());

            model.addAttribute("grammar", grammar);
            model.addAttribute("rules", rules);
            
            return "grammarMetaEdit";
        } catch (DatabaseException e) {
            throw new GenericException(rb.getString("dbError"), e);
        } finally {
            try {baseXClient.close();} catch (Exception e) {LOGGER.log(Level.ERROR, e);}
        }
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

        if (grammar.getName() == null || "".equals(grammar.getName().trim())) {
            throw new GenericException(rb.getString("emptyGrammarName"));
        }
        
        try {
            baseXClient = Utils.connectToBaseX();
            GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
            grammarManager.setBaseXClient(baseXClient);

            grammarManager.updateGrammar(grammar);

            return "redirect:/grammar/" + grammar.getId();
        } catch (DatabaseException e) {
            throw new GenericException(rb.getString("dbError"), e);
        } finally {
            try {baseXClient.close();} catch (Exception e) {LOGGER.log(Level.ERROR, e);}
        }
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

        try {
            baseXClient = Utils.connectToBaseX();
            RuleManagerBaseXImpl ruleManager = new RuleManagerBaseXImpl();
            ruleManager.setBaseXClient(baseXClient);           
            ruleManager.updateRule(rule);
            return "redirect:/grammar/" + rule.getGrammarId().toString();
        } catch (DatabaseException e) {
            throw new GenericException(rb.getString("dbError"), e);
        } finally {
            try {baseXClient.close();} catch (Exception e) {LOGGER.log(Level.ERROR, e);}
        }
    }

    /**
     * Creates new grammar with posted name and redirects to grammar meta edit
     * page of created grammar.
     *
     * @param request HTTP Request with posted data - grammar name.
     * @return Redirects to the grammar meta edit view.
     */
    @RequestMapping(value = "/create-grammar", method = RequestMethod.POST)
    public String doCreateGrammar(@RequestParam String name) {
        if ("".equals(name)) {
            throw new GenericException(rb.getString("emptyGrammarName"));
        }

        GrammarMeta gm = new GrammarMeta();
        gm.setName(name.trim());

        try {
            baseXClient = Utils.connectToBaseX();
            GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
            grammarManager.setBaseXClient(baseXClient);

            gm = grammarManager.createGrammar(gm);

            return "redirect:/grammar/" + gm.getId();
        } catch (DatabaseException e) {
            throw new GenericException(rb.getString("dbError"), e);
        } finally {
            try {baseXClient.close();} catch (Exception e) {LOGGER.log(Level.ERROR, e);}
        }
    }

    /**
     * Deletes grammar according to ID.
     *
     * @param id ID of grammar.
     * @return The grammar page view.
     */
    @RequestMapping(value = "/delete-grammar/{id}")
    public String doDeleteGrammar(@PathVariable Long id) {
        GrammarMeta gm = new GrammarMeta();
        gm.setId(id);

        try {
            baseXClient = Utils.connectToBaseX();
            GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
            grammarManager.setBaseXClient(baseXClient);

            grammarManager.deletaGrammar(gm);

            return "redirect:/";
        } catch (DatabaseException e) {
            throw new GenericException(rb.getString("dbError"), e);
        } finally {
            try {baseXClient.close();} catch (Exception e) {LOGGER.log(Level.ERROR, e);}
        }
    }

    /**
     * Adds new rule with posted name and redirects to grammar page with rule
     * listing.
     *
     * @param request Request with posted rule name.
     * @return Redirects to the grammar page view.
     */
    @RequestMapping(value = "/rule-add", method = RequestMethod.POST)
    public String doCreateRule(@RequestParam String ruleId, @RequestParam Long grammarId) {
        if ("".equals(ruleId)) {
            throw new GenericException(rb.getString("emptyRuleName"));
        }
        
        try {
            baseXClient = Utils.connectToBaseX();
            RuleManagerBaseXImpl ruleManager = new RuleManagerBaseXImpl();
            ruleManager.setBaseXClient(baseXClient);
            Rule rule = new Rule();
            rule.setId(ruleId.trim());
            rule.setGrammarId(grammarId);
            ruleManager.addRule(rule);
            
            return "redirect:/grammar/" + grammarId;
        } catch (DatabaseException e) {
            throw new GenericException(rb.getString("dbError"), e);
        } catch (RuleExistsException e) {
            throw new GenericException(rb.getString("ruleExists"), e);
        } finally {
            try {baseXClient.close();} catch (Exception e) {LOGGER.log(Level.ERROR, e);}
        }
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
    public String ruleEdit(ModelMap model, @PathVariable Long grammarId, @PathVariable String ruleId) {
        try {
            baseXClient = Utils.connectToBaseX();
            GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
            grammarManager.setBaseXClient(baseXClient);
            RuleManagerBaseXImpl ruleManager = new RuleManagerBaseXImpl();
            ruleManager.setBaseXClient(baseXClient);
            
            GrammarMeta gm = grammarManager.findGrammarMeta(grammarId);
            if (gm == null) {
                throw new NotFoundException();
            }
            Rule rule = ruleManager.findRuleById(ruleId, gm);
            if (rule == null) {
                throw new NotFoundException();
            }
            List<Rule> rules = ruleManager.findAllRules(gm);

            model.addAttribute("gm", gm);
            model.addAttribute("rule", rule);
            model.addAttribute("rules", rules);
            
            return "ruleEdit";
        } catch (DatabaseException e) {
            throw new GenericException(rb.getString("dbError"), e);
        } finally {
            try {baseXClient.close();} catch (Exception e) {LOGGER.log(Level.ERROR, e);}
        }
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
        if ("".equals(ruleId)) {
            throw new GenericException(rb.getString("emptyRuleName"));
        }
        Rule rule = new Rule();
        rule.setId(ruleId.trim());
        rule.setGrammarId(grammarId);

        try {
            baseXClient = Utils.connectToBaseX();
            RuleManagerBaseXImpl ruleManager = new RuleManagerBaseXImpl();
            ruleManager.setBaseXClient(baseXClient);

            ruleManager.deleteRule(rule);
            
            return "redirect:/grammar/" + grammarId;
        } catch (DatabaseException e) {
            throw new GenericException(rb.getString("dbError"), e);
        } finally {
            try {baseXClient.close();} catch (Exception e) {LOGGER.log(Level.ERROR, e);}
        }
    }

    /**
     * Installs database and redirects to grammar page.
     *
     * @param request Request with connection data.
     * @return Redirects to the grammar page view.
     */
    @RequestMapping(value = "/doInstall", method = RequestMethod.POST)
    public String doInstall(@RequestParam("inputHost") String host,
                            @RequestParam("inputUser") String user,
                            @RequestParam("inputPass") String pass,
                            @RequestParam("inputName") String name,
                            @RequestParam("inputPort") String port) {
        try {
            // If DB is already seted as installed - do not process
            if (Config.dbInstalled()) {
                throw new NotFoundException();
            }

            Config.setValue(Config.C_DB_USER, user);
            Config.setValue(Config.C_DB_HOST, host);
            Config.setValue(Config.C_DB_PASS, pass);
            Config.setValue(Config.C_DB_NAME, name);
            Config.setValue(Config.C_DB_PORT, port);

            Utils.installDB();
            Config.setValue(Config.C_DB_INST, Boolean.TRUE.toString());
            
            return "redirect:/";   
        } catch (ConfigException e) {
            throw new GenericException(rb.getString("configError"), e);
        } catch (DatabaseException e) {
            throw new GenericException(rb.getString("dbError"), e);
        } 
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
            if (Config.dbInstalled()) {
                throw new NotFoundException();
            }
        } catch (ConfigException e) {
            throw new GenericException(rb.getString("configError"), e);
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
        
        try {
            baseXClient = Utils.connectToBaseX();
            GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
            grammarManager.setBaseXClient(baseXClient);

            Grammar grammar = grammarManager.findGrammar(grammarId);
            if (grammar == null) {
                throw new NotFoundException();
            }

            return grammar.getContent();

        } catch (DatabaseException e) {
            throw new GenericException(rb.getString("dbError"));
        } finally {
            try {baseXClient.close();} catch (Exception e) {LOGGER.log(Level.ERROR, e);}
        }
    }
}
