package TheRuler.Common;

import java.io.*;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;

/**
 *
 * @author pyty
 */
public class Config {

    private static Properties init() {
        Properties props = new Properties();
        try {
            props.load(Config.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException ex) {
            throw new RuntimeException("Error opening DB config file", ex);
        }
        return props;
    }
    
    public String getDbUrl() {
        Properties props = init();
        return props.getProperty("dburl");
    }
    
    public static final String BASE_PATH = "/TheRuler";
//    public static final Locale LOCALE = Locale.getDefault();
    public static final Locale LOCALE = new Locale("cs", "CZ");
    
    public static final String GRAMMARS_ROOT = "<grammars></grammars>";
    public static final String GRAMMARS_FILE = "Meta.xml";
    
    public static final String DB_HOST = "localhost";
    public static final String DB_USER = "admin";
    public static final String DB_PASS = "pass";
    public static final String DB_NAME = "Test44";
    public static final int    DB_PORT = 1984;
    
    public static Boolean getDbInstalled() {
        Properties props = init();
        return Boolean.parseBoolean(props.getProperty("DB_INSTALLED", "FALSE"));
    }
    
    public static void setDbInstalled(Boolean b) {
        Properties props = init();
        props.setProperty("DB_INSTALLED", b.toString());
        try {
            URL url = Config.class.getClassLoader().getResource("config.properties");  
            String path = url.getPath();  
            Writer writer = new FileWriter(path);
            props.store(writer, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
