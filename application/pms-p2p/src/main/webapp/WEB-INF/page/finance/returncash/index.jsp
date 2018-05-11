<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<link rel="stylesheet" type="text/css" href="/static/theme/returncach.css"/>
<!--My 97 date -->
<script type="text/javascript" src="/static/component/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/static/component/datepicker/calendar.js"></script>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/finance/index">财务管理</a></li>
                <li class="active"><b>返现券</b></li>
            </ol>
            <form id="dataform" class="form-inline" method="post" action="${path}/finance/returncash">
                <div class="form-group">
                    <label>日期：</label>
                    <input id="beginDate"  name="startDate"  type="text" readonly="readonly" maxlength="20" class="form-control input-sm" style="width:163px;"
                           value="${returncachRedpacketForm.startDate}"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd', isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'});"
                           onFocus="WdatePicker({isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'})" />
                    --
                    <input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="form-control input-sm" style="width:163px;"
                           value="${returncachRedpacketForm.endDate}"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd', isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'});"
                           onFocus="WdatePicker({isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'})" />
                </div>&nbsp;&nbsp;
                <div class="form-group">
                    返现券代码：
                    <input id="activityCode" name="activityCode" type="text" class="form-control input-sm"
                           value="${returncachRedpacketForm.activityCode}"/>
                </div>&nbsp;&nbsp;
                <input type="hidden" id="systemgrantFlag" name="systemgrantFlag">
                <input type="hidden" id="page" name="page">
                <shiro:hasPermission name="returncash:search">
                    <button id="searchBtn" type="button" class="btn btn-primary btn-sm">搜索</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="returncash:export">
                    <button type="button" class="btn btn-success btn-sm" onclick="javascript: exportReturncashs(this );">导出</button>
                </shiro:hasPermission>
            </form>
            <hr style="margin-bottom: 0;">
            <h4 class="red_font">总成本: ${totalCost}</h4>
            <div class="panel panel-default">
                <div class="panel-heading">返现券列表</div>
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>返现券代码</th>
                        <th>来源</th>
                        <th>返现券门槛</th>
                        <th>返现券金额</th>
                        <th>返现券有效期</th>
                        <th>发送数量</th>
                        <th>激活数量</th>
                        <th>产生成本</th>
                        <!-- <th>转化投资额</th> -->
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${list}" var="v" varStatus="i">
                        <tr>
                            <td>${v.activityCode == null ? "系统自动发放" : v.activityCode}</td>
                            <td>${v.remarks}</td>
                            <td>${v.investMoney}</td>
                            <td>${v.redMoney}</td>
                            <td>${v.effectDay}</td>
                            <td>${v.redNumber}</td>
                            <td>${v.activeCount}</td>
                            <td class="red_font">${v.redMoneySum}</td>
                            <td>
                                <shiro:hasPermission name="returncash:details">
                                    <a href="${path}/finance/returncash/detail?startDate=${returncachRedpacketForm.startDate}&endDate=${returncachRedpacketForm.endDate}&redpacketId=${v.redpacketId}&systemgrantFlag=${returncachRedpacketForm.systemgrantFlag}">查看详情</a>
                                </shiro:hasPermission>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                    <tfoot>
                    <tr>
                        <td>总计</td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td>
                        	<span>${totalRedNumber}</span><!-- 总发送数量 -->
                        </td>
                        <td >
                            <span>${totalActiveCount}</span><!-- 总激活数量 -->
                        </td>
                        <td >
                            <span class="red_font">${totalCost}</span><!-- 总产生成本 -->
                        </td>
                        <td></td>
                    </tr>
                    </tfoot>
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
        var systemgrantFlag = false;
        var activityCode = $("#activityCode").val();

        if(beginDate > endDate) {
            alert("【开始日期不能晚于结束日期】");
            return false;
        }
        if(endDateMonths - beginDateMonths > 6) {
            alert("【统计起止月份不能大于6个月】");
            return false;
        }

        if(activityCode == "系统自动发放") {
            systemgrantFlag = true;
        }
        $("#getSystemgrantFlag").val(systemgrantFlag);
        $("#activityCode").val($("#activityCode").val().trim());
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

    function exportReturncashs(obj){
        /*var form = document.forms[0];
        form.action = path + "/finance/returncash/export";
        form.submit();
        form.action = path + "/finance/returncash"*/
        var cloneForm = $(obj).parents('form.form-inline').clone(true);
        $(cloneForm).attr('action', '${path}/finance/returncash/export').submit();
        return false;
    }
</script>