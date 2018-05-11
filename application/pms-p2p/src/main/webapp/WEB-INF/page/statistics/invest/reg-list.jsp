<%@ page import="java.util.Date" %>
<%--
  Created by IntelliJ IDEA.
  User: bogle
  Date: 2015/11/10
  Time: 15:28
  To change this template use File | Settings | File Templates.
--%>
<%@page language="java" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<link rel="stylesheet" href="/static/component/HoldOn/HoldOn.min.css">
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script type="text/javascript" src="/static/component/HoldOn/HoldOn.min.js"></script>
<script type="text/javascript" src="/static/component/jquery-form/jquery.form.js"></script>
<script type="text/javascript" src="/static/component/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/statistics/index">统计管理</a></li>
                <li class="active"><b>注册统计 <img title="列表数据说明：注册成功的即计算" width="15px" src="/static/image/help.png"/></b></li>
            </ol>
            <form:form id="dataform" commandName="reg" method="post" class="form-inline" action="${path}/statistics/reg/reg-list">
                <div class="form-group">
                    <label>注册日期：</label>
                    <form:input value="" type="text" cssClass="form-control  input-sm" id="startTime" path="startTime" readonly="readonly" maxlength="20"  style="width:163px;" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'});"/> --
                    <form:input type="text" cssClass="form-control  input-sm" path="endTime" readonly="readonly" maxlength="20" style="width:163px;" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'});"/>
                </div>
                <div class="row" style="margin-top: 10px;">
                    <div class="col-md-12">
                        <div class="form-group">
                            <label>实名认证：</label>
                            <form:select path="isAuth" cssClass="form-control">
                                <form:option value="">全部</form:option>
                                <form:option value="1">是</form:option>
                                <form:option value="0">否</form:option>
                            </form:select>
                        </div>
                        <div class="form-group">
                            <label>是否绑卡：</label>
                            <form:select path="isBankCar" cssClass="form-control">
                                <form:option value="">全部</form:option>
                                <form:option value="1">是</form:option>
                                <form:option value="0">否</form:option>
                            </form:select>
                        </div>
                        <div class="form-group">
                            <label>是否投资：</label>
                            <form:select path="isInvestMoney" cssClass="form-control">
                                <form:option value="">全部</form:option>
                                <form:option value="1">是</form:option>
                                <form:option value="0">否</form:option>
                            </form:select>
                        </div>
                        <div class="form-group">
                            <label>累计投资金额：</label>
                            <form:input value="" type="text" cssClass="form-control  input-sm" id="minInvestMoney" path="minInvestMoney" readonly="readonly" maxlength="20"  style="width:163px;" />
                            --
                            <form:input type="text" cssClass="form-control  input-sm" id="maxInvestMoney" path="maxInvestMoney" readonly="readonly" maxlength="20" style="width:163px;"/>
                        </div>
                        <div class="form-group">
                            <shiro:hasPermission name="statisticsReg:search">
                                <button id="searchBtn" type="submit" onclick="return vaildData(this);" class="btn btn-primary btn-sm" >查询</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="statisticsReg:export">
                                <button id="export" type="button" class="btn btn-success btn-sm" >导出</button>
                            </shiro:hasPermission>
                        </div>
                    </div>
                </div>
                <input type="hidden" id="page" name="page">
            </form:form>
            <table class="table table-striped table-bordered table-condensed table-hover" style="margin-top: 20px;">
                <thead>
                <tr class="success">
                    <th>注册日期</th>
                    <th>手机号码</th>
                    <th>姓名</th>
                    <th>实名认证</th>
                    <th>是否绑卡</th>
                    <th>累计充值金额 <img title="该用户累计充值的金额，从银行等支付方式向分利宝账户支付成功的金额" width="15px" src="/static/image/help.png"/></th>
                    <th>累计投资次数 <img title="该用户累计投资的次数,客户投资成功即计算，包含流标的，债权转出的也不减去，购买的债权（按实际付费金额）, 新手标" width="15px" src="/static/image/help.png"/></th>
                    <th>累计投资金额 <img title="该用户累计投资的金额,客户投资成功即计算，包含流标的，债权转出的也不减去，购买的债权（按实际付费金额）, 新手标" width="15px" src="/static/image/help.png"/></th>
                    <th>在投金额 <img title="该用户的在投金额,包含：投资标的成功,投资中冻结的（已投资放款前）债权转让(转出未成功的算在投, 转出成功要减去,债权转入成功按标的价值计算)与新手标。不包含流标的。" width="15px" src="/static/image/help.png"/></th>
                    <th>账户余额 <img title="用户分利宝钱包里的现金余额，与用户在账户中看到的一致" width="15px" src="/static/image/help.png"/></th>
                    <th>客户端类型 <img title="客户注册的平台类型,以投资所使用的平台为准：分为 手机客户端（ios）手机客户端（Android）手机WAP端，PC端，其他" width="15px" src="/static/image/help.png"/></th>
                    <th>渠道来源 <img title="客户注册的渠道 显示渠道名称 括号内为渠道的编号" width="15px" src="/static/image/help.png"/></th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${list== null || fn:length(list) == 0}">
                        <%-- <tr>
                             <td colspan="4">无数据</td>
                         </tr>--%>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="item" items="${list}">
                            <tr>
                                <td>${item.registerTime}</td>
                                <td>${item.phone}</td>
                                <td>${item.name}</td>
                                <td>${item.realName}</td>
                                <td>${item.bindBank}</td>
                                <td>${item.fund}</td>
                                <td>${item.investNum}</td>
                                <td>${item.investMoney}</td>
                                <td>${item.investSum}</td>
                                <td>${item.balance}</td>
                                <td>${item.clientType}</td>
                                <td>${item.channelName}</td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                </tbody>
                <tfoot>
                <tr>
                    <td>总计 : </td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td>${regForm.investMoneyTotal}</td>
                    <td>${regForm.investSumTotal}</td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
                <tr>
                    <td colspan="12">总人数 : ${regTotal}</td>
                </tr>
                </tfoot>
            </table>
            <tiles:insertDefinition name="paginator"/>
        </div>
    </div>
</div>

<script type="application/javascript">
    (function ($) {

        $("#export").on('click', function () {
            var cloneForm = $(this).parents('form.form-inline').clone(true);
            $(cloneForm).attr('action', '${path}/statistics/reg/reg-export').submit();
            return false;
        });
        var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $('#dataform').on('submit',function() {
            //debugger;
            var staTime = $('input[name="startTime"]').val();
            var endTime = $('input[name="endTime"]').val();
            if(staTime != '' && endTime != ''){
                var d1 = new Date(staTime.replace(/-/g,"/"));
                var d2 = new Date(endTime.replace(/-/g,"/"));

                var beginDateSplit = staTime.split('-');
                var endDateSplit = endTime.split('-');
                var beginDateMonths = parseInt(beginDateSplit[0]) * 12 + parseInt(beginDateSplit[1]);
                var endDateMonths = parseInt(endDateSplit[0]) * 12 + parseInt(endDateSplit[1]);

                if (Date.parse(d1) - Date.parse(d2) > 0) {
                    layer.closeAll();
                    bootbox.alert({message:"<h3>【结束时间不能早于开始时间】</h3>"});
                }
                else if(endDateMonths - beginDateMonths > 1){
                    layer.closeAll();
                    bootbox.alert({message:"<h3>【跨度月数不能大于1个月】</h3>"});
                }
                else{
                    return true;
                }
            }
            else{
                layer.closeAll();
                bootbox.alert({message:"<h3>【查询日期不能为空】</h3>"});
            }
            return false;
        });
        layer.closeAll();
    })(jQuery);
    
    function vaildData(obj) {
        var minInvestMoney =  $.trim($("#minInvestMoney").val());
        var maxInvestMoney = $.trim($("#maxInvestMoney").val());
        var reg = /^\d+(\.\d+)?$/;
        if(minInvestMoney != ""){
            if(!reg.test(minInvestMoney))
            {
                bootbox.alert({size: 'small',message:"累计投资金额只能输入数字！！！"});
                $("#minInvestMoney").focus();
                return false;
            }
        }
        if(maxInvestMoney != ""){
            if(!reg.test(maxInvestMoney))
            {
                bootbox.alert({size: 'small',message:"累计投资金额只能输入数字！！！"});
                $("#maxInvestMoney").focus();
                return false;
            }
        }
        return true;
    }
</script>