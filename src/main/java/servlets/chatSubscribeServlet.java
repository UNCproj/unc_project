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
        System.out.println("Пришло в сервлет для отправки:" + lastMessageId);
        System.out.println(request.getCharacterEncoding());

        PrintWriter out = response.getWriter();
//        out.println("Hello");
        Connection connection = null;
        try {
            connection = DataSource.getInstance().getConnection();
System.out.println("|"+lastMessageId+"|");
            Statement statementOutputMessages = connection.createStatement();
            ResultSet resultSetOutputMessagees =  statementOutputMessages.executeQuery(SQLQueriesHelper.outputMessages(lastMessageId));
            ArrayList <Message> messagesArray = new ArrayList<Message>();
            int step = 0;
            if(resultSetOutputMessagees.next()==false){
                System.out.println("Подходящих записей нет");
                while(true){
                    resultSetOutputMessagees =  statementOutputMessages.executeQuery(SQLQueriesHelper.outputMessages(lastMessageId));
                    if(resultSetOutputMessagees.next()) {
                        break;
                    }else if(step==10){
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
                Message message = new Message(id_message,date_message,text_message);
                messagesArray.add(message);
            }while (resultSetOutputMessagees.next());

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            System.out.println(gson.toJson(messagesArray));

            out.println(gson.toJson(messagesArray));

//            out.println(id_message);
//            out.println(text_message);
//            out.println(date_message);
//
//            ArrayList <String> arrStr = new ArrayList<>();
//            arrStr.add(0,"hello");
//            arrStr.add(1,"it's me...");
//
//            String[] arrStr = new String[2];
//            arrStr[0]="123";
//            arrStr[1]="456";
//
//            out.println(arrStr[0]);
//            out.println(arrStr[1]);

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
        Message(String id, String date, String text){
            this.id = id;
            this.date = date;
            this.text = text;
        }
    }
}