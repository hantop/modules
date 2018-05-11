
<%@page language="java" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>





<div class="col-md-12">
    <table class="table table-hover account table-condensed" style="margin-top: 8px;">
        <tr>
            <td  style="text-align:right;">手机号：</td>
            <td>${userDetailInfo.phoneNum}</td>
            <td  style="text-align:right;">姓名：</td>
            <td>${userDetailInfo.name}</td>
            <td  style="text-align:right;">身份证号码：</td>
            <td>${userDetailInfo.idcard}</td>
            <td  style="text-align:right;">银行卡号：</td>
            <td>${userDetailInfo.bankcardNum}</td>
        </tr>
        <tr>
            <td  style="text-align:right;">账号余额（元）：</td>
            <td>
                <c:choose>
                    <c:when test="${userDetailInfo.balance == -1414}">
                        未开户
                    </c:when>
                    <c:otherwise>
                        ${userDetailInfo.balance}
                    </c:otherwise>
                </c:choose>
            </td>
            <td  style="text-align:right;">在投金额（元）：</td>
            <td>
                <c:choose>
                    <c:when test="${userDetailInfo.investingMoney == -1414}">
                        未开户
                    </c:when>
                    <c:otherwise>
                        ${userDetailInfo.investingMoney}
                    </c:otherwise>
                </c:choose>
            </td>
            <td  style="text-align:right;">提现冻结金额：</td>
            <td>
                <c:choose>
                    <c:when test="${userDetailInfo.withdrawBlockAmount == -1414}">
                        未开户
                    </c:when>
                    <c:otherwise>
                        ${userDetailInfo.withdrawBlockAmount}
                    </c:otherwise>
                </c:choose>
            </td>
            <td  style="text-align:right;">投资冻结金额：</td>
            <td>
                <c:choose>
                    <c:when test="${userDetailInfo.investorBlockAmount == -1414}">
                        未开户
                    </c:when>
                    <c:otherwise>
                        ${userDetailInfo.investorBlockAmount}
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <td  style="text-align:right;">已收利息：</td>
            <td>
                <c:choose>
                    <c:when test="${userDetailInfo.interestReceivedAmount == -1414}">
                        未开户
                    </c:when>
                    <c:otherwise>
                        ${userDetailInfo.interestReceivedAmount}
                    </c:otherwise>
                </c:choose>
            </td>
            <td  style="text-align:right;">待收利息：</td>
            <td>
                <c:choose>
                    <c:when test="${userDetailInfo.interestingAmount == -1414}">
                        未开户
                    </c:when>
                    <c:otherwise>
                        ${userDetailInfo.interestingAmount}
                    </c:otherwise>
                </c:choose>
            </td>
            <td  style="text-align:right;"></td>
            <td></td>
            <td  style="text-align:right;"></td>
            <td></td>
        </tr>
    </table>

</div>
<div class="col-md-12">
    <ul class="nav nav-tabs">
        <li <c:if test="${getInvestList}">class="active"</c:if> ><a href="${path}/cs/investUser/getInvestList?userId=${userId}">投标记录</a></li>
        <li <c:if test="${getPlanList}">class="active"</c:if> ><a href="${path}/cs/investUser/getPlanList?userId=${userId}" >计划记录</a></li>
        <li <c:if test="${getTransactionList}">class="active"</c:if> ><a href="${path}/cs/investUser/getTransactionList?userId=${userId}">交易流水</a></li>
    </ul>

    <br/>

</div>