package filters;

import beans.AdvertBean;
import beans.AdvertsManager;
import beans.ElasticSearchManager;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Created by Денис on 20.04.2016.
 */
@WebFilter(filterName = "IndexNewAdvertFilter", urlPatterns = {"/uncadd", "/uncupdate", "/upload"})
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

        if (type == null || type.equals("advert")) {
            AdvertBean advertBean = new AdvertBean();

            elasticSearchManager.reindex();
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }
}
