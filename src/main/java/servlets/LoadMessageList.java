package servlets;

import beans.UserAccount;
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
@WebServlet(name = "LoadMessageList", urlPatterns = "/loadMesList")
public class LoadMessageList extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                while(resultSet.next()){
                    String recipientId = resultSet.getString("recipient");
                    String recipientLogin = resultSet.getString("login");
                    String text = resultSet.getString("mess_text");
                    String date = resultSet.getString("mess_date");
                    arrMess.add(new Message(recipientId, recipientLogin, text, date));
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

class Message{
    private String recipientId;
    private String recipientLogin;
    private String text;
    private String date;
    Message(String i, String l, String t, String d){
        recipientId = i;
        recipientLogin = l;
        text = t;
        date = d;
    }
        }
