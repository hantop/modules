<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<div class="sidenav">
    <div class="sidenav-logo">
    </div>
    <div class="sidenav-nav">
        <h2>首页</h2>
        <ul>
        <shiro:hasPermission name="index:view">
            <li>
                <span class="icon icon-home"></span>
                首页
            </li>
        </shiro:hasPermission>
        </ul>
    </div>
</div>

