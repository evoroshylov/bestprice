import com.bluespurs.test.util.Client;
import org.junit.Test;

import java.sql.SQLException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * Created by admin on 5/31/2017.
 */
public class ClientTest {

    @Test
    public void testBestBuyResourceMethodWithoutArguments() {
        assertEquals("https://api.bestbuy.com/v1/products(null)?format=json&show=sku,name,salePrice&pageSize=1&sort=salesRankMediumTerm.asc&apiKey=pfe9fpy68yg28hvvma49sc89", Client.getBestBuyBody(null));
    }

    @Test
    public void testWalmartMethodWithoutArguments() {
        assertEquals("http://api.walmartlabs.com/v1/search?query=null&format=json&apiKey=rm25tyum3p9jm9x9x7zxshfa&sort=price&order=asc&numItems=1", Client.getWalmartBody(null));
        //assertNull(sqlConnector.readByEasirId("000000")); //the value which is not in the db
    }

}
