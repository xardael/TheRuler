package TheRuler.Model;

import TheRuler.Common.BaseXClient;
import TheRuler.Common.Config;
import TheRuler.Common.Utils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * 
 * @author pyty
 */
public class GrammarManagerBaseXImpl implements GrammarManager {

        private BaseXClient baseXClient;
        private static final Logger LOGGER = Logger.getLogger(GrammarManagerBaseXImpl.class.getName());
        
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
	 * @param grammarMeta
	 */
	public GrammarMeta createGrammar(GrammarMeta grammarMeta) throws Exception {
            if (grammarMeta == null) {
                throw new IllegalArgumentException();
            } else if (grammarMeta.getName() == null || grammarMeta.getName() == "") {
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
            
            String insertNodeCommand = "insert node " +
                                       "<grammarRecord id='" + newId + "'>" +
                                       "  <name>" + grammarMeta.getName() + "</name>" +
                                       "  <description>" + ((grammarMeta.getDescription() == null) ? "" : grammarMeta.getDescription()) + "</description>" +
                                       "  <date>" + Utils.convertDateToGmtString(new Date()) + "</date>" +
                                       "</grammarRecord>" +
                                       "into //grammars";
            
            String result = baseXClient.execute("xquery " + insertNodeCommand);
            
            //String grammar = "<grammar xmlns='http://www.w3.org/2001/06/grammar' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:schemaLocation='http://www.w3.org/2001/06/grammar http://www.w3.org/TR/speech-grammar/grammar.xsd' xml:lang='en-US' version='1.0'></grammar>";
            String grammar = "<grammar><rule id='EmptyRule'></rule></grammar>";
            
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
	 * 
	 * @param id
	 */
	public Grammar findGrammar(Long id) throws Exception {
            if (id == null) {
                throw new IllegalArgumentException();
            }
            
            // Get grammar meta
            Grammar grammar = new Grammar();
            GrammarMeta gm = findGrammarMeta(id);
            
            grammar.setMeta(gm);
            
            // Get grammar content
            BaseXClient.Query query = baseXClient.query("for $doc in collection('" + Config.getDbName() + "') " +
                                                        "where matches(document-uri($doc), '" + id + ".xml') " +
                                                        "return $doc");
            String xml = query.execute();
            
            grammar.setContent(xml);
            
            return grammar;    
	}

	/**
	 * 
	 * @param id
	 */
	public GrammarMeta findGrammarMeta(Long id) throws Exception{
            if (id == null) {
                throw new IllegalArgumentException();
            }
            
            BaseXClient.Query query = baseXClient.query("for $grammarRecord in //grammars/grammarRecord " + 
                                                        "where $grammarRecord[@id='" + id + "'] " +
                                                        "return $grammarRecord");
            String xml = query.execute();
            
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
            grammarMeta.setDate(Utils.convertGmtStringToLocaleString(dateString));
        
        
            return grammarMeta;
            
            
            //return XmlToClassParser.parseGrammarMeta(query.execute());
	}

	public List<GrammarMeta> findAllGrammarMetas() throws Exception {
            BaseXClient.Query query = baseXClient.query("<grammars> " +
                                                        "{for $grammarRecord in //grammars/grammarRecord " + 
                                                        "return $grammarRecord} " +
                                                        "</grammars>");
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
	 * 
	 * @param grammar
	 */
	public void updateGrammar(Grammar grammar) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param grammarMeta
	 */
	public void updateGrammarMeta(TheRuler.Model.GrammarMeta grammarMeta) throws Exception{
            if (grammarMeta == null) {
                throw new IllegalArgumentException();
            } else if (grammarMeta.getId() == null || grammarMeta.getName() == null) {
                throw new IllegalArgumentException();
            }
            
            String result = baseXClient.execute("xquery exists(//grammars/grammarRecord[@id=" + grammarMeta.getId() + "])");
            
            if (result.equals("false")) {
                throw new IllegalArgumentException();
            }
   
            String updateNodeCommand = "replace node //grammars/grammarRecord[@id=" + grammarMeta.getId() + "] with" +
                                       "<grammarRecord id='" + grammarMeta.getId() + "'>" +
                                       "  <name>" + grammarMeta.getName() + "</name>" +
                                       "  <description>" + grammarMeta.getDescription() + "</description>" +
                                       "  <date>" + grammarMeta.getDate() + "</date>" +
                                       "</grammarRecord>";
            
            baseXClient.execute("xquery " + updateNodeCommand);
/*  
 * exists(//grammars/grammarRecord[@id=1])
 
<grammarRecord id="1">
    <name>My Grammar Name Updated</name>
    <description>Decription of my grammar</description>
    <date>10.12.2011</date>
  </grammarRecord>
 
 */                
	}

	/**
	 * 
	 * @param grammar
	 */
	public void updateGrammarContent(TheRuler.Model.Grammar grammar) throws Exception{
            if (grammar == null) {
                throw new IllegalArgumentException();
            } else if (grammar.getMeta().getId() == null) {
                throw new IllegalArgumentException();
            }
            
            Long id = grammar.getMeta().getId();
            
            String result = baseXClient.execute("xquery exists(//grammars/grammarRecord[@id=" + id.toString() + "])");
            
            if (result.equals("false")) {
                throw new IllegalArgumentException();
            }
   
            InputStream bais = new ByteArrayInputStream(grammar.getContent().getBytes("UTF-8"));
            
            try {
                baseXClient.replace(id + ".xml", bais);
                System.out.println(baseXClient.info());
            } catch (IOException iOException) {
                iOException.printStackTrace();
            }
	}

	/**
	 * 
	 * @param grammarMeta
	 */
	public void deletaGrammar(GrammarMeta grammarMeta) throws Exception{
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