package servlets;

import beans.BeansHelper;
import beans.UserAccountBean;
import com.google.gson.JsonObject;
import db.DataSource;
import db.SQLQueriesHelper;
import unc.helpers.Crypt2;

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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Денис on 15.12.2015.
 */

@WebServlet(name = "loginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject responseJSON = logIn(request.getParameter("login"), request.getParameter("pass"));
        boolean isLoggedIn = responseJSON
                .get("logged")
                .getAsBoolean();
        PrintWriter out;

        try {
            out = response.getWriter();
        }
        catch (IOException e) {
            throw new ServletException(e);
        }

        String userId = null;

        if (isLoggedIn) {
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

                    userId = results.getString("object_id");
                }

                updateLastLoginDateStatement = connection.createStatement();
                updateLastLoginDateStatement.executeUpdate(
                        SQLQueriesHelper.updateParam(new BigDecimal(userId), SQLQueriesHelper.LAST_VISIT_DATE_ATTR_ID, null, new Date()));
            }
            catch (Exception e) {
                throw new ServletException(e);
            }
            finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }

                    if (selectObjInfoStatement != null) {
                        selectObjInfoStatement.close();
                    }

                    if (updateLastLoginDateStatement != null) {
                        updateLastLoginDateStatement.close();
                    }
                }
                catch (SQLException e) {
                    throw new ServletException(e);
                }
            }

            UserAccountBean userAccountBean = new UserAccountBean();
            userAccountBean.initialize(userId.toString(), request.getParameter("login"),
                    String.valueOf(request.getParameter("pass").hashCode()),
                    email, userPicFile, isLoggedIn, new Date());
            try {
                userAccountBean.updateAllInfo();
            } catch (PropertyVetoException ex) {
                Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            request.getSession().setAttribute(BeansHelper.USER_ACCOUNT_SESSION_KEY, userAccountBean);
        }

        if (userId != null) {
            responseJSON.addProperty("userId", userId.toString());
        }
        out.print(responseJSON);
    }

    private JsonObject logIn(String login, String password) throws ServletException {
        JsonObject constrViolationsJSON = new JsonObject();

        if (login == null || password == null) {
            constrViolationsJSON.addProperty("logged", false);
            constrViolationsJSON.addProperty("cause", "Нужно ввести логин и пароль");
            return constrViolationsJSON;
        }

        if (login.length() == 0 || password.length() == 0) {
            constrViolationsJSON.addProperty("logged", false);
            constrViolationsJSON.addProperty("cause", "Нужно ввести логин и пароль");
            return constrViolationsJSON;
        }

        Logger log = Logger.getLogger("loginlog");
        boolean islog = false;
        try {
            Connection con = DataSource.getInstance().getConnection();
            Statement statement = con.createStatement();
            String[] types = new String[2];
            types[0] = "1";
            types[1] = "2";
            ResultSet result = statement.executeQuery(SQLQueriesHelper.selectFullObjectInformationByName(types, login));
            if (result == null) {
                constrViolationsJSON.addProperty("logged", false);
                constrViolationsJSON.addProperty("cause", "Пользователь не зарегистрирован");
                return constrViolationsJSON;
            }

            if (!result.next()) {
                constrViolationsJSON.addProperty("logged", false);
                constrViolationsJSON.addProperty("cause", "Пользователь не зарегистрирован");
                return constrViolationsJSON;
            }
            else {
                result.beforeFirst();
            }

            while (result.next()) {
                String attrName = result.getString("attr_name");
                if (attrName.equals("is_invalid")){
                    String attr_value = result.getString("value");
                    if ((attr_value!=null)&&(attr_value.equals("true"))){
                        islog = false;
                        constrViolationsJSON.addProperty("logged", false);
                        constrViolationsJSON.addProperty("cause", "Пользователь заблокирован");
                        return constrViolationsJSON;
                    }
                }
                
                if (attrName.equals(SQLQueriesHelper.PASSWORD_ATTR)) {
                    String attr_value = result.getString("value");                   
                    if (attr_value.equals(Crypt2.sha256(password))) {
                        islog = true;
                    }
                    else {
                        islog = false;
                        constrViolationsJSON.addProperty("logged", false);
                        constrViolationsJSON.addProperty("cause", "Неверный пароль");
                        return constrViolationsJSON;
                    }
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }

        constrViolationsJSON.addProperty("logged", true);
        return constrViolationsJSON;
    }
}
