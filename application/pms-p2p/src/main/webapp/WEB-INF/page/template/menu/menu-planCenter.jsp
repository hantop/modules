<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div class="sidenav">
    <div class="sidenav-logo">
    </div>
    <div class="sidenav-nav">
        <h2>计划中心</h2>
        <ul>
            <%--<shiro:hasPermission name="analysePlan:view">--%>
            <%--<li>
                <span class="icon icon-chain-broken"></span>
                <a href="">计划分析</a>
            </li>--%>
            <%--</shiro:hasPermission>--%>
            <shiro:hasPermission name="releasePlan:view">
            <li>
                <span class="icon icon-paint-brush"></span>
                <a href="${path}/planCenter/releasePlan/index">计划发布</a>
            </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="planType:view">
            <li>
                <span class="icon icon-user"></span>
                <a href="${path}/planCenter/planType/index">模板管理</a>
            </li>
            </shiro:hasPermission>
        </ul>
    </div>
</div>