<%@page language="java" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div class="container">
	<div class="row">
		<div class="col-md-12">
			<ol class="breadcrumb">
				<li class="active"><a href="${path}/reward/index">奖励管理</a></li>
				<li class="active"><a href="${path}/reward/experienceGold">体验金管理</a></li>
				<li class="active"><b>发送详情</b></li>
			</ol>
            <c:if test="${fn:length(statisticsList) > 0}">
                <div>
                    <table class="table">
                        <c:set value="0" var="totalCount" />
                        <c:set value="0" var="totalSum" />
                        <c:set value="0" var="totalIncome" />
                        <c:forEach items="${statisticsList}" var="item">
                            <c:set value="${totalCount + item.grantCount}" var="totalCount" />
                            <c:set value="${totalSum + item.grantSum}" var="totalSum" />
                            <c:set value="${totalIncome + item.incomeSum}" var="totalIncome" />
                        </c:forEach>
                        <tr>
                            <td class="text-danger">总计</td>
                            <td class="text-danger">发送数：${totalCount}</td>
                            <td class="text-danger">发送金额（元）：${totalSum}</td>
                            <td class="text-danger">到期利息（元）：<fmt:formatNumber pattern=".00" value="${totalIncome}" /></td>
                        </tr>
                        <c:forEach var="item" items="${statisticsList}">
                        <tr>
                            <td>体验金：${item.activityCode}</td>
                            <td>发送数：${item.grantCount}</td>
                            <td>发送金额（元）：${item.grantSum}</td>
                            <td>到期利息（元）：<fmt:formatNumber pattern=".00" value="${item.incomeSum}" /></td>
                        </tr>
                        </c:forEach>
                    </table>
                    <hr>
                </div>
            </c:if>
			<form id="dataform" method="post" class="form-inline" action="${path}/reward/experienceGold/detail">
				<shiro:hasPermission name="experienceGoldDetail:search">
				<div class="form-group">
				    <label>体验金类型：</label>
					<select name="goldId" class="form-control" id="goldId">
						<option value="">全部</option>
						<c:forEach items="${experienceList}" var="gold">
							<option value="${gold.id}" ${userExperience.goldId == gold.id ? 'selected="selected"' : ''}>
									${gold.activityCode}
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
				    <label>发送状态：</label>
					<select class="form-control"
							name="grantStatus" id="grantStatus">
						<option value="">全部</option>
						<option value="0"
								<c:if test="${userExperience.grantStatus == 0 }">selected="selected"</c:if>>未发送</option>
						<option value="1"
								<c:if test="${userExperience.grantStatus == 1 }">selected="selected"</c:if>>成功</option>
						<%-- <option value="2" <c:if test="${userExperience.grantStatus == 2 }">selected="selected"</c:if>>失败</option>--%>
						<option value="3" <c:if test="${userExperience.grantStatus == 3 }">selected="selected"</c:if>>已作废</option>
					</select>
				</div>
				<button type="button" class="btn btn-primary btn-sm" id="searchBtn">搜索</button>
				</shiro:hasPermission>
				
				<shiro:hasPermission name="experienceGoldDetail:export">
				<button type="button" class="btn btn-success btn-sm" onclick="javascript: exportDetail();">导出</button>
				</shiro:hasPermission>
				<button type="button" class="btn btn-warning btn-sm" onclick="window.location.href='${path}/reward/experienceGold'">返回</button>
				
				<input type="hidden" id="grantId" name="grantId" value="${userExperience.grantId}"/>
				<input type="hidden" id="page" name="page">
				<input type="hidden" name="granted" value="${granted}">
			</form>
			<hr>
		</div>
	</div>
	<div class="row">
		<div class="col-md-6">
			<div class="panel panel-default">
				<table class="table table-striped table-bordered table-condensed">
					<thead>
						<tr class="success">
							<th>用户手机号</th>
							<th>体验金类型</th>
							<th>发送状态</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${list}" var="v" varStatus="vs">
							<tr>
								<td>${v.phone}</td>
								<td>${v.activityCode}</td>
								<td><c:if test="${v.grantStatus==0}">未发送</c:if> <c:if
										test="${v.grantStatus==1}">已成功</c:if> <c:if
										test="${v.grantStatus==2}">发送失败</c:if>
										<c:if test="${v.grantStatus==3}">已作废</c:if>
								</td>
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
	$('body').css({'overflow-y':'auto'});

	$('#searchBtn').bind('click', function() {
		$("#dataform").submit();
	});

	function exportDetail() {
		var form = $("#dataform");
		form.attr('action',path + "/reward/experienceGold/detailExport");
		form.submit();
		form.attr('action',path + "/reward/experienceGold/detail");
	}
</script>
