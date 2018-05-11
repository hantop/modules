<%@page language="java" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script type="text/javascript" src="/static/component/jquery-form/jquery.form.js"></script>
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script type="text/javascript" src="/static/component/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<div class="container">
	<div class="row">
		<div class="col-md-12">
			<ol class="breadcrumb">
				<li class="active"><a href="${path}/reward/index">奖励管理</a></li>
				<li class="active"><b>体验金管理</b></li>
			</ol>
			<shiro:hasPermission name="experienceGoldEdit:view">
			    <button type="button" class="btn btn-warning btn-sm" onclick="window.location.href='${path}/reward/experienceGold/experience-gold-list'">体验金编辑</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="experienceGold:template">
			    <a type="button" class="btn btn-primary btn-sm" href="/static/template/体验金发放模板_短息来源内容.xlsx">下载表格模板</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="experienceGold:import">
			    <button type="button" class="btn btn-success btn-sm" id="importButton">导入表格</button>
			</shiro:hasPermission>
			<hr>
			<form id="dataform" method="post" class="form-inline" action="${path}/reward/experienceGold">
			    <shiro:hasPermission name="experienceGold:search">
			    <div class="form-group">
                    <label>发放日期：</label>
                    <input type="text" class="form-control" id="startDate" name="startDate" readonly="readonly" maxlength="20" class="form-control input-sm" style="width:163px;" value="${rewardRecordCondition.startDate}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"> --
                    <input type="text" class="form-control" id="endDate" name="endDate" readonly="readonly" maxlength="20" class="form-control input-sm" style="width:163px;" value="${rewardRecordCondition.endDate}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});">
                </div>
                &nbsp;&nbsp;
                <div class="form-group">
                    <label>发送名单：</label>
                    <input type="text" class="form-control"  name="grantName" value="${rewardRecordCondition.grantName}">
                </div>
                &nbsp;&nbsp;
                <div class="form-group">
                    <button id="searchBtn" type="button" class="btn btn-primary btn-sm" >查询</button>
                </div>
                </shiro:hasPermission>
				<input type="hidden" id="page" name="page">
			</form>
			<div class="panel panel-default" style="margin-top: 20px;">
				<table class="table table-striped table-bordered table-condensed">
					<thead>
						<tr class="success">
							<th>发送日期</th>
							<th>发送名单</th>
							<th>操作者</th>
				            <th>总发送金额</th>
                            <th>总发送数</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${list}" var="v" varStatus="vs">
							<tr>
								<td><fmt:formatDate value="${v.grantTime}"
										pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
								<td>${v.grantName}</td>
								<td>${v.operator}</td>
					            <td  style="color:red">${v.grantSum}</td>
                                <td>${v.grantCount}</td>
								<td>
								 <c:choose>
                    					<c:when test="${v.inService != 1}">
								    <shiro:hasPermission name="experienceGoldDetail:view">
								    &nbsp;&nbsp;
								    <a style="text-decoration: underline;"
									href="${path}/reward/experienceGold/detail?grantId=${v.id}&granted=${v.granted==1?true:false}">查看详情</a>
									</shiro:hasPermission>
									</c:when>
                   					<c:otherwise>
								    <font color="#AAAAAA">查看详情</font>&nbsp;&nbsp;&nbsp;
								     </c:otherwise>
                				</c:choose>
									
									<shiro:hasPermission name="experienceGold:grant">
									&nbsp;&nbsp;
									<c:if test="${v.granted==0}">
									<c:choose>
                    					<c:when test="${v.inService != 1}">
										<a style="text-decoration: underline;" identity="${v.id}" role="delete" href="#">发送
										</a>&nbsp;&nbsp;
										</c:when>
                   						 <c:otherwise>
										<font color="#AAAAAA">发送</font>&nbsp;&nbsp;&nbsp;
										   </c:otherwise>
                						</c:choose>
									</c:if>
									<c:if test="${v.granted==1}">
									     <font color="#AAAAAA">已发送</font> 
									</c:if>
									<c:if test="${v.granted==3}">
									     <font color="#AAAAAA">发送</font>&nbsp;&nbsp;&nbsp; 
									</c:if>
									</shiro:hasPermission>
									
									<shiro:hasPermission name="experienceGold:cancel">
									&nbsp;&nbsp;
								    <c:if test="${v.granted==0}">
								    <c:choose>
                    					<c:when test="${v.inService != 1}">
										<a style="text-decoration: underline;" onclick="javascript:cancel(${v.id})"
											href="javascript:void(0)">作废
										</a>
										</c:when>
                   						 <c:otherwise>
									     <font color="#AAAAAA">作废</font>
									     </c:otherwise>
                						</c:choose>
									</c:if>
									<c:if test="${v.granted==1}">
									     <font color="#AAAAAA">作废</font> 
									</c:if>
									<c:if test="${v.granted==3}">
									     <font color="#AAAAAA">已作废</font> 
									</c:if>
									</shiro:hasPermission>
									<c:if test="${v.inService != 0}">
                                         &nbsp;&nbsp;&nbsp;<span style="margin-left:50px;color: red;font-family: 'Arial Black'">后台发放中...</span>
                                    </c:if>
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

