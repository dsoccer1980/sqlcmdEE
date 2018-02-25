<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="actions">
    <table border="1" class="container">
        <script template type="text/x-jquery-tmpl">
            <tr>
                <td>
                    {{= userName}}
                </td>
                <td>
                    {{= database}}
                </td>
                <td>
                    {{= action}}
                </td>
            </tr>
        </script>
    </table>
</div>