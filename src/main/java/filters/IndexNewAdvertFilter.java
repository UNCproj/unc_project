package filters;

import beans.AdvertsManager;
import beans.ElasticSearchManager;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.index.IndexResponse;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

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
//            Enumeration<String> parameterNames = req.getParameterNames();
//            AdvertBean advertBean = new AdvertBean();
//
//            while (parameterNames.hasMoreElements()) {
//                String parameterName = parameterNames.nextElement();
//                String parameterValue = req.getParameter(parameterName);
//
//                advertBean.setAttribute(parameterName, parameterValue);
//            }
//
//            HashMap<String, String> attrsPseudonims = new HashMap<>();
//            attrsPseudonims.put("object_id", "id");
//            attrsPseudonims.put("object_name", "name");
//            attrsPseudonims.put("type_id", "category");
//
//            final Gson gson = new GsonBuilder()
//                    .setPrettyPrinting()
//                    .registerTypeAdapter(AdvertBean.class, new AdvertSerializer(attrsPseudonims))
//                    .create();
//
//            String advertJson = gson.toJson(advertBean);
//
//            IndexRequest indexRequest = new IndexRequest("adverts", "advert");
//            indexRequest.source(advertJson);
//
            IndexResponse response = null;
//            try {
//                response = elasticSearchManager
//                        .getClient()
//                        .index(indexRequest)
//                        .get();
//            } catch (InterruptedException | ExecutionException  e) {
//                throw new ServletException(e);
//            }
//
            RefreshRequest refreshRequest = new RefreshRequest("adverts");

            elasticSearchManager.reindex();

            elasticSearchManager
                    .getClient()
                    .admin()
                    .indices()
                    .refresh(refreshRequest)
                    .actionGet();

            String id = response.getId();
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }
}
