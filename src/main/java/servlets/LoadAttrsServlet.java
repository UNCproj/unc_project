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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "LoadAttrsServlet", urlPatterns = "/loadAttrs")
public class LoadAttrsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Сработал LoadAttrsServlet");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html");

        String type = request.getParameter("type");
        String type1 = request.getParameter("type1");
        String type2 = request.getParameter("type2");
        String type3 = request.getParameter("type3");
        String type4 = request.getParameter("type4");

        PrintWriter out = response.getWriter();

        Connection connection = null;
        try{
            connection = DataSource.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQLQueriesHelper.selectAttrs(type, type1, type2, type3, type4));
            ArrayList <Attributes> arr = new ArrayList();
            while (resultSet.next()){
                String name = resultSet.getString("attr_name");
                String nameRu = resultSet.getString("attr_name_ru");
                String types = resultSet.getString("attr_type");
                if(!name.equals("registration_date") && !name.equals("user_advert")) {
                    Attributes attributes = new Attributes(name, nameRu, types);
                    arr.add(attributes);
                }
//
            }
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            System.out.println(gson.toJson(arr));

            out.println(gson.toJson(arr));
        } catch (SQLException e) {
            e.printStackTrace();
                Logger.getLogger(StatServlet.class.getName()).log(Level.SEVERE, null, e);
                response.sendRedirect("/error.jsp");
        } catch (PropertyVetoException e) {
            e.printStackTrace();
                Logger.getLogger(StatServlet.class.getName()).log(Level.SEVERE, null, e);
                response.sendRedirect("/error.jsp");
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                Logger.getLogger(StatServlet.class.getName()).log(Level.SEVERE, null, e);
                response.sendRedirect("/error.jsp");
            }
        }

//        System.out.println(request.getParameter("type"));
//        System.out.println(request.getParameter("type1"));
//        System.out.println(request.getParameter("type2"));
//        System.out.println(request.getParameter("type3"));
//        System.out.println(request.getParameter("type4"));
    }
}
class Attributes{
    String attrName;
    String attrNameRu;
    String attrType;
    Attributes(String name, String nameRu, String type){
        attrName = name;
        attrNameRu = nameRu;
        attrType = type;
    }
}
