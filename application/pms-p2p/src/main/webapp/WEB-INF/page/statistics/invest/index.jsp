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
<script type="text/javascript" src="/static/component/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/static/component/datepicker/calendar.js"></script>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/statistics/index">统计管理</a></li>
                <li class="active"><b>投资统计</b><img title="列表展示的为所搜索的投资日期时间段内产生投资行为的客户的数据（包含未放款，放款，流标）购买标的，债权，新手标的都计算，债权转出后数据也不消去。最新投资的在最前. 数据查询跨度为半年。数据为实时数据。搜索基于投资日期为基准作为依据，其他筛选项为辅助筛选" width="15px" src="/static/image/help.png"/></li>
            </ol>
            <form:form id="dataform" commandName="invest" method="post" class="form-inline" action="${path}/statistics/invest">
                <div class="row">
                    <div class="col-md-12">
                        <div class="form-group">
                            <label>投资日期：</label>
                            <form:input value="" type="text" cssClass="form-control  input-sm" id="startTime" path="investStartTime" readonly="readonly" maxlength="20"  style="width:120px;" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'});"/>
                            --
                            <form:input type="text" cssClass="form-control input-sm" path="investEndTime" id="d4332" readonly="readonly" maxlength="20" style="width:120px;" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'});"/>
                        </div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <div class="form-group">
                            <label>注册日期：</label>
                            <form:input value="" type="text" cssClass="form-control  input-sm" id="startTime" path="regStartTime" readonly="readonly" maxlength="20"  style="width:120px;" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'});"/>
                            --
                            <form:input type="text" cssClass="form-control  input-sm" path="regEndTime" readonly="readonly" maxlength="20" style="width:120px;" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'});"/>
                        </div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <div class="form-group">
                            <label>投资金额：</label>
                            <form:input value="" type="text" cssClass="form-control  input-sm" id="startTime" path="minInvestMoney" readonly="readonly" maxlength="20"  style="width:110px;" />
                            --
                            <form:input type="text" cssClass="form-control  input-sm" path="maxInvestMoney" readonly="readonly" maxlength="20" style="width:110px;"/>
                        </div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    </div>
                </div>
                <div class="row" style="margin-top: 10px;">
                    <div class="col-md-12">
                        <div class="form-group">
                            <label>产品类型：</label>
                            <form:select path="productType" cssClass="form-control">
                                <form:option value="">全部</form:option>
                                <form:options items="${productTypes}" itemLabel="value" itemValue="key"></form:options>
                            </form:select>
                        </div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <div class="form-group">
                            <label>标的类型：</label>
                            <form:select path="bidType" cssClass="form-control">
                                <form:option value="">全部</form:option>
                                <form:options items="${bidTypes}" itemLabel="enumValue" itemValue="enumKey"></form:options>
                            </form:select>
                        </div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <%--<div class="form-group">
                            <label>状态：</label>
                            <form:select path="status" cssClass="form-control">
                                <form:option value="">全部</form:option>
                                <form:options items="${statuses}" itemLabel="enumValue" itemValue="enumKey"></form:options>
                            </form:select>
                        </div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;--%>
                        <div class="form-group">
                            <label>首投<img title="用户的首次投资，债权，新手标也算首投，流标不重置首投状态.，就是产生第一次动作的那次都算首投" width="15px" src="/static/image/help.png"/>：</label>
                            <c:choose>
                                <c:when test="${invest.firstInvest == null || invest.firstInvest eq ''}">
                                    <input type="checkbox" name="firstInvestStr"
                                           value="${invest.firstInvest}"/>
                                </c:when>
                                <c:otherwise>
                                    <input type="checkbox" name="firstInvestStr"
                                           value="${invest.firstInvest}" checked="checked"/>
                                </c:otherwise>
                            </c:choose>
                        </div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <div class="form-group">
                            <label>注册渠道：</label>
                            <form:input type="text" cssClass="form-control input-sm" path="channelName" style="width:163px;" />
                        </div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

                        <input type="hidden" name="def" value="false" />
                        <input type="hidden" id="page" name="page">
                        <shiro:hasPermission name="statisticsInvest:search">
                            <button type="button" class="btn btn-primary btn-sm" id="searchBtn">搜索</button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="statisticsInvest:export">
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
                        <th>手机号码 <img title="用户的手机号码" width="15px" src="/static/image/help.png"/></th>
                        <th>投资日期 <img title="实际产生投资动作的日期" width="15px" src="/static/image/help.png"/></th>
                        <th>注册日期 <img title="用户的手机号码" width="15px" src="/static/image/help.png"/></th>
                        <th>姓名 <img title="显示用户姓名" width="15px" src="/static/image/help.png"/></th>
                        <th>注册渠道 <img title="客户注册的渠道 显示渠道名称 括号内为渠道的编号(主要用于客服电销的时候需要剔除部分渠道的用户,还有与渠道进行商务结算的时候需要根据客户的数据进行结算, 在渠道帮忙推广的时候已经包含投资催促服务费的情况下需要剔除)" width="15px" src="/static/image/help.png"/></th>
                        <th>投资金额 <img title="该次投资的投资本金，或债权转入的实际支付金额" width="15px" src="/static/image/help.png"/></th>
                        <th>累计投资金额 <img title="该用户累计投资的金额, 客户投资成功即计算，包含流标的，债权转出的也不减去，购买的债权（按实际付费金额）, 新手标" width="15px" src="/static/image/help.png"/></th>
                        <th>累计投资次数 <img title="该用户累计投资的次数, 客户投资成功即计算，包含流标的，债权转出的也不减去，购买的债权（按实际付费金额）, 新手标" width="15px" src="/static/image/help.png"/></th>
                        <th>账户余额 <img title="用户分利宝钱包里的现金余额, 不包含提现未到账冻结的金额。但提现失败后返回的需要计算(用于对比在投金额, 存在余额没使用的用户是重点营销对象)" width="15px" src="/static/image/help.png"/></th>
                        <th>产品类型 <img title="所投资标的产品的类型，开店王/车贷王/房贷王/散标/" width="15px" src="/static/image/help.png"/></th>
                        <th>产品分类 <img title="选所投资产品的分类，新手/普通/债权" width="15px" src="/static/image/help.png"/></th>
                        <th>投资期限<img title="该次投资的标的的期限，如为债权则显示所购买的债权的期限" width="15px" src="/static/image/help.png"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${list}" var="v" varStatus="vs">
                        <tr>
                            <td>${v.phoneNum}</td>
                            <td>${v.createTime}</td>
                            <td>${v.regtime}</td>
                            <td>${v.realName}</td>
                            <td>${v.channelName}</td>
                            <td>${v.investMoney eq null ? '0' : v.investMoney}</td>
                            <td>${v.investTotalMoney}</td>
                            <td>${v.investCount}</td>
                            <td>${v.surplusMoney}</td>
                            <td>${v.productType}</td>
                            <td>
                                <c:forEach items="${bidTypes}" var="var">
                                    <c:if test="${v.bidType == var.enumKey}">
                                        ${var.enumValue}
                                    </c:if>
                                </c:forEach>
                            </td>
                            <td>${v.limitTime}</td>
                            <%--<td>
                                <c:forEach items="${statuses}" var="var">
                                    <c:if test="${v.status == var.enumKey}">
                                        ${var.enumValue}
                                    </c:if>
                                </c:forEach>
                            </td>--%>
                        </tr>
                    </c:forEach>
                    </tbody>
                    <tfoot>
                    <tr>
                        <td colspan="5">总计</td>
                        <td colspan="8">${investTotal.investTotalMoney}</td>
                    </tr>
                        <td>总人数： ${investTotal.totalInvestor}</td>
                        <td>总行数： ${investTotal.totalLine}</td>
                        <td colspan="11"></td>
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
            alert("统计日期不能为空");
            return;
        }

        var beginDateSplit = staTime.split('-');
        var endDateSplit = endTime.split('-');
        var beginDateMonths = parseInt(beginDateSplit[0]) * 12 + parseInt(beginDateSplit[1]);
        var endDateMonths = parseInt(endDateSplit[0]) * 12 + parseInt(endDateSplit[1]);
        if (endDateMonths - beginDateMonths > 6) {
            alert("【统计起止月份不能大于6个月】");
            return false;
        }

        if (staTime != '' && endTime != '') {
            var d1 = new Date(staTime.replace(/-/g, "/"));
            var d2 = new Date(endTime.replace(/-/g, "/"));
            if (Date.parse(d1) - Date.parse(d2) > 0) {
                alert("【注册日期结束时间不能早于开始时间】");
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
        $(document.body).append(cloneForm)
        $(cloneForm).attr('action', '${path}/statistics/invest/export').submit();
        return false;
    }
</script>