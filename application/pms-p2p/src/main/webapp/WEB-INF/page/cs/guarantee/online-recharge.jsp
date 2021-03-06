<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<link rel="stylesheet" href="/static/component/HoldOn/HoldOn.min.css">
<jsp:include page="/WEB-INF/page/inc/jquery-validation.jsp"/>
<script type="text/javascript" src="/static/component/HoldOn/HoldOn.min.js"></script>
<div class="">
    <div class="col-md-12" style="margin-top: 20px;">
        <span class="badge" style="font-size: 18px;">${title}</span>
        <form:form class="form-horizontal" id="rechargePre_form"  role="form" action=""
                   method="get"
                   commandName="rechargeForm" >
            <input name="userId" type="hidden" value="${rechargeForm.userId}" />
            <input name="rechargeWay" type="hidden" value="WEB" />
            <div class="form-group">
                <spring:bind path="account">
                    <label class="col-sm-2 control-label text-right">
                        充值账户：
                    </label>
                    <div class="col-sm-6">
                        <form:input path="account" name="account" cssClass="form-control" style="border:none;" disabled="true"/>
                    </div>
               </spring:bind>
            </div>
            <div class="form-group">
                <spring:bind path="name">
                    <label class="col-sm-2 control-label text-right">
                        账户名称：
                    </label>
                    <div class="col-sm-6">
                        <form:input name="name" path="name" cssClass="form-control" style="border:none;" disabled="true"/>
                    </div>
                </spring:bind>
            </div>
            <div class="form-group">
                <spring:bind path="bankCode">
                    <label class="col-sm-2 control-label text-right">
                        选择银行：
                    </label>
                    <div class="col-sm-6">
                        <form:select path="bankCode" id="bankCode_select" class="form-control">
                            <form:option value="">请选择银行</form:option>
                            <form:options items="${bankCodes}" itemLabel="name" itemValue="code"></form:options>
                        </form:select>
                    </div>
                    <div class="col-sm-4 help">
                    </div>
                </spring:bind>
            </div>
            <div class="form-group">
                <spring:bind path="amount">
                    <label class="col-sm-2 control-label text-right">
                        充值金额(元)：
                    </label>
                    <div class="col-sm-6">
                        <form:input name="amount" id="online_amount" path="amount" cssClass="form-control" maxlength="12"/>
                    </div>
                    <div class="col-sm-4 help">
                        <span id="errorMsg_amount"></span>
                    </div>
                </spring:bind>
            </div>
            <div class="form-group" style="margin-top: 40px;">
                <div class="col-sm-offset-2 col-sm-10">
                    <a type="button" class="btn btn-default btn-sm" href="${path}/cs/guarantee">取消</a>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <c:if test="${hasUser}">
                        <input id="recharge_btn_submit" class="btn btn-primary" value="充值" readonly/>
                    </c:if>
                    <c:if test="${!hasUser}">
                        <input class="btn btn-primary" value="充值" disabled="true" readonly/>
                    </c:if>
                </div>
            </div>
        </form:form>
    </div>
</div>
<form id="recharge_form" action="" method="post"></form>
<script type="application/javascript">
    (function ($) {
        $("#recharge_btn_submit").attr("disabled", "true"); //.removeAttr("disabled");//启用按钮
        $("#recharge_btn_submit").bind('click', function () {
            $.ajax({
                type: "post",
                async: false,
                url: "${path}/cs/guarantee/doRecharge",
                data:$('#rechargePre_form').serialize(),
                dataType: "json",
                success: function(data){
                    if (data.code != '200') {
                        bootbox.alert({message: "<h3>" + data.message + "</h3>"});
                        return;
                    }
                    $("#recharge_form").attr('action', data.data.postUrl);
                    $.each(data.data.postParams, function (index, obj) {
                        $('#recharge_form').append('<input type="hidden" name=\'' + obj.key + '\' value=\'' + obj.value + '\'/>');
                    });
                    $("#recharge_form").submit(); // 提交到新网
                },
                error: function (e) {
                    bootbox.alert({message: "<h3>系统发生未知错误，请稍后重试！</h3>"} );
                }
            });
        });
        var submitTag = false;//判断能否提交

        $("#bankCode_select").bind('change', function () {
            var bankCode = $(this).val();
            if (bankCode =='') {
                $("#recharge_btn_submit").attr("disabled", "true");

            }
            if (bankCode !='' && submitTag) {
                $("#recharge_btn_submit").removeAttr("disabled");

            }
        });
        $("input").keyup(function(){
            checkNum();
        });
        var vN = /^\d+(\.\d+)?$/
        var vD =/^0{1}([.]\d{1,2})?$|^[1-9]\d*([.]{1}[0-9]{1,2})?$/;
        var vM =/^([0-9]{1,9}|[0-9]{1,9}\.[0-9]{1,2})$/;
        //校验金额
        function checkNum(){
            $("#recharge_btn_submit").attr("disabled", "true");
            var amount = $("#online_amount").val();
            if(amount!=''){
                if(!vN.test(amount)){
                    submitTag = false;
                    $("#errorMsg_amount").html('<label style="color: red;font-size: 14px">仅支持输入数字</label>');
                }else if(amount=='0.00'||amount=='0.0'||amount=='0.'){
                    submitTag = false;
                    $("#errorMsg_amount").html('<label style="color: red;font-size: 14px">输入金额错误</label>');
                }else if(!vD.test(amount)){
                    submitTag = false;
                    $("#errorMsg_amount").html('<label style="color: red;font-size: 14px">仅录入小数点后2位</label>');
                }else if(!vM.test(amount)){
                    submitTag = false;
                    $("#errorMsg_amount").html('<label style="color: red;font-size: 14px">仅录入 0-1000,000,000之间的数字</label>');
                }else{
                    $("#errorMsg_amount").html('');
                    if (amount != '') {
                        submitTag = true;
                    }
                    if ($("#bankCode_select").val() != '' && amount != '') {
                        $("#recharge_btn_submit").removeAttr("disabled");
                    }
                }
            }
        }
    })(jQuery)
</script>



