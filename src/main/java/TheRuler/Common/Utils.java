package TheRuler.Common;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.basex.core.Context;

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
