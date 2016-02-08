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
@WebServlet(name = "UploadServlet", urlPatterns = "upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part filePart = request.getPart("file");
        File uploadDir = new File(getServletContext().getInitParameter("upload.location"));
        File uploadFile = new File(uploadDir, filePart.getName());

        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, uploadFile.toPath());
        }
    }
}
