package beans;

import db.DataSource;
import db.SQLQueriesHelper;

import javax.ejb.Stateless;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Денис on 20.04.2016.
 */
@Stateless
public class AdvertsManagerBean implements AdvertsManager {
    @Override
    public ArrayList<AdvertBean> getAdverts(String adCategoryId, boolean isOnlyValid) throws PropertyVetoException, SQLException, IOException {
        ArrayList<AdvertBean> adverts = new ArrayList<>();

        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement()
        ) {
            try (ResultSet childrenTypesResults = statement.executeQuery(SQLQueriesHelper.getTypeChildren(adCategoryId))) {
                ArrayList<String> categories = new ArrayList<>();

                while (childrenTypesResults.next()) {
                    categories.add(childrenTypesResults.getString("ot_id"));
                }

                try (
                        ResultSet advListResults = statement.executeQuery(
                                SQLQueriesHelper.getAdvertsList(
                                        categories.toArray(new String[0]), null, null, null, null, null
                                )
                        )
                ) {
                    while (advListResults.next()) {
                        if (isOnlyValid && Boolean.parseBoolean(advListResults.getString("is_invalid"))) {
                            continue;
                        }

                        AdvertBean adv = new AdvertBean();

                        adv.setAttribute("object_id", advListResults.getString("object_id"));
                        adv.setAttribute("object_name", advListResults.getString("object_name"));
                        adv.setAttribute("type_id", advListResults.getString("type_id"));

                        adv.setAttribute(SQLQueriesHelper.DESCRIPTION_ATTR,
                                advListResults.getString(SQLQueriesHelper.DESCRIPTION_ATTR));

                        adv.setAttribute(SQLQueriesHelper.CITY_ADVERT_ATTR,
                                advListResults.getString(SQLQueriesHelper.CITY_ADVERT_ATTR));

                        adv.setAttribute("pic", advListResults.getString("pic"));

                        adv.setAttribute(SQLQueriesHelper.PRICE_ADVERT_ATTR,
                                advListResults.getString(SQLQueriesHelper.PRICE_ADVERT_ATTR));

                        adv.setAttribute(SQLQueriesHelper.REG_DATE_ATTR,
                                advListResults.getString(SQLQueriesHelper.REG_DATE_ATTR));

                        adverts.add(adv);
                    }
                }
            }
        }

        return adverts;
    }

    @Override
    public ArrayList<String>[] getAllCategories() throws PropertyVetoException, SQLException, IOException {
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement()
        ) {
            try (ResultSet childrenTypesResults = statement.executeQuery(
                    SQLQueriesHelper.getTypeChildren(SQLQueriesHelper.ADVERT_TYPE_ID)
            )) {
                ArrayList<String> categoriesIds = new ArrayList<>();
                ArrayList<String> categoriesNames = new ArrayList<>();

                while (childrenTypesResults.next()) {
                    categoriesIds.add(childrenTypesResults.getString("ot_id"));
                    categoriesNames.add(childrenTypesResults.getString("ot_name"));
                }

                return new ArrayList[]{categoriesIds, categoriesNames};
            }
        }
    }

    @Override
    public ArrayList<String[]> getAttributes(String adCategoryId) throws IOException, SQLException, PropertyVetoException {
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement()
        ) {
            try (ResultSet attrResults = statement.executeQuery(
                    SQLQueriesHelper.getAllAttributes(adCategoryId == null ? SQLQueriesHelper.ADVERT_TYPE_ID : adCategoryId)
            )) {
                ArrayList<String[]> attributes = new ArrayList<>();

                while (attrResults.next()) {
                    attributes.add(
                            new String[] {
                                    attrResults.getString("attr_name"),
                                    attrResults.getString("attr_name_ru"),
                                    attrResults.getString("attr_type"),
                                    attrResults.getString("search_group")
                            }
                    );
                }

                return attributes;
            }
        }
    }

    @Override
    public ArrayList<String[]> getFirstLevelCategories(String adCategoryId, String adCategoryName) throws SQLException, IOException, PropertyVetoException {
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement()
        ) {
            if (adCategoryId == null) {
                adCategoryId = getAdCategoryId(adCategoryName);
            }

            try (ResultSet childrenTypesResults = statement.executeQuery(
                    SQLQueriesHelper.getTypeFirstLevelChildren(adCategoryId != null ?
                            adCategoryId :
                            SQLQueriesHelper.ADVERT_TYPE_ID)
            )) {
                ArrayList<String[]> categories = new ArrayList<>();

                while (childrenTypesResults.next()) {
                    categories.add(new String[] {childrenTypesResults.getString("ot_id"),
                            childrenTypesResults.getString("ot_name")});
                }

                return  categories;
            }
        }
    }

    @Override
    public ArrayList<String> getParentCategories(String adCategoryId, String adCategoryName) throws SQLException, IOException, PropertyVetoException {
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement()
        ) {
            if (adCategoryId == null) {
                adCategoryId = getAdCategoryId(adCategoryName);
            }

            try (ResultSet childrenTypesResults = statement.executeQuery(
                    SQLQueriesHelper.selectCategories(adCategoryId != null ?
                            adCategoryId :
                            SQLQueriesHelper.ADVERT_TYPE_ID)
            )) {
                ArrayList<String> parentsCategories = new ArrayList<>();

                while (childrenTypesResults.next()) {
                    parentsCategories.add(childrenTypesResults.getString("ot_name"));
                }

                return parentsCategories;
            }
        }
    }

    @Override
    public ArrayList<String> getSubCategories(String adCategoryId) throws PropertyVetoException, SQLException, IOException {
        ArrayList<String> advertsTypes = new ArrayList<>();

        if (adCategoryId == null) {
            return null;
        }

        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement()
        ) {
            try (ResultSet childrenTypesResults = statement.executeQuery(SQLQueriesHelper.getTypeChildren(adCategoryId))) {

                while (childrenTypesResults.next()) {
                    advertsTypes.add(childrenTypesResults.getString("ot_id"));
                }
            }
        }

        return advertsTypes;
    }

    @Override
    public String getAdCategoryId(String adCategoryName) throws SQLException, IOException, PropertyVetoException {
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement()
        ) {
            try (
                    ResultSet adCatIdResults = statement.executeQuery(
                            SQLQueriesHelper.getTypeIdByTypeName(adCategoryName)
                    )
            ) {
                adCatIdResults.next();
                return adCatIdResults.getString("id");
            }
        }
    }
}
