<%-- 
    Document   : unc_object
    Created on : 04.03.2016, 11:03:18
    Author     : Andrey
--%>
<%@page import="beans.BeansHelper"%>
<%@ page import="beans.RecommenderBean" %>
<%@ page import="beans.UserAccountBean" %>
<%@page import="org.jboss.as.naming.InitialContext"%>
<%@page import="org.jboss.logging.Logger"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="unc.helpers.Param" %>
<%@ page import="unc.helpers.UncObject" %>
<%@ page import="javax.naming.Context" %>
<%@ page import="java.beans.PropertyVetoException" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Properties" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="javax.ejb.EJB" %>
<%@ page import="beans.AdvertsManager" %>
<%
    Logger log = Logger.getLogger("unc_obj_log");
    String objectId = request.getParameter("id");
    UncObject currentObject = new UncObject(objectId, null, true);
    request.setAttribute("currentObjectType", currentObject.getParentType());

    if (currentObject.getType() == null) {
        response.sendRedirect("jsp404.jsp");
    }

    try {
        currentObject.selectFromDB();
    } catch (SQLException | PropertyVetoException e) {
        e.printStackTrace();
        response.sendRedirect("error.jsp");
    }

    Param prm = currentObject.getParam("is_invalid");
    Param prm_del_msg = currentObject.getParam("delete_msg");
    Param prm_del_id = currentObject.getParam("delete_by");
    String del_id = "", del_msg = "";
    if ((prm != null) && (prm.getValue() != null) && (prm.getValue().equals("true"))) {
        if (prm_del_msg != null) {
            del_msg = prm_del_msg.getValue();
        }
        if (prm_del_id != null) {
            del_id = prm_del_id.getValue();
        }
        //response.sendRedirect("ModerServlet/deleted?del_id="+del_id+"&del_msg="+del_msg);
        response.sendRedirect("jsp404.jsp");
    }

    ArrayList<String> listCategories = currentObject.selectCategory(currentObject.getType());

    UserAccountBean user = (UserAccountBean) request.getSession().getAttribute(BeansHelper.USER_ACCOUNT_SESSION_KEY);
    if (user != null) {
        request.setAttribute("uname", user.getId());
    }
    
    if (request.getSession().getAttribute("RobokassaFail") != null && (boolean) request.getSession().getAttribute("RobokassaFail")) { %>
    <script language=JavaScript>
        alert("Покупка не была совершена!");
    </script>
    <% request.getSession().setAttribute("RobokassaFail", false); 
} %> 

