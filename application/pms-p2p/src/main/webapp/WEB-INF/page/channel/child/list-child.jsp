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
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/channel/index">渠道管理</a></li>
                <li class="active"><b>+添加/修改渠道</b></li>
            </ol>
            <form:form class="form-inline" role="form" method="post" action="${path}/channel/child/list-child" id="dataform"  commandName="channelDTO">
                <div class="form-group">
                    <shiro:hasPermission name="channelManage:create">
                        <a class="btn btn-warning btn-sm" href="${path}/channel/child/edit-child"  role="button">新增</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="channelType:view">
                        <a class="btn btn-info btn-sm" href="${path}/channel/parent/list-parent"  role="button">管理渠道分类</a>
                    </shiro:hasPermission>
                </div>
                
                <shiro:hasPermission name="channelManage:search">
                <div class="form-group">
                    <label class="control-label">渠道名称</label>
                </div>
                <div class="form-group">
                    <form:input path="name" cssClass="form-control input-sm" maxlength="30"/>
                </div>
                <div class="form-group">
                    <label class="control-label">渠道编码</label>
                </div>
                <div class="form-group">
                    <form:input path="code" cssClass="form-control input-sm" maxlength="10"/>
                </div>
                <button id="searchBtn" type="button" class="btn btn-primary btn-sm">搜索</button>
                </shiro:hasPermission>
                
                <input type="hidden" id="page" name="page">
            </form:form>
        </div>
        <div class="col-md-12">
            <table class="table table-striped table-bordered table-condensed table-hover" style="margin-top: 20px;">
                <thead>
                    <tr class="success">
                        <th>渠道名称</th>
                        <th>一级分类</th>
                        <th>渠道编号</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${list== null || fn:length(list) == 0}">
                        <%--<tr>
                            <td colspan="4">无数据</td>
                        </tr>--%>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="item" items="${list}">
                            <tr>
                                <td>${item.name}</td>
                                <td>${item.parent.name}</td>
                                <td>${item.code}</td>
                                <td>
                                    <shiro:hasPermission name="channelManage:edit">
                                    <a class="" href="${path}/channel/child/edit-child?id=${item.id}" role="button">编辑</a>
                                    &nbsp;
                                    </shiro:hasPermission>
                                    
                                    <shiro:hasPermission name="channelManage:delete">
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

<script type="application/javascript">
    $("#searchBtn").bind("click", function() {
        var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $("#dataform").submit();
        layer.close();

    });

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
                                '${path}/channel/child/deleteChild',
                                {
                                    contentType: 'application/json; charset=utf-8',
                                    data: JSON.stringify(id),
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
            })
        });


    })(jQuery);
</script>