<%--
  Created by IntelliJ IDEA.
  User: bogle
  Date: 2015/11/10
  Time: 15:28
  To change this template use File | Settings | File Templates.
--%>
<%@page language="java" pageEncoding="utf-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<link rel="stylesheet" href="/static/component/HoldOn/HoldOn.min.css">
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script type="text/javascript" src="/static/component/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/static/component/datepicker/calendar.js"></script>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<style type="text/css">
    .red_font {
        color: red;
    }
</style>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/finance/index">财务管理</a></li>
                <li class="active"><b>现金红包</b></li>
            </ol>
            <form:form class="form-inline" role="form" method="post" action="${path}/finance/cash/cashList" id="dataform"
                       commandName="cashRedpacket">
                <div class="form-group">
                    <label class="control-label">投资日期：</label>
                </div>
                <div class="form-group">
                    <form:input cssClass="form-control input-sm" path="startTime" readonly="readonly" maxlength="30" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/> --
                </div>
                <div class="form-group">
                    <form:input cssClass="form-control input-sm" path="endTime" readonly="readonly" maxlength="30" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
                </div>
                <div class="form-group">
                    <label class="control-label">红包金额</label>
                </div>
                <div class="form-group">
                    <form:input path="minMoney" cssClass="form-control input-sm" cssStyle="width:100px;"/> --
                </div>
                <div class="form-group">
                    <form:input path="maxMoney" cssClass="form-control input-sm" cssStyle="width:100px;"/>
                </div>
                <div class="form-group">
                    <label class="control-label">用户手机号</label>
                </div>
                <div class="form-group">
                    <form:input path="phoneNum" cssClass="form-control input-sm" maxlength="11"/>
                </div>
                <input type="hidden" id="page" name="page">
                <shiro:hasPermission name="cashPacket:search">
                    <button type="submit" class="btn btn-primary btn-sm">搜索</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="cashPacket:export">
                    <button id="export" type="button" class="btn btn-success btn-sm">导出</button>
                </shiro:hasPermission>
            </form:form>
            <hr style="margin-bottom: 0;">
            <h4 class="red_font">总成本: <c:out value="${sumMoney}" default="0"/></h4>
        </div>
        <div class="col-md-5">
            <table class="table table-striped table-bordered table-condensed table-hover" >
                <thead>
                    <tr class="success">
                        <th>发送日期</th>
                        <th>用户手机号</th>
                        <th>发送金额</th>
                    </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${list== null || fn:length(list) == 0}">
                        <%--<tr>
                            <td colspan="4">无数据</td>
                        </tr>--%>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="item" items="${list}">
                            <tr>
                                <td>${item.sendTime}</td>
                                <td>${item.phoneNum}</td>
                                <td>${item.money}</td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
            <tiles:insertDefinition name="paginator"/>
        </div>
    </div>
</div>

<script type="application/javascript">
    (function ($) {
         var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
          });
            $("#dataform").on('submit',function() {
                var reg = /^(13|14|15|17|18)[0-9]{9}$/;

                var startTime = Date.parse(new Date($('input[name="startTime"]').val()));
                var endTime = Date.parse(new Date($('input[name="endTime"]').val()));
                if(startTime > endTime) {
                    layer.closeAll();
                    bootbox.alert({message:"<h3>统计开始日期不能大于结束日期</h3>"});
                    return false;
                }
                var minMoney = $.trim($('input[name="minMoney"]').val());
                var maxMoney = $.trim($('input[name="maxMoney"]').val());
                if(minMoney != '' && isNaN(parseFloat(minMoney))) {
                    layer.closeAll();
                    bootbox.alert({message:"<h3>红包金额只能为数字</h3>"});
                    return false;
                }
                if(maxMoney != '' && isNaN(parseFloat(maxMoney))) {
                    layer.closeAll();
                    bootbox.alert({message:"<h3>红包金额只能为数字</h3>"});
                    return false;
                }
                if((minMoney != '' && !isNaN(parseFloat(minMoney)) && parseFloat(minMoney) < 0) || (maxMoney != '' && !isNaN(parseFloat(maxMoney)) && parseFloat(maxMoney) < 0 )) {
                    layer.closeAll();
                    bootbox.alert({message:"<h3>红包金不能为负数</h3>"});
                    return false;
                }
                if(minMoney != '' && !isNaN(parseFloat(minMoney)) && maxMoney != '' && !isNaN(parseFloat(maxMoney)) && parseFloat(minMoney) > parseFloat(maxMoney)) {
                    layer.closeAll();
                    bootbox.alert({message:"<h3>红包金额最小值不能大于最大值</h3>"});
                    return false;
                }
                var phoneNum = $('input[name="phoneNum"]').val();
                if(phoneNum != '' && !reg.test(phoneNum)) {
                    layer.closeAll();
                    bootbox.alert({message:"<h3>手机号码格式不正确</h3>"});
                    return false;
                }
                return true;
            });
        layer.closeAll();

        $("#export").on('click', function () {
            var cloneForm = $(this).parents('form.form-inline').clone(true);
            $(cloneForm).attr('action', '${path}/finance/cash/export').submit();
            return false;
        });
    })(jQuery);
</script>