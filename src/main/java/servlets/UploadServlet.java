package servlets;

import beans.BeansHelper;
import beans.UserAccountBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Date;
import java.util.Random;
import java.util.function.BooleanSupplier;

/**
 * Created by Денис on 07.02.2016.
 */
@WebServlet(name = "UploadServlet", urlPatterns = "/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part filePart = request.getPart("file");
        String fileName = request.getParameter("flowFilename");
        File uploadDir = new File(getServletContext().getInitParameter("upload.location"));
        File uploadFile = new File(uploadDir, fileName);

        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, uploadFile.toPath());
        }
        catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
