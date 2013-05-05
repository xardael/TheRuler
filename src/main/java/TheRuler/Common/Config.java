package TheRuler.Common;

import TheRuler.Exceptions.ConfigException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.Properties;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Static fields and methods for application configuration.
 *
 * @author Peter Gren
 */
public class Config {
    private static final Logger LOGGER = Logger.getLogger(Config.class);

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
    public static final String GRAMMARS_FILE_NAME = "GrammarsFile.xml";
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
     * Check if is database installed as boolean.
     * 
     * @return TRUE if is database installed, FALSE otherwise.
     */
    public static Boolean dbInstalled() throws ConfigException {
        return Boolean.parseBoolean(Config.getValue(Config.C_DB_INST));
    }
        
    /**
     * Gets value of given key from properties file.
     *
     * @param key Key in config file.
     * @return If key exists, its value is returned. Otherwise returns empty string.
     */
    public static int getPortAsInt() throws ConfigException {
        try {
            Properties props = init();
            return Integer.parseInt(props.getProperty(Config.C_DB_PORT, "0"));
        } catch (NumberFormatException e) {
            return 0;
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new ConfigException(e);
        }
    }
    
    /**
     * Gets value of given key from properties file.
     *
     * @param key Key in config file.
     * @return If key exists, its value is returned. Otherwise returns empty string.
     */
    public static String getValue(String key) throws ConfigException {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        
        try {
            Properties props = init();
            return props.getProperty(key, "");
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new ConfigException(e);
        }
    }

    /**
     * Sets given key value into properties file.
     *
     * @param key Name of key in config file.
     * @param value Value to store.
     */
    public static void setValue(String key, String value) throws ConfigException {
        if (key == null || value == null) {
            throw new IllegalArgumentException();
        }
        Writer writer = null;
        
        try {
            Properties props = init();
            props.setProperty(key, value);
            URL url = Config.class.getClassLoader().getResource(CONFIG_FILE_NAME);
            String path = url.getPath();
            writer = new FileWriter(path);
            props.store(writer, "");            
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, e);
            throw new ConfigException(e);
        } finally {
            try {writer.close();} catch (Exception e) {LOGGER.log(Level.ERROR, e);}
        }
    }
}
