package TheRuler.Model;

import TheRuler.Common.BaseXClient;
import TheRuler.Common.Config;
import TheRuler.Common.Utils;
import TheRuler.Exceptions.DatabaseException;
import java.io.IOException;
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
            String lastIdQuery = "max(//grammars/grammarRecord/grammarMeta/string(@id))";
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
                    + "<grammarRecord>"
                    + "  <grammarMeta id='" + newId + "'>"
                    + "    <name>" + HtmlUtils.htmlEscape(grammarMeta.getName()) + "</name>"
                    + "    <description>" + ((grammarMeta.getDescription() == null) ? "" : HtmlUtils.htmlEscape(grammarMeta.getDescription().trim())) + "</description>"
                    + "    <date>" + Utils.convertDateToGmtString(new Date()) + "</date>"
                    + "  </grammarMeta>"
                    + "  " + Config.GRAMMAR_ROOT_NAME
                    + "</grammarRecord>"
                    + "into //grammars";
            
            LOGGER.log(Level.DEBUG, "Executing query: " + query);
            baseXClient.execute(query);

//            String grammarWrapper = String.format("<gwrap id='%s'>%s</gwrap>", newId, Config.GRAMMAR_ROOT_NAME);
//            InputStream bais = new ByteArrayInputStream(grammarWrapper.getBytes("UTF-8"));
//                        
//            // Create new XML file in database for created grammar.
//            baseXClient.add(newId.toString() + ".xml", bais);
            
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

            String query = String.format("//grammars/grammarRecord[grammarMeta/@id=%s]/grammar", id);
            
            // Get grammar content
            BaseXClient.Query basexQuery = baseXClient.query(query);
            String xml = basexQuery.execute();
            
            grammar.setContent(xml);
            
            return grammar;
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, e);
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
            String query = String.format("//grammars/grammarRecord/grammarMeta[@id=%s]", id);
            BaseXClient.Query basexQuery = baseXClient.query(query);
            String xml = basexQuery.execute();
            
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
                    + "return $grammarRecord/grammarMeta} "
                    + "</grammars>");
            String xml = query.execute();
            
            List<GrammarMeta> grammarMetas = new ArrayList<GrammarMeta>();
            
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            Document doc = db.parse(is);
            NodeList grammarRecords = doc.getElementsByTagName("grammarMeta");
            
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
            
            String query = "xquery replace node //grammars/grammarRecord/grammarMeta[@id=" + grammarMeta.getId() + "] with"
                    + "<grammarMeta id='" + grammarMeta.getId() + "'>"
                    + "  <name>" + ((grammarMeta.getName() == null) ? old.getName() : HtmlUtils.htmlEscape(grammarMeta.getName())) + "</name>"
                    + "  <description>" + ((grammarMeta.getDescription() == null) ? (old.getDescription() == null ? "" : old.getDescription()) : HtmlUtils.htmlEscape(grammarMeta.getDescription().trim())) + "</description>"
                    + "  <date>" + ((grammarMeta.getDate()) == null ? old.getDate() : grammarMeta.getDate()) + "</date>"
                    + "</grammarMeta>";
            
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
            
            String content = Config.GRAMMAR_ROOT_NAME;
            
            if (!"".equals(grammar.getContent())) {
                content = grammar.getContent();
            }
            
            //baseXClient.replace(id + ".xml", bais);

            String query = "xquery replace node //grammars/grammarRecord[grammarMeta/@id=%s]/grammar with %s";
            query = String.format(query, grammar.getId(), content);
            LOGGER.log(Level.DEBUG, "Executing query: " + query);
            baseXClient.execute(query);
            
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
            String query = "xquery delete node //grammars/grammarRecord[grammarMeta/@id=" + grammarMeta.getId() + "]";
            LOGGER.log(Level.DEBUG, "Executing query: " + query);
            LOGGER.debug("Executing query: " + query);
            baseXClient.execute(query);
            LOGGER.log(Level.INFO, "deleteGrammar - deleted grammar with id = " + grammarMeta.getId());
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        }
    }
    
    /**
     * Finds grammar by name.
     * 
     * @param grammarName The grammar name.
     * @return Grammar with given name.
     */
    public Grammar findGrammarByName(String grammarName) throws DatabaseException {
        if (grammarName == null || "".equals(grammarName)) {
            throw new IllegalArgumentException();
        }
        
        try {
            if (!grammarExistsByName(grammarName)) {
                return null;
            }
            String idString = baseXClient.execute("xquery string(//grammars/grammarRecord/grammarMeta[name/text()='" + grammarName + "'][1]/@id)");
            Long id = Long.parseLong(idString);
            return findGrammar(id);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
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
            String result = baseXClient.execute("xquery exists(//grammars/grammarRecord[grammarMeta/@id=" + grammarMeta.getId() + "])");
            return Boolean.parseBoolean(result);
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        } 
    }
    
    /**
     * Checks if grammar with given name exists.
     * 
     * @param grammarName Grammar name.
     * @return TRUE if grammar with given ID exists, FALSE otherwise.
     */
    public boolean grammarExistsByName(String grammarName) throws DatabaseException {
        if (grammarName == null || "".equals(grammarName)) {
            throw new IllegalArgumentException();
        }
        
        try {
            String result = baseXClient.execute("xquery exists(//grammars/grammarRecord/grammarMeta/name[text()='" + grammarName + "'])");
            if (Boolean.parseBoolean(result)) {
                return true;
            }
            return false;
        } catch (NumberFormatException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        } 
    }
}