<%@page language="java" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<script type="text/javascript" src="/static/component/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/static/component/datepicker/calendar.js"></script>



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
				<li class="active"><a href="${path}/cs/index">风控管理</a></li>
				<li class="active"><b>进单审核</b></li>
			</ol>
			<div>
				<form class="form-inline form-search" role="form" id="dataform"  action="${path}/riskcontrol/consumerBidList">
					<input type="hidden" value="${userId}" name="userId">
					<input type="hidden" id="page" name="page">
					<input type="hidden" id="fromPage" name="fromPage" value="true">


					<div class="form-group">
						<label class="control-label">用户手机号：</label>
					</div>
					<div class="form-group">
						<input class="form-control" name="phone" maxlength="30" value="${consumerBid.phone}"/>
					</div>

                    &nbsp;&nbsp;
					<div class="form-group">
						<label class="control-label">用户姓名：</label>
					</div>
					<div class="form-group">
						<input class="form-control" name="userName" maxlength="30" value="${consumerBid.userName}"/>
					</div>

					&nbsp;&nbsp;
					<div class="form-group">
						<label>审核状态：</label>
					</div>
					<div class="form-group">
						<select class="form-control" name="auditStatus" >
							<option value=""  >全部</option>
							<option value="1" <c:if test="${consumerBid.auditStatus=='1'}">selected="selected"</c:if> >通过</option>
							<option value="2" <c:if test="${consumerBid.auditStatus=='2'}">selected="selected"</c:if> >不通过</option>
						</select>
					</div>
					<button type="submit" class="btn btn-primary">查询</button>
				</form>
			</div>
			<br>
			<div class="table-responsive">
				<table id="datatable" class="table table-striped table-bordered table-condensed">
					<thead>
					<tr class="success">
						<th>用户账户</th>
						<th>用户手机号</th>
						<th>用户姓名</th>
						<th>借款金额</th>
						<th>借款期限</th>
						<th>还款方式</th>
						<th>申请时间</th>
						<th>审核状态</th>
						<th>操作</th>
					</tr>
					</thead>
					<c:forEach items="${consumerBidList}" var="consumerBid" varStatus="vs">
						<tr >
							<td>${consumerBid.accountNo}</td>
							<td>${consumerBid.phone}</td>
							<td>${consumerBid.userName}</td>
							<td>${consumerBid.loanAmount}</td>
							<td>${consumerBid.cycle}${consumerBid.cycleType=='m'?'个月':'天'}</td>
							<td>
								<c:choose>
									<c:when test="${consumerBid.paybackWay=='DEBX'}">等额本息</c:when>
									<c:when test="${consumerBid.paybackWay=='MYFX'}">每月付息,到期还本</c:when>
									<c:when test="${consumerBid.paybackWay=='YCFQ'}">本息到期一次付清</c:when>
									<c:when test="${consumerBid.paybackWay=='DEBJ'}">等额本金</c:when>
									<c:otherwise>未知状态</c:otherwise>
								</c:choose>
							</td>
							<td><fmt:formatDate value="${consumerBid.applyTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td>
								<c:if test="${consumerBid.auditStatus==0}">
								<span style="color: blue">未审核</span>
								</c:if>
								<c:if test="${consumerBid.auditStatus==1}">
									<span style="color: green">通过</span>
								</c:if>
								<c:if test="${consumerBid.auditStatus==2}">
									<span style="color: red">不通过</span>
								</c:if>
							</td>
							<td><a href="${path}/riskcontrol/baseInfo?id=${consumerBid.id}">查看</a></td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
		</div>

		<tiles:insertDefinition name="paginator"/>

	</div>
</div>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>

<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>