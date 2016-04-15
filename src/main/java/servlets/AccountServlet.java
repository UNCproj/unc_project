package servlets;

import beans.BeansHelper;
import beans.UserAccountBean;
import java.beans.PropertyVetoException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;

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
            try {
                userAccount.preparePersonalInfo();
            }
            catch (PropertyVetoException | IOException | SQLException e) {
                Logger.getLogger(StatServlet.class.getName()).log(Level.SEVERE, null, e);
                resp.sendRedirect("/error.jsp");
                //throw new ServletException(e);
            }

            req.getRequestDispatcher("/accountSettings.jsp").forward(req, resp);
        }
        else {
            req.getRequestDispatcher("/account.jsp").forward(req, resp);
        }
    }
}
