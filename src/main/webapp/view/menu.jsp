<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <c:set var="ctx" value="${pageContext.request.contextPath}"/>
    <head>
        <title>Menu</title>
        <script type="text/javascript" src="${ctx}/resources/js/jquery-3.3.1.js"></script>
        <script type="text/javascript" src="${ctx}/resources/js/jquery.tmpl.js"></script>
        <script type="text/javascript" src="${ctx}/resources/js/menu.js"></script>
    </head>
     <body>
         <div id="menu">
                     <div id="loading">Loading...</div>

                     <div class="container" style="margin-top: 0px">
                         <script template="row" type="text/x-jquery-tmpl">
                            <a href="{{= $data}}">{{= $data}}</a><br>
                          </script>
                      </div>

         </div>

     </body>
</html>