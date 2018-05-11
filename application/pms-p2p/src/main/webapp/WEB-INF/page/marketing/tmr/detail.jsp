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
                <li class="active"><b>绩效详情</b></li>
            </ol>

            <div class="form-group">
                    <span style="font-size: 13px"><label>姓名：</label>${tmrInfo.tmrName}</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <span style="font-size: 13px"><label >名单：</label>${tmrInfo.fileName}</span>
            </div>
            <hr style="margin-bottom: 0;">
            <div class="form-group">
                <span style="font-size: 13px"><label>总投资人数：</label>${tmrTotal.numbers}</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <span style="font-size: 13px"><label >投资金额总计：</label><span style="color: red">${tmrTotal.investTotal}</span></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <span style="font-size: 13px"><label >激活返现券总计：</label><span style="color: red">${tmrTotal.activateTotal}</span></span>
            </div>
            <hr style="margin-bottom: 0;"><hr style="margin-bottom: 0;">
            <form id="dataform" class="form-inline" method="post" action="${path}/marketing/tmr/detail">
                <div class="form-group">
                    <label>导入日期：</label>
                    <input id="beginDate"  name="startTime"  type="text" readonly="readonly" maxlength="20" class="form-control input-sm" style="width:163px;"
                           value="${tmrPerformanceForm.startTime}"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd', isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'});"
                           onFocus="WdatePicker({isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'})" />
                    --
                    <input id="endDate" name="endTime" type="text" readonly="readonly" maxlength="20" class="form-control input-sm" style="width:163px;"
                           value="${tmrPerformanceForm.endTime}"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd', isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'});"
                           onFocus="WdatePicker({isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'})" />
                </div>
                <input type="hidden" id="page" name="page">
                <input type="hidden" id="id" name="id" value="${tmrPerformanceForm.id}">
                <shiro:hasPermission name="marketingDetail:search">
                    <button id="searchBtn" type="submit" class="btn btn-primary btn-sm">搜索</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="marketingDetail:import">
                    <button type="button" class="btn btn-success btn-sm" onclick="javascript: exportInvesters(this);">导出</button>
                </shiro:hasPermission>
            </form>
            <hr style="margin-bottom: 0;">
            <div class="panel panel-default">
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>手机号码</th>
                        <th>姓名</th>
                        <th>借款标题</th>
                        <th>借款期限</th>
                        <th>投资日期</th>
                        <th>投资金额<img title="投资的本金" width="15px" src="/static/image/help.png"/></th>
                        <th>激活返现券金额 <img title="该用户的历史激活的返现券总和。" width="15px" src="/static/image/help.png"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${list}" var="v" varStatus="i">
                        <tr>
                            <td>${v.investPhone}</td>
                            <td>${v.investUser}</td>
                            <td>${v.investBid}</td>
                            <td>${v.investDate}</td>
                            <td><fmt:formatDate value="${v.investTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                            <td>${v.investMoney}</td>
                            <td>${v.activateRedBag}</td>
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
    $(function(){
    });

    $("#searchBtn").bind("click", function() {
        var beginDate = $("#beginDate").val();
        var endDate = $("#endDate").val();
        var beginDateSplit = beginDate.split('-');
        var endDateSplit = endDate.split('-');
        var beginDateMonths = parseInt(beginDateSplit[0]) * 12 + parseInt(beginDateSplit[1]);
        var endDateMonths = parseInt(endDateSplit[0]) * 12 + parseInt(endDateSplit[1]);
        if(beginDate > endDate) {
            bootbox.alert({message: "【开始日期不能晚于结束日期】", title: '错误信息'});
            return false;
        }
        if(endDateMonths - beginDateMonths > 6) {
            bootbox.alert({message: "【统计起止月份不能大于6个月】", title: '错误信息'});
            return false;
        }
    });

    function pagination(page) {
        $('#page').val(page);
        $('#dataform').submit();
    }

    function calculatePerformance(pId){
        bootbox.confirm({
            title: '提示',
            size: 'small',
            message: "<span style='font-size: 19px;font-style: inherit'>确认计算？</span>",
            callback: function (result) {
                if (result) {
                    $.post(
                            path + "/marketing/tmr/calculate",
                            {"id" : pId},
                            function (data) {
                                var res = data.codeStatus;
                                if(res == "success"){
                                    bootbox.alert({message: "数据已生产，请查看详情！！！", title: '错误信息'});
                                }else{
                                    bootbox.alert({message: "系统错误，请稍后再试！！！", title: '错误信息'});
                                }
                            }
                    );
                }
            }
        });
    }

    function exportInvesters(obj){

        var cloneForm = $(obj).parents('form.form-inline').clone(true);
        $(cloneForm).attr('action', '${path}/marketing/tmr/export').submit();
        return false;
    }
</script>