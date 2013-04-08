package TheRuler.Common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.basex.core.Context;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author pyty
 */
public class Utils {
    
    public static BaseXClient connectToBaseX() throws IOException {
        BaseXClient baseXClient = new BaseXClient(Config.DB_HOST, Config.DB_PORT, Config.DB_USER, Config.DB_PASS);
        baseXClient.execute("OPEN " + Config.DB_NAME);
        return baseXClient;
    }
    
    public static String serializeXml(Element element) throws Exception
    {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(buffer);

        DOMSource source = new DOMSource(element);
        TransformerFactory.newInstance().newTransformer().transform(source, result);

        String xml = new String(buffer.toByteArray());
        
        return xml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
    }
    
    public static void installDB() throws IOException{
        BaseXClient baseXClient = new BaseXClient(Config.DB_HOST, Config.DB_PORT, Config.DB_USER, Config.DB_PASS);
        baseXClient.execute("DROP DB " + Config.DB_NAME);
        baseXClient.execute("CREATE DB " + Config.DB_NAME);
        baseXClient.execute("OPEN " + Config.DB_NAME);
        InputStream bais = new ByteArrayInputStream(Config.GRAMMARS_ROOT.getBytes("UTF-8"));
        baseXClient.add(Config.GRAMMARS_FILE, bais);
    }
    
    public static boolean validate(String xml) throws SAXException, IOException {
        // 1. Lookup a factory for the W3C XML Schema language
        SchemaFactory factory = 
        SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        
        // 2. Compile the schema. 
        // Here the schema is loaded from a java.io.File, but you could use 
        // a java.net.URL or a javax.xml.transform.Source instead.
        File schemaLocation = new File("/opt/xml/docbook/xsd/docbook.xsd");
        Schema schema = factory.newSchema(schemaLocation);
        
        // 3. Get a validator from the schema.
        Validator validator = schema.newValidator();
        
                
        InputStream stream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        
        // 4. Parse the document you want to check.
        Source source = new StreamSource(stream);
        
        // 5. Check the document
        try {
            validator.validate(source);
            return true;
        }
        catch (SAXException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
    
    public static String test() {
        BaseXClient baseXClient = null;
        
        try {
            baseXClient = new BaseXClient(Config.DB_HOST, Config.DB_PORT, Config.DB_USER, Config.DB_PASS);
            baseXClient.execute("OPEN " + Config.DB_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("user", "admin");
        properties.put("password", "pass");
        
        //BaseXClient bxc = new BaseXClient(args)
        
        // BaseXClient.Query query = baseXClient.query(
        // "for $cv in //cv " +
        // "where $cv/meta/hash/text() = \"" + hash + "\" " +
        // "return $cv"
        // );
        // String result = query.execute();
        //
        
        Context context = new Context(properties);
        String result = null;
        
        try {
            BaseXClient.Query query = baseXClient.query(
                    //"for $file in doc('etc/factbook.xml') return data($file)"
                    //"for $file in doc('etc/factbook.xml') return data($file)"
                    "file:list($dir as xs:string) "
            );
            result = query.execute();
        } catch (IOException ex) {
            Logger.getLogger("test").log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
}
