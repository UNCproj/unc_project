package servlets;

import beans.BeansHelper;
import beans.UserAccountBean;
import db.DataSource;
import db.SQLQueriesHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 * Created by Денис on 15.12.2015.
 */

@WebServlet(name = "loginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        boolean isLoggedIn = logIn(request.getParameter("login"), request.getParameter("pass"));
        PrintWriter out;
        StringBuffer responseJSON = new StringBuffer("{");

        try {
            out = response.getWriter();
        }
        catch (IOException e) {
            throw new ServletException(e);
        }

        if (isLoggedIn) {
            BigDecimal userId = null;
            String email = null;
            String userPicFile = null;
            Connection connection = null;
            Statement selectObjInfoStatement = null;
            Statement updateLastLoginDateStatement = null;

            try {
                connection = DataSource.getInstance().getConnection();
                selectObjInfoStatement = connection.createStatement();
                String[] types = new String[1];
                types[0] = SQLQueriesHelper.USER_TYPE_ID;
                ResultSet results = selectObjInfoStatement.executeQuery(
                        SQLQueriesHelper.selectFullObjectInformationByName(types, request.getParameter("login")));

                while(results.next()) {
                    String attrName = results.getString("attr_name");

                    switch (attrName) {
                        case SQLQueriesHelper.EMAIL_ATTR:
                            email = results.getString("value");
                            break;

                        case SQLQueriesHelper.USER_PIC_FILE_ATTR:
                            userPicFile = results.getString("value");
                            break;
                    }

                    userId = results.getBigDecimal("object_id");
                }

                updateLastLoginDateStatement = connection.createStatement();
                updateLastLoginDateStatement.executeUpdate(
                        SQLQueriesHelper.updateParam(userId, SQLQueriesHelper.LAST_VISIT_DATE_ATTR_ID, null, new Date()));
            }
            catch (Exception e) {
                throw new ServletException(e);
            }
            finally {
                try {
                    if (connection != null)
                        connection.close();

                    if (selectObjInfoStatement != null)
                        selectObjInfoStatement.close();

                    if (updateLastLoginDateStatement != null)
                        updateLastLoginDateStatement.close();
                }
                catch (SQLException e) {
                    throw new ServletException(e);
                }
            }

            UserAccountBean userAccountBean = new UserAccountBean();
            userAccountBean.initialize(userId.toString(), request.getParameter("login"), request.getParameter("pass"),
                    email, userPicFile, isLoggedIn, new Date());
            request.getSession().setAttribute(BeansHelper.USER_ACCOUNT_SESSION_KEY, userAccountBean);
        }

        responseJSON.append("\"logged\":\"" + isLoggedIn + "\"}");
        out.print(responseJSON);
    }

    private boolean logIn(String login, String password) throws ServletException {

        try {
            Connection con = DataSource.getInstance().getConnection();
            Statement statement = con.createStatement();
            String[] types = new String[2];
            types[0] = "1";
            types[1] = "2";
            ResultSet result = statement.executeQuery(SQLQueriesHelper.selectFullObjectInformationByName(types, login));

            if (result == null) {
                return false;
            }

            while (result.next()) {
                String attrName = result.getString("attr_name");
                if (attrName.equals(SQLQueriesHelper.PASSWORD_ATTR)) {
                    String attr_value = result.getString("value");
                    if (attr_value.equals(String.valueOf(password.hashCode()))) {
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }

        return false;
    }
}
