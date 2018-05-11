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
                <li class="active"><a href="${path}/finance/index">加息券成本详情</a></li>
                <li class="active"><b>加息券</b></li>
            </ol>
            <form id="dataform" class="form-inline" method="post" action="${path}/finance/coupon/detail">
                <div class="form-group">
                    手机号：
                    <input id="phoneNum" name="phoneNum" type="text" class="form-control input-sm"
                           value="${datailForm.phoneNum}"/>
                </div>&nbsp;&nbsp;
                <input type="hidden" id="startDate" name="startDate" value="${returncachRedpacketDetailForm.startDate}">
                <input type="hidden" id="endDate" name="endDate" value="${returncachRedpacketDetailForm.endDate}">
                <input type="hidden" id="redpacketId" name="redpacketId" value="${returncachRedpacketDetailForm.redpacketId}">
                <input type="hidden" id="systemgrantFlag" name="systemgrantFlag" value="${returncachRedpacketDetailForm.systemgrantFlag}">
                <input type="hidden" id="page" name="page">
                <button id="searchBtn" type="submit" class="btn btn-primary btn-sm">搜索</button>
                <shiro:hasPermission name="returncashDetails:export">
                    <button type="button" class="btn btn-success btn-sm" onclick="javascript: exportUserUsedReturncashs(this);">导出</button>
                </shiro:hasPermission>
            </form>
            <hr style="margin-bottom: 0;">
            <div class="panel panel-default">
                <div class="panel-heading">加息券列表</div>
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>激活时间</th>
                        <th>用户手机号</th>
                        <th>加息劵类型</th>
                        <th>预计加息</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${list}" var="v" varStatus="i">
                        <tr>
                            <td>
                                <fmt:formatDate value="${v.activityTime}" pattern="yyyy-MM-dd HH:mm:ss" />
                            </td>
                            <td>${v.phoneNum}</td>
                            <td>${v.redMoney}</td>
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

    function exportUserUsedReturncashs(obj){
        /*var form = document.forms[0];
        form.action = path + "/finance/returncash/detail/export";
        form.submit();
        form.action = path + "/finance/returncash/detail"*/

        var cloneForm = $(obj).parents('form.form-inline').clone(true);
        $(cloneForm).attr('action', '${path}/finance/coupon/detail/export').submit();
        return false;
    }
</script>