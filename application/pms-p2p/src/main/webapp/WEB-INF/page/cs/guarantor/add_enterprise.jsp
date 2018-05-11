<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<link rel="stylesheet" href="/static/component/HoldOn/HoldOn.min.css">
<jsp:include page="/WEB-INF/page/inc/jquery-validation.jsp"/>
<script type="text/javascript" src="/static/component/HoldOn/HoldOn.min.js"></script>
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<style>
.error{
    color:red;
}
</style>
<div class="container">
    <div class="row">
        <div class="col-sm-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/cs/index">客服管理</a></li>
                <li class="active"><a href="${path}/cs/guarantor">账户管理</a></li>
                <li class="active"><b>企业信息</b></li>
            </ol>
            <span class="badge" style="font-size: 18px;">${title}</span>
            <form:form class="form-inline" id="enterprise_info_form" role="form" action=""
                       method="post" commandName="bussinessInfo">
                <input type="hidden" name="userId" value="${userId}"/>
                <div class="col-sm-12">
                    <div style="margin-bottom: 6px;">
                        <font color="#4682b4" size="3px">企业信息</font>
                        （“统一社会信用代码”可代替“组织机构代码”、“税务登记号”、“营业执照编号”）3证可填写其中一项。
                    </div>
                    <div class="col-sm-12" style="margin-bottom: 6px;">
                        <spring:bind path="businessName">
                            <label class="col-sm-2 control-label text-right">
                                企业名称
                            </label>
                            <div class="col-sm-2">
                                <form:input path="businessName" cssClass="form-control" maxlength="40" required="required" />
                            </div>
                        </spring:bind>
                        <spring:bind path="uniformSocialCreditCode">
                            <label class="col-sm-2 control-label text-right">
                                统一社会信用代码
                            </label>
                            <div class="col-sm-2">
                                <form:input path="uniformSocialCreditCode" id="uniformSocialCreditCodeId" cssClass="form-control" maxlength="40"/>
                            </div>
                        </spring:bind>
                    </div>
                    <div class="col-sm-12" style="margin-bottom: 6px;">
                        <spring:bind path="businessLicenseNumber">
                            <label class="col-sm-2 control-label text-right">
                                营业执照编号
                            </label>
                            <div class="col-sm-2">
                                <form:input path="businessLicenseNumber" id="businessLicenseNumberId" cssClass="form-control" maxlength="40"/>
                            </div>
                        </spring:bind>
                        <spring:bind path="taxID">
                            <label class="col-sm-2 control-label text-right">
                                税务登记号
                            </label>
                            <div class="col-sm-2">
                                <form:input path="taxID" id="taxID" cssClass="form-control" maxlength="40"/>
                            </div>
                        </spring:bind>
                    </div>
                    <div class="col-sm-12" style="margin-bottom: 6px;">
                        <spring:bind path="organizationCode">
                            <label class="col-sm-2 control-label text-right">
                                组织机构代码
                            </label>
                            <div class="col-sm-2">
                                <form:input path="organizationCode" id="organizationCodeId" cssClass="form-control" maxlength="40"/>
                            </div>
                        </spring:bind>
                        <spring:bind path="creditCode">
                            <label class="col-sm-2 control-label text-right">
                                机构信用代码
                            </label>
                            <div class="col-sm-2">
                                <form:input path="creditCode" cssClass="form-control" maxlength="40" required="required"/>
                            </div>
                        </spring:bind>
                    </div>
                </div>
                <div class="col-sm-12">
                    <div style="margin-bottom: 2px;">
                        <font color="#4682b4" size="3px">法人信息</font>
                    </div>
                    <div class="col-sm-12" style="margin-bottom: 6px;">
                        <spring:bind path="corporateJurisdicalPersonalName">
                            <label class="col-sm-2 control-label text-right">
                                法人姓名
                            </label>
                            <div class="col-sm-2">
                                <form:input path="corporateJurisdicalPersonalName" cssClass="form-control" maxlength="40" required="required"/>
                            </div>
                        </spring:bind>
                        <spring:bind path="identificationType">
                            <label class="col-sm-2 control-label text-right">
                                法人证件类型
                            </label>
                            <div class="col-sm-2">
                                <form:select path="identificationType" class="form-control" required="required">
                                    <option value="PRC_ID">身份证</option>
                                    <%--<option value="PASSPORT">护照</option>--%>
                                    <%--<option value="COMPATRIOTS_CARD">港澳台同胞回乡证</option>--%>
                                    <%--<option value="PERMANENT_RESIDENCE">外国人永久居留证</option>--%>
                                </form:select>
                            </div>
                        </spring:bind>
                    </div>
                    <div class="col-sm-12" style="margin-bottom: 6px;">
                        <spring:bind path="identification">
                            <label class="col-sm-2 control-label text-right">
                                法人证件号
                            </label>
                            <div class="col-sm-2">
                                <form:input path="identification" cssClass="form-control" maxlength="40" required="required"/>
                            </div>
                        </spring:bind>
                    </div>
                </div>
                <div class="col-sm-12">
                    <div style="margin-bottom: 2px;">
                        <font color="#4682b4" size="3px">企业联系人</font>
                    </div>
                    <div class="col-sm-12" style="margin-bottom: 6px;">
                        <spring:bind path="linkman">
                            <label class="col-sm-2 control-label text-right">
                                企业联系人
                            </label>
                            <div class="col-sm-2">
                                <form:input path="linkman" cssClass="form-control" maxlength="40" required="required"/>
                            </div>
                        </spring:bind>
                        <spring:bind path="phone">
                            <label class="col-sm-2 control-label text-right">
                                联系人号码
                            </label>
                            <div class="col-sm-2">
                                <form:input path="phone" cssClass="form-control" maxlength="40" required="required"/>
                            </div>
                        </spring:bind>
                    </div>
                </div>
                <div class="col-sm-12">
                    <div style="margin-bottom: 2px;">
                        <font color="#4682b4" size="3px">银行信息</font>
                    </div>
                    <div class="col-sm-12" style="margin-bottom: 6px;">
                        <spring:bind path="bankCode">
                            <label class="col-sm-2 control-label text-right">
                                开户银行
                            </label>
                            <div class="col-sm-2">
                                <form:select path="bankCode" class="form-control" required="required">
                                    <form:option value="">请选择银行</form:option>
                                    <form:options items="${bankCodes}" itemLabel="name" itemValue="code"></form:options>
                                </form:select>
                            </div>
                        </spring:bind>
                        <spring:bind path="bankLicenseNumber">
                            <label class="col-sm-2 control-label text-right">
                                开户银行许可证号
                            </label>
                            <div class="col-sm-2">
                                <form:input path="bankLicenseNumber" cssClass="form-control" maxlength="40" required="required"/>
                            </div>
                        </spring:bind>
                    </div>
                    <div class="col-sm-12" style="margin-bottom: 6px;">
                        <spring:bind path="publicAccount">
                            <label class="col-sm-2 control-label text-right">
                                企业对公账户
                            </label>
                            <div class="col-sm-2">
                                <form:input path="publicAccount" cssClass="form-control" maxlength="40" required="required"/>
                            </div>
                        </spring:bind>
                    </div>
                </div>
                <div class="form-group" style="margin-top: 30px;">
                    <div class="col-sm-offset-6 col-sm-10">
                       <div class="col-sm-2">
                           <a type="button" id="cancel_enterprise_btn" class="btn btn-default btn-sm" href="${path}/cs/guarantor">取消</a>
                       </div>
                        <div class="col-sm-1"></div>
                        <div class="col-sm-2">
                            <button type="button" id="save_enterprise_btn" class="btn btn-primary btn-sm">保存</button>
                        </div>
                        <div class="col-sm-1"></div>
                        <div class="col-sm-2">
                            <button type="button" id="submit_enterprise_btn" class="btn btn-primary btn-sm">保存并提交</button> <!-- submit_enterprise -->
                        </div>
                        <div class="col-sm-2">
                            <a type="button" id="close_enterprise_btn" class="btn btn-primary btn-sm" href="${path}/cs/guarantor" style="display: none">关闭</a>
                        </div>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>
