package servlets;

import beans.UserAccountBean;
import db.DataSource;
import db.SQLQueriesHelper;
import unc.helpers.UncObject;

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
import java.util.Enumeration;

/**
 * Created by alex on 15.05.2016.
 */
@WebServlet(name = "CheckNewMessage", urlPatterns = "/checkNewMessage")

public class CheckNewMessage extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Сработал CheckNewMessage");
        System.out.println("Пришло - " + request.getParameter("action"));
        String count;
        PrintWriter out = response.getWriter();

        UserAccountBean userAccountBean = (UserAccountBean) request.getSession().getAttribute("userAccount");
        if (userAccountBean != null) {
            String userId = userAccountBean.getId();
            count = "0";

            Connection connection = null;
            try{
                connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement();
                int n = 0;
                while (count.equals("0") && n<3) {
                    ResultSet resultSet = statement.executeQuery(SQLQueriesHelper.selectCountNewMessage(userId));
                    if (resultSet.next()) {
                        count = resultSet.getString("new_message");
                        Thread myThread = Thread.currentThread();
                        myThread.sleep(1000);
                    }
                    n++;
                }

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

            out.println(count);

        } else {
            out.println(-1);
        }

    }
}
