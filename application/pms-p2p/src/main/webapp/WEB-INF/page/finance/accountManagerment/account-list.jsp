<%@page language="java" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<link href="/static/component/JqueryPagination/mricode.pagination.css" rel="stylesheet">

<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<script type="text/javascript" src="/static/component/jquery-form/jquery.form.js"></script>
<script type="text/javascript" src="/static/component/JqueryPagination/mricode.pagination.js"></script>

<style>
    li {
        color: #286090;
    }
</style>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/finance/index">财务管理</a></li>
                <li class="active"><b>平台账户管理</b></li>
            </ol>
            <span id="errorMsg"  style="align-content: center"></span>
            <hr style="margin-bottom: 0;">
            <div class="panel panel-default">
            	<%--<input  id="resultCode" name="resultCode" value="${resultCode}" type="hidden"/>--%>
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>账户名称</th>
                        <th>账户余额（元）</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                            <%--XW_PLATFORM_FUNDS_TRANSFER_WLZH:新网平台总账户;
                            XW_PLATFORM_COMPENSATORY_WLZH:新网平台代偿账户;
                            XW_PLATFORM_MARKETING_WLZH:新网平台营销款账户;
                            XW_PLATFORM_PROFIT_WLZH:新网平台分润账户;
                            XW_PLATFORM_INCOME_WLZH:新网平台收入账户;
                            XW_PLATFORM_INTEREST_WLZH:新网平台派息账户;
                            XW_PLATFORM_ALTERNATIVE_RECHARGE_WLZH:新网平台代充值账户;--%>
                            <tr>
                                <td>平台总账户</td>
                                <td>${balances.XW_PLATFORM_FUNDS_TRANSFER_WLZH}</td>
                                <td>
                                    <ul class="list-inline">
                                        <shiro:hasPermission name="accountmanagement:recharge">
                                            <li><a href="${path}/finance/accountmanagement/rechargePage?accountType=XW_PLATFORM_FUNDS_TRANSFER_WLZH&balance=${balances.XW_PLATFORM_FUNDS_TRANSFER_WLZH}">充值</a></li>
                                        </shiro:hasPermission>
                                        <shiro:lacksPermission name="accountmanagement:recharge">
                                            <li>&#12288;&#12288;</li>
                                        </shiro:lacksPermission>

                                        <shiro:hasPermission name="accountmanagement:withdraw">
                                            <li><a href="${path}/finance/accountmanagement/withdrawPage?accountType=XW_PLATFORM_FUNDS_TRANSFER_WLZH&balance=${balances.XW_PLATFORM_FUNDS_TRANSFER_WLZH}&isBindBank=${isBindBanks.XW_PLATFORM_FUNDS_TRANSFER_WLZH }">提现</a></li>
                                        </shiro:hasPermission>
                                        <shiro:lacksPermission name="accountmanagement:withdraw">
                                            <li>&#12288;&#12288;</li>
                                        </shiro:lacksPermission>

                                        <shiro:hasPermission name="accountmanagement:transfer">
                                            <li><a href="${path}/finance/accountmanagement/transferPage?accountType=XW_PLATFORM_FUNDS_TRANSFER_WLZH&balance=${balances.XW_PLATFORM_FUNDS_TRANSFER_WLZH}">划拨</a></li>
                                        </shiro:hasPermission>
                                        <shiro:lacksPermission name="accountmanagement:transfer">
                                            <li>&#12288;&#12288;</li>
                                        </shiro:lacksPermission>

                                        <c:choose>
                                            <c:when test="${isBindBanks.XW_PLATFORM_FUNDS_TRANSFER_WLZH == 0}">
                                                <shiro:hasPermission name="accountmanagement:bindcard">
                                                    <li><a href="${path}/finance/accountmanagement/bindcardPage?accountType=XW_PLATFORM_FUNDS_TRANSFER_WLZH">绑定卡</a></li>
                                                </shiro:hasPermission>
                                                <shiro:lacksPermission name="accountmanagement:bindcard">
                                                    <li>&#12288;&#12288;&#12288;</li>
                                                </shiro:lacksPermission>
                                            </c:when>
                                            <c:otherwise>
                                                <shiro:hasPermission name="accountmanagement:unbind">
                                                    <li><a href ="#" onclick="unbindBankcard('XW_PLATFORM_FUNDS_TRANSFER_WLZH')" >解绑卡</a></li>
                                                </shiro:hasPermission>
                                                <shiro:lacksPermission name="accountmanagement:unbind">
                                                    <li>&#12288;&#12288;&#12288;</li>
                                                </shiro:lacksPermission>
                                            </c:otherwise>
                                        </c:choose>

                                        <shiro:hasPermission name="accountmanagement:history">
                                            <li><a href="${path}/finance/accountmanagement/tradeHistory?accountType=XW_PLATFORM_FUNDS_TRANSFER_WLZH">交易记录</a></li>
                                        </shiro:hasPermission>
                                    </ul >
                                </td>
                            </tr>
                            <tr>
                                 <td>代偿金账户</td>
                                 <td>${balances.XW_PLATFORM_COMPENSATORY_WLZH}</td>
                                 <td>
                                     <ul class="list-inline">
                                         <shiro:hasPermission name="accountmanagement:recharge">
                                             <li><a href="${path}/finance/accountmanagement/rechargePage?accountType=XW_PLATFORM_COMPENSATORY_WLZH&balance=${balances.XW_PLATFORM_COMPENSATORY_WLZH}">充值</a></li>
                                         </shiro:hasPermission>
                                         <shiro:lacksPermission name="accountmanagement:recharge">
                                             <li>&#12288;&#12288;</li>
                                         </shiro:lacksPermission>

                                         <shiro:hasPermission name="accountmanagement:withdraw">
                                            <li><a href="${path}/finance/accountmanagement/withdrawPage?accountType=XW_PLATFORM_COMPENSATORY_WLZH&balance=${balances.XW_PLATFORM_COMPENSATORY_WLZH}&isBindBank=${isBindBanks.XW_PLATFORM_COMPENSATORY_WLZH }">提现</a></li>
                                         </shiro:hasPermission>
                                         <shiro:lacksPermission name="accountmanagement:withdraw">
                                             <li>&#12288;&#12288;</li>
                                         </shiro:lacksPermission>

                                         <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</li>

                                         <c:choose>
                                             <c:when test="${isBindBanks.XW_PLATFORM_COMPENSATORY_WLZH == 0}">
                                                 <shiro:hasPermission name="accountmanagement:bindcard">
                                                    <li><a href="${path}/finance/accountmanagement/bindcardPage?accountType=XW_PLATFORM_COMPENSATORY_WLZH">绑定卡</a></li>
                                                 </shiro:hasPermission>
                                                 <shiro:lacksPermission name="accountmanagement:bindcard">
                                                     <li>&#12288;&#12288;&#12288;</li>
                                                 </shiro:lacksPermission>
                                             </c:when>
                                             <c:otherwise>
                                                 <shiro:hasPermission name="accountmanagement:unbind">
                                                    <li><a href ="javascritp:void(0);" onclick="unbindBankcard('XW_PLATFORM_COMPENSATORY_WLZH')" >解绑卡</a></li>
                                                 </shiro:hasPermission>
                                                 <shiro:lacksPermission name="accountmanagement:unbind">
                                                     <li>&#12288;&#12288;&#12288;</li>
                                                 </shiro:lacksPermission>
                                             </c:otherwise>
                                         </c:choose>
                                         <shiro:hasPermission name="accountmanagement:history">
                                            <li><a href="${path}/finance/accountmanagement/tradeHistory?accountType=XW_PLATFORM_COMPENSATORY_WLZH">交易记录</a></li>
                                         </shiro:hasPermission>
                                     </ul >
                                 </td>
                            </tr>
                            <tr>
                                <td>营销款账户</td>
                                <td>${balances.XW_PLATFORM_MARKETING_WLZH}</td>
                                <td>
                                    <ul class="list-inline">
                                        <shiro:hasPermission name="accountmanagement:recharge">
                                            <li><a href="${path}/finance/accountmanagement/rechargePage?accountType=XW_PLATFORM_MARKETING_WLZH&balance=${balances.XW_PLATFORM_MARKETING_WLZH}">充值</a></li>
                                        </shiro:hasPermission>
                                        <shiro:lacksPermission name="accountmanagement:recharge">
                                            <li>&#12288;&#12288;</li>
                                        </shiro:lacksPermission>

                                        <shiro:hasPermission name="accountmanagement:withdraw">
                                            <li><a href="${path}/finance/accountmanagement/withdrawPage?accountType=XW_PLATFORM_MARKETING_WLZH&balance=${balances.XW_PLATFORM_MARKETING_WLZH}&isBindBank=${isBindBanks.XW_PLATFORM_MARKETING_WLZH }">提现</a></li>
                                        </shiro:hasPermission>
                                        <shiro:lacksPermission name="accountmanagement:withdraw">
                                            <li>&#12288;&#12288;</li>
                                        </shiro:lacksPermission>
                                        <li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</li>
                                        <c:choose>
                                            <c:when test="${isBindBanks.XW_PLATFORM_MARKETING_WLZH == 0}">
                                                <shiro:hasPermission name="accountmanagement:bindcard">
                                                    <li><a href="${path}/finance/accountmanagement/bindcardPage?accountType=XW_PLATFORM_MARKETING_WLZH">绑定卡</a></li>
                                                </shiro:hasPermission>
                                                <shiro:lacksPermission name="accountmanagement:bindcard">
                                                    <li>&#12288;&#12288;&#12288;</li>
                                                </shiro:lacksPermission>
                                            </c:when>
                                            <c:otherwise>
                                                <shiro:hasPermission name="accountmanagement:unbind">
                                                    <li><a href ="#" onclick="unbindBankcard('XW_PLATFORM_MARKETING_WLZH')" >解绑卡</a></li>
                                                </shiro:hasPermission>
                                                <shiro:lacksPermission name="accountmanagement:unbind">
                                                    <li>&#12288;&#12288;&#12288;</li>
                                                </shiro:lacksPermission>
                                            </c:otherwise>
                                        </c:choose>
                                        <shiro:hasPermission name="accountmanagement:history">
                                            <li><a href="${path}/finance/accountmanagement/tradeHistory?accountType=XW_PLATFORM_MARKETING_WLZH">交易记录</a></li>
                                        </shiro:hasPermission>
                                    </ul >
                                </td>
                            </tr>
                            <tr>
                                <td>分润账户</td>
                                <td>${balances.XW_PLATFORM_PROFIT_WLZH}</td>
                                <td>

                                </td>
                            </tr>
                            <tr>
                                <td>收入账户</td>
                                <td>${balances.XW_PLATFORM_INCOME_WLZH}</td>
                                <td>
                                    <ul class="list-inline">
                                        <shiro:hasPermission name="accountmanagement:recharge">
                                            <li><a href="${path}/finance/accountmanagement/rechargePage?accountType=XW_PLATFORM_INCOME_WLZH&balance=${balances.XW_PLATFORM_INCOME_WLZH}">充值</a></li>
                                        </shiro:hasPermission>
                                        <shiro:lacksPermission name="accountmanagement:recharge">
                                            <li>&#12288;&#12288;</li>
                                        </shiro:lacksPermission>

                                        <shiro:hasPermission name="accountmanagement:withdraw">
                                          <li><a href="${path}/finance/accountmanagement/withdrawPage?accountType=XW_PLATFORM_INCOME_WLZH&balance=${balances.XW_PLATFORM_INCOME_WLZH}&isBindBank=${isBindBanks.XW_PLATFORM_INCOME_WLZH }">提现</a></li>
                                        </shiro:hasPermission>
                                        <shiro:lacksPermission name="accountmanagement:withdraw">
                                            <li>&#12288;&#12288;</li>
                                        </shiro:lacksPermission>

                                        <shiro:hasPermission name="accountmanagement:transfer">
                                            <li><a href="${path}/finance/accountmanagement/transferPage?accountType=XW_PLATFORM_INCOME_WLZH&balance=${balances.XW_PLATFORM_INCOME_WLZH}">划拨</a></li>
                                        </shiro:hasPermission>
                                        <shiro:lacksPermission name="accountmanagement:transfer">
                                            <li>&#12288;&#12288;</li>
                                        </shiro:lacksPermission>

                                        <c:choose>
                                            <c:when test="${isBindBanks.XW_PLATFORM_INCOME_WLZH == 0}">
                                                <shiro:hasPermission name="accountmanagement:bindcard">
                                                    <li><a href="${path}/finance/accountmanagement/bindcardPage?accountType=XW_PLATFORM_INCOME_WLZH">绑定卡</a></li>
                                                </shiro:hasPermission>
                                                <shiro:lacksPermission name="accountmanagement:bindcard">
                                                    <li>&#12288;&#12288;&#12288;</li>
                                                </shiro:lacksPermission>
                                            </c:when>
                                            <c:otherwise>
                                                <shiro:hasPermission name="accountmanagement:unbind">
                                                    <li><a href ="#" onclick="unbindBankcard('XW_PLATFORM_INCOME_WLZH')" >解绑卡</a></li>
                                                </shiro:hasPermission>
                                                <shiro:lacksPermission name="accountmanagement:unbind">
                                                    <li>&#12288;&#12288;&#12288;</li>
                                                </shiro:lacksPermission>
                                            </c:otherwise>
                                        </c:choose>
                                        <shiro:hasPermission name="accountmanagement:history">
                                            <li><a href="${path}/finance/accountmanagement/tradeHistory?accountType=XW_PLATFORM_INCOME_WLZH">交易记录</a></li>
                                        </shiro:hasPermission>
                                    </ul >
                                </td>
                            </tr>
                            <tr>
                                <td>派息账户</td>
                                <td>${balances.XW_PLATFORM_INTEREST_WLZH}</td>
                                <td>
                                    <ul class="list-inline">
                                        <shiro:hasPermission name="accountmanagement:recharge">
                                            <li><a href="${path}/finance/accountmanagement/rechargePage?accountType=XW_PLATFORM_INTEREST_WLZH&balance=${balances.XW_PLATFORM_INTEREST_WLZH}">充值</a></li>
                                        </shiro:hasPermission>
                                        <shiro:lacksPermission name="accountmanagement:recharge">
                                            <li>&#12288;&#12288;</li>
                                        </shiro:lacksPermission>

                                        <li><a href="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a></li>
                                        <li><a href="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a></li>
                                        <c:choose>
                                            <c:when test="${isBindBanks.XW_PLATFORM_INTEREST_WLZH == 0}">
                                                <shiro:hasPermission name="accountmanagement:bindcard">
                                                    <li><a href="${path}/finance/accountmanagement/bindcardPage?accountType=XW_PLATFORM_INTEREST_WLZH">绑定卡</a></li>
                                                </shiro:hasPermission>
                                                <shiro:lacksPermission name="accountmanagement:bindcard">
                                                    <li>&#12288;&#12288;&#12288;</li>
                                                </shiro:lacksPermission>
                                            </c:when>
                                            <c:otherwise>
                                                <shiro:hasPermission name="accountmanagement:unbind">
                                                    <li><a href ="#" onclick="unbindBankcard('XW_PLATFORM_INTEREST_WLZH')" >解绑卡</a></li>
                                                </shiro:hasPermission>
                                                <shiro:lacksPermission name="accountmanagement:unbind">
                                                    <li>&#12288;&#12288;&#12288;</li>
                                                </shiro:lacksPermission>
                                            </c:otherwise>
                                        </c:choose>
                                        <shiro:hasPermission name="accountmanagement:history">
                                            <li><a href="${path}/finance/accountmanagement/tradeHistory?accountType=XW_PLATFORM_INTEREST_WLZH">交易记录</a></li>
                                        </shiro:hasPermission>
                                    </ul >
                                </td>
                            </tr>
                            <tr>
                                <td>代充值账户</td>
                                <td>${balances.XW_PLATFORM_ALTERNATIVE_RECHARGE_WLZH}</td>
                                <td>
                                    <ul class="list-inline">
                                        <shiro:hasPermission name="accountmanagement:recharge">
                                            <li><a href="${path}/finance/accountmanagement/rechargePage?accountType=XW_PLATFORM_ALTERNATIVE_RECHARGE_WLZH&balance=${balances.XW_PLATFORM_ALTERNATIVE_RECHARGE_WLZH}">充值</a></li>
                                        </shiro:hasPermission>
                                        <shiro:lacksPermission name="accountmanagement:recharge">
                                            <li>&#12288;&#12288;</li>
                                        </shiro:lacksPermission>

                                        <shiro:hasPermission name="accountmanagement:withdraw">
                                            <li><a href="${path}/finance/accountmanagement/withdrawPage?accountType=XW_PLATFORM_ALTERNATIVE_RECHARGE_WLZH&balance=${balances.XW_PLATFORM_ALTERNATIVE_RECHARGE_WLZH}&isBindBank=${isBindBanks.XW_PLATFORM_ALTERNATIVE_RECHARGE_WLZH }">提现</a></li>
                                        </shiro:hasPermission>
                                        <shiro:lacksPermission name="accountmanagement:withdraw">
                                            <li>&#12288;&#12288;</li>
                                        </shiro:lacksPermission>
                                        <li><a href="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a></li>
                                        <c:choose>
                                            <c:when test="${isBindBanks.XW_PLATFORM_ALTERNATIVE_RECHARGE_WLZH == 0}">
                                                <shiro:hasPermission name="accountmanagement:bindcard">
                                                    <li><a href="${path}/finance/accountmanagement/bindcardPage?accountType=XW_PLATFORM_ALTERNATIVE_RECHARGE_WLZH">绑定卡</a></li>
                                                </shiro:hasPermission>
                                                <shiro:lacksPermission name="accountmanagement:bindcard">
                                                    <li>&#12288;&#12288;&#12288;</li>
                                                </shiro:lacksPermission>
                                            </c:when>
                                            <c:otherwise>
                                                <shiro:hasPermission name="accountmanagement:unbind">
                                                    <li><a href ="#" onclick="unbindBankcard('XW_PLATFORM_ALTERNATIVE_RECHARGE_WLZH')" >解绑卡</a></li>
                                                </shiro:hasPermission>
                                                <shiro:lacksPermission name="accountmanagement:unbind">
                                                    <li>&#12288;&#12288;&#12288;</li>
                                                </shiro:lacksPermission>
                                            </c:otherwise>
                                        </c:choose>
                                        <shiro:hasPermission name="accountmanagement:history">
                                            <li><a href="${path}/finance/accountmanagement/tradeHistory?accountType=XW_PLATFORM_ALTERNATIVE_RECHARGE_WLZH">交易记录</a></li>
                                        </shiro:hasPermission>
                                    </ul >
                                </td>
                            </tr>
                    </tbody>
                </table>
            </div>
        </div>
          </div>
       </div>     
