<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div class="sidenav">
    <div class="sidenav-logo">
    </div>
    <div class="sidenav-nav">
        <h2>微信管理</h2>
        <ul>
            <shiro:hasPermission name="weixinmenu:view">
                <li>
                    <span class="icon icon-chain-broken"></span>
                    <a href="${path}/weixin/menu/index">菜单管理</a>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="weixinkeyword:view">
                <li>
                    <span class="icon icon-chain-broken"></span>
                    <a href="${path}/weixin/msg/index">关键字回复消息管理</a>
                </li>
            </shiro:hasPermission>
        </ul>
    </div>
</div>