package beans;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

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
                                         String adCategoryId, String adCategoryName)
            throws PropertyVetoException, SQLException, IOException {
        if (adCategoryId == null && adCategoryName != null) {
            adCategoryId = advertsManager.getAdCategoryId(adCategoryName);
        }

        final ArrayList<String> advertsTypes = advertsManager.getSubCategories(adCategoryId);

        if (advertsTypes != null) {
            advertsTypes.add(adCategoryId);
        }

        final Client client = elasticSearchManager.getClient();

        final SearchRequestBuilder searchRequestBuilder = client.prepareSearch("adverts")
                .setSearchType(SearchType.DEFAULT);

        if (advertsTypes != null && advertsTypes.size() > 0) {
            searchRequestBuilder.setPostFilter(QueryBuilders.termsQuery("category", advertsTypes));
        }


        if (query != null) {
            searchRequestBuilder.setQuery(query);
        }

        searchRequestBuilder.setSize(5000);

        return searchRequestBuilder.execute().actionGet();
    }

    public SearchResponse searchById(String advertId) {
        Client client = elasticSearchManager.getClient();

        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("adverts")
                .setSearchType(SearchType.DEFAULT);

        searchRequestBuilder.setPostFilter(QueryBuilders.termQuery("id", advertId));

        return searchRequestBuilder.execute().actionGet();
    }

    public SearchResponse searchByIds(String[] advertsIds) {
        Client client = elasticSearchManager.getClient();

        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("adverts")
                .setSearchType(SearchType.DEFAULT);

        searchRequestBuilder.setPostFilter(QueryBuilders.termsQuery("id", advertsIds));

        return searchRequestBuilder.execute().actionGet();
    }

    public SearchResponse getAllAdverts() {
        Client client = elasticSearchManager.getClient();

        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("adverts")
                .setSearchType(SearchType.DEFAULT);

        return searchRequestBuilder.execute().actionGet();
    }

    public SearchResponse geoQuery(double centerLatitude, double centerLongitude, double radius) {
        Client client = elasticSearchManager.getClient();

        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("adverts")
                .setSearchType(SearchType.DEFAULT);

        searchRequestBuilder.setQuery(
                QueryBuilders.geoDistanceQuery("map_coordinates")
                .point(centerLatitude, centerLongitude)
                .distance(radius, DistanceUnit.KILOMETERS)
                .optimizeBbox("memory")
                .geoDistance(GeoDistance.ARC)
        );

        return searchRequestBuilder.execute().actionGet();
    }
}
