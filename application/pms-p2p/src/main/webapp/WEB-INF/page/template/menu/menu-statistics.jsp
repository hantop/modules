<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<div class="sidenav">
    <div class="sidenav-logo">
    </div>
    <div class="sidenav-nav">
        <h2>统计管理</h2>
        <ul>
            <%--<shiro:hasPermission name="statisticsReg:view">
                <li>
                    <span class="icon icon-bar-chart"></span>
                    <a href="${path}/statistics/reg/reg-list?def=true">注册统计</a>
                </li>
            </shiro:hasPermission>--%>
            <shiro:hasPermission name="statisticsInvest:view">
            <li>
                <span class="icon icon-bar-chart"></span>
                <a href="${path}/statistics/invest?def=true">投资统计</a>
            </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="firstInvest:view">
                <li>
                    <span class="icon icon-bar-chart"></span>
                    <a href="${path}/statistics/firstInvest?def=true">首投信息</a>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="statisticsReturnedmoney:view">
                <li>
                    <span class="icon icon-bar-chart"></span>
                    <a href="${path}/statistics/returnedmoney">回款信息</a>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="withdraw:view">
                <li>
                    <span class="icon icon-bar-chart"></span>
                    <a href="${path}/statistics/withdraw?def=true">提现信息</a>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="statisticsAuth:view">
            <li>
                <span class="icon icon-bar-chart"></span>
                <a href="${path}/statistics/authentication">实名认证统计</a>
            </li>
            </shiro:hasPermission>
             <shiro:hasPermission name="statisticsIntegrate:view">
                <li>
                    <span class="icon icon-bar-chart"></span>
                    <a href="${path}/statistics/integrate">积分统计</a>
                </li>
            </shiro:hasPermission>
        </ul>
    </div>
</div>