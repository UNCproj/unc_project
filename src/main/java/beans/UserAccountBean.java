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
    private String userPicFile;
    private Date lastLoginDate;
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

}
