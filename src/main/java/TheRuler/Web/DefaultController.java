package TheRuler.Web;

import TheRuler.Common.Config;
import TheRuler.Model.DAOImpl;
import TheRuler.Model.Grammar;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DefaultController {
	

	/**
	 * Home page - grammar listing
	 * 
	 * @param model 
	 * @return The index view (FTL)
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
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
                
                DAOImpl dao = new DAOImpl();
                String result = dao.test();
                model.addAttribute("text", result);
               
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
