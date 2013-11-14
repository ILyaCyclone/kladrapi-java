package cyclone.tools.kladrapi.client;


import java.io.IOException;
import java.io.PrintStream;

import java.util.List;
import java.util.logging.LogManager;


public class SimpleTest {
    public SimpleTest() {
        super();
    }

    public static void main(String[] args) {
        try {
            LogManager.getLogManager().readConfiguration(SimpleTest.class.getResourceAsStream("logging.properties"));
        } catch (IOException e) {
            System.err.println("Could not setup logger configuration: " + e.toString());
        }
        
        // демонстрационные токен и ключ
        String token = "51dfe5d42fb2b43e3300006e";
        String key = "86a2c2a06f1b2451a87d05512cc2c3edfdf41969";

        // создаём экземпляр клиента API с токеном и ключом
        KladrApiClient client = new KladrApiClient(token, key);

        // запрашиваем названия населённых пунктов на "мос", максимум 5 результатов
        List<String> cityNameList = client.getCityNames("мос", 5);
        System.out.println("Города на \"мос\" = " + cityNameList);
        System.out.println();

        // запрашиваем КЛАДР-объекты улиц, названия которых начинаются на "ново", в городе "москва"
        // максимум 5 результатов
        List<KladrObject> streetList = client.getKladrStreetsByCityName("ново", "москва", 5);
        System.out.println("Улицы Москвы на \"ново\":");
        for (KladrObject street : streetList) {
            System.out.println(street.getType() + " " + street.getName());
        }

        //        KladrApiClient client = new KladrApiClient(KladrApiClientTest.DEMO_TOKEN, KladrApiClientTest.DEMO_KEY);
        //        client.setProxy(null, null);
        //
        //        List<KladrObject> cityList = client.getKladrCities("мос", 3, true);
        //        System.out.println("cityList = " + cityList);
        //        for (KladrObject ko : cityList) {
        //            printKladrObject(ko, System.out);
        //        }
        //        System.out.println("-----------------------");

        //        List<String> cityNameList = client.getCityNames("мос", 5);
        //        System.out.println("cityNameList = " + cityNameList);
        //        System.out.println("-----------------------");
        //
        //        List<KladrObject> streetList = client.getKladrStreetsByCityId("ново", "7700000000000", 3);
        //        System.out.println("streetList = " + streetList);
        //        for (KladrObject ko : streetList) {
        //            printKladrObject(ko, System.out);
        //        }
        //        System.out.println("-----------------------");
        //
        //        List<KladrObject> streetListByName = client.getKladrStreetsByCityName("пав", "мос", 10);
        //        System.out.println("streetListByName = " + streetListByName);
        //        System.out.println("-----------------------");
        //
        //
        //        List<KladrObject> districtList = client.getKladrDistricts("арх", 3);
        //        System.out.println("districtList = " + districtList);
        //        for (KladrObject district : districtList) {
        //            printKladrObject(district, System.out);
        //        }
        //
        //        List<String> districtNameList = client.getDistrictNames("арх", 3);
        //        System.out.println("districtNameList = " + districtNameList);
        //        System.out.println("-----------------------");
        //
        //        JSONArray jsonArray =
        //            client.getKladrJSONArray(new KladrApiUrlBuilder().setContentType(KladrApiUrlBuilder.CONTENT_TYPE_CITY).setLimit(3).setQuery("мос").toString());
        //        System.out.println(jsonArray);

    }

    public static void printKladrObject(KladrObject ko, PrintStream ps) {

        ps.println("Id = " + ko.getId());
        ps.println("Name = " + ko.getName());
        ps.println("Zip = " + ko.getZip());
        ps.println("Type = " + ko.getType());
        ps.println("TypeShort = " + ko.getTypeShort());
        ps.println("Okato = " + ko.getOkato());
        List<KladrObject> parents = ko.getParents();
        int i = 1;
        for (KladrObject parent : parents) {
            ps.println("-- parent " + (i++));
            printKladrObject(parent, ps);
        }
        System.out.println("-------");
    }
}
