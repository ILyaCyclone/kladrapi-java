package cyclone.tools.kladrapi.client;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* POJO class to store KLADR object.
 * kladr-api service response result example:
 *
 * "id": "2800200000100",
 * "name": "Архара",
 * "zip": "676740",
 * "type": "Поселок городского типа",
 * "typeShort": "пгт",
 * "okato": "10205551000",
 * "parents": [
 *     {
 *         "id": "2800000000000",
 *         "name": "Амурская",
 *         "zip": "675000",
 *         "type": "Область",
 *         "typeShort": "обл",
 *         "okato": "10000000000",
 *     },
 *     {
 *         "id": "2800200000000",
 *         "name": "Архаринский",
 *         "zip": null,
 *         "type": "Район",
 *         "typeShort": "р-н",
 *         "okato": "10205000000",
 *     }
 * ]
 */

public class KladrObject {
    public KladrObject() {
        super();
    }

    private static Logger _logger = Logger.getLogger(KladrObject.class.getName());

    public KladrObject(String id, String name, String zip, String type, String typeShort, String okato,
                       List<KladrObject> parents) {
        super();

        if (id == null || id.length() == 0) {
            throw new NullPointerException("id can't be null");
        }
        if (name == null || name.length() == 0) {
            throw new NullPointerException("name can't be null");
        }

        this.id = id;
        this.name = name;
        this.zip = zip;
        this.type = type;
        this.typeShort = typeShort;
        this.okato = okato;
        this.parents = parents;
    }

    public KladrObject(String id, String name, String zip, String type, String typeShort, String okato) {
        this(id, name, zip, type, typeShort, okato, null);
    }


    public KladrObject(JSONObject kladrJSONObject) {
        super();
        try {
            this.id = kladrJSONObject.getString("id");
        } catch (JSONException e) {
            throw new NullPointerException("id can't be null");
        }

        try {
            this.name = kladrJSONObject.getString("name");
        } catch (JSONException e) {
            throw new NullPointerException("name can't be null");
        }

        this.zip = kladrJSONObject.optString("zip");
        this.type = kladrJSONObject.optString("type");
        this.typeShort = kladrJSONObject.optString("typeShort");
        this.okato = kladrJSONObject.optString("okato");

        List<KladrObject> parents = new ArrayList<KladrObject>();
        JSONArray parentArray = kladrJSONObject.optJSONArray("parents");
        if (parentArray != null) {
            for (int i = 0; i < parentArray.length(); i++) {
                try {
                    JSONObject parentObject = parentArray.getJSONObject(i);
                    KladrObject parentKladrObject = new KladrObject(parentObject);
                    parents.add(parentKladrObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                    _logger.severe(e.getMessage());
                }
            }
            this.parents = parents;
        }
    }

    private String id, name, zip, type, typeShort, okato;
    private List<KladrObject> parents;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.typeShort.equals("проезд") || this.typeShort.equals("км")) {
            // exclusion
            builder.append(name).append(" ").append(typeShort);
        } else {
            builder.append(typeShort).append(". ").append(name);
        }

        // or easier way
        // builder.append(this.name).append(" ").append(typeShort);

        if (parents != null && !parents.isEmpty()) {
            int parentsSize = parents.size();
            builder.append(" (");
            for (int i = 0; i < parentsSize; i++) {
                KladrObject parent = parents.get(parentsSize - i - 1);
                builder.append(parent.getName()).append(" ").append(parent.getTypeShort());
                builder.append(".");
                if (i != parentsSize - 1) {
                    builder.append(", ");
                }
            }
            builder.append(")");
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof KladrObject)) {
            return false;
        }
        final KladrObject other = (KladrObject)object;
        if (!(id == null ? other.id == null : id.equals(other.id))) {
            return false;
        }
        if (!(name == null ? other.name == null : name.equals(other.name))) {
            return false;
        }
        if (!(zip == null ? other.zip == null : zip.equals(other.zip))) {
            return false;
        }
        if (!(type == null ? other.type == null : type.equals(other.type))) {
            return false;
        }
        if (!(typeShort == null ? other.typeShort == null : typeShort.equals(other.typeShort))) {
            return false;
        }
        if (!(okato == null ? other.okato == null : okato.equals(other.okato))) {
            return false;
        }
        if (!(this.getParents() == null ? other.getParents() == null : this.getParents().equals(other.getParents()))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + ((id == null) ? 0 : id.hashCode());
        result = PRIME * result + ((name == null) ? 0 : name.hashCode());
        result = PRIME * result + ((zip == null) ? 0 : zip.hashCode());
        result = PRIME * result + ((type == null) ? 0 : type.hashCode());
        result = PRIME * result + ((typeShort == null) ? 0 : typeShort.hashCode());
        result = PRIME * result + ((okato == null) ? 0 : okato.hashCode());
        result = PRIME * result + ((parents == null) ? 0 : parents.hashCode());
        return result;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getZip() {
        return zip;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setTypeShort(String typeShort) {
        this.typeShort = typeShort;
    }

    public String getTypeShort() {
        return typeShort;
    }

    public void setOkato(String okato) {
        this.okato = okato;
    }

    public String getOkato() {
        return okato;
    }

    public void setParents(List<KladrObject> parents) {
        this.parents = parents;
    }

    public List<KladrObject> getParents() {
        return parents != null ? parents : Collections.<KladrObject>emptyList();
    }

    public String toJSONObjectString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("\"id\":\"").append(id).append("\"");
        if (zip != null) {
            builder.append(",\"zip\":\"").append(zip).append("\"");
        } else {
            builder.append(",\"zip\":null");
        }
        builder.append(",\"typeShort\":\"").append(typeShort).append("\"");
        builder.append(",\"name\":\"").append(name).append("\"");
        if (okato != null) {
            builder.append(",\"okato\":\"").append(okato).append("\"");
        } else {
            builder.append(",\"okato\":null");
        }
        builder.append(",\"type\":\"").append(type).append("\"");
        builder.append("}");
        return builder.toString();
    }
}
