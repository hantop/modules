<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<div class="sidenav">
    <div class="sidenav-logo">
    </div>
    <div class="sidenav-nav">
        <h2>宣传管理</h2>
        <ul>
            <shiro:hasPermission name="publicityStartup:view">
                <li>
                    <span class="icon icon-eye"></span>
                    <a href="${path}/publicity/startup">启动页设置</a>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="publicityAdvert:view">
                <li>
                    <span class="icon icon-flag-checkered"></span>
                    <a href="${path}/publicity/advert">广告页设置</a>
                </li>
            </shiro:hasPermission>


            <shiro:hasPermission name="knowMore:view">

                <li>
                    <span class="icon icon-flag-checkered"></span>
                    <a href="${path}/publicity/class/knowmoreList?status=-1">网贷知多点</a>
                </li>

            </shiro:hasPermission>

            <shiro:hasPermission name="knowEarly:view">
                <li>
                    <span class="icon icon-flag-checkered"></span>
                    <a href="${path}/publicity/class/riskKnowEarlyList?status=-1">风险早知道</a>
                </li>
            </shiro:hasPermission>


        </ul>
    </div>
</div>