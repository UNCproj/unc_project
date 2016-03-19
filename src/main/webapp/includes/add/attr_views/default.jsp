<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div>
        <label>${param.attr_name_ru}</label>
    <display:column>
        <c:choose>

            <c:when test="${param.attr_type.equals(\"1\")}">
                <input type="text" ng-model="object.${param.attr_name}"/>
            </c:when>

            <c:when test="${param.attr_type.equals(\"2\")}">
                <input type="password"  ng-model="object.${param.attr_name}" />
            </c:when>

            <c:when test="${param.attr_type.equals(\"3\")}">
                <%--<img src="${param.attr_value}">--%>
                <input type="file" ng-model="object.${param.attr_name}" multiple="multiple"/>
                <img src="${param.attr_value}">
                <%--<button type="button" ng-model="object.${param.attr_name}" flow-btn></button>--%>
            </c:when>

            <c:when test="${param.attr_type.equals(\"4\")}">
                <textarea ng-model="object.${param.attr_name}"></textarea>
            </c:when>

            <c:when test="${param.attr_type.equals(\"5\")}">
                <input type="number" ng-model="object.${param.attr_name}"/>
            </c:when>

            <c:when test="${param.attr_type.equals(\"6\")}">
                <select ng-model="object.${param.attr_name}"
                        <%--required ng-options="n for n in ${param.attr_name} track by n">--%>
                        required ng-options="n for n in ${param.attr_name}">
                    <option disabled value="">--...--</option>
                </select>
            </c:when>

            <c:when test="${param.attr_type.equals(\"7\")}">
                <select ng-model="object.category" required
                        ng-options="c.label for c in category track by c.label">
                    <option disabled value="">--...--</option>
                </select>

                <span ng-show="object.category.type">
                    <select ng-model="object.type" required
                            ng-options="t.label for t in object.category.type track by t.label">
                        <option disabled value="">--...--</option>
                    </select>
                </span>

                <span ng-show="object.type.typeType">
                    <select ng-model="object.typeType" required
                            ng-options="t.label for t in object.type.typeType track by t.label">
                        <option disabled value="">--...--</option>
                    </select>
                </span>

                <span ng-show="object.typeType.typeTypeType">
                    <select ng-model="object.typeTypeType" required
                            ng-options="t.label for t in object.typeType.typeTypeType track by t.label">
                        <option disabled value="">--...--</option>
                    </select>
                </span>
            </c:when>

            <c:otherwise></c:otherwise>

        </c:choose>
    </display:column>
</div>