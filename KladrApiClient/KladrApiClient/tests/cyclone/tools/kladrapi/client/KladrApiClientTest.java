package cyclone.tools.kladrapi.client;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.LogManager;

import org.json.JSONArray;
import org.json.JSONObject;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/* JUnit 4 KladrApiClient test class */
public class KladrApiClientTest {
    public KladrApiClientTest() {
    }
    
    // Using demo token and key. Change periodically.
    // Get your token and key here: http://kladr-api.ru/
    public static final String DEMO_TOKEN = "51dfe5d42fb2b43e3300006e", DEMO_KEY =
        "86a2c2a06f1b2451a87d05512cc2c3edfdf41969";

    private KladrApiClient client;
    private KladrObject[] expectedKladrDistricts, expectedKladrCities, exprectedKladrStreets;
    private String[] expectedStreetNames =
        new String[] { "Ново-Переведеновская", "Новоалексеевская", "Новобутовская" };
    private JSONArray expectedKladrCityJSONArray;

    @Before
    public void setUp() throws Exception {
        try {
            LogManager.getLogManager().readConfiguration(SimpleTest.class.getResourceAsStream("logging.properties"));
        } catch (IOException e) {
            System.err.println("Could not setup logger configuration: " + e.toString());
        }

        this.client = new KladrApiClient(DEMO_TOKEN, DEMO_KEY);
//        this.client.setProxy(null, null);

        expectedKladrDistricts = new KladrObject[2];
        expectedKladrDistricts[0] =
                new KladrObject("0200300000000", "Архангельский", "453030", "Район", "р-н", "80203000000",
                                new ArrayList() {
                    {
                        add(new KladrObject("0200000000000", "Башкортостан", "450000", "Республика", "Респ",
                                            "80000000000"));
                    }
                });

        expectedKladrDistricts[1] =
                new KladrObject("2800200000000", "Архаринский", "", "Район", "р-н", "10205000000", new ArrayList() {
                    {
                        add(new KladrObject("2800000000000", "Амурская", "675000", "Область", "обл", "10000000000"));
                    }
                });


        expectedKladrCities = new KladrObject[3];
        expectedKladrCities[0] = new KladrObject("7700000000000", "Москва", "", "Город", "г", "45000000000");
        expectedKladrCities[1] =
                new KladrObject("4001800100000", "Мосальск", "249930", "Город", "г", "29229501000", new ArrayList() {
                    {
                        add(new KladrObject("4000000000000", "Калужская", "", "Область", "обл", "29000000000"));
                        add(new KladrObject("4001800000000", "Мосальский", "", "Район", "р-н", "29229000000"));
                    }
                });
        expectedKladrCities[2] =
                new KladrObject("5001400009151", "Московский", "142784", "Город", "г", "46228505000", new ArrayList() {
                    {
                        add(new KladrObject("5000000000000", "Московская", "", "Область", "обл", "46000000000"));
                        add(new KladrObject("5001400000000", "Ленинский", "", "Район", "р-н", "46228000000"));
                    }
                });

        exprectedKladrStreets = new KladrObject[3];
        exprectedKladrStreets[0] = new KladrObject("77000000000708401", "Ново-Переведеновская", "", "Улица", "ул", "");
        exprectedKladrStreets[1] =
                new KladrObject("77000000000201700", "Новоалексеевская", "129626", "Улица", "ул", "45280552000");
        exprectedKladrStreets[2] =
                new KladrObject("77000000000201800", "Новобутовская", "117624", "Улица", "ул", "45293594000");

        expectedKladrCityJSONArray = new JSONArray();
        JSONObject city1 =
            new JSONObject(new KladrObject("7700000000000", "Москва", null, "Город", "г", "45000000000").toJSONObjectString());
        JSONObject city2 =
            new JSONObject(new KladrObject("4001800100000", "Мосальск", "249930", "Город", "г", "29229501000").toJSONObjectString());
        JSONObject city3 =
            new JSONObject(new KladrObject("5001400009151", "Московский", "142784", "Город", "г", "46228505000").toJSONObjectString());

        expectedKladrCityJSONArray.put(city1);
        expectedKladrCityJSONArray.put(city2);
        expectedKladrCityJSONArray.put(city3);
    }

//    @After
//    public void tearDown() throws Exception {
//    }

    private void assertListsEqual(String errorMessage, Object[] expectedListItems, List actualList) {
        List expectedList = new ArrayList(expectedListItems.length);
        for (Object expectedItem : expectedListItems) {
            expectedList.add(expectedItem);
        }
        assertEquals(errorMessage, expectedList, actualList);
    }

    private static final String LIST_SHOULD_BE_EMPTY = "List should be empty", WRONG_QUERY = "asd";

