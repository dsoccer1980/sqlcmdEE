<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Help</title>
</head>
<body>

<c:forEach items="${items}" var="item">
<a href=${item}>${item}</a><br>
</c:forEach>

</body>