<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Help</title>
</head>
<body>
        <table border="1">
            <c:forEach items="${actions}" var="userAction">
                <tr>
                        <td>
                            ${userAction.userName}
                        </td>
                        <td>
                            ${userAction.dbName}
                         </td>
                         <td>
                            ${userAction.action}
                         </td>
                </tr>
             </c:forEach>
        </table>
 <%@include file="footer.jsp" %>

</body>
</html>