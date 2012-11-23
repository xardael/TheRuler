package TheRuler.Model;

import TheRuler.Common.BaseXClient;
import java.io.StringReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
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
public class GrammarManagerBaseXImpl implements GrammarManager {

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
            Long newId = Long.parseLong(lastId) + 1L;
            
            String insertNodeCommand = "insert node " +
                                       "<grammarRecord id='" + newId + "'>" +
                                       "  <name>" + grammarMeta.getName() + "</name>" +
                                       "  <description>" + ((grammarMeta.getDescription() == null) ? "" : grammarMeta.getDescription()) + "</description>" +
                                       "  <date>" + ((grammarMeta.getDate() == null) ? "" : grammarMeta.getDate()) + "</date>" +
                                       "</grammarRecord>" +
                                       "into //grammars";
            
            String result = baseXClient.execute("xquery " + insertNodeCommand);
            
            grammarMeta.setId(newId);
            return grammarMeta;
	}

	/**
	 * 
	 * @param id
	 */
	public Grammar findGrammar(Long id) throws Exception {
            Grammar grammar = new Grammar();
            GrammarMeta gm = findGrammarMeta(id);
            
            grammar.setMeta(gm);
            grammar.setContent("Tu bude ten zazrak");
            
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
            String date = null;
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
                date = line.getFirstChild().getNodeValue();

            grammarMeta.setId(Long.parseLong(grammarId));
            grammarMeta.setName(name);
            grammarMeta.setDescription(description);
            grammarMeta.setDate(date);
        
        
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
                gm.setDate(line.getFirstChild().getNodeValue());
                
                grammarMetas.add(gm);
            }
                    

//            grammarMeta.setId(Long.parseLong(grammarId));
//            grammarMeta.setName(name);
//            grammarMeta.setDescription(description);
//            grammarMeta.setDate(new Date());
            
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
	public void updateGrammarContent(TheRuler.Model.Grammar grammar) {
		throw new UnsupportedOperationException();
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
	}

}