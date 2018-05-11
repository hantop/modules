<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script type="text/javascript" src="/static/component/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/static/component/datepicker/calendar.js"></script>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
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
				<li class="active"><a>客服管理</a></li>
				<li class="active"><b>日志查询</b></li>
			</ol>
			<jsp:include page="common.jsp"/>
			<div>
				<c:if test="${getLoginInfo == 1}">
				<table id="loginInfoTable" class="table table-striped table-bordered table-condensed">
					<thead>
					<tr class="success">
						<th>手机号</th>
						<th>姓名</th>
						<th>身份证号</th>
						<th>时间</th>
						<th>行为</th>
						<th>ip</th>
						<th>操作结果</th>
					</tr>
					</thead>
					<tbody>
					<c:forEach items="${logInfoList}" var="v" varStatus="vs">
						<tr>
							<td>${v.phoneNum}</td>
							<td>${v.name}</td>
							<td>${v.idcard}</td>
							<td><fmt:formatDate value="${v.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td>
								<c:if test="${v.conduct == 0}">
									登录
								</c:if>
								<c:if test="${v.conduct == 1}">
									退出登录
								</c:if>
							</td>
							<td>${v.ip}</td>
							<td>
								<c:if test="${v.status == 0}">
									失败
								</c:if>
								<c:if test="${v.status == 1}">
									成功
								</c:if>
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
				</c:if>
				<c:if test="${getLoginInfo == 2}">
				<table id="fundInfoTable" class="table table-striped table-bordered table-condensed">
					<thead>
					<tr class="success">
						<th>手机号</th>
						<th>姓名</th>
						<th>身份证号</th>
						<th>时间</th>
						<th>行为</th>
						<%--<th>金额</th>--%>
						<th>操作结果</th>
					</tr>
					</thead>
					<tbody>
					<c:forEach items="${logInfoList}" var="v" varStatus="vs">
						<tr>
							<td>${v.phoneNum}</td>
							<td>${v.name}</td>
							<td>${v.idcard}</td>
							<td><fmt:formatDate value="${v.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td>
								<c:if test="${v.conduct == 2}">
									充值
								</c:if>
								<c:if test="${v.conduct == 3}">
									提现
								</c:if>
								<c:if test="${v.conduct == 4}">
									投资
								</c:if>
								<c:if test="${v.conduct == 5}">
									借款
								</c:if>
								<c:if test="${v.conduct == 6}">
									债转申请
								</c:if>
								<c:if test="${v.conduct == 7}">
									购买债权
								</c:if>
							</td>
							<%--<td>${v.price}</td>--%>
							<td>
								<c:if test="${v.status == 0}">
									失败
								</c:if>
								<c:if test="${v.status == 1}">
									成功
								</c:if>
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
				</c:if>
				<c:if test="${getLoginInfo == 3}">
				<table id="otherInfoTable" class="table table-striped table-bordered table-condensed">
					<thead>
					<tr class="success">
						<th>手机号</th>
						<th>姓名</th>
						<th>身份证号</th>
						<th>时间</th>
						<th>行为</th>
						<th>操作结果</th>
						<th>备注</th>
					</tr>
					</thead>
					<tbody>
					<c:forEach items="${logInfoList}" var="v" varStatus="vs">
						<tr>
							<td>${v.phoneNum}</td>
							<td>${v.name}</td>
							<td>${v.idcard}</td>
							<td><fmt:formatDate value="${v.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td>
								<c:if test="${v.conduct == 8}">
									绑定银行卡
								</c:if>
								<c:if test="${v.conduct == 9}">
									解绑银行卡
								</c:if>
								<c:if test="${v.conduct == 10}">
									修改登录密码
								</c:if>
							</td>
							<td>
								<c:if test="${v.status == 0}">
									失败
								</c:if>
								<c:if test="${v.status == 1}">
									成功
								</c:if>
							</td>
							<td>${v.remarks}</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
				</c:if>
				<tiles:insertDefinition name="paginator"/>
			</div>
		</div>
	</div>
</div>
<script>
	/**
	 * 重置
	 */
	$('#resetBtn').bind('click', function () {
		window.location.href = "${path}/cs/logInfo";
	});

	/**
	 * 查询
	 */
	$('#searchBtn').bind('click', function () {
		var phoneNum = $("#phoneNum").val();
		var name = $("#name").val();
		var idCard = $("#idCard").val();
		var startTime = $("#startTime").val();
		var endTime = $("#endTime").val();
		if ((phoneNum == null || phoneNum == '') && (name == null || name == '') && (idCard == null || idCard == '')
				&& (startTime == null || startTime == '') && (endTime == null || endTime == '')) {
			bootbox.alert({message: "<h3>请至少填写一项</h3>"});
		}else {
			var index = layer.load(0, {
				shade: [0.4,'#fff',false] //0.1透明度的白色背景
			});
			$("#parent_form").submit();
			layer.close();
		}
	});

	/*tab页请求*/
	function getDetailInfo(type){
		if(type > 0){
			$("#conductType").val(type);
		}
		var index = layer.load(0, {
			shade: [0.4,'#fff',false] //0.1透明度的白色背景
		});
		$("#parent_form").submit();
		layer.close();
	}

	function pagination(page) {
		$('#page').val(page);
		var index = layer.load(0, {
			shade: [0.4,'#fff',false] //0.1透明度的白色背景
		});
		$("#parent_form").submit();
		layer.close();
	}
</script>
