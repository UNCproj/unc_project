package filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Created by Денис on 23.04.2016.
 */
@WebFilter(filterName = "DeleteAdvertFromIndexFilter", servletNames = "ModerServlet")
public class DeleteAdvertFromIndexFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        chain.doFilter(req, resp);


    }

    public void init(FilterConfig config) throws ServletException {

    }

}
