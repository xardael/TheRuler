package TheRuler.Model;

import TheRuler.Common.BaseXClient;
import TheRuler.Common.Config;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.cmd.Retrieve;
import org.basex.core.cmd.XQuery;

/**
 *
 * @author pyty
 */
public class DAOImpl {
    
    private static final Logger LOGGER = Logger.getLogger("a");

    private BaseXClient baseXClient;

    public DAOImpl() {
        try {
            baseXClient = new BaseXClient(Config.DB_HOST, Config.DB_PORT, Config.DB_USER, Config.DB_PASS);
            baseXClient.execute("OPEN " + Config.DB_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public String test()
    {
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("user", "admin");
        properties.put("password", "pass");
        
        //BaseXClient bxc = new BaseXClient(args)
        
//        BaseXClient.Query query = baseXClient.query(
//                "for $cv in //cv " +
//                "where $cv/meta/hash/text() = \"" + hash + "\" " +
//                "return $cv"
//            );
//            String result = query.execute();
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
            Logger.getLogger(DAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
}
