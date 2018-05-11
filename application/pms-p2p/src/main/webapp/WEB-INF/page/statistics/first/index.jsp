<%--
  Created by IntelliJ IDEA.
  User: wangyunjing
  Date: 2015/11/6
  Time: 17:31
  To change this template use File | Settings | File Templates.
--%>
<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!--My 97 date -->
<script type="text/javascript" src="/static/component/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/static/component/datepicker/calendar.js"></script>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/statistics/index">统计管理</a></li>
                <li class="active"><b>首投信息</b><img title="查询所有首投用户的数据,按首次投资日期查询,放款才计算(流标的及未放款就不会出现在列表结果中),不包含新手标，但包含债权转让。时间跨度半年" width="15px" src="/static/image/help.png"/></li>
            </ol>
            <form:form id="dataform" commandName="firstInvestForm" method="post" class="form-inline" action="${path}/statistics/firstInvest">
                <div class="row" style="margin-top: 10px;">
                    <div class="col-md-12">
                        <div class="form-group">
                            <label>首投日期：</label>
                            <form:input value="" type="text" cssClass="form-control  input-sm" path="investStartTime" readonly="readonly" maxlength="20"  style="width:163px;" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'});"/>
                            --
                            <form:input type="text" cssClass="form-control  input-sm" path="investEndTime" readonly="readonly" maxlength="20" style="width:163px;" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'});"/>
                        </div>
                        <div class="form-group">
                            <label>注册日期：</label>
                            <form:input value="" type="text" cssClass="form-control  input-sm" path="regStartTime" readonly="readonly" maxlength="20"  style="width:163px;" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'});"/>
                            --
                            <form:input type="text" cssClass="form-control  input-sm" path="regEndTime" readonly="readonly" maxlength="20" style="width:163px;" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'});"/>
                        </div>
                    </div>
                </div>
                <div class="row" style="margin-top: 10px;">
                    <div class="col-md-12">
                        <div class="form-group">
                            <label>首投金额：</label>
                            <form:input value="" type="text" cssClass="form-control  input-sm" path="minInvestMoney" readonly="readonly" maxlength="20"  style="width:163px;" />
                            --
                            <form:input type="text" cssClass="form-control  input-sm" path="maxInvestMoney" readonly="readonly" maxlength="20" style="width:163px;"/>
                        </div>
                        <input type="hidden" name="def" value="false" />
                        <input type="hidden" id="page" name="page">
                        <shiro:hasPermission name="firstInvest:search">
                            <button type="button" class="btn btn-primary btn-sm" id="searchBtn">搜索</button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="firstInvest:export">
                            <button type="button" class="btn btn-success btn-sm"  onclick="javascript: exportInvestStatistics(this);">导出</button>
                        </shiro:hasPermission>
                    </div>
                </div>
            </form:form>

            <hr style="margin-bottom: 0;">
            <div class="panel panel-default">
                <div class="panel-heading">统计信息</div>
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>手机号码</th>
                        <th>姓名</th>
                        <th>首投日期 <img title="用户投资成功的日期(非放款日期，流标的就不会出现在列表中)" width="15px" src="/static/image/help.png"/></th>
                        <th>注册日期 </th>
                        <th>首投金额 <img title="首次投资的本金、债权转入实际购买的价格" width="15px" src="/static/image/help.png"/></th>
                        <th>投资期限 <img title="该次投资的标的期限、债权的期限" width="15px" src="/static/image/help.png"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${list}" var="v" varStatus="vs">
                        <tr>
                            <td>${v.phoneNum}</td>
                            <td>${v.realName}</td>
                            <td>${v.createTime}</td>
                            <td>${v.regtime}</td>
                            <td>${v.money}</td>
                            <td>${v.limitTime}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                    <tfoot>
                    <tr>
                        <td>总人数 : ${paginator.total}</td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
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
    $(function () {

    });

    $('#searchBtn').bind('click', function () {
        var staTime = $("input[name='investStartTime']").val();
        var endTime = $("input[name='investEndTime']").val();
        var moneyStart = $("input[name='minInvestMoney']").val();
        var moneyEnd = $("input[name='maxInvestMoney']").val();


        if (staTime == '' && endTime == '') {
            alert("查询日期不能为空");
            return;
        }

        var beginDateSplit = staTime.split('-');
        var endDateSplit = endTime.split('-');
        var beginDateMonths = parseInt(beginDateSplit[0]) * 12 + parseInt(beginDateSplit[1]);
        var endDateMonths = parseInt(endDateSplit[0]) * 12 + parseInt(endDateSplit[1]);
        if (endDateMonths - beginDateMonths > 6) {
            alert("【查询起止月份不能大于6个月】");
            return false;
        }

        if (staTime != '' && endTime != '') {
            var d1 = new Date(staTime.replace(/-/g, "/"));
            var d2 = new Date(endTime.replace(/-/g, "/"));
            if (Date.parse(d1) - Date.parse(d2) > 0) {
                alert("【投资日期结束时间不能早于开始时间】");
                return;
            }
        }


        //输入金额为整数
        var re = /^\d+(\.\d+)?$/;
        if (moneyStart != '') {
            if (!re.test(moneyStart)) {
                alert("起始金额请输入正整数");
                $("#moneyStart").focus();
                return false;
            }
        }
        if (moneyEnd != '') {
            if (!re.test(moneyEnd)) {
                alert("结束金额请输入正整数");
                $("#moneyEnd").focus();
                return false;
            }
        }

        if(moneyStart != '' && moneyEnd != '') {
            moneyStart = parseFloat(moneyStart);
            moneyEnd = parseFloat(moneyEnd);
            if(moneyStart > moneyEnd) {
                alert("期间开始金额不能大于结束金额");
                $("#moneyEnd").focus();
                return false;
            }
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

    function exportInvestStatistics(obj) {
        var total = '${paginator.total}';
        console.info(total);
        var cloneForm = $(obj).parents('form.form-inline').clone(true);
        $('<input name="limit" value="' + total + '"/>').appendTo(cloneForm);
        $(cloneForm).attr('action', '${path}/statistics/firstInvest/export').submit();
        return false;
    }
</script>
