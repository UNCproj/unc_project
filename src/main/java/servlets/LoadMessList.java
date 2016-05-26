package servlets;

import beans.UserAccount;
import beans.UserAccountBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import db.DataSource;
import db.SQLQueriesHelper;
import unc.helpers.Crypt2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by alex on 05.05.2016.
 */
@WebServlet(name = "LoadMessList", urlPatterns = "/loadMessList")
public class LoadMessList extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("LoadMessList");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        String id = request.getParameter("id");
        UserAccountBean userAccountBean = (UserAccountBean) request.getSession().getAttribute("userAccount");
        String senderId = userAccountBean.getId();
        if (id.equals(senderId)) {
            ArrayList<Message> arrMess = new ArrayList<>();
            Connection connection = null;
            try {
                connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(SQLQueriesHelper.selectAllUsersDialog(id));
                Thread myThread = Thread.currentThread();
                try {
                    myThread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while(resultSet.next()){
                    String recipient_Id = resultSet.getString("recipient");
                    String recipient_Login = resultSet.getString("login");
                    String text_ = resultSet.getString("mess_text");
                    String date_ = resultSet.getString("mess_date");
                    String read_Status = resultSet.getString("read_status");
                    String recipient_Name = Crypt2.decrypt(resultSet.getString("name"));
                    String recipient_Surname = Crypt2.decrypt(resultSet.getString("surname"));
                    String mess_Id = resultSet.getString("mess_id");
                    String sender_Id = resultSet.getString("sender");
                    arrMess.add(new Message(recipient_Id, recipient_Login, text_, date_,read_Status,recipient_Name,recipient_Surname,mess_Id,sender_Id));
                }
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


            PrintWriter out = response.getWriter();
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            System.out.println(gson.toJson(arrMess));
            out.println(gson.toJson(arrMess));
        }
    }
}

//class Message{
//    private String recipientId;
//    private String recipientLogin;
//    private String text;
//    private String date;
//    private String readStatus;
//    private String recipientName;
//    private String recipientSurname;
//    private String messId;
//    private String senderId;
//    Message(String recipientId, String recipientLogin, String text, String date,String readStatus,String recipientName,
//            String recipientSurname,String messId,String senderId){
//        this.recipientId = recipientId;
//        this.recipientLogin = recipientLogin;
//        this.text = text;
//        this.date = date;
//        this.readStatus = readStatus;
//        this.recipientName = recipientName;
//        this.recipientSurname = recipientSurname;
//        this.messId = messId;
//        this.senderId = senderId;
//    }
//}
