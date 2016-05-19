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
                    <c:when test="${param.attr_name.equals(\"login\") ||
                                    param.attr_name.equals(\"registration_date\") ||
                                    param.attr_name.equals(\"last_visit_date\")}">
                        <div>${param.attr_value}</div>
                    </c:when>
                    <c:otherwise>
                        <input id="${param.attr_name}" type="text" ng-init="object.${param.attr_name}='${param.attr_value}'" ng-model="object.${param.attr_name}" />
                    </c:otherwise>
                </c:choose>
            </c:when>
            <c:when test="${param.attr_type.equals(\"2\")}">
                <script>var isOldPassEmpty = ${param.attr_value == null || param.attr_value.length() == 0};</script>
                <input id="${param.attr_name}" type="password" ng-model="object.${param.attr_name}" />
                <div class="errors-wrapper col-sm-offset-2 col-sm-10">
                    <div class="alert alert-danger" ng-show="nullPassError">Необходимо указать пароль</div>
                </div>
            </c:when>
            <c:when test="${param.attr_type.equals(\"3\")}">
            <script>
                if (initialUserId == undefined) {
                    var initialUserId = '${userAccount.getId()}';
                }

                if (paramAttrName == undefined) {
                    var paramAttrName = '${param.attr_name}';
                }

                if (paramAttrValue == undefined) {
                    var paramAttrValue = '${param.attr_value}';
                }
            </script>
            <div id="main"
                 flow-init="{target: '/unc-project/upload', testChunks:false}"
                 flow-name="uploader.flow"
                 flow-file-added="fileAdded($file, $event, $flow)"
                 flow-complete="complete()">

                <img id="${param.attr_name}"
                     src="${(param.attr_value != null && param.attr_value.length() > 0) ?
                                                        param.attr_value :
                                                        initParam["default.image"]}"/>
                <br/>
                <label for="load-avatar">Загрузить:</label>
                <div id="load-avatar" class="alert bg-primary" flow-drop>
                    Перетащите изображение сюда
                </div>
                или
                <button type="button" path="${param.attr_value}" flow-btn>Загрузить</button>
                <br/>
                <div class="alert alert-success" role="alert" ng-show="isAvatarChanged">
                    Изображение успешно обновлено!
                </div>
            </div>
            </c:when>
            <c:when test="${param.attr_type.equals(\"4\")}">
                <textarea id="${param.attr_name}">${param.attr_value}</textarea>
            </c:when>
            <c:when test="${param.attr_type.equals(\"7\")}">
                <div>Gold</div>
            </c:when>
            <c:when test="${param.attr_type.equals(\"8\")}">
                <div id="map" coords='${param.attr_value}'
                data-toggle="modal" data-target="#mapModal"></div>
                <input id="clear-markers" type="button" value="Удалить маркер" width="100px">
            </c:when>
        </c:choose>
    </td>
</tr>
