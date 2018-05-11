<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<div class="sidenav">
    <div class="sidenav-logo">
    </div>
    <div class="sidenav-nav">
        <h2>财务管理</h2>
        <ul>
            <shiro:hasPermission name="withdrawLimit:view">
                <li>
                    <span class="icon icon-user"></span>
                    <a href="${path}/finance/withdrawLimit">用户提现设置</a>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="returnGold:view">
                <li>
                    <span class="icon icon-cny"></span>
                    <a href="${path}/finance/returngold">体验金</a>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="cashPacket:view">
                <li>
                    <span class="icon icon-envelope-square"></span>
                    <a href="${path}/finance/cash/cashList?defTime=true">现金红包</a>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="returncash:view">
                <li>
                    <span class="icon icon-money"></span>
                    <a href="${path}/finance/returncash">返现券</a>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="coupon:view">
                <li>
                    <span class="icon icon-money"></span>
                    <a href="${path}/finance/coupon">加息券</a>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="replacementRecharge:view">
                <li>
                    <span class="icon icon-cny"></span>
                    <a href="${path}/finance/replacementRecharge">代充值管理</a>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="accountmanagement:view">
                <li>
                    <span class="icon icon-money"></span>
                    <a href="${path}/finance/accountmanagement">平台账户管理</a>
                 </li>
            </shiro:hasPermission>
</ul>
</div>
</div>