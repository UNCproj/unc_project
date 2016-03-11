<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr>
    <td>
    ${param.attr_name}
    </td>
    <td>
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
    </td>
</tr>