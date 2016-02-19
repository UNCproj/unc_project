package servlets;

import image.resize.ImageResizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * Created by Денис on 15.02.2016.
 */
@WebServlet(name = "ImageResizeServlet", urlPatterns = "/imageResize")
public class ImageResizeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String imageName = request.getParameter("imageName");

        if (imageName != null) {
            File uploadDir = new File(getServletContext().getInitParameter("upload.location"));
            File uploadFile = new File(uploadDir, imageName);

            ImageResizer ir = new ImageResizer();
            ir.resize(uploadFile.getAbsolutePath(), uploadFile.getAbsolutePath(), 150, 150);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
