<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div class="sidenav">
    <div class="sidenav-logo">
    </div>
    <div class="sidenav-nav">
        <h2>风控管理</h2>
        <ul>

            <li>
                <span class="icon icon-chain-broken"></span>
                <a href="${path}/riskcontrol/consumerBidList?listType=wait">进单审核</a>
            </li>
        </ul>

    </div>
</div>