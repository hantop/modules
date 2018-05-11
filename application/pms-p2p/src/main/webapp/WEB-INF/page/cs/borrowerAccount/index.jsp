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
                <li class="active"><b>借款用户</b></li>
            </ol>
			<div id="msg"><span style="margin-left:50px;color: red;font-family: 'Arial Black'">${msg}</span></div>
            <form:form id="dataform" commandName="borrowerAccountForm" method="post" class="form-inline" action="${path}/cs/borrower">
                <div class="form-group">
                    <label>用户账号：</label>
                    <form:input value="" type="text" cssClass="form-control input-sm" id="account" path="account" maxlength="20" style="width:150px;" />
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
                <div class="form-group">
                    <label>账户状态：</label>
                    <form:select path="activeStatus" cssClass="form-control">
                        <form:option value="">全部</form:option>
                        <form:option value="0">未激活</form:option>
                        <form:option value="1">已激活</form:option>
                    </form:select>
                </div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="hidden" id="page" name="page">
                <shiro:hasPermission name="borrowerInfo:search">
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
                        <th>待还金额</th>
                        <th>待还笔数</th>
                        <th>逾期金额</th>
                        <th>逾期笔数</th>
                        <th>账户状态</th>
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
                            <td><%--${v.amountToBePaid}--%>
                                <c:if test="${v.amountToBePaid == null}">
                                    0
                                </c:if>
                                <c:if test="${v.amountToBePaid != null}">
                                    <fmt:formatNumber value="${v.amountToBePaid}" pattern="#.##" type="number"/>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${v.numTobePaid == null}">
                                    0
                                </c:if>
                                <c:if test="${v.numTobePaid != null}">
                                    <fmt:formatNumber value="${v.numTobePaid}" pattern="#.##" type="number"/>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${v.overdueAmount == null}">
                                    0
                                </c:if>
                                <c:if test="${v.overdueAmount != null}">
                                    <fmt:formatNumber value="${v.overdueAmount}" pattern="#.##" type="number"/>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${v.overdueNum == null}">
                                    0
                                </c:if>
                                <c:if test="${v.overdueNum != null}">
                                    <fmt:formatNumber value="${v.overdueNum}" pattern="#.##" type="number"/>
                                </c:if>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${v.activeStatus == 1}">
                                        已激活
                                    </c:when>
                                    <c:otherwise>
                                        未激活
                                    </c:otherwise>
                                </c:choose>
                                <%--<c:if test="${v.activeStatus == 1}">
                                    已激活
                                </c:if>
                                <c:if test="${v.activeStatus == 'DEACTIVATED'}">
                                    未激活
                                </c:if>--%>
                            </td>
                            <td>
                                <c:if test="${v.userType == 'ORGANIZATION'}">
                                    &nbsp;&nbsp;&nbsp;<a href="${path}/cs/borrower/view?userId=${v.userId}">查看</a>
                                </c:if>
                                <c:if test="${v.userType == 'PERSONAL'}">
                                    <font color="#AAAAAA">&nbsp;&nbsp;&nbsp;查看&nbsp;</font>
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
    });

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
