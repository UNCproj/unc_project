<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:choose>
    <c:when test="${param.attr_type.equals(\"1\") ||
                    param.attr_type.equals(\"5\") ||
                    param.attr_type.equals(\"6\")}">
        <label for="${param.attr_name}">${param.attr_name_ru}</label>
        <input id="${param.attr_name}" type="text" ng-init="object.${param.attr_name}='${param.attr_value}'" ng-model="object.${param.attr_name}" />
    </c:when>
    <c:when test="${param.attr_type.equals(\"2\")}">
        <label for="${param.attr_name}">${param.attr_name_ru}</label>
        <input id="${param.attr_name}" type="password" ng-init="object.${param.attr_name}='${param.attr_value}'" ng-model="object.${param.attr_name}" />
    </c:when>
    <c:when test="${param.attr_type.equals(\"3\")}">
        <label for="${param.attr_name}">${param.attr_name_ru}</label>
        <img id="${param.attr_name}" src="${param.attr_value}">
    </c:when>
    <c:when test="${param.attr_type.equals(\"4\")}">
        <label for="${param.attr_name}">${param.attr_name_ru}</label>
        <textarea id="${param.attr_name}">${param.attr_value}</textarea>
    </c:when>
    <c:otherwise></c:otherwise>
</c:choose>
<br />