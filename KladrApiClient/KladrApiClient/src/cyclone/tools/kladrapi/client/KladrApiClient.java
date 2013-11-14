package cyclone.tools.kladrapi.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.URLConnection;

import java.util.ArrayList;
import java.util.List;

import java.util.Properties;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Java client for http://kladr-api.ru/ service
 *  
 * @author      ILya Cyclone <ilya.cyclone@gmail.com>
 * @version     1.0
 * @since       2013-11-14
 */

public class KladrApiClient {
    public KladrApiClient() {
        super();
    }

    private final static Logger _logger = Logger.getLogger(KladrApiClient.class.getName());

    private String token, key;
    private String proxyHost, proxyPort;

    /**
     * Get your token and key here: http://kladr-api.ru/
     * @param token
     * @param key
     */
    public KladrApiClient(String token, String key) {
        super();
        this.token = token;
        this.key = key;

        // read configuration file
        Properties prop = new Properties();
        InputStream propStream = null;
        try {
            propStream =
                    KladrApiClient.class.getClassLoader().getResourceAsStream("cyclone/tools/kladrapi/client/config.properties");
            if (propStream != null) {
                prop.load(propStream);
                // read proxy configuration
                String proxyHost = prop.getProperty("proxyHost");
                String proxyPort = prop.getProperty("proxyPort");
                if (proxyHost != null && proxyPort != null) {
                    this.proxyHost = proxyHost;
                    this.proxyPort = proxyPort;
                    _logger.fine("Setting proxy: http://" + proxyHost + ":" + proxyPort);
                }
            }
        } catch (Exception ex) {
            _logger.severe("Couldn't read config.properties");
        } finally {
            if (propStream != null) {
                try {
                    propStream.close();
                } catch (IOException e) {
                    _logger.severe("Couldn't close input stream: " + e.getMessage());
                }
            }
        }

    }

    /** Returns list of districts without parents
     * @param query - search query
     * @param limit - max number of returned objects
     * @return list of districts as KladrObject
     */
    public List<KladrObject> getKladrDistricts(String query, int limit) {
        return getKladrDistricts(query, limit, false);
    }

    /** Returns list of districts with or without parents
     * @param query - search query
     * @param limit - max number of returned objects
     * @param withParent - return parents (region)?
     * @return list of districts as KladrObject
     */
    public List<KladrObject> getKladrDistricts(String query, int limit, boolean withParent) {
        String params =
            new KladrApiUrlBuilder().setContentType(KladrApiUrlBuilder.CONTENT_TYPE_DISTRICT).setWithParent(withParent).setLimit(limit).setQuery(query).toString();
        List<KladrObject> districtList = getKladrObjectList(params);
        return districtList;
    }

    /** Returns list of district names. Duplicates are omitted.
     * @param query - search query
     * @param limit - max number of returned objects
     * @return list of district names as String
     */
    public List<String> getDistrictNames(String query, int limit) {
        return extractKladrObjectListNames(getKladrDistricts(query, limit));
    }

    /** Returns list of cities without parents
     * @param query - search query
     * @param limit - max number of returned objects
     * @return list of cities as KladrObject
     */
    public List<KladrObject> getKladrCities(String query, int limit) {
        return getKladrCities(query, limit, false);
    }

    /** Returns list of cities with or without parents
     * @param query - search query
     * @param limit - max number of returned objects
     * @param withParent - return parents (region, district)?
     * @return list of cities as KladrObject
     */
    public List<KladrObject> getKladrCities(String query, int limit, boolean withParent) {
        String params =
            new KladrApiUrlBuilder().setContentType(KladrApiUrlBuilder.CONTENT_TYPE_CITY).setWithParent(withParent).setLimit(limit).setQuery(query).toString();

        List<KladrObject> cityList = getKladrObjectList(params);
        return cityList;
    }

    /** Returns list of city names. Duplicates are omitted.
     * @param query - search query
     * @param limit - max number of returned objects
     * @return list of city names as String
     */
    public List<String> getCityNames(String query, int limit) {
        return extractKladrObjectListNames(getKladrCities(query, limit));
    }

    /** Returns list of streets of specified city by KLADR city code
     * @param query - search query
     * @param cityId - KLADR city code
     * @param limit - max number of returned objects
     * @return list of streets as KladrObject
     */
    public List<KladrObject> getKladrStreetsByCityId(String query, String cityId, int limit) {
        String params =
            new KladrApiUrlBuilder().setContentType(KladrApiUrlBuilder.CONTENT_TYPE_STREET).setCityId(cityId).setLimit(limit).setQuery(query).toString();

        List<KladrObject> streetList = getKladrObjectList(params);

        //         add trailing 0 symbols to make length = 13
        //        if (streetList.isEmpty()) {
        //            if (cityId.length() != 13) {
        //                StringBuilder cityCodeAppender = new StringBuilder(cityId);
        //                for (int i = cityId.length(); i < 13; i++) {
        //                    cityCodeAppender.append(0);
        //                }
        //                cityId = cityCodeAppender.toString();
        //                streetList = getKladrStreetsByCityId(query, cityId, limit);
        //            }
        //        }
        return streetList;
    }

