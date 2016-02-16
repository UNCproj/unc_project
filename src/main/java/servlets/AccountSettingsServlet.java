package servlets;

import beans.BeansHelper;
import beans.UserAccountBean;
import db.DataSource;
import db.SQLQueriesHelper;
import validation.UserRegistrationValidationBean;
import validation.UserUpdateValidationBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by Денис on 05.02.2016.
 * Output violations: pass, login_or_email, error
 */
@WebServlet(name = "AccountSettingsServlet", urlPatterns = "/accountSettings")
public class AccountSettingsServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String settingsGroup = request.getParameter("settingsGroup");
        if (settingsGroup == null) {
            //TODO:logging
            return;
        }

        UserAccountBean account = (UserAccountBean) request.getSession().getAttribute(BeansHelper.USER_ACCOUNT_SESSION_KEY);

        String password = request.getParameter("pass");
        String newLogin = request.getParameter("login");
        String newPass = request.getParameter("changePass");
        String newEmail = request.getParameter("email");
        String newUserPicFile = request.getParameter("userPicFile");

        UserUpdateValidationBean validationBean = new UserUpdateValidationBean(
                newLogin, password, newPass, newEmail);

        Hashtable<String, String> constraintViolations = validationBean.validate(account);

        if (validationBean.isValid()) {
            try (
                    Connection connection = DataSource.getInstance().getConnection();
                    Statement paramUpdateStatement = connection.createStatement()
            ) {
                switch (settingsGroup) {
                    case "main":

                        if (newLogin != null && newLogin.length() > 0) {
                            connection.setAutoCommit(false);

                            paramUpdateStatement.executeUpdate(
                                    SQLQueriesHelper.updateParam(new BigDecimal(account.getId()),
                                            SQLQueriesHelper.LOGIN_ATTR_ID, newLogin, null));

                            paramUpdateStatement.executeUpdate(
                                    SQLQueriesHelper.changeName(new BigDecimal(account.getId()), newLogin));

                            connection.commit();
                            connection.setAutoCommit(true);
                        }

                        if (newPass != null && newPass.length() > 0) {
                            paramUpdateStatement.executeUpdate(
                                    SQLQueriesHelper.updateParam(new BigDecimal(account.getId()),
                                            SQLQueriesHelper.PASSWORD_ATTR_ID, newPass, null));
                        }

                        if (newEmail != null && newEmail.length() > 0) {
                            paramUpdateStatement.executeUpdate(
                                    SQLQueriesHelper.updateParam(new BigDecimal(account.getId()),
                                            SQLQueriesHelper.EMAIL_ATTR_ID, newEmail, null));
                        }

                        if (newUserPicFile != null && newUserPicFile.length() > 0) {
                            newUserPicFile = getServletContext().getInitParameter("upload.url") + newUserPicFile;

                            paramUpdateStatement.executeUpdate(
                                    SQLQueriesHelper.updateParam(new BigDecimal(account.getId()),
                                            SQLQueriesHelper.USER_PIC_FILE_ATTR_ID, newUserPicFile, null));
                        }
                        break;

                    case "about":
                        String firstName = request.getParameter("firstName");
                        String secondName = request.getParameter("secondName");
                        String surname = request.getParameter("surname");
                        String phone = request.getParameter("phone");
                        String streetAndHouse = request.getParameter("streetAndHouse");
                        String city = request.getParameter("city");
                        String country = request.getParameter("country");
                        String additionalInfo = request.getParameter("additionalInfo");

                        updateOrCreateUserParam(firstName, SQLQueriesHelper.FIRST_NAME_ATTR_ID, paramUpdateStatement, account);
                        updateOrCreateUserParam(secondName, SQLQueriesHelper.SECOND_NAME_ATTR_ID, paramUpdateStatement, account);
                        updateOrCreateUserParam(surname, SQLQueriesHelper.SURNAME_ATTR_ID, paramUpdateStatement, account);
                        updateOrCreateUserParam(phone, SQLQueriesHelper.PHONE_ATTR_ID, paramUpdateStatement, account);
                        updateOrCreateUserParam(streetAndHouse, SQLQueriesHelper.STREET_AND_HOUSE_NAME_ATTR_ID, paramUpdateStatement, account);
                        updateOrCreateUserParam(city, SQLQueriesHelper.CITY_ADVERT_ATTR_ID, paramUpdateStatement, account);
                        updateOrCreateUserParam(country, SQLQueriesHelper.COUNTRY_ATTR_ID, paramUpdateStatement, account);
                        updateOrCreateUserParam(additionalInfo, SQLQueriesHelper.ADDITIONAL_INFO_ATTR_ID, paramUpdateStatement, account);
                        break;
                }

                account.updateAllInfo();
            } catch (Exception e) {
                //TODO: logging
                throw new ServletException(e);
            }
        }

        try (PrintWriter out = response.getWriter()) {
            //TODO: return JSON
        }
        catch (IOException e) {
            throw new ServletException(e);
        }
    }

    private void updateOrCreateUserParam(String value, String paramTypeId, Statement paramUpdateStatement,
                                         UserAccountBean account) throws Exception {
        int updateResult;

        if (value != null && value.length() > 0) {
            updateResult = paramUpdateStatement.executeUpdate(
                    SQLQueriesHelper.updateParam(new BigDecimal(account.getId()),
                            paramTypeId, value, null));

            if (updateResult == 0) {
                paramUpdateStatement.executeUpdate(
                        SQLQueriesHelper.insertParam(new BigDecimal(account.getId()), paramTypeId, value, null));
            }
        }
    }
}
