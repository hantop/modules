<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<link rel="stylesheet" href="/static/component/zTree_v3/css/zTreeStyle/zTreeStyle.css" type="text/css">
<link rel="stylesheet" href="/static/component/bootstrap/css/bootstrap.min.css" type="text/css">
<script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.exedit-3.5.js"></script>
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<link rel="stylesheet" href="/static/theme/ztree-diy.css" type="text/css">

<div class="container  content-top">
	<div class="row">
		<div class="col-md-12">
			<ol class="breadcrumb">
				<li class="active"><a href="${path}/system/index">系统管理</a></li>
				<li class="active"><b>组织管理</b></li>
			</ol>
			<hr/>
			<div class="col-md-3">
				<div class="panel panel-default">
					<div class="panel-body">
						<ul id="tree" class="ztree" style="width:230px; overflow:auto;"></ul>
					</div>
				</div>
			</div>
			<div class="col-md-9" id="maintain">
				<div class="panel panel-default">
					<div class="panel-body">
						<form id="dataform"  method="post" action="${path}/system/pmsgroup">
						<div class="form-group">
							<table class="table table-striped table-bordered table-condensed">
								<thead>
								<tr class="success">
									<th>ID</th>
									<th>部门名称</th>
									<th>排序</th>
									<th>操作</th>
								</tr>
								</thead>
								<tbody>
								<c:forEach items="${pmsGroupList}" var="v" varStatus="i">
									<tr>
										<td>${v.id}</td>
										<td>${v.name}</td>
										<td>${v.orderid}</td>
										<td>
											<shiro:hasPermission name="organization:delete">
												<a href="#" onclick="deptDel(${v.id})">删除</a>
											</shiro:hasPermission>
											<shiro:hasPermission name="organizationUsers:view">
												<a href="${path}/system/pmsgroup/users?id=${v.id}">成员查询</a>
											</shiro:hasPermission>
										</td>
									</tr>
								</c:forEach>
								<input type="hidden" id="page" name="page">
								</tbody>
							</table>
						</div>
					</form>
					</div>
				</div>
				<tiles:insertDefinition name="paginator"/>
			</div>
			<jsp:include page="tree.jsp"></jsp:include>
		</div>
	</div>
</div>
<script>
		function pagination(page) {
			$('#page').val(page);
			$('#dataform').submit();
		}
</script>