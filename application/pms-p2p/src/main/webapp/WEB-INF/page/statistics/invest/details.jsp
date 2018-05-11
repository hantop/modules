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

<div class="container">
  <div class="row">
    <div class="col-md-12">
      <ol class="breadcrumb">
        <li class="active"><a href="${path}/statistics/index">统计管理</a></li>
        <li class="active"><a href="${path}/statistics/invest">用户信息</a></li>
        <li class="active"><b>用户详情</b></li>
      </ol>
      <form id="dataform" class="form-inline" method="get" action="${path}/statistics/invest/details">
        <input type="hidden" id="page" name="page">
        <input type="hidden" id="fromPage" name="fromPage" value="true">
        <input type="hidden" id="userId" name="${userInvest.userId}">
      </form>
      <div class="form-group"><label>手机号码：${userInvest.phone}</label>&nbsp;&nbsp;&nbsp;&nbsp;<label>邀请人在投金额：${userInvest.formatMoney}元</label></div>
      <hr style="margin-bottom: 0;">
      <div class="panel panel-default">
        <div class="panel-heading">推广明细</div>
        <table class="table table-striped table-bordered table-condensed">
          <thead>
          <tr class="success">
            <%--<th class="text-center"><input type="checkbox"></th>--%>
            <%--<th>序号</th>--%>
            <th>被推广用户手机</th>
            <th>注册时间</th>
            <th>投资总金额(元)</th>
            <th>获得返现券金额(元)</th>
            <th>激活返现券金额(元)</th>
          </tr>
          </thead>
          <tbody>
          <c:forEach items="${userRankList}" var="v" varStatus="vs">
            <tr>
              <%--<td class="text-center">
                <input type="checkbox">
              </td>--%>
              <td>${v.phone}</td>
              <td><fmt:formatDate value="${v.regtime}" pattern="yyyy-MM-dd"></fmt:formatDate></td>
              <td>${v.outmoney}</td>
              <td>${v.allred}</td>
              <td>${v.usered}</td>
                <%--<c:choose>
                  <c:when test="${v.status eq 1}">
                    <td style="color: green !important;">启用</td>
                  </c:when>
                  <c:otherwise>
                    <td style="color: red !important;">禁用</td>
                  </c:otherwise>
                </c:choose>--%>
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

  function pagination(page) {
  //  $('#page').val(page);
  //  $('#dataform').submit();
    location.href = "${path}/statistics/invest/details?fromPage=true&page="+page+"&userId=${userInvest.userId}";
  }
</script>
