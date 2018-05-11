<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<div class="sidenav">
    <div class="sidenav-logo">
    </div>
    <div class="sidenav-nav">
        <h2>渠道管理</h2>
        <ul>
        <shiro:hasPermission name="channelStatistics:view">
            <li>
                <span class="icon icon-bar-chart"></span>
                <a href="${path}/channel/origin">渠道统计</a>
            </li>
        </shiro:hasPermission>
        <shiro:hasPermission name="channelManage:view">
            <li>
                <span class="icon icon-paint-brush"></span>
                <a href="${path}/channel/child/list-child">添加/修改渠道</a>
            </li>
        </shiro:hasPermission>
        </ul>
    </div>
</div>



