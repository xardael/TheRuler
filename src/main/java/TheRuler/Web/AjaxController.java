package TheRuler.Web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @author pyty
 */
@Controller
public class AjaxController {
    
    @RequestMapping(value = "/validate", method = RequestMethod.GET)
    public @ResponseBody String validateForm(@RequestParam String name) {
        String result = "Validation result";
        return result;
    }
}
