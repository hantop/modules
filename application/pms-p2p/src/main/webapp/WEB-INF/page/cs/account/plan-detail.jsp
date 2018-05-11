<%@page language="java" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<link href="/static/component/JqueryPagination/mricode.pagination.css" rel="stylesheet">

<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<script type="text/javascript" src="/static/component/jquery-form/jquery.form.js"></script>
<script type="text/javascript" src="/static/component/JqueryPagination/mricode.pagination.js"></script>
<script type="text/javascript" src="/static/component/JqueryPagination/jsrender.js"></script>
<script type="text/javascript" src="/static/component/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/static/component/datepicker/calendar.js"></script>
<script type="application/javascript">
    (function ($) {
        $.fn.serializeObject = function () {
            "use strict";

            var result = {};
            var extend = function (i, element) {
                var node = result[element.name];

                // If node with same name exists already, need to convert it to an array as it
                // is a multi-value field (i.e., checkboxes)
                if ($.trim(element.value) !== "") {
                    if ('undefined' !== typeof node && node !== null) {
                        if ($.isArray(node)) {
                            node.push(element.value);
                        } else {
                            result[element.name] = [node, element.value];
                        }
                    } else {
                        result[element.name] = element.value;
                    }
                }
            };
            $.each(this.serializeArray(), extend);
            return result;
        };
    })(jQuery);

    Array.prototype.contains = function (obj) {
        var i = this.length;
        while (i--) {
            if (this[i] === obj) {
                return true;
            }
        }
        return false;
    }
</script>

<style type="text/css">
    /**去除bootstrap td 的边框*/
    table.account tr td {
        border: none;
    }
</style>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/cs/index">客户管理</a></li>
                <li class="active"><a href="${path}/transaction/account-index?userId=${investPlanForm.userId}">用户信息</a></li>
                <li class="active"><b>标的信息</b></li>
            </ol>
            <form id="dataform" class="form-inline" action="${path}/transaction/plan-detail">
                <input type="hidden" id="userId" name="userId" value="${investPlanForm.userId}">
                <input type="hidden" id="planType" name="planType" value="${investPlanForm.planType}">
                <input type="hidden" id="planId" name="planId" value="${investPlanForm.planId}">
                <input type="hidden" id="recordId" name="recordId" value="${investPlanForm.recordId}">
                <input type="hidden" id="page" name="page">
            </form>
            <hr style="margin-bottom: 0;">
            <div class="panel panel-default">
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>标的类型</th>
                        <th>标的名称</th>
                        <th>匹配金额</th>
                        <th>年利率</th>
                        <th>标的期限</th>
                        <th>还款方式</th>
                        <th>匹配时间</th>
                        <th>退出时间</th>
                        <th>标的状态</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${list}" var="v" varStatus="i">
                        <tr>
                            <td>${v.bidType}</td>
                            <td>${v.bidName}</td>
                            <td>${v.matchMoney}</td>
                            <td>${v.rate}%</td>
                            <td>
                                <c:if test="${v.month > 0}">
                                    ${v.month}个月
                                </c:if>
                                <c:if test="${v.day > 0}">
                                    ${v.day}天
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${v.repayment.equals('DEBX')}">
                                    等额本息
                                </c:if>
                                <c:if test="${v.repayment.equals('MYFX')}">
                                    每月付息,到期还本
                                </c:if>
                                <c:if test="${v.repayment.equals('YCFQ')}">
                                    一次付清
                                </c:if>
                                <c:if test="${v.repayment.equals('DEBJ')}">
                                    等额本金
                                </c:if>
                            </td>
                            <td>
                                <fmt:formatDate value="${v.matchTime}" pattern="yyyy-MM-dd HH:mm"/>
                            </td>
                            <td>
                                <fmt:formatDate value="${v.quitTime}" pattern="yyyy-MM-dd"/>
                            </td>
                            <td>
                                <c:if test="${v.status.equals('TBZ')}">
                                    投标中
                                </c:if>
                                <c:if test="${v.status.equals('DFK')}">
                                    待放款
                                </c:if>
                                <c:if test="${v.status.equals('HKZ')}">
                                    还款中
                                </c:if>
                                <c:if test="${v.status.equals('YJQ')}">
                                    已结清
                                </c:if>
                                <c:if test="${v.status.equals('ZRZ')}">
                                    转让中
                                </c:if>
                                <c:if test="${v.status.equals('YZR')}">
                                    已转让
                                </c:if>
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
<script type="application/javascript">
    $(function () {
        $.ajaxSetup({
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            complete: function (data, TS) {
                //console.info(arguments);
                //对返回的数据data做判断，
                //session过期的话，就location到一个页面
                if(TS !== 'success') {
                    window.location.reload();
                }
            }
        });

        function getUrlParam(url,key){
            url = url || location.search;
            var arr = [],obj = {};
            if(key){
                url.replace(new RegExp("[&?]"+ key + "=([^&#]*)","ig"), function(a,b) {
                    arr.push(b);
                });
                return arr.join(",");
            }else{
                url.replace(/[?&]([^&#]+)=([^&#]*)/g, function(a,b,c){
                    obj[b]=c;
                });
                return obj;
            }
        }
        
        //获取地址栏中的参数
        /*var url = window.location.search;
        var userId = getUrlParam(url,"userId");
        var planType = getUrlParam(url,"planType");
        var planId = getUrlParam(url,"planId");
        $("#userId").val(userId);
        $("#planType").val(planType);
        $("#planId").val(planId);*/
        /*var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $("#dataform").submit();
        layer.close();*/
    });

    function pagination(page) {
        $('#page').val(page);
        var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $('#dataform').submit();
        layer.close();
    }
</script>
