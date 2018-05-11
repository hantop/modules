<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div class="sidenav">
    <div class="sidenav-logo">
    </div>
    <div class="sidenav-nav">
        <h2>客服管理</h2>
        <ul>
            <shiro:hasPermission name="unbindBankCard:view">
            <li>
                <span class="icon icon-chain-broken"></span>
                <a href="${path}/cs/unbindBankCards">解绑银行卡</a>
            </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="replacePhone:view">
            <li>
                <span class="icon icon-paint-brush"></span>
                <a href="${path}/cs/phone">更换手机号码</a>
            </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="accountInfo:view">
            <li>
                <span class="icon icon-user"></span>
                <a href="${path}/transaction/account-search">用户查询</a>
            </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="borrowerInfo:view">
            <li>
                <span class="icon icon-user"></span>
                <a href="${path}/cs/borrower">借款用户</a>
            </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="guarantee:view">
            <li>
                <span class="icon icon-user"></span>
                <a href="${path}/cs/guarantee">担保用户</a>
            </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="guarantor:view">
            <li>
                <span class="icon icon-user"></span>
                <a href="${path}/cs/guarantor">账户管理</a>
            </li>
            </shiro:hasPermission>
            <li>
                <span class="icon icon-user"></span>
                <a href="${path}/cs/investUser/list">投资用户</a>

            </li>
            <li>
                <span class="icon icon-chain-broken"></span>
                <a href="${path}/cs/logInfo">日志查询</a>
            </li>
        </ul>
    </div>
</div>