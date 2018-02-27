<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="tables">
    <table border="1" class="container">
        <script template="row" type="text/x-jquery-tmpl">
            <tr>
                <td>
                    <a href="#/find/{{= $data}}">{{= $data}}</a><br>
                </td>
                <td>
                    <a href="#/clear/{{= $data}}">clear</a><br>
                </td>
            </tr>
        </script>
    </table>
</div>
