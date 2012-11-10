/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
    
    public static final String DB_HOST = "http://localhost:1527/";
    public static final String DB_USER = "user";
    public static final String DB_PASS = "pass";

    public static final String DB_DRIVER = "org.apache.derby.jdbc.ClientDriver";
}
