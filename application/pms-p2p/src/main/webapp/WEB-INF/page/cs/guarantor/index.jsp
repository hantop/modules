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

<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/cs/index">客户管理</a></li>
                <li class="active"><b>账户管理</b></li>
            </ol>
			<div id="msg"><span style="margin-left:50px;color: red;font-family: 'Arial Black'">${msg}</span></div>
            <form:form id="dataform" commandName="guarantorForm" method="post" class="form-inline" action="${path}/cs/guarantor">
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
                <div class="form-group">
                    <label>账户类型：</label>
                    <form:select path="accountType" cssClass="form-control">
                        <form:option value="">全部</form:option>
                        <form:option value="BORROWERS">借款账户</form:option>
                        <form:option value="GUARANTEECORP">担保账户</form:option>
                    </form:select>
                </div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <div class="form-group">
                    <label>开户状态：</label>
                    <form:select path="auditStatus" cssClass="form-control">
                        <form:option value="">全部</form:option>
                        <form:option value="WAIT">待提交</form:option>
                        <form:option value="AUDIT">审核中</form:option>
                        <form:option value="BACK">审核回退</form:option>
                        <form:option value="REFUSED">审核拒绝</form:option>
                        <form:option value="PASSED">审核通过</form:option>
                    </form:select>
                </div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="hidden" id="page" name="page">
                <shiro:hasPermission name="guarantor:search">
                    <button type="button" class="btn btn-primary btn-sm" id="searchBtn">搜索</button>
                </shiro:hasPermission>
            </form:form>

            <shiro:hasPermission name="guarantor:add">
                <%--<button type="button" class="btn btn-primary btn-sm" id="createBtn">创建担保人</button>--%>
                <a class="btn btn-warning btn-sm" href="${path}/cs/guarantor/new" style="margin-top: 8px;margin-left: 10px;" role="button">创建担保人</a>
            </shiro:hasPermission>

            <hr style="margin-bottom: 0;">
            <div class="panel panel-default">
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>用户类型</th>
                        <th>用户账号</th>
                        <th>用户名称</th>
                        <th>账户类型</th>
                        <th>开户状态</th>
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
                            <td>
                                <c:if test="${v.accountType == 'BORROWERS'}">
                                    借款账户
                                </c:if>
                                <c:if test="${v.accountType == 'GUARANTEECORP'}">
                                    担保账户
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${v.auditStatus == 'WAIT'}">
                                    待提交
                                </c:if>
                                <c:if test="${v.auditStatus == 'AUDIT'}">
                                    审核中
                                </c:if>
                                <c:if test="${v.auditStatus == 'BACK'}">
                                    审核回退
                                </c:if>
                                <c:if test="${v.auditStatus == 'REFUSED'}">
                                    审核拒绝
                                </c:if>
                                <c:if test="${v.auditStatus == 'PASSED'}">
                                    审核通过
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${v.accountType == 'GUARANTEECORP' and v.userType == 'ORGANIZATION'}">
                                    <shiro:hasPermission name="guarantor:info">
                                        <a href="${path}/cs/guarantor/view?userId=${v.userId}&accountType=${v.accountType}&auditStatus=${v.auditStatus}" style="color: #2991ef">查看</a>
                                    </shiro:hasPermission>
                                    <c:if test="${v.auditStatus == 'WAIT'}">
                                        <a href="${path}/cs/guarantor/view?userId=${v.userId}&accountType=${v.accountType}&auditStatus=${v.auditStatus}" style="color: #2991ef">修改</a>
                                    </c:if>
                                    <c:if test="${v.auditStatus == 'AUDIT'}">
                                        <a href ="javascript:return false;" onclick="return false;" style="cursor: default;">
                                            <i class="edit" style="opacity: 0.6">修改</i>
                                        </a>
                                    </c:if>
                                    <c:if test="${v.auditStatus == 'BACK'}">
                                        <a href ="javascript:return false;" onclick="return false;" style="cursor: default;">
                                            <i class="edit" style="opacity: 0.6">修改</i>
                                        </a>
                                    </c:if>
                                    <c:if test="${v.auditStatus == 'REFUSED'}">
                                        <a href ="javascript:return false;" onclick="return false;" style="cursor: default;">
                                            <i class="edit" style="opacity: 0.6">修改</i>
                                        </a>
                                    </c:if>
                                    <c:if test="${v.auditStatus == 'PASSED'}">
                                        <shiro:hasPermission name="guarantor:update">
                                            <a id="update_enterprise_a_id" href="javascript:void(0)" onclick="updateEnterprise('${v.userId}');" style="color: #2991ef">修改</a>
                                        </shiro:hasPermission>
                                    </c:if>
                                </c:if>
                                <c:if test="${v.accountType == 'BORROWERS' and v.userType == 'ORGANIZATION'}">
                                    <shiro:hasPermission name="guarantor:info">
                                        <a href="${path}/cs/guarantor/view?userId=${v.userId}&accountType=${v.accountType}&auditStatus=${v.auditStatus}" style="color: #2991ef">查看</a>
                                    </shiro:hasPermission>
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

<form id="update_enterprise_form" action="" method="post"></form>
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

    function updateEnterprise(userId) {
        $.ajax({
            type: "get",
            async: false,
            url: "${path}/cs/guarantor/updateEnterprise",
            data: {userId: userId},
            dataType: "json",
            success: function(data){
                if (data.code != '200') {
                    alert(data.message);
                    return;
                }
                $("#update_enterprise_form").attr('action', data.data.postUrl);
                $.each(data.data.postParams, function (index, obj) {
                    $('#update_enterprise_form').append('<input type="hidden" name=\'' + obj.key + '\' value=\'' + obj.value + '\'/>');
                });
                $("#update_enterprise_form").submit(); // 提交到新网
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
