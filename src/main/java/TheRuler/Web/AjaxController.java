package TheRuler.Web;

import TheRuler.Common.Utils;
import TheRuler.Model.GrammarMeta;
import TheRuler.Model.Rule;
import TheRuler.Model.RuleManagerBaseXImpl;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.bind.Validator;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

/**
 * 
 * @author pyty
 */
@Controller
public class AjaxController {
    
    @RequestMapping(value="/ajax/availability", method=RequestMethod.GET)
    public @ResponseBody GrammarMeta getAvailability(@RequestParam String name) {
        GrammarMeta gm = new GrammarMeta();
        if (name != null) {
            gm.setName(name);
        }
        return gm;
    }
    
    @RequestMapping(value="/ajax/findRules", method=RequestMethod.POST)
    public @ResponseBody List<String> findRules(@RequestParam Long grammarId, @RequestParam String searchText) {
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
    
    @RequestMapping(value="/ajax/validateXml", method=RequestMethod.POST)
    public @ResponseBody String validateXml(@RequestParam String content) {
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
