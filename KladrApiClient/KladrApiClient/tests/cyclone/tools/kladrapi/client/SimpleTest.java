package cyclone.tools.kladrapi.client;

import cyclone.tools.kladrapi.client.KladrApiClient;
import cyclone.tools.kladrapi.client.KladrApiUrlBuilder;
import cyclone.tools.kladrapi.client.KladrObject;

import java.io.IOException;

import java.io.PrintStream;

import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.json.JSONArray;

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

        KladrApiClient client = new KladrApiClient(KladrApiClientTest.DEMO_TOKEN, KladrApiClientTest.DEMO_KEY);
        client.setProxy(null, null);

        List<KladrObject> cityList = client.getKladrCities("мос", 3, true);
        System.out.println("cityList = " + cityList);
        for (KladrObject ko : cityList) {
            printKladrObject(ko, System.out);
        }
        System.out.println("-----------------------");

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