<script type="application/javascript">
    $(function () {
    	 $.ajaxSetup({
             contentType: "application/x-www-form-urlencoded;charset=utf-8",
             cache: false,
             complete: function (data, TS) {
                 //console.info(arguments);
                 //对返回的数据data做判断，
                 //session过期的话，就location到一个页面
                 if(TS !== 'success') {
                     window.location.reload();
                 }
             }
         });
    });
    function unbindBankcard(accountType){
        console.log(1111)
        $.ajax({
            url: "${path}/finance/accountmanagement/unbindBankcard",
            type: 'post',
            data:{
                accountType:accountType,uri:"${path}/finance/accountmanagement"
            },
            dataType:"json",
            success:function(result){
                if(result.code==200){
                    var params=result.data.postParams;
                    var url= result.data.postUrl;
                    submitForm(url,params);
                }else{
                    $("#errorMsg").html('<label style="color: red;font-size: 14px">'+result.message+'</label>');
                }
            }
        });
    }


    function submitForm(url,params){
        //alert(params);alert(url);
        var form = $("<form></form>");
        form.attr('action',url);
        form.attr('method','post');
        form.attr('target', '_self');
        for(i in params){
            var input = $("<input type='text' name='"+params[i].key+"' />");
            input.attr('value',params[i].value);
            form.append(input);
        }
        document.body.appendChild(form[0]);
        form.submit();
    }
</script>
