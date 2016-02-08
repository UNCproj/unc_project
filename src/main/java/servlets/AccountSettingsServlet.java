package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Денис on 05.02.2016.
 */
@WebServlet(name = "AccountSettingsServlet", urlPatterns = "/accountSettings")
public class AccountSettingsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String settingsGroup = request.getParameter("settingsGroup");

        if (settingsGroup == null) {
            //TODO:logging
            return;
        }



        switch (settingsGroup) {
            case "main":
                break;
            case "about":
                break;
        }
    }
}
