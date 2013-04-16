package TheRuler.Model;

import TheRuler.Common.BaseXClient;
import TheRuler.Common.Config;
import TheRuler.Common.Utils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.web.util.HtmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Database manager for grammars, which works with BaseX XML database.
 *
 * @author Peter Gren
 */
public class GrammarManagerBaseXImpl implements GrammarManager {

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
     * Persist grammar meta information as a new grammar with blank content
     * into BaseX database.
     *
     * @param grammarMeta Grammar meta.
     * @return GrammerMeta object with newly created ID.
     */
    public GrammarMeta createGrammar(GrammarMeta grammarMeta) throws IOException {
        if (grammarMeta == null) {
            throw new IllegalArgumentException();
        } else if (grammarMeta.getName() == null || "".equals(grammarMeta.getName())) {
            throw new IllegalArgumentException();
        }

        BaseXClient.Query query = baseXClient.query("max(//grammars/grammarRecord/string(@id))");
        String lastId = query.execute();
        Long newId;
        // Becaouse of first node insertion
        try {
            newId = Long.parseLong(lastId);
        } catch (NumberFormatException nfe) {
            newId = 0L;
        }
        newId++;

        String insertNodeCommand = "insert node "
                + "<grammarRecord id='" + newId + "'>"
                + "  <name>" + HtmlUtils.htmlEscape(grammarMeta.getName()) + "</name>"
                + "  <description>" + ((grammarMeta.getDescription() == null) ? "" : HtmlUtils.htmlEscape(grammarMeta.getDescription())) + "</description>"
                + "  <date>" + Utils.convertDateToGmtString(new Date()) + "</date>"
                + "</grammarRecord>"
                + "into //grammars";

        String result = baseXClient.execute("xquery " + insertNodeCommand);

        //String grammar = "<grammar xmlns='http://www.w3.org/2001/06/grammar' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:schemaLocation='http://www.w3.org/2001/06/grammar http://www.w3.org/TR/speech-grammar/grammar.xsd' xml:lang='en-US' version='1.0'></grammar>";
        String grammar = "<grammar></grammar>";

        InputStream bais = new ByteArrayInputStream(grammar.getBytes("UTF-8"));


        try {
            baseXClient.add(newId.toString() + ".xml", bais);
            System.out.println(baseXClient.info());
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }

        LOGGER.log(Level.INFO, "createGrammar - created grammar with id = " + newId);

        grammarMeta.setId(newId);
        return grammarMeta;
    }

    /**
     * Retrieves grammar meta information and grammar content from
     * database and returns it as a Grammar object.
     * 
     * @param id Grammar object for given ID.
     * @return All grammar information in Grammr object. If grammar meta with
     *         given ID does not exist, returns null.
     */
    public Grammar findGrammar(Long id) throws IOException, ParserConfigurationException, SAXException {
        if (id == null) {
            throw new IllegalArgumentException();
        }

        // Get grammar meta
        Grammar grammar = new Grammar();
        GrammarMeta gm = findGrammarMeta(id);
        if (gm == null) {
            return null;
        }

        grammar.setMeta(gm);

        // Get grammar content
        BaseXClient.Query query = baseXClient.query("for $doc in collection('" + Config.getValue(Config.C_DB_NAME) + "') "
                + "where matches(document-uri($doc), '" + id + ".xml') "
                + "return $doc");
        String xml = query.execute();

        grammar.setContent(xml);

        return grammar;
    }

    /**
     * Retrieves grammar meta information from
     * database and returns it wraped in a GrammarMeta object.
     *
     * @param id Grammr ID.
     * @return Grammr meta information in GrammarMeta object. If grammar meta with
     *         given ID does not exist, returns null.
     */
    public GrammarMeta findGrammarMeta(Long id) throws IOException, ParserConfigurationException, SAXException {
        if (id == null) {
            throw new IllegalArgumentException();
        }

        BaseXClient.Query query = baseXClient.query("for $grammarRecord in //grammars/grammarRecord "
                + "where $grammarRecord[@id='" + id + "'] "
                + "return $grammarRecord");
        String xml = query.execute();

        if ("".equals(xml)) {
            return null;
        }
        
        String grammarId = null;
        String name = null;
        String description = null;
        String dateString = null;
        GrammarMeta grammarMeta = new GrammarMeta();

        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xml));
        Document doc = db.parse(is);

        Element root = doc.getDocumentElement();
        grammarId = root.getAttribute("id");

        NodeList nameNodes = root.getElementsByTagName("name");
        Element line = (Element) nameNodes.item(0);
        name = line.getFirstChild().getNodeValue();

        NodeList descriptionNodes = root.getElementsByTagName("description");
        line = (Element) descriptionNodes.item(0);
        if (line.getFirstChild() != null) {
            description = line.getFirstChild().getNodeValue();
        }

        NodeList dateNodes = root.getElementsByTagName("date");
        line = (Element) dateNodes.item(0);
        dateString = line.getFirstChild().getNodeValue();

        grammarMeta.setId(Long.parseLong(grammarId));
        grammarMeta.setName(name);
        grammarMeta.setDescription(description);
        grammarMeta.setDate(dateString);


        return grammarMeta;
    }

    /**
     * Returns grammar meta for all grammars in database.
     * 
     * @return List of GrammarMetas.
     */
    public List<GrammarMeta> findAllGrammarMetas() throws IOException, ParserConfigurationException, SAXException, ParseException {
        BaseXClient.Query query = baseXClient.query("<grammars> "
                + "{for $grammarRecord in //grammars/grammarRecord "
                + "return $grammarRecord} "
                + "</grammars>");
        String xml = query.execute();

        List<GrammarMeta> grammarMetas = new ArrayList<GrammarMeta>();

        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xml));
        Document doc = db.parse(is);
        NodeList grammarRecords = doc.getElementsByTagName("grammarRecord");

        for (int i = 0; i < grammarRecords.getLength(); i++) {
            GrammarMeta gm = new GrammarMeta();
            Element element = (Element) grammarRecords.item(i);

            gm.setId(Long.parseLong(element.getAttribute("id")));

            NodeList nodes = element.getElementsByTagName("name");
            Element line = (Element) nodes.item(0);
            gm.setName(line.getFirstChild().getNodeValue());

            nodes = element.getElementsByTagName("description");
            line = (Element) nodes.item(0);
            if (line.getFirstChild() != null) {
                gm.setDescription(line.getFirstChild().getNodeValue());
            }

            nodes = element.getElementsByTagName("date");
            line = (Element) nodes.item(0);
            String dateString = line.getFirstChild().getNodeValue();
            gm.setDate(Utils.convertGmtStringToLocaleString(dateString));

            grammarMetas.add(gm);
        }


