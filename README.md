## Java client for kladr-api.ru
See: http://kladr-api.ru/

Requires org.json JAR library.
See: http://www.json.org/ , https://code.google.com/p/org-json-java/downloads/list 

Oracle JDeveloper 11.1.2.3 project.

## Usage Example
```java
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
```
