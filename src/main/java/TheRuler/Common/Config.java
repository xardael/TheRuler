package TheRuler.Common;

import java.io.*;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;
import org.springframework.util.StringUtils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 *
 * @author pyty
 */
public class Config {
    

    /**
     * Load properties from file. 
     * 
     * @return Properties object containing data loaded from file
     * @throws IOException 
     */
    private static Properties init() throws IOException {
        Properties props = new Properties();
        props.load(Config.class.getClassLoader().getResourceAsStream("config.properties"));
        return props;
    }

    /*
     * Constants 
     */
    public static final String GRAMMAR_ROOT_NAME = "<grammar></grammar>";
    public static final String GRAMMARS_ROOT_NAME = "<grammars></grammars>";
    public static final String GRAMMARS_FILE_NAME = "Meta.xml";
    public static final String CONFIG_FILE_NAME = "config.properties";
    
    /*
     * Constatn names for config.properties 
     */
    public static final String C_DB_HOST = "DB_HOST";
    public static final String C_DB_USER = "DB_USER";
    public static final String C_DB_PASS = "DB_PASS";
    public static final String C_DB_NAME = "DB_NAME";
    public static final String C_DB_PORT = "DB_PORT"; 
    public static final String C_DB_INST = "DB_INST"; 
    
    /**
     * Get value of given key from properties file
     * 
     * @param key
     * @return If key exists, its value is returned. Otherwise returns empty string.
     * @throws IOException 
     */
    public static String getValue(String key) throws IOException {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        Properties props = init();
        return props.getProperty(key, "");
    }
    
    /**
     * Set given key value into properties file
     * 
     * @param key
     * @param value
     * @throws IOException, IllegalArgumentException
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
