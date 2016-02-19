package servlets;

import beans.AdvertBean;
import beans.BeansHelper;
import beans.UserAccountBean;
import db.DataSource;
import db.SQLQueriesHelper;
import jdk.nashorn.internal.ir.debug.JSONWriter;
import org.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Денис on 18.01.2016.
 */
@WebServlet(urlPatterns = "/bookmarks")
public class BookmarksServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        UserAccountBean user = (UserAccountBean)request.getSession().getAttribute(BeansHelper.USER_ACCOUNT_SESSION_KEY);
        ArrayList<AdvertBean> adverts = new ArrayList<>();
        Connection connection = null;
        ArrayList<Integer> bookmarksIDs = new ArrayList<>();

        try {
            connection = DataSource.getInstance().getConnection();
            Statement statement = connection.createStatement();
            String[] types = new String[1];
            types[0] = SQLQueriesHelper.USER_TYPE_ID;
            ResultSet results = statement.executeQuery(
                    SQLQueriesHelper.selectFullObjectInformationByName(types, user.getLogin()));

            while (results.next()) {
                if (results.getString("attr_name").equals(SQLQueriesHelper.BOOKMARK_ATTR)) {
                    bookmarksIDs.add(results.getInt("value"));
                }
            }

            String[] bIDsStr = Arrays.copyOf(bookmarksIDs.toArray(), bookmarksIDs.size(), String[].class);
            Statement statement2 = connection.createStatement();
            types[0] = SQLQueriesHelper.ADVERT_TYPE_ID;
            results = statement2.executeQuery(
                            SQLQueriesHelper.selectFullObjectInformationById(
                                    types,
                                    bIDsStr
                            )
                      );

            while (results.next()) {
                String currentAdvertId = String.valueOf(results.getInt("object_id"));

                int currentAdvertIndex = -1;
                for (int i = 0; i < adverts.size(); i++) {
                    if (adverts.get(i).getId() == currentAdvertId) {
                        currentAdvertIndex = i;
                        break;
                    }
                }

                if (currentAdvertIndex == - 1) {
                    adverts.add(new AdvertBean());
                    currentAdvertIndex = adverts.size() - 1;
                    adverts.get(currentAdvertIndex).setName(results.getString("name"));
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

        JSONArray responseJSON = new JSONArray(adverts);
        try {
            response.getWriter().print(responseJSON);
        }
        catch (IOException e) {
            throw new ServletException(e);
        }
    }
}
