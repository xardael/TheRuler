package TheRuler.Web;

import TheRuler.Common.BaseXClient;
import TheRuler.Common.Utils;
import TheRuler.Exceptions.BadRequestException;
import TheRuler.Exceptions.DatabaseException;
import TheRuler.Exceptions.InternalErrorException;
import TheRuler.Exceptions.RuleExistsException;
import TheRuler.Model.*;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
     * Return raw SRGS grammar.
     *
     * @param grammarId ID of grammar.
     * @return Grammar with application/srgs+xml MIME type.
     */
    @RequestMapping(value = "/rest/grammar", method = RequestMethod.GET, produces = "application/srgs+xml;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getGrammar(@RequestParam String grammarId) {        
        if (grammarId == null) {
            throw new BadRequestException();
        }
        
        try {
            baseXClient = Utils.connectToBaseX();
            GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
            grammarManager.setBaseXClient(baseXClient);

            Grammar grammar = grammarManager.findGrammarByName(grammarId);

            if (grammar == null) {
                throw new BadRequestException();
            }

            return Utils.fixSrgsHeader(grammar.getContent());
        } catch (DatabaseException e) {
            throw new InternalErrorException();
        } finally {
            try {baseXClient.close();} catch (Exception e) {LOGGER.log(Level.ERROR, e);}
        }
    }

    /**
     * Creates new grammar with posted name and opitonal content.
     *
     * @param grammarId Name of created grammar.
     * @param grammarContent SRGS Grammar content (optional).
     * @return ID of created grammar in JSON variable grammarId.
     */
    @RequestMapping(value = "/rest/create-grammar", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Map<String, String> createGrammar(@RequestParam String grammarId) {
        if ("".equals(grammarId.trim())) {
            throw new BadRequestException();
        }

        try {
            baseXClient = Utils.connectToBaseX();
            HashMap<String, String> result = new HashMap<String, String>();
            GrammarMeta gm = new GrammarMeta();
            gm.setName(grammarId);
            GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
            grammarManager.setBaseXClient(baseXClient);

            if (grammarManager.grammarExistsByName(grammarId)) {
                throw new BadRequestException();
            }
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
     * @param grammarId ID of grammar.
     * @return HTTP status.
     */
    @RequestMapping(value = "/rest/delete-grammar", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String deleteGrammar(@RequestParam String grammarId) {
        try {
            baseXClient = Utils.connectToBaseX();
            GrammarMeta gm = new GrammarMeta();
            GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
            grammarManager.setBaseXClient(baseXClient);
            Grammar grammar = grammarManager.findGrammarByName(grammarId);
            if (grammar == null) {
                throw new BadRequestException();
            }
            gm.setId(grammar.getId());
            grammarManager.deletaGrammar(gm);
            return "";
        } catch (DatabaseException e) {
            throw new InternalErrorException();
        } catch (IllegalArgumentException e) {
            // Grammar does not exist.
            throw new BadRequestException();
        } finally {
            try {baseXClient.close();} catch (Exception e) {LOGGER.log(Level.ERROR, e);}
        }
    }

    /**
     * Adds new rule with posted name into grammar with given ID.
     *
     * @param ruleId Rule ID.
     * @param ruleContent Rule content.
     * @param grammarId ID of grammar to insert the rule.
     * @return HTTP status.
     */
    @RequestMapping(value = "/rest/create-rule", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public String createRule(@RequestParam String ruleId, @RequestParam String ruleContent, @RequestParam String grammarId) {
        try {
            baseXClient = Utils.connectToBaseX();
            RuleManagerBaseXImpl ruleManager = new RuleManagerBaseXImpl();
            ruleManager.setBaseXClient(baseXClient);
            
            Rule rule = new Rule();
            rule.setId(ruleId);
            if (!"".equals(ruleContent)) {
                rule.setContent(ruleContent);
            }
            
            GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
            grammarManager.setBaseXClient(baseXClient);
            Grammar grammar = grammarManager.findGrammarByName(grammarId);
            if (grammar == null) {
                throw new BadRequestException();
            }
            
            rule.setGrammarId(grammar.getId());

            ruleManager.addRule(rule);
        } catch (DatabaseException e) {
            throw new InternalErrorException();
        } catch (RuleExistsException e) {
            throw new BadRequestException();
        }catch (IllegalArgumentException e) {
            throw new BadRequestException();
        } finally {
            try {baseXClient.close();} catch (Exception e) {LOGGER.log(Level.ERROR, e);}
        }

        return "";
    }

    /**
     * Retrieves rule from grammar.
     *
     * @param grammarId ID of grammar containg rule.
     * @param ruleId ID of rule.
     * @return Rule with MIME type application/xml.
     */
    @RequestMapping(value = "/rest/rule", produces = "application/xml;charset=UTF-8")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getRule(@RequestParam String grammarId, @RequestParam String ruleId) {
        try {
            baseXClient = Utils.connectToBaseX();
            GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
            grammarManager.setBaseXClient(baseXClient);
            RuleManagerBaseXImpl ruleManager = new RuleManagerBaseXImpl();
            ruleManager.setBaseXClient(baseXClient);
            Grammar grammar = grammarManager.findGrammarByName(grammarId);
            if (grammar == null) {
                throw new BadRequestException();
            }
            GrammarMeta gm = grammarManager.findGrammarMeta(grammar.getId());
            if (gm == null) {
                throw new BadRequestException();
            }
            Rule rule = ruleManager.findRuleById(ruleId, gm);
            if (rule == null) {
                throw new BadRequestException();
            }
           
            return rule.getContent();
        } catch (DatabaseException e) {
            throw new InternalErrorException();
        } finally {
            try {baseXClient.close();} catch (Exception e) {LOGGER.log(Level.ERROR, e);}
        }
    }

    /**
     * Deletes rule from grammar.
     *
     * @param grammarId ID of grammar containg rule.
     * @param ruleId ID of rule.
     * @return HTTP status.
     */
    @RequestMapping(value = "/rest/delete-rule", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String deleteRule(@RequestParam String grammarId, @RequestParam String ruleId) {
        try {
            baseXClient = Utils.connectToBaseX();
            Rule rule = new Rule();
            rule.setId(ruleId);
            GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
            grammarManager.setBaseXClient(baseXClient);
            Grammar grammar = grammarManager.findGrammarByName(grammarId);
            if (grammar == null) {
                throw new BadRequestException();
            }
            rule.setGrammarId(grammar.getId());
            RuleManagerBaseXImpl ruleManager = new RuleManagerBaseXImpl();
            ruleManager.setBaseXClient(baseXClient);
            ruleManager.deleteRule(rule);
            return "";
        } catch (DatabaseException e) {
            throw new InternalErrorException();
        } catch (IllegalArgumentException e) {
            // Rule does not exist.
            throw new BadRequestException();
        } finally {
            try {baseXClient.close();} catch (Exception e) {LOGGER.log(Level.ERROR, e);}
        }
    }
}
