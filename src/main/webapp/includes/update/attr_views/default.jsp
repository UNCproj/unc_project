<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr>
    <td>
    ${param.attr_name_ru}
    </td>
    <td>
        <c:choose>
            <c:when test="${param.attr_type.equals(\"1\") ||
                            param.attr_type.equals(\"5\") ||
                            param.attr_type.equals(\"6\")}">
                <input id="${param.attr_name}" type="text" ng-init="object.${param.attr_name}='${param.attr_value}'" ng-model="object.${param.attr_name}" />
            </c:when>
            <c:when test="${param.attr_type.equals(\"2\")}">
                <input id="${param.attr_name}" type="password" ng-init="object.${param.attr_name}='${param.attr_value}'" ng-model="object.${param.attr_name}" />
            </c:when>
            <c:when test="${param.attr_type.equals(\"3\")}">
                <img id="${param.attr_name}" src="${param.attr_value}">
            </c:when>
            <c:when test="${param.attr_type.equals(\"4\")}">
                <textarea id="${param.attr_name}">${param.attr_value}</textarea>
            </c:when>
            <c:otherwise>
                <input id="${param.attr_name}" type="text" ng-init="object.${param.attr_name}='${param.attr_value}'" ng-model="object.${param.attr_name}" />
            </c:otherwise>
        </c:choose>
    </td>
</tr>
