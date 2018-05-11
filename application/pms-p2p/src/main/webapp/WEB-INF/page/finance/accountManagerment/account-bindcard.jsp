<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<link rel="stylesheet" href="/static/component/HoldOn/HoldOn.min.css">
<jsp:include page="/WEB-INF/page/inc/jquery-validation.jsp"/>
<script type="text/javascript" src="/static/component/HoldOn/HoldOn.min.js"></script>
<div class="container">
    <div class="row">
        <ol class="breadcrumb">
            <li class="active"><a href="">财务管理</a></li>
            <li class="active"><a href="">平台账户管理</a></li>
            <li class="active"><b>绑卡</b></li>
        </ol>
        <div class="" style="margin-top: 20px;margin-left: 80px;">
            <span id="errorMsg"  style="align-content: center"></span>
            <form:form class="form" id="bindInfo_form"  role="form" action=""
                       method="post"
                       commandName="bindInfo" >
                <input type="hidden" id="accountType" value="${accountType}"/>
                <div class="form-group">
                    <label for="bankCode_select_id">
                        开户银行
                    </label>
                    <form:select path="bankCode" id="bankCode_select_id" class="form-control" style="width:240px;">
                        <form:option value="">请选择</form:option>
                        <form:options items="${bankCodes}" itemLabel="name" itemValue="code"></form:options>
                    </form:select>
                </div>
                <div class="form-group">
                    <label >
                        企业对公账户
                    </label><br>
                    <input id="bankcardNo"  class="form-control input-sm"  maxlength="40"  style="width:240px;"/> <%-- input-sm--%>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-1 col-sm-6"  style="margin-top: 12px;">
                        <a type="button" class="btn btn-default btn-sm" href="${path}/finance/accountmanagement">取消</a>
                        <input type="button" id="submit_bindBank_form" class="btn btn-primary" value="提交" readonly/>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>
<form id="bind_enpcard_form" action="" method="post"></form>
<script type="application/javascript">
    document.onkeydown = function(event) {
        var target, code, tag;
        if (!event) {
            event = window.event; //针对ie浏览器
            target = event.srcElement;
            code = event.keyCode;
            if (code == 13) {
                tag = target.tagName;
                if (tag == "TEXTAREA") { return true; }
                else { return false; }
            }
        }
        else {
            target = event.target; //针对遵循w3c标准的浏览器，如Firefox
            code = event.keyCode;
            if (code == 13) {
                tag = target.tagName;
                if (tag == "INPUT") { return false; }
                else { return true; }
            }
        }
    };
    (function ($) {
        $("#submit_bindBank_form").bind('click', function () {
            var accountType = $("#accountType").val();
            var bankCode = $("#bankCode_select_id option:selected").val();
            var bankcardNo = $("#bankcardNo").val();
            var vBankCode = /([\d]{4})([\d]{4})([\d]{4})([\d]{4})([\d]{0,})?/;
            if(bankCode==null || bankCode==''){
                $("#errorMsg").html('<label style="color: red;font-size: 14px">开户银行不能为空！</label>');
            }else if(bankcardNo==null || bankcardNo==''){
                $("#errorMsg").html('<label style="color: red;font-size: 14px">银行卡不能为空！</label>');
            }else if(!vBankCode.test(bankcardNo)){
                $("#errorMsg").html('<label style="color: red;font-size: 14px">银行卡输入错误！</label>');
            }else{
                $.ajax({
                    type: "post",
                    async: false,
                    url: '${path}/finance/accountmanagement/bindBank',
                    data: {accountType:accountType,bankCode:bankCode,bankcardNo:bankcardNo,redirectUrl:'${path}/finance/accountmanagement'},
                    dataType: "json",
                    success: function(data){
                        if (data.code == '200') {
                            $("#bind_enpcard_form").attr('action', data.data.postUrl);
                            $.each(data.data.postParams, function (index, obj) {
                                $('#bind_enpcard_form').append('<input type="hidden" name=\'' + obj.key + '\' value=\'' + obj.value + '\'/>');
                            });
                            $("#bind_enpcard_form").submit(); // 提交到新网
                        }else{
                            $("#errorMsg").html('<label style="color: red;font-size: 14px">'+data.message+'</label>');
                        }


                    },
                    error: function (e) {
                        console.log(e);
                    }
                });
            }

        })
    })(jQuery);
</script>



