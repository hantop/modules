<%@page language="java" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<div class="container">
	<div class="row">
		<div class="col-md-12">
			<ol class="breadcrumb">
				<li class="active"><a href="${path}/reward/index">奖励管理</a></li>
				<li class="active"><a href="${path}/reward/red-packet/cash-red-packet-index">现金红包管理</a></li>
				<li class="active"><b>发送详情</b></li>
			</ol>
			<c:if test="${granted==true}">
                <div>
                    <table class="table">
                        <tr>
                            <td class="text-danger">总计</td>
                            <td class="text-danger">发送数：${grantCount}</td>
                            <td class="text-danger">发送金额（元）：${grantSum}</td>
                        </tr>
                    </table>
                    <hr>
                </div>
            </c:if>
			<form id="dataform" method="post" action="${path}/reward/red-packet/cash-red-packet-detail" class="form-inline">
				<input type="hidden" id="page" name="page">
				<input type="hidden" name="granted" value="${granted}">
				<input type="hidden" id="grantId" name="grantId"	value="${userCashRedPacket.grantId}">
                <shiro:hasPermission name="cashRedPacketDetail:search">
				<label>发送状态：</label>
					<select name="grantStatus" id="grantStatus" class="form-control">
					<option value="">全部</option>
					<option value="DZZ"
						<c:if test="${userCashRedPacket.grantStatus == 'DZZ' }">selected="selected"</c:if>>未发送</option>
					<option value="CG"
						<c:if test="${userCashRedPacket.grantStatus == 'CG' }">selected="selected"</c:if>>成功</option>
					 <option value="SB" <c:if test="${userCashRedPacket.grantStatus == 'SB' }">selected="selected"</c:if>>失败</option>
					<option value="ZF" <c:if test="${userCashRedPacket.grantStatus == 'ZF' }">selected="selected"</c:if>>已作废</option>
				</select>
				&nbsp;&nbsp; 
				<button type="button" class="btn btn-primary" id="searchBtn">搜索</button>
				</shiro:hasPermission>
				<shiro:hasPermission name="cashRedPacketDetail:export">
				    <button type="button" class="btn btn-success" onclick="javascript: exportDetail();">导出</button>
				</shiro:hasPermission>	
				<button type="button" class="btn btn-warning" onclick="window.location.href='${path}/reward/red-packet/cash-red-packet-index'">返回</button>
			</form>
			<hr>
		</div>
		<div class="col-md-6">
			<div class="panel panel-default">
				<table class="table table-striped table-bordered table-condensed">
					<thead>
						<tr class="success">
							<th>用户手机号</th>
							<th>红包金额</th>
							<th>发送状态</th>
							<th>异常信息</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${list}" var="v" varStatus="vs">
							<tr>
								<td>${v.phone}</td>
								<td>${v.money}</td>
								<td><c:if test="${v.grantStatus=='DZZ'}">未发送</c:if>
								 <c:if	test="${v.grantStatus=='CG'}">已成功</c:if> 
								 <c:if	test="${v.grantStatus=='SB'}">发送失败</c:if>
								 <c:if	test="${v.grantStatus=='ZF'}">已作废</c:if>
								 </td>
								<td>${v.msg}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<tiles:insertDefinition name="paginator" />
		</div>
	</div>
</div>
<script type="text/javascript">
	$(function() {
	});

	$('#searchBtn').bind('click', function() {
		var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
		$("#dataform").submit();
		layer.close();
	});

	function exportDetail() {
		var form = $("#dataform");
		form.attr('action',path + "/reward/red-packet/cash-red-packet-detail-export");
		/*var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });*/
		form.submit();
	//	layer.close();
		form.attr('action',path + "/reward/red-packet/cash-red-packet-detail");
	}
</script>
