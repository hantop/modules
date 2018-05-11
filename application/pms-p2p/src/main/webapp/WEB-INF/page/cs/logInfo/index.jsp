<%@page language="java" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
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
                <li class="active"><a href="${path}/cs/index">客服管理</a></li>
                <li class="active"><b>日志查询</b></li>
            </ol>
        </div>
        <jsp:include page="common.jsp"/>
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
</script>

