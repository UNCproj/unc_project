package servlets;

import db.DataSource;
import db.SQLQueriesHelper;
import validation.UserRegistrationValidationBean;

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
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "RegistrationServlet", urlPatterns = "/registration")
public class RegistrationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        PrintWriter out = response.getWriter();
        String login = request.getParameter("login");
        String pass = request.getParameter("pass");
        String retypePass = request.getParameter("retypePass");
        String email = request.getParameter("email");
        String avatar = request.getParameter("ava");
        String fname = request.getParameter("fname");
        String sname = request.getParameter("sname");
        UserRegistrationValidationBean registrationValidationBean =
                new UserRegistrationValidationBean(login, pass, retypePass, email);
        String constrViolationsJSON = registrationValidationBean.validate();

        if (registrationValidationBean.isValid()) {
            Connection connection = null;
            Statement statement1 = null;
            Statement statement2 = null;
            Statement statement3 = null;

            try {
                connection = DataSource.getInstance().getConnection();
                connection.setAutoCommit(false);

                statement1 = connection.createStatement();
                statement1.executeUpdate(SQLQueriesHelper.insertNewUser(login));

                statement2 = connection.createStatement();
                String[] types = new String[1];
                types[0] = "1";
                ResultSet results = statement2.executeQuery(SQLQueriesHelper.selectFullObjectInformationByName(types, login));

                results.next();
                BigDecimal userId = results.getBigDecimal("object_id");

                statement3 = connection.createStatement();
                statement3.executeUpdate(SQLQueriesHelper.insertParam(userId, SQLQueriesHelper.LOGIN_ATTR_ID, login, null));

                statement3 = connection.createStatement();
                statement3.executeUpdate(SQLQueriesHelper.insertParam(userId, SQLQueriesHelper.PASSWORD_ATTR_ID, String.valueOf(pass.hashCode()), null));

                statement3 = connection.createStatement();
                statement3.executeUpdate(SQLQueriesHelper.insertParam(userId, SQLQueriesHelper.REG_DATE_ATTR_ID, null, new java.util.Date()));

                statement3 = connection.createStatement();
                statement3.executeUpdate(SQLQueriesHelper.insertParam(userId, SQLQueriesHelper.LAST_VISIT_DATE_ATTR_ID, null, new java.util.Date()));

                statement3 = connection.createStatement();
                statement3.executeUpdate(SQLQueriesHelper.insertParam(userId, SQLQueriesHelper.EMAIL_ATTR_ID, email, null));

                statement3 = connection.createStatement();
                statement3.executeUpdate(SQLQueriesHelper.insertParam(userId, SQLQueriesHelper.USER_PIC_FILE_ATTR_ID, avatar.equals("") ? getInitParameter("default.userpic"):avatar, null));
                
                statement3 = connection.createStatement();
                statement3.executeUpdate(SQLQueriesHelper.insertParam(userId, SQLQueriesHelper.FIRST_NAME_ATTR_ID, fname.equals("") ? getInitParameter("default.userpic"):fname, null));
                
                statement3 = connection.createStatement();
                statement3.executeUpdate(SQLQueriesHelper.insertParam(userId, SQLQueriesHelper.SURNAME_ATTR_ID, sname.equals("") ? getInitParameter("default.userpic"):sname, null));

                connection.commit();
            } catch (Exception e) {
                try {
                    if (connection != null)
                        connection.rollback();
                }
                catch (SQLException rbe) {
                    Logger.getLogger(StatServlet.class.getName()).log(Level.SEVERE, null, rbe);
                    response.sendRedirect("/error.jsp");
                }
                Logger.getLogger(StatServlet.class.getName()).log(Level.SEVERE, null, e);
                response.sendRedirect("/error.jsp");
                //throw new ServletException(e);
            }
            finally {
                try {
                    if (connection != null)
                        connection.close();

                    if (statement1 != null)
                        statement1.close();

                    if (statement2 != null)
                        statement2.close();

                    if (statement3 != null)
                        statement3.close();
                }
                catch (Exception e) {
                    Logger.getLogger(StatServlet.class.getName()).log(Level.SEVERE, null, e);
                    response.sendRedirect("/error.jsp");
                    //throw new ServletException(e);
                }
            }
        }

        out.print(constrViolationsJSON);
    }
}