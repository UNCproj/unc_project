package servlets;

import beans.UserAccountBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import db.DataSource;
import db.SQLQueriesHelper;
import unc.helpers.UncObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyVetoException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;

@WebServlet(name = "LoadTypesServlet", urlPatterns = "/loadTypes")
public class LoadTypesServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html");

        String type1 = request.getParameter("type1");
        String type2 = request.getParameter("type2");
        String type3 = request.getParameter("type3");
        String type4 = request.getParameter("type4");

        System.out.println(type1);
        System.out.println(type2);
        System.out.println(type3);
        System.out.println(type4);

        PrintWriter out = response.getWriter();

        Connection connection = null;
        try {
            connection = DataSource.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQLQueriesHelper.selectTypes(type1, type2, type3, type4));
            String[] arrStr = null;
            if (resultSet.next()) {
                Integer sizeInteger = Integer.valueOf(resultSet.getString("sizeselect"));
                int size = (int) sizeInteger;
                int i = 0;
                arrStr = new String[size];
                do {
                    arrStr[i] = resultSet.getString("ot_name");
                    i++;
                } while (resultSet.next());
            }
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            System.out.println(gson.toJson(arrStr));

            out.println(gson.toJson(arrStr));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

//        System.out.println(request.getParameter("type1"));
//        System.out.println(request.getParameter("type2"));
//        System.out.println(request.getParameter("type3"));
//        System.out.println(request.getParameter("type4"));
    }
}
