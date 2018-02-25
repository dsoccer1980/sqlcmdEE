<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Help</title>
</head>
<body>

<form action="create" method="post">

 Table name<input type="text" name="tablename" id="tablename"> <br>
 Column1 <input type="text" name="column1" id="column1"> <br>
 Column2 <input type="text" name="column2" id="column2"> <br>
 Column3 <input type="text" name="column3" id="column3"> <br>
  <input type="submit" value="create" id="create">
</form>

</body>