package com.bluespurs.test.ctrl;

import com.bluespurs.test.bestprice.schema.Product;
import com.bluespurs.test.util.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Yevgen Voroshylov on 5/31/2017.
 */
@RestController
@RequestMapping("/product")
public class ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public @ResponseBody Product getProduct (@RequestParam(value="name") String name) {

        if (name == null || name.isEmpty()) {
            return null;
        }

        Product walmart = Client.requestWalmartResource(name);
        if (walmart != null) {
            log.warn(walmart.getProductName() + " " + walmart.getBestPrice());
        }


        Product bestbuy = Client.requestBestBuyResource(name);
        if (bestbuy != null) {
            log.debug(bestbuy.getProductName() + " " + bestbuy.getBestPrice());
        }

        if (walmart == null && bestbuy == null) return null;

        if (walmart == null && bestbuy != null) return bestbuy;
        if (walmart != null && bestbuy == null) return walmart;

        return (bestbuy.getBestPrice() > walmart.getBestPrice()) ? walmart : bestbuy;
    }

}
