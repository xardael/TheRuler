package TheRuler.Model;

import TheRuler.Common.BaseXClient;
import TheRuler.Common.Config;
import TheRuler.Common.Utils;
import TheRuler.Exceptions.ConfigException;
import TheRuler.Exceptions.DatabaseException;
import TheRuler.Exceptions.RuleExistsException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Database manager for rules, which works with BaseX XML database.
 *
 * @author Peter Gren
 */
public class RuleManagerBaseXImpl implements RuleManager {
    
    private BaseXClient baseXClient;
    private static final Logger LOGGER = Logger.getLogger(RuleManagerBaseXImpl.class);
    
    /**
     * Sets BaseX connection identificator for this manager.
     *
     * @param baseXClient BaseXClient instance, identifying valid opened
     * connection to the database.
     */
    public void setBaseXClient(BaseXClient baseXClient) {
        if (baseXClient == null) {
            throw new IllegalArgumentException();
        }

        this.baseXClient = baseXClient;
    }

    /**
     * Persist given rule as a new rule into BaseX database.
     *
     * @param grammarMeta Grammar meta.
     * @param rule Rule object.
     */
    public void addRule(Rule rule) throws DatabaseException, RuleExistsException {
        if (rule == null) {
            throw new IllegalArgumentException();
        } else if (rule.getId() == null || "".equals(rule.getId()) || rule.getGrammarId() == null) {
            throw new IllegalArgumentException();
        }

        if (ruleExists(rule)) {
            throw new RuleExistsException(rule.getId());
        }
        
        try {
            String query = "xquery insert node <rule id='" + rule.getId() + "'> "
                    + (rule.getContent() == null ? "" : rule.getContent())
                    + "</rule> "
                    + "into doc('" + Config.getValue(Config.C_DB_NAME) + "/" + rule.getGrammarId() + ".xml')/grammar";
            
            LOGGER.log(Level.DEBUG, "Executing query: " + query);
            baseXClient.execute(query);
            LOGGER.log(Level.INFO, "addRule - added rule with ID = " + rule.getId());
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        } catch (ConfigException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Checks if rule with given ID exists in given grammar.
     * 
     * @param id Rule ID to check.
     * @param grammarId Grammar ID.
     * @return TRUE if rule exists, FALSE otherwise.
     */
    private Boolean ruleExists(Rule rule) throws DatabaseException {
        if (rule == null || rule.getGrammarId() == null || baseXClient == null) {
            throw new IllegalArgumentException();
        }

        try {
            String query = String.format("xquery exists(doc('%s/%s.xml')//rule[@id='%s'])", Config.getValue(Config.C_DB_NAME), rule.getGrammarId(), rule.getId());
            String result = baseXClient.execute(query);
            
            if ("true".equals(result)) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        } catch (ConfigException e) {
            throw new DatabaseException(e);
        }
    }
    
    /**
     * Retrieves rule with given ID in given grammar from
     * database and returns it as a Rule object.
     * 
     * @param id Rule ID.
     * @param grammarMeta GrammarMeta object with ID.
     * @return Selected rule. If rule with given ID does not exist returns null.
     */
    public Rule findRuleById(String id, GrammarMeta grammarMeta) throws DatabaseException {
        if (id == null || grammarMeta == null || grammarMeta.getId() == null || baseXClient == null) {
            throw new IllegalArgumentException();
        }

        try {
            String queryString = String.format("doc('%s/%s.xml')//rule[@id='%s'][1]", Config.getValue(Config.C_DB_NAME), grammarMeta.getId(), id);
            BaseXClient.Query query = baseXClient.query(queryString);
            LOGGER.log(Level.DEBUG, "Executing query: " + queryString);
            
            String xml = query.execute();
            
            if ("".equals(xml)) {
                return null;
            }
            
            Rule rule = new Rule();
            rule.setId(id);
            rule.setGrammarId(grammarMeta.getId());
            rule.setContent(xml);
            
            return rule;
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        } catch (ConfigException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Returns list of all rules in given grammar.
     *
     * @param grammarMeta GrammarMeta with ID of grammar.
     * @return List of all rules in given grammar.
     */
    public List<Rule> findAllRules(GrammarMeta grammarMeta) throws DatabaseException {
        if (grammarMeta == null) {
            throw new IllegalArgumentException();
        }
        
        try {
            BaseXClient.Query query = baseXClient.query("<rules> "
                    + "{for $rule in doc('" + Config.getValue(Config.C_DB_NAME) + "/" + grammarMeta.getId() + ".xml')//rule "
                    + "return $rule} "
                    + "</rules>");
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
                rule.setGrammarId(grammarMeta.getId());
                
                rules.add(rule);
            }
            
            return rules;
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        } catch (ParserConfigurationException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        } catch (SAXException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        } catch (TransformerException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        } catch (ConfigException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Provides pattern match search directly in BaseX database.
     * 
     * @param id Part of ID to find.
     * @param grammarMeta GrammarMeta of grammar you want to search in.
     * @return List of rules which contain given string in ID.
     */
    public List<Rule> findAllRulesById(String id, GrammarMeta grammarMeta) throws DatabaseException {
        try {
            BaseXClient.Query query = baseXClient.query("<rules> "
                    + "{for $rule in doc('" + Config.getValue(Config.C_DB_NAME) + "/" + grammarMeta.getId() + ".xml')//rule[contains( "
                    + "translate(@id,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz') "
                    + ", '" + id + "')] "
                    + "return $rule} "
                    + "</rules>");
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
                rule.setGrammarId(grammarMeta.getId());
                rule.setContent(Utils.serializeXml(element));
                rules.add(rule);
            }
            
            return rules;
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        } catch (ParserConfigurationException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        } catch (SAXException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        } catch (TransformerException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        } catch (ConfigException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Update rule in BaseX database.
     *
     * @param rule Rule object with ID.
     * @param grammarMeta GrammarMeta object of grammar containing given rule with ID.
     */
    public void updateRule(Rule rule) throws DatabaseException {
        if (rule == null) {
            throw new IllegalArgumentException();
        } else if (rule.getId() == null || "".equals(rule.getId()) || rule.getGrammarId() == null) {
            throw new IllegalArgumentException();
        }

        if(!ruleExists(rule)) {
            throw new IllegalArgumentException();
        }
        
        try {
            String query = "xquery replace node doc('%1$s/%2$s.xml')//rule[@id='%3$s'][1] with %4$s";
            query = String.format(query, Config.getValue(Config.C_DB_NAME), rule.getGrammarId(), rule.getId(), rule.getContent());
            LOGGER.log(Level.DEBUG, "Executing query: " + query);
            baseXClient.execute(query);
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        }  catch (ConfigException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Deletes given rule from BaseX database.
     *
     * @param rule Rule object with ID.
     * @param grammarMeta GrammarMeta object of grammar containing given rule with ID.
     */
    public void deleteRule(Rule rule) throws DatabaseException {
        if (rule == null) {
            throw new IllegalArgumentException();
        } else if (rule.getId() == null || "".equals(rule.getId()) || rule.getGrammarId() == null) {
            throw new IllegalArgumentException();
        }

        if(!ruleExists(rule)) {
            throw new IllegalArgumentException();
        }

        try {
            String query = String.format("xquery delete node doc('%s/%s.xml')//rule[@id='%s'][1]", Config.getValue(Config.C_DB_NAME), rule.getGrammarId(), rule.getId());
            LOGGER.log(Level.DEBUG, "Executing query: " + query);
            baseXClient.execute(query);
            LOGGER.log(Level.INFO, "deleteRule - deleted rule with id = '" + rule.getId() + "' in grammar " + rule.getGrammarId());
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        } catch (ConfigException e) {
            throw new DatabaseException(e);
        }
    }
}