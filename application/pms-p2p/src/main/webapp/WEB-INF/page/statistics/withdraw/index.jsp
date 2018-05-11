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
                <li class="active"><b>提现信息</b><img title="提现成功的记录,实时数据." width="15px" src="/static/image/help.png"/></li>
            </ol>
            <form:form id="dataform" commandName="withdraw" method="post" class="form-inline" action="${path}/statistics/withdraw">
                <div class="row" style="margin-top: 10px;">
                    <div class="col-md-12">
                        <div class="form-group">
                            <label>提现日期：</label>
                            <form:input value="" type="text" cssClass="form-control  input-sm" path="withdrawStartTime" readonly="readonly" maxlength="20"  style="width:163px;" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'});"/>
                            --
                            <form:input type="text" cssClass="form-control  input-sm" path="withdrawEndTime" readonly="readonly" maxlength="20" style="width:163px;" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'});"/>
                        </div>

                        <div class="form-group">
                            <label>提现金额：</label>
                            <form:input value="" type="text" cssClass="form-control  input-sm" path="minWithdrawMoney" readonly="readonly" maxlength="20"  style="width:163px;" />
                            --
                            <form:input type="text" cssClass="form-control  input-sm" path="maxWithdrawMoney" readonly="readonly" maxlength="20" style="width:163px;"/>
                        </div>
                        <input type="hidden" name="def" value="false" />
                        <input type="hidden" id="page" name="page">
                        <shiro:hasPermission name="withdraw:search">
                            <button type="button" class="btn btn-primary btn-sm" id="searchBtn">搜索</button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="withdraw:export">
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
                        <th>提现日期 <img title="用户提现成功的日期(从账户转出开始算,非到账) 精确到天" width="15px" src="/static/image/help.png"/></th>
                        <th>提现金额 <img title="该次提现的实际金额" width="15px" src="/static/image/help.png"/></th>
                        <th>累计投资金额 <img title="该用户累计投资的金额,只计算放款的，包含购买的债权（按实际付费金额）, 新手标,债权转出的也不减去" width="15px" src="/static/image/help.png"/></th>
                        <th>累计投资次数 <img title="用户历史累计投资的次数,只计算放款的，包含购买的债权,新手标,债权转出的也不减去" width="15px" src="/static/image/help.png"/></th>
                        <th>在投金额 <img title="该用户的在投金额,包含：投资标的成功,投资中冻结的（已投资放款前）债权转让(转出未成功的算在投, 转出成功要减去,债权转入成功按标的价值计算)与新手标。不包含流标的。" width="15px" src="/static/image/help.png"/></th>
                        <th>账户余额 <img title="用户分利宝钱包里的现金余额,不包含提现中冻结的金额与待放款的。但提现失败后返回的需要计算。" width="15px" src="/static/image/help.png"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${list}" var="v" varStatus="vs">
                        <tr>
                            <td>${v.phoneNum}</td>
                            <td>${v.realName}</td>
                            <td>${v.createTime}</td>
                            <td>${v.withdrawMoney eq null ? '0' : v.withdrawMoney}</td>
                            <td>${v.investTotalMoney eq null ? '0' : v.investTotalMoney}</td>
                            <td>${v.investCount eq null ? '0' : v.investCount}</td>
                            <td>${v.investing eq null ? '0' : v.investing}</td>
                            <td>${v.surplusMoney}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                    <tfoot>
                    <tr>
                        <td>总计</td>
                        <td></td>
                        <td></td>
                        <td>${withdrawTotal.withdrawTotalMoney}</td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td>总人数 : ${withdrawTotal.totalWithdrawr}</td>
                        <td></td>
                        <td></td>
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
        var staTime = $("input[name='withdrawStartTime']").val();
        var endTime = $("input[name='withdrawEndTime']").val();
        var moneyStart = $("input[name='minWithdrawMoney']").val();
        var moneyEnd = $("input[name='maxWithdrawMoney']").val();


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
                alert("【查询日期结束时间不能早于开始时间】");
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
        $("#dataform").submit();
        layer.close();
    }

    function exportInvestStatistics(obj) {
        var total = '${paginator.total}';
        console.info(total);
        var cloneForm = $(obj).parents('form.form-inline').clone(true);
        $('<input name="limit" value="' + total + '"/>').appendTo(cloneForm);
        $(cloneForm).attr('action', '${path}/statistics/withdraw/export').submit();
        return false;
    }
</script>
