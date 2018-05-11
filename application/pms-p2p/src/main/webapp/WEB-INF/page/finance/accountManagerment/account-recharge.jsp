<%@page language="java" pageEncoding="utf-8" %>account-withdraw.jsp
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
        #rechargeTab li a {
            padding: 10px 20px;
        }
        #rechargeTab span {
            float: right;
            font-size: 12px;
            padding: 15px 5px 0 0;
            color: #77aaee;
        }
        #payMode_Ebank ul {
            display: none;
            height: 380px;
            overflow: auto;
            position: absolute;
            z-index: 10;
            width:360px;
        }

        #payMode_Ebank ul li {
            border-top: none;
        }
</style>
<div class="container">
    <div class="panel panel-default recharge-box" data-left-target="#recharge" data-left-parent="#accountmanagementlist" style="min-height: 720px;margin-top: 60px;">
        <div class="panel-heading" style="height: 50px;line-height: 25px;font-size: 16px;">充值
            <span style="font-size: 12px;padding-left: 3px;color: #99aabb;">联合新网银行存管账户，保障资金安全</span>
            <span style="font-size: 12px;color: #666;float: right;">可用余额：<span style="padding: 0 5px 0 10px;color: #ff4949;font-size: 14px;">${balance}</span>元</span>
        </div>
        <ul id="rechargeTab" class="nav nav-tabs" style="margin: 20px 40px 45px;">
            <li class="payMode_Ebank"><a href="#" data-toggle="tab" aria-expanded="false">网银充值</a></li>
        </ul>
        <form id="payMode_Ebank" class="panel-body form-horizontal" method="post" action="" >
            <div class="row">

                <div class="form-group">
                    <label for="Ebank_bankCardNo" class="col-lg-2 col-lg-offset-2 control-label">选择银行</label>
                    <div class="col-lg-4">
                        <div class="form-control chooseBank">
                            请选择银行<img class="toogleImg" src="/static/image/fullDown.png" style="float: right;width: 18px;padding-top: 6px;">
                        </div>
                        <ul>
                        </ul>
                        <input type="hidden" id="Ebank_bankCardNo" value="true">
                    </div>
                    <div class="col-lg-4 tip-box Ebank-choose-tip-box">
                        <span id="errorMsgBank"  style="align-content: center;padding-top: 3px;"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="form-group">
                    <label for="Ebank_credit" class="col-lg-2 col-lg-offset-2 control-label">充值金额</label>
                    <div class="col-lg-4">
                        <input type="tel" data-tyep="Ebank_sum" class="form-control" id="Ebank_credit" placeholder="单笔充值不低于1元"
                               name="amount" >
                        <input name="accountType" id="accountType" type="hidden" value="${accountType}"/>
                        <input type="hidden" id="xwCode">
                    </div>
                    <div class="col-lg-4 tip-box Ebank-sum-tip-box">
                        <span id="errorMsg"  style="align-content: center;padding-top: 3px;"></span>
                    </div>

                </div>
            </div>
            <div class="row">
                <div class="form-group">
                    <div style="width:390px" class="col-lg-3 col-lg-offset-4">
                        <input id="rechargeButton_Ebank" class="btn btn-default btn-block" value="充值" onclick="doRecharge()" readonly/>
                    </div>
                </div>
            </div>
        </form>

    </div>
</div>