    /** Returns list of streets of specified city by city name.
     * If more than one city found assuming that the first is implied.
     * @param query - search query
     * @param cityName - name of the city
     * @param limit - max number of returned objects
     * @return list of streets as KladrObject
     */
    public List<KladrObject> getKladrStreetsByCityName(String query, String cityName, int limit) {
        List<KladrObject> streetList = null;

        List<KladrObject> cityList = getKladrCities(cityName, 1);
        if (cityList.size() > 0) {
            KladrObject city = cityList.get(0);
            streetList = getKladrStreetsByCityId(query, city.getId(), limit);
        }

        return streetList;
    }

    /** Returns list of street names of specified city by KLADR city code. Duplicated as omitted.
     * @param query - search query
     * @param cityId - KLADR city code
     * @param limit - max number of returned objects
     * @return list of street names as String
     */
    public List<String> getStreetNamesByCityId(String query, String cityId, int limit) {
        return extractKladrObjectListNames(getKladrStreetsByCityId(query, cityId, limit));
    }

    /** Returns list of street names of specified city by city name. Duplicated as omitted.
     * @param query - search query
     * @param cityName - name of the city
     * @param limit - max number of returned objects
     * @return list of street names as String
     */
    public List<String> getStreetNamesByCityName(String query, String cityName, int limit) {
        return extractKladrObjectListNames(getKladrStreetsByCityName(query, cityName, limit));
    }


    /** Returns list of names of given KladrObject list. Duplicates are omitted.
     * @param kladrObjectList - list of KladrObjects to get names from
     * @return list of KladrObjects names as String
     */
    public static List<String> extractKladrObjectListNames(List<KladrObject> kladrObjectList) {
        List<String> nameList = new ArrayList<String>();
        if (!kladrObjectList.isEmpty()) {
            String previousName = null;
            for (KladrObject kladrObject : kladrObjectList) {
                // remove duplicates
                String currentName = kladrObject.getName();
                if (previousName == null || !previousName.equals(currentName)) {
                    nameList.add(kladrObject.getName());
                    previousName = currentName;
                }
            }
        }
        return nameList;
    }


    /** Returns list of KladrObjects from kladr-api service with given parameters
     * @param params should start with & symbol. Token or key are not required here, e.g. "&contentType=city&query=somequery&limit=5"
     * @return list of KladrObjects
     */
    public List<KladrObject> getKladrObjectList(String params) {
        JSONArray results = getKladrJSONArray(params);
        List<KladrObject> list = new ArrayList<KladrObject>();
        for (int i = 0; i < results.length(); i++) {
            try {
                list.add(new KladrObject(results.getJSONObject(i)));
            } catch (JSONException e) {
                _logger.severe("Error parsing KladrObject JSON string: " + results + ": " + e.getMessage());
            }
        }
        return list;
    }

    /** Returns JSONArray of results from kladr-api service with given parameters.
     * @param params should start with & symbol. Token or key are not required here, e.g. "&contentType=city&query=somequery&limit=5"
     * @return JSONArray
     */
    public JSONArray getKladrJSONArray(String params) {
        JSONArray results = null;

        String jsonString = queryKladrApi(params);

        JSONObject json = null;
        try {
            json = new JSONObject(jsonString);
        } catch (JSONException e) {
            _logger.severe("Bad JSON string received: " + e.getMessage());
        }

        if (json != null) {
            try {
                results = json.getJSONArray("result");
            } catch (JSONException e) {
                _logger.severe("Result not found in received JSON string: " + e.getMessage());
            }
        }

        return results;
    }

    /** Returns unmodified String answer from kladr-api service with given parameters
     * @param params should start with & symbol. Token or key are not required here, e.g. "&contentType=city&query=somequery&limit=5"
     * Full parameter list:
     * regionId – region code
     * districtId – district code
     * cityId – city code
     * streetId – street code
     * buildingId – building code
     * query – search query
     * contentType – returned objects type (region, district, city, street, building)
     * withParent – query parent objects?
     * limit – max number of returned objects
     * @return kladr-api service answer as String
     */
    public String queryKladrApi(String params) {
        String response = null;
        try {
            // building request URL
            // only params needed here
            if (params.startsWith("http://")) {
                params = params.substring(params.indexOf("?"));
                params = params.replace("?", "&");
            }
            if (params.contains("&")) {
                params = params.substring(params.indexOf("&"));
            }

            String urlString = KladrApiUrlBuilder.getBaseUrl(this.token, this.key);
            urlString = urlString + params;
            _logger.fine("Querying kladr-api: " + urlString);
            URL url = null;
            // if proxy needed
            if (proxyHost != null && proxyPort != null) {
                url = new URL("http", proxyHost, Integer.parseInt(proxyPort), urlString);
            } else {
                url = new URL(urlString);
            }

            URLConnection conn = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder responseBuilder = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                //inputLine = StringEscapeUtils.unescapeJava(inputLine);
                responseBuilder.append(inputLine);
            }
            response = responseBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setProxy(String proxyHost, String proxyPort) {
        setProxyHost(proxyHost);
        setProxyPort(proxyPort);
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyPort() {
        return proxyPort;
    }
}
