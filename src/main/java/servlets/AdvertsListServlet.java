package servlets;

import beans.AdvertBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import db.DataSource;
import db.SQLQueriesHelper;

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

/**
 * Возможные значения параметра action:
 * get_adverts - получить список объявлений с заданными параметрами
 * get_all_categories - получить список всех категорий (дети заданного типа и всех его потомков)
 * get_first_lvl_categories - получить список главных категорий (дети заданного типа)
 * get_adverts_count - получить количество объявлений с заданными параметрами
 * get_adverts_attributes - получить атрибуты заданного типа объявлений
 */
@WebServlet(name = "AdvertsListServlet", urlPatterns = "/advertsList")
public class AdvertsListServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action.equals("get_adverts")) {

            String adCategoryId = request.getParameter("adCategoryId");
            String adsStartingNum = request.getParameter("adsStartingNum");
            String adsCount = request.getParameter("adsCount");
            String sortingParam = request.getParameter("sortingParam");
            String sortingOrder = request.getParameter("sortingOrder");
            String adNamePattern = request.getParameter("adNamePattern");

            ArrayList<AdvertBean> adverts = getAdverts(adCategoryId, adsStartingNum, adsCount, sortingParam,
                    sortingOrder, adNamePattern);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String advListJson = gson.toJson(adverts, adverts.getClass());

            response.setContentType("text/html; charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.print(advListJson);
            }
        }
        else if (action.equals("get_all_categories")) {
            try (
                    Connection connection = DataSource.getInstance().getConnection();
                    Statement statement = connection.createStatement()
            ) {
                try (ResultSet childrenTypesResults = statement.executeQuery(
                        SQLQueriesHelper.getTypeChildren(SQLQueriesHelper.ADVERT_TYPE_ID)
                )) {
                    ArrayList<String> categoriesIds = new ArrayList<>();
                    ArrayList<String> categoriesNames = new ArrayList<>();

                    while (childrenTypesResults.next()) {
                        categoriesIds.add(childrenTypesResults.getString("ot_id"));
                        categoriesNames.add(childrenTypesResults.getString("ot_name"));
                    }

                    ArrayList<String>[] jsonWrapper = new ArrayList[]{categoriesIds, categoriesNames};
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String categoriesJson = gson.toJson(jsonWrapper, jsonWrapper.getClass());

                    response.setContentType("text/html; charset=UTF-8");
                    try (PrintWriter out = response.getWriter()) {
                        out.print(categoriesJson);
                    }
                }
            } catch (SQLException | PropertyVetoException e) {
                throw new ServletException(e);
            }
        }
        else if (action.equals("get_first_lvl_categories")) {
            try (
                    Connection connection = DataSource.getInstance().getConnection();
                    Statement statement = connection.createStatement()
            ) {
                try (ResultSet childrenTypesResults = statement.executeQuery(
                        SQLQueriesHelper.getTypeFirstLevelChildren(SQLQueriesHelper.ADVERT_TYPE_ID)
                )) {
                    ArrayList<String[]> categories = new ArrayList<>();

                    while (childrenTypesResults.next()) {
                        categories.add(new String[] {childrenTypesResults.getString("ot_id"),
                                childrenTypesResults.getString("ot_name")});
                    }

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String categoriesJson = gson.toJson(categories, categories.getClass());

                    response.setContentType("text/html; charset=UTF-8");
                    try (PrintWriter out = response.getWriter()) {
                        out.print(categoriesJson);
                    }
                }
            } catch (SQLException | PropertyVetoException e) {
                throw new ServletException(e);
            }
        }
        else if (action.equals("get_adverts_count")) {
            String adCategoryId = request.getParameter("adCategoryId");
            String adsStartingNum = request.getParameter("adsStartingNum");
            String adsCount = request.getParameter("adsCount");
            String sortingParam = request.getParameter("sortingParam");
            String sortingOrder = request.getParameter("sortingOrder");
            String adNamePattern = request.getParameter("adNamePattern");

            Integer foundedAdsCount = getAdverts(adCategoryId, adsStartingNum, adsCount, sortingParam,
                    sortingOrder, adNamePattern).size();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String advListJson = gson.toJson(foundedAdsCount, foundedAdsCount.getClass());

            response.setContentType("text/html; charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.print(advListJson);
            }
        }
        else if (action.equals("get_adverts_attributes")) {
            String adCategoryId = request.getParameter("adCategoryIds");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    private ArrayList<AdvertBean> getAdverts(String adCategoryId, String adsStartingNum, String adsCount,
                                             String sortingParam, String sortingOrder, String adNamePattern)
            throws ServletException, IOException {
        ArrayList<AdvertBean> adverts = new ArrayList<>();

        try (
                Connection connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement()
        ) {
            try (ResultSet childrenTypesResults = statement.executeQuery(SQLQueriesHelper.getTypeChildren(adCategoryId))) {
                ArrayList<String> categories = new ArrayList<>();

                while (childrenTypesResults.next()) {
                    categories.add(childrenTypesResults.getString("ot_id"));
                }

                try (
                        ResultSet advListResults = statement.executeQuery(
                                SQLQueriesHelper.getAdvertsList(categories.toArray(new String[0]),
                                        adsStartingNum, adsCount, sortingParam, sortingOrder, adNamePattern)
                        )
                ) {
                    while (advListResults.next()) {
                        AdvertBean adv = new AdvertBean();

                        adv.setId(advListResults.getString("object_id"));
                        adv.setName(advListResults.getString("object_name"));
                        adv.setDescription(advListResults.getString(SQLQueriesHelper.DESCRIPTION_ATTR));
                        adv.setCity(advListResults.getString(SQLQueriesHelper.CITY_ADVERT_ATTR));
                        adv.setPic(advListResults.getString("pic"));
                        adv.setPrice(advListResults.getString(SQLQueriesHelper.PRICE_ADVERT_ATTR));
                        adv.setRegistrationDate(advListResults.getString(SQLQueriesHelper.REG_DATE_ATTR));

                        adverts.add(adv);
                    }
                }
            }
        } catch (SQLException | PropertyVetoException e) {
            throw new ServletException(e);
        }

        return adverts;
    }
}
