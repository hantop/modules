<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<link href="/static/component/JqueryPagination/mricode.pagination.css" rel="stylesheet">

<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<script type="text/javascript" src="/static/component/jquery-form/jquery.form.js"></script>
<script type="text/javascript" src="/static/component/JqueryPagination/mricode.pagination.js"></script>
<script type="text/javascript" src="/static/component/datepicker/WdatePicker.js"></script>
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
                <li class="active"><a href="${path}/cs/index">财务管理</a></li>
                <li class="active"><b>平台账户管理</b></li>
                <li class="active"><b>
                    <%--XW_PLATFORM_FUNDS_TRANSFER_WLZH:新网平台总账户;
                            XW_PLATFORM_COMPENSATORY_WLZH:新网平台代偿账户;
                            XW_PLATFORM_MARKETING_WLZH:新网平台营销款账户;
                            XW_PLATFORM_PROFIT_WLZH:新网平台分润账户;
                            XW_PLATFORM_INCOME_WLZH:新网平台收入账户;
                            XW_PLATFORM_INTEREST_WLZH:新网平台派息账户;
                            XW_PLATFORM_ALTERNATIVE_RECHARGE_WLZH:新网平台代充值账户;--%>
                    <c:choose>
                        <c:when test="${accountType == 'XW_PLATFORM_FUNDS_TRANSFER_WLZH'}">
                            平台总账户
                        </c:when>
                        <c:when test="${accountType == 'XW_PLATFORM_COMPENSATORY_WLZH'}">
                            平台代偿账户
                        </c:when>
                        <c:when test="${accountType == 'XW_PLATFORM_MARKETING_WLZH'}">
                            平台营销款账户
                        </c:when>
                        <c:when test="${accountType == 'XW_PLATFORM_PROFIT_WLZH'}">
                            平台分润账户
                        </c:when>
                        <c:when test="${accountType == 'XW_PLATFORM_INCOME_WLZH'}">
                            平台收入账户
                        </c:when>
                        <c:when test="${accountType == 'XW_PLATFORM_INTEREST_WLZH'}">
                            平台派息账户
                        </c:when>
                        <c:when test="${accountType == 'XW_PLATFORM_ALTERNATIVE_RECHARGE_WLZH'}">
                            平台代充值账户
                        </c:when>
                        <c:otherwise>
                        </c:otherwise>
                    </c:choose>

                </b></li>
            </ol>
			<div id="msg"><span style="margin-left:50px;color: red;font-family: 'Arial Black'">${msg}</span></div>
            <form class="form-inline" method="post" action="${path}/finance/accountmanagement/tradeHistory"  id="parent_form">
               <%-- <shiro:hasPermission name="accountInfo:search">--%>
                   <div class="form-group">
                       <input type="hidden" id="accountType" name="accountType" value ="${accountType}">
                       <label class="control-label">日期</label>
                       <input name="startDate"
                              type="text" readonly="readonly" maxlength="20"
                              class="form-control input-sm" style="width: 163px;"
                              value="<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"/>"
                              onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,readOnly:true,maxDate:'#F{$dp.$D(\'endDate\')}'});"
                              onFocus="WdatePicker({isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'})"/>
                       --
                       <input name="endDate"
                              type="text" readonly="readonly" maxlength="20"
                              class="form-control input-sm" style="width: 163px;"
                              value="<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"/>"
                              onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false,readOnly:true,minDate:'#F{$dp.$D(\'startDate\')}'});"
                              onFocus="WdatePicker({isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'})"/>
                   </div>
                   <input type="hidden" id="page" name="page">
                   <button id="searchBtn" type="button" class="btn btn-primary btn-sm">搜索</button>
                   <button type="button" class="btn btn-success btn-sm" onclick="javascript: exportReturncashs(this );">导出</button>
                <%--</shiro:hasPermission>--%>

            </form>
            <hr style="margin-bottom: 0;">
            <div class="panel panel-default">
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>时间</th>
                        <th>名称</th>
                        <th>交易金额（元）</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${historys}" var="v" varStatus="vs">
                        <tr>
                            <td>${v.tradeTime}</td>
                            <td>${v.tradeTypeName}</td>
                            <td>${v.tradeAmount}</td>
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
    	 
    	 $('#searchBtn').bind('click', function() {
             var startDate = $("#startDate").val();
	         var endDate = $("#endDate").val();
	         if (startDate == '' && endDate == '') {
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
    function pagination(page) {
        $('#page').val(page);
        var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $("#parent_form").submit();
        layer.close();
    }
    function exportReturncashs(obj){
        console.log(111)
        console.log(obj);
        console.log($('#page').val());
        var form = $("#parent_form");
        form.attr('action',path + "/finance/accountmanagement/export");
        form.submit();
        form.attr('action',path + "/finance/accountmanagement/tradeHistory");
    }
</script>
