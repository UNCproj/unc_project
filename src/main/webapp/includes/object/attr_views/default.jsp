<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tr>
    <td>
    ${param.attr_name_ru}
    </td>
    <td>
        <c:choose>
            <c:when test="${param.attr_type.equals(\"1\")}">
                ${param.attr_value}
            </c:when>
            <c:when test="${param.attr_type.equals(\"2\")}">
                'Скрыто'
            </c:when>
            <c:when test="${param.attr_type.equals(\"3\")}">
                <img src="${param.attr_value}">
            </c:when>
            <c:when test="${param.attr_type.equals(\"4\")}">
                ${param.attr_value}
            </c:when>
            <c:otherwise></c:otherwise>
        </c:choose>
    </td>
</tr>