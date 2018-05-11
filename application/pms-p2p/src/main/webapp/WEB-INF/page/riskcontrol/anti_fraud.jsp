<%@page language="java" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>




<style type="text/css">
    /**去除bootstrap td 的边框*/
    table.account tr td {
        border: none;
    }
</style>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/cs/index">风控管理</a></li>
                <li class="active"><b>进单审核</b></li>
                <li class="active"><b>借款详情</b></li>
            </ol>

            <jsp:include page="loan_detail_common.jsp"/>


            <table class="table table-hover account table-condensed" style="margin-top: 8px;">

                <tr>

                    <td style="text-align:right;">是否通过身份信息实名验证：</td>
                    <td style="text-align:left;width: 25%">是</td>
                    <td style="text-align:right;">通过手机号查询银行短时逾期：</td>
                    <td style="text-align:left;width: 25%">${riskAntiFraud.phoneQueryBankOverdue==null || riskAntiFraud.phoneQueryBankOverdue=='' ?'未命中':riskAntiFraud.phoneQueryBankOverdue}</td>
                </tr>
                <tr>
                    <td style="text-align:right;">通过手机号查询银行失联：</td>
                    <td>${riskAntiFraud.phoneQueryBankLost==null || riskAntiFraud.phoneQueryBankLost==''? '未命中':riskAntiFraud.phoneQueryBankLost}</td>
                    <%--<td style="text-align:right;">身份证类异常（过期，非二代身份证）：</td>
                    <td>${riskAntiFraud.idQueryBankCheat==null?'未命中':riskAntiFraud.idQueryBankCheat}</td>--%>
                    <td style="text-align:right;">通过手机号查询银行欺诈：</td>
                    <td>${riskAntiFraud.phoneQueryBankCheat==null || riskAntiFraud.phoneQueryBankCheat==''?'未命中':riskAntiFraud.phoneQueryBankCheat}</td>
                </tr>
                <tr>

                    <td style="text-align:right;">通过手机号查询小贷或P2P欺诈：</td>
                    <td>${riskAntiFraud.phoneQueryP2PCheat==null || riskAntiFraud.phoneQueryP2PCheat==''? '未命中':riskAntiFraud.phoneQueryP2PCheat}</td>
                    <td style="text-align:right;">通过手机号查询电信欠费：</td>
                    <td>${riskAntiFraud.phoneQueryTelecomeOwe==null || riskAntiFraud.phoneQueryTelecomeOwe==''?'未命中':riskAntiFraud.phoneQueryTelecomeOwe}</td>
                </tr>


                   <%-- <td style="text-align:right;">身份证命中信贷逾期名单：</td>
                    <td>${riskAntiFraud.idQueryP2POverdue==null || riskAntiFraud.phoneQueryBankOverdue==''?'未命中':riskAntiFraud.idQueryP2POverdue}</td>--%>
                <td style="text-align:right;">通过手机号查询银行拒绝：</td>
                <td>${riskAntiFraud.phoneQueryBankRefuse==null || riskAntiFraud.phoneQueryBankRefuse==''?'未命中':riskAntiFraud.phoneQueryBankRefuse}</td>
                    <td style="text-align:right;">通过手机号查询小贷或P2P不良：</td>
                    <td>${riskAntiFraud.phoneQueryP2PBadness==null || riskAntiFraud.phoneQueryP2PBadness==''?'未命中':riskAntiFraud.phoneQueryP2PBadness}</td>
                </tr>
                <tr>

                    <td style="text-align:right;">通过身份证查询银行不良：</td>
                    <td>${riskAntiFraud.idQueryBankBadness==null || riskAntiFraud.idQueryBankBadness==''?'未命中':riskAntiFraud.idQueryBankBadness}</td>
                    <td style="text-align:right;">通过手机号查询小贷或P2P短时逾期：</td>
                    <td>${riskAntiFraud.phoneQueryP2POverdue==null || riskAntiFraud.phoneQueryP2POverdue==''?'未命中':riskAntiFraud.phoneQueryP2POverdue}</td>
                </tr>
                <tr>

                    <td style="text-align:right;">通过身份证查询银行短时逾期：</td>
                    <td>${riskAntiFraud.idQueryBankOverdue==null || riskAntiFraud.idQueryBankOverdue==''?'未命中':riskAntiFraud.idQueryBankOverdue}</td>
                    <td style="text-align:right;">通过手机号查询小贷或P2P失联：</td>
                    <td>${riskAntiFraud.phoneQueryP2PLost==null || riskAntiFraud.phoneQueryP2PLost==''?'未命中':riskAntiFraud.phoneQueryP2PLost}</td>
                </tr>
                <tr>

                    <td style="text-align:right;">通过身份证查询银行欺诈：</td>
                    <td>${riskAntiFraud.idQueryBankCheat==null || riskAntiFraud.idQueryBankCheat==''?'未命中':riskAntiFraud.idQueryBankCheat}</td>
                    <td style="text-align:right;">通过手机号查询小贷或P2P拒绝：</td>
                    <td>${riskAntiFraud.phoneQueryP2PRefuse==null || riskAntiFraud.phoneQueryP2PRefuse==''?'未命中':riskAntiFraud.phoneQueryP2PRefuse}</td>
                </tr>
                <tr>

                    <td style="text-align:right;">通过身份证查询银行失联：</td>
                    <td>${riskAntiFraud.idQueryBankLost==null || riskAntiFraud.idQueryBankLost==''?'未命中':riskAntiFraud.idQueryBankLost}</td>
                    <td style="text-align:right;">通过手机号查询保险骗保：</td>
                    <td>${riskAntiFraud.phoneQueryP2POwe==null || riskAntiFraud.phoneQueryP2POwe==''?'未命中':riskAntiFraud.phoneQueryP2POwe}</td>
                </tr>
                <tr>

                    <td style="text-align:right;">通过身份证查询银行拒绝：</td>
                    <td>${riskAntiFraud.idQueryBankRefuse==null || riskAntiFraud.idQueryBankRefuse==''?'未命中':riskAntiFraud.idQueryBankRefuse}</td>
                    <td style="text-align:right;">通过标识查询银行不良：</td>
                    <td>${riskAntiFraud.signQueryBankBadness==null || riskAntiFraud.signQueryBankBadness==''?'未命中':riskAntiFraud.signQueryBankBadness}</td>
                </tr>
                <tr>

                    <td style="text-align:right;">通过身份证查询资信不良：</td>
                    <td>${riskAntiFraud.idQueryCreditBadness==null || riskAntiFraud.idQueryCreditBadness==''?'未命中':riskAntiFraud.idQueryCreditBadness}</td>
                    <td style="text-align:right;">通过标识查询银行短时逾期：</td>
                    <td>${riskAntiFraud.signQueryBankOverdue==null || riskAntiFraud.signQueryBankOverdue==''?'未命中':riskAntiFraud.signQueryBankOverdue}</td>
                </tr>
                <tr>

                    <td style="text-align:right;">通过身份证查询小贷或P2P不良：</td>
                    <td>${riskAntiFraud.idQueryP2PBadness==null || riskAntiFraud.idQueryP2PBadness==''?'未命中':riskAntiFraud.idQueryP2PBadness}</td>
                    <td style="text-align:right;">通过标识查询银行欺诈：</td>
                    <td>${riskAntiFraud.signQueryBankCheat==null || riskAntiFraud.signQueryBankCheat==''?'未命中':riskAntiFraud.signQueryBankCheat}</td>
                </tr>
                <tr>

                    <td style="text-align:right;">通过身份证查询小贷或P2P短时逾期：</td>
                    <td>${riskAntiFraud.idQueryP2POverdue==null || riskAntiFraud.idQueryP2POverdue==''?'未命中':riskAntiFraud.idQueryP2POverdue}</td>
                    <td style="text-align:right;">通过标识查询银行失联：</td>
                    <td>${riskAntiFraud.signQueryBankLost==null || riskAntiFraud.signQueryBankLost==''?'未命中':riskAntiFraud.signQueryBankLost}</td>
                </tr>
                <tr>

                    <td style="text-align:right;">通过身份证查询小贷或P2P欺诈：</td>
                    <td>${riskAntiFraud.idQueryP2PCheat==null || riskAntiFraud.idQueryP2PCheat==''?'未命中':riskAntiFraud.idQueryP2PCheat}</td>
                    <td style="text-align:right;">通过标识查询银行拒绝：</td>
                    <td>${riskAntiFraud.signQueryBankRefuse==null || riskAntiFraud.signQueryBankRefuse==''?'未命中':riskAntiFraud.signQueryBankRefuse}</td>
                </tr>
                <tr>

                    <td style="text-align:right;">通过身份证查询小贷或P2P失联：</td>
                    <td>${riskAntiFraud.idQueryP2PLost==null || riskAntiFraud.idQueryP2PLost==''?'未命中':riskAntiFraud.idQueryP2PLost}</td>
                    <td style="text-align:right;">通过标识查询小贷或P2P不良：</td>
                    <td>${riskAntiFraud.signQueryP2PBadness==null || riskAntiFraud.signQueryP2PBadness==''?'未命中':riskAntiFraud.signQueryP2PBadness}</td>
                </tr>
                <tr>

                    <td style="text-align:right;">通过身份证查询小贷或P2P拒绝：</td>
                    <td>${riskAntiFraud.idQueryP2PRefuse==null || riskAntiFraud.idQueryP2PRefuse==''?'未命中':riskAntiFraud.idQueryP2PRefuse}</td>
                    <td style="text-align:right;">通过标识查询小贷或P2P短时逾期：</td>
                    <td>${riskAntiFraud.signQueryP2POverdue==null || riskAntiFraud.signQueryP2POverdue==''?'未命中':riskAntiFraud.signQueryP2POverdue}</td>
                </tr>
                <tr>

                    <td style="text-align:right;">通过身份证查询电信欠费：</td>
                    <td>${riskAntiFraud.idQueryTelecomeOwe==null || riskAntiFraud.idQueryTelecomeOwe==''?'未命中':riskAntiFraud.idQueryTelecomeOwe}</td>
                    <td style="text-align:right;">通过标识查询小贷或P2P欺诈：</td>
                    <td>${riskAntiFraud.signQueryP2PCheat==null || riskAntiFraud.signQueryP2PCheat==''?'未命中':riskAntiFraud.signQueryP2PCheat}</td>
                </tr>
                <tr>

                    <td style="text-align:right;">通过身份证查询保险骗保：</td>
                    <td>${riskAntiFraud.idQueryInsuranceCheat==null || riskAntiFraud.idQueryInsuranceCheat==''?'未命中':riskAntiFraud.idQueryInsuranceCheat}</td>
                    <td style="text-align:right;">通过标识查询小贷或P2P失联：</td>
                    <td>${riskAntiFraud.signQueryP2PLost==null || riskAntiFraud.signQueryP2PLost==''?'未命中':riskAntiFraud.signQueryP2PLost}</td>
                </tr>
                <tr>

                    <td style="text-align:right;">通过身份证查询法院失信人：</td>
                    <td>${riskAntiFraud.idQueryCourtBreak==null || riskAntiFraud.idQueryCourtBreak=='未命中'?'未命中':'命中'}</td>
                    <td style="text-align:right;">通过标识查询小贷或P2P拒绝：</td>
                    <td>${riskAntiFraud.signQueryP2PRefuse==null || riskAntiFraud.signQueryP2PRefuse==''?'未命中':riskAntiFraud.signQueryP2PRefuse}</td>
                </tr>
                <tr>

                    <td style="text-align:right;">通过身份证查询法院被执行人：</td>
                    <td>${riskAntiFraud.idQueryCourtEnforced==null || riskAntiFraud.idQueryCourtEnforced=='未命中'?'未命中':'命中'}</td>
                    <td style="text-align:right;">通过标识查询电信欠费：</td>
                    <td>${riskAntiFraud.signQueryTelecomeOwe==null || riskAntiFraud.signQueryTelecomeOwe==''?'未命中':riskAntiFraud.signQueryTelecomeOwe}</td>
                </tr>
                <tr>

                    <td style="text-align:right;">通过手机号查询银行不良：</td>
                    <td>${riskAntiFraud.phoneQueryBankBadness==null || riskAntiFraud.phoneQueryBankBadness==''?'未命中':riskAntiFraud.phoneQueryBankBadness}</td>
                    <td style="text-align:right;">通过标识查询保险骗保：</td>
                    <td>${riskAntiFraud.sighQueryInsuranceCheat==null || riskAntiFraud.sighQueryInsuranceCheat==''?'未命中':riskAntiFraud.sighQueryInsuranceCheat}</td>
                </tr>


                <tr>

            </table>
        </div>

    </div>
</div>

