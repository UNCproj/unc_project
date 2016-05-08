package servlets;

import beans.ElasticSearchManager;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Денис on 08.05.2016.
 */
@WebServlet(name = "SearchManagementServlet", urlPatterns = "/esmanagement")
public class SearchManagementServlet extends HttpServlet {
    @EJB
    private ElasticSearchManager elasticSearchManager;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "reindex":
                elasticSearchManager.reindex();
                break;
            case "createindex":
                elasticSearchManager.createIndex();
                break;
            case "deleteindex":
                elasticSearchManager.deleteIndex();
                break;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
