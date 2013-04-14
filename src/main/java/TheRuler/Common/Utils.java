package TheRuler.Common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author pyty
 */
public class Utils {
    
    public static BaseXClient connectToBaseX() throws IOException {
        BaseXClient baseXClient = new BaseXClient(Config.getValue(Config.C_DB_HOST), Integer.parseInt(Config.getValue(Config.C_DB_PORT)), Config.getValue(Config.C_DB_USER), Config.getValue(Config.C_DB_PASS));
        baseXClient.execute("OPEN " + Config.getValue(Config.C_DB_NAME));
        return baseXClient;
    }
    
    public static String serializeXml(Element element) throws TransformerException
    {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(buffer);

        DOMSource source = new DOMSource(element);
        TransformerFactory.newInstance().newTransformer().transform(source, result);

        String xml = new String(buffer.toByteArray());
        
        return xml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
    }
    
    public static void installDB() throws IOException{
        String result;
        BaseXClient baseXClient = new BaseXClient(Config.getValue(Config.C_DB_HOST), Integer.parseInt(Config.getValue(Config.C_DB_PORT)), Config.getValue(Config.C_DB_USER), Config.getValue(Config.C_DB_PASS));
        //result = baseXClient.execute("DROP DB " + Config.getDbName());
        result = baseXClient.execute("CREATE DB " + Config.getValue(Config.C_DB_NAME));
        result = baseXClient.execute("OPEN " + Config.getValue(Config.C_DB_NAME));
        InputStream bais = new ByteArrayInputStream(Config.GRAMMARS_ROOT_NAME.getBytes("UTF-8"));
        baseXClient.add(Config.GRAMMARS_FILE_NAME, bais);
    }
    
    public static String convertDateToGmtString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(date);
    }
    
    public static Date convertGmtStringToDate(String gmtDateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.parse(gmtDateString);
    }
    
    public static String convertGmtStringToLocaleString(String gmtDateString) throws ParseException {
        Date date = convertGmtStringToDate(gmtDateString);
        //SimpleDateFormat dateFormat = new SimpleDateFormat(); // Using default Locale
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG); // Using default Locale
        dateFormat.setTimeZone(TimeZone.getDefault());
        return dateFormat.format(date);
    }
    
    public static String validate(String xml) throws SAXException, IOException {
        // 1. Lookup a factory for the W3C XML Schema language
        SchemaFactory factory =  SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        
        // 2. Compile the schema. 
        // Here the schema is loaded from a java.io.File, but you could use 
        // a java.net.URL or a javax.xml.transform.Source instead.
        Schema schema = factory.newSchema(Utils.class.getClassLoader().getResource("grammar-core.xsd"));
        
        // 3. Get a validator from the schema.
        Validator validator = schema.newValidator();
        
        // 4. Parse the document you want to check.
        InputStream stream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        Source source = new StreamSource(stream);
        
        // 5. Check the document
        try {
            validator.validate(source);
            return "true";
        }
        catch (SAXException ex) {
            return ex.getMessage();
        }
    }
    
    public static void t() throws NullPointerException {
        throw new NullPointerException("Skvele");
    }
}
