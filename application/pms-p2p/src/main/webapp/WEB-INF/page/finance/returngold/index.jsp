<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!--My 97 date -->
<script type="text/javascript" src="/static/component/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/static/component/datepicker/calendar.js"></script>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<style type="text/css">
    .red_font {
        color: red;
    }
</style>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/finance/index">财务管理</a></li>
                <li class="active"><b>体验金</b></li>
            </ol>
            <form id="dataform" class="form-inline" method="post" action="${path}/finance/returngold">
                <div class="form-group">
                    <label>日期：</label>
                    <input id="beginDate"  name="startTime"  type="text" readonly="readonly" maxlength="20" class="form-control input-sm" style="width:163px;"
                           value="${returnGoldForm.startTime}"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd', isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'});"
                           onFocus="WdatePicker({isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'})" />
                    --
                    <input id="endDate" name="endTime" type="text" readonly="readonly" maxlength="20" class="form-control input-sm" style="width:163px;"
                           value="${returnGoldForm.endTime}"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd', isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'});"
                           onFocus="WdatePicker({isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'})" />
                </div>&nbsp;&nbsp;

                <div class="form-group">
                    体验金代码：
                    <input id="goldCode" name="goldCode" value="${returnGoldForm.goldCode}" type="text" class="form-control input-sm"/>
                </div>&nbsp;&nbsp;

                <input type="hidden" id="page" name="page">
                <shiro:hasPermission name="returnGold:search">
                    <button id="searchBtn" type="button" class="btn btn-primary btn-sm">搜索</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="returnGold:export">
                    <button type="button" class="btn btn-success btn-sm" onclick="javascript: exportReturnGold(this);">导出</button>
                </shiro:hasPermission>
            </form>
            <hr style="margin-bottom: 0;">
            <h4 class="red_font">总成本: ${total}</h4>
            <div class="panel panel-default">
                <div class="panel-heading">体验金列表</div>
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>体验金代码</th>
                        <th>体验金金额</th>
                        <th>体验金有效期</th>
                        <th>年化收益</th>
                        <th>到账人数</th>
                        <th>产生成本</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${list}" var="v" varStatus="i">
                        <tr>
                            <td>${v.activityCode}</td>
                            <td>${v.experienceGold}</td>
                            <td>${v.effectDay}</td>
                            <td>${v.yearYield}%</td>
                            <td>${v.usersCount}</td>
                            <td><span class="red_font">${v.userspay}</span></td>
                            <td>
                                <shiro:hasPermission name="returnGold:details">
                                    <a href="${path}/finance/returngold/details?gid=${v.id}&startTime=${returnGoldForm.startTime}&endTime=${returnGoldForm.endTime}">查看详情</a>
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
    $("#searchBtn").bind("click", function() {
        var beginDate = $("#beginDate").val();
        var endDate = $("#endDate").val();
        var beginDateSplit = beginDate.split('-');
        var endDateSplit = endDate.split('-');
        var beginDateMonths = parseInt(beginDateSplit[0]) * 12 + parseInt(beginDateSplit[1]);
        var endDateMonths = parseInt(endDateSplit[0]) * 12 + parseInt(endDateSplit[1]);
        if(beginDate > endDate) {
            alert("【开始日期不能晚于结束日期】");
            return false;
        }
        if(endDateMonths - beginDateMonths > 6) {
            alert("【统计起止月份不能大于6个月】");
            return false;
        }
        var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $("#dataform").submit();
        layer.close();

    });

    function pagination(page) {
        $('#page').val(page);
        var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $('#dataform').submit();
        layer.close();
    }

    $().ready(function(){

    });

    function exportReturnGold(obj){

        /*var form = document.forms[0];
        form.action = path + "/finance/returngold/export";
        form.submit();
        form.action = path + "/finance/returngold"*/
        var cloneForm = $(obj).parents('form.form-inline').clone(true);
        $(cloneForm).attr('action', '${path}/finance/returngold/export').submit();
        return false;
    }
</script>