<!DOCTYPE html>
<html ng-app="objectSettings">
    <head>
        <c:catch var="e">
            <c:import url="/includes/object/scripts/${currentObjectType}.jspf" />
        </c:catch>
        <c:if test="${!empty e}">
            <c:import url="/includes/object/scripts/default.jspf" />
        </c:if>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%= currentObject.getName()%></title>
        <c:catch var="e">
            <c:import url="/includes/object/css/<%= currentObject.getParentType()%>.jspf" />
        </c:catch>
        <c:if test="${!empty e}">
            <c:import url="/includes/object/css/default.jspf" />
        </c:if>
    </head>
    <body>
        <% if (user != null) {%>
            <input id="rights" type="hidden" value="<%= user.isIsAdmin() ? "admin" : (user.isIsModer() ? "moderator" : "user")%>">
        <% } %>
        <script type="application/javascript">
            var uncObjectType = ${currentObjectType};
        </script>
        <div class="main">
            <c:catch>
                <%@ include file="/includes/object/headers/default.jsp" %>
            </c:catch>
            <div class="content">
                <%if (!"1".equals(currentObject.getType())) { %>
                <div class="list-categories clearfix">
                    <ul>
                        <li class="list-categories-li">
                            <a href="/unc-project/index.jsp">Главная</a>
                        </li>
                        <% for (int i = 0; i < listCategories.size(); i++) { %>
                        <li class="list-categories-li">
                            <script type="text/javascript">
                                var temp = encodeURIComponent('<%= listCategories.get(i)%>');
                                document.write('<a href="/unc-project/index.jsp?categoryName=' + temp + '"><%= listCategories.get(i)%></a>');
                            </script>
                        </li>
                        <% } %>
                        <li class="list-categories-li"><%= currentObject.getName()%></li>
                    </ul>
                </div>
                <% } %>
                <ul class="custom-tabs nav nav-tabs tabs" id="tab_name">
                    <% if (currentObject.getAttributeGroups() != null && currentObject.getAttributeGroups().size() != 0) { %>
                    <li class="active"><a href="#tab<%= currentObject.getAttributeGroups().get(0)%>" data-toggle="tab">Основная информация</a></li>
                        <% for (int i = 1; i < currentObject.getAttributeGroups().size(); i++) {%>
                    <li><a href="#tab<%= currentObject.getAttributeGroups().get(i)%>" data-toggle="tab">Дополнительная информация</a></li>
                        <% } %>
                        <%if ("1".equals(currentObject.getType())) {%>
                    <li><a href="#statid" data-toggle="tab">Статистика</a></li>
                        <% } %>
                        <%if ("4".equals(currentObject.getParentType()) && user != null && user.getId().equals(request.getParameter("id"))) {%>
                    <li><a href="#adstatid" data-toggle="tab">Статистика просмотров</a></li>
                        <% } %>
                        <%if ((user != null) && (user.isIsAdmin()) && (currentObject.getId().equals(user.getId()))) {%>
                    <li><a href="#adminka" data-toggle="tab">Администрирование</a></li>
                        <% } %>
                        <%if ((user != null) && (user.isIsAdmin()) && (!currentObject.getId().equals(user.getId()))) {%>
                    <li><a href="#adm" data-toggle="tab">Администрирование</a></li>
                        <% } %>
                        <%if ("1".equals(currentObject.getType()) && user != null && user.getId().equals(request.getParameter("id"))) {%>
                    <li><a href="#messages" data-toggle="tab">Мои сообщения</a></li>
                        <% } %>
                    <% } %>
                </ul>
                <div class="settings tab-content" id="tab_content">
                    <% String activeSwicth1 = "active", s; 
                       boolean pics_select = true, photo_type = true, advert_name = true; %>
                    <% for (int i = 0; i < currentObject.getAttributeGroups().size(); i++) {
                            String attrGroupName = currentObject.getAttributeGroups().get(i);
                            ArrayList<Param> currentGroupParams = currentObject.getParams(attrGroupName);%>
                    <div id="tab<%= currentObject.getAttributeGroups().get(i)%>" class="tab-pane fade in <%= activeSwicth1%>">
                        <div class="table-pos">
                            <table class="table table-params">
                                <% if ((currentObject.getParam("is_admin") != null)
                                            && (currentObject.getParam("is_admin").getValue() != null)
                                            && (currentObject.getParam("is_admin").getValue().equals("true"))) {%> 
                                <%}%>
                                <%  for (int j = 0; j < currentGroupParams.size(); j++) { %>
                                <% if ((currentGroupParams.get(j).getType() != null) && (currentGroupParams.get(j).getType().equals("2"))) {
                                        continue;
                                    }%>
                                <%  ArrayList<String> pics = null;
                                    
                                    if ((currentGroupParams.get(j).getType() != null) && currentGroupParams.get(j).getType().equals("3")) {
                                        if (pics_select) {
                                            pics = new ArrayList<>();
                                            System.out.println(currentGroupParams.get(j).getValue());
                                            pics.add(currentGroupParams.get(j).getValue());
                                            pics_select = false;
                                            for (int k = j + 1; k < currentGroupParams.size(); k++) {
                                                System.out.println(currentGroupParams.get(k).getValue());
                                                if (currentGroupParams.get(k).getType().equals("3")) {
                                                    System.out.println(currentGroupParams.get(k).getValue());
                                                    pics.add(currentGroupParams.get(k).getValue());
                                                }
                                            }
                                            request.setAttribute("list", pics);
                                            request.setAttribute("user_photo", currentObject.getType());
                                        }
                                        else {
                                            continue;
                                        }
                                    }
                                %>
                                <% if (photo_type && i == 0 && j == 1 && (currentObject.getParam("is_admin") != null)
                                            && (currentObject.getParam("is_admin").getValue() != null)
                                            && (currentObject.getParam("is_admin").getValue().equals("true"))) { 
                                            j--;
                                            photo_type = false;
                                %>
                                <tr>
                                    <td class="attr-center" >
                                        Тип пользователя
                                    </td>
                                    <td>
                                        <img class="img-rule" src="/unc-project/resources/img/admin_logo.png">
                                    </td>
                                </tr>
                                <%} else if (photo_type && i == 0 && j == 1 && (currentObject.getParam("is_moderator") != null)
                                            && (currentObject.getParam("is_moderator").getValue() != null)
                                            && (currentObject.getParam("is_moderator").getValue().equals("true"))){
                                            j--;
                                            photo_type = false;
                                %>
                                <tr>
                                    <td class="attr-center" >
                                        Тип пользователя
                                    </td>
                                    <td>
                                        <img class="img-rule" src="/unc-project/resources/img/moder.png">
                                    </td>
                                </tr>
                                <% } else if (advert_name && "4".equals(currentObject.getParentType()) && i == 0 && j == 1) {
                                              advert_name = false;
                                              j--;  
                                %>
                                <tr>
                                    <td class="attr-center" >
                                        Название
                                    </td>
                                    <td>
                                        <%= currentObject.getName() %>
                                    </td>
                                </tr>
                                <%} else if ("4".equals(currentObject.getParentType()) && currentGroupParams.get(j).getType().equals("8")) {
                                    continue;                                              
                                } else { %>
                                <c:catch var="e">
                                    <c:import url="/includes/object/attr_views/<%= currentObject.getType()%>.jsp" />
                                </c:catch>
                                <c:if test="${!empty e}">
                                    <c:import url="/includes/object/attr_views/default.jsp">
                                        <c:param name="attr_name" value="<%= currentGroupParams.get(j).getName()%>" />
                                        <c:param name="attr_name_ru" value="<%= currentGroupParams.get(j).getRuName()%>" />
                                        <c:param name="attr_value" value="<%= currentGroupParams.get(j).getValue()%>" />
                                        <c:param name="attr_type" value="<%= currentGroupParams.get(j).getType()%>" />
                                    </c:import>
                                </c:if>       
                                <% } %>
                                <%} %>
                                <% if ("4".equals(currentObject.getParentType()) && user != null) { %>
                                <tr>
                                    <td>
                                    </td>
                                    <td>
                                        <script>
                                            var isInBookmarksGlobal = <%=user.isInBookmarks(request.getParameter("id"))%>;
                                        </script>
                                        <a class="a-outline button-style"
                                           ng-controller="BookmarksController"
                                           ng-click="addOrDeleteBookmark('<%= request.getParameter("id")%>')"
                                           style="width: 200px; cursor: pointer">
                                            {{bookmarksButtonText}}
                                        </a>
                                    </td>
                                </tr>
                                <% } %>
                            </table>
                        </div>
                        <div>
                            <% ArrayList<String[]> listReferences = currentObject.lisrReferences(); %>
                            <% if ("1".equals(currentObject.getType()) && user != null && user.getId().equals(request.getParameter("id"))) {%>
                            <a class="a-outline button-style" href="unc_update.jsp?id=<%=request.getParameter("id")%>" style="width: 200px">
                                Изменить данные
                            </a>
                            <%}%>
                        </div>
                        <div>
                            <% if ("4".equals(currentObject.getParentType()) && user != null && listReferences.get(0)[0].equals(user.getId()) ) {%>
                            <a class="a-outline button-style" href="unc_update.jsp?id=<%=request.getParameter("id")%>" style="width: 200px">
                                Изменить данные
                            </a>
                            <%}%>
                        </div>
                            <%if ("4".equals(currentObject.getParentType()) && user != null && user.isLoggedIn() && !currentObject.isVip() && user.getId().equals(request.getParameter("id"))) {%>
                                <div class="clearfix robokassa-button" >
                                    <ul>
                                        <li class="robo-li robokassa-li">
                                            Оплатить Vip-статус за 10 р
                                        </li>

                                        <%
                                            String sMrchLogin = "UNC-project";
                                            String sMrchPass1 = "GVic7cAaCBs52j71lCuk";
                                            //String sMrchPass2 = "n8DcTF8ogp410TZQqJgI";
                                            String nInvId = "100";
                                            String sDesc = "Test payment ROBOKASSA";
                                            String sOutSum = "10";
                                            String sCulture = "ru";
                                            String sEncoding = "utf-8";
                                            String shp_id_a = currentObject.getId();
                                            String crc = currentObject.MD5(new String[]{sMrchLogin, sOutSum, nInvId, sMrchPass1, "shp_id_a=" + shp_id_a});
                                        %>
                                        <li class="robo-li">
                                            <script language=JavaScript src='https://auth.robokassa.ru/Merchant/PaymentForm/FormSS.js?MerchantLogin=<%= sMrchLogin%>&OutSum=<%= sOutSum%>&InvoiceID=<%= nInvId%>&Description=<%= sDesc%>&shp_id_a=<%= shp_id_a%>&isTest=1&SignatureValue=<%= crc%>'></script>
                                        </li>
                                    </ul>
                                </div>
                            <%}%>
                        <div class="references">
                            <% if ("1".equals(currentObject.getType())) { %>
                            <h4>Список обьявлений</h4>
                            <ul class="references-ul">
                                <% for (int k = 0; k < listReferences.size(); k++) { %>
                                <% if (!listReferences.get(k)[2].equals("true")) {%>
                                <li class="references-ul-li">
                                    <a a href="unc_object.jsp?id=<%= listReferences.get(k)[0]%>"><%= listReferences.get(k)[1]%></a>
                                </li>
                                <%} else {%>
                                <li class="references-ul-li">
                                    <a a href="unc_object.jsp?id=<%= listReferences.get(k)[0]%>"><%= listReferences.get(k)[1]%></a> : Заблокированно
                                </li>
                                <%}%>
                            <% } %>
                            </ul>
                            <%} else if ("4".equals(currentObject.getParentType())) {%>
                            <h4 class="robokassa-li">Продавец : <a a href="unc_object.jsp?id=<%= listReferences.get(0)[0]%>"><%= listReferences.get(0)[1]%></a></h4>
                                <%}%>
                        </div>
                    </div>
                    <% activeSwicth1 = ""; %>
                    <% } %>
                    <%if ("1".equals(currentObject.getType())) {%>
                    <div id="statid" class="tab-pane fade in">
                        <div ng-controller="LineCtrl" >
                            <div id = "dropdown1" action="" class="tab-pane fade in" ng-show="isExistData">
                                <div class="dropdown">
                                    <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                                        {{selectedItem.name}}
                                        <span class="caret"></span>
                                    </button>
                                    <ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
                                        <li ng-repeat="a in subjects"><a ng-click="dropboxitemselected(a)">{{a.name}}</a></li>
                                    </ul>
                                </div>
                                <canvas id="line1" class="chart chart-line" chart-data="data" 
                                        chart-labels="labels" chart-legend="true" chart-series="series"
                                        chart-click="onClick" chart-options="opts" width="800" height="200" >
                                </canvas>
                            </div>
                        </div>
                        <div id="ifempty"></div>
                    </div>
                    <% } %>                       
                    <%if ("4".equals(currentObject.getParentType())) {%>
                    <div id="adstatid" class="tab-pane fade in">

                        <input type="hidden" id="hidden_user_name" value="${uname}">
                        <h3>Статистика просмотров объявления:</h3>
                        <div ng-controller="AdvertStatCtrl">
                            <div class="graph-pos" ng-controller="AdvertStatCtrl">
                                <canvas id="line2" class="chart chart-line" chart-data="data"
                                        chart-labels="labels" chart-legend="true" chart-series="series"
                                        chart-click="onClick" chart-options="opts" width="800" height="400">
                                </canvas>
                            </div>
                        </div>
                    </div>
                    <% } %>
                    <div  id="adminka" class="tab-pane fade in" >
                        <%if ("1".equals(currentObject.getParentType())) {%>
                        <div ng-controller="AdminCtrl">
                            <ul class="custom-tabs nav nav-tabs tabs" id="tn">
                                <li class="active"><a href="#user_list" data-toggle="tab">Список пользователей</a></li>
                                <li><a href="#migr" data-toggle="tab">Миграция</a></li>
                                <li><a href="#reg_stat" data-toggle="tab">Статистика регистраций</a></li>
                            </ul>
                            <div class="settings tab-content" id="usrs_tab_content">
                                <div id="user_list" class="tab-pane fade in active">
                                    <div>
                                        <pagination num-pages="numPages" total-items="totalItems" ng-model="currentPage" ng-change="pageChanged()" class="pagination-sm" items-per-page="itemsPerPage"></pagination>
                                        Записей на странице:  <select ng-model="viewby" ng-change="setItemsPerPage(viewby)"><option>3</option><option>5</option><option>10</option><option>20</option><option>30</option><option>40</option><option>50</option></select>
                                        <table class="table">
                                            <thead>
                                                <tr>
                                                    <td class="users-td" ><b>Ссылка<b></td>
                                                    <td class="users-td" ><b>Логин<b></td>
                                                    <td class="users-td"><b>Имя</b></td>
                                                    <td class="users-td"><b>Фамилия</b></td>
                                                    <td class="users-td"><b>Фото<b></td>
                                                    <td class="users-td"><b>Права</b></td>
                                                    <td class="users-td"><b>Статус</b></td>
                                                    <td class="users-td"><b>Действия</b></td>
                                                    </tr>
                                            </thead>
                                            <tbody>
                                                <tr ng-repeat="row in users.slice(((currentPage - 1) * itemsPerPage), ((currentPage) * itemsPerPage))">
                                                    <td class="users-td"><a ng-href="{{'/unc-project/unc_object.jsp?id=' + row.id}}">Перейти</a></td>
                                                    <td class="users-td">{{row.login}}</td>
                                                    <td class="users-td">{{row.firstName}}</td>
                                                    <td class="users-td">{{row.surname}}</td>
                                                    <td class="users-td"><img width="100px" height="100px" ng-src="{{row.userPicFile != null ? row.userPicFile : '/unc-project/resources/img/default.png'}}"/></td>
                                                    <td class="users-td" ng-class="{'moder-td': row.isModer == true, 'admin-td': row.isAdmin == true}">
                                                        {{row.isAdmin === true ? 'Администратор' : (row.isModer == true ? 'Модератор' : 'Пользователь')}}
                                                    </td>
                                                    <td class="users-td" ng-class="{'notblocked-td': row.isInvalid != true, 'blocked-td': row.isInvalid == true}">
                                                        {{row.isInvalid === true ? "Заблокирован" : "Обычный"}}
                                                    </td>
                                                    <td class="users-td admin-control">
                                                        <span ng-if="(true)">
                                                            <button style="float:top;" ng-if="(row.isInvalid != true)" type="button" class="btn btn-danger btn-small" ng-click="set_del_id(row.id); clickToDel()">
                                                                Заблокировать
                                                            </button>
                                                            <button style="float:top;" ng-if="(row.isInvalid === true)" type="button" class="btn btn-danger btn-small" ng-click="set_del_id(row.id); clickToUnblock()">
                                                                Разблокировать
                                                            </button>
                                                            <button style="float:top;" ng-if="(row.isModer === true)" type="button" class="btn btn-danger btn-small" ng-click="clickToModer(row.id, 'false', 'moderator')">
                                                                Снять права модератора
                                                            </button>
                                                            <button style="float:top;" ng-if="(row.isModer != true)" type="button" class="btn btn-danger btn-small" ng-click="clickToModer(row.id, 'true', 'moderator')">
                                                                Сделать модератором
                                                            </button>
                                                            <button style="float:top;" ng-if="(row.isAdmin != true)" type="button" class="btn btn-danger btn-small" ng-click="clickToModer(row.id, 'true', 'admin')">
                                                                Сделать администратором
                                                            </button>
                                                            <button style="float:top;" ng-if="(row.isAdmin === true)" type="button" class="btn btn-danger btn-small" ng-click="clickToModer(row.id, 'false', 'admin')">
                                                                Снять права администратора
                                                            </button>
                                                        </span>
                                                    </td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                                <div id="migr" class="tab-pane fade in">
                                    <div ng-controller="MigrationUser" class="references">
                                        <div
                                            flow-init="{target: '/unc-project/MigrationUserServlet', testChunks:false}"
                                            flow-file-added="fileAdded($file, $event, $flow)"
                                            flow-name="uploader.flow"
                                            flow-file-success="success($message)">
                                            <h3>Миграция пользователей</h3>
                                            <button type="button" flow-btn>Выбрать excel</button>
                                        </div>
                                        <h3>{{CountRowUser}}</h3>
                                        <h3>{{err}}</h3>
                                        <ul class="references-ul">
                                            <li class="references-ul-li" ng-repeat="x in listerr">
                                                {{x}}
                                            </li>
                                        </ul>
                                    </div>
                                    <div ng-controller="MigrationAdvert" class="references">
                                        <div
                                            flow-init="{target: '/unc-project/MigrationAdvertServlet', testChunks:false}"
                                            flow-file-added="fileAdded($file, $event, $flow)"
                                            flow-name="uploader.flow"
                                            flow-file-success="success($message)">
                                            <h3>Миграция обьявлений</h3>
                                            <button type="button" flow-btn>Выбрать excel</button>
                                        </div>
                                        <h3>{{CountRowAdvert}}</h3>
                                        <h3>{{err}}</h3>
                                        <ul class="references-ul">
                                            <li class="references-ul-li" ng-repeat="x in listerr">
                                                {{x}}
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                                <div id="reg_stat" class="tab-pane fade in">
                                    <h3>Количество новых пользователей</h3>
                                    <canvas id="regs_graph" class="chart chart-line" chart-data="reg_data.count" 
                                            chart-labels="reg_data.date" chart-legend="true" chart-series="reg_data.series"
                                            chart-options="reg_data.opts" width="800" height="200" >
                                    </canvas>
                                    <h3>Общее количество пользователей</h3>
                                    <canvas id="regs_graph" class="chart chart-line" chart-data="reg_data.count" 
                                            chart-labels="reg_data.date" chart-legend="true" chart-series="reg_data.series"
                                            chart-options="reg_data.opts" width="800" height="200" >
                                    </canvas>
                                </div>
                            </div>
                        </div>
                        <%}%>
                    </div>
                    <div id="adm" class="tab-pane fade in">

                        <% if ((user != null) && user.isIsModer() && user.isIsAdmin() && ("4".equals(currentObject.getParentType()))) { %>
                        <br>
                        <div>
                            <div ng-controller="ModerCtrl">
                                <button class="btn btn-primary" ng-click="clickToDel();">
                                    <nobr>Удалить объявление</nobr>
                                </button>
                            </div>
                        </div>
                        <%
                            }
                        %>
                    </div>
                    <%if ("1".equals(currentObject.getType()) && user != null && user.getId().equals(request.getParameter("id"))) {%>
                    <div id="messages" class="tab-pane fade in">
                        <div ng-controller="MessagesController">
                            <div class="message-list"></div>
                        </div>
                    </div>
                    <% } %>
                    <br>
                </div>
                <%if ("1".equals(currentObject.getType()) && !user.getId().equals(request.getParameter("id"))) {%>
                <div>
                    <a class="a-outline button-style" href="chat.jsp?id=<%=request.getParameter("id")%>" style="width: 230px">
                        Отправить сообщение
                    </a>
                </div>
                <%}%>
                <c:catch var="e">
                    <c:import url="/includes/object/footers/default.jspf" />
                </c:catch>
            </div>
        </div>
    </body>
</html>
