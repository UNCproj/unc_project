<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr>
    <td class="attr-center" >
        ${param.attr_name_ru}
    </td>
    <td>
        <c:choose>
            <c:when test="${param.attr_type.equals(\"1\")}">
                ${param.attr_value}
            </c:when>
            <c:when test="${param.attr_type.equals(\"2\")}">
                
            </c:when>
            <c:when test="${param.attr_type.equals(\"3\")}">
                <c:choose>
                    <c:when test="${!param.attr_value.equals(\"\")}">
                        <img class="img" src="${param.attr_value}">
                    </c:when>
                    <c:otherwise>
                        <img class="img" src="/unc-project/resources/img/default.png">
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:when test="${param.attr_type.equals(\"7\")}">
                <c:choose>
                    <c:when test="${!param.attr_value.equals(\"\")}">
                        ${param.attr_value}
                    </c:when>
                    <c:otherwise>
                       Обычный
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:when test="${param.attr_type.equals(\"8\")}">
                <div id="map" coords='${param.attr_value}'
                     data-toggle="modal" data-target="#mapModal"></div>
                <div id="mapModal" tabindex="-1" role="dialog" class="modal fade modal-fullscreen" role="dialog">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                                <h4 class="modal-title">Местоположение</h4>
                            </div>
                            <div class="modal-body">
                                <div id="mapExtended" coords='${param.attr_value}'></div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:when>
            <c:otherwise>${param.attr_value}</c:otherwise>
        </c:choose>
    </td>
</tr>