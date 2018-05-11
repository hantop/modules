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
                <li class="active"><b>计划</b></li>
            </ol>
            <form id="dataform" class="form-inline" method="post" action="${path}/biz/plan">
                <div class="form-group">
                    <label>计划名称：</label>
                    <input id="title" name="title" type="text" class="form-control input-sm"
                           style="width:163px;"
                           value="${planForm.title}"/>
                    &nbsp;
                    <label>发布时间：</label>
                    <input id="releaseStartDate" name="releaseStartDate" type="text" readonly="readonly" maxlength="20"
                           class="form-control input-sm" style="width:163px;"
                           value="${planForm.releaseStartDate}"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
                    --
                    <input id="releaseEndDate" name="releaseEndDate" type="text" readonly="readonly" maxlength="20"
                           class="form-control input-sm" style="width:163px;"
                           value="${planForm.releaseEndDate}"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
                    &nbsp;
                    <label>投满时间：</label>
                    <input id="tenderfullStartDate" name="tenderfullStartDate" type="text" readonly="readonly" maxlength="20"
                           class="form-control input-sm" style="width:163px;"
                           value="${planForm.tenderfullStartDate}"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
                    --
                    <input id="tenderfullEndDate" name="tenderfullEndDate" type="text" readonly="readonly" maxlength="20"
                           class="form-control input-sm" style="width:163px;"
                           value="${planForm.tenderfullEndDate}"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
                </div>
                <br/>
                <br/>
                <div class="form-group">
                    <label>状态</label>
                    <select id="status" name="status" class="form-control input-sm"
                            style="width:115px;" value="${planForm.status}">
                        <option value="">全部</option>
                        <option value="TBZ" <c:if test='${planForm.status == "TBZ"}'>selected="selected"</c:if>>投标中</option>
                        <option value="DFK" <c:if test='${planForm.status == "DFK"}'>selected="selected"</c:if>>已满额</option>
                        <option value="HKZ" <c:if test='${planForm.status == "HKZ"}'>selected="selected"</c:if>>还款中</option>
                        <option value="YJQ" <c:if test='${planForm.status == "YJQ"}'>selected="selected"</c:if>>已结清</option>
                    </select>
                </div>
                <input type="hidden" id="page" name="page">
                <input type="hidden" id="fromPage" name="fromPage" value="true">
                <shiro:hasPermission name="plan:search">
                    <button type="button" class="btn btn-primary btn-sm" id="searchBtn">搜索</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="plan:export">
                    <button type="button" class="btn btn-success btn-sm" onclick="javascript: exportPlan();">导出</button>
                </shiro:hasPermission>
            </form>
            <hr style="margin-bottom: 0;">
            <div class="panel panel-default">
                <div class="panel-heading">计划</div>
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>序号</th>
                        <th>名称</th>
                        <th>金额</th>
                        <th>期限</th>
                        <th>投资利率</th>
                        <th>加息利率</th>
                        <th>投资人数</th>
                        <th>标的数据(个)</th>
                        <th>发布时间</th>
                        <th>投满时间</th>
                        <th>状态</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${plans}" var="v" varStatus="vs">
                        <tr>
                            <td>${vs.index + 1}</td>
                            <td>${v.title}</td>
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
                            <td>${v.investPeopleNum}</td>
                            <td>${v.loanNum}</td>
                            <td>
                                <fmt:formatDate value="${v.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                            </td>
                            <td>
                                <fmt:formatDate value="${v.tenderfullTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${v.status eq 'TBZ'}">
                                        投资中
                                    </c:when>
                                    <c:when test="${v.status eq 'DFK'}">
                                        已满额
                                    </c:when>
                                    <c:when test="${v.status eq 'HKZ'}">
                                        还款中
                                    </c:when>
                                    <c:when test="${v.status eq 'YJQ'}">
                                        已结清
                                    </c:when>
                                </c:choose>
                            </td>
                            <td>
                                <shiro:hasPermission name="plan:search">
                                    <a href="${path}/biz/plan/id/${v.id}">查看详情</a>
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
    $('#searchBtn').bind('click', function() {
        var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $("#dataform").submit();
        layer.close();
    });

    function exportPlan() {
        var form = $("#dataform");
        form.attr('action',path + "/biz/plan/export");
        form.submit();
        form.attr('action',path + "/biz/plan");
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
