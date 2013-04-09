package TheRuler.Common;

import java.io.*;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
    
    public static String getDbHost() {
        return "localhost";
    }
    public static  void setDbHost() {
        throw new NotImplementedException();
    }
    
    public static String getDbUser() {
        return "admin";
    }    
    public static void setDbUser() {
        throw new NotImplementedException();
    }
    
    public static String getDbPass() {
        return "pass";
    }    
    public static void setDbPass() {
        throw new NotImplementedException();
    }
    
    public static String getDbName() {
        return "nova";
    }    
    public static void setDbName() {
        throw new NotImplementedException();
    }
    
    public static int getDbPort() {
        return 1984;
    }    
    public static void setDbPort() {
        throw new NotImplementedException();
    }
    
    public static Boolean getDbInstalled() {
        Properties props = init();
        return Boolean.parseBoolean(props.getProperty("DB_INSTALLED", "FALSE"));
    }
    public static final void setDbInstalled() {
        throw new NotImplementedException();
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
