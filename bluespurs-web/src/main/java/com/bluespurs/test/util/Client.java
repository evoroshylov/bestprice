package com.bluespurs.test.util;

import com.bluespurs.test.bestprice.schema.Product;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * Created by Yevgen Voroshylov on 5/31/2017.
 */
public class Client {
    private static final Logger log = LoggerFactory.getLogger(Client.class);
    private static HttpClient httpClient;
    private static final String walmart_key = "rm25tyum3p9jm9x9x7zxshfa";
    private static final String bestbuy_key = "pfe9fpy68yg28hvvma49sc89";

    public static HttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = HttpClientBuilder.create().build();
        }
        return httpClient;
    }

    /**
     * @param name contains search value
     * @return the URL with all parameters to obtain data from walmart
     */
    public static String getWalmartBody(String name) {
        StringBuilder str = new StringBuilder();
        str.append("http://api.walmartlabs.com/v1/search?query="+name+"&format=json&apiKey="+walmart_key+"&sort=price&order=asc&numItems=1");
        return str.toString();
    };

    /**
     * @param name contains search value
     * @return the URL with all parameters to obtain data from Best Buy
     */
    public static String getBestBuyBody(String name) {
        StringBuilder str = new StringBuilder();
        str.append("https://api.bestbuy.com/v1/products("+name+")");
        str.append("?format=json&show=sku,name,salePrice&pageSize=1&sort=salesRankMediumTerm.asc&apiKey=" + bestbuy_key);
        return str.toString();
    };

    /**
     * @param name contains search value
     * @return Product object or null if any issue is being raised
     */
    public static Product requestWalmartResource(String name) {
        Product result = null;

        HttpRequestBase request;

        String responseString = "";
        request = new HttpGet(getWalmartBody(name));
        log.info("resourceURL: '" + getWalmartBody(name) + "'");
        request.addHeader("content-type", "application/json");

        HttpResponse response = null;
        int code = -1;
        try {
            response = getHttpClient().execute(request);
            code = response.getStatusLine().getStatusCode();
            if (code >= 400) {
                throw new RuntimeException(
                        "Could not access protected resource. Server returned http code: "
                                + code);
            }

            HttpEntity entity = response.getEntity();
            responseString = entity == null ? "" : EntityUtils.toString(entity, "UTF-8");

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(responseString);

            log.info(json.get("query").toString());

            log.info(json.toString());
            if (!json.get("totalResults").toString().equalsIgnoreCase("0")) {
                for (Object o : (JSONArray) json.get("items")) {
                    //get event_name
                    result = new Product();
                    result.setCurrency("CAD");
                    result.setLocation("Walmart");
                    result.setProductName(((JSONObject) o).get("name").toString());
                    result.setBestPrice(Double.parseDouble(((JSONObject) o).get("salePrice").toString()));
                }
            }

        } catch (IOException | ParseException e) {
            log.error(e.getLocalizedMessage());
        } finally {
            try {
                if (response != null) EntityUtils.consume(response.getEntity());
            } catch (IOException e) {
                log.error(e.getLocalizedMessage());
            }
        }
        return result;
    }

    /**
     * @param name contains search value
     * @return Product object or null if any issue is being raised
     */
    public static Product requestBestBuyResource(String name) {
        name = "search="+name.replace(" ", "&search=");
        Product result = null;

        HttpRequestBase request;

        String responseString = "";
        log.info("resourceURL: '" +  getBestBuyBody(name) + "'");
        request = new HttpGet(getBestBuyBody(name));
        request.addHeader("content-type", "application/json");

        HttpResponse response = null;
        int code = -1;
        try {
            response = getHttpClient().execute(request);
            log.info(responseString);
            code = response.getStatusLine().getStatusCode();
            if (code >= 400) {
                throw new RuntimeException(
                        "Could not access protected resource. Server returned http code: "
                                + code);
            }

            HttpEntity entity = response.getEntity();
            responseString = entity == null ? "" : EntityUtils.toString(entity, "UTF-8");

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(responseString);

            //handleResponse(response);

            log.info(json.get("total").toString());

            if (!json.get("total").toString().equalsIgnoreCase("0")) {
                for (Object o : (JSONArray) json.get("products")) {
                    //get event_name
                    result = new Product();
                    result.setCurrency("CAD");
                    result.setLocation("Best Buy");
                    result.setProductName(((JSONObject) o).get("name").toString());
                    result.setBestPrice(Double.parseDouble(((JSONObject) o).get("salePrice").toString()));
                }
            }

        } catch (IOException | ParseException e) {
            log.error(e.getLocalizedMessage());
        } finally {
            try {
                if (response != null) EntityUtils.consume(response.getEntity());
            } catch (IOException e) {
                log.error(e.getLocalizedMessage());
            }
        }
        return result;
    }
}
