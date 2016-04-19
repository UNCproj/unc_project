package beans;

import javax.ejb.Local;
import javax.ejb.Stateless;
import java.util.HashMap;

/**
 * Created by Денис on 18.01.2016.
 */
@Stateless
public class AdvertBean implements Advert {
    private String id;
    private String name;
    private String description;
    private String category;
    private String pic;
    private String price;
    private String city;
    private String registrationDate;
    private HashMap<String, String> additionalAttributesMap;

    public AdvertBean() {
        additionalAttributesMap = new HashMap<>();
    }

    public AdvertBean(String id, String name) {
        this();
        this.id = id;
        this.name = name;
    }

    public AdvertBean(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String Id) {
        this.id = Id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String getPic() {
        return pic;
    }

    @Override
    public void setPic(String pic) {
        this.pic = pic;
    }

    @Override
    public String getPrice() {
        return price;
    }

    @Override
    public void setPrice(String price) {
        this.price = ("000000000000000" + price).substring(price.length());
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String getRegistrationDate() {
        return registrationDate;
    }

    @Override
    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public String getAdditionalAttribute(String name) {
        return additionalAttributesMap.get(name);
    }

    @Override
    public void setAdditionalAttribute(String name, String value) {
        additionalAttributesMap.put(name, value);
    }
}
