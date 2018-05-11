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
                <li class="active"><a href="${path}/marketing/index">营销管理</a></li>
                <li class="active"><b>活动设置</b></li>
                <li class="active"><b>添加加息券</b></li>
            </ol>
            <form id="dataform" class="form-inline" method="post" action="${path}/marketing/activity/couponList">
                <input type="hidden" id="page" name="page">
                <input type="hidden" id="num" name="num" value="shenmedongxi">
            </form>
            <hr style="margin-bottom: 0;">
            <div class="panel panel-default">
                <div class="panel-heading">加息券列表</div>
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>代码</th>
                        <th>加息券有效期</th>
                        <th>投资限额</th>
                        <th>加息幅度</th>
                        <th>投资期限</th>
                        <th>可用标类型</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    	<c:forEach var="item" items="${list}">
                            <tr>
                                <td>${item.couponCode}
                                 <input name="item" type="hidden"  onclick="checkItem(this);" value="${item.id}">
                                </td>
                                <td>${item.effectDay}</td>
                                <td>上限:&nbsp;&nbsp;${item.maxInvestMoney}<br/>
                                   	 下限:&nbsp;&nbsp;${item.minInvestMoney}
								</td>
                                <td><fmt:formatNumber value="${item.scope}" pattern="#.##" type="number"/>%</td>
                                <td>
                                <c:choose>
                                	<c:when test="${(item.minInvestDay == 0 || item.minInvestDay eq null) && (item.maxInvestDay == 0 || item.maxInvestDay eq null)}">
                                		<c:out value='不限' default="" escapeXml="true"/>
                                	</c:when>
                                	<c:otherwise>
                                		<c:out value='${item.minInvestDay} - ${item.maxInvestDay}天' default="" escapeXml="true"/>
                                	</c:otherwise>
                                </c:choose>
                                </td>
                                <td>
                                	<c:choose>
                                	<c:when test="${item.bidTypeStr eq null }">
                                		<c:out value='不限' default="" escapeXml="true"/>
                                	</c:when>
                                	<c:otherwise>
                                		<c:out value='${item.bidTypeStr}' default="" escapeXml="true"/>
                                	</c:otherwise>
                                </c:choose>
                                </td>
                                <td>
                               		<a class="selectCoupon" href="javascript:void(0);" data-json='${item.jsonString}'>选择</a>
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
<script type="text/javascript">
	var num = getQueryString('id');
	$("#num").val(parseInt(num));
	if(num != null){
		sessionStorage.setItem("num", num);
	}
	var numId = sessionStorage.getItem("num");
	function getQueryString(name) {
	    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	    var r = window.location.search.substr(1).match(reg);
	    if (r != null) return unescape(r[2]);
	    return null;
	}
	
    function pagination(page,num) {
        $('#page').val(page);
        var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $("#dataform").submit();
        layer.close();
    }
    
     $(".selectCoupon").click(function(){
		//window.returnValue = $(this).data("json");
		 window.opener.returnValue = $(this).data("json");
		 var a = window.opener.returnValue;
		window.opener.get(window.opener.returnValue, numId); 
		//操作完之后关闭窗口
		window.close();
	}) 
</script>