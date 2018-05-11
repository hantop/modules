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
                <li class="active"><b>用户查询</b></li>
            </ol>
			<div id="msg"><span style="margin-left:50px;color: red;font-family: 'Arial Black'">${msg}</span></div>
            <form class="form-inline" method="post" action="${path}/transaction"  id="parent_form">
                <shiro:hasPermission name="accountInfo:search">
                <div class="form-group">
                    <label class="control-label">用户手机号</label>
                    &nbsp;&nbsp;
                    <input path="phoneNum" id="phoneNum" name="phoneNum" value="${transactionForm.phoneNum}" class="form-control input-sm" maxlength="30"/>
                    &nbsp;&nbsp;
                    <label class="control-label">姓名</label>
                    &nbsp;&nbsp;
                    <input path="phoneNum" id="name" name="name" value="${transactionForm.name}" class="form-control input-sm" maxlength="30"/>
                    &nbsp;&nbsp;
                    <label class="control-label">身份证</label>
                    &nbsp;&nbsp;
                    <input path="phoneNum" id="idCard" name="idCard" value="${transactionForm.idCard}" class="form-control input-sm" maxlength="30"/>
                    &nbsp;&nbsp;
                </div>
                <button id="searchBtn" type="button" class="btn btn-primary btn-sm">查询</button>
                </shiro:hasPermission>
                <input type="hidden" name="operator" value="<shiro:principal/>" id="operator">
            </form>
            <hr style="margin-bottom: 0;">
            <div class="panel panel-default">
            	<input  id="resultCode" name="resultCode" value="${resultCode}" type="hidden"/>
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>序号</th>
                        <th>手机号</th>
                        <th>姓名</th>
                        <th>昵称</th>
                        <th>身份证号码</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${userDetailList}" var="v" varStatus="vs">
                        <tr>
                            <td>${vs.index + 1}</td>
                            <td>${v.phoneNum}</td>
                            <td>${v.name}</td>
                            <td>${v.nickName}</td>
                            <td>${v.idCard}</td>
                            <td>
                                <a href="${path}/transaction/account-index?userId=${v.userId}">查看</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
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
    	 
    	 $('#searchBtn').bind('click', function() {
             var phoneNum = $("#phoneNum").val();
	         var name = $("#name").val();
	         var idCard = $("#idCard").val();
	         var resultCode = $("#resultCode").val();
	         var $this = $('#parent_form');
	         if ((phoneNum == null || phoneNum == '') && (name == null || name == '') && (idCard == null || idCard == '')) {
	             bootbox.alert({message: "<h3>请至少填写一项</h3>"});
	         } else  {
	        	 var index = layer.load(0, {
		                shade: [0.4,'#fff',false] //0.1透明度的白色背景
		            }); 
	            $("#parent_form").submit();
	           layer.close();
			}
        });
    });

</script>
