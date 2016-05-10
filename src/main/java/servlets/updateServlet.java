package servlets;

import unc.helpers.UncObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * Created by Денис on 26.02.2016.
 */
@WebServlet(name = "updateServlet", urlPatterns = "/uncupdate")
public class updateServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String objectId = request.getParameter("id");
        Enumeration<String> paramsNames = request.getParameterNames();

        UncObject obj = new UncObject(objectId, null);

        while (paramsNames.hasMoreElements()) {
            String paramName = paramsNames.nextElement();

            if (paramName.equals("id")) {
                continue;
            }

            if (paramName.equals("login")) {
                continue;
            }

            String paramValue = request.getParameter(paramName);
            obj.setParam(paramName, paramValue);
        }

        try {
            obj.updateInDB();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
