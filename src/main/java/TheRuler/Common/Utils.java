package TheRuler.Common;

import TheRuler.Exceptions.ConfigException;
import TheRuler.Exceptions.DatabaseException;
import java.io.*;
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
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Static methods which provides utility functions.
 *
 * @author Peter Gren
 */
public class Utils {
    
    private static final Logger LOGGER = Logger.getLogger(Utils.class);

    /**
     * Tries to open database connection.
     * 
     * @return BaseXClient instance as a connection identificator.
     */
    public static BaseXClient connectToBaseX() throws DatabaseException {
        try {
            BaseXClient baseXClient = new BaseXClient(Config.getValue(Config.C_DB_HOST), Integer.parseInt(Config.getValue(Config.C_DB_PORT)), Config.getValue(Config.C_DB_USER), Config.getValue(Config.C_DB_PASS));
            baseXClient.execute("OPEN " + Config.getValue(Config.C_DB_NAME));
            return baseXClient;
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        }  catch (ConfigException e) {
            throw new DatabaseException(e);
        }
    }
    
    /**
     * Convert XML object hierarchy into string. Used for printing XML
     * directly into UI.
     * 
     * @param element XML element.
     * @return String XML without prolog as a text.
     */
    public static String serializeXml(Element element) throws TransformerException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(buffer);
        DOMSource source = new DOMSource(element);
        TransformerFactory.newInstance().newTransformer().transform(source, result);
        String xml = "";
        try {
            xml = buffer.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.log(Level.ERROR, e);
        }
        return xml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
    }
    
    /**
     * Adds prolog and refs to schema into given SRGS grammar's header.
     * 
     * @param xml XML string.
     * @return 
     */
    public static String fixSrgsHeader(String xml) {
        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<!DOCTYPE grammar PUBLIC \"-//W3C//DTD GRAMMAR 1.0//EN\"  \"http://www.w3.org/TR/speech-grammar/grammar.dtd\">\n" +
                        "<grammar xmlns=\"http://www.w3.org/2001/06/grammar\"\n" +
                        "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xsi:schemaLocation=\"http://www.w3.org/2001/06/grammar\n" + 
                        "                             http://www.w3.org/TR/speech-grammar/grammar.xsd\"\n " +
                        "         xml:lang=\"en-US\" version=\"1.0\">\n";
        
        return xml.replace("<grammar>", header);
    }

    /**
     * Installs new database and set proper structure.
     * Login information must be stored in configuration propertis file.
     * If db with configured name exists, it will be overwritten.
     */
    public static void installDB() throws DatabaseException, ConfigException {
        BaseXClient baseXClient = null;
        try {
            baseXClient = new BaseXClient(Config.getValue(Config.C_DB_HOST), Integer.parseInt(Config.getValue(Config.C_DB_PORT)), Config.getValue(Config.C_DB_USER), Config.getValue(Config.C_DB_PASS));
            baseXClient.execute("CREATE DB " + Config.getValue(Config.C_DB_NAME));
            baseXClient.execute("OPEN " + Config.getValue(Config.C_DB_NAME));
            InputStream bais = new ByteArrayInputStream(Config.GRAMMARS_ROOT_NAME.getBytes("UTF-8"));
            baseXClient.add(Config.GRAMMARS_FILE_NAME, bais);
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new DatabaseException(e);
        }  finally {
            try {baseXClient.close();} catch (Exception e) {LOGGER.log(Level.ERROR, e);}
        }
    }
    
    /**
     * Escapes XML reserved characters into entities.
     * 
     * See <a href="https://www.owasp.org/index.php/XSS_%28Cross_Site_Scripting%29_Prevention_Cheat_Sheet">OWASP XSS (Cross Site Scripting) Prevention Cheat Sheet</a>.
     * 
     * @param input String to escape.
     * @return Escaped string.
     */
    public static String escapeXml(String input) {
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#x27;")
                    .replace("/", "&#x2F;"); 
    }

    /**
     * Converts java Date to string using proper format and GMT timezone. 
     * Used for storing date into XML as a String.
     * 
     * @param date Java date.
     * @return Formated text date.
     */
    public static String convertDateToGmtString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Config.DATE_FORMAT_STORED);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(date);
    }

    /**
     * Converts date from XML file, which is in gmt timezone and default
     * format into java object.
     * 
     * @param gmtDateString String in default date format Config.DATE_FORMAT_STORED.
     * @return Date in GMT.
     */
    public static Date convertGmtStringToDate(String gmtDateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Config.DATE_FORMAT_STORED);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.parse(gmtDateString);
    }

    /**
     * Converts GMT date string to locale string in locale timezone.
     * 
     * @param gmtDateString String in dufalt date format Config.DATE_FORMAT_STORED.
     * @return GMT date in locale string and timezone.
     */
    public static String convertGmtStringToLocaleString(String gmtDateString) throws ParseException {
        Date date = convertGmtStringToDate(gmtDateString);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG); // Using default Locale
        dateFormat.setTimeZone(TimeZone.getDefault());
        return dateFormat.format(date);
    }

    /**
     * Validates XML against SRGS schema.
     * 
     * @param xml String value stated for validation.
     * @return True if is valid, error output otherwise.
     */
    public static String validate(String xml) throws SAXException, IOException {
        SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        Schema schema = factory.newSchema(Utils.class.getClassLoader().getResource(Config.SRGS_SCHEMA_FILE_NAME));
        Validator validator = schema.newValidator();
        InputStream stream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
        Source source = new StreamSource(stream);
        try {
            validator.validate(source);
            return "true";
        } catch (SAXException ex) {
            return ex.getMessage();
        }
    }
}
