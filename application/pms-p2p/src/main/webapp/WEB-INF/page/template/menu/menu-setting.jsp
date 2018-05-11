<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div class="sidenav">
    <div class="sidenav-logo">
    </div>
    <div class="sidenav-nav">
        <h2>系统设置</h2>
        <ul>
            <shiro:hasPermission name="settingEnum:view">
                <li>
                    <span class="icon icon-slack"></span>
                    <a href="${path}/setting/enum">系统枚举</a>
                </li>
            </shiro:hasPermission>
        </ul>
    </div>
</div>
