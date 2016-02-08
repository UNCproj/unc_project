package beans;

import javax.ejb.Local;
import javax.ejb.Stateless;

/**
 * Created by Денис on 18.01.2016.
 */
@Stateless
@Local
public class AdvertBean implements Advert {
    private String id;
    private String name;
    private String description;

    public AdvertBean() {

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
}