    /**
     * @see KladrApiClient#getCityNames(String,int)
     */
    @Test
    public void testGetCityNames() {
        assertTrue(LIST_SHOULD_BE_EMPTY, client.getCityNames(WRONG_QUERY, 3).isEmpty());

        assertListsEqual("City name list incorrect", new String[] { "Москва", "Мосальск", "Московский" },
                         client.getCityNames("мос", 3));
    }

    /**
     * @see KladrApiClient#getStreetNamesByCityId(String,String,int)
     */
    @Test
    public void testGetStreetNamesByCityId() {
        assertTrue(LIST_SHOULD_BE_EMPTY, client.getStreetNamesByCityId(WRONG_QUERY, "7700000000000", 3).isEmpty());

        assertListsEqual("Street name list incorrect", expectedStreetNames,
                         client.getStreetNamesByCityId("ново", "7700000000000", 3));
    }

    /**
     * @see KladrApiClient#getStreetNamesByCityName(String,String,int)
     */
    @Test
    public void testGetStreetNamesByCityName() {
        assertTrue(LIST_SHOULD_BE_EMPTY, client.getStreetNamesByCityName(WRONG_QUERY, "москва", 3).isEmpty());

        assertListsEqual("City name list incorrect", expectedStreetNames,
                         client.getStreetNamesByCityName("ново", "москва", 3));
    }

    /**
     * @see KladrApiClient#getDistrictNames(String,int)
     */
    @Test
    public void testGetDistrictNames() {
        assertTrue(LIST_SHOULD_BE_EMPTY, client.getDistrictNames(WRONG_QUERY, 3).isEmpty());

        assertListsEqual("District name list incorrect", new String[] { "Архангельский", "Архаринский" },
                         client.getDistrictNames("арх", 3));
    }

    /**
     * @see KladrApiClient#getKladrDistricts(String,int,boolean)
     */
    @Test
    public void testGetKladrDistricts() {
        assertTrue(LIST_SHOULD_BE_EMPTY, client.getKladrDistricts(WRONG_QUERY, 3).isEmpty());

        assertListsEqual("District list incorrect", expectedKladrDistricts, client.getKladrDistricts("арх", 3, true));
    }

    /**
     * @see KladrApiClient#getKladrCities(String,int,boolean)
     */
    @Test
    public void testGetKladrCities() {
        assertTrue(LIST_SHOULD_BE_EMPTY, client.getKladrCities(WRONG_QUERY, 3).isEmpty());

        assertListsEqual("City list incorrect", expectedKladrCities, client.getKladrCities("мос", 3, true));
    }

    /**
     * @see KladrApiClient#getKladrStreetsByCityId(String,String,int)
     */
    @Test
    public void testGetKladrStreetsByCityId() {
        assertTrue(LIST_SHOULD_BE_EMPTY, client.getKladrStreetsByCityId(WRONG_QUERY, "7700000000000", 3).isEmpty());

        assertListsEqual("Street list incorrect", exprectedKladrStreets,
                         client.getKladrStreetsByCityId("ново", "7700000000000", 3));
    }

    /**
     * @see KladrApiClient#getKladrStreetsByCityName(String,String,int)
     */
    @Test
    public void testGetKladrStreetsByCityName() {
        assertTrue(LIST_SHOULD_BE_EMPTY, client.getKladrStreetsByCityName(WRONG_QUERY, "москва", 3).isEmpty());

        assertListsEqual("Street list incorrect", exprectedKladrStreets,
                         client.getKladrStreetsByCityName("ново", "москва", 3));
    }

    /**
     * @see KladrApiClient#getKladrJSONArray(String)
     */
    @Test
    public void testGetKladrJSONArray() {
        JSONArray jsonArray =
            client.getKladrJSONArray(new KladrApiUrlBuilder().setContentType(KladrApiUrlBuilder.CONTENT_TYPE_CITY).setLimit(3).setQuery("мос").toString());

        assertEquals("JSONArray incorrect", expectedKladrCityJSONArray.toString(), jsonArray.toString());
    }

    /**
     * @see KladrApiClient#getKladrObjectList(String)
     */
    @Test
    public void testGetKladrObjectList() {
        String params =
            new KladrApiUrlBuilder().setContentType(KladrApiUrlBuilder.CONTENT_TYPE_STREET).setCityId("7700000000000").setLimit(3).setQuery("ново").toString();

        List<KladrObject> streetList = client.getKladrObjectList(params);

        assertEquals("Kladr object list incorrect", streetList, Arrays.asList(exprectedKladrStreets));
    }

    /**
     * @see KladrApiClient#extractKladrObjectListNames(java.util.List<cyclone.tools.kladrapi.client.KladrObject>)
     */
    @Test
    public void testExtractKladrObjectListNames() {
        List<String> cityNames = KladrApiClient.extractKladrObjectListNames(Arrays.asList(expectedKladrCities));
        assertEquals("Extract names incorrect", cityNames,
                     Arrays.asList(new String[] { "Москва", "Мосальск", "Московский" }));
    }
}
