package TheRuler.Model;

import TheRuler.Common.BaseXClient;
import TheRuler.Common.Config;
import TheRuler.Common.Utils;
import TheRuler.Exceptions.GenericException;
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
import org.apache.log4j.Priority;
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
    private static final Logger LOGGER = Logger.getLogger(GrammarManagerBaseXImpl.class.getName());
    
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
    public void addRule(Rule rule, GrammarMeta grammarMeta) throws IOException {
        if (grammarMeta == null || rule == null) {
            throw new IllegalArgumentException();
        } else if (grammarMeta.getId() == null || rule.getId() == null || rule.getId() == "") {
            throw new IllegalArgumentException();
        }

        if (ruleExists(rule.getId(), grammarMeta.getId())) {
            throw new GenericException("rule exists");
        }

        String insertNodeCommand = "insert node <rule id='" + rule.getId() + "'> "
                + (rule.getContent() == null ? "" : rule.getContent())
                + "</rule> "
                + "into doc('" + Config.getValue(Config.C_DB_NAME) + "/" + grammarMeta.getId() + ".xml')/grammar";

        String result = baseXClient.execute("xquery " + insertNodeCommand);
    }

    /**
     * Checks if rule with given ID exists in given grammar.
     * 
     * @param id Rule ID to check.
     * @param grammarId Grammar ID.
     * @return TRUE if rule exists, FALSE otherwise.
     */
    public Boolean ruleExists(String id, Long grammarId) throws IOException {
        if (id == null || grammarId == null || baseXClient == null) {
            throw new IllegalArgumentException();
        }

        String query = String.format("xquery exists(doc('%s/%s.xml')//rule[@id='%s'])", Config.getValue(Config.C_DB_NAME), grammarId, id);
        String result = baseXClient.execute(query);
        
        if ("true".equals(result)) {
            return true;
        } else {
            return false;
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
    public Rule findRuleById(String id, GrammarMeta grammarMeta) throws IOException {
        if (id == null || grammarMeta == null || grammarMeta.getId() == null || baseXClient == null) {
            throw new IllegalArgumentException();
        }

        String queryString = String.format("doc('%s/%s.xml')//rule[@id='%s'][1]", Config.getValue(Config.C_DB_NAME), grammarMeta.getId(), id);
        BaseXClient.Query query = baseXClient.query(queryString);
        LOGGER.log(Level.INFO, "Executing query: " + queryString);
        String xml = query.execute();
        
        if ("".equals(xml)) {
            return null;
        }

        Rule rule = new Rule();
        rule.setId(id);
        rule.setContent(xml);

        return rule;
    }

    /**
     * Returns list of all rules in given grammar.
     *
     * @param grammarMeta GrammarMeta with ID of grammar.
     * @return List of all rules in given grammar.
     */
    public List<Rule> findAllRules(GrammarMeta grammarMeta) throws IOException, ParserConfigurationException, SAXException, TransformerException {
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

            rules.add(rule);
        }

        return rules;
    }

    /**
     * Provides pattern match search directly in BaseX database.
     * 
     * @param id Part of ID to find.
     * @param grammarMeta GrammarMeta of grammar you want to search in.
     * @return List of rules which contain given string in ID.
     */
    public List<Rule> findAllRulesById(String id, GrammarMeta grammarMeta) throws IOException, ParserConfigurationException, SAXException, TransformerException {
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

            rule.setContent(Utils.serializeXml(element));

            rules.add(rule);
        }

        return rules;
    }

    /**
     * Update rule in BaseX database.
     *
     * @param rule Rule object with ID.
     * @param grammarMeta GrammarMeta object of grammar containing given rule with ID.
     */
    public void updateRule(Rule rule, GrammarMeta grammarMeta) throws IOException {
        if (grammarMeta == null || rule == null) {
            throw new IllegalArgumentException();
        } else if (grammarMeta.getId() == null || rule.getId() == null || rule.getId() == "") {
            throw new IllegalArgumentException();
        }

        if(!ruleExists(rule.getId(), grammarMeta.getId())) {
            throw new IllegalArgumentException();
        }
        
//        String updateNodeCommand = "replace node doc('%1$s/%2$s.xml')//rule[@id='%3$s'] with"
//                                 + "<rule id='%3$s'>%4$s</rule>";
        
        String updateNodeCommand = "replace node doc('%1$s/%2$s.xml')//rule[@id='%3$s'][1] with %4$s";
        
        updateNodeCommand = String.format(updateNodeCommand, Config.getValue(Config.C_DB_NAME), grammarMeta.getId(), rule.getId(), rule.getContent());

        LOGGER.log(Level.INFO, "Executing query: " + updateNodeCommand);
        String result = baseXClient.execute("xquery " + updateNodeCommand);
    }

    /**
     * Deletes given rule from BaseX database.
     *
     * @param rule Rule object with ID.
     * @param grammarMeta GrammarMeta object of grammar containing given rule with ID.
     */
    public void deleteRule(Rule rule, GrammarMeta grammarMeta) throws IOException {
        if (grammarMeta == null || rule == null) {
            throw new IllegalArgumentException();
        } else if (grammarMeta.getId() == null || rule.getId() == null || rule.getId().equals("")) {
            throw new IllegalArgumentException();
        }

        String result = baseXClient.execute("xquery exists(doc('" + Config.getValue(Config.C_DB_NAME) + "/" + grammarMeta.getId() + ".xml')//rule[@id='" + rule.getId() + "'])");

        if (result.equals("false")) {
            throw new IllegalArgumentException();
        }

        baseXClient.execute("xquery delete node doc('" + Config.getValue(Config.C_DB_NAME) + "/" + grammarMeta.getId() + ".xml')//rule[@id='" + rule.getId() + "'][1]");
    }
}