package TheRuler.Model;

import TheRuler.Common.BaseXClient;
import TheRuler.Common.Config;
import TheRuler.Common.Utils;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * 
 * @author pyty
 */
public class RuleManagerBaseXImpl implements RuleManager {

        private BaseXClient baseXClient;
        
        /**
         * 
         * @param baseXClient 
         */
        public void setBaseXClient (BaseXClient baseXClient) {
            if (baseXClient == null) {
                throw new IllegalArgumentException();
            }
            
            this.baseXClient = baseXClient;
        }
    
	/**
	 * 
	 * @param rule
	 * @param grammarMeta
	 */
	public void addRule(Rule rule, GrammarMeta grammarMeta) throws Exception{
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param id
	 * @param grammarMeta
	 */
	public Rule findRuleById(String id, TheRuler.Model.GrammarMeta grammarMeta) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param grammarMeta
	 */
	public List<Rule> findAllRules(GrammarMeta grammarMeta) throws Exception {
            BaseXClient.Query query = baseXClient.query("<rules> " +
                                                        "{for $rule in doc('" + Config.DB_NAME + "/" + grammarMeta.getId() +  ".xml')//rule " +
                                                        "return $rule} " +
                                                        "</rules>");
            String xml = query.execute();
            
            List<Rule> rules = new ArrayList<Rule>();
            
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            Document doc = db.parse(is);
            NodeList grammarRecords = doc.getElementsByTagName("rule");

            for (int i = 0; i < grammarRecords.getLength(); i++) {
                Rule rule = new Rule();
                Element element = (Element) grammarRecords.item(i);

                rule.setId(element.getAttribute("id"));
                
                rule.setContent(Utils.serializeXml(element));
                
                rules.add(rule);
            }
                    

//            grammarMeta.setId(Long.parseLong(grammarId));
//            grammarMeta.setName(name);
//            grammarMeta.setDescription(description);
//            grammarMeta.setDate(new Date());
            
            return rules;
	}

	/**
	 * 
	 * @param id
	 * @param grammarMeta
	 */
	public List<Rule> findAllRulesById(String id, GrammarMeta grammarMeta) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param rule
	 * @param grammarMeta
	 */
	public void updateRule(Rule rule, GrammarMeta grammarMeta) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param rule
	 * @param grammarMeta
	 */
	public void deleteRule(Rule rule, GrammarMeta grammarMeta) throws Exception {
		throw new UnsupportedOperationException();
	}

}