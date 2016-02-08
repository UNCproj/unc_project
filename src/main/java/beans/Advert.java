package beans;

import javax.ejb.Local;

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
}
