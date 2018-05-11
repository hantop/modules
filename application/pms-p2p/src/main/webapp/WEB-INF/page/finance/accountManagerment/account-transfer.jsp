<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
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
            <span class="badge" style="font-size: 18px;">划拨</span>
            <span id="errorMsg"  style="align-content: center"></span>
            <form:form id="dataform" role="form" class="form-horizontal"  action="">
                    <input type="hidden" id="sourceAccountType" value="${accountType}"/>
                <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                        划出账户：
                    </label>
                    <div class="col-sm-2 help">
                        <c:choose>
                            <c:when test="${accountType == 'XW_PLATFORM_FUNDS_TRANSFER_WLZH'}">
                                平台总账户
                            </c:when>
                            <c:when test="${accountType == 'XW_PLATFORM_INCOME_WLZH'}">
                                平台收入账户
                            </c:when>
                            <c:otherwise>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                        划入账户：
                    </label>
                    <div class="col-sm-2 help">
                        <select id="targetAccountType" name="account" class="form-control input-sm" >
                            <option value="" selected>请选择</option>
                            <option value="XW_PLATFORM_COMPENSATORY_WLZH" >平台代偿账户</option>
                            <option value="XW_PLATFORM_MARKETING_WLZH" >平台营销款账户</option>
                            <option value="XW_PLATFORM_PROFIT_WLZH" >平台分润账户</option>
                            <option value="XW_PLATFORM_INTEREST_WLZH" >平台派息账户</option>
                            <option value="XW_PLATFORM_ALTERNATIVE_RECHARGE_WLZH" >平台代充值账户</option>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                        划拨金额：
                    </label>
                    <div class="col-sm-2 help">
                        <input id="amount" class="form-control input-sm" />
                    </div>
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
                        <button type="button" class="btn btn-primary btn-sm" onclick="doFundsTransfer()">划拨</button>
                        <a type="button" class="btn btn-default btn-sm" href="${path}/finance/accountmanagement">返回</a>
                    </div>
                </div>
            </form:form>
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
    $("input").keyup(function(){
        checkNum();
    });
    var balance = '${balance}';
    var submitTag = true;//判断能否提交
    var vN = /^\d+(\.\d+)?$/
    var vD =/^0{1}([.]\d{1,2})?$|^[1-9]\d*([.]{1}[0-9]{1,2})?$/;
    var vM =/^([0-9]{1,9}|[0-9]{1,9}\.[0-9]{1,2})$/;
    //校验金额
    function checkNum(){
        var amount = $("#amount").val();
        if(amount!=''){
            if(!vN.test(amount)){
                $("#errorMsg").html('<label style="color: red;font-size: 14px">仅支持输入数字</label>');
                submitTag = false;
            }else if(amount=='0.00'||amount=='0.0'||amount=='0.'){
                $("#errorMsg").html('<label style="color: red;font-size: 14px">划拨金额错误</label>');
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
    function doFundsTransfer(){
        checkNum();
        var sourceAccountType = $("#sourceAccountType").val();
        var targetAccountType = $("#targetAccountType").val();
        var amount = $("#amount").val();
        if(targetAccountType==null || targetAccountType==''){
            $("#errorMsg").html('<label style="color: red;font-size: 14px">划入账户不能为空！</label>');
            submitTag = false;
            return;
        }
        if(amount==null || amount==''){
            $("#errorMsg").html('<label style="color: red;font-size: 14px">划拨金额不能为空！</label>');
            submitTag = false;
            return;
        }
        if(amount=='0'){
            $("#errorMsg").html('<label style="color: red;font-size: 14px">划拨金额应大于0</label>');
            submitTag = false;
            return;
        }

        if(submitTag){
            bootbox.confirm({
                title: '平台划拨',
                size: 'small',
                message: confirmMsg(sourceAccountType, targetAccountType, amount),
                callback: function (result) {
                    if (result) {
                        $.ajax({
                            url: "${path}/finance/accountmanagement/transfer",
                            type: 'post',
                            headers:{version:"3.2.0",Authorization:"dEtOblZ0aTdmMGY3M2dNUE51SHlBZFRZOStLQ012ZUFCQkkvbUpnSjg5cUVJWS9GQkxkbER2QUJkUlQzalBMZQ"},
                            data:{
                                sourceAccountType: sourceAccountType,targetAccountType: targetAccountType,amount:amount
                            },
                            dataType:"json",
                            success:function(result){
                                if(result.code==200){
                                    window.location.href="${path}/finance/accountmanagement";
                                }
                            }
                        });
                    }
                }
            });
            return false;

        }

    }
    function confirmMsg(sourceAccountType, targetAccountType, amount) {
        var msg = null;
        var accountType = null;
        if(sourceAccountType == 'XW_PLATFORM_FUNDS_TRANSFER_WLZH'){
            sourceAccountType = '平台总账户'
        }else if(sourceAccountType == 'XW_PLATFORM_INCOME_WLZH'){
            sourceAccountType = '平台收入账户'
        }

        if(targetAccountType == 'XW_PLATFORM_COMPENSATORY_WLZH'){
            targetAccountType = '平台代偿账户'
        }else if(targetAccountType == 'XW_PLATFORM_MARKETING_WLZH'){
            targetAccountType = '平台营销款账户'
        }else if(targetAccountType == 'XW_PLATFORM_PROFIT_WLZH'){
            targetAccountType = '平台分润账户'
        }else if(targetAccountType == 'XW_PLATFORM_INTEREST_WLZH'){
            targetAccountType = '平台派息账户'
        }else if(targetAccountType == 'XW_PLATFORM_ALTERNATIVE_RECHARGE_WLZH'){
            targetAccountType = '平台代充值账户'
        }

        msg = "划出账户:&nbsp;&nbsp;" + sourceAccountType + "<br/>" + "划入账户:&nbsp;&nbsp;" + targetAccountType + "<br/>"
            + "划拨金额:&nbsp;&nbsp;" + "￥" +  amount + "<br/>";
        return msg;
    }
    (function ($) {
    })(jQuery)
</script>