<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!--My 97 date -->
<script type="text/javascript" src="/static/component/datepicker/calendar.js"></script>
<link rel="stylesheet" href="/static/component/HoldOn/HoldOn.min.css">
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script type="text/javascript" src="/static/component/HoldOn/HoldOn.min.js"></script>
<script type="text/javascript" src="/static/component/jquery-form/jquery.form.js"></script>
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script type="text/javascript" src="/static/component/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/static/component/HoldOn/HoldOn.min.js"></script>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>

<link rel="stylesheet" href="/static/component/HoldOn/HoldOn.min.css">
<style type="text/css">
    .red_font {
        color: red;
    }
</style>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/planCenter/index">计划中心</a></li>
                <li class="active"><b>模板管理</b></li>
            </ol>
            <form id="dataform" class="form-inline" method="post" action="${path}/planCenter/planType/index">
                <div class="form-group">
                    <label>组合名称：</label>
                    <input id="title" name="title" value="${planTypeForm.title}" type="text" class="form-control input-sm"/>
                </div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <%--<div class="form-group">
                    <label>组合类型：</label>
                    <select id="productTerm" name="productTerm" class="form-control input-sm"
                            style="width:115px;" value="${planMarketSettingForm.productTerm}">
                        <option value="0">全部</option>
                        <option value="10"
                                <c:if test='${planMarketSettingForm.productTerm == 10}'>selected="selected"</c:if>>10天</option>
                        <option value="20"
                                <c:if test='${planMarketSettingForm.productTerm == 20}'>selected="selected"</c:if>>20天</option>
                        <option value="30"
                                <c:if test='${planMarketSettingForm.productTerm == 20}'>selected="selected"</c:if>>30天</option>
                    </select>
                </div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <div class="form-group">
                    <label>组合类型：</label>
                    <select id="productType" name="productType" class="form-control input-sm"
                            style="width:115px;" value="${planMarketSettingForm.productType}">
                        <option value="0">全部</option>
                        <option value="1"
                                <c:if test='${planMarketSettingForm.productType == 1}'>selected="selected"</c:if>>月月升组合</option>
                        <option value="2"
                                <c:if test='${planMarketSettingForm.productType == 2}'>selected="selected"</c:if>>消费信贷组合</option>
                    </select>
                </div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--%>
                <input type="hidden" id="page" name="page">
                <shiro:hasPermission name="planType:view">
                    <button id="searchBtn" type="submit" class="btn btn-primary btn-sm">搜索</button>
                </shiro:hasPermission>
            </form>
            <hr>
            <div class="panel panel-default" style="width:1300px!important;">
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>序号</th>
                        <th>组合名称</th>
                        <th>组合限额</th>
                        <th>组合期限</th>
                        <th>组合利率</th>
                        <th>组合加息</th>
                        <th>状态</th>
                        <th>组合类型</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    	<c:forEach var="item" items="${list}" varStatus="vs">
                            <tr>
                                <%--<td>
                                    <c:choose>
                                        <c:when test="${item.isCG == 1}">
                                            普通
                                        </c:when>
                                        <c:when test="${item.isCG == 2}">
                                            存管
                                        </c:when>
                                    </c:choose>
                                </td>--%>
                                <td>${vs.index + 1}</td>
                                <td>${item.title}</td>
                                <td>${item.amount}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${item.cycleType == 'm'}">
                                            ${item.cycle}个月
                                        </c:when>
                                        <c:when test="${item.cycleType == 'd'}">
                                            ${item.cycle}天
                                        </c:when>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${item.rate > 0}">
                                            <fmt:formatNumber value="${item.rate}" pattern="#.##" type="number"/>%
                                        </c:when>
                                    </c:choose>
                                </td>
                                <td>
                                    <fmt:formatNumber value="${item.raisedRate}" pattern="#.##" type="number"/>%
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${item.status == 'TY'}">
                                            停用
                                        </c:when>
                                        <c:when test="${item.status == 'QY'}">
                                            启用
                                        </c:when>
                                        <c:otherwise>
                                            -
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>消费贷组合</td>
                                <td>
                                    <shiro:hasPermission name="planType:setStatus">
                                    <c:choose>
                                        <c:when test="${item.status == 'TY'}">
                                            <a href="#" onclick="setStatus(${item.id}, 'QY');" role="button">&nbsp;&nbsp;&nbsp;启用&nbsp;</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="#" onclick="setStatus(${item.id}, 'TY');" role="button">&nbsp;&nbsp;&nbsp;停用&nbsp;</a>
                                        </c:otherwise>
                                    </c:choose>
                                    </shiro:hasPermission>
                                    <shiro:hasPermission name="planType:edit">
                                    <a class="glyphicon glyphicon-pencil" href="${path}/planCenter/planType/edit?id=${item.id}" role="button">修改</a>
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

<div id="tips" class="modal fade">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="mySmallModalLabel">Tips</h4>
            </div>
            <div class="modal-body tips"></div>
        </div>
    </div>
</div>

<script type="text/javascript">
    function pagination(page) {
        $('#page').val(page);
        var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $("#dataform").submit();
        layer.close();
    }

    function confirmMsg(id, status) {
        var msg = null;
        if (status == 'QY') {
            msg = "确定启用该计划模板?";
        } else {
            msg = "确定停用该计划模板?";
        }
        return msg;
    }

    function confirmTitle(id, status) {
        var title = null;
        if (status == 'QY') {
            title = "启用";
        } else {
            title = "停用";
        }
        return title;
    }

    function setStatus(id, status) {
        bootbox.confirm({
            title: confirmTitle(id, status),
            size: 'small',
            message: confirmMsg(id, status),
            callback: function (result) {
                if (result) {
                    $.post(
                        path + "/planCenter/planType/setStatus",
                        {"id" : id,
                        "status" : status},
                        function (data) {
                            var code = data.code;
                            console.log(code + "shshshshsh");
                            var message = null;
                            if(code == '200'){
                                if(status == 'QY'){
                                    bootbox.alert({message: "启用成功", title: '操作信息',callback: function() {
                                        window.location.reload(true);}});
                                }else {
                                    bootbox.alert({message: "停用成功", title: '操作信息',callback: function() {
                                        window.location.reload(true);}});
                                }
                            }else{
                                if(status == 'QY'){
                                    bootbox.alert({message: "启用失败，请稍后再试!", title: '错误信息'});
                                }else {
                                    bootbox.alert({message: "停用失败，请稍后再试!", title: '错误信息'});
                                }
                            }
                        }
                    );
                }
            }
        })
    }

</script>