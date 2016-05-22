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
import java.util.stream.Collectors;

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
            try (
                    ResultSet advListResults = statement.executeQuery(
                            SQLQueriesHelper.selectAdvertsListWithAllInfo("4")
                    )
            ) {
                while (advListResults.next()) {
                    final String currentAdvId = advListResults.getString("object_id");
                    AdvertBean currentAdvertBean = null;

                    for (AdvertBean advert : adverts) {
                        if (advert.getId().equals(currentAdvId)) {
                            currentAdvertBean = advert;
                            break;
                        }
                    }

                    if (currentAdvertBean == null) {
                        currentAdvertBean = new AdvertBean();
                        currentAdvertBean.setId(currentAdvId);
                        currentAdvertBean.setName(advListResults.getString("object_name"));
                        currentAdvertBean.setAttribute("category", advListResults.getString("type_id"));
                        adverts.add(currentAdvertBean);
                    }

                    currentAdvertBean.setAttribute(advListResults.getString("attr_name"),
                            advListResults.getString("value"));
                }

                if (isOnlyValid) {
                    adverts = (ArrayList<AdvertBean>) adverts
                            .stream()
                            .filter(advertBean ->
                                !Boolean.parseBoolean(advertBean.getAttribute("is_invalid"))
                            )
                            .collect(Collectors.toList());
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
