<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script>
        $(document).ready(function(){
                //Examples of how to assign the Colorbox event to elements
                $(".group1").colorbox({rel:'group1'});
                //Example of preserving a JavaScript event for inline calls.
                $("#click").click(function(){ 
                        $('#click').css({"background-color":"#f00", "color":"#fff", "cursor":"inherit"}).text("Open this window again and this message will still be here.");
                        return false;
                });
        });
</script>
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
                        <c:choose>
                            <c:when test="${user_photo.equals(\"1\")}">
                              <img class="img" src="<c:out value="${list[0]}"/>">
                            </c:when>
                            <c:otherwise>
                                <div class="images-news clear-fix">
                                    <a class="group1 big-image" href="<c:out value="${list[0]}"/>"><img class="img" src="<c:out value="${list[0]}"/>"></a>
                                    <div class="js-mini-carusel">
                                        <div class=" mini-carusel">
                                            <div class="sub-images-news items carusel-list">
                                                <c:forEach items="${list}" var="i" begin="1">
                                                    <a class="group1 mini-image" href="<c:out value="${i}"/>"><img class="img" src="<c:out value="${i}"/>"></a>
                                                </c:forEach>  
                                            </div>
                                        </div>
                                        <div class="carusel-navigation">
                                            <div class="arrow arrow-left no-active"></div>
                                            <div class="arrow arrow-right"></div>
                                        </div>
                                    </div> 
                                </div>
                            </c:otherwise>
                        </c:choose>
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