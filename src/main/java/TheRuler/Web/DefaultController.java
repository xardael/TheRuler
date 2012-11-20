package TheRuler.Web;

import TheRuler.Common.BaseXClient;
import TheRuler.Common.Config;
import TheRuler.Common.Utils;
import TheRuler.Model.Grammar;
import TheRuler.Model.GrammarManagerBaseXImpl;
import TheRuler.Model.GrammarMeta;
import java.util.Date;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * @author pyty
 */
@Controller
public class DefaultController {
	

	/**
	 * Home page - grammar listing
	 * 
	 * @param model 
	 * @return The index view (FTL)
	 */
	@RequestMapping("/")
	public String index(ModelMap model) {

                model.addAttribute("basePath", Config.BASE_PATH);
                               
		return "index";
	}
        
        /**
	 * Grammar edit
	 * 
	 * @param model 
	 * @return The index view (FTL)
	 */
	@RequestMapping(value = "/grammar/aaaa", method = RequestMethod.GET)
	public String grammar(ModelMap model) {
                
                model.addAttribute("basePath", Config.BASE_PATH);
                model.addAttribute("text", "finally");
                
//                String result = Utils.test();
//                
                try {
                    BaseXClient baseXClient = Utils.connectToBaseX();
                    GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
                    grammarManager.setBaseXClient(baseXClient);
                    GrammarMeta gm = grammarManager.findGrammarMeta(1L);
                    GrammarMeta test = gm;
                    
                    model.addAttribute("gm", gm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
//                GrammarMeta gm = new GrammarMeta();
//                    gm.setId(1L);
//                    gm.setName("Name");
//                    gm.setDescription("description");
//                    gm.setDate(new Date());
//                model.addAttribute("gm", gm);
                
                
                
		return "grammar";
	}

	/**
	 * Add a new grammar to db
	 * 
	 * @param user
	 * @return Redirect to /index page to display grammar list
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(@ModelAttribute("grammar") Grammar grammar) {

            
            return "redirect:index.html";
	}

}
