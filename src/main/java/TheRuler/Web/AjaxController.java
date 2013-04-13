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
