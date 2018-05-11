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
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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
        <li class="active"><b>实名认证统计</b></li>
      </ol>
      <form id="dataform" class="form-inline" method="post" action="${path}/statistics/authentication">
        <div class="form-group">
          <label>统计日期：</label>
          <input id="beginDate"  name="beginDate"  type="text" readonly="readonly" maxlength="20" class="form-control input-sm" style="width:163px;"
                 <%--value="<fmt:formatDate value="${invest.beginDate}" pattern="yyyy-MM-dd"/>"--%>
                 value="${auth.beginDate}"
                 onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
          --
          <input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="form-control input-sm" style="width:163px;"
                 <%--value="<fmt:formatDate value="${invest.endDate}" pattern="yyyy-MM-dd"/>"--%>
                  value="${auth.endDate}"
                 onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
        </div>
        <input type="hidden" id="page" name="page">
        <input type="hidden" id="fromPage" name="fromPage" value="true">
        <shiro:hasPermission name="statisticsAuth:search">
            <button type="button" class="btn btn-primary btn-sm" id="searchBtn">搜索</button>
        </shiro:hasPermission>
       <%-- <button type="button" class="btn btn-success btn-sm"  onclick="javascript: exportInvestStatistics();">导出</button>--%>
      </form>
      <hr style="margin-bottom: 0;">
      <div class="panel panel-default">
        <div class="panel-heading">统计信息</div>
        <table class="table table-striped table-bordered table-condensed">
          <thead>
          <tr class="success">
            <th>统计日期</th>
            <th>实名认证申请数</th>
          </tr>
          </thead>
          <tbody>
          <c:forEach items="${authList}" var="v" varStatus="vs">
            <tr>
              <td><fmt:formatDate value="${v.createDate}" pattern="yyyy-MM-dd"></fmt:formatDate></td>
              <td>${v.authCount}</td>
            </tr>
          </c:forEach>
          </tbody>
          <tbody>
          <tr>
            <td>总计</td>
            <td>
                <c:if test="${authTotal.authCount != null }">
                 <span style='font-weight: bold;color: red;font-size: 20px'>${authTotal.authCount} * 1.5 = ${authTotal.authMoney}</span>
                </c:if>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
      <tiles:insertDefinition name="paginator"/>
    </div>
  </div>
</div>
<script type="text/javascript">

  $('#searchBtn').bind('click', function() {
    var term = false;
    var staTime = $("#beginDate").val();
    var endTime = $("#endDate").val();

    if(staTime =='' && endTime ==''){
      alert("统计日期不能为空");
      return;
    }

    var beginDateSplit = staTime.split('-');
    var endDateSplit = endTime.split('-');
    var beginDateMonths = parseInt(beginDateSplit[0]) * 12 + parseInt(beginDateSplit[1]);
    var endDateMonths = parseInt(endDateSplit[0]) * 12 + parseInt(endDateSplit[1]);
    if(endDateMonths - beginDateMonths > 6) {
      alert("【统计起止月份不能大于6个月】");
      return false;
    }

    if(staTime != '' && endTime != ''){
      var d1 = new Date(staTime.replace(/-/g,"/"));
      var d2 = new Date(endTime.replace(/-/g,"/"));
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
    $("#dataform").submit();
    layer.close();
  }
</script>
