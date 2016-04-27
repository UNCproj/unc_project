package beans;

import javax.ejb.Local;
import java.util.ArrayList;

/**
 * Created by Денис on 18.01.2016.
 */
@Local
public interface Advert {
    String getId();
    void setId(String Id);
    String getName();
    void setName(String name);
    String getDescription();
    void setDescription(String description);
    String getCategory();
    void setCategory(String category);
    String getPic();
    void setPic(String pic);
    String getPrice();
    void setPrice(String price);
    String getCity();
    void setCity(String city);
    String getRegistrationDate();
    void setRegistrationDate(String registrationDate);
    String getAdditionalAttribute(String name);
    void setAdditionalAttribute(String name, String value);
}
