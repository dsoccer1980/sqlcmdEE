<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Help</title>
</head>
<body>

<form action="connect" method="post">

 Database name<input type="text" name="dbname" id="dbname"> <br>
 Username <input type="text" name="username" id="username"> <br>
 Password <input type="password" name="password" id="password"> <br>
  <input type="submit" value="connect" id="connect">
</form>
<%@include file="footer.jsp" %>

</body>