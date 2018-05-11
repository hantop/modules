<%--
  Created by IntelliJ IDEA.
  User: Lullaby
  Date: 2015/7/17
  Time: 16:23
  To change this template use File | Settings | File Templates.
--%>
<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<link rel="stylesheet" href="/static/component/HoldOn/HoldOn.min.css">
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script type="text/javascript" src="/static/component/HoldOn/HoldOn.min.js"></script>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<div class="container content-top">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/system/index">系统管理</a></li>
                <li class="active"><b>系统用户</b></li>
            </ol>
            <div>
                <form id="dataform" class="form-inline" method="post" action="${path}/system/pmsuser">
                    <shiro:hasPermission name="systemUser:create">
                        <a href="${path}/system/pmsuser/saveOrUpdate" class="btn btn-success btn-sm">添加用户</a>
                    </shiro:hasPermission>

                    <shiro:hasPermission name="systemUser:search">
                        <div class="form-group">
                            <label for="realname">姓名</label>
                            <input type="text" class="form-control input-sm" id="realname" name="realname"
                                   value="${user.realname}">
                        </div>
                        <%--<div class="form-group">
                            <label for="username">账号</label>
                            <input type="text" class="form-control input-sm" id="username" name="username" value="${user.username}">
                        </div>
                        <div class="form-group">
                            <label for="phone">电话</label>
                            <input type="text" class="form-control input-sm" id="phone" name="phone" value="${user.phone}">
                        </div>--%>
                        <button id="searchBtn" type="button" class="btn btn-primary btn-sm">查询</button>
                    </shiro:hasPermission>
                    <input type="hidden" id="page" name="page">
                </form>
            </div>
            <hr style="margin-bottom: 0;">
            <div class="panel panel-default">
                <div class="panel-heading">用户列表</div>
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>登陆账号</th>
                        <th>真实姓名</th>
                        <th>手机号</th>
                        <th>邮箱</th>
                        <th>部门</th>
                        <th>角色</th>
                        <th>状态</th>
                        <th>是否离职</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${list}" var="v" varStatus="vs">
                        <tr>
                            <td>${v.username}</td>
                            <td>${v.realname}</td>
                            <td>${v.phone}</td>
                            <td>${v.email}</td>
                            <td>${v.department}</td>
                            <td>
                                <c:if test="${!empty v.roles}">
                                    <c:set var="roles" value=""/>
                                    <c:forEach items="${v.roles}" var="item">
                                        <c:set var="roles" value="${roles},${item.name}"/>
                                    </c:forEach>
                                    ${fn:substring(roles, 1, -1)}
                                </c:if>
                            </td>
                            <c:choose>
                                <c:when test="${v.status eq 1}">
                                    <td style="color: green !important;">启用</td>
                                </c:when>
                                <c:otherwise>
                                    <td style="color: red !important;">禁用</td>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${v.dimission}">
                                    <td style="color: green !important;">在职</td>
                                </c:when>
                                <c:otherwise>
                                    <td style="color: red !important;">离职</td>
                                </c:otherwise>
                            </c:choose>
                            <td>
                                <shiro:hasPermission name="systemUser:edit">
                                <a class="" href="${path}/system/pmsuser/saveOrUpdate?id=${v.id}" role="button">编辑</a>
                                &nbsp;
                                </shiro:hasPermission>
                                
                                <shiro:hasPermission name="systemUser:setRole">
                                <a class="" href="${path}/system/pmsuser/settingUserRole?userId=${v.id}&username=${v.username}" role="button">角色设置</a>
                                &nbsp;
                                </shiro:hasPermission>
                                
                                <shiro:hasPermission name="systemUser:setDep">
                                <%--<a class="" href="${path}/system/pmsuser/settingDepartment?userId=${v.id}&username=${v.username}" role="button">部门设置</a>--%>
                                &nbsp;
                                </shiro:hasPermission>
                                
                                <shiro:hasPermission name="systemUser:delete">
                                <a class="" href="${path}" onclick="delUser(${v.id});return false">删除</a>
                                </shiro:hasPermission>
                                &nbsp;
                                <shiro:hasPermission name="systemUser:dimission">
                                    <c:if test="${v.dimission}">
                                        <a class="" href="${path}" onclick="dimissionUser(${v.id},'${v.realname}');return false">离职</a>
                                    </c:if>
                                </shiro:hasPermission>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <tiles:insertDefinition name="paginator"/>
        </div>
    </div>
</div>
<script type="text/javascript">
    $("#searchBtn").bind("click", function() {
        var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $("#dataform").submit();
        layer.close();
    });
    function pagination(page) {
        $('#page').val(page);
        var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $("#dataform").submit();
        layer.close();
    }

    $(function ($) {
        delUser = function (id) {
            bootbox.confirm({
                title: '提示',
                size: 'small',
                message: "确认删除么",
                callback: function (result) {
                    if (result) {
                        var page = "${paginator.pageNum}";
                        $.ajax(
                                '${path}/system/pmsuser/delUser',
                                {
                                    contentType: 'application/json; charset=utf-8',
                                    data: JSON.stringify([id]),
                                    dataType: "json",
                                    type: "DELETE",
                                    beforeSend: function (xhr) {
                                        HoldOn.open({
                                            theme: "sk-cube-grid",
                                            message: '请稍等.....',
                                            backgroundColor: "#000"
                                        });
                                    }
                                }
                        )
                        .success(function (result) {
                            if (result) {
                                bootbox.alert({
                                    buttons: {
                                        ok: {
                                            label: 'OK'
                                        }
                                    },
                                    title: '提示',
                                    size: 'small',
                                    message: '删除成功',
                                    callback: function () {
                                        pagination(page);
                                    }
                                });
                            } else {
                                bootbox.alert({
                                    buttons: {
                                        ok: {
                                            label: 'OK'
                                        }
                                    },
                                    title: '提示',
                                    size: 'small',
                                    message: '删除失败'
                                });
                            }
                        })
                        .error(function (jqXHR, textStatus, errorThrown) {
                            bootbox.alert({
                                buttons: {
                                    ok: {
                                        label: 'OK'
                                    }
                                },
                                title: '提示',
                                size: 'small',
                                message: '删除失败'
                            });
                        })

                        .complete(function (jqXHR, textStatus) {
                            HoldOn.close();
                        });
                    }
                }
            });
        }

        dimissionUser = function(id,username){
            bootbox.confirm({
                title: '提示',
                size: 'small',
                message: "<span style='font-size: 13px;font-style: inherit'>确认用户【"+username+"】已经离职？</span>",
                callback: function (result) {
                    if (result) {
                        $.post(
                                path + "/system/pmsuser/dimissionUser",
                                {"id" : id},
                                function (data) {
                                    var res = data.codeStatus;
                                    if(res == "success"){
                                        bootbox.alert({message: "操作成功！！！", title: '操作信息',callback: function() {
                                            window.location.reload(true);}});
                                    }else{
                                        bootbox.alert({message: "操作失败，请稍后再试！！！", title: '错误信息'});
                                    }
                                }
                        );
                    }
                }
            });
        }
    })(jQuery);
</script>