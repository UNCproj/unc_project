<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="beans.BeansHelper" %>
<%@ page import="beans.UserAccountBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="resources/css/angular-chart.css">
    <link rel="stylesheet" type="text/css" href="resources/css/template.css">
    <title>unc_project | Main Page</title>
</head>
<body>
    <div class="main">
            <div id="header">
                <ul class="menu">
                    <li><a class="a-outline button-style" href="index.jsp">Главная</a></li>
                </ul>
                <div class="enter">
                    <a class="button-style button-style-enter a-outline" href="reg-and-login.jsp">Войти</a>
                </div>
            </div>
            <div class="content">
                <a href="${pageContext.request.contextPath}/account?accountLogin=${userAccount.getLogin()}">Личный кабинет</a>
                <%
                    UserAccountBean accountBean = (UserAccountBean)session.getAttribute(BeansHelper.USER_ACCOUNT_SESSION_KEY);
                    if (accountBean != null && accountBean.isLoggedIn()) {
                %>
                    <a href="${pageContext.request.contextPath}/logout">Выйти из профиля</a>
                <%}%>
            </div>
            <c:catch var="e">
                <c:import url="/includes/object/footers/default.jspf" />
            </c:catch>
    </div>
</body>
</html>
