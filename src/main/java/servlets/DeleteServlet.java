package servlets;

import beans.BeansHelper;
import beans.UserAccountBean;
import db.DataSource;
import db.SQLQueriesHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by alex on 18.05.2016.
 */
@WebServlet(name = "DeleteServlet", urlPatterns = "/delete")
public class DeleteServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Сработал DeleteServlet");
        System.out.println(request.getParameter("id"));

        String id = request.getParameter("id");
        String type = "";

        UserAccountBean userAccountBean = (UserAccountBean) request.getSession().getAttribute("userAccount");
        String userId = userAccountBean.getId();

        Connection connection = null;
        try {
            connection = DataSource.getInstance().getConnection();

            Statement statementType = connection.createStatement();
            ResultSet resultSetType = statementType.executeQuery(SQLQueriesHelper.findTypeIdById(id));
            if (resultSetType.next()) {
                type = resultSetType.getString("type");
                System.out.println("type" + type);
            }

            if (type.equals("1") && userId.equals(id)){
                System.out.println("Удаляем пользоавателя");
                Statement statement = connection.createStatement();

                ArrayList <String> advAr = new ArrayList();
                Statement statementAdverts = connection.createStatement();
                ResultSet resultSetAdverts = statementAdverts.executeQuery(SQLQueriesHelper.selectUsersAdverts(id));
                while(resultSetAdverts.next()){
                    advAr.add(resultSetAdverts.getString("object_id"));
                }

                statement.executeUpdate(SQLQueriesHelper.deleteUsersAdvertsParams(id));
                statement.executeUpdate(SQLQueriesHelper.deleteUsersAdvertsStat(id));
                statement.executeUpdate(SQLQueriesHelper.deleteUsersParams(id));
                statement.executeUpdate(SQLQueriesHelper.deleteReferences(id));
                for (int i=0;i<advAr.size();i++){
                    statement.executeUpdate(SQLQueriesHelper.deleteObject(advAr.get(i)));
                }
                statement.executeUpdate(SQLQueriesHelper.deleteObjectParams(id));
                statement.executeUpdate(SQLQueriesHelper.deleteObjectParams(id));
                statement.executeUpdate(SQLQueriesHelper.deleteObjectStat(id));
                statement.executeUpdate(SQLQueriesHelper.deleteObject(id));
                System.out.println("Все прошло успешно");
                request.getSession().setAttribute(BeansHelper.USER_ACCOUNT_SESSION_KEY, null);
                request.getSession().setAttribute(BeansHelper.VK_TOKEN, null);
                request.getSession().setAttribute(BeansHelper.VK_AUTHORIZATION_KEY, null);
                request.getSession().setAttribute(BeansHelper.VK_CODE, null);
                response.sendRedirect("/unc-project/index");
            } else if (type.equals("4")){
                System.out.println("Удаляем объявление");
                Statement statementRef = connection.createStatement();
                ResultSet resultSetRef = statementRef.executeQuery(SQLQueriesHelper.selectUserIdByAdvertId(id));
                if (resultSetRef.next()){
                    if (userId.equals(resultSetRef.getString("object_reference_id"))){
                        System.out.println("Это мое объявление");
                        Statement statement = connection.createStatement();
                        statement.executeUpdate(SQLQueriesHelper.deleteObjectParams(id));
                        statement.executeUpdate(SQLQueriesHelper.deleteObjectStat(id));
                        statement.executeUpdate(SQLQueriesHelper.deleteAdvertsRef(id));
                        statement.executeUpdate(SQLQueriesHelper.deleteObject(id));
                        System.out.println("Все прошло успешно");
                        response.sendRedirect("/unc-project/index.jsp");
                    }
                }
            } else if (type.equals("388") && userId.equals(id)){

            } else if (type.equals("392") && userId.equals(id)){

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
    }
}
