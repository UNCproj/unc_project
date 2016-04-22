package filters;

import beans.UserAccountBean;
import db.DataSource;
import db.SQLQueriesHelper;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebFilter(urlPatterns = "/chat.jsp")
public class ChatFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String url = String.valueOf(httpRequest.getRequestURL());
        String id = httpRequest.getParameter("id");
        System.out.println(url + "?id=" + id);

        Connection connection = null;
        try{
            connection = DataSource.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQLQueriesHelper.verificationUser(id));
            String newId = null;
            if (resultSet.next()) {
                newId = resultSet.getString("object_id");
            }
            if(id.equals(newId)){
                chain.doFilter(request, response);
            }else if(!id.equals(newId)){
                httpResponse.sendRedirect(url + "?id=" + resultSet.getString("object_id"));
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

    @Override
    public void destroy() {

    }
}
