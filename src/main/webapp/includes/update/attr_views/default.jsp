<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<tr>
    <td>
    ${param.attr_name_ru}
    </td>
    <td>
        <c:choose>
            <c:when test="${param.attr_type.equals(\"1\") ||
                            param.attr_type.equals(\"5\") ||
                            param.attr_type.equals(\"6\")}">
                <c:choose>
                    <c:when test="${param.attr_name.equals(\"login\")}">
                        <div>${param.attr_value}</div>
                    </c:when>
                    <c:otherwise>
                        <input id="${param.attr_name}" type="text" ng-init="object.${param.attr_name}='${param.attr_value}'" ng-model="object.${param.attr_name}" />
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:when test="${param.attr_type.equals(\"2\")}">
                <input id="${param.attr_name}" type="password" ng-init="object.${param.attr_name}='${param.attr_value}'" ng-model="object.${param.attr_name}" />
            </c:when>
            <c:when test="${param.attr_type.equals(\"3\")}">
            <div id="main"
                 flow-init="{target: '/unc-project/upload', testChunks:false}"
                 flow-file-added="fileAdded($file, $event, $flow)"
                 flow-complete="complete()"
                 flow-name="uploader.flow">

                <img id="${param.attr_name}"
                     src="${(param.attr_value != null && param.attr_value.length() > 0) ?
                                                        param.attr_value :
                                                        initParam["default.image"]}"
                     style="width: 300px; height: 225px"
                />
                <br/>
                <label for="load-avatar">Загрузить аватар:</label>
                <div id="load-avatar" class="alert bg-primary" flow-drop>
                    Перетащите изображение сюда
                </div>
                или
                <button type="button" flow-btn>Загрузить аватар</button>
                <br/>
                <div class="alert alert-success" role="alert" ng-show="isAvatarChanged">
                    Аватар успешно обновлен!
                </div>
            </div>
            </c:when>
            <c:when test="${param.attr_type.equals(\"4\")}">
                <textarea id="${param.attr_name}">${param.attr_value}</textarea>
            </c:when>
        </c:choose>
    </td>
</tr>
