<%-- 
    Document   : mystopsList.jsp
    Loops over hashmap, producin a JSON list of stops
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="application/json" %>
[<c:choose>
    <c:when test="${mystopsList != null}">
        <c:forEach items="${mystopsList}" var="stop" varStatus="id">
            {"display_name":"${stop.value.title}","id":"${stop.value.id}"}${id.last ? "" : ","}
        </c:forEach>
    </c:when>
</c:choose>]

