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
                <li class="active"><a href="${path}/reward/rateCoupon">加息券管理</a></li>
                <li class="active"><b>加息券编辑</b></li>
            </ol>
            <form:form class="form-inline" method="post" action="${path}/reward/rateCoupon/rate-coupon-type-list" id="dataform" commandName="rateCouponForm">
                <shiro:hasPermission name="rateCouponEdit:create">
                <div class="form-group" style="margin-bottom: 5px;">
                    <a class="btn btn-info btn-sm" href="${path}/reward/rateCoupon/rate-coupon-type-edit" role="button">添加加息券类型</a>
                </div><br/>
                </shiro:hasPermission>

                <shiro:hasPermission name="rateCouponEdit:search">
                <div class="form-group">
                    <label>加息券代码: </label>
                    <form:input path="couponCode" cssClass="form-control" maxlength="30"/>
                </div>
                <input type="hidden" id="page" name="page">

                <button type="submit" class="btn btn-primary btn-sm">搜索</button>
                </shiro:hasPermission>
            </form:form>
        </div>
        <div class="col-md-12">
            <table class="table table-striped table-bordered table-condensed table-hover" style="margin-top: 20px;">
                <thead>
                    <tr class="success">
                        <th>加息券代码</th>
                        <th>加息幅度</th>
                        <th>投资限额</th>
                        <th>投资期限</th>
                        <th>可用标的类型</th>
                        <th>加息券有效期</th>
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
                                <td><c:out value="${item.couponCode}" default="" escapeXml="true"/></td>
                                <td>
                                    <fmt:formatNumber value="${item.scope}" pattern="#.##" type="number"/>%
                                </td>
                                <td>${item.minInvestMoney}-${item.maxInvestMoney}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${item.minInvestDay > 0 && item.maxInvestDay > 0}">
                                            ${item.minInvestDay}-${item.maxInvestDay}天
                                        </c:when>
                                        <c:otherwise>
                                            不限
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${item.bidTypeStr}</td>
                                <td>${item.effectDay}天</td>
                                <td>
                                    <c:if test="${item.isCanOperate() eq true}">
                                        <shiro:hasPermission name="rateCouponEdit:edit">
                                            <a class="glyphicon glyphicon-pencil" href="${path}/reward/rateCoupon/rate-coupon-type-edit?id=${item.id}" role="button">编辑</a>
                                        &nbsp;
                                        </shiro:hasPermission>
                                        <shiro:hasPermission name="rateCouponEdit:delete">
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
                                '${path}/reward/rateCoupon/rate-coupon-type-remove',
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