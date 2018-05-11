<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<style>
        .form-horizontal .control-label {
            padding-top: 1px;
            margin-bottom: 0;
            text-align: -webkit-center;
        }
</style>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <span class="badge" style="font-size: 18px;">提现</span>
            <form id="dataform" role="form" class="panel-body form-horizontal"  action="">
                    <input type="hidden" id="accountType" value="${accountType}"/>
                <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                        提现账户：
                    </label>
                    <div class="col-sm-8">
                        <label class="col-sm-2 control-label">
                            <c:if test="${accountType=='XW_PLATFORM_FUNDS_TRANSFER_WLZH'}">平台总账户</c:if>
                            <c:if test="${accountType=='XW_PLATFORM_COMPENSATORY_WLZH'}">平台代偿账户</c:if>
                            <c:if test="${accountType=='XW_PLATFORM_MARKETING_WLZH'}">平台营销款账户</c:if>
                            <c:if test="${accountType=='XW_PLATFORM_PROFIT_WLZH'}">平台分润账户</c:if>
                            <c:if test="${accountType=='XW_PLATFORM_INCOME_WLZH'}">平台收入账户</c:if>
                            <c:if test="${accountType=='XW_PLATFORM_INTEREST_WLZH'}">平台派息账户</c:if>
                            <c:if test="${accountType=='XW_PLATFORM_ALTERNATIVE_RECHARGE_WLZH'}">平台代充值账户</c:if>
                        </label>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                        提现金额：
                    </label>
                    <div class="col-sm-2 help">
                        <c:if test="${isBindBank==0}">
                            <input id="amount" class="form-control input-sm"  readonly="readonly"/>
                        </c:if>
                        <c:if test="${isBindBank==1}">
                            <input id="amount" class="form-control input-sm"  />
                        </c:if>
                    </div>
                    <span id="errorMsg"  style="align-content: center"></span>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                        账户余额：
                    </label>
                    <div class="col-sm-2 help">
                        ${balance}
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <c:if test="${isBindBank==0}">
                            <button type="button" class="btn btn-primary btn-sm" onclick="doWithdraw()" disabled="disabled">提现</button>
                        </c:if>
                        <c:if test="${isBindBank==1}">
                            <button type="button" class="btn btn-primary btn-sm" onclick="doWithdraw()">提现</button>
                        </c:if>
                        <a type="button" class="btn btn-default btn-sm" href="${path}/finance/accountmanagement">返回</a>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

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
    var isBindBank = true;

    if('${isBindBank==0}'=='true'){
        $("#errorMsg").html('<label style="color: red;font-size: 14px">未绑定银行卡</label>');
        submitTag = false;
        isBindBank = false;
    }
    $("input").keyup(function(){
        checkNum();
    });
    var submitTag = false;//判断能否提交
    var vN = /^\d+(\.\d+)?$/
    var vD =/^0{1}([.]\d{1,2})?$|^[1-9]\d*([.]{1}[0-9]{1,2})?$/;
    var vM =/^([0-9]{1,9}|[0-9]{1,9}\.[0-9]{1,2})$/;
    //校验金额
    function checkNum(){
        var amount = $("#amount").val();
        var balance = '${balance}';
        if(isBindBank==true && amount!=''){
            if(!vN.test(amount)){
                $("#errorMsg").html('<label style="color: red;font-size: 14px">仅支持输入数字</label>');
                submitTag = false;
            }else if(amount=='0.00'||amount=='0.0'||amount=='0.'){
                $("#errorMsg").html('<label style="color: red;font-size: 14px">提现金额错误</label>');
                submitTag = false;
            }else if(!vD.test(amount)){
                $("#errorMsg").html('<label style="color: red;font-size: 14px">仅录入小数点后2位</label>');
                submitTag = false;
            }else if(!vM.test(amount)){
                $("#errorMsg").html('<label style="color: red;font-size: 14px">仅录入 0-1000,000,000之间的数字</label>');
                submitTag = false;
            }else if(parseInt(amount)>balance){
                $("#errorMsg").html('<label style="color: red;font-size: 14px">提现金额不能大于余额</label>');
                submitTag = false;
            }else{
                $("#errorMsg").html('');
                submitTag = true;
            }
        }
    }
    function doWithdraw(){
        var amount = $("#amount").val();
        if(amount==null || amount==''){
            $("#errorMsg").html('<label style="color: red;font-size: 14px">提现金额不能为空！</label>');
            submitTag = false;
            return;
        }
        if(submitTag){
            var accountType = $("#accountType").val();
            var amount = $("#amount").val();
            $.ajax({
                url: "${path}/finance/accountmanagement/withdraw",
                type: 'post',
                headers:{version:"3.2.0",Authorization:"dEtOblZ0aTdmMGY3M2dNUE51SHlBZFRZOStLQ012ZUFCQkkvbUpnSjg5cUVJWS9GQkxkbER2QUJkUlQzalBMZQ"},
                data:{
                    clientType: 7,screenType: 1,deviceId:1,token:123,
                    amount:amount,bankCode:"PCBC",uri:"${path}/finance/accountmanagement/withdrawSuccess",payMode:0,accountType:accountType
                },
                dataType:"json",
                success:function(result){
                    if(result.code==200){
                        var params=result.data.postParams;
                        var url= result.data.postUrl;
                        submitForm(url,params);
                    }else{
                        $("#errorMsg").html('<label style="color: red;font-size: 14px">'+result.message+'</label>');
                    }
                }
            });
        }

    }
    function submitForm(url,params){
        //alert(params);alert(url);
        var form = $("<form></form>");
        form.attr('action',url);
        form.attr('method','post');
        form.attr('target', '_self');
        for(i in params){
            var input = $("<input type='text' name='"+params[i].key+"' />");
            input.attr('value',params[i].value);
            form.append(input);
        }
        document.body.appendChild(form[0]);
        form.submit();
    }
    (function ($) {
    })(jQuery)
</script>