package TheRuler.Common;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.Properties;

/**
 * Static fields and methods for application configuration.
 *
 * @author Peter Gren
 */
public class Config {

    /**
     * Loads properties from file.
     *
     * @return Properties object containing data loaded from file.
     */
    private static Properties init() throws IOException {
        Properties props = new Properties();
        props.load(Config.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME));
        return props;
    }

    /***********************************************
     * Constants
     ***********************************************/
    /** SRGS grammar root element. */
    public static final String GRAMMAR_ROOT_NAME = "<grammar></grammar>";
    /** Meta file root element. */
    public static final String GRAMMARS_ROOT_NAME = "<grammars></grammars>";
    /** Meta file name. */
    public static final String GRAMMARS_FILE_NAME = "Meta.xml";
    /** Configuration file name. */
    public static final String CONFIG_FILE_NAME = "config.properties";
    /** Localization file name. */
    public static final String MESSAGES_FILE_NAME = "loacale/messages";
    /** SRGS XML Schema file name for validation. */
    public static final String SRGS_SCHEMA_FILE_NAME = "grammar-core.xsd";
    /** Format in which is date stored in XML files. */
    public static final String DATE_FORMAT_STORED = "yyyy-MM-dd HH:mm:ssZ";
    
    /***********************************************
     * Constatn names for config.properties
     ***********************************************/
    /** Database host constant name in propertis file. */
    public static final String C_DB_HOST = "DB_HOST";
    /** Database user name constant name in propertis file. */
    public static final String C_DB_USER = "DB_USER";
    /** Database password constant name in propertis file. */
    public static final String C_DB_PASS = "DB_PASS";
    /** Database name constant name in propertis file. */
    public static final String C_DB_NAME = "DB_NAME";
    /** Database port constant name in propertis file. */
    public static final String C_DB_PORT = "DB_PORT";
    /** Constant name for "database installed " flag.  */
    public static final String C_DB_INST = "DB_INST";

    /**
     * Gets value of given key from properties file.
     *
     * @param key Key in config file.
     * @return If key exists, its value is returned. Otherwise returns empty string.
     */
    public static String getValue(String key) throws IOException {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        Properties props = init();
        return props.getProperty(key, "");
    }

    /**
     * Sets given key value into properties file.
     *
     * @param key Name of key in config file.
     * @param value Value to store.
     */
    public static void setValue(String key, String value) throws IOException {
        if (key == null || value == null) {
            throw new IllegalArgumentException();
        }
        Properties props = init();
        props.setProperty(key, value);
        URL url = Config.class.getClassLoader().getResource(CONFIG_FILE_NAME);
        String path = url.getPath();
        Writer writer = new FileWriter(path);
        props.store(writer, "");
    }
}
