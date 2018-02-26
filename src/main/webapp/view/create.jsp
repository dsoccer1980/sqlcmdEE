<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="create-form">
    <table>
        <tr>
            <td>Table name</td>
            <td><input type="text" name="tablename" id="tablename"/></td>
        </tr>
        <tr>
            <td>Column1</td>
            <td><input type="text" name="column1" id="column1"/></td>
        </tr>
        <tr>
            <td>Column2</td>
            <td><input type="text" name="column2" id="column2"/></td>
        </tr>
        <tr>
            <td>Column3</td>
            <td><input type="text" name="column3" id="column3"/></td>
        </tr>
        <tr>
            <td></td>
            <td><input type="submit" value="create" id="create"/></td>
            <td><span id="messageCreate"></span></td>
        </tr>
    </table>
</div>