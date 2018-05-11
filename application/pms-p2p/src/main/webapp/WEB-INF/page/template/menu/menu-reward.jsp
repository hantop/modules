<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div class="sidenav">
    <div class="sidenav-logo">
    </div>
    <div class="sidenav-nav">
        <h2>奖励管理</h2>
        <ul>
            <shiro:hasPermission name="experienceGold:view">
            <li>
                <span class="icon icon-cny"></span>
                <a href="${path}/reward/experienceGold">体验金管理</a>
            </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="cashRedPacket:view">
            <li>
                <span class="icon icon-envelope-square"></span>
                <a href="${path}/reward/red-packet/cash-red-packet-index">现金红包管理</a>
            </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="cashBackVoucher:view">
            <li>
                <span class="icon icon-money"></span>
                <a href="${path}/reward/cashBackVoucher">返现券管理</a>
            </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="rateCoupon:view">
            <li>
                <span class="icon icon-money"></span>
                <a href="${path}/reward/rateCoupon">加息劵管理</a>
            </li>
            </shiro:hasPermission>
        </ul>
    </div>
</div>