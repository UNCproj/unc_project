package servlets;

import beans.AdvertBean;
import beans.BeansHelper;
import beans.UserAccountBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import db.DataSource;
import db.SQLQueriesHelper;
import serializers.AdvertSerializer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Денис on 18.01.2016.
 */
@WebServlet(urlPatterns = "/bookmarks")
public class BookmarksServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        UserAccountBean user = (UserAccountBean)request.getSession().getAttribute(BeansHelper.USER_ACCOUNT_SESSION_KEY);
        ArrayList<AdvertBean> adverts = new ArrayList<>();
        Connection connection = null;
        ArrayList<String> bookmarksIDs = new ArrayList<>();

        try {
            connection = DataSource.getInstance().getConnection();
            Statement statement = connection.createStatement();
            String[] types = new String[1];
            types[0] = SQLQueriesHelper.USER_TYPE_ID;
            ResultSet results = statement.executeQuery(
                    SQLQueriesHelper.selectFullObjectInformationByName(types, user.getLogin()));

            while (results.next()) {
                if (results.getString("attr_name").equals(SQLQueriesHelper.BOOKMARK_ATTR)) {
                    String bookmarkId = results.getString("value");
                    bookmarksIDs.add(bookmarkId);
                }
            }

            String[] bIDsStr = Arrays.copyOf(bookmarksIDs.toArray(), bookmarksIDs.size(), String[].class);
            Statement statement2 = connection.createStatement();
            results = statement2.executeQuery(
                            SQLQueriesHelper.selectFullObjectInformationById(
                                    null,
                                    bIDsStr
                            )
                      );

            while (results.next()) {
                String currentAdvertId = results.getString("object_id");

                int currentAdvertIndex = -1;
                for (int i = 0; i < adverts.size(); i++) {
                    AdvertBean currentAdvertBean = adverts.get(i);

                    if (currentAdvertBean.getAttribute("id") != null &&
                            currentAdvertBean.getAttribute("id").equals(currentAdvertId)) {
                        currentAdvertIndex = i;
                        break;
                    }
                }

                if (currentAdvertIndex == - 1) {
                    adverts.add(new AdvertBean());
                    currentAdvertIndex = adverts.size() - 1;
                    adverts.get(currentAdvertIndex).setId(results.getString("object_id"));
                    adverts.get(currentAdvertIndex).setName(results.getString("object_name"));
                }

                switch (results.getString("attr_name")) {
                    case "description":
                        adverts.get(currentAdvertIndex).setDescription(results.getString("value"));
                        break;
                }
            }
        }
        catch (Exception e) {
            throw new ServletException(e);
        }
        finally {
            try {
                connection.close();
            }
            catch (Exception e) {
                throw new ServletException(e);
            }
        }

        final Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(AdvertBean.class, new AdvertSerializer())
                .create();

        try {
            PrintWriter responseWriter = response.getWriter();
            response.setCharacterEncoding("UTF-8");
            responseWriter.print(gson.toJson(adverts));
        }
        catch (IOException e) {
            throw new ServletException(e);
        }
    }
}
