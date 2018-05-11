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
            </ol>
                <shiro:hasPermission name="activity:create">
                <div class="form-group">
                    <a class="btn btn-info" href="${path}/marketing/activity/edit"
                       role="button">添加活动</a>
                </div><br/>
               </shiro:hasPermission>
            <form id="dataform" class="form-inline" method="post" action="${path}/marketing/activity">
                <input type="hidden" id="page" name="page">
            </form>
            <hr style="margin-bottom: 0;">
            <div class="panel panel-default">
                <div class="panel-heading">活动列表</div>
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>活动编码</th>
                        <th>活动名称</th>
                        <th>活动时间</th>
                        <th>活动状态</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    	<c:forEach var="item" items="${list}">
                            <tr>
                                <td>${item.code}</td>
                                <td>${item.name}</td>
                                <td><fmt:formatDate value="${item.timeStart}"
										pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
									&nbsp;&nbsp;--&nbsp;&nbsp; <fmt:formatDate value="${item.timeEnd}"
										pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
									</td>
                                <td>${item.status}</td>
                                <td>
                                   <shiro:hasPermission name="activity:edit">
                                	<c:choose>
                                		<c:when test="${item.status == '活动中' || item.status == '未开始'}">
                                		<a class="glyphicon glyphicon-pencil" href="${path}/marketing/activity/edit?id=${item.id}" role="button">编辑</a>
                                        &nbsp;
                                		</c:when>
                                		<c:otherwise>
                                        <font color="#AAAAAA">&nbsp;&nbsp;&nbsp;编辑&nbsp;&nbsp;</font>
                                   		</c:otherwise>
                                   	</c:choose>
                                   </shiro:hasPermission>
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
    function pagination(page) {
        $('#page').val(page);
        var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $("#dataform").submit();
        layer.close();
    }
</script>