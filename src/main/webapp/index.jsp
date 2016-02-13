<%@ page import="beans.BeansHelper" %>
<%@ page import="beans.UserAccountBean" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>unc_project | Main Page</title>
</head>
<body>
    <a href="${pageContext.request.contextPath}/account?accountLogin=${userAccount.getLogin()}">Личный кабинет</a>
    <%
        UserAccountBean accountBean = (UserAccountBean)session.getAttribute(BeansHelper.USER_ACCOUNT_SESSION_KEY);
        if (accountBean != null && accountBean.isLoggedIn()) {
    %>
        <a href="${pageContext.request.contextPath}/logout">Выйти из профиля</a>
    <%}%>
</body>
</html>
