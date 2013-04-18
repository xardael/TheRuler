package TheRuler.Web;

import TheRuler.Common.BaseXClient;
import TheRuler.Common.Config;
import TheRuler.Common.Utils;
import TheRuler.Exceptions.GenericException;
import TheRuler.Exceptions.BadRequestException;
import TheRuler.Exceptions.NotFoundException;
import TheRuler.Model.*;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

/**
 * Controller for handling request for HTTP API.
 *
 * @author Peter Gren
 */
@Controller
public class RestController {
    /**
     * Displays grammar page - rule listing.
     *
     * @param model Empty ModelMap.
     * @param id ID of grammar.
     * @param request Request for accesing url params.
     * @return The grammar page view.
     */
    @RequestMapping(value = "/rest/grammar/{id}", method = RequestMethod.GET, produces = "application/srgs+xml;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getGrammar(ModelMap model, @PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        if (id == null) {
            throw new IllegalArgumentException();
        }

        String result = "";
        
        try {
            BaseXClient baseXClient = Utils.connectToBaseX();
            GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
            grammarManager.setBaseXClient(baseXClient);

            Grammar grammar = grammarManager.findGrammar(id);
            
            response.setStatus(202);
            //throw new BadRequestException();
            //return "";
            return "";
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Saves posted grammar and redirects to grammar page.
     *
     * @param grammar A grammar from HTML form.
     * @return Redirects to the grammar page view.
     */
    @RequestMapping(value = "/rest/update-grammar", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String updateGrammar(Grammar grammar) {

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
     * Saves posted rule.
     *
     * @param rule A rule. Format: <code><rule id="ruleId">content</rule></code>.
     * @return Redirects to the grammar page view.
     */
    @RequestMapping(value = "/rest/update-rule", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String updateRule(@RequestParam("grammarId") Long grammarId, @RequestParam("ruleId") String ruleId, @RequestParam("content") String content) {

        if (content == null || grammarId == null || ruleId == null) {
            throw new BadRequestException();
        }

        BaseXClient baseXClient = null;

        try {
            baseXClient = Utils.connectToBaseX();
            RuleManagerBaseXImpl ruleManager = new RuleManagerBaseXImpl();
            ruleManager.setBaseXClient(baseXClient);

            GrammarMeta gm = new GrammarMeta();
            gm.setId(grammarId);
            
            Rule rule = new Rule();
            rule.setGrammarId(grammarId);
            rule.setId(ruleId);
            rule.setContent(content);
            
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

        return "";
    }

    /**
     * Creates new grammar with posted name and redirects to grammar meta edit
     * page of created grammar.
     *
     * @param request HTTP Request with posted data - grammar name.
     * @return Redirects to the grammar meta edit view.
     */
    @RequestMapping(value = "/rest/create-grammar", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String createGrammar(HttpServletRequest request) {
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
    @RequestMapping(value = "/rest/delete-grammar/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteGrammar(@PathVariable String id) {

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
    @RequestMapping(value = "/rest/rule-add", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String createRule(HttpServletRequest request) {

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
    @RequestMapping(value = "/rest/grammar/{grammarId}/rule/{ruleId}")
    @ResponseStatus(HttpStatus.OK)
    public String getRule(ModelMap model, @PathVariable String grammarId, @PathVariable String ruleId, HttpServletRequest request) {

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
    @RequestMapping(value = "/rest/delete-rule/{grammarId}/{ruleId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String deleteRule(@PathVariable Long grammarId, @PathVariable String ruleId) {
        if (grammarId == null || grammarId == 0 || ruleId == null || ruleId.equals("")) {
            throw new IllegalArgumentException();
        }

        throw new IllegalArgumentException();
        
//        BaseXClient baseXClient = null;
//        GrammarMeta gm = new GrammarMeta();
//        gm.setId(grammarId);
//        Rule rule = new Rule();
//        rule.setId(ruleId);
//
//        try {
//            baseXClient = Utils.connectToBaseX();
//            RuleManagerBaseXImpl ruleManager = new RuleManagerBaseXImpl();
//            ruleManager.setBaseXClient(baseXClient);
//
//            //ruleManager.deleteRule(rule, gm);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (baseXClient != null) {
//                try {
//                    baseXClient.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
        
       // return "";
    }
    
    @ExceptionHandler (IOException.class)
    @ResponseStatus (HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleAllExceptions(Exception ex) {
        return "";
    }
}
