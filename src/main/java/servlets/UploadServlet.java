package servlets;

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
import java.nio.file.StandardCopyOption;

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

        try(InputStream input = filePart.getInputStream()) {
            Files.copy(input, uploadFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
