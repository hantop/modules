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

                    <td style="text-align:right;">单位名称：</td>

                    <c:choose>
                        <c:when test="${riskWorkInfo.companyName!= null && riskWorkInfo.companyName!= ''}">
                            <td style="text-align:left;width: 50%">${riskWorkInfo.companyName}</td>
                        </c:when>
                        <c:otherwise>
                            <td style="text-align:left; width: 50%" >----</td>
                        </c:otherwise>
                    </c:choose>


                </tr>
                <tr>
                    <td style="text-align:right;">单位电话：</td>

                    <c:choose>
                        <c:when test="${riskWorkInfo.companyPhone!= null && riskWorkInfo.companyPhone!= ''}">
                            <td style="text-align:left;width: 50%">${riskWorkInfo.companyPhone}</td>
                        </c:when>
                        <c:otherwise>
                            <td style="text-align:left; width: 50%" >----</td>
                        </c:otherwise>
                    </c:choose>


                </tr>
                <tr>
                    <td style="text-align:right;">单位地址：</td>
                    <c:choose>
                        <c:when test="${riskWorkInfo.companyAddress!= null && riskWorkInfo.companyAddress!= ''}">
                            <td style="text-align:left;width: 50%">${riskWorkInfo.companyAddress}</td>
                        </c:when>
                        <c:otherwise>
                            <td style="text-align:left; width: 50%" >----</td>
                        </c:otherwise>
                    </c:choose>


                </tr>
                <tr>
                    <td style="text-align:right;">职位：</td>
                    <c:choose>
                        <c:when test="${riskWorkInfo.position!= null && riskWorkInfo.position!= ''}">
                            <td style="text-align:left;width: 50%">${riskWorkInfo.position}</td>
                        </c:when>
                        <c:otherwise>
                            <td style="text-align:left; width: 50%" >----</td>
                        </c:otherwise>
                    </c:choose>


                </tr>
                <tr>
                    <td style="text-align:right;">月收入：</td>
                    <c:choose>
                        <c:when test="${riskWorkInfo.monthlyIncome!= null  && riskWorkInfo.monthlyIncome!= ''}">
                            <td style="text-align:left;width: 50%">${riskWorkInfo.monthlyIncome}</td>
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

