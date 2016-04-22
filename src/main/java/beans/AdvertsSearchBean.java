package beans;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.sort.SortOrder;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Денис on 04.04.2016.
 */
@Stateless
public class AdvertsSearchBean {
    @EJB
    private ElasticSearchManager elasticSearchManager;
    @EJB
    private AdvertsManager advertsManager;

    public SearchResponse advertsSearch(QueryBuilder query,
                                         String adCategoryId, String adCategoryName, String sortingParam,
                                         String sortingOrder, String additionalAttributes[],
                                         String adsStartingNum, String adsCount)
            throws PropertyVetoException, SQLException, IOException {
        if (adCategoryId == null && adCategoryName != null) {
            adCategoryId = advertsManager.getAdCategoryId(adCategoryName);
        }

        ArrayList<String> advertsTypes = advertsManager.getSubCategories(adCategoryId);

        if (advertsTypes != null) {
            advertsTypes.add(adCategoryId);
        }

        Client client = elasticSearchManager.getClient();

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
}
