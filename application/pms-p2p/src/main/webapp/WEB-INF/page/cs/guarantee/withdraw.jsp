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
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/cs/index">客户管理</a></li>
                <li class="active"><a href="${path}/cs/guarantee">担保用户管理</a></li>
                <li class="active"><b>担保用户提现</b></li>
            </ol>
        </div>
        <div class="col-md-12" style="margin-top: 20px;">
            <span class="badge" style="font-size: 18px;">${title}</span>
            <form:form class="form-horizontal" id="withdrawPre_form"  role="form" action=""
                       method="get"
                       commandName="withdrawForm" >
                <input name="userId" type="hidden" value="${withdrawForm.userId}" />
                <input name="withdrawType" type="hidden" value="URGENT" /> <!-- ，NORMAL 表示普通T1，URGENT 表示加急T0，NORMAL_URGENT
                表示智能T0; -->
                <div class="form-group">
                    <spring:bind path="account">
                        <label class="col-sm-2 control-label text-right">
                            提现账户：
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
                    <spring:bind path="amount">
                        <label class="col-sm-2 control-label text-right">
                            提现金额(元)：
                        </label>
                        <div class="col-sm-6">
                            <form:input name="amount" id="withdraw_amount" path="amount" cssClass="form-control" maxlength="9"/>
                        </div>
                        <div class="col-sm-4 help">
                            <span id="withdraw_errorMsg_amount"></span>
                        </div>
                    </spring:bind>
                </div>
                <div class="form-group">
                    <spring:bind path="amount">
                        <label class="col-sm-2 control-label text-right">
                            账户余额(元)：
                        </label>
                        <div class="col-sm-6">
                            <label><span style="display: inline-block; margin-top: 8px;">￥${balance}</span></label>
                        </div>
                        <div class="col-sm-4 help">
                        </div>
                    </spring:bind>
                </div>
                <div class="form-group" style="margin-top: 40px;">
                    <div class="col-sm-offset-2 col-sm-10">
                        <a type="button" class="btn btn-default btn-sm" href="${path}/cs/guarantee">取消</a>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <c:if test="${hasUser}">
                            <input id="withdraw_btn_submit" class="btn btn-primary" value="提现" readonly/>
                        </c:if>
                        <c:if test="${!hasUser}">
                            <input class="btn btn-primary" value="提现" disabled="true" readonly/>
                        </c:if>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>
<c:choose>
    <c:when test="${!hasUser}">
        <!-- Modal -->
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
            <div class="modal-dialog modal-sm" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title">
                            提示
                        </h4>
                    </div>
                    <div class="modal-body">
                        没有此用户信息
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">确定</button>
                    </div>
                </div>
            </div>
        </div>
        <script type="application/javascript">
            (function ($) {
                $("#myModal").modal({
                    keyboard: true,
                    show: true,
                    backdrop: 'static'
                });
            })(jQuery);
        </script>
    </c:when>
</c:choose>
<form id="withdraw_form" action="" method="post"></form>

<script type="application/javascript">
    (function ($) {
        $("#withdraw_btn_submit").bind('click', function () {
            $.ajax({
                type: "post",
                async: false,
                url: "${path}/cs/guarantee/doWithdraw",
                data:$('#withdrawPre_form').serialize(),
                dataType: "json",
                success: function(data){
                    if (data.code != '200') {
                        bootbox.alert({message: "<h3>" + data.message + "</h3>"});
                        return;
                    }
                    $("#withdraw_form").attr('action', data.data.postUrl);
                    $.each(data.data.postParams, function (index, obj) {
                        $('#withdraw_form').append('<input type="hidden" name=\'' + obj.key + '\' value=\'' + obj.value + '\'/>');
                    });
                    $("#withdraw_form").submit(); // 提交到新网
                },
                error: function (e) {
                    bootbox.alert({message: "<h3>系统发生未知错误，请稍后重试！</h3>"} );
                }
            });
        });
        $("#withdraw_btn_submit").attr("disabled", "true");
        $("input").keyup(function(){
            checkNum();
        });
        var vN = /^\d+(\.\d+)?$/
        var vD =/^0{1}([.]\d{1,2})?$|^[1-9]\d*([.]{1}[0-9]{1,2})?$/;
        var vM =/^([0-9]{1,9}|[0-9]{1,9}\.[0-9]{1,2})$/;
        //校验金额
        function checkNum(){
            $("#withdraw_btn_submit").attr("disabled", "true");
            var amount = $("#withdraw_amount").val();
            if(amount!=''){
                if(!vN.test(amount)){
                    $("#withdraw_errorMsg_amount").html('<label style="color: red;font-size: 14px">仅支持输入数字</label>');
                }else if(amount=='0.00'||amount=='0.0'||amount=='0.'){
                    $("#withdraw_errorMsg_amount").html('<label style="color: red;font-size: 14px">输入金额错误</label>');
                }else if(!vD.test(amount)){
                    $("#withdraw_errorMsg_amount").html('<label style="color: red;font-size: 14px">仅录入小数点后2位</label>');
                }else if(!vM.test(amount)){
                    $("#withdraw_errorMsg_amount").html('<label style="color: red;font-size: 14px">仅录入 0-1000,000,000之间的数字</label>');
                }else{
                    $("#withdraw_errorMsg_amount").html('');
                    if (amount != '') {
                        $("#withdraw_btn_submit").removeAttr("disabled");
                    }
                }
            }
        }
    })(jQuery)
</script>



