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
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/reward/index">奖励管理</a></li>
                <li class="active"><a href="${path}/reward/experienceGold">体验金管理</a></li>
                <li class="active"><b>体验金编辑</b></li>
            </ol>
            <form:form class="form-inline" method="post" action="${path}/reward/experienceGold/experience-gold-list" id="dataform" commandName="experienceGold">
                <shiro:hasPermission name="experienceGoldEdit:create">
                <div class="form-group">
                    <a class="btn btn-info btn-sm" href="${path}/reward/experienceGold/experience-gold-edit" role="button">添加体验金类型</a>
                </div>
                </shiro:hasPermission>
                <shiro:hasPermission name="experienceGoldEdit:search">
                <div class="form-group">
                    <form:input path="activityCode" cssClass="form-control" maxlength="30"/>
                </div>
                <input type="hidden" id="page" name="page">
                
                    <button type="submit" class="btn btn-primary btn-sm">搜索</button>
                </shiro:hasPermission>
            </form:form>
        </div>
        <div class="col-md-6">
            <table class="table table-striped table-bordered table-condensed table-hover" style="margin-top: 20px;">
                <thead>
                    <tr class="success">
                        <th>体验金代码</th>
                        <th>体验金金额</th>
                        <th>体验金有效期</th>
                        <th>体验金年化收益</th>
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
                                <td><c:out value="${item.activityCode}" default="" escapeXml="true"/></td>
                                <td>${item.experienceGold}</td>
                                <td>${item.effectDay}天</td>
                                <td>${item.yearYield}%</td>
                                <td>
                                    <c:if test="${item.grantStatus == null}">
                                        <shiro:hasPermission name="experienceGoldEdit:edit">
                                        <a class="glyphicon glyphicon-pencil" href="${path}/reward/experienceGold/experience-gold-edit?id=${item.id}" role="button">编辑</a>
                                        &nbsp;
                                        </shiro:hasPermission>
                                        <shiro:hasPermission name="experienceGoldEdit:delete">
                                        <a class="glyphicon glyphicon-remove" href="#" role="delete" identity="${item.id}">删除</a>
                                        </shiro:hasPermission>
                                    </c:if>
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
                                '${path}/reward/experienceGold/experience-gold-remove',
                                {
                                    contentType: 'application/json; charset=utf-8',
                                    data: JSON.stringify(id),
                                    dataType: "json",
                                    type: "DELETE",
                                    beforeSend: function (xhr) {
                                    	var index = layer.load(0, {
                    						shade: [0.4,'#fff',false] //0.1透明度的白色背景
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
                                        message: jqXHR.responseJSON.message
                                    });
                                })
                                .complete(function (jqXHR, textStatus) {
                                    layer.closeAll();
                                });
                    }

                }
            })
        });
    })(jQuery);
</script>