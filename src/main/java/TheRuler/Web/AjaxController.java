/*
 * Copyright (c) 2013, Peter Gren <peter@gren.sk>
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     - Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     - Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     - Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 * 
 */

package TheRuler.Web;

import java.io.File;
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
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
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
    public class User {

        private String name = null;
        private String education = null;
        // Getter and Setter are omitted for making the code short

        public String getEducation() {
            return education;
        }

        public void setEducation(String education) {
            this.education = education;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        
        
    }
  
    
    private List<User> userList = new ArrayList<User>();

    @RequestMapping(value="/AddUser.htm",method=RequestMethod.GET)
    public String showForm(){
        return "AddUser";
    }

    @RequestMapping(value="/AddUser.htm",method=RequestMethod.POST)
    public @ResponseBody void addUser(HttpServletResponse response) throws Exception {
        String returnText = "SuperMan";
//        if(!result.hasErrors()){
//            userList.add(user);
//            returnText = "User has been added to the list. Total number of users are " + userList.size();
//        }else{
//            returnText = "Sorry, an error has occur. User has not been added to list.";
//        }
        String helloAjax = "<b>Hello Ajax</b>" ; 
  
        response.setCharacterEncoding("UTF-8");  
        response.setContentType("text/html");  
        response.getWriter().write(helloAjax);  
    }

    @RequestMapping(value="/ShowUsers.htm")
    public String showUsers(ModelMap model){
        model.addAttribute("Users", userList);
        return "ShowUsers";
    }
    
    
    
    @RequestMapping(value="/availability", method = RequestMethod.GET)
    public @ResponseBody String getAvailability(@RequestParam String name, @RequestParam String education) {
        
        Map<String,Object> json = new HashMap<String, Object>();
        json.put("Name:", name);
        json.put("Education:", education);
        
        //new JSONObject(map);
        
        return json.toString();
    }
    
    @RequestMapping(value="/ajax/validation", method = RequestMethod.POST)
    public @ResponseBody String getAvailability(@RequestParam String xml) {
        try {
//            URL schemaFile = new URL("http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd");
//            Source xmlFile = new StreamSource(new File("web.xml"));
//            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//            Schema schema = schemaFactory.newSchema(schemaFile);
//            Validator validator = schema.newValidator();
//            try {
//            validator.validate(xmlFile);
//            System.out.println(xmlFile.getSystemId() + " is valid");
//            } catch (SAXException e) {
//            System.out.println(xmlFile.getSystemId() + " is NOT valid");
//            System.out.println("Reason: " + e.getLocalizedMessage());
//            }
            
        } catch (Exception ex) {
            Logger.getLogger(AjaxController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "a";
    }
    
    @RequestMapping(value="/ajaxik", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public @ResponseBody Map<String, Object>  getRegionSettings(@RequestBody Map<String, Object> obj) {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("message", "Hello " + obj.get("name") + "!");
        return json;
    }
    
    @RequestMapping(value = "/helloajax", method = RequestMethod.GET)
    public @ResponseBody void fetchFlowDowns(HttpServletResponse response) throws Exception {
		
		String helloAjax = "Hello Ajax";

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.getWriter().write(helloAjax);

	}
}
