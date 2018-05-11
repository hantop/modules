
<%@page language="java" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>





<div class="col-md-12">
    <table class="table table-hover account table-condensed" style="margin-top: 8px;">
        <tr>
            <td>用户姓名：${consumerBid.userName}</td>
            <td>身份证号：
                <c:if test="${fn:length(consumerBid.idNo)==15}">
                    ${fn:substring(consumerBid.idNo, 0, 2)}************ ${fn:substring(consumerBid.idNo, 11, 15)}
                </c:if>
                <c:if test="${fn:length(consumerBid.idNo)==18}">
                    ${fn:substring(consumerBid.idNo, 0, 2)}********* ${fn:substring(consumerBid.idNo, 14, 18)}
                </c:if>

            </td>
            <td>手机号码：${consumerBid.phone}</td>
            <td>银行卡号：${consumerBid.bankNo==null?'未绑卡':consumerBid.bankNo}</td>
        </tr>

        <tr>
            <td>借款金额：￥${consumerBid.loanAmount}</td>
            <td>借款期限：${consumerBid.cycle}${consumerBid.cycleType=='d'?'天':'个月'}</td>
            <td>还款方式：
                <c:choose>
                    <c:when test="${consumerBid.paybackWay=='DEBX'}">等额本息</c:when>
                    <c:when test="${consumerBid.paybackWay=='MYFX'}">每月付息,到期还本</c:when>
                    <c:when test="${consumerBid.paybackWay=='YCFQ'}">本息到期一次付清</c:when>
                    <c:when test="${consumerBid.paybackWay=='DEBJ'}">等额本金</c:when>
                    <c:otherwise>未知状态</c:otherwise>
                </c:choose>
            </td>
            <td>申请时间：<fmt:formatDate value="${consumerBid.applyTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
        </tr>





    </table>

    <table class="table table-hover account table-condensed" style="margin-top: 8px;">


        <hr/>

        <tr>
            <td>审核时间： <fmt:formatDate value="${consumerBid.auditTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>

        </tr>

        <tr>
            <td>审核结果：
                <c:if test="${consumerBid.auditStatus==0}">

                    <span style="color: blue">未审核</span>
                </c:if>
                <c:if test="${consumerBid.auditStatus==1}">

                    <span style="color: green">通过</span>
                </c:if>

                <c:if test="${consumerBid.auditStatus==2}">

                    <span style="color: red">不通过</span>
                </c:if>


            </td>
        </tr>

        <tr>
            <td>信用分：

                <c:if test="${consumerBid.creditScore>=350&&consumerBid.creditScore<550}">
                    <span style="color:red ">${consumerBid.creditScore} 信用较差</span>
                </c:if>
                <c:if test="${consumerBid.creditScore>=550&&consumerBid.creditScore<600}">
                    <span style="color: #FFB95E ">${consumerBid.creditScore} 信用一般</span>
                </c:if>
                <c:if test="${consumerBid.creditScore>=600&&consumerBid.creditScore<650}">
                    <span style="color:#3FB3FF ">${consumerBid.creditScore} 信用良好</span>
                </c:if>
                <c:if test="${consumerBid.creditScore>=650&&consumerBid.creditScore<700}">
                    <span style="color: #9BDA92 ">${consumerBid.creditScore} 信用优秀</span>
                </c:if>
                <c:if test="${consumerBid.creditScore>=700}">
                    <span style="color: #0E760D">${consumerBid.creditScore} 信用极好</span>
                </c:if>
            </td>
        </tr>

    </table>

    <hr/>

</div>
<div class="col-md-12">
    <ul class="nav nav-tabs">
        <li <c:if test="${riskBaseInfo!=null}">class="active"</c:if>><a href="${path}/riskcontrol/baseInfo?id=${consumerBid.id}">基本信息</a></li>
        <li <c:if test="${riskWorkInfo!=null}">class="active"</c:if> ><a href="${path}/riskcontrol/workInfo?id=${consumerBid.id}" >工作情况</a></li>
        <li <c:if test="${riskAntiFraud!=null}">class="active"</c:if> ><a href="${path}/riskcontrol/antiFraud?id=${consumerBid.id}" >反欺诈</a></li>
        <li <c:if test="${riskMutiBorrow!=null}">class="active"</c:if> ><a href="${path}/riskcontrol/mutiplyBorrow?id=${consumerBid.id}" >多头借贷</a></li>
    </ul>

    <br/>

</div>