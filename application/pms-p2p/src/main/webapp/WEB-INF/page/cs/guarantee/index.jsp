<%@page language="java" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<link href="/static/component/JqueryPagination/mricode.pagination.css" rel="stylesheet">

<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<script type="text/javascript" src="/static/component/jquery-form/jquery.form.js"></script>
<script type="text/javascript" src="/static/component/JqueryPagination/mricode.pagination.js"></script>
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
                <li class="active"><b>担保用户</b></li>
            </ol>
			<div id="msg"><span style="margin-left:50px;color: red;font-family: 'Arial Black'">${msg}</span></div>
            <form:form id="dataform" commandName="guaranteeForm" method="post" class="form-inline" action="${path}/cs/guarantee">
                <div class="form-group">
                    <label>用户账号：</label>
                    <form:input value="" type="text" cssClass="form-control input-sm" id="account" path="account" maxlength="20" style="width:110px;" />
                </div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <div class="form-group">
                    <label>用户名称：</label>
                    <form:input value="" type="text" cssClass="form-control input-sm" id="name" path="name" maxlength="20" style="width:110px;" />
                </div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <div class="form-group">
                    <label>用户类型：</label>
                    <form:select path="userType" cssClass="form-control">
                        <form:option value="">全部</form:option>
                        <form:option value="ORGANIZATION">企业</form:option>
                        <form:option value="PERSONAL">个人</form:option>
                    </form:select>
                </div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="hidden" id="page" name="page">
                <shiro:hasPermission name="guarantee:search">
                    <button type="button" class="btn btn-primary btn-sm" id="searchBtn">搜索</button>
                </shiro:hasPermission>
            </form:form>
            <hr style="margin-bottom: 0;">
            <div class="panel panel-default">
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>用户类型</th>
                        <th>用户账号</th>
                        <th>用户名称</th>
                        <th>账号余额</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${list}" var="v" varStatus="vs">
                        <tr>
                            <td>
                                <c:if test="${v.userType == 'PERSONAL'}">
                                    个人
                                </c:if>
                                <c:if test="${v.userType == 'ORGANIZATION'}">
                                    企业
                                </c:if>
                            </td>
                            <td>${v.account}</td>
                            <td>${v.name}</td>
                            <td>${v.balance}</td>
                            <td>
                                <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                                <c:if test="${v.userType == 'ORGANIZATION'}">
                                    <shiro:hasPermission name="guarantee:info">
                                        <a href="${path}/cs/guarantee/view?userId=${v.userId}" style="color: #2991ef">查看</a>
                                    </shiro:hasPermission>
                                </c:if>
                                <c:if test="${v.userType == 'PERSONAL'}">
                                    <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                                </c:if>
                                <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                                <shiro:hasPermission name="guarantee:recharge">
                                    <a href="${path}/cs/guarantee/rechargePre?userId=${v.userId}" style="color: #2991ef">充值</a>
                                </shiro:hasPermission>
                                <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                                <shiro:hasPermission name="guarantee:withdraw">
                                    <a href="${path}/cs/guarantee/withdrawPre?userId=${v.userId}" style="color: #2991ef">提现</a>
                                </shiro:hasPermission>
                                <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
                                <c:choose>
                                    <c:when test="${v.isBindBank != null and v.isBindBank == 0}">
                                        <shiro:hasPermission name="guarantee:bind">
                                            <a href="javascript:void(0)" onclick="bindBankcard('${v.userId}', '${v.userType}');" style="color: #2991ef">绑定卡</a>
                                        </shiro:hasPermission>
                                    </c:when>
                                    <c:when test="${v.isBindBank != null and v.isBindBank==1 and v.allowUnbind != null and v.allowUnbind ==1}">
                                        <shiro:hasPermission name="guarantee:unbind">
                                            <a href="javascript:void(0)" onclick="unbindBankcard('${v.userId}');" style="color: #2991ef">解绑卡</a>
                                        </shiro:hasPermission>
                                    </c:when>
                                    <c:otherwise>
                                        <a href ="javascript:return false;" onclick="return false;" style="cursor: default;">
                                            <i class="edit" style="opacity: 0.6">解绑卡</i>
                                        </a>
                                    </c:otherwise>
                                </c:choose>
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
<form id="bind_or_unbind_form" action="" method="post"></form>
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
    });
    // 绑定银行卡
    function bindBankcard(userId, userType) {
        var data = {userId:userId, userType:userType};
        if (userType == 'ORGANIZATION') {
            self.location='${path}/cs/guarantee/bankcard?userId='+userId;
        } else {
            doSubmitOperator("${path}/cs/guarantee/bindBank", data);
        }

    }
    // 解绑银行卡
    function unbindBankcard(userId) {
        var data = {userId:userId};
        doSubmitOperator("${path}/cs/guarantee/unbindBank", data);
    }
    function doSubmitOperator(url, data) {
        $("#bind_or_unbind_form").empty();
        $.ajax({
            type: "get",
            async: false,
            url: url,
            data: data,
            dataType: "json",
            success: function(data){
                if (data.code != '200') {
                    bootbox.alert(data.message);
                    return;
                }
                $("#bind_or_unbind_form").attr('action', data.data.postUrl);
                $.each(data.data.postParams, function (index, obj) {
                    $('#bind_or_unbind_form').append('<input type="hidden" name=\'' + obj.key + '\' value=\'' + obj.value + '\'/>');
                });
                $("#bind_or_unbind_form").submit(); // 提交到新网
            },
            error: function (e) {
                console.log(e);
            }
        });
    }
    $('#searchBtn').bind('click', function() {
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

</script>
