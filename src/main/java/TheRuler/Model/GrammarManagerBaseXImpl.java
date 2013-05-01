package TheRuler.Model;

import TheRuler.Common.BaseXClient;
import TheRuler.Common.Config;
import TheRuler.Common.Utils;
import TheRuler.Exceptions.ConfigException;
import TheRuler.Exceptions.DatabaseException;
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
import org.w3c.dom.DOMException;
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
    private static final Logger LOGGER = Logger.getLogger(GrammarManagerBaseXImpl.class);

    public GrammarManagerBaseXImpl() {
        //Logger.getRootLogger().setLevel(Level.DEBUG);
    }
    
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
    public GrammarMeta createGrammar(GrammarMeta grammarMeta) throws DatabaseException {
        if (grammarMeta == null) {
            throw new IllegalArgumentException();
        } else if (grammarMeta.getName() == null || "".equals(grammarMeta.getName())) {
            throw new IllegalArgumentException();
        }

        try {
            String lastIdQuery = "max(//grammars/grammarRecord/string(@id))";
            LOGGER.log(Level.DEBUG, "Executing query: " + lastIdQuery);
            BaseXClient.Query basexQuery = baseXClient.query(lastIdQuery);
            String lastId = basexQuery.execute();
            Long newId;
            // Because of first node insertion
            try {
                newId = Long.parseLong(lastId);
            } catch (NumberFormatException nfe) {
                newId = 0L;
            }
            newId++;
            
            String query = "xquery insert node "
                    + "<grammarRecord id='" + newId + "'>"
                    + "  <name>" + HtmlUtils.htmlEscape(grammarMeta.getName()) + "</name>"
                    + "  <description>" + ((grammarMeta.getDescription() == null) ? "" : HtmlUtils.htmlEscape(grammarMeta.getDescription().trim())) + "</description>"
                    + "  <date>" + Utils.convertDateToGmtString(new Date()) + "</date>"
                    + "</grammarRecord>"
                    + "into //grammars";
            
            LOGGER.log(Level.DEBUG, "Executing query: " + query);
            baseXClient.execute(query);

            InputStream bais = new ByteArrayInputStream(Config.GRAMMAR_ROOT_NAME.getBytes("UTF-8"));
                        
            // Create new XML file in database for created grammar.
            baseXClient.add(newId.toString() + ".xml", bais);
            
            LOGGER.log(Level.INFO, "createGrammar - created grammar with id = " + newId);
            
            grammarMeta.setId(newId);
            return grammarMeta;
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        } 
    }

    /**
     * Retrieves grammar meta information and grammar content from
     * database and returns it as a Grammar object.
     * 
     * @param id Grammar object for given ID.
     * @return All grammar information in Grammr object. If grammar meta with
     *         given ID does not exist, returns null.
     */
    public Grammar findGrammar(Long id) throws DatabaseException {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        
        try {
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
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        } catch (ConfigException e) {
            throw new DatabaseException(e);
        }
        
    }

    /**
     * Retrieves grammar meta information from
     * database and returns it wraped in a GrammarMeta object.
     *
     * @param id Grammr ID.
     * @return Grammr meta information in GrammarMeta object. If grammar meta with
     *         given ID does not exist, returns null.
     */
    public GrammarMeta findGrammarMeta(Long id) throws DatabaseException {
        if (id == null) {
            throw new IllegalArgumentException();
        }

        try {
            BaseXClient.Query query = baseXClient.query("for $grammarRecord in //grammars/grammarRecord "
                    + "where $grammarRecord[@id='" + id + "'] "
                    + "return $grammarRecord");
            String xml = query.execute();
            
            if ("".equals(xml)) {
                // Grammar with given ID does not exist.
                return null;
            }
            
            String description = null;
            GrammarMeta grammarMeta = new GrammarMeta();
            
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            Document doc = db.parse(is);
            
            Element root = doc.getDocumentElement();
            
            NodeList nameNodes = root.getElementsByTagName("name");
            Element line = (Element) nameNodes.item(0);
            String name = line.getFirstChild().getNodeValue();
            
            NodeList descriptionNodes = root.getElementsByTagName("description");
            line = (Element) descriptionNodes.item(0);
            if (line.getFirstChild() != null) {
                description = line.getFirstChild().getNodeValue();
            }
            
            NodeList dateNodes = root.getElementsByTagName("date");
            line = (Element) dateNodes.item(0);
            String dateString = line.getFirstChild().getNodeValue();
            
            grammarMeta.setId(id);
            grammarMeta.setName(name);
            grammarMeta.setDescription(description);
            grammarMeta.setDate(dateString);
            
            return grammarMeta;
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        } catch (ParserConfigurationException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        } catch (SAXException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        } catch (DOMException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        }
    }

    /**
     * Returns grammar meta for all grammars in database.
     * 
     * @return List of GrammarMetas.
     */
    public List<GrammarMeta> findAllGrammarMetas() throws DatabaseException {
        try {
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
            
            return grammarMetas;
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        } catch (ParserConfigurationException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        } catch (SAXException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        } catch (DOMException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        } catch (ParseException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        }
    }

    /**
     * Updates grammar meta information and grammar content in database
     * according to given Grammar object.
     *
     * @param grammar Grammar stated for update.
     */
    public void updateGrammar(Grammar grammar) throws DatabaseException {
        updateGrammarMeta(grammar.getMeta());
        updateGrammarContent(grammar);
    }

    /**
     * Updates grammar meta information in database
     * according to given GrammarMeta object.
     * 
     * @param grammarMeta GrammarMeta stated for update.
     */
    public void updateGrammarMeta(GrammarMeta grammarMeta) throws DatabaseException {
        if (grammarMeta == null) {
            throw new IllegalArgumentException();
        } else if (grammarMeta.getId() == null || grammarMeta.getName() == null) {
            throw new IllegalArgumentException();
        }

        try {
            GrammarMeta old = findGrammarMeta(grammarMeta.getId());
            if (old == null) {
                throw new IllegalArgumentException();
            }
            
            String query = "xquery replace node //grammars/grammarRecord[@id=" + grammarMeta.getId() + "] with"
                    + "<grammarRecord id='" + grammarMeta.getId() + "'>"
                    + "  <name>" + ((grammarMeta.getName() == null) ? old.getName() : HtmlUtils.htmlEscape(grammarMeta.getName())) + "</name>"
                    + "  <description>" + ((grammarMeta.getDescription() == null) ? old.getDescription() : HtmlUtils.htmlEscape(grammarMeta.getDescription().trim())) + "</description>"
                    + "  <date>" + ((grammarMeta.getDate()) == null ? old.getDate() : grammarMeta.getDate()) + "</date>"
                    + "</grammarRecord>";
            
            LOGGER.log(Level.DEBUG, "Executing query: " + query);
            baseXClient.execute(query);
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        }
    }

    /**
     * Updates grammar content in database according to given Grammar object.
     *
     * @param grammar Grammar stated for update.
     */
    private void updateGrammarContent(Grammar grammar) throws DatabaseException {
        if (grammar == null) {
            throw new IllegalArgumentException();
        } else if (grammar.getId() == null) {
            throw new IllegalArgumentException();
        }

        try {
            Long id = grammar.getId();
            GrammarMeta gm = new GrammarMeta();
            gm.setId(id);
            
            if (!grammarExists(gm)) {
                throw new IllegalArgumentException();
            }
            
            InputStream bais = new ByteArrayInputStream(Config.GRAMMAR_ROOT_NAME.getBytes("UTF-8"));
            
            if (!"".equals(grammar.getContent())) {
                bais = new ByteArrayInputStream(grammar.getContent().getBytes("UTF-8"));
            }
            
            baseXClient.replace(id + ".xml", bais);
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        } 
    }

    /**
     * Deletes grammr from database.
     *
     * @param grammarMeta GrammarMeta containg ID of grammar stated for deletion.
     */
    public void deletaGrammar(GrammarMeta grammarMeta) throws DatabaseException {
        if (grammarMeta == null) {
            throw new IllegalArgumentException();
        } else if (grammarMeta.getId() == null) {
            throw new IllegalArgumentException();
        }

        try {
            if (!grammarExists(grammarMeta)) {
                throw new IllegalArgumentException();
            }
            String query = "xquery delete node //grammars/grammarRecord[@id=" + grammarMeta.getId() + "]";
            LOGGER.log(Level.DEBUG, "Executing query: " + query);
            LOGGER.debug("Executing query: " + query);
            baseXClient.execute(query);
            
            query = "delete " + grammarMeta.getId() + ".xml";
            LOGGER.log(Level.DEBUG, "Executing query: " + query);
            baseXClient.execute(query);
            
            LOGGER.log(Level.INFO, "deleteGrammar - deleted grammar with id = " + grammarMeta.getId());
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        }
    }
    
    /**
     * Checks if grammar with given ID exists.
     * 
     * @param gm Grammar meta containg ID.
     * @return TRUE if grammar with given ID exists, FALSE otherwise.
     */
    private boolean grammarExists(GrammarMeta grammarMeta) throws DatabaseException {
        try {
            String result = baseXClient.execute("xquery exists(//grammars/grammarRecord[@id=" + grammarMeta.getId() + "])");
            return Boolean.parseBoolean(result);
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        } 
    }
}