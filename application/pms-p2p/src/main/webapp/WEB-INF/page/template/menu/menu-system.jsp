<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div class="sidenav">
    <div class="sidenav-logo">
    </div>
    <div class="sidenav-nav">
        <h2>系统管理</h2>
        <ul>
            <shiro:hasPermission name="systemUser:view">
                <li>
                    <span class="icon icon-group"></span>
                    <a href="${path}/system/pmsuser">系统用户</a>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="systemRole:view">
                <li>
                    <span class="icon icon-group"></span>
                    <a href="${path}/system/pmsrole">系统角色</a>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="systemGroup:view">
                <li>
                    <span class="icon icon-group"></span>
                    <a href="${path}/system/pmsgroup">系统组织</a>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="permission:view">
                <li>
                    <span class="icon icon-gavel"></span>
                    <a href="${path}/system/pmspermit">系统权限</a>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="permission:view">
                <li>
                    <span class="icon icon-gavel"></span>
                    <a href="${path}/system/pmslog">系统日志</a>
                </li>
            </shiro:hasPermission>
            <%--<li>
                <span class="icon icon-dashboard"></span>
                <a href="${path}/system/pmsmenu">系统菜单</a>
            </li>--%>
            <%--<li>
                <span class="icon icon-stack-overflow"></span>
                <a href="${path}/system/enum">系统枚举</a>
            </li>--%>
        </ul>
        <%--<h2>系统设置</h2>
        <ul>
            <li>
                <span class="icon icon-slack"></span>
                <a href="${path}/system/const">平台常量管理</a>
            </li>
        </ul>--%>
    </div>
</div>
