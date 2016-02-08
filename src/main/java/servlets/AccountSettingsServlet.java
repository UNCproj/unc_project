package servlets;

import beans.BeansHelper;
import beans.UserAccountBean;
import db.DataSource;
import db.SQLQueriesHelper;
import validation.UserRegistrationValidationBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Денис on 05.02.2016.
 * violations: pass, login_or_email, error
 */
@WebServlet(name = "AccountSettingsServlet", urlPatterns = "/accountSettings")
public class AccountSettingsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserAccountBean account = (UserAccountBean) request.getSession().getAttribute(BeansHelper.USER_ACCOUNT_SESSION_KEY);
        String password = request.getParameter("pass");
        PrintWriter out;

        try {
            out = response.getWriter();
        }
        catch (IOException e) {
            throw new ServletException(e);
        }

        if (!String.valueOf(password.hashCode()).equals(account.getPassword())) {
            out.print("{\"changed\":false, \"violation\":\"pass\"}");
            return;
        }

        String settingsGroup = request.getParameter("settingsGroup");

        if (settingsGroup == null) {
            //TODO:logging
            return;
        }

        Connection connection = null;
        Statement paramUpdateStatement = null;
        try {
            connection = DataSource.getInstance().getConnection();
            paramUpdateStatement = connection.createStatement();

            switch (settingsGroup) {
                case "main":
                    String newLogin = request.getParameter("login");
                    String newPass = request.getParameter("changePass");
                    String newEmail = request.getParameter("email");
                    String newUserPicFile = "/unc_project/resources" + request.getParameter("userPicFile");

                    UserRegistrationValidationBean validationBean = new UserRegistrationValidationBean(newLogin,
                            newPass, newPass, newEmail);

                    if (validationBean.isValid()) {
                        paramUpdateStatement.executeUpdate(
                                SQLQueriesHelper.updateParam(new BigDecimal(account.getId()),
                                        SQLQueriesHelper.LOGIN_ATTR_ID, newLogin, null));

                        paramUpdateStatement.executeUpdate(
                                SQLQueriesHelper.updateParam(new BigDecimal(account.getId()),
                                        SQLQueriesHelper.PASSWORD_ATTR_ID, newPass, null));

                        paramUpdateStatement.executeUpdate(
                                SQLQueriesHelper.updateParam(new BigDecimal(account.getId()),
                                        SQLQueriesHelper.EMAIL_ATTR_ID, newEmail, null));

                        paramUpdateStatement.executeUpdate(
                                SQLQueriesHelper.updateParam(new BigDecimal(account.getId()),
                                        SQLQueriesHelper.USER_PIC_FILE_ATTR_ID, newUserPicFile, null));
                    }
                    else {
                        out.print("{\"changed\":false, \"violation\":\"login_or_email\"}");
                    }
                    break;

                case "about":
                    break;
            }
        }
        catch (Exception e) {
            out.print("{\"changed\":false, \"violation\":\"error\"}");
            throw new ServletException(e);
        }
        finally {
            try {
                if (connection != null)
                    connection.close();

                if (paramUpdateStatement != null)
                    paramUpdateStatement.close();
            }
            catch (SQLException e) {
                throw new ServletException(e);
            }
        }
    }
}