<script type="application/javascript">
    var submitTag = false;//判断能否提交
    $(document).ready(function () {
        $.ajax({
                url:'${path}/getBankList',
                type:'get',
                dataType:'json',
                success:function(data){
                    var liData = "";
                    if(data.length > 0){
                        $.each(data,function(n,v){
                            //选择银行列表
                            liData += '<li class="form-control"><img src="/static/image/banklogo/'+ v.code +'.png"><input type="hidden" class="bankCode" value="'+ v.code +'"><input type="hidden" class="xwCode" value="'+ v.xinwangCode +'"></li>'
                        })
                        $('#payMode_Ebank ul').html(liData);

                    }
                },
                error:function(e) {
                    console.log(e);
                }
        });

        //新网网银充值选择银行
        $('.chooseBank').click(function() {
            $('#payMode_Ebank ul').slideToggle();
            if($(".chooseBank .toogleImg").attr('src')== "/static/image/fullDown.png") {
                $(".chooseBank .toogleImg").attr('src',"/static/image/fullUp.png");
            }else {
                $(".chooseBank .toogleImg").attr('src',"/static/image/fullDown.png");
            }
        })
        var Ebank_bankCode = '';
        $('#payMode_Ebank ul').on('click','li',function() {
            $('.Ebank-choose-tip-box .tip-title').html("");
            Ebank_bankCode = $(this).find('.bankCode').val();
            var liData ='<img src="/static/image/banklogo/'+Ebank_bankCode+'.png"><input type="hidden" class="Ebank_bankCode" value="'+Ebank_bankCode + '">' +
                    '<img class="toogleImg" src="/static/image/fullDown.png" style="float: right;width: 18px;padding-top: 6px;">';
            $('.chooseBank').html(liData);
            $('#xwCode').val($(this).find('.xwCode').val());
            $('#payMode_Ebank ul').slideToggle();
        })

    });

    $("input").keyup(function(){
        checkNum();
    });




    //校验金额
    function checkNum(){
        console.log('checkNum')
        var vN = /^\d+(\.\d+)?$/
        var vD =/^0{1}([.]\d{1,2})?$|^[1-9]\d*([.]{1}[0-9]{1,2})?$/;
        var vM =/^([0-9]{1,9}|[0-9]{1,9}\.[0-9]{1,2})$/;
        var amount = $("#Ebank_credit").val();
        if(amount==''){
            $("#errorMsg").html('<label style="color: red;font-size: 14px">请输入充值金额</label>');
            submitTag = false;
        }else if(!vN.test(amount)){
            $("#errorMsg").html('<label style="color: red;font-size: 14px">仅支持输入数字</label>');
            submitTag = false;
        }else if(amount=='0.00'||amount=='0.0'||amount=='0.'){
            $("#errorMsg").html('<label style="color: red;font-size: 14px">提现金额错误</label>');
            submitTag = false;
        }else if(!vD.test(amount)) {
            $("#errorMsg").html('<label style="color: red;font-size: 14px">仅录入小数点后2位</label>');
            submitTag = false;
        }else if(!vM.test(amount)){
            $("#errorMsg").html('<label style="color: red;font-size: 14px">仅录入 0-1000,000,000之间的数字</label>');
            submitTag = false;
        }else{
            $("#errorMsg").html('');
            submitTag = true;
        }

    }

    function doRecharge(){
        var amount = $("#Ebank_credit").val();
        var bankcode = $("#xwCode").val();
        var accountType = $("#accountType").val();
        if(bankcode==null || bankcode==''){
            $("#errorMsgBank").html('<label style="color: red;font-size: 14px">银行不能为空！</label>');
            submitTag = false;
            return;
        }else{
            $("#errorMsgBank").html('');
        }
        if(amount==null || amount==''){
            $("#errorMsg").html('<label style="color: red;font-size: 14px">充值金额不能为空！</label>');
            submitTag = false;
            return;
        }
        if(amount=='0'){
            $("#errorMsg").html('<label style="color: red;font-size: 14px">充值金额应大于0</label>');
            submitTag = false;
            return;
        }
        console.log('doRecharge')
        console.log('submitTag:'+submitTag)
        if(submitTag){

            $.ajax({
                url: "${path}/finance/accountmanagement/recharge",
                type: 'post',
                headers:{version:"3.2.0",Authorization:"dEtOblZ0aTdmMGY3M2dNUE51SHlBZFRZOStLQ012ZUFCQkkvbUpnSjg5cUVJWS9GQkxkbER2QUJkUlQzalBMZQ"},
                data:{
                    clientType: 7,screenType: 1,deviceId:1,token:123,
                    amount:amount,bankcode:bankcode,uri:"${path}/finance/accountmanagement/rechargeSuccess",payMode:0,F03:accountType
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

    $("input").keyup(function(){
        checkNum();
    });
    var submitTag = false;//判断能否提交
    var vN = /^\d+(\.\d+)?$/
    var vD =/^0{1}([.]\d{1,2})?$|^[1-9]\d*([.]{1}[0-9]{1,2})?$/;
    var vM =/^([0-9]{1,9}|[0-9]{1,9}\.[0-9]{1,2})$/;
    //校验金额
    function checkNum(){
        var amount = $("#Ebank_credit").val();
        if(amount!=''){
            if(!vN.test(amount)){
                $("#errorMsg").html('<label style="color: red;font-size: 14px">仅支持输入数字</label>');
                submitTag = false;
            }else if(amount=='0.00'||amount=='0.0'||amount=='0.'){
                $("#errorMsg").html('<label style="color: red;font-size: 14px">充值金额错误</label>');
                submitTag = false;
            }else if(!vD.test(amount)){
                $("#errorMsg").html('<label style="color: red;font-size: 14px">仅录入小数点后2位</label>');
                submitTag = false;
            }else if(!vM.test(amount)){
                $("#errorMsg").html('<label style="color: red;font-size: 14px">仅录入 0-1000,000,000之间的数字</label>');
                submitTag = false;
            }else{
                $("#errorMsg").html('');
                submitTag = true;
            }
        }
    }
</script>