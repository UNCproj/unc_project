package beans;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Денис on 26.04.2016.
 */
@Stateless
public class RecommenderBean {
    @EJB
    private AdvertsSearchBean advertsSearchBean;

    public void calculateRelated() {
        SearchResponse searchResponse = advertsSearchBean.getAllAdverts();

    }

    public ArrayList<AdvertBean> getRelatedAdverts(String advertId, int count) {
        ArrayList<AdvertBean> relatedAdverts = new ArrayList<>();

        //get related adverts ids
        SearchResponse searchResponse = advertsSearchBean.searchById(advertId);
        SearchHit hit = searchResponse.getHits().getHits()[0];
        String[] relatedIds = (String[]) hit.getSource().get("related");

        //get related adverts
        searchResponse = advertsSearchBean.searchByIds(relatedIds);

        for (SearchHit searchHit: searchResponse.getHits()) {
            relatedAdverts.add(new AdvertBean((HashMap) searchHit.getSource()));
        }

        return relatedAdverts;
    }
}
