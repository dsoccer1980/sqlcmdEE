<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <c:set var="ctx" value="${pageContext.request.contextPath}"/>
    <head>
         <title>Help</title>
        <script type="text/javascript" src="${ctx}/resources/js/jquery-3.3.1.js"></script>
        <script type="text/javascript" src="${ctx}/resources/js/help.js"></script>
    </head>

    <body>
        <div id="help_container">
            Существующие комманды:  <br>
            <div id="commands">
                 <div id="loading">Loading...</div>
            </div>
        </div>

        <%@include file="footer.jsp" %>

    </body>
</html>
