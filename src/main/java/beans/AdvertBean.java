package beans;

import java.util.HashMap;

/**
 * Created by Денис on 18.01.2016.
 */
public class AdvertBean {
    private HashMap<String, String> attributesMap;

    public AdvertBean() {
        attributesMap = new HashMap<>();
    }

    public AdvertBean(String id, String name) {
        this();
        setId(id);
        setName(name);
    }

    public AdvertBean(String id, String name, String description) {
        this(id, name);
        setDescription(description);
    }

    public AdvertBean(HashMap<String, String> attributes) {
        attributesMap = attributes;
    }

    public String getId() {
        return getAttribute("id");
    }

    public void setId(String id) {
        setAttribute("id", id);
    }

    public String getName() {
        return getAttribute("name");
    }

    public void setName(String name) {
        setAttribute("name", name);
    }

    public String getDescription() {
        return getAttribute("description");
    }

    public void setDescription(String description) {
        setAttribute("description", description);
    }

    public String getAttribute(String name) {
        return attributesMap.get(name.toLowerCase());
    }

    public void setAttribute(String name, String value) {
        attributesMap.put(name.toLowerCase(), value);
    }

    public HashMap<String, String> getAttributesMap() {
        return attributesMap;
    }
}
