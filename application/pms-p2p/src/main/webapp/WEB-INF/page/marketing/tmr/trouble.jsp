<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!--My 97 date -->
<script type="text/javascript" src="/static/component/datepicker/calendar.js"></script>
<link rel="stylesheet" href="/static/component/HoldOn/HoldOn.min.css">
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script type="text/javascript" src="/static/component/HoldOn/HoldOn.min.js"></script>
<script type="text/javascript" src="/static/component/jquery-form/jquery.form.js"></script>
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script type="text/javascript" src="/static/component/datepicker/WdatePicker.js"></script>
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
                <li class="active"><b>电话绩效详情</b> </li>
                <li class="active"><b>异常名单</b></li>
            </ol>
            <hr style="margin-bottom: 0;">
            <form id="dataform" class="form-inline" method="post" action="${path}/marketing/tmr/trouble">
                <div class="form-group">
                    <label>姓名：</label><span style="font-weight: bolder;color: #8B3E2F">${info.tmrName}</span>
                </div>&nbsp;&nbsp;&nbsp;&nbsp;
                <div class="form-group">
                    <label>名单名称：</label><span style="font-weight: bolder;color: #8B3E2F">${info.fileName}</span>
                </div>
                <input type="hidden" id="page" name="page">
                <input type="hidden" id="id" name="id" value="${info.id}">
            </form>
            <hr style="margin-bottom: 0;">
            <hr style="margin-bottom: 0;">
            <div class="panel panel-default">
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>手机号码</th>
                        <th>异常原因</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${list}" var="v" varStatus="i">
                        <tr>
                            <td>${v.phoneNumber}</td>
                            <td>${v.msg}</td>
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
        $('#dataform').submit();
    }
</script>