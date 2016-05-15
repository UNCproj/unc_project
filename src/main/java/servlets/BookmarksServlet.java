package servlets;

import beans.AdvertBean;
import beans.BeansHelper;
import beans.UserAccountBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import serializers.AdvertSerializer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Денис on 18.01.2016.
 */
@WebServlet(urlPatterns = "/bookmarks")
public class BookmarksServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String action = request.getParameter("action");

        switch (action) {
            case "getBookmarksList":
                getBookmarksList(request, response);
                break;

            case "addBookmark":
                addBookmark(request, response);
                break;

            case "deleteBookmarks":
                deleteBookmarks(request, response);
                break;
        }
    }

    private void getBookmarksList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        UserAccountBean accountBean = (UserAccountBean)request.getSession().getAttribute(BeansHelper.USER_ACCOUNT_SESSION_KEY);

        ArrayList<AdvertBean> bookmarks = null;

        try {
            bookmarks = accountBean.getBookmarks();
        } catch (PropertyVetoException | SQLException | IOException e) {
            e.printStackTrace();
        }

        final Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(AdvertBean.class, new AdvertSerializer())
                .create();

        try {
            response.setHeader("Content-Type", "text/plain; charset=UTF-8");
            PrintWriter responseWriter = response.getWriter();
            responseWriter.print(gson.toJson(bookmarks));
        }
        catch (IOException e) {
            throw new ServletException(e);
        }
    }

    private void addBookmark(HttpServletRequest request, HttpServletResponse response) {
        String bookmarkId = request.getParameter("bookmarkId");
        UserAccountBean user = (UserAccountBean)request.getSession().getAttribute(BeansHelper.USER_ACCOUNT_SESSION_KEY);

        try {
            user.addBookmark(bookmarkId);
        } catch (PropertyVetoException | SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteBookmarks(HttpServletRequest request, HttpServletResponse response) {
        String bookmarkId = request.getParameter("bookmarkId");
        UserAccountBean user = (UserAccountBean)request.getSession().getAttribute(BeansHelper.USER_ACCOUNT_SESSION_KEY);

        try {
            user.deleteBookmark(bookmarkId);
        } catch (PropertyVetoException | SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
