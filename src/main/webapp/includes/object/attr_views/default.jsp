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
                'Скрыто'
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
            <c:otherwise>${param.attr_value}</c:otherwise>
        </c:choose>
    </td>
</tr>