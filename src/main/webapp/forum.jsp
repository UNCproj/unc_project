<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="beans.BeansHelper" %>
<%@ page import="beans.UserAccountBean" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="unc.helpers.UncObject" %>
<%@ page import="db.DataSource" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="db.SQLQueriesHelper" %>
<%@ page import="java.net.URLDecoder" %>
<%@ page import="org.hornetq.core.protocol.stomp.Stomp" %>
<html>
<head>
    <title>Форум</title>
    <script src="resources/scripts/jquery-1.12.0.min.js"></script>
    <script src="resources/scripts/angular.min.js"></script>
    <script src="resources/scripts/bootstrap.min.js"></script>
    <script src="resources/scripts/forum.js"></script>
    <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="resources/css/angular-chart.css">
    <link rel="stylesheet" type="text/css" href="resources/css/template.css">
    <link rel="stylesheet" type="text/css" href="resources/css/params.css">
    <link rel="stylesheet" type="text/css" href="resources/css/forum.css">
</head>
<body>
<%UserAccountBean userAccountBean = (UserAccountBean) request.getSession().getAttribute("userAccount");%>
<div class="main">

    <c:catch>
        <%@ include file="/includes/object/headers/default.jsp" %>
    </c:catch>

    <div class="content">

            <%
            String type = request.getParameter("type");
            String id = request.getParameter("id");

            if (type == null && id == null) {
        %>
        <div class="header-forum page-header">
            <h1>Форум</h1>
        </div>
            <%
            Connection connection = null;
            try {
                connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(SQLQueriesHelper.selectForumTopics());
                while (resultSet.next()) {
        %>

        <div class="forum-theme" onclick="location.href='forum.jsp?type=<%=resultSet.getString("ot_name")%>'">
            <div class="forum-theme-title">
                <%=resultSet.getString("ot_name")%>
            </div>
            <div class="forum-theme-description">
                <%if (resultSet.getString("ot_name").equals("Обсуждение")) {%>
                Данный раздел предназначен для обсуждения.
                <%} else if (resultSet.getString("ot_name").equals("Вопросы")) {%>
                Здесь вы можете задать свои вопросы.
                <%} else if (resultSet.getString("ot_name").equals("Предложения")) {%>
                Оставляйте свои пожелания и предложения по улучшению работы сайта.
                <%}%>
            </div>
        </div>
            <%
                }
            } finally {
                connection.close();
            }
        } else if (type != null && id == null) {
            type = URLDecoder.decode(type, "UTF-8");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + type);
            %>
        <div class="header-forum page-header">
            <h1><%=type%></h1>
        </div>
            <%
            Connection connection = null;
            try {
                connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(SQLQueriesHelper.selectselectForumTopicsWithAttrs(type));
                while (resultSet.next()) {
        %>
    <div style="position: relative">
        <div class="forum-topic" onclick="location.href='forum.jsp?id=<%=resultSet.getString("object_id")%>'">
            <div class="forum-topic-title">
                <%=resultSet.getString("object_name")%>
            </div>
            <div class="forum-topic-create">
                <%=resultSet.getString("creation_date")%><br>
                Комментарии:<%=resultSet.getString("numb_comments")%>
            </div>
        </div>
        <% if(userAccountBean!=null){
            System.out.println("!!!!!!!!!!!#" +resultSet.getString("creation_id").equals(userAccountBean.getId()));
        }
            if(userAccountBean != null && ((userAccountBean.isIsAdmin() || userAccountBean.isIsModer()) ||
                    resultSet.getString("creation_id").equals(userAccountBean.getId()))){%>
        <div class="delete-forum">
            <span class="glyphicon glyphicon-remove" id="<%=resultSet.getString("object_id")%>"></span>
            <%--<p id="<%=resultSet.getString("object_id")%>">Удалить обсуждение</p>--%>
        </div>
        <%}%>
    </div>
            <%
                }
            } finally {
                connection.close();
            }
            if (userAccountBean != null) {
                String userId = userAccountBean.getId();
        %>
        <div class="forum-create-topic">
            <%--<a href="unc_add.jsp?type=forum_topic&name=<%=type%>">Создать обсуждение</a>--%>
            <a class="a-outline button-style" href="unc_add.jsp?type=forum_topic&name=<%=type%>" style="width: 250px">Создать
                обсуждение</a>
        </div>
            <%
        } else {
        %>
        <div class="forum-information">
            <p>Чтобы создать обсуждение <a href="reg-and-login.jsp">войдите под своим аккаунтом или
                зарегестрируйтесь</a></p>
        </div>
            <%
            }


        } else if (type == null && id != null) {
            Connection connection = null;
            try {
                connection = DataSource.getInstance().getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(SQLQueriesHelper.selectForumsDiscussion(id));
                while (resultSet.next()) {
        %>
        <div class="forum-question">
            <div class="question-name"><%=resultSet.getString("object_name")%></div>
            <div class="question-description"><%=resultSet.getString("description")%></div>
            <div class="question-create"><%=resultSet.getString("creation_date")%>
                <a href="unc_object.jsp?id=<%=resultSet.getString("creation_id")%>">
                    <%=resultSet.getString("creation_login")%>
                </a>
            </div>
        </div>
            <%
                    Statement statement1 = connection.createStatement();
                    ResultSet resultSet1 = statement1.executeQuery(SQLQueriesHelper.selectForumComments(id));
                    while (resultSet1.next()) {
                %>
    <div style="position: relative" id="edew">
        <div class="forum-comment">
            <div class="comment-text"><%=resultSet1.getString("object_name")%>
            </div>
            <div class="comment-create"><%=resultSet1.getString("date_creation")%>
                <a href="unc_object.jsp?id=<%=resultSet1.getString("id_creation")%>">
                    <%=resultSet1.getString("login_creation")%>
                </a>
            </div>
        </div>
        <%if(userAccountBean != null && ((userAccountBean.isIsAdmin() || userAccountBean.isIsModer()) ||
                resultSet1.getString("id_creation").equals(userAccountBean.getId()))){%>
        <div class="delete-forum">
            <span class="glyphicon glyphicon-remove" id="<%=resultSet1.getString("object_id")%>"></span>
            <%--<p id="<%=resultSet1.getString("object_id")%>">Удалить обсуждение</p>--%>
        </div>
        <%}%>
    </div>
            <%
                    }
                    if (userAccountBean != null) {
                        String userId = userAccountBean.getId();
                %>
        <div class="forum-create-comment">
            <a class="a-outline button-style" href="unc_add.jsp?type=forum_comment&id=<%=request.getParameter("id")%>"
               style="width: 230px">Оставить комментарий</a>
        </div>
            <%
                } else {
                %>
        <div class="forum-information">
            <p>Чтобы оставить комментарий <a href="reg-and-login.jsp">войдите под своим аккаунтом или
                зарегестрируйтесь</a>
            </p>
        </div>
            <%
                                }
                            }
                        } finally {
                            connection.close();
                        }


                    }
                %>
                <%--<%System.out.println(userAccountBean != null);%>--%>
                <%--<%System.out.println(userAccountBean.isIsAdmin());%>--%>
                <%--<%System.out.println(userAccountBean.isIsModer());%>--%>
        <c:catch var="e">
            <c:import url="/includes/object/footers/default.jspf"/>
        </c:catch>
</body>
</html>
