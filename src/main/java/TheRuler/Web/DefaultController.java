package TheRuler.Web;

import TheRuler.Common.BaseXClient;
import TheRuler.Common.Config;
import TheRuler.Common.Utils;
import TheRuler.Model.Grammar;
import TheRuler.Model.GrammarManagerBaseXImpl;
import TheRuler.Model.GrammarMeta;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
	public String index(ModelMap model) throws IOException{

                model.addAttribute("basePath", Config.BASE_PATH);
                BaseXClient baseXClient = Utils.connectToBaseX();
                
                try {
                    GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
                    grammarManager.setBaseXClient(baseXClient);
                    
                    List<GrammarMeta> grammarMetas = grammarManager.findAllGrammarMetas();
                    
                    model.addAttribute("grammarMetas", grammarMetas);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    baseXClient.close();
                }
                               
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
                    //GrammarMeta gm = grammarManager.findGrammarMeta(1L);
                    //GrammarMeta test = gm;
                    
                    GrammarMeta grammarMeta = new GrammarMeta();
                    grammarMeta.setId(2L);
                    grammarMeta.setName("super Test");
                    grammarMeta.setDescription("Lorem ipsum dolor sit amet");
                    grammarMeta.setDate(new Date());
                    
                    //grammarMeta = grammarManager.createGrammar(grammarMeta);
                    
                    List<GrammarMeta> grammarMetas = new ArrayList<GrammarMeta>();
                    grammarMetas = grammarManager.findAllGrammarMetas();
                    
                    model.addAttribute("grammarMetas", grammarMetas);
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
