package TheRuler.Web;

import TheRuler.Common.BaseXClient;
import TheRuler.Common.Config;
import TheRuler.Common.Utils;
import TheRuler.Exceptions.ResourceNotFoundException;
import TheRuler.Model.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
	@RequestMapping(value = "/grammar/{id}", method = RequestMethod.GET)
	public String grammar(ModelMap model, @PathVariable String id, HttpServletRequest request) {
            
            if (id == null || id.equals("")) {
                throw new IllegalArgumentException();
            }
            
            boolean search = false;
            
            if (request.getParameter("search") != null) {
                search = true;
                if (request.getParameter("name").equals("")) {
                    throw new IllegalArgumentException();
                }        
            }

            try {
                BaseXClient baseXClient = Utils.connectToBaseX();
                GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
                grammarManager.setBaseXClient(baseXClient);

                GrammarMeta gm = grammarManager.findGrammarMeta(Long.parseLong(id));

                model.addAttribute("gm", gm);
                
                RuleManagerBaseXImpl ruleManager = new  RuleManagerBaseXImpl();
                ruleManager.setBaseXClient(baseXClient);
                
                List<Rule> rules;
                
                if (search) {
                    rules = ruleManager.findAllRulesById(request.getParameter("name").toLowerCase(), gm);
                } else {
                    rules = ruleManager.findAllRules(gm);
                }
                
                model.addAttribute("rules", rules);
                model.addAttribute("search", search);
                model.addAttribute("searchString", request.getParameter("name"));
                
            } catch (Exception e) {
                e.printStackTrace();
            }
                
            return "grammar";
	}
        
        /**
	 * Grammar edit
	 * 
	 * @param model 
	 * @return The index view (FTL)
	 */
	@RequestMapping(value = "/edit-grammar/{id}")
	public String editGrammar(ModelMap model, @PathVariable String id) {
            
            if (id.equals("") || id == null) {
                throw new IllegalArgumentException();
            }

            try {
                BaseXClient baseXClient = Utils.connectToBaseX();
                GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
                grammarManager.setBaseXClient(baseXClient);

                GrammarMeta gm = grammarManager.findGrammarMeta(Long.parseLong(id));

                Grammar grammar = grammarManager.findGrammar(Long.parseLong(id));
                
                model.addAttribute("gm", gm);
                model.addAttribute("grammar", grammar);
            } catch (Exception e) {
                e.printStackTrace();
            }
                
            return "grammarMetaEdit";
	}

        @RequestMapping(value= "/save-grammar", method = RequestMethod.POST)
        public String saveGrammar(@ModelAttribute("user") GrammarMeta gm) {
            
            if (gm == null) {
                throw new IllegalArgumentException();
            }

            BaseXClient baseXClient = null;
            
            try {
                baseXClient = Utils.connectToBaseX();
                GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
                grammarManager.setBaseXClient(baseXClient);
            
                grammarManager.updateGrammarMeta(gm);
            
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
             
             
            //model.addAttribute("cv", initedCvDocument.getCv());
            //return "redirect:/grammar/" + gm.getId();
            return "redirect:/edit-grammar/" + gm.getId();
        }
        
        @RequestMapping(value= "/save-grammar-content", method = RequestMethod.POST)
        public String saveGrammarContent(Grammar grammar) {
            
            if (grammar == null) {
                throw new IllegalArgumentException();
            }
//            } else if (grammar.getMeta() == null) {
//                throw new NullPointerException();
//            } else if (grammar.getMeta().getId() == null) {
//                throw new NullPointerException();
//            }

            BaseXClient baseXClient = null;
            
            try {
                baseXClient = Utils.connectToBaseX();
                GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
                grammarManager.setBaseXClient(baseXClient);
            
                grammarManager.updateGrammarContent(grammar);
            
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
             
             
            //model.addAttribute("cv", initedCvDocument.getCv());
            //return "redirect:/grammar/" + gm.getId();
            return "redirect:/grammar/" + grammar.getMeta().getId();
        }
        
        @RequestMapping(value= "/create-grammar", method = RequestMethod.POST)
        public String createGrammar(ModelMap model, HttpServletRequest request) {
            
            if (request.getParameter("name").equals("")) {
                throw new IllegalArgumentException();
            }

            BaseXClient baseXClient = null;
            GrammarMeta gm = new GrammarMeta();
            gm.setName(request.getParameter("name"));
            
            Date d = new Date();
            
            
            
            gm.setDate((new Date()).toString());
            
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
             
             
            //model.addAttribute("cv", initedCvDocument.getCv());
            //return "redirect:/grammar/" + gm.getId();
            return "redirect:/grammar/" + gm.getId();
        }
        
        @RequestMapping(value = "/delete-grammar/{id}")
	public String deleteGrammar(ModelMap model, @PathVariable String id) {
            
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
             
             
            //model.addAttribute("cv", initedCvDocument.getCv());
            //return "redirect:/grammar/" + gm.getId();
            return "redirect:/";
        }
        
	/**
	 * Add a new grammar to db
	 * 
	 * @param user
	 * @return Redirect to /index page to display grammar list
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(ModelMap model, HttpServletRequest request) {

//            if (request.getParameter("name").equals("")) {
//                throw new IllegalArgumentException();
//            }
//
//            GrammarMeta gm = new GrammarMeta();
//            gm.setName(request.getParameter("name"));
//            
//            
//            //model.addAttribute("cv", initedCvDocument.getCv());
            return "redirect:index.html";
	}
        
        
        @RequestMapping(value= "/rule-search", method = RequestMethod.GET)
        public String ruleSearch(ModelMap model, HttpServletRequest request) {
            
            if (request.getParameter("name").equals("") || request.getParameter("grammarId").equals("")) {
                throw new IllegalArgumentException();
            }

            BaseXClient baseXClient = null;
            GrammarMeta gm = new GrammarMeta();
            gm.setId(Long.parseLong(request.getParameter("grammarId")));
            model.addAttribute("gm", gm);
            
            String searchText = request.getParameter("name");
            
            try {
                baseXClient = Utils.connectToBaseX();
                RuleManagerBaseXImpl ruleManager = new RuleManagerBaseXImpl();
                ruleManager.setBaseXClient(baseXClient);
            
                List<Rule> rules = ruleManager.findAllRulesById(searchText, gm);
                
                model.addAttribute("rules", rules);
                
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

            return "ruleSearch";
        }
        
        @RequestMapping(value= "/rule-add", method = RequestMethod.POST)
        public String ruleAdd(HttpServletRequest request) {
            
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
            
                List<Rule> rules = ruleManager.findAllRules(gm);
                Rule rule = new Rule();
                rule.setId(request.getParameter("ruleId"));
                        
                ruleManager.addRule(rule, gm);
                
                
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
        
        @RequestMapping(value = "/grammar/{grammarId}/rule/{ruleId}")
	public String ruleEdit(ModelMap model, @PathVariable String grammarId, @PathVariable String ruleId) {
            
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

                model.addAttribute("gm", gm);
                model.addAttribute("rule", rule);
                
            } catch (Exception e) {
                e.printStackTrace();
            }
                
            return "ruleEdit";
	}
        
        @RequestMapping(value = "/delete-rule/{grammarId}/{ruleId}")
	public String deleteRule(ModelMap model, @PathVariable Long grammarId, @PathVariable String ruleId) {
            if (grammarId == null || grammarId == 0 || ruleId == null || ruleId.equals("")) {
                throw new IllegalArgumentException();
            }

            BaseXClient baseXClient = null;
            GrammarMeta gm = new GrammarMeta();
            gm.setId(grammarId);
            Rule rule = new Rule();
            rule.setId(ruleId);
            
            try {
                baseXClient = Utils.connectToBaseX();
                RuleManagerBaseXImpl ruleManager = new RuleManagerBaseXImpl();
                ruleManager.setBaseXClient(baseXClient);
            
                ruleManager.deleteRule(rule, gm);
            
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
             
             
            //model.addAttribute("cv", initedCvDocument.getCv());
            //return "redirect:/grammar/" + gm.getId();
            return "redirect:/grammar/" + gm.getId();
        }

        @RequestMapping(value = "/doInstall", method = RequestMethod.POST)
        public String install(ModelMap model, HttpServletRequest request) {
//            if (request.getParameter("ruleId") == null || request.getParameter("ruleId").equals("") 
//             || request.getParameter("grammarId") == null || request.getParameter("grammarId").equals("")) {
//                throw new IllegalArgumentException();
//            }

            String host = request.getParameter("inputHost");
            String user = request.getParameter("inputUser");
            String pass = request.getParameter("inputPass");
            String name = request.getParameter("inputName");
            String port = request.getParameter("inputPort");
            
            // If DB is already seted as installed - do not process
            if (Config.getDbInstalled()) {
                //throw new ResourceNotFoundException();
            }
                        
            try {
                Utils.installDB();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Set DB asi installed into properties file
            Config.setDbInstalled(Boolean.TRUE);

            return "redirect:/";
        }
        
        @RequestMapping(value = "/install", method = RequestMethod.GET)
	public String install(ModelMap model) {
//            if (request.getParameter("ruleId") == null || request.getParameter("ruleId").equals("") 
//             || request.getParameter("grammarId") == null || request.getParameter("grammarId").equals("")) {
//                throw new IllegalArgumentException();
//            }

            return "install";
        }
}
