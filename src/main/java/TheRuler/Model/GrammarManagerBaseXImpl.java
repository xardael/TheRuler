package TheRuler.Model;

import TheRuler.Common.BaseXClient;
import java.io.IOException;
import java.io.StringReader;
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
	public GrammarMeta createGrammar(GrammarMeta grammarMeta) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param id
	 */
	public Grammar findGrammar(Long id) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param id
	 */
	public GrammarMeta findGrammarMeta(Long id) throws Exception{
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
                description = line.getFirstChild().getNodeValue();

            grammarMeta.setId(Long.parseLong(grammarId));
            grammarMeta.setName(name);
            grammarMeta.setDescription(description);
            grammarMeta.setDate(new Date());
        
        
            return grammarMeta;
            
            
            //return XmlToClassParser.parseGrammarMeta(query.execute());
	}

	public List<GrammarMeta> findAllGrammarMetas() {
		throw new UnsupportedOperationException();
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
	public void updateGrammarMeta(TheRuler.Model.GrammarMeta grammarMeta) {
		throw new UnsupportedOperationException();
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
	public void deletaGrammar(GrammarMeta grammarMeta) {
		throw new UnsupportedOperationException();
	}

}