import com.bluespurs.test.util.Client;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * Created by admin on 5/31/2017.
 */
public class ClientTest {

    @Test
    public void testBestBuyResourceMethodWithoutArguments() throws UnsupportedEncodingException {
        assertEquals("", Client.getBestBuyBody(null));
    }

    @Test
    public void testWalmartMethodWithoutArguments() throws UnsupportedEncodingException {
        assertEquals("", Client.getWalmartBody(null));
        //assertNull(sqlConnector.readByEasirId("000000")); //the value which is not in the db
    }

}
