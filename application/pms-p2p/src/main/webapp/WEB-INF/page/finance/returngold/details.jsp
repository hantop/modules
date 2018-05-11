<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!--My 97 date -->
<script type="text/javascript" src="/static/component/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/static/component/datepicker/calendar.js"></script>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/finance/index">财务管理</a></li>
                <li class="active"><a href="${path}/finance/returngold">体验金</a></li>
                <li class="active"><b>体验金</b></li>
            </ol>
            <form id="dataform" class="form-inline" method="post" action="${path}/finance/returngold/details">
                <div class="form-group">
                    用户手机号码：
                    <input id="telphone" name="telphone" type="text" value="${telphone}" class="form-control input-sm"/>
                </div>&nbsp;&nbsp;

                <input type="hidden" id="page" name="page">
                <input type="hidden" id="gid" name="gid" value="${gid}">
                <input type="hidden" id="startTime" name="startTime" value="${startTime}">
                <input type="hidden" id="endTime" name="endTime" value="${endTime}">
                <shiro:hasPermission name="returnGoldDetails:search">
                    <button id="searchBtn" type="submit" class="btn btn-primary btn-sm">搜索</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="returnGoldDetails:export">
                    <button type="button" class="btn btn-success btn-sm" onclick="javascript: exportGoldDetails(this);">导出</button>
                </shiro:hasPermission>
            </form>
            <hr style="margin-bottom: 0;">
            <div class="panel panel-default">
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>产生日期</th>
                        <th>用户手机号</th>
                        <th>体验金利息</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${list}" var="v" varStatus="i">
                        <tr>
                            <td>${v.earningsDate}</td>
                            <td>${v.telphone}</td>
                            <td>${v.userspay}</td>
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

    $().ready(function(){

    });

    function exportGoldDetails(obj){
        /*var form = document.forms[0];
        form.action = path + "/finance/returngold/exportDetails";
        form.submit();
        form.action = path + "/finance/returngold//details?gid=${gid}&startTime=${startTime}&endTime=${endTime}"*/

        var cloneForm = $(obj).parents('form.form-inline').clone(true);
        $(cloneForm).attr('action', '${path}/finance/returngold/exportDetails').submit();
        return false;
    }
</script>