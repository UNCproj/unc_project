<%@ page import="java.sql.Connection" %>
<%@ page import="db.DataSource" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="db.SQLQueriesHelper" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.google.gson.GsonBuilder" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.beans.PropertyVetoException" %>
<%@ page import="beans.UserAccountBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    UserAccountBean userAccountBean = (UserAccountBean) request.getSession().getAttribute("userAccount");
    String userId = userAccountBean.getId();

    Connection connection = null;
    try {
        connection = DataSource.getInstance().getConnection();
        Statement statementOutputUsers = connection.createStatement();
        ResultSet resultSetOutputUsers =  statementOutputUsers.executeQuery(SQLQueriesHelper.outputUsers(userId));%>
        <p>Выберите пользователя которому хотите написать:</p>
        <form action="chat.jsp" method="get">
        <select name="name">
        <option disabled>Выберите пользователя</option>
        <%while (resultSetOutputUsers.next()) {
            out.println("<option value=\""+resultSetOutputUsers.getString("object_name")+"\">"+
                    resultSetOutputUsers.getString("object_name")+"</option>");
        }%>
        </select>
        <input type="submit">
        </form>
    <%} catch (SQLException e) {
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
%>

</body>
</html>
