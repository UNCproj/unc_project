package servlets;

import beans.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@WebServlet(name = "chatSubscribeServlet", urlPatterns = "/chatSubscribe")
public class chatSubscribeServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        String lastMessageId = request.getParameter("lastMessageId");
        String recipientId = request.getParameter("recipientId");

        UserAccountBean userAccountBean = (UserAccountBean) request.getSession().getAttribute("userAccount");
        String senderId = userAccountBean.getId();

        PrintWriter out = response.getWriter();
        Connection connection = null;
        try {
            connection = DataSource.getInstance().getConnection();

            Statement statementOutputMessages = connection.createStatement();
            ResultSet resultSetOutputMessagees =  statementOutputMessages.executeQuery(SQLQueriesHelper.outputMessages(
                    lastMessageId,senderId,recipientId
            ));
            ArrayList <Message> messagesArray = new ArrayList<Message>();
            int step = 0;
            if(resultSetOutputMessagees.next()==false){
                while(true){
                    resultSetOutputMessagees =  statementOutputMessages.executeQuery(SQLQueriesHelper.outputMessages(
                            lastMessageId,senderId,recipientId));
                    if(resultSetOutputMessagees.next()) {
                        break;
                    }else if(step==1){
                        break;
                    }else{
                        Thread myThread = Thread.currentThread();
                        myThread.sleep(1000);
                        step++;
                    }
                }
            }
            int n = 0;
            do {
                String id_message = resultSetOutputMessagees.getString("id_message");
                String date_message = resultSetOutputMessagees.getString("date_message");
                String text_message = resultSetOutputMessagees.getString("text_message");
                String sender_message = resultSetOutputMessagees.getString("object_name");
                String sender_id = resultSetOutputMessagees.getString("sender_message");
                String recipient_id = resultSetOutputMessagees.getString("recipient_message");
                String read_status = resultSetOutputMessagees.getString("read_status");
                String name_ = resultSetOutputMessagees.getString("name");
                String surname_ = resultSetOutputMessagees.getString("surname");
                if (recipient_id.equals(senderId) && read_status.equals("no")){
                    Statement statementRead = connection.createStatement();
                    statementRead.executeUpdate(SQLQueriesHelper.updateReadStatus(id_message));
                }
                Message message = new Message(id_message,date_message,text_message,sender_message, sender_id, recipient_id, read_status, name_, surname_);
                messagesArray.add(message);
                n++;
            }while (resultSetOutputMessagees.next() && n<10);

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            System.out.println(gson.toJson(messagesArray));

            out.println(gson.toJson(messagesArray));

        } catch (SQLException e) {
//            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    class Message {
        String id;
        String date;
        String text;
        String sender;
        String senderId;
        String recipientId;
        String readStatus;
        String name;
        String surname;
        Message(String id, String date, String text, String sender, String senderId, String recipientId, String readStatus, String name, String surname){
            this.id = id;
            this.date = date;
            this.text = text;
            this.sender = sender;
            this.senderId = senderId;
            this.recipientId = recipientId;
            this.readStatus = readStatus;
            this.name = name;
            this.surname = surname;
        }
    }
}