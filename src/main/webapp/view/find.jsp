<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<head>
    <title>Find</title>
    <script type="text/javascript" src="${ctx}/resources/js/jquery-3.3.1.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/jquery.tmpl.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/find.js"></script>
</head>
<body>
<div id="find">
    <div id="loading">Loading...</div>
    <div class="container" style="margin-top: 0px">
        <row-template style="display: none">
             <table border="1">
                <tr>
                    <%--{{each $data}}--%>
                        <td>
                            {{= $data}}
                        </td>

                </tr>
               </table>
        </row-template>
    </div>
</div>

</body>
</html>