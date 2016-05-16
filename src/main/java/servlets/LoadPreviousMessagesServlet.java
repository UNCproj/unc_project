package servlets;

import beans.UserAccountBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import db.DataSource;
import db.SQLQueriesHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by alex on 16.05.2016.
 */
@WebServlet(name = "LoadPreviousMessagesServlet", urlPatterns = "/loadPreMess")
public class LoadPreviousMessagesServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        System.out.println("Сработал LoadPreviousMessagesServlet");
        String messageId = request.getParameter("messId");
        String recipientId = request.getParameter("recId");
        UserAccountBean userAccountBean = (UserAccountBean) request.getSession().getAttribute("userAccount");
        String senderId = userAccountBean.getId();
        Connection connection = null;
        PrintWriter out = response.getWriter();
        try{
            connection = DataSource.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQLQueriesHelper.selectPreviousMessages(messageId, senderId, recipientId));

            ArrayList <Message> messagesArray = new ArrayList<Message>();
            int n = 0;
        while (resultSet.next() && n<10) {
            String recipient_Id = resultSet.getString("recipient");
            String recipient_Login = resultSet.getString("login");
            String text_ = resultSet.getString("mess_text");
            String date_ = resultSet.getString("mess_date");
            String read_Status = resultSet.getString("read_status");
            String recipient_Name = resultSet.getString("name");
            String recipient_Surname = resultSet.getString("surname");
            String mess_Id = resultSet.getString("mess_id");
            String sender_Id = resultSet.getString("sender");
            if (recipient_Id.equals(senderId) && read_Status.equals("no")) {
                Statement statementRead = connection.createStatement();
                statementRead.executeUpdate(SQLQueriesHelper.updateReadStatus(mess_Id));
            }
            Message message = new Message(recipient_Id, recipient_Login, text_, date_,read_Status,recipient_Name,recipient_Surname,mess_Id,sender_Id);
            messagesArray.add(message);
            n++;
        }


            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            System.out.println(gson.toJson(messagesArray));

            out.println(gson.toJson(messagesArray));
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
