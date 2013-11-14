package cyclone.tools.kladrapi.client;


import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;

import java.util.logging.Logger;

public class KladrApiUrlBuilder {
    public KladrApiUrlBuilder() {
        super();
    }

    public KladrApiUrlBuilder(String contentType, String query, Integer limit) {
        super();
        //        this.contentType = contentType;
        setContentType(contentType);
        this.query = query;
        this.limit = limit;
    }

    private static Logger _logger = Logger.getLogger(KladrApiUrlBuilder.class.getName());

    public static final String KLADR_API_BASE_URL = "http://kladr-api.ru/api.php";

    public static final String CONTENT_TYPE_REGION = "region", CONTENT_TYPE_DISTRICT = "district", CONTENT_TYPE_CITY =
        "city", CONTENT_TYPE_STREET = "street", CONTENT_TYPE_BUILDING = "building";

    private String contentType, query, regionId, districtId, cityId, buildingId; //, urlParams;
    private Integer limit;
    private Boolean withParent;

    public static String getBaseUrl(String token, String key) {
        if (token == null || key == null) {
            throw new NullPointerException("token and key are required");
        }
        return new StringBuilder(KLADR_API_BASE_URL).append("&token=").append(token).append("&key=").append(key).toString();
    }

    //    private String token, key;
    //
    //    public KladrApiUrlBuilder(String token, String key) {
    //        this.token = token;
    //        this.key = key;
    //    }
    //
    //    public KladrApiUrlBuilder setToken(String token) {
    //        this.token = token;
    //        return this;
    //    }
    //
    //    public KladrApiUrlBuilder setKey(String key) {
    //        this.key = key;
    //        return this;
    //    }

    //    public String getBaseUrl(String token, String key) {
    //        return new StringBuilder(KLADR_AP_URL).append("?token=").append(token).append("&key=").append(key).toString();
    //    }

    //    public String buildFullUrl() {
    //        return this.buildFullUrl(true);
    //    }
    //
    //    public String buildFullUrl(boolean encodeQuery) {
    //        StringBuilder builder = new StringBuilder(KLADR_API_BASE_URL);
    //        builder.append("?token=").append(token);
    //        builder.append("&key=").append(key);
    //        builder.append(this.build(encodeQuery));
    //
    //        return builder.toString();
    //    }
    //
    //    public KladrApiUrlBuilder setUrlParams(String urlParams) {
    //        this.urlParams = urlParams;
    //        return this;
    //    }

    public KladrApiUrlBuilder setContentType(String contentType) {
        if (!(contentType.equalsIgnoreCase(CONTENT_TYPE_DISTRICT) || contentType.equalsIgnoreCase(CONTENT_TYPE_CITY) ||
              contentType.equalsIgnoreCase(CONTENT_TYPE_STREET) || contentType.equalsIgnoreCase(CONTENT_TYPE_BUILDING))) {
            throw new NullPointerException(new StringBuilder("contentType must be one of the following: ").append(CONTENT_TYPE_REGION).append(CONTENT_TYPE_DISTRICT).append(", ").append(CONTENT_TYPE_CITY).append(", ").append(CONTENT_TYPE_STREET).append(", ").append(CONTENT_TYPE_BUILDING).toString());
        }

        this.contentType = contentType;
        return this;
    }

    public KladrApiUrlBuilder setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public KladrApiUrlBuilder setQuery(String query) {
        this.query = query;
        return this;
    }

    public KladrApiUrlBuilder setWithParent(boolean withParent) {
        this.withParent = withParent;
        return this;
    }

    public KladrApiUrlBuilder setCityId(String cityId) {
        this.cityId = cityId;
        return this;
    }

    public KladrApiUrlBuilder setRegionId(String regionId) {
        this.regionId = regionId;
        return this;
    }

    public KladrApiUrlBuilder setDistrictId(String districtId) {
        this.districtId = districtId;
        return this;
    }

    public KladrApiUrlBuilder setBuildingId(String buildingId) {
        this.buildingId = buildingId;
        return this;
    }

    public String encode(String string) throws UnsupportedEncodingException {
        return encode(string, "UTF-8");
    }

    public String encode(String string, String encoding) throws UnsupportedEncodingException {
        return URLEncoder.encode(string, encoding);
    }

    @Override
    public String toString() {
        return toString(true);
    }

    public String toString(boolean encodeQuery) {
        //        if (this.contentType == null && (this.urlParams == null || !this.urlParams.contains("contentType"))) {
        //            throw new NullPointerException("contentType is required");
        //        }
        if (this.contentType == null) {
            throw new NullPointerException("contentType is required");
        }
        //        if ((this.contentType.equals(KLADR_STREET) || (this.urlParams != null && !urlParams.contains("contentType=street")))
        //            && (this.cityId == null || (this.urlParams != null && !urlParams.contains("cityId")))
        //        ) {
        //            throw new NullPointerException("cityId is required when contentType is street");
        //        }
        if (this.contentType.equals(CONTENT_TYPE_STREET) && this.cityId == null) {
            throw new NullPointerException("cityId is required when contentType is street");
        }

        StringBuilder builder = new StringBuilder();
        //        if (this.contentType != null) {
        builder.append("&contentType=").append(this.contentType);
        //        }
        if (this.regionId != null) {
            builder.append("&regionId=").append(this.regionId);
        }
        if (this.districtId != null) {
            builder.append("&districtId=").append(this.districtId);
        }
        if (this.cityId != null) {
            builder.append("&cityId=").append(this.cityId);
        }
        if (this.buildingId != null) {
            builder.append("&buildingId=").append(this.buildingId);
        }

        if (this.withParent != null && this.withParent) {
            builder.append("&withParent=1");
        }
        if (this.limit != null) {
            builder.append("&limit=").append(this.limit);
        }
        //        if (this.urlParams != null) {
        //            builder.append(urlParams);
        //        }

        if (query != null) {
            if (encodeQuery) {
                try {
                    builder.append("&query=").append(encode(this.query));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    _logger.severe(e.getMessage());

                    builder.append("&query=").append(this.query);
                }
            } else {
                builder.append("&query=").append(this.query);
            }

        }
        return builder.toString();
    }
}
