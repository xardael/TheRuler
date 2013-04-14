package TheRuler.Model;

import TheRuler.Common.BaseXClient;
import TheRuler.Common.Config;
import TheRuler.Common.Utils;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Peter Gren
 */
public class RuleManagerBaseXImpl implements RuleManager {

    private BaseXClient baseXClient;

    /**
     *
     * @param baseXClient
     */
    public void setBaseXClient(BaseXClient baseXClient) {
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
    public void addRule(Rule rule, GrammarMeta grammarMeta) throws Exception {
        if (grammarMeta == null || rule == null) {
            throw new IllegalArgumentException();
        } else if (grammarMeta.getId() == null || rule.getId() == null || rule.getId() == "") {
            throw new IllegalArgumentException();
        }

        // Tu ma byt este kontrola ci ID uz neexistuje. Presnejsie mohla by byt.

        String insertNodeCommand = "insert node <rule id='" + rule.getId() + "'> "
                + (rule.getContent() == null ? "" : rule.getContent())
                + "</rule> "
                + "into doc('" + Config.getValue(Config.C_DB_NAME) + "/" + grammarMeta.getId() + ".xml')/grammar";

        String result = baseXClient.execute("xquery " + insertNodeCommand);
    }

    /**
     *
     * @param id
     * @param grammarMeta
     */
    public Rule findRuleById(String id, GrammarMeta grammarMeta) throws Exception {
        if (id == null || grammarMeta == null || grammarMeta.getId() == null || baseXClient == null) {
            throw new IllegalArgumentException();
        }

        BaseXClient.Query query = baseXClient.query("doc('" + Config.getValue(Config.C_DB_NAME) + "/" + grammarMeta.getId() + ".xml')//rule[@id='" + id + "']/child::*");
        String xml = query.execute();

        Rule rule = new Rule();
        rule.setId(id);
        rule.setContent(xml);

        return rule;
    }

    /**
     *
     * @param grammarMeta
     */
    public List<Rule> findAllRules(GrammarMeta grammarMeta) throws Exception {
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
     *
     * @param rule
     * @param grammarMeta
     */
    public void updateRule(Rule rule, GrammarMeta grammarMeta) throws Exception {
        if (grammarMeta == null || rule == null) {
            throw new IllegalArgumentException();
        } else if (grammarMeta.getId() == null || rule.getId() == null || rule.getId() == "") {
            throw new IllegalArgumentException();
        }

        // Tu ma byt este kontrola ci ID uz neexistuje. Presnejsie mohla by byt.

        String insertNodeCommand = "insert node <rule id='" + rule.getId() + "'> "
                + (rule.getContent() == null ? "" : rule.getContent())
                + "</rule> "
                + "into doc('" + Config.getValue(Config.C_DB_NAME) + "/" + grammarMeta.getId() + ".xml')/grammar";

        String result = baseXClient.execute("xquery " + insertNodeCommand);
    }

    /**
     *
     * @param rule
     * @param grammarMeta
     */
    public void deleteRule(Rule rule, GrammarMeta grammarMeta) throws Exception {
        if (grammarMeta == null || rule == null) {
            throw new IllegalArgumentException();
        } else if (grammarMeta.getId() == null || rule.getId() == null || rule.getId().equals("")) {
            throw new IllegalArgumentException();
        }

        String result = baseXClient.execute("xquery exists(doc('" + Config.getValue(Config.C_DB_NAME) + "/" + grammarMeta.getId() + ".xml')//rule[@id='" + rule.getId() + "'])");

        if (result.equals("false")) {
            throw new IllegalArgumentException();
        }

        baseXClient.execute("xquery delete node doc('" + Config.getValue(Config.C_DB_NAME) + "/" + grammarMeta.getId() + ".xml')//rule[@id='" + rule.getId() + "']");
    }
}