package servlets;

import beans.BeansHelper;
import beans.UserAccountBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Денис on 15.12.2015.
 */
@WebServlet(name = "indexServlet", urlPatterns = "/index")
public class IndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserAccountBean userAccountBean = (UserAccountBean)req.getSession().getAttribute(BeansHelper.USER_ACCOUNT_SESSION_KEY);

        if (userAccountBean == null || !userAccountBean.isLoggedIn()) {
            req.getRequestDispatcher("/reg-and-login.jsp").forward(req, resp);
        }
        else {
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }
    }
}
