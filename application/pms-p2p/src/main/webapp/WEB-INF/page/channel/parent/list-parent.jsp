<%--
  Created by IntelliJ IDEA.
  User: bogle
  Date: 2015/11/10
  Time: 15:28
  To change this template use File | Settings | File Templates.
--%>
<%@page language="java" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<link rel="stylesheet" href="/static/component/HoldOn/HoldOn.min.css">
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script type="text/javascript" src="/static/component/HoldOn/HoldOn.min.js"></script>
<div class="container">
    <div class="row">
        <div class="col-md-12">

            <div class="col-md-12">
                <ol class="breadcrumb">
                    <li class="active"><a href="${path}/channel/index">渠道管理</a></li>
                    <li class="active"><a href="${path}/channel/child/list-child">+添加/修改渠道</a></li>
                    <li class="active"><b>管理渠道分类</b></li>
                </ol>
                <form:form class="form-inline" method="post" action="${path}/channel/parent/list-parent" id="dataform" commandName="channelDTO">
                    <shiro:hasPermission name="channelType:create">
                    <div class="form-group">
                        <a class="btn btn-warning" href="${path}/channel/parent/edit-parent" role="button">添加</a>
                    </div>
                    </shiro:hasPermission>
                    
                    <shiro:hasPermission name="channelType:search">
                    <div class="form-group">
                        <form:input path="name" cssClass="form-control input-sm" maxlength="30"/>
                    </div>
                    <button type="submit" class="btn btn-primary btn-sm">搜索</button>
                    </shiro:hasPermission>
                    <input type="hidden" id="page" name="page">
                </form:form>
            </div>
            <div class="col-md-4">
                <table class="table table-bordered table-condensed table-striped table-hover" style="margin-top: 20px;">
                    <thead>
                        <tr class="success">
                            <th>渠道名称</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${list== null || fn:length(list) == 0}">
                           <%-- <tr>
                                <td colspan="4">无数据</td>
                            </tr>--%>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="item" items="${list}">
                                <tr>
                                    <td>${item.name}</td>
                                    <td>
                                        <shiro:hasPermission name="channelType:edit">
                                        <a class="" href="${path}/channel/parent/edit-parent?id=${item.id}" role="button">编辑</a>
                                        &nbsp;
                                        </shiro:hasPermission>
                                        
                                        <shiro:hasPermission name="channelType:delete">
                                        <a class="" href="#" role="delete" identity="${item.id}">删除</a>
                                        </shiro:hasPermission>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
                <tiles:insertDefinition name="paginator"/>
            </div>
        </div>
    </div>
</div>

<script type="application/javascript">
    (function ($) {
        $('a[role="delete"]').on('click', function () {
            var identity = $(this).attr('identity');
            bootbox.confirm({
                title: '提示',
                size: 'small',
                message: "确认删除么",
                callback: function (result) {
                    if (result) {
                        var page = "${paginator.pageNum}";
                        var id = [identity];
                        $.ajax(
                                '${path}/channel/parent/deleteParent',
                                {
                                    contentType: 'application/json; charset=utf-8',
                                    data: JSON.stringify(id),
                                    dataType: "json",
                                    type: "DELETE",
                                    beforeSend: function (xhr) {
                                        HoldOn.open({
                                            theme: "sk-cube-grid",
                                            message: '请稍等.....',
                                            backgroundColor:"#000"
                                        });
                                    }
                                }
                        )
                                .success(function (result) {
                                    if (result.success) {
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
                                        message: jqXHR.responseJSON.message
                                    });
                                })
                                .complete(function (jqXHR, textStatus) {
                                    HoldOn.close();
                                });
                    }

                }
            })
        });
    })(jQuery);
</script>