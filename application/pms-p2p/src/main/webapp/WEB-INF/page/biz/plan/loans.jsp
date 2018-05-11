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
                <li class="active"><a href="${path}/biz/index">业务管理</a></li>
                <li class="active"><b>计划标的</b></li>
            </ol>
            <form id="dataform" class="form-inline" method="post" action="${path}/biz/plan/loans">
                <div class="form-group">
                    <label>计划名称：</label>
                    <input id="planName" name="planName" type="text" class="form-control input-sm"
                           style="width:163px;"
                           value="${loansForm.planName}"/>
                    &nbsp;
                    <label>标的名称：</label>
                    <input id="loanName" name="loanName" type="text" class="form-control input-sm"
                           style="width:163px;"
                           value="${loansForm.loanName}"/>
                    &nbsp;
                    <label>发布时间：</label>
                    <input id="releaseStartDate" name="releaseStartDate" type="text" readonly="readonly" maxlength="20"
                           class="form-control input-sm" style="width:163px;"
                           value="${loansForm.releaseStartDate}"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
                    --
                    <input id="releaseEndDate" name="releaseEndDate" type="text" readonly="readonly" maxlength="20"
                           class="form-control input-sm" style="width:163px;"
                           value="${loansForm.releaseEndDate}"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
                </div>
                <br/>
                <br/>
                <div class="form-group">
                    <label>投满时间：</label>
                    <input id="tenderfullStartDate" name="tenderfullStartDate" type="text" readonly="readonly" maxlength="20"
                           class="form-control input-sm" style="width:163px;"
                           value="${loansForm.tenderfullStartDate}"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
                    --
                    <input id="tenderfullEndDate" name="tenderfullEndDate" type="text" readonly="readonly" maxlength="20"
                           class="form-control input-sm" style="width:163px;"
                           value="${loansForm.tenderfullEndDate}"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
                    &nbsp;
                    <label>状态</label>
                    <select id="status" name="status" class="form-control input-sm"
                            style="width:115px;" value="${loansForm.status}">
                        <option value="">全部</option>
                        <option value="TBZ" <c:if test='${loansForm.status == "TBZ"}'>selected="selected"</c:if>>投标中</option>
                        <option value="DFK" <c:if test='${loansForm.status == "DFK"}'>selected="selected"</c:if>>待放款</option>
                        <option value="YFK" <c:if test='${loansForm.status == "YFK"}'>selected="selected"</c:if>>已放款</option>
                    </select>
                </div>
                <input type="hidden" id="planId" name="planId" value="${loansForm.planId}">
                <input type="hidden" id="page" name="page">
                <input type="hidden" id="fromPage" name="fromPage" value="true">
                <shiro:hasPermission name="loans:search">
                    <button type="button" class="btn btn-primary btn-sm" id="searchBtn">搜索</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="loans:export">
                    <button type="button" class="btn btn-success btn-sm" onclick="javascript: exportPlanLoans();">导出</button>
                </shiro:hasPermission>
            </form>
            <hr style="margin-bottom: 0;">
            <div class="panel panel-default">
                <div class="panel-heading">计划</div>
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>序号</th>
                        <th>计划名称</th>
                        <th>标的名称</th>
                        <th>标的金额</th>
                        <th>标的期限</th>
                        <th>投资利率</th>
                        <th>加息利率</th>
                        <th>状态</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${loans}" var="v" varStatus="vs">
                        <tr>
                            <td>${vs.index + 1}</td>
                            <td>${v.planName}</td>
                            <td>${v.loanName}</td>
                            <td>${v.amount}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${v.cycleType eq 'm'}">
                                        ${v.cycle}月
                                    </c:when>
                                    <c:otherwise>
                                        ${v.cycle}天
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${v.rateStr}%</td>
                            <td>${v.raisedRateStr}%</td>
                            <td>
                                <c:choose>
                                    <c:when test="${v.status eq 'TBZ'}">
                                        投标中
                                    </c:when>
                                    <c:when test="${v.status eq 'DFK'}">
                                        待放款
                                    </c:when>
                                    <c:otherwise>
                                        已放款
                                    </c:otherwise>
                                </c:choose>
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
    $('#searchBtn').bind('click', function() {
        var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $("#dataform").submit();
        layer.close();
    });

    function exportPlanLoans() {
        var form = $("#dataform");
        form.attr('action',path + "/biz/plan/loans/export");
        form.submit();
        form.attr('action',path + "/biz/plan/loans");
    }

    function pagination(page) {
        $('#page').val(page);
            var index = layer.load(0, {
                shade: [0.4,'#fff',false] //0.1透明度的白色背景
            });
            $("#dataform").submit();
            layer.close();
    }
</script>
