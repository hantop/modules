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
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/reward/index">奖励管理</a></li>
                <li class="active"><a href="${path}/reward/red-packet/back-voucher-list">返现券管理</a></li>
                <li class="active"><b>返现券编辑</b></li>
            </ol>
            <form:form class="form-inline" method="post" action="${path}/reward/red-packet/back-voucher-type-list" id="dataform" commandName="redPacket">
                <shiro:hasPermission name="cashBackVoucherEdit:create">
                <div class="form-group">
                    <a class="btn btn-info" href="${path}/reward/red-packet/back-voucher-type-edit"
                       role="button">添加返现券类型</a>
                </div><br/>
                </shiro:hasPermission>
                
                <shiro:hasPermission name="cashBackVoucherEdit:search">
                <div class="form-group">
                    <label>返现券代码: </label><form:input path="activityCode" cssClass="form-control input-sm" maxlength="30"/>
                    <label>来源: </label><form:input path="remarks" cssClass="form-control input-sm" maxlength="30"/> 
                </div>
                <button type="submit" class="btn btn-primary">搜索</button>
                </shiro:hasPermission>
                
                <input type="hidden" id="page" name="page">
            </form:form>
        </div>
        <div class="col-md-12">
            <table class="table table-striped table-bordered table-condensed table-hover" style="margin-top: 20px;">
                <thead>
                    <tr class="success">
                        <th>返现券代码</th>
                        <th>返现券金额</th>
                        <th>返现券门槛</th>
                        <th>投资期限</th>
                        <th>可用产品类型</th>
                        <th>返现券有效期</th>
                        <th>来源</th>
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
                                <td>${item.redMoney}</td>
                                <td>${item.investMoney}</td>
                                <td>
                                <c:choose>
                                	<c:when test="${item.investDeadLine == 0 || item.investDeadLine eq null}">
                                		<c:out value='不限' default="" escapeXml="true"/>
                                	</c:when>
                                	<c:otherwise>
                                		<c:out value='大于等于${item.investDeadLine}天' default="" escapeXml="true"/>
                                	</c:otherwise>
                                </c:choose>
                                </td> 
                                <td>
                                	 <c:out value="${item.bidTypeAlias}" default="" escapeXml="true"/>
                                </td>
                                <td>${item.effectDay}天</td>
                               <td>${item.remarks}</td>
                                <td>
                                    <shiro:hasPermission name="cashBackVoucherEdit:edit">
                                    <c:choose>
                                        <c:when test="${item.isGranted() eq true}">
                                        <a class="glyphicon glyphicon-pencil" href="${path}/reward/red-packet/back-voucher-type-edit?id=${item.id}" role="button">编辑</a>
                                        &nbsp;
                                        </c:when>
                                    <c:otherwise>
                                        <font color="#AAAAAA">&nbsp;&nbsp;&nbsp;编辑&nbsp;&nbsp;</font>
                                    </c:otherwise>
                                    </c:choose>
                                    </shiro:hasPermission>
                                    <shiro:hasPermission name="cashBackVoucherEdit:delete">
                                    <c:choose>
                                        <c:when test="${item.isGranted() eq true}">
                                            <a class="glyphicon glyphicon-remove" href="#" role="delete" identity="${item.id}">删除</a>
                                        </c:when>
                                        <c:otherwise>
                                            <font color="#AAAAAA">&nbsp;&nbsp;&nbsp;删除&nbsp;&nbsp;</font>
                                        </c:otherwise>
                                    </c:choose>
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
                                '${path}/reward/red-packet/back-voucher-type-remove',
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