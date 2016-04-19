package servlets;

import beans.BeansHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Денис on 05.02.2016.
 */
@WebServlet(name = "LogoutServlet", urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().setAttribute(BeansHelper.USER_ACCOUNT_SESSION_KEY, null);
        request.getSession().setAttribute(BeansHelper.VK_TOKEN, null);
        request.getSession().setAttribute(BeansHelper.VK_AUTHORIZATION_KEY, null);
        request.getSession().setAttribute(BeansHelper.VK_CODE, null);
        response.sendRedirect("/unc-project/index");
    }
}
