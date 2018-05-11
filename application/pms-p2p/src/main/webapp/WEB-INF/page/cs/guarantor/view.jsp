<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<jsp:include page="/WEB-INF/page/inc/jquery-validation.jsp"/>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<style type="text/css">
    textarea{ resize:none; width:200px; height:50px;}
    /**去除bootstrap td 的边框*/
    table.account tr td {
        border: none;
    }
</style>
<!-- ECharts单文件引入 -->
<script type="text/javascript">

</script>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/cs/index">客服管理</a></li>
                <li class="active"><a href="${path}/cs/guarantor">账户管理</a></li>
                <li class="active"><b>查看企业信息</b></li>
            </ol>
            <div class="col-md-12">
                <div>
                    <font color="#4682b4" size="3px">企业信息</font>
                    （“统一社会信用代码”可代替“组织机构代码”、“税务登记号”、“营业执照编号”）3证可填写其中一项。
                </div>
            <div class="col-md-8">
                <table class="table table-hover account table-condensed" style="margin-top: 8px;">
                    <tr>
                        <td class="text-right">企业名称</td>
                        <td><input id="businessName" readonly="readonly" value="${bussinessInfo.businessName}"></td>
                        <td class="text-right">统一社会信用代码</td>
                        <td><input id="uniformSocialCreditCode" readonly="readonly" value="${bussinessInfo.uniformSocialCreditCode}"></td>
                    </tr>
                    <tr>
                        <td class="text-right">营业执照编号</td>
                        <td><input id="businessLicenseNumber" readonly="readonly" value="${bussinessInfo.businessLicenseNumber}"></td>
                        <td class="text-right">税务登记号</td>
                        <td><input id="taxID" readonly="readonly" value="${bussinessInfo.taxID}"></td>
                    </tr>
                    <tr>
                        <td class="text-right">组织机构代码</td>
                        <td><input id="organizationCode" readonly="readonly" value="${bussinessInfo.organizationCode}"></td>
                        <td class="text-right"></td>
                        <td></td>
                    </tr>
                </table>
            </div>
            </div>
            <div class="col-md-12">
                <div>
                <font color="#4682b4" size="3px">法人信息</font>
                </div>
            <div class="col-md-8">
                <table class="table table-hover account table-condensed" style="margin-top: 8px;">
                    <tr>
                        <td class="text-right">法人姓名</td>
                        <td><input id="corporateJurisdicalPersonalName" readonly="readonly" value="${bussinessInfo.corporateJurisdicalPersonalName}"></td>
                        <td class="text-right">法人证件类型</td>
                        <td><input id="identificationType" readonly="readonly" value="身份证"></td>
                    </tr>
                    <tr>
                        <td class="text-right">法人证件号</td>
                        <td><input id="identification" readonly="readonly" value="${bussinessInfo.identification}"></td>
                        <td class="text-right"></td>
                        <td></td>
                    </tr>
                </table>
            </div>
            </div>
            <div class="col-md-12">
                <div>
                    <font color="#4682b4" size="3px">企业联系人</font>
                </div>
                <div class="col-md-8">
                    <table class="table table-hover account table-condensed" style="margin-top: 8px;">
                        <tr class="text-left">
                            <td class="text-right">企业联系人</td>
                            <td><input id="linkman" readonly="readonly" value="${bussinessInfo.linkman}"></td>
                            <td class="text-right">联系人手机号</td>
                            <td><input id="phone" readonly="readonly" value="${bussinessInfo.phone}"></td>
                        </tr>
                    </table>
                </div>
            </div>
            <div class="col-md-12">
                <div>
                    <font color="#4682b4" size="3px">银行信息</font>
                </div>
                <div class="col-md-8">
                    <table class="table table-hover account table-condensed" style="margin-top: 8px;">
                        <tr>
                            <td class="text-right">开户银行许可证号</td>
                            <td><input id="bankLicenseNumber" readonly="readonly" value="${bussinessInfo.bankLicenseNumber}"></td>
                            <td class="text-right">企业对公账户</td>
                            <td><input id="publicAccount" readonly="readonly" value="${bussinessInfo.publicAccount}"></td>
                        </tr>
                    </table>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-15">
                    <a type="button" class="btn btn-default"
                       href="${path}/cs/guarantor">关闭</a>&nbsp;&nbsp;&nbsp;&nbsp;
                </div>
            </div>
        </div>
    </div>
</div>
<script type="application/javascript">
    $(function() {
    })
</script>