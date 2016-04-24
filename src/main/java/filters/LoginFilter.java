package filters;

import beans.UserAccountBean;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by alex on 01.04.2016.
 */
@WebFilter(urlPatterns = {"/unc_add.jsp","/chat.jsp"})
//@WebFilter(urlPatterns = {"/chat.jsp"})
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
            httpResponse.sendRedirect("/unc-project/reg-and-login.jsp");
        }else{
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
