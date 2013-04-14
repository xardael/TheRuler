package TheRuler.Web;

import TheRuler.Common.Utils;
import TheRuler.Model.GrammarMeta;
import TheRuler.Model.Rule;
import TheRuler.Model.RuleManagerBaseXImpl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.SAXException;

/**
 * Controller for handling AJAX requests.
 *
 * @author Peter Gren
 */
@Controller
public class AjaxController {

    /**
     * Finds rules. Provides functionality for insert ruleref feature.
     *
     * @param grammarId ID of grammar.
     * @param searchText Search text.
     * @return JSON formated collection of strings.
     */
    @RequestMapping(value = "/ajax/findRules", method = RequestMethod.POST)
    public @ResponseBody
    List<String> findRules(@RequestParam Long grammarId, @RequestParam String searchText) {
        GrammarMeta gm = new GrammarMeta();
        if (grammarId != null) {
            gm.setId(grammarId);
        }
        List<Rule> rules = new ArrayList<Rule>();
        List<String> ids = new ArrayList<String>();
        if (searchText != null) {
            RuleManagerBaseXImpl ruleManager = new RuleManagerBaseXImpl();
            try {
                ruleManager.setBaseXClient(Utils.connectToBaseX());
                rules = ruleManager.findAllRulesById(searchText, gm);

                for (Rule rule : rules) {
                    ids.add(rule.getId());
                }
            } catch (Exception ex) {
                Logger.getLogger(AjaxController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return ids;
    }

    /**
     * Validates posted XML and return String AJAX response.
     *
     * @param content XML string to validate.
     * @return True if is content is valid, otherwise returns validation error.
     */
    @RequestMapping(value = "/ajax/validateXml", method = RequestMethod.POST)
    public @ResponseBody
    String validateXml(@RequestParam String content) {
        String result = "false";

        try {
            result = Utils.validate(content);
        } catch (SAXException ex) {
            Logger.getLogger(AjaxController.class.getName()).log(Level.SEVERE, null, ex);
            return "false";
        } catch (IOException ex) {
            Logger.getLogger(AjaxController.class.getName()).log(Level.SEVERE, null, ex);
            return "false";
        }

        return result;
    }
}
