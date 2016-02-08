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
 * Created by Денис on 05.02.2016.
 */

@WebServlet(name = "accountServlet", urlPatterns = "/account")
public class AccountServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestedLogin = req.getParameter("accountLogin");

        if (requestedLogin == null) {
            //TODO: 404 page
            return;
        }

        UserAccountBean userAccount = (UserAccountBean) req.getSession().getAttribute(BeansHelper.USER_ACCOUNT_SESSION_KEY);

        if (userAccount == null) {
            req.getRequestDispatcher("/reg-and-login.jsp").forward(req, resp);
            return;
        }

        if (requestedLogin.equals(userAccount.getLogin())) {
            req.getRequestDispatcher("/accountSettings.jsp").forward(req, resp);
        }
        else {
            req.getRequestDispatcher("/account.jsp").forward(req, resp);
        }
    }
}
