<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <title><tiles:insertAttribute name="title"/></title>
    <c:set var="path" value="${pageContext.request.contextPath}" scope="request"/>
    <link rel="icon" href="/static/favicon.ico" type="image/x-icon"/>
    <link rel="stylesheet" href="/static/component/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="/static/theme/index.css">
    <link rel="stylesheet" href="/static/theme/font.css">
    <script type="text/javascript" src="/static/library/jquery/jquery-2.1.4.min.js"></script>
    <script type="text/javascript" src="/static/component/bootstrap/js/bootstrap.js"></script>
    <script type="text/javascript" src="/static/script/index/index.js"></script>
</head>
<body>
<input type="hidden" id="path" name="path" value="${path}">
<script type="text/javascript">
    var path = $("#path").val();
</script>
<tiles:insertAttribute name="header"/>
<%--<div class="container-fluid">--%>
<%--<div class="row">--%>
<%--<div class="col-md-2 menu-sild">--%>
<tiles:insertAttribute name="menu"/>
<%--</div>--%>
<%--<div class="col-md-10 content-top">--%>
<tiles:insertAttribute name="body"/>
<%--</div>--%>
<%--</div>--%>
<%--</div>--%>
</body>
</html>