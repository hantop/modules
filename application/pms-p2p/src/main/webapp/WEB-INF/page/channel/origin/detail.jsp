<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div class="container">
    <div class="row">
        <ol class="breadcrumb">
            <li class="active"><a href="${path}/channel/index">渠道管理</a></li>
            <li class="active"><a href="${path}/channel/origin">渠道统计</a></li>
            <li class="active"><b>渠道详情</b></li>
        </ol>
        <div class="col-md-12">
            <form id="dataform" class="form-inline" method="post" action="${path}/channel/origin/detail">
                <input type="hidden" id="page" name="page">
                <input type="hidden" id="channelId" name="channelId" value="${channelDetailForm.channelId}">
                <input type="hidden" id="startDate" name="startDate" value="${channelDetailForm.startDate}">
                <input type="hidden" id="endDate" name="endDate" value="${channelDetailForm.endDate}">
                <shiro:hasPermission name="channelStatisticsDetail:search">
                <div class="form-group">
                    <label>投资金额：</label>
                    <input id="minimumMoney" name="minimumMoney" value="${channelDetailForm.minimumMoney}" type="text" maxlength="10" class="form-control input-sm" style="width:163px;"/>
                    --
                    <input id="maximumMoney"  name="maximumMoney" value="${channelDetailForm.maximumMoney}" type="text" maxlength="10" class="form-control input-sm" style="width:163px;"/>
                </div>&nbsp;&nbsp;
                <button id="searchBtn" type="submit" class="btn btn-primary btn-sm">搜索</button>
                </shiro:hasPermission>
                
                <shiro:hasPermission name="channelStatisticsDetail:export">
                <button type="button" class="btn btn-success btn-sm" onclick="javascript: exportDetail(this);">导出</button>
                </shiro:hasPermission>
            </form>
            <hr style="margin-bottom: 0;">
            <div class="panel panel-default">
                <div class="panel-heading">渠道详情</div>
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>手机号码</th>
                        <th>用户名</th>
                        <th>姓名</th>
                        <th>是否绑卡</th>
                        <th>实名认证</th>
                        <th>身份证号码</th>
                        <th>充值金额 <img title="该用户的累计充值金额" width="15px" src="/static/image/help.png"/></th>
                        <th>投资次数 <img title="该用户的累计投标次数,投资成功即计算，不包含债权转入，流标。债权转出不会减去" width="15px" src="/static/image/help.png"/></th>
                        <th>投资金额 <img title="该用户累计投标的金额,不包含债权转入，流标 。债券转出不会减去" width="15px" src="/static/image/help.png"/></th>
                        <th>激活红包金额 <img title="该用户的历史激活的返现券总和。" width="15px" src="/static/image/help.png"/></th>
                    </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${channelUserStatisticses}" var="v" varStatus="i" >
                            <tr>
                                <td>${v.phoneNum}</td>
                                <td>${v.nickname}</td>
                                <td>${v.name}</td>
                                <td>
                                    <c:if test="${v.isBindBankcard=='true'}">是</c:if> 
                                    <c:if test="${v.isBindBankcard=='false'}">否</c:if>
                                </td>
                                <td>
                                    <c:if test="${v.isAuth=='true'}">是</c:if> 
                                    <c:if test="${v.isAuth=='false'}">否</c:if>                                
                                </td>
                                <td>${v.idCard}</td>
                                <td>${v.rechargeSum}</td>
                                <td>${v.investCount}</td>
                                <td>${v.investSum}</td>
                                <td>${v.activeRedpacketSum}</td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <th>总计</th>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td>${rechargeSum}</td>
                            <td>${investCount}</td>
                            <td>${investSum}</td>
                            <td>${activeRedpacketSum}</td>
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
        var minimumMoney = $("#minimumMoney").val();
        var maximumMoney = $("#maximumMoney").val();
        //输入金额为整数
        var re = /^[0-9]+[0-9]*]*$/;
        if(minimumMoney != ''){
          if (!re.test(minimumMoney))
          {
            alert("起始金额请输入正整数");
            $("#minimumMoney").focus();
            return false;
          }
        }
        if(maximumMoney != ''){
          if (!re.test(maximumMoney))
          {
            alert("结束金额请输入正整数");
            $("#maximumMoney").focus();
            return false;
          }
        }
        if(minimumMoney != ''&&maximumMoney != ''&&minimumMoney-maximumMoney>0){
        	alert("结束金额不能小于起始金额");
        	return false;
        }
    });
    
    function exportDetail(obj){
// 		var form = document.forms[0];
//		form.action = path + "/channel/origin/detail/export";
//		form.submit();
//		form.action = path + "/channel/origin/detail"
        var cloneForm = $(obj).parents('form.form-inline').clone(true);
        $(cloneForm).attr('action', '${path}/channel/origin/detail/export').submit();
        return false;
  }
</script>