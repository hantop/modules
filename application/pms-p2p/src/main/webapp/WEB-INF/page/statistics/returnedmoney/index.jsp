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
                <li class="active"><b>回款信息</b><img title="符合查询时间内已回款或计划回款的标的,债权转出不含,但包含转入债权的回款" width="15px" src="/static/image/help.png"/></li>
            </ol>
            <form id="dataform" class="form-inline" method="post" action="${path}/statistics/returnedmoney">
                <div class="form-group">
                    <label>回款日期：</label>
                    <input id="startDate" name="startDate" type="text" readonly="readonly" maxlength="20"
                           class="form-control input-sm" style="width:163px;"
                           value="${returnedmoneyInfoForm.startDate}"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
                    --
                    <input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20"
                           class="form-control input-sm" style="width:163px;"
                           value="${returnedmoneyInfoForm.endDate}"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
                    &nbsp;
                    <label>回款状态：</label>
                    <select name="status" class="form-control input-sm"
                            style="width:115px;" value="${returnedmoneyInfoForm.status}">
                        <option value="">全部</option>
                        <option value="1"
                                <c:if test="${returnedmoneyInfoForm.status == 1 }">selected="selected"</c:if>>已还款</option>
                        <option value="2"
                                <c:if test="${returnedmoneyInfoForm.status == 2 }">selected="selected"</c:if>>未还款</option>
                    </select>
                    <label>首次回款<img title="勾选显示首次回款用户1:对于未回款的(未产生实际回款), 预计回款日期同一天的,金额大的算首回2:对于债权转入的, 按债权本金计算" width="15px" src="/static/image/help.png"/>：</label>
                    <c:choose>
                        <c:when test="${returnedmoneyInfoForm.firstReturnedmoney == null}">
                            <input type="checkbox" name="firstReturnedmoney"
                                   value="${returnedmoneyInfoForm.firstReturnedmoney}"/>
                        </c:when>
                        <c:otherwise>
                            <input type="checkbox" name="firstReturnedmoney"
                                   value="${returnedmoneyInfoForm.firstReturnedmoney}" checked="checked"/>
                        </c:otherwise>
                    </c:choose>

                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                </div>
                <input type="hidden" id="page" name="page">
                <input type="hidden" id="fromPage" name="fromPage" value="true">
                <shiro:hasPermission name="statisticsReturnedmoney:search">
                    <button  type="button" class="btn btn-primary btn-sm" id="searchBtn">搜索</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="statisticsReturnedmoney:export">
                    <button type="button" class="btn btn-success btn-sm" onclick="javascript: exportReturnedmoney();">导出</button>
                </shiro:hasPermission>
            </form>
            <hr style="margin-bottom: 0;">
            <div class="panel panel-default">
                <div class="panel-heading">回款信息</div>
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>手机号码</th>
                        <th>姓名</th>
                        <th>借款标题</th>
                        <th>借款期限</th>
                        <th>回款日期 <img title="用户回款的日期,已回款为显示已回款的日期,未回款为计划回款的日期,精确到天" width="15px" src="/static/image/help.png"/></th>
                        <th>回款本金 <img title="该用户在该标的中的的投资本金" width="15px" src="/static/image/help.png"/></th>
                        <th>在投金额 <img title="该用户的在投金额,包含：投资标的成功,投资中冻结的（已投资放款前）债权转让(转出未成功的算在投, 转出成功要减去,债权转入成功按标的价值计算)与新手标。不包含流标的。" width="15px" src="/static/image/help.png"/></th>
                        <th>账户余额</th>
                        <th>回款状态 <img title="已回款(包含正常回款与提前还款)未回款。" width="15px" src="/static/image/help.png"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${returnedmoneyList}" var="v" varStatus="vs">
                        <tr>
                            <td>${v.phonenum}</td>
                            <td>${v.realname}</td>
                            <td>${v.loanTitle}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${v.loanDeadlineType == 1}">
                                        ${v.loanDeadline}天
                                    </c:when>
                                    <c:otherwise>
                                        ${v.loanDeadline}月
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td><fmt:formatDate value="${v.shouldReturnDate}"
                                                pattern="yyyy-MM-dd"></fmt:formatDate></td>
                            <td>${v.principal}</td>
                            <td>${v.investingAmount}</td>
                            <td>${v.balance}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${v.status == 1}">
                                        已还款
                                    </c:when>
                                    <c:otherwise>
                                        未还款
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                    <tfoot>
                    <tr>
                        <td>总人数</td>
                        <td colspan="8">
                            <span>${totalPerson}</span>
                        </td>
                    </tr>
                    </tfoot>
                </table>
            </div>
            <tiles:insertDefinition name="paginator"/>
        </div>
    </div>
</div>
<script type="text/javascript">

    function exportReturnedmoney() {
        var form = $("#dataform");
        form.attr('action',path + "/statistics/returnedmoney/export");
        form.submit();
        form.attr('action',path + "/statistics/returnedmoney");
    }

    $('#searchBtn').bind('click', function () {
        var term = false;
        var staTime = $("#startDate").val();
        var endTime = $("#endDate").val();

        if (staTime == '' && endTime == '') {
            alert("统计日期不能为空");
            return;
        }

        var startDateSplit = staTime.split('-');
        var endDateSplit = endTime.split('-');
        var startDateMonths = parseInt(startDateSplit[0]) * 12 + parseInt(startDateSplit[1]);
        var endDateMonths = parseInt(endDateSplit[0]) * 12 + parseInt(endDateSplit[1]);
        if (endDateMonths - startDateMonths > 6) {
            alert("【统计起止月份不能大于6个月】");
            return false;
        }

        if (staTime != '' && endTime != '') {
            var d1 = new Date(staTime.replace(/-/g, "/"));
            var d2 = new Date(endTime.replace(/-/g, "/"));
            if (Date.parse(d1) - Date.parse(d2) > 0) {
                alert("【统计日期结束时间不能早于开始时间】");
                return;
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
        layer.closeAll();
    }
</script>
