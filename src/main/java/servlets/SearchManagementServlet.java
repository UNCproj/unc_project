package servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import schedule.tasks.IndexAdvertsTask;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Денис on 21.03.2016.
 */

/**
 * Возможные значения параметра action:
 * rebuild_index - перестраивает индекс elasticsearch
 */
@WebServlet(name = "SearchManagementServlet", urlPatterns = "/searchManagement")
public class SearchManagementServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "rebuild_index":
                IndexAdvertsTask indexAdvertsTask = new IndexAdvertsTask();
                indexAdvertsTask.indexAdverts();

                try(PrintWriter out = response.getWriter()) {
                    out.print(indexAdvertsTask.isIndexed());
                }
                break;
        }
    }
}
