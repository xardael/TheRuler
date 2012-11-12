package TheRuler.Common;

import java.util.Locale;

/**
 *
 * @author pyty
 */
public class Config {
    public static final String BASE_PATH = "/TheRuler";
//    public static final Locale LOCALE = Locale.getDefault();
    public static final Locale LOCALE = new Locale("cs", "CZ");
    
    public static final String DB_HOST = "localhost";
    public static final String DB_USER = "admin";
    public static final String DB_PASS = "pass";
    public static final String DB_NAME = "TheRuler";
    public static final int    DB_PORT = 1984;
}
