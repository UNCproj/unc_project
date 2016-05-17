<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<div id="header">
    <ul class="menu">
            <li><a class="a-outline button-style" href="index.jsp">Главная</a></li>
        <li><a class="a-outline button-style" href="unc_add.jsp?type=advert">Новое объявление</a></li>
        <li><a class="a-outline button-style" href="forum.jsp">Форум</a></li>
        <%--<li><a class="a-outline button-style" href="faq.jsp">FAQ</a></li>--%>
        <li class="private-office">
            <a class="button-style a-outline" href="bookmarks.jsp">
                Закладки
            </a>
        </li>
        </ul>
    <%
        UserAccountBean accountBean = (UserAccountBean)session.getAttribute(BeansHelper.USER_ACCOUNT_SESSION_KEY);
        if (accountBean != null && accountBean.isLoggedIn()) {
    %>
        <div class="enter-login clearfix">
            <ul class="enter-login-ul">
                <li class="private-office">
                    <a class="private-office button-style a-outline" href="${pageContext.request.contextPath}/unc_object.jsp?id=${userAccount.getId()}">Личный кабинет</a>
                </li>
                <li>
                    <a class="button-style a-outline" href="${pageContext.request.contextPath}/logout">Выйти</a>
                </li>
            </ul>
        </div>
    <%} else {%>
        <div class="enter">
            <a class="button-style button-style-enter a-outline" href="reg-and-login.jsp">Войти</a>
        </div>
    <%}%>
</div>

