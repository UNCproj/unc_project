package filters;

import beans.AdvertsManager;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Created by Денис on 20.04.2016.
 */
@WebFilter(filterName = "IndexNewAdvertFilter", servletNames = "AddServlet")
public class IndexNewAdvertFilter implements Filter {
    @EJB
    private AdvertsManager advertsManagerBean;

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        String type = req.getParameter("type");
        type.equals("");

        ArrayList<String>[] allCategories;

        try {
            allCategories = advertsManagerBean.getAllCategories();
        } catch (PropertyVetoException | SQLException e) {
            throw new ServletException(e);
        }

        Enumeration<String> parameterNames = req.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String parameterValue = req.getParameter(parameterNames.nextElement());
        }

        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }
}
