package TheRuler.Web;

import TheRuler.Common.BaseXClient;
import TheRuler.Common.Config;
import TheRuler.Common.Utils;
import TheRuler.Exceptions.GenericException;
import TheRuler.Exceptions.ResourceNotFoundException;
import TheRuler.Model.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

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
//            try {
//                Utils.t();
//            } catch (NullPointerException e) {
//                org.apache.log4j.Logger.getLogger(GrammarManagerBaseXImpl.class.getName()).error( "failed!", e );
//                throw new  NullPointerException("b;a");
//            }
            

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
                    //throw new IllegalArgumentException();
                    return "redirect:/grammar/" + id;
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
        public String doInstall(ModelMap model, HttpServletRequest request) {
//            if (request.getParameter("ruleId") == null || request.getParameter("ruleId").equals("") 
//             || request.getParameter("grammarId") == null || request.getParameter("grammarId").equals("")) {
//                throw new IllegalArgumentException();
//            }
            
            try {
                if (Boolean.parseBoolean(Config.getValue(Config.C_DB_INST))) {
                    //throw new ResourceNotFoundException();
                }
            } catch (IOException ioe) {

            }

            String host = request.getParameter("inputHost");
            String user = request.getParameter("inputUser");
            String pass = request.getParameter("inputPass");
            String name = request.getParameter("inputName");
            String port = request.getParameter("inputPort");
            try {
                // If DB is already seted as installed - do not process
                if (Boolean.parseBoolean(Config.getValue(Config.C_DB_INST))) {
                    throw new ResourceNotFoundException();
                }
                
                Config.setValue(Config.C_DB_HOST, host);
                Config.setValue(Config.C_DB_USER, user);
                Config.setValue(Config.C_DB_PASS, pass);
                Config.setValue(Config.C_DB_NAME, name);
                Config.setValue(Config.C_DB_PORT, port);
                
                
            } catch (IOException ex) {
                Logger.getLogger(DefaultController.class.getName()).log(Level.SEVERE, null, ex);
            }
                        
            try {
                Utils.installDB();
                Config.setValue(Config.C_DB_INST, Boolean.TRUE.toString());
            } catch (Exception e) {
                String s = e.getMessage();
            }
            try {
                // Set DB asi installed into properties file

                Config.setValue(Config.C_DB_INST, Boolean.TRUE.toString());
            } catch (IOException ex) {
                Logger.getLogger(DefaultController.class.getName()).log(Level.SEVERE, null, ex);
            }

            return "redirect:/";
        }
        
        @RequestMapping(value = "/install", method = RequestMethod.GET)
	public String install(ModelMap model) {
            try {
                if (Boolean.parseBoolean(Config.getValue(Config.C_DB_INST))) {
                    throw new ResourceNotFoundException();
                }
            } catch (IOException ioe) {

            }

            return "install";
        }
        
        @RequestMapping(value = "/export/{grammarId}", method = RequestMethod.GET, produces="text/plain;charset=UTF-8")
        @ResponseBody
        public String plaintext(HttpServletResponse response, @PathVariable Long grammarId) {
            response.setHeader("Content-Disposition", "attachment;filename=\"export.xml\"");
            if (grammarId == null || grammarId == 0) {
                throw new IllegalArgumentException();
            }
            
            BaseXClient baseXClient = null;
            try {
                baseXClient = Utils.connectToBaseX();
                GrammarManagerBaseXImpl grammarManager = new GrammarManagerBaseXImpl();
                grammarManager.setBaseXClient(baseXClient);

                Grammar grammar = grammarManager.findGrammar(grammarId);

                return grammar.getContent();
                
            } catch (Exception e) {
                return "ERROR";
            } finally {
                if (baseXClient != null) {
                    try {
                        baseXClient.close();
                    } catch (IOException e) {
                        return "ERROR";
                    }
                }
            }
        }
}
