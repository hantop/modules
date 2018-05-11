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
                    <c:choose>

                        <c:when test="${riskBaseInfo.idName != null && riskBaseInfo.idName!= ''}">
                            <td style="text-align:right;">用户姓名：</td>
                            <td  style="text-align:left; width: 50%" >${riskBaseInfo.idName}</td>
                        </c:when>

                        <c:otherwise>
                            <td style="text-align:right;">用户姓名：</td>
                            <td  style="text-align:left; width: 50%" >----</td>
                        </c:otherwise>
                    </c:choose>


                </tr>
                <tr>
                    <td style="text-align:right;">身份证号：</td>
                    <td style="text-align:left; width: 50%" >
                    <c:choose>
                      <c:when test="${consumerBid.idNo != null && riskBaseInfo.idNo!= ''}">
                        <c:if test="${fn:length(consumerBid.idNo)==15}">
                            ${fn:substring(consumerBid.idNo, 0, 2)}************ ${fn:substring(consumerBid.idNo, 11, 15)}
                        </c:if>
                        <c:if test="${fn:length(consumerBid.idNo)==18}">
                            ${fn:substring(consumerBid.idNo, 0, 2)}********* ${fn:substring(consumerBid.idNo, 14, 18)}
                        </c:if>
                      </c:when>
                        <c:otherwise>
                           ----
                           </c:otherwise>
                        </c:choose>

                    </td>

                </tr>
                <tr>
                    <td style="text-align:right;">手机号：</td>
                    <c:choose>
                        <c:when test="${riskBaseInfo.phoneNum != null && riskBaseInfo.phoneNum!= ''}">
                            <td style="text-align:left; width: 50%" >${riskBaseInfo.phoneNum}</td>
                        </c:when>
                        <c:otherwise>
                            <td style="text-align:left; width: 50%" >----</td>
                        </c:otherwise>
                    </c:choose>


                </tr>
                <tr>
                    <c:choose>
                        <c:when test="${riskBaseInfo.sex != null && riskBaseInfo.sex!= ''||riskBaseInfo.sex==0}">
                            <td style="text-align:right;">性别：</td>
                            <td style="text-align:left; width: 50%" >${riskBaseInfo.sex==0?'男':''}${riskBaseInfo.sex==1?'女':''}</td>
                        </c:when>
                        <c:otherwise>
                            <td style="text-align:right;">性别：</td>
                            <td style="text-align:left; width: 50%" >----</td>
                        </c:otherwise>
                    </c:choose>




                </tr>
                <tr>


                    <td style="text-align:right;">年龄：</td>
                    <c:choose>
                        <c:when test="${riskBaseInfo.age!= null && riskBaseInfo.age!= ''}">
                            <td style="text-align:left; width: 50%" >${riskBaseInfo.age}</td>
                        </c:when>
                        <c:otherwise>
                            <td style="text-align:left; width: 50%" >----</td>
                        </c:otherwise>
                    </c:choose>


                </tr>
                <tr>
                    <td style="text-align:right;">婚育情况：</td>
                    <c:choose>
                        <c:when test="${riskBaseInfo.marryStatus!= null && riskBaseInfo.marryStatus!= ''}">
                            <td style="text-align:left; width: 50%" >${riskBaseInfo.marryStatus}</td>
                        </c:when>
                        <c:otherwise>
                            <td style="text-align:left; width: 50%" >----</td>
                        </c:otherwise>
                    </c:choose>


                </tr>
                <tr>
                    <td style="text-align:right;">身份证归属地：</td>
                    <c:choose>
                        <c:when test="${riskBaseInfo.idLocation!= null && riskBaseInfo.idLocation!= ''}">
                            <td style="text-align:left; width: 50%" >${riskBaseInfo.idLocation}</td>
                        </c:when>
                        <c:otherwise>
                            <td style="text-align:left; width: 50%" >----</td>
                        </c:otherwise>
                    </c:choose>


                </tr>
                <tr>
                    <td style="text-align:right;">居住地：</td>

                    <c:choose>
                        <c:when test="${riskBaseInfo.address!= null && riskBaseInfo.address!= ''}">
                            <td style="text-align:left; width: 50%" >${riskBaseInfo.address}</td>
                        </c:when>
                        <c:otherwise>
                            <td style="text-align:left; width: 50%" >----</td>
                        </c:otherwise>
                    </c:choose>

                </tr>
                <tr>
                    <td style="text-align:right;">QQ号：</td>
                    <c:choose>
                        <c:when test="${riskBaseInfo.qq!= null && riskBaseInfo.qq!= ''}">
                            <td style="text-align:left; width: 50%" >${riskBaseInfo.qq}</td>
                        </c:when>
                        <c:otherwise>
                            <td style="text-align:left; width: 50%" >----</td>
                        </c:otherwise>
                    </c:choose>

                </tr>
            </table>
        </div>

    </div>
</div>

