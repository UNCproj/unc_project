package filters;

import beans.UserAccountBean;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by alex on 01.04.2016.
 */
@WebFilter(urlPatterns = {"/unc_add.jsp","/unc_update.jsp","/chat.jsp","/bookmarks.jsp","/esmanagement", "/delete"})
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        UserAccountBean userAccountBean = (UserAccountBean) httpRequest.getSession().getAttribute("userAccount");

        if(userAccountBean==null){
            StringBuilder requestedURI = new StringBuilder("/unc-project/reg-and-login.jsp?from=");
            requestedURI.append(httpRequest.getRequestURI());

            if (httpRequest.getQueryString() != null) {
                requestedURI.append("?");
                requestedURI.append(httpRequest.getQueryString());
            }

            httpResponse.sendRedirect(requestedURI.toString());
        }else{
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
