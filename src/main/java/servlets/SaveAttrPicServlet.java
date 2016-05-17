package servlets;

import db.DataSource;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet(name = "SaveAttrPicServlet", urlPatterns = "/saveAttrPicServlet")

public class SaveAttrPicServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Сработал SaveAttrPicServlet");
        System.out.println();

        String adverId = request.getParameter("id");
        String path = request.getParameter("user_pic_file");
        System.out.println(path);
        Connection connection = null;
        try{
            connection = DataSource.getInstance().getConnection();
            Statement statement = connection.createStatement();
            String query = "insert into unc_params(object_id, attr_id, value) values ("+adverId+",6,'" + path + "')";
            System.out.println(query);
            statement.executeUpdate(query);
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
