package servlets;

import beans.*;
import db.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(name = "chatPublishServlet", urlPatterns = "/chatPublish")
public class chatPublishServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        String message = noQuotes(request.getParameter("message"));
        String sellerLogin = request.getParameter("sellerLogin");
        UserAccountBean userAccountBean = (UserAccountBean) request.getSession().getAttribute("userAccount");
        String senderId = userAccountBean.getId();
//        System.out.println("В сервлет пришло для добавления в базу - " + sellerLogin + ", " + message);

        String messageId = "";

        if (message != null) {
            Connection connection = null;
            try {
                connection = DataSource.getInstance().getConnection();

                Statement statementId = connection.createStatement();
                ResultSet resultSetId = statementId.executeQuery(SQLQueriesHelper.newId());
                resultSetId.next();
                messageId = resultSetId.getString("id");

                Statement statementfindIdNamed = connection.createStatement();
                ResultSet resultSetFindIdNamed = statementfindIdNamed.executeQuery(SQLQueriesHelper.findIdNamed(sellerLogin));
                resultSetFindIdNamed.next();
                String recipientId = resultSetFindIdNamed.getString("object_id");

                Statement statementMessage = connection.createStatement();
                statementMessage.executeUpdate(SQLQueriesHelper.newMessage(messageId, message, senderId, recipientId));

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
    public String noQuotes (String text){
        String message = text;

        for (int i=0; i<text.length();i++){
            if(text.charAt(i)=='\''){
                message=text.substring(0,i+1) + "'" + text.substring(i+1);
                text=message;
                i++;
            }
        }
System.out.println(message);
        return message;
    }
}