<form id="enterprise_form" action="" method="post"></form>
<c:choose>
    <%--auditStatus == 'PASSED' or auditStatus == 'REFUSED' or auditStatus == 'AUDIT'--%>
    <c:when test="${auditStatus != null and auditStatus != 'WAIT'}">
        <script type="application/javascript">
            (function ($) {
                $("#cancel_enterprise_btn").hide();
                $("#save_enterprise_btn").hide();
                $("#submit_enterprise_btn").hide();
                $("#close_enterprise_btn").show();
                $('input').attr("readonly","readonly")//将input元素设置为readonly
                $('select').attr("disabled","disabled");
            })(jQuery);
        </script>
    </c:when>
</c:choose>

<script type="application/javascript">
    (function ($) {
        $("#save_enterprise_btn").bind('click', function () {
            if(!validateInput()) {
                return;
            }
            $.ajax({
                url:'${path}/cs/guarantor/new_enterprise',
                data:$('#enterprise_info_form').serialize(),
                type:"POST",
                dataType: 'json',
                success:function(data) {
                    bootbox.alert({message: "<h3>" + data.message + "</h3>"});
                },
                error: function () {

                }
            });
        });

        $("#submit_enterprise_btn").bind('click', function () {
            if(!validateInput()) {
                return;
            }
            $.ajax({
                type: "post",
                async: false,
                url: "${path}/cs/guarantor/submit_enterprise",
                data:$('#enterprise_info_form').serialize(),
                dataType: "json",
                success: function(data){
                    if (data.code != '200') {
                        bootbox.alert({message: "<h3>" + data.message + "</h3>"});
                        return;
                    }
                    $("#enterprise_form").attr('action', data.data.postUrl);
                    $.each(data.data.postParams, function (index, obj) {
                        $('#enterprise_form').append('<input type="hidden" name=\'' + obj.key + '\' value=\'' + obj.value + '\'/>');
                    });
                    $("#enterprise_form").submit(); // 提交到新网
                },
                error: function (e) {
                    console.log(e);
                }
            });
        });
        function validateInput(){
            var isPass = $("#enterprise_info_form").valid();
            var input4 = $("#organizationCodeId").val();
            var input3 = $("#taxID").val();
            var input2 = $("#businessLicenseNumberId").val();
            var input1 = $("#uniformSocialCreditCodeId").val();
            var boolean1 = (input1 == "");
            var boolean2 = (input2 == "" && input3 == "" && input4 == "")
            if (boolean1 && boolean2) {
                bootbox.alert("【统一社会信用代码】与【组织机构代码、税务登记号、营业执照编号】不能同时为空");
            } else if (boolean1 && !boolean2) {
                bootbox.alert("【组织机构代码、税务登记号、营业执照编号】不能为空");
                return false;
            }
            if ((isPass && input1 != "") || (isPass && input2 != "" && input3 != "" && input4 != "" )) {
                return true;
            }
            return false;
        };

    })(jQuery)
</script>



