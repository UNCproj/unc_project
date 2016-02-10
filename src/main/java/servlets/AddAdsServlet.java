package servlets;

import beans.*;
import db.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Alex on 09.02.2016.
 */

@WebServlet(name = "addAdsServlet", urlPatterns = "/add-ads")
public class AddAdsServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html");

        RequestDispatcher requestDispatcher = getServletContext().getNamedDispatcher("UploadServlet");
        requestDispatcher.include(request, response);

        String city = request.getParameter("city");
        String price = request.getParameter("price");
        String category = "";
        if(request.getParameter("category")!=null){
            category = request.getParameter("category");
        }
        if(request.getParameter("typeCategory")!=null){
            category = category + ":" + request.getParameter("typeCategory");
        }
        if(request.getParameter("typeTypes")!=null){
            category = category + ":" + request.getParameter("typeTypes");
        }
        if(request.getParameter("typeTypesType")!=null){
            category = category + ":" + request.getParameter("typeTypesType");
        }
        String name = request.getParameter("adsName");
        String description = request.getParameter("adsDescription");
        UserAccountBean userAccountBean = (UserAccountBean) request.getSession().getAttribute("userAccount");
        String userId = userAccountBean.getId();
        String advertId = "";

        SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy:MM:dd kk:mm:ss");
        String date =  dateFormate.format(new Date());

        if (city != null && price !=null && name != null && userId != null) {
            Connection connection = null;
            try {
                connection = DataSource.getInstance().getConnection();

                Statement statementId = connection.createStatement();
                ResultSet resultSetId = statementId.executeQuery(SQLQueriesHelper.newId());
                resultSetId.next();
                advertId = resultSetId.getString("id");

                Statement statementAdvert =  connection.createStatement();
                statementAdvert.executeUpdate(SQLQueriesHelper.newAdvert(advertId, name));

                Statement statementReference = connection.createStatement();
                statementReference.executeUpdate(SQLQueriesHelper.newReference(advertId, userId, SQLQueriesHelper.USERS_ADVERT_ATTR_ID));

                Statement statementAdvertCategory = connection.createStatement();
                statementAdvertCategory.executeUpdate(SQLQueriesHelper.insertAdvertsParams(advertId,category, SQLQueriesHelper.CATEGORY_ADVERT_ATTR_ID));

                Statement statementAdvertCity = connection.createStatement();
                statementAdvertCity.executeUpdate(SQLQueriesHelper.insertAdvertsParams(advertId,city,SQLQueriesHelper.CITY_ADVERT_ATTR_ID));

                Statement statementAdvertPrice = connection.createStatement();
                statementAdvertPrice.executeUpdate(SQLQueriesHelper.insertAdvertsParams(advertId,price,SQLQueriesHelper.PRICE_ADVERT_ATTR_ID));

                Statement statementAdvertDescription = connection.createStatement();
                statementAdvertDescription.executeUpdate(SQLQueriesHelper.insertAdvertsParams(advertId, description, SQLQueriesHelper.DESCRIPTION_ATTR_ID));

                Statement statementAdvertDate = connection.createStatement();
                statementAdvertDate.executeUpdate((SQLQueriesHelper.insertDate(advertId, date)));

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
        }
    }
}
