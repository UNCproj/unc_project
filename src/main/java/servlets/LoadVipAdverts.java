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
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "LoadVipAdverts", urlPatterns = "/load-vip-adverts")
public class LoadVipAdverts extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html");
        String type = request.getParameter("type");
        String n = "6";//Колличество объявлений для вывода + 1
        ArrayList <VipAdvert> vipAdvertsArray = new ArrayList<>();

        Connection connection = null;
        try {
            connection = DataSource.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = null;
            if (type.equals("-1")) {
                resultSet = statement.executeQuery(SQLQueriesHelper.selectVipAdverts(n));
            } else {
                resultSet = statement.executeQuery(SQLQueriesHelper.selectVipAdverts(n, type));
            }

            while (resultSet.next()){
                vipAdvertsArray.add(new VipAdvert(resultSet.getString("object_id"),
                        resultSet.getString("object_name"),resultSet.getString("value") ));
            }


        } catch (SQLException | PropertyVetoException e) {
            //e.printStackTrace();
            Logger.getLogger(StatServlet.class.getName()).log(Level.SEVERE, null, e);
            response.sendRedirect("/error.jsp");
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                //e.printStackTrace();
                Logger.getLogger(StatServlet.class.getName()).log(Level.SEVERE, null, e);
                response.sendRedirect("/error.jsp");
            }
        }
        PrintWriter out = response.getWriter();

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        out.println(gson.toJson(vipAdvertsArray));
    }
    class VipAdvert {
        private String id;
        private String name;
        private String price;
        VipAdvert(String id, String name, String price){
            this.id = id;
            this.name = name;
            this. price = price;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}
