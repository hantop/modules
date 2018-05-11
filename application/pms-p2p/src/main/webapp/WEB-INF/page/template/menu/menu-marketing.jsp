<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<div class="sidenav">
    <div class="sidenav-logo">
    </div>
    <div class="sidenav-nav">
        <h2>营销管理</h2>
        <ul>
            <shiro:hasPermission name="marketing:view">
                <li>
                    <span class="icon icon-cny"></span>
                    <a href="${path}/marketing/tmr">电销绩效详情</a>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="activity:view">
                <li>
                    <span class="icon icon-cny"></span>
                    <a href="${path}/marketing/activity">活动设置</a>
                </li>
            </shiro:hasPermission>
        </ul>
    </div>
</div>