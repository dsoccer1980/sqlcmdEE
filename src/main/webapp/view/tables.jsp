<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="tables">
    <div class="container">
        <script template="row" type="text/x-jquery-tmpl">
            <a href="#/find/{{= $data}}">{{= $data}}</a><br>
        </script>
    </div>
</div>
