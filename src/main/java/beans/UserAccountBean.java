package beans;

import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.servlet.ServletException;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import db.DataSource;
import db.SQLQueriesHelper;

/**
 * Created by Денис on 15.12.2015.
 */
@Stateful
@Local
public class UserAccountBean implements UserAccount {
    private String id;
    private String login;
    private String password;
    private String email;
    private Date lastLoginDate;
    private String userPicFile;
    private String firstName = null;
    private String secondName = null;
    private String surname = null;
    private String phone = null;
    private String streetAndHouse = null;
    private String city = null;
    private String country = null;
    private String additionalInfo = null;

    private boolean isLoggedIn = false;

    @Override
    public void initialize(String id, String login, String password, String email, String userPicFile, boolean isLoggedIn, Date currentLoginDate) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.email = email;
        this.userPicFile = userPicFile;
        this.isLoggedIn = isLoggedIn;
        lastLoginDate = currentLoginDate;
    }

    @Override
    public String getId() { return this.id; };

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getUserPicFile() {
        return userPicFile;
    }

    @Override
    public void setUserPicFile(String userPicFile) {
        this.userPicFile = userPicFile;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getSecondName() {
        return secondName;
    }

    @Override
    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    @Override
    public String getSurname() {
        return surname;
    }

    @Override
    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String getStreetAndHouse() {
        return streetAndHouse;
    }

    @Override
    public void setStreetAndHouse(String streetAndHouse) {
        this.streetAndHouse = streetAndHouse;
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
    public String getCountry() {
        return country;
    }

    @Override
    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String getAdditionalInfo() {
        return additionalInfo;
    }

    @Override
    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    @Override
    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    @Override
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    @Override
    public void setLogged(boolean isLogged) {
        isLoggedIn = isLogged;
    }

    public void preparePersonalInfo() throws PropertyVetoException, IOException, SQLException {
        if (firstName == null) {
            Connection connection = DataSource.getInstance().getConnection();
            loadPersonalInfo(connection);
        }
    }

    public void updateAllInfo() throws PropertyVetoException, SQLException, IOException {
        Connection connection = DataSource.getInstance().getConnection();
        loadPersonalInfo(connection);
        loadMainInfo(connection);
    }

    private void loadMainInfo(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery(SQLQueriesHelper.selectFullObjectInformationById(
                new String[]{ SQLQueriesHelper.USER_TYPE_ID },
                new String[]{ id }));

        while (results.next()) {
            if (results.getString("attr_name").equals(SQLQueriesHelper.LOGIN_ATTR)) {
                this.login = results.getString("value");
            } else if (results.getString("attr_name").equals(SQLQueriesHelper.PASSWORD_ATTR)) {
                this.password = results.getString("value");
            } else if (results.getString("attr_name").equals(SQLQueriesHelper.EMAIL_ATTR)) {
                this.email = results.getString("value");
            } else if (results.getString("attr_name").equals(SQLQueriesHelper.USER_PIC_FILE_ATTR)) {
                this.userPicFile = results.getString("value");
            }
        }
    }

    /**
     * Загружает персональные данные - от firstName до additionalInfo
     */
    private void loadPersonalInfo(Connection connection) throws PropertyVetoException, SQLException, IOException {
        Statement statement = connection.createStatement();
        String[] types = new String[1];
        types[0] = SQLQueriesHelper.USER_TYPE_ID;
        ResultSet results = statement.executeQuery(
                SQLQueriesHelper.selectFullObjectInformationByName(types, login));

        String attrName = null;
        while (results.next()) {
            attrName = results.getString("attr_name");

            if (results.getString("attr_name").equals(SQLQueriesHelper.FIRST_NAME_ATTR)) {
                this.firstName = results.getString("value");
            } else if (results.getString("attr_name").equals(SQLQueriesHelper.SECOND_NAME_ATTR)) {
                this.secondName = results.getString("value");
            } else if (results.getString("attr_name").equals(SQLQueriesHelper.SURNAME_ATTR)) {
                this.surname = results.getString("value");
            } else if (results.getString("attr_name").equals(SQLQueriesHelper.PHONE_ATTR)) {
                this.phone = results.getString("value");
            } else if (results.getString("attr_name").equals(SQLQueriesHelper.STREET_AND_HOUSE_NAME_ATTR)) {
                this.streetAndHouse = results.getString("value");
            } else if (results.getString("attr_name").equals(SQLQueriesHelper.CITY_ADVERT_ATTR)) {
                this.city = results.getString("value");
            } else if (results.getString("attr_name").equals(SQLQueriesHelper.COUNTRY_ATTR)) {
                this.country = results.getString("value");
            } else if (results.getString("attr_name").equals(SQLQueriesHelper.ADDITIONAL_INFO_ATTR)) {
                this.additionalInfo = results.getString("value");
            }
        }
    }
}
