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
                    }else if(step==100){
                        break;
                    }else{
                        Thread myThread = Thread.currentThread();
                        myThread.sleep(1000);
                        step++;
                    }
                }
            }
            do {
                String id_message = resultSetOutputMessagees.getString("id_message");
                String date_message = resultSetOutputMessagees.getString("date_message");
                String text_message = resultSetOutputMessagees.getString("text_message");
                String sender_message = resultSetOutputMessagees.getString("object_name");
                Message message = new Message(id_message,date_message,text_message,sender_message);
                messagesArray.add(message);
            }while (resultSetOutputMessagees.next());

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            System.out.println(gson.toJson(messagesArray));

            out.println(gson.toJson(messagesArray));

        } catch (SQLException e) {
            e.printStackTrace();
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
        Message(String id, String date, String text, String sender){
            this.id = id;
            this.date = date;
            this.text = text;
            this.sender = sender;
        }
    }
}