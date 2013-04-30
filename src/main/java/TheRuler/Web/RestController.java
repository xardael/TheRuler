package TheRuler.Web;

import TheRuler.Common.BaseXClient;
import TheRuler.Common.Utils;
import TheRuler.Exceptions.BadRequestException;
import TheRuler.Exceptions.DatabaseException;
import TheRuler.Exceptions.GenericException;
import TheRuler.Exceptions.InternalErrorException;
import TheRuler.Exceptions.NotFoundException;
import TheRuler.Exceptions.RuleExistsException;
import TheRuler.Model.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
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

    private static final Logger LOGGER = Logger.getLogger(RestController.class);
    private BaseXClient baseXClient = null;

    /**
     * Displays grammar page - rule listing.
     *
     * @param model Empty ModelMap.
     * @param grammarId ID of grammar.
     * @param request Request for accesing url params.
     * @return The grammar page view.
     */
    @RequestMapping(value = "/rest/grammar", method = RequestMethod.GET, produces = "application/srgs+xml;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getGrammar(@RequestParam("grammarId") Long grammarId) {
        if (grammarId == null) {
            throw new BadRequestException();
        }

        try {
            baseXClient = Utils.connectToBaseX();
            GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
            grammarManager.setBaseXClient(baseXClient);

            Grammar grammar = grammarManager.findGrammar(grammarId);

            if (grammar == null) {
                throw new NotFoundException();
            }

            return Utils.fixSrgsHeader(grammar.getContent());
        } catch (DatabaseException e) {
            throw new BadRequestException();
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
    @RequestMapping(value = "/rest/update-grammar", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String updateGrammar(Grammar grammar) {

        if (grammar == null) {
            throw new IllegalArgumentException();
        }

        try {
            baseXClient = Utils.connectToBaseX();
            GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
            grammarManager.setBaseXClient(baseXClient);

            grammarManager.updateGrammar(grammar);

        } catch (DatabaseException e) {
            throw new InternalErrorException();
        } finally {
            try {baseXClient.close();} catch (Exception e) {LOGGER.log(Level.ERROR, e);}
        }

        return "redirect:/grammar/" + grammar.getId();
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

        try {
            baseXClient = Utils.connectToBaseX();
            RuleManagerBaseXImpl ruleManager = new RuleManagerBaseXImpl();
            ruleManager.setBaseXClient(baseXClient);

            Rule rule = new Rule();
            rule.setGrammarId(grammarId);
            rule.setId(ruleId);
            rule.setContent(content);
            rule.setGrammarId(grammarId);

            ruleManager.updateRule(rule);

            return "";
        } catch (DatabaseException e) {
            throw new InternalErrorException();
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
    @RequestMapping(value = "/rest/create-grammar", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Map<String, String> createGrammar(@RequestParam("grammarName") String grammarName) {
        if (grammarName == null || "".equals(grammarName)) {
            throw new BadRequestException();
        }

        HashMap<String, String> result = new HashMap<String, String>();
        GrammarMeta gm = new GrammarMeta();
        gm.setName(grammarName);

        try {
            baseXClient = Utils.connectToBaseX();
            GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
            grammarManager.setBaseXClient(baseXClient);

            gm = grammarManager.createGrammar(gm);
            result.put("grammarId", gm.getId().toString());

            return result;
        } catch (DatabaseException e) {
            throw new InternalErrorException();
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
    @RequestMapping(value = "/rest/delete-grammar/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteGrammar(@PathVariable Long id) {
        GrammarMeta gm = new GrammarMeta();
        gm.setId(id);

        try {
            baseXClient = Utils.connectToBaseX();
            GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
            grammarManager.setBaseXClient(baseXClient);

            grammarManager.deletaGrammar(gm);

            return "redirect:/";
        } catch (DatabaseException e) {
            throw new InternalErrorException();
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
    @RequestMapping(value = "/rest/create-rule", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String createRule(@RequestParam("ruleId") String ruleId, @RequestParam("ruleContent") String content, @RequestParam("grammarId") Long grammarId) {
        if (ruleId == null || "".equals(ruleId)) {
            throw new BadRequestException();
        }

        GrammarMeta gm = new GrammarMeta();
        gm.setId(grammarId);

        try {
            baseXClient = Utils.connectToBaseX();
            RuleManagerBaseXImpl ruleManager = new RuleManagerBaseXImpl();
            ruleManager.setBaseXClient(baseXClient);

            if (ruleManager.findRuleById(ruleId, gm) == null) {
                throw new BadRequestException();
            }

            Rule rule = new Rule();
            rule.setId(ruleId);
            rule.setContent(content);
            rule.setGrammarId(grammarId);

            ruleManager.addRule(rule);


        } catch (DatabaseException e) {
            throw new InternalErrorException();
        } catch (RuleExistsException e) {
            throw new BadRequestException();
        }finally {
            try {baseXClient.close();} catch (Exception e) {LOGGER.log(Level.ERROR, e);}
        }

        return "";
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
    @ResponseBody
    public String getRule(ModelMap model, @PathVariable Long grammarId, @PathVariable String ruleId) {
        try {
            GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
            grammarManager.setBaseXClient(baseXClient);
            RuleManagerBaseXImpl ruleManager = new RuleManagerBaseXImpl();
            ruleManager.setBaseXClient(baseXClient);

            GrammarMeta gm = grammarManager.findGrammarMeta(grammarId);
            Rule rule = ruleManager.findRuleById(ruleId, gm);
            if (rule == null) {
                throw new NotFoundException();
            }
            List<Rule> rules = ruleManager.findAllRules(gm);

            model.addAttribute("gm", gm);
            model.addAttribute("rule", rule);
            model.addAttribute("rules", rules);

            return rule.getContent();
        } catch (DatabaseException e) {
            throw new InternalErrorException();
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
    @RequestMapping(value = "/rest/delete-rule/{grammarId}/{ruleId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String deleteRule(@PathVariable Long grammarId, @PathVariable String ruleId) {
        GrammarMeta gm = new GrammarMeta();
        gm.setId(grammarId);
        Rule rule = new Rule();
        rule.setId(ruleId);
        rule.setGrammarId(grammarId);
        
        try {
            baseXClient = Utils.connectToBaseX();
            RuleManagerBaseXImpl ruleManager = new RuleManagerBaseXImpl();
            ruleManager.setBaseXClient(baseXClient);

            ruleManager.deleteRule(rule);

            return "";
        } catch (DatabaseException e) {
            throw new InternalErrorException();
        } finally {
            try {baseXClient.close();} catch (Exception e) {LOGGER.log(Level.ERROR, e);}
        }
    }


    @ExceptionHandler (value = {IOException.class, ParserConfigurationException.class, SAXException.class})
    @ResponseStatus (HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleAllExceptions(Exception e) {
        return "";
    }
}
