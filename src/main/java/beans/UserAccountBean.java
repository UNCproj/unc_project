package beans;

import db.DataSource;
import db.SQLQueriesHelper;

import javax.ejb.Local;
import javax.ejb.Stateful;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;
import unc.helpers.Crypt2;

/**
 * Created by Денис on 15.12.2015.
 */
@Stateful
@Local
public class UserAccountBean implements UserAccount {
    transient Logger log = Logger.getLogger("uab_log");
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
    private boolean isModer=false;
    private boolean isAdmin=false;
    private boolean isInvalid=false;
    @Override
    public void initialize(String id, String login, String password, String email, String userPicFile, boolean isLoggedIn, Date currentLoginDate) {
        this.setId(id);
        this.login = login;
        this.password = password;
        this.email = email;
        this.userPicFile = userPicFile;
        this.isLoggedIn = isLoggedIn;
        lastLoginDate = currentLoginDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && ((o.getClass().equals(UserAccountBean.class) && ((UserAccountBean)o).getId().equals(id)) || (o.getClass().equals(String.class) && ((String)o).equals(id)))){
            return true;
        }
        else {
            return false;
        }
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
        loadMainInfo(connection);
        loadPersonalInfo(connection);
        if (connection != null){
            connection.close();
        }
    }

    private void loadMainInfo(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery(SQLQueriesHelper.selectFullObjectInformationById(new String[]{ SQLQueriesHelper.USER_TYPE_ID },
                new String[]{ getId()}));
        while (results.next()) {
            if (results.getString("attr_name").equals(SQLQueriesHelper.LOGIN_ATTR)) {
                this.login = results.getString("value");
            } else if (results.getString("attr_name").equals(SQLQueriesHelper.PASSWORD_ATTR)) {
                this.password = results.getString("value");
            } else if (results.getString("attr_name").equals(SQLQueriesHelper.EMAIL_ATTR)) {
                this.email = Crypt2.decrypt(results.getString("value"));
            } else if (results.getString("attr_name").equals(SQLQueriesHelper.USER_PIC_FILE_ATTR)) {
                this.userPicFile = results.getString("value");
            } else if (results.getString("attr_name").equals(SQLQueriesHelper.MODER_ATTR)){
                if (results.getString("value") != null)
                this.isModer = results.getString("value").equals("true");
            } else if (results.getString("attr_name").equals(SQLQueriesHelper.ADMIN_ATTR)){
                if (results.getString("value") != null)
                this.isAdmin = results.getString("value").equals("true");
            }  else if (results.getString("attr_name").equals("is_invalid")){
                if (results.getString("value") != null)
                this.isInvalid = results.getString("value").equals("true");
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
                this.firstName = Crypt2.decrypt(results.getString("value"));
            } else if (results.getString("attr_name").equals(SQLQueriesHelper.SECOND_NAME_ATTR)) {
                this.secondName = Crypt2.decrypt(results.getString("value"));
            } else if (results.getString("attr_name").equals(SQLQueriesHelper.SURNAME_ATTR)) {
                this.surname = Crypt2.decrypt(results.getString("value"));
            } else if (results.getString("attr_name").equals(SQLQueriesHelper.PHONE_ATTR)) {
                this.phone = Crypt2.decrypt(results.getString("value"));
            } else if (results.getString("attr_name").equals(SQLQueriesHelper.STREET_AND_HOUSE_NAME_ATTR)) {
                this.streetAndHouse = Crypt2.decrypt(results.getString("value"));
            } else if (results.getString("attr_name").equals(SQLQueriesHelper.CITY_ADVERT_ATTR)) {
                this.city = Crypt2.decrypt(results.getString("value"));
            } else if (results.getString("attr_name").equals(SQLQueriesHelper.COUNTRY_ATTR)) {
                this.country = Crypt2.decrypt(results.getString("value"));
            } else if (results.getString("attr_name").equals(SQLQueriesHelper.ADDITIONAL_INFO_ATTR)) {
                this.additionalInfo = Crypt2.decrypt(results.getString("value"));
            } 
        }
    }

    /**
     * @return the isModer
     */
    public boolean isIsModer() {
        return isModer;
    }
    
    public void setisIsModer(boolean value) {
        isModer = value;
    }

    /**
     * @return the isAdmin
     */
    public boolean isIsAdmin() {
        return isAdmin;
    }
    
    public void setisIsAdmin(boolean value) {
        isAdmin = value;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the isInvalid
     */
    public boolean isIsInvalid() {
        return isInvalid;
    }

    /**
     * @param isInvalid the isInvalid to set
     */
    public void setIsInvalid(boolean isInvalid) {
        this.isInvalid = isInvalid;
    }

    public ArrayList<AdvertBean> getBookmarks() throws PropertyVetoException, SQLException, IOException {
        ArrayList<AdvertBean> adverts = new ArrayList<>();
        ArrayList<String> bookmarksIDs = new ArrayList<>();

        try (Connection connection = DataSource.getInstance().getConnection()) {
            Statement statement = connection.createStatement();
            String[] types = new String[1];
            types[0] = SQLQueriesHelper.USER_TYPE_ID;
            ResultSet results = statement.executeQuery(
                    SQLQueriesHelper.selectFullObjectInformationByName(types, login));

            while (results.next()) {
                if (results.getString("attr_name").equals(SQLQueriesHelper.BOOKMARK_ATTR)) {
                    String bookmarkId = results.getString("value");
                    bookmarksIDs.add(bookmarkId);
                }
            }

            String[] bIDsStr = Arrays.copyOf(bookmarksIDs.toArray(), bookmarksIDs.size(), String[].class);
            Statement statement2 = connection.createStatement();
            results = statement2.executeQuery(
                    SQLQueriesHelper.selectFullObjectInformationById(
                            null,
                            bIDsStr
                    )
            );

            while (results.next()) {
                String currentAdvertId = results.getString("object_id");

                int currentAdvertIndex = -1;
                for (int i = 0; i < adverts.size(); i++) {
                    AdvertBean currentAdvertBean = adverts.get(i);

                    if (currentAdvertBean.getAttribute("id") != null &&
                            currentAdvertBean.getAttribute("id").equals(currentAdvertId)) {
                        currentAdvertIndex = i;
                        break;
                    }
                }

                if (currentAdvertIndex == -1) {
                    adverts.add(new AdvertBean());
                    currentAdvertIndex = adverts.size() - 1;
                    adverts.get(currentAdvertIndex).setId(results.getString("object_id"));
                    adverts.get(currentAdvertIndex).setName(results.getString("object_name"));
                }

                adverts.get(currentAdvertIndex).setAttribute(results.getString("attr_name"), results.getString("value"));

            }
        }

        return adverts;
    }

    public void addBookmark(String bookmarkId) throws SQLException, IOException, PropertyVetoException {
        try (Connection connection = DataSource.getInstance().getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(
                    SQLQueriesHelper.newReference(getId(), bookmarkId, SQLQueriesHelper.BOOKMARK_ATTR_ID)
            );
        }
    }

    public void deleteBookmark(String bookmarkId) throws PropertyVetoException, SQLException, IOException {
        try (Connection connection = DataSource.getInstance().getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate(
                    SQLQueriesHelper.deleteReference(getId(), SQLQueriesHelper.BOOKMARK_ATTR_ID, bookmarkId)
            );
        }
    }

    public boolean isInBookmarks(String advertId) throws PropertyVetoException, IOException, SQLException {
        ArrayList<AdvertBean> bookmarks = getBookmarks();

        return bookmarks.stream().anyMatch(advertBean -> advertBean.getId().equals(advertId));
    }
}
