<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Help</title>
</head>
<body>

<form:form method="POST" action="connect" modelAttribute="connection">

 Database name<form:input path="dbName"/> <br>
 Username <form:input path="userName"/> <br>
 Password <form:password path="password"/><br>
  <input type="submit" value="connect">
</form:form>
<%@include file="footer.jsp" %>

</body>