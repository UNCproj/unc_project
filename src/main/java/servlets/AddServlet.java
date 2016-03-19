package servlets;

import beans.UserAccountBean;
import db.DataSource;
import db.SQLQueriesHelper;
import unc.helpers.UncObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyVetoException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;

@WebServlet(name = "AddServlet", urlPatterns = "/uncadd")
public class AddServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Сработал addservlet");
        String type = request.getParameter("type");
        String type1 = request.getParameter("type1");
        String type2 = request.getParameter("type2");
        String type3 = request.getParameter("type3");
        String type4 = request.getParameter("type4");
        String objectName = request.getParameter("name");

        System.out.println(type1);
        System.out.println(type2);
        System.out.println(type3);
        System.out.println(type4);

        Enumeration<String> paramsNames = request.getParameterNames();

        UserAccountBean userAccountBean = (UserAccountBean) request.getSession().getAttribute("userAccount");
        String userId = userAccountBean.getId();

        Connection connection = null;
        String objectType = "";
        try{
            connection = DataSource.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQLQueriesHelper.selectChildTypeIdByTypeNames(type, type1,
                    type2, type3, type4));
            resultSet.next();
            objectType = resultSet.getString("ot_id");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }

        UncObject obj = new UncObject(null, objectName, objectType, userId);

        while (paramsNames.hasMoreElements()) {
            String paramName = paramsNames.nextElement();

            if (paramName.equals("type") || paramName.equals("name") || paramName.equals("type1")
                    || paramName.equals("type2") || paramName.equals("type3") || paramName.equals("type4")) {
                continue;
            }

            String paramValue = request.getParameter(paramName);
            obj.setParam(paramName, paramValue);
        }

        try {
            obj.insertIntoDB();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}