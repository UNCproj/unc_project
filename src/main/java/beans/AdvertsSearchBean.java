package beans;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import db.DataSource;
import db.SQLQueriesHelper;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.mapper.StrictDynamicMappingException;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.jboss.as.clustering.ManagedScheduledExecutorService;
import schedule.tasks.IndexAdvertsTask;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by Денис on 04.04.2016.
 */
@Stateless
public class AdvertsSearchBean implements AdvertsSearch {
    @EJB
    private ESClientsProvider esClientsProvider;
    private ManagedScheduledExecutorService executorService;

    @Override
    public SearchResponse advertsSearch(QueryBuilder query,
                                         String adCategoryId, String adCategoryName, String sortingParam,
                                         String sortingOrder, String additionalAttributes[],
                                         String adsStartingNum, String adsCount)
            throws PropertyVetoException, SQLException, IOException {
        if (adCategoryId == null && adCategoryName != null) {
            adCategoryId = getAdCategoryId(adCategoryName);
        }

        ArrayList<String> advertsTypes = getSubCategories(adCategoryId);

        if (advertsTypes != null) {
            advertsTypes.add(adCategoryId);
        }

        Client client = esClientsProvider.getClient();

        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("adverts")
                .setSearchType(SearchType.DEFAULT);

        if (advertsTypes != null && advertsTypes.size() > 0) {
            searchRequestBuilder.setPostFilter(QueryBuilders.termsQuery("category", advertsTypes));
        }

        if (additionalAttributes != null) {
            Gson inputAttrsJson = new GsonBuilder().setPrettyPrinting().create();

            Type type = Object.class;

            for (String additionalAttribute : additionalAttributes) {
                LinkedTreeMap attributeInfo = inputAttrsJson.fromJson(additionalAttribute, type);

                String attrName = (String) attributeInfo.get("name");
                String attrType = (String) attributeInfo.get("type");
                ArrayList<String> attrValues = (ArrayList<String>) attributeInfo.get("values");

                if (attrValues == null || attrValues.size() == 0) {
                    continue;
                }

                if (attrType != null) {
                    switch (attrType) {
                        case "discrete_multi":
                            List<String> filteredValues = attrValues.stream()
                                                                    .filter(val -> val != null && !val.equals(""))
                                                                    .collect(Collectors.toList());

                            if (filteredValues != null && filteredValues.size() != 0) {
                                searchRequestBuilder.setPostFilter(
                                        QueryBuilders.termsQuery(attrName, filteredValues)
                                );
                            }
                            break;

                        case "continous":
                            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery(attrName);

                            if (!Objects.equals(attrValues.get(0), "")) {
                                rangeQuery = rangeQuery.from(attrValues.get(0));
                            }

                            if (!Objects.equals(attrValues.get(1), "")) {
                                rangeQuery = rangeQuery.to(attrValues.get(1));
                            }

                            searchRequestBuilder.setPostFilter(rangeQuery);
                            break;
                    }
                }
            }
        }


        if (query != null) {
            searchRequestBuilder.setQuery(query);
        }

        searchRequestBuilder.setFrom(adsStartingNum == null ? 0 : Integer.parseInt(adsStartingNum));

        if (adsCount != null) {
            searchRequestBuilder.setSize(Integer.parseInt(adsCount));
        }

        searchRequestBuilder.addSort(sortingParam == null ? "name" : sortingParam,
                (sortingOrder != null && sortingOrder.equals("desc")) ? SortOrder.DESC : SortOrder.ASC);


        return searchRequestBuilder.execute().actionGet();
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
    public void runIndexer() {
        IndexAdvertsTask indexAdvertsTask = new IndexAdvertsTask();
        executorService.scheduleAtFixedRate(indexAdvertsTask, 0L, 6L, TimeUnit.HOURS);
    }

    private ArrayList<String> getSubCategories(String adCategoryId) throws PropertyVetoException, SQLException, IOException {
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

    private String getAdCategoryId(String adCategoryName) throws SQLException, IOException, PropertyVetoException {
        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement()
        ) {
            try (
                ResultSet adCatIdResults = statement.executeQuery(
                        SQLQueriesHelper.getTypeIdByTypeName(adCategoryName)
                )) {
                    adCatIdResults.next();
                    return adCatIdResults.getString("id");
                }
        }
    }
}