<div class="modal fade" id="contentErrorListModal" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">导入失败</h4>
			</div>
			<div class="modal-body">
				<c:forEach items="${contentErrorList}" var="e" varStatus="es">
					<p class='text-danger'>${e}</p>
				</c:forEach>
			</div>
			<div class="modal-footer"></div>
		</div>
	</div>
</div>

<div class="modal fade" id="fileErrorModal" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">导入失败</h4>
			</div>
			<div class="modal-body">
				<p class='text-danger'>${fileError}</p>
			</div>
			<div class="modal-footer"></div>
		</div>
	</div>
</div>

<div class="modal fade" id="grantMessageModal" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
			</div>
			<div class="modal-body">
				<p>${grantMessage}</p>
			</div>
			<div class="modal-footer"></div>
		</div>
	</div>
</div>

<script type="text/javascript">
	$(function() {
		
		$('#searchBtn').bind('click', function() {
		    var staTime = $("#startDate").val();
		    var endTime = $("#endDate").val();
		    if(staTime != '' && endTime != ''){
		        var d1 = new Date(staTime.replace(/-/g,"/"));
		        var d2 = new Date(endTime.replace(/-/g,"/"));
		        
		        var beginDateSplit = staTime.split('-');
		        var endDateSplit = endTime.split('-');
		        var beginDateMonths = parseInt(beginDateSplit[0]) * 12 + parseInt(beginDateSplit[1]);
		        var endDateMonths = parseInt(endDateSplit[0]) * 12 + parseInt(endDateSplit[1]);
		        
		        if (Date.parse(d1) - Date.parse(d2) > 0) {
		        	bootbox.alert({message:"<h3>【结束时间不能早于开始时间】</h3>"});
		        }
		        else if(endDateMonths - beginDateMonths > 6){
		        	bootbox.alert({message:"<h3>【跨度月数不能大于6个月】</h3>"});
		        }
		        else{
					var index = layer.load(0, {
						shade: [0.4,'#fff',false] //0.1透明度的白色背景
					});
					$("#dataform").submit();
					layer.close();
		        }
		    }
		    else{
		    	bootbox.alert({message:"<h3>【查询日期不能为空】</h3>"});
		    }
		});
		
		$('a[role="delete"]').on('click', function () {
			var identity = $(this).attr('identity');
			bootbox.confirm({
				title: '提示',
				size: 'small',
				message: "确认发放?",
				callback: function (result) {
					if (result) {
						var data = {id : identity};
						var page = "${paginator.pageNum}";
						$.ajax(
								'${path}/reward/experienceGold/grant',
								{
									contentType: 'application/json; charset=utf-8',
									data: JSON.stringify(data),
									dataType: "json",
									type: "POST",
									beforeSend: function (xhr) {
										var index = layer.load(0, {
											shade: [0.4,'#fff',false] //0.1透明度的白色背景
										});
									}
								}
						)
								.success(function (result) {
									bootbox.alert({
										buttons: {
											ok: {
												label: 'OK'
											}
										},
										title: '提示',
										size: 'small',
										message: result.message,
										callback: function () {
											pagination('${paginator.pageNum}')
										}
									});
								})
								.error(function (jqXHR, textStatus, errorThrown) {
									console.info(jqXHR)
									bootbox.alert({
										buttons: {
											ok: {
												label: 'OK'
											}
										},
										title: '提示',
										size: 'small',
										message: jqXHR.responseJSON.message
									});
								})
								.complete(function (jqXHR, textStatus) {
									layer.closeAll();
								});
					}
				}
			})
			return false;
		});


		if ("${contentErrorList}" != "") {
			$('#contentErrorListModal').modal('show');
		}
		if ("${fileError}" != "") {
			$('#fileErrorModal').modal('show');
		}
		if ("${message}" != "") {
			bootbox.alert({
				title : '提示',
				size : 'small',
				message : "${message}",
				callback : function(result) {
				}
			});
		}

		$('#importButton').click(function() {
			var file = $('<input type="file" name="file"/>');
			file.trigger('click');
			var operator = $('<input type="text" name="operator" value="' + $('#currentUserName').text() + '"/>')
			var form = $("<form method='post' enctype='multipart/form-data'></form>");
			$(form).append(file).append(operator);
			$(file).on('change',function() {
				var filepath = $(file).val();
				var extStart = filepath.lastIndexOf(".");
				var ext = filepath.substring(extStart, filepath.length)
						.toUpperCase();
				if (ext != ".XLS" && ext != ".XLSX") {
					$(file).val("");
					bootbox.alert({message:"<h3>【文件限于xls,xlsx格式】</h3>"});
					return false;
				}
				bootbox.confirm({
					title: '提示',
					size: 'small',
					message: "确认导入吗？",
					callback: function (result) {
						if (result) {
							form.ajaxSubmit({
								url: '${path}/reward/experienceGold/import',
								beforeSubmit: function (arr, $form, options) {
									var index = layer.load(0, {
										shade: [0.4,'#fff',false] //0.1透明度的白色背景
									});
								},
								success: function (data) {
									if (data.length > 0) {
										var error = "";
										for (var i = 0; i < data.length; i++) {
											error += "<p class='text-danger'>" + data[i] + "</p>"
										}
										bootbox.alert({message: error, title: '错误信息'});
									} else {
										bootbox.confirm({
											title: '提示',
											size: 'small',
											message: "导入成功",
											callback: function (result) {
												pagination('${paginator.pageNum}')
											}
										});
									}
								},
								complete: function (xhr, status) {
									delete file;
									delete operator;
									delete form;
									layer.closeAll();
								}
							});
						}
					}
				});
			});

		});

		$("#importInput").change(
				function() {
					var index = layer.load(0, {
						shade: [0.4,'#fff',false] //0.1透明度的白色背景
					});
					
					var filepath = $("#importInput").val();
					var extStart = filepath.lastIndexOf(".");
					var ext = filepath.substring(extStart, filepath.length)
							.toUpperCase();
					if (ext != ".XLS" && ext != ".XLSX") {
						$("#importInput").val("");
						bootbox.alert({message:"<h3>【文件限于xls,xlsx格式】</h3>"});
						layer.closeAll();
						return false;
					}
					var importFileName = filepath.substring(filepath
							.lastIndexOf("\\") + 1, filepath.lastIndexOf("."));
					$('#importFileName').val(importFileName);
					$('#operator').val($('#currentUserName').text());
					$('#importForm').submit();
					return true;
				});
	});
	
	function cancel(id){
		bootbox.confirm({
			title : '提示',
			size : 'small',
			message : "确认作废？",
			callback : function(result) {
				if(result){
					  <%--window.location.href="${path}/reward/experienceGold/cancel?id="+id;--%>
					var data = {id:id};
					$.ajax(
							'${path}/reward/experienceGold/cancel',
							{
								contentType: 'application/json; charset=utf-8',
								data: JSON.stringify(data),
								dataType: "json",
								type: "POST",
								beforeSend: function (xhr) {
									var index = layer.load(0, {
										shade: [0.4,'#fff',false] //0.1透明度的白色背景
									});
								}
							}
					)
							.success(function (result) {
								bootbox.alert({
									buttons: {
										ok: {
											label: 'OK'
										}
									},
									title: '提示',
									size: 'small',
									message: result.message,
									callback: function () {
										pagination('${paginator.pageNum}')
									}
								});
							})
							.error(function (jqXHR, textStatus, errorThrown) {
								console.info(jqXHR)
								bootbox.alert({
									buttons: {
										ok: {
											label: 'OK'
										}
									},
									title: '提示',
									size: 'small',
									message: jqXHR.responseJSON.message
								});
							})
							.complete(function (jqXHR, textStatus) {
								layer.closeAll();
							});


				}
			}
		});
	}
	

</script>
