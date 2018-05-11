<%--
  Created by IntelliJ IDEA.
  User: bogle
  Date: 2015/11/10
  Time: 15:28
  To change this template use File | Settings | File Templates.
--%>
<%@page language="java" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script type="text/javascript" src="/static/component/jquery-form/jquery.form.js"></script>
<script type="text/javascript" src="/static/component/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/reward/index">奖励管理</a></li>
                <li class="active"><b>返现券管理</b></li>
            </ol>
            <form:form class="form-inline" >
                <shiro:hasPermission name="cashBackVoucherEdit:view">
                <div class="form-group">
                    <a class="btn btn-warning btn-sm"
                       href="${path}/reward/red-packet/back-voucher-type-list"
                       role="button">返现券编辑</a>
                </div>
                </shiro:hasPermission>
                
                <shiro:hasPermission name="cashBackVoucher:template">
                <div class="form-group">
                    <a class="btn btn-primary btn-sm"
                       href="/static/template/返现券发放模板_短息来源内容.xlsx"
                       role="button">下载表格模板</a>
                </div>
                </shiro:hasPermission>
                
                <shiro:hasPermission name="cashBackVoucher:import">
                <div class="form-group">
                    <input class="btn btn-success btn-sm"
                           id="importButton"
                           role="button" value="导入表格" type="button">
                </div>
                </shiro:hasPermission>
                <input type="hidden" name="operator" value="<shiro:principal/>" id="operator">
            </form:form>
            <hr>
            <form id="dataform" method="post" class="form-inline" action="${path}/reward/red-packet/back-voucher-list">
			    <shiro:hasPermission name="cashBackVoucher:search">
			    <div class="form-group">
                    <label>激活日期：</label>
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
            <table class="table table-striped table-bordered table-condensed table-hover" style="margin-top: 20px;">
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
                <c:choose>
                    <c:when test="${list== null || fn:length(list) == 0}">
                        <%-- <tr>
                             <td colspan="4">无数据</td>
                         </tr>--%>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="item" items="${list}">
                            <tr>
                                <td><fmt:formatDate value="${item.grantTime}"
										pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                                <td>${item.grantName}</td>
                                <td>${item.operator}</td>
                                <td style="color:red">${item.grantSum}</td>
                                <td>${item.grantCount}</td>
                                <td>
                                    <shiro:hasPermission name="cashBackVoucherDetail:view">
                                    &nbsp;&nbsp;
                                    <a style="text-decoration: underline;"
                                       href="${path}/reward/red-packet/back-voucher-detial-list?grantId=${item.id}&granted=${item.granted==1?true:false}"
                                       role="button"
                                            >查看详情</a>
                                    </shiro:hasPermission>         
                                            
                                    <shiro:hasPermission name="cashBackVoucher:grant">       
                                    &nbsp;&nbsp;
                                    <c:if test="${item.granted == 0}">
                                        <c:if test="${running == 0}">
                                            <a style="text-decoration: underline;"
                                               href="#" onclick="javascript:send(${item.id})"
                                               role="button">发送</a>&nbsp;&nbsp;&nbsp;
                                        </c:if>
                                        <c:if test="${running != 0}">
                                            <font color="#AAAAAA">发送</font>
                                        </c:if>
                                    </c:if>
                                    <c:if test="${item.granted==1}">
										<font color="#AAAAAA">已发送</font>
									</c:if>
                                    <c:if test="${item.granted==3}">
										<font color="#AAAAAA">发送</font>&nbsp;&nbsp;&nbsp;
									</c:if>
									</shiro:hasPermission> 
									
									<shiro:hasPermission name="cashBackVoucher:cancel">
									&nbsp;&nbsp;
							        <c:if test="${item.granted == 0}">
                                        <c:if test="${running == 0}">
                                            <a style="text-decoration: underline;" onclick="javascript:cancel(${item.id})"
                                               href="javascript:void(0)">作废
                                            </a>
                                        </c:if>
                                        <c:if test="${running != 0}">
                                            <font color="#AAAAAA">作废</font>
                                        </c:if>
									</c:if>
									<c:if test="${item.granted==1}">
									     <font color="#AAAAAA">作废</font> 
									</c:if>
									<c:if test="${item.granted==3}">
									     <font color="#AAAAAA">已作废</font> 
									</c:if>
									</shiro:hasPermission>
                                     <c:if test="${item.inService != 0}">
                                         &nbsp;&nbsp;&nbsp;<span style="margin-left:50px;color: red;font-family: 'Arial Black'">后台发放中...</span>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
            <tiles:insertDefinition name="paginator"/>
        </div>
    </div>
</div>

<script type="application/javascript">
    (function ($) {
    	
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
    	
        $('#importButton').click(function () {
            var file = $('<input type="file" name="file"/>');
            file.trigger('click');
            var operator = $('<input type="text" name="operator" value="' + $('#operator').val() + '"/>')
            var form = $("<form method='post' enctype='multipart/form-data'></form>");
            $(form).append(file).append(operator);
            $(file).on('change', function () {
                bootbox.confirm({
                    title: '提示',
                    size: 'small',
                    message: "确认导入吗？",
                    callback: function (result) {
                        if (result) {
                            form.ajaxSubmit({
                                url: '${path}/reward/red-packet/import-back-voucher',
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

		if ("${message}" != "") {
            bootbox.alert({
                title: '提示',
                size: 'small',
                message: "${message}",
                callback: function (result) {
                }
            });
		}
    })(jQuery);
    
	function cancel(id){
		bootbox.confirm({
			title : '提示',
			size : 'small',
			message : "确认作废？",
			callback : function(result) {
				if(result){
					  <%--window.location.href="${path}/reward/red-packet/back-voucher-cancel?id="+id;--%>
                    var data = {id:id};
                    $.ajax(
                            '${path}/reward/red-packet/back-voucher-cancel',
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

    function send(id){
        bootbox.confirm({
            title : '提示',
            size : 'small',
            message : "确认发送？",
            callback : function(result) {
                if(result){
                	/* var index = layer.load(0, {
                        shade: [0.4,'#fff',false] //0.1透明度的白色背景 back-voucher-grant
                    }); */
                    <%--window.location.href="${path}/reward/red-packet/back-voucher-grant?id="+id;--%>
                    var data = {id:id};
                    $.ajax(
                            '${path}/reward/red-packet/async-grant',
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
                    ).success(function (result) {
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
                            }).error(function (jqXHR, textStatus, errorThrown) {
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
        return false;
    }
</script>
