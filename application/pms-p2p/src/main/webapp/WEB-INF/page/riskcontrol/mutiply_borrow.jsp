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

                    <td style="text-align:right;">1天内设备使用过多的身份证或 手机号进行申请：</td>
                    <c:choose>
                        <c:when test="${riskMutiBorrow.deviceUseMutiId1D!= null && riskMutiBorrow.deviceUseMutiId1D!= ''}">
                            <td style="text-align:left;width: 50%">${riskMutiBorrow.deviceUseMutiId1D}</td>
                        </c:when>
                        <c:otherwise>
                            <td style="text-align:left; width: 50%" >0</td>
                        </c:otherwise>
                    </c:choose>
                </tr>

                <tr>

                    <td style="text-align:right;">7天内设备使用过多的身份证 或手机号进行申请：</td>
                    <c:choose>
                        <c:when test="${riskMutiBorrow.deviceUseMutiId7D!= null && riskMutiBorrow.deviceUseMutiId1D!= ''}">
                            <td style="text-align:left;width: 50%">${riskMutiBorrow.deviceUseMutiId7D}</td>
                        </c:when>
                        <c:otherwise>
                            <td style="text-align:left; width: 50%" >0</td>
                        </c:otherwise>
                    </c:choose>

                </tr>

                <tr>

                    <td style="text-align:right;">1天内身份证使用过多设备 进行申请：</td>
                    <c:choose>
                        <c:when test="${riskMutiBorrow.idUseMutiDevice1D!= null && riskMutiBorrow.deviceUseMutiId1D!= ''}">
                            <td style="text-align:left;width: 50%">${riskMutiBorrow.idUseMutiDevice1D}</td>
                        </c:when>
                        <c:otherwise>
                            <td style="text-align:left; width: 50%" >0</td>
                        </c:otherwise>
                    </c:choose>
                </tr>
                <tr>

                    <td style="text-align:right;">7天内身份证使用过多设备 进行申请：</td>
                    <c:choose>
                        <c:when test="${riskMutiBorrow.idUseMutiDevice7D!= null && riskMutiBorrow.idUseMutiDevice7D!= ''}">
                            <td style="text-align:left;width: 50%">${riskMutiBorrow.idUseMutiDevice7D}</td>
                        </c:when>
                        <c:otherwise>
                            <td style="text-align:left; width: 50%" >0</td>
                        </c:otherwise>
                    </c:choose>


                </tr>
                <tr>

                    <td style="text-align:right;">1个月内身份证使用过多设备 进行申请：</td>
                    <c:choose>
                        <c:when test="${riskMutiBorrow.idUseMutiDevice1M!= null && riskMutiBorrow.idUseMutiDevice1M!= ''}">
                            <td style="text-align:left;width: 50%">${riskMutiBorrow.idUseMutiDevice1M}</td>
                        </c:when>
                        <c:otherwise>
                            <td style="text-align:left; width: 50%" >0</td>
                        </c:otherwise>
                    </c:choose>

                </tr>
                <tr>

                    <td style="text-align:right;">7天内申请人在多个平台 申请借款：</td>

                    <c:choose>
                        <c:when test="${riskMutiBorrow.borrowMutiPlatform7D!= null && riskMutiBorrow.borrowMutiPlatform7D!= ''}">
                            <td style="text-align:left;width: 50%">${riskMutiBorrow.borrowMutiPlatform7D}</td>
                        </c:when>
                        <c:otherwise>
                            <td style="text-align:left; width: 50%" >0</td>
                        </c:otherwise>
                    </c:choose>
                </tr>
                <tr>

                    <td style="text-align:right;">1个月内申请人在多个平台 申请借款：</td>

                    <c:choose>
                        <c:when test="${riskMutiBorrow.borrowMutiPlatform1M!= null && riskMutiBorrow.borrowMutiPlatform1M!= ''}">
                            <td style="text-align:left;width: 50%">${riskMutiBorrow.borrowMutiPlatform1M}</td>
                        </c:when>
                        <c:otherwise>
                            <td style="text-align:left; width: 50%" >0</td>
                        </c:otherwise>
                    </c:choose>

                </tr>
                <tr>

                    <td style="text-align:right;">3个月内申请人在多个平台 申请借款：</td>
                    <c:choose>
                        <c:when test="${riskMutiBorrow.borrowMutiPlatform3M!= null && riskMutiBorrow.borrowMutiPlatform3M!= ''}">
                            <td style="text-align:left;width: 50%">${riskMutiBorrow.borrowMutiPlatform3M}</td>
                        </c:when>
                        <c:otherwise>
                            <td style="text-align:left; width: 50%" >0</td>
                        </c:otherwise>
                    </c:choose>

                </tr>
                <tr>

                    <td style="text-align:right;">6个月内申请人在多个平台 申请借款：</td>
                    <c:choose>
                        <c:when test="${riskMutiBorrow.borrowMutiPlatform6M!= null && riskMutiBorrow.borrowMutiPlatform6M!= ''}">
                            <td style="text-align:left;width: 50%">${riskMutiBorrow.borrowMutiPlatform6M}</td>
                        </c:when>
                        <c:otherwise>
                            <td style="text-align:left; width: 50%" >0</td>
                        </c:otherwise>
                    </c:choose>

                </tr>
                <tr>

                    <td style="text-align:right;">12个月内申请人在多个平台 申请借款：</td>
                    <c:choose>
                        <c:when test="${riskMutiBorrow.borrowMutiPlatform12M!= null && riskMutiBorrow.borrowMutiPlatform12M!= ''}">
                            <td style="text-align:left;width: 50%">${riskMutiBorrow.borrowMutiPlatform12M}</td>
                        </c:when>
                        <c:otherwise>
                            <td style="text-align:left; width: 50%" >0</td>
                        </c:otherwise>
                    </c:choose>

                </tr>
                <tr>

                    <td style="text-align:right;">18个月内申请人在多个平台 申请借款：</td>
                    <c:choose>
                        <c:when test="${riskMutiBorrow.borrowMutiPlatform18M!= null && riskMutiBorrow.borrowMutiPlatform18M!= ''}">
                            <td style="text-align:left;width: 50%">${riskMutiBorrow.borrowMutiPlatform18M}</td>
                        </c:when>
                        <c:otherwise>
                            <td style="text-align:left; width: 50%" >0</td>
                        </c:otherwise>
                    </c:choose>

                </tr>
                <tr>

                    <td style="text-align:right;">24个月内申请人在多个平台 申请借款：</td>
                    <c:choose>
                        <c:when test="${riskMutiBorrow.borrowMutiPlatform24M!= null && riskMutiBorrow.borrowMutiPlatform24M!= ''}">
                            <td style="text-align:left;width: 50%">${riskMutiBorrow.borrowMutiPlatform24M}</td>
                        </c:when>
                        <c:otherwise>
                            <td style="text-align:left; width: 50%" >0</td>
                        </c:otherwise>
                    </c:choose>

                </tr>
                <tr>

                    <td style="text-align:right;">近60个月以上申请人在多个平台 申请借款：</td>
                    <c:choose>
                        <c:when test="${riskMutiBorrow.borrowMutiPlatform60M!= null && riskMutiBorrow.borrowMutiPlatform60M!= ''}">
                            <td style="text-align:left;width: 50%">${riskMutiBorrow.borrowMutiPlatform60M}</td>
                        </c:when>
                        <c:otherwise>
                            <td style="text-align:left; width: 50%" >0</td>
                        </c:otherwise>
                    </c:choose>

                </tr>
                <tr>

                    <td style="text-align:right;">3个月内申请人在多个平台被放款_ 不包含本合作方：</td>
                    <c:choose>
                        <c:when test="${riskMutiBorrow.loanExclude3M!= null && riskMutiBorrow.loanExclude3M!= ''}">
                            <td style="text-align:left;width: 50%">${riskMutiBorrow.loanExclude3M}</td>
                        </c:when>
                        <c:otherwise>
                            <td style="text-align:left; width: 50%" >0</td>
                        </c:otherwise>
                    </c:choose>

                </tr>
                <tr>

                    <td style="text-align:right;">3个月内申请人被本合作方放款：</td>
                    <c:choose>
                        <c:when test="${riskMutiBorrow.loanInclude3M!= null && riskMutiBorrow.loanInclude3M!= ''}">
                            <td style="text-align:left;width: 50%">${riskMutiBorrow.loanInclude3M}</td>
                        </c:when>
                        <c:otherwise>
                            <td style="text-align:left; width: 50%" >0</td>
                        </c:otherwise>
                    </c:choose>

                </tr>
                <tr>

                    <td style="text-align:right;">6个月内申请人在多个平台被放款：</td>
                    <c:choose>
                        <c:when test="${riskMutiBorrow.loanMutiPlatform6M!= null && riskMutiBorrow.loanMutiPlatform6M!= ''}">
                            <td style="text-align:left;width: 50%">${riskMutiBorrow.loanMutiPlatform6M}</td>
                        </c:when>
                        <c:otherwise>
                            <td style="text-align:left; width: 50%" >0</td>
                        </c:otherwise>
                    </c:choose>

                </tr>
                <tr>

                    <td style="text-align:right;">12个月内申请人在多个平台被放款：</td>
                    <c:choose>
                        <c:when test="${riskMutiBorrow.loanMutiPlatform12M!= null && riskMutiBorrow.loanMutiPlatform12M!= ''}">
                            <td style="text-align:left;width: 50%">${riskMutiBorrow.loanMutiPlatform12M}</td>
                        </c:when>
                        <c:otherwise>
                            <td style="text-align:left; width: 50%" >0</td>
                        </c:otherwise>
                    </c:choose>

                </tr>
                <tr>

                    <td style="text-align:right;">18个月内申请人在多个平台被放款：</td>
                    <c:choose>
                        <c:when test="${riskMutiBorrow.loanMutiPlatform18M!= null && riskMutiBorrow.loanMutiPlatform18M!= ''}">
                            <td style="text-align:left;width: 50%">${riskMutiBorrow.loanMutiPlatform18M}</td>
                        </c:when>
                        <c:otherwise>
                            <td style="text-align:left; width: 50%" >0</td>
                        </c:otherwise>
                    </c:choose>

                </tr>
                <tr>

                    <td style="text-align:right;">24个月内申请人在多个平台被放款：</td>
                    <c:choose>
                        <c:when test="${riskMutiBorrow.loanMutiPlatform24M!= null && riskMutiBorrow.loanMutiPlatform24M!= ''}">
                            <td style="text-align:left;width: 50%">${riskMutiBorrow.loanMutiPlatform24M}</td>
                        </c:when>
                        <c:otherwise>
                            <td style="text-align:left; width: 50%" >0</td>
                        </c:otherwise>
                    </c:choose>

                </tr>
                <tr>

                    <td style="text-align:right;">近60个月以上申请人在多个 平台被放款：</td>
                    <c:choose>
                        <c:when test="${riskMutiBorrow.loanMutiPlatform60M!= null && riskMutiBorrow.loanMutiPlatform60M!= ''}">
                            <td style="text-align:left;width: 50%">${riskMutiBorrow.loanMutiPlatform60M}</td>
                        </c:when>
                        <c:otherwise>
                            <td style="text-align:left; width: 50%" >0</td>
                        </c:otherwise>
                    </c:choose>

                </tr>


            </table>
        </div>

    </div>
</div>