//            grammarMeta.setId(Long.parseLong(grammarId));
//            grammarMeta.setName(name);
//            grammarMeta.setDescription(description);
//            grammarMeta.setDate(new Date());

        LOGGER.log(Level.INFO, "findAllGrammars - grammars finded");

        return grammarMetas;
    }

    /**
     * Updates grammar meta information and grammar content in database
     * according to given Grammar object.
     *
     * @param grammar Grammar stated for update.
     */
    public void updateGrammar(Grammar grammar) throws IOException {
        updateGrammarMeta(grammar.getMeta());
        updateGrammarContent(grammar);
    }

    /**
     * Updates grammar meta information in database
     * according to given GrammarMeta object.
     * 
     * @param grammarMeta GrammarMeta stated for update.
     */
    public void updateGrammarMeta(TheRuler.Model.GrammarMeta grammarMeta) throws IOException {
        if (grammarMeta == null) {
            throw new IllegalArgumentException();
        } else if (grammarMeta.getId() == null || grammarMeta.getName() == null) {
            throw new IllegalArgumentException();
        }

        String result = baseXClient.execute("xquery exists(//grammars/grammarRecord[@id=" + grammarMeta.getId() + "])");

        if ("false".equals(result)) {
            throw new IllegalArgumentException();
        }

        String updateNodeCommand = "replace node //grammars/grammarRecord[@id=" + grammarMeta.getId() + "] with"
                + "<grammarRecord id='" + grammarMeta.getId() + "'>"
                + "  <name>" + HtmlUtils.htmlEscape(grammarMeta.getName()) + "</name>"
                + "  <description>" + HtmlUtils.htmlEscape(grammarMeta.getDescription()) + "</description>"
                + "  <date>" + grammarMeta.getDate() + "</date>"
                + "</grammarRecord>";

        baseXClient.execute("xquery " + updateNodeCommand);
    }

    /**
     * Updates grammar content in database according to given Grammar object.
     *
     * @param grammar Grammar stated for update.
     */
    public void updateGrammarContent(Grammar grammar) throws IOException {
        if (grammar == null) {
            throw new IllegalArgumentException();
        } else if (grammar.getId() == null) {
            throw new IllegalArgumentException();
        }

        Long id = grammar.getId();

        String result = baseXClient.execute("xquery exists(//grammars/grammarRecord[@id=" + id.toString() + "])");

        if (result.equals("false")) {
            throw new IllegalArgumentException();
        }

        InputStream bais = new ByteArrayInputStream(Config.GRAMMAR_ROOT_NAME.getBytes("UTF-8"));

        if (!"".equals(grammar.getContent())) {
            bais = new ByteArrayInputStream(grammar.getContent().getBytes("UTF-8"));
        }

        try {
            baseXClient.replace(id + ".xml", bais);
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }

    }

    /**
     * Delete grammr from database.
     *
     * @param grammarMeta GrammarMeta containg ID of grammar stated for deletion.
     */
    public void deletaGrammar(GrammarMeta grammarMeta) throws IOException {
        if (grammarMeta == null) {
            throw new IllegalArgumentException();
        } else if (grammarMeta.getId() == null) {
            throw new IllegalArgumentException();
        }

        String result = baseXClient.execute("xquery exists(//grammars/grammarRecord[@id=" + grammarMeta.getId() + "])");

        if (result.equals("false")) {
            throw new IllegalArgumentException();
        }

        baseXClient.execute("xquery delete node //grammars/grammarRecord[@id=" + grammarMeta.getId() + "]");

        try {
            baseXClient.execute("delete " + grammarMeta.getId() + ".xml");
            System.out.println(baseXClient.info());
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }
}