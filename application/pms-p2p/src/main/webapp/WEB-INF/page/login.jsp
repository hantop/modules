<!DOCTYPE html>
<%@ page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <title>welcome</title>
    <link rel="icon" href="/static/favicon.ico" type="image/x-icon"/>
    <link rel="stylesheet" type="text/css" href="/static/component/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/theme/login.css"/>
</head>
<body>
<nav class="navbar navbar-default navbar-static-top">
    <div class="container-fluid">
        <div class="row-fluid">
            <div class="col-md-12">
                <div class="navbar-header">
                    <div class="navbar-brand">
                        <div class="login-logo">
                            <img src="/static/image/login-logo.png"/>
                            <b>产品管理系统</b>${aa}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</nav>
<div class="container1">
    <div class="row">
        <div class="col-md-4">
            <form>
                <div class="form-group">
                    <input type="text" class="form-control" id="username" value="" placeholder="请输入用户名"/>
                </div>
                <div class="form-group">
                    <input type="password" class="form-control" id="password" value="" placeholder="请输入登录密码"/>
                </div>

                <c:if test="${errorNum eq '1'}">
                    <div class="form-group">
                        <div class="kaptcha-div">
                            <img id="kaptcha" class="kaptcha" src="captcha">
                        </div>
                        <div class="captcha-div">
                            <input type="text" class="form-control captcha" id="captcha" value="" placeholder="请输入验证码"/>
                        </div>
                    </div>
                </c:if>

                <div class="form-group">
                    <button type="button" id="btn-submit" class="btn btn-default">登录</button>
                </div>
                <div class="form-group text-center">
                    <img id="loading" class="hidden" src="/static/image/loading.gif">
                </div>
            </form>
        </div>
    </div>
    <div id="tips" class="modal fade">
        <div class="modal-dialog modal-sm">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">Tips</h4>
                </div>
                <div class="modal-body">
                    <b class="text-warning"></b>
                </div>
            </div>
        </div>
    </div>
</div>
<footer class="footer">
    <div class="container">
        <p class="text-muted text-center copyright">
            <span class="copyright">
                Copyright©2015广东分利宝网络科技有限公司<br/>备案号：粤ICP备15052387号-1
            </span>
        </p>
    </div>
</footer>
<script type="text/javascript" src="/static/library/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="/static/component/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/static/script/login.js"></script>
</body>
</html>