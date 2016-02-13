package beans;

import javax.ejb.Local;
import java.util.Date;

/**
 * Created by Денис on 15.12.2015.
 */
@Local
public interface UserAccount {
    void initialize(String id, String login, String password, String email, String user_pic_file, boolean isLoggedIn, Date currentLoginDate);
    String getId();
    String getLogin();
    void setLogin(String login);
    String getPassword();
    void setPassword(String password);
    String getEmail();
    void setEmail(String email);
    String getUserPicFile();
    void setUserPicFile(String userPicFile);
    String getFirstName();
    void setFirstName(String firstName);
    String getSecondName();
    void setSecondName(String secondName);
    String getSurname();
    void setSurname(String surname);
    String getPhone();
    void setPhone(String phone);
    String getStreetAndHouse();
    void setStreetAndHouse(String streetAndHouse);
    String getCity();
    void setCity(String city);
    String getCountry();
    void setCountry(String country);
    String getAdditionalInfo();
    void setAdditionalInfo(String additionalInfo);
    Date getLastLoginDate();
    boolean isLoggedIn();
    void setLogged(boolean isLogged);
}
