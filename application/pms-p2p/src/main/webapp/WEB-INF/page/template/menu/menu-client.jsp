<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<div class="sidenav">
    <div class="sidenav-logo">
    </div>
    <div class="sidenav-nav">
        <h2>客户端管理</h2>
        <ul>
            <shiro:hasPermission name="clientStartup:view">
                <li>
                    <span class="icon icon-eye"></span>
                    <a href="${path}/client/startup">启动页设置</a>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="clientAdvert:view">
                <li>
                    <span class="icon icon-flag-checkered"></span>
                    <a href="${path}/client/advert">广告页设置</a>
                </li>
            </shiro:hasPermission>
        </ul>
    </div>
</div>