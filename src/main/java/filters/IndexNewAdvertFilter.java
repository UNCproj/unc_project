package filters;

import beans.AdvertBean;
import beans.AdvertsManager;
import beans.ElasticSearchManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;
import serializers.AdvertSerializer;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * Created by Денис on 20.04.2016.
 */
@WebFilter(filterName = "IndexNewAdvertFilter", urlPatterns = {"/uncadd"})
public class IndexNewAdvertFilter implements Filter {
    @EJB
    private AdvertsManager advertsManagerBean;
    @EJB
    private ElasticSearchManager elasticSearchManager;

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        chain.doFilter(req, resp);

        String type = req.getParameter("type");

        if (type.equals("advert")) {
            Enumeration<String> parameterNames = req.getParameterNames();
            AdvertBean advertBean = new AdvertBean();

            while (parameterNames.hasMoreElements()) {
                String parameterName = parameterNames.nextElement();
                String parameterValue = req.getParameter(parameterName);

                advertBean.setAttribute(parameterName, parameterValue);
            }

            HashMap<String, String> attrsPseudonims = new HashMap<>();
            attrsPseudonims.put("object_id", "id");
            attrsPseudonims.put("object_name", "name");
            attrsPseudonims.put("type_id", "category");

            final Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(AdvertBean.class, new AdvertSerializer(attrsPseudonims))
                    .create();

            final String advertJson = gson.toJson(advertBean);

            BulkRequestBuilder bulkRequest = elasticSearchManager
                    .getClient()
                    .prepareBulk();

            bulkRequest.add(
                    elasticSearchManager
                    .getClient()
                    .prepareIndex("adverts", "advert")
                    .setSource(advertJson)
            );

            final BulkResponse bulkResponse = bulkRequest.execute().actionGet();

            if (bulkResponse.hasFailures()) {
                int a = 5;
                a = a - 1;
            }

            IndexResponse response =
                    elasticSearchManager
                    .getClient()
                    .prepareIndex("adverts", "advert")
                    .setSource(advertJson)
                    .get();

            elasticSearchManager.reindex();
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }
}
