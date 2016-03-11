<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div>
    <label>${param.attr_name}</label>
    <display:column>
        <c:choose>
            <c:when test="${param.attr_type.equals(\"1\")}">
                <input type="text" ng-init="object.${param.attr_name}='${param.attr_value}'" ng-model="object.${param.attr_name}" />
            </c:when>
            <c:when test="${param.attr_type.equals(\"2\")}">
                <input type="password" ng-init="object.${param.attr_name}='${param.attr_value}'" ng-model="object.${param.attr_name}" />
            </c:when>
            <c:when test="${param.attr_type.equals(\"3\")}">
                <img src="${param.attr_value}">
            </c:when>
            <c:when test="${param.attr_type.equals(\"4\")}">
                <textarea>${param.attr_value}</textarea>
            </c:when>
            <c:otherwise></c:otherwise>
        </c:choose>
    </display:column>
</div>