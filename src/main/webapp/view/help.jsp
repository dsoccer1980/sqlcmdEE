<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="help">
    <div>Существующие команды:</div>
    <div>
              <dl class="container">
                  <script template="row" type="text/x-jquery-tmpl">
                          <dt>{{= command}}</dt>
                          <dd>{{= description}}</dd>
                       </script>
              </dl>
    </div>
</div>

