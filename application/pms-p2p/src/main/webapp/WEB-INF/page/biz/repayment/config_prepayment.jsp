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
                <li class="active"><a href="${path}/biz/index">业务管理</a></li>
                <li class="active"><b>还款管理</b></li>
                <li class="active"><b>${isSubrogation?"提前担保代偿":"提前还款"}配置</b></li>
            </ol>



            <table class="table table-hover account table-condensed" style="margin-top: 8px;">


                <tr>
                    <td style="text-align:right;">标的名称：</td>
                    <td  style="text-align:left; width: 80%" >${title}</td>

                </tr>
                <tr>
                    <td style="text-align:right;">需还本金：</td>
                    <td  style="text-align:left; " >${data.repayBudgetVO.principal}</td>

                </tr>
                <tr>
                    <td style="text-align:right;">需还利息：</td>
                    <td  style="text-align:left; " >${data.repayBudgetVO.interest}</td>

                </tr>

                <tr>
                    <td style="text-align:right;">利息管理费：</td>
                    <td  style="text-align:left; " >${data.repayBudgetVO.interestManagementFee}</td>

                </tr>

                <tr>
                    <td style="text-align:right;">剩余服务费/总服务费：</td>
                    <td  style="text-align:left;" >${data.repayBudgetVO.serviceFeeNotPay}/${data.repayBudgetVO.serviceFee}</td>

                </tr>
                <tr>
                    <td style="text-align:right;">当期还款日：</td>
                    <td  style="text-align:left;" ><fmt:formatDate value="${repayDay}" pattern="yyyy-MM-dd"></fmt:formatDate></td>

                </tr>
                <tr>
                    <td style="text-align:right;">距离还款日：</td>
                    <td  style="text-align:left;" >${distanceRefund}</td>

                </tr>
                <tr>
                    <td style="text-align:right;">违约金收取：</td>
                    <td  style="text-align:left;" >

                        <select name="penaltyFlag" id="penaltyFlag">

                            <option value="1">是</option>
                            <option value="0">否</option>
                        </select>
                    </td>

                </tr>
                <tr>
                    <td style="text-align:right;">违约金金额：</td>
                    <td  style="text-align:left;" >

                        <select name="penaltyType" id="penaltyType">

                            <option value="0">默认方式</option>
                            <option value="1">自行设置</option>
                            <optgroup value="2" ></optgroup>

                        </select>

                        <input type="number" id="penaltyAmount" name="penaltyAmount" style="display: none"/>

                        <span id="originalPenalty">${data.repayBudgetVO.prepayPenalty}</span>
                        <span id="showZero"  style="display: none">0.00</span>
                    </td>

                </tr>

                <tr>
                    <td style="text-align:right;">违约金分成：</td>
                    <td  style="text-align:left;" >

                        <input type="number" id="penaltyDivideRate"  />%


                    </td>

                </tr>
                <tr>
                    <td style="text-align:right;">需还总金额：</td>
                    <td  style="text-align:left;" id="totalAmountTd">${data.repayBudgetVO.totalAmount}</td>

                </tr>
                <tr>
                    <td style="text-align:right;">还款账户余额：</td>
                    <td  style="text-align:left;" >${data.repayBudgetVO.balance}</td>

                </tr>

                <tr>
                    <td  style="text-align:right;"><button onclick="submitConfig()" id="searchBatn"  type="button" class="btn btn-primary btn-sm">提交</button></td>

                    <td    style="text-align:left;" ><button onclick="notConfig()" id="searchBatns"  type="button" class="btn btn-primary btn-sm" style="background-color: white;color: black;border-color: #ccc" >取消</button></td>

                </tr>



            </table>

        </div>

    </div>
</div>


<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<script type="text/javascript" src="/static/component/jquery-form/jquery.form.js"></script>
<script type="text/javascript" src="/static/component/JqueryPagination/jsrender.js"></script>
<script>
    $(function(){

        if(${data.erroCode}==500){

            alert("${data.message}");
            window.history.back();

        }

    });

    $("#penaltyFlag").change(function(){
        var opt=$("#penaltyFlag").val();
        if(opt==0){
            $("#penaltyType").attr("disabled","disabled").css("background-color","gray").val(2);
            $("#penaltyAmount").hide();
            $("#originalPenalty").hide();
            $("#showZero").show();

        }else {
            $("#penaltyType").removeAttr("disabled").css('background-color', "white").val(0);
            $("#originalPenalty").show();
            $("#showZero").hide();
        }
        updateTotalAmount();

    });

    $("#penaltyType").change(function(){
        var opt=$("#penaltyType").val();
        if(opt==0){
            $("#originalPenalty").show();
            $("#penaltyAmount").hide();
        }else{
            $("#penaltyAmount").show();
            $("#originalPenalty").hide();
        }
        updateTotalAmount();

    });


    /**
     * 输入违约金即时效果
     */
    $("#penaltyAmount").blur(function(){

        updateTotalAmount();


    });

    /**
     * 更新需还总金额
     */
    function updateTotalAmount(){
        var prepayPenalty =  ${data.repayBudgetVO.prepayPenalty};// 违约金
        var balance = ${data.repayBudgetVO.balance};//账户余额
        var totalAmount =${data.repayBudgetVO.totalAmount};//需还总额
        if($("#penaltyFlag").val()==0){
            totalAmount=totalAmount-parseFloat(prepayPenalty);
            prepayPenalty=0;
        }else {
            if($("#penaltyType").val()==1){
                totalAmount=totalAmount-prepayPenalty;
                prepayPenalty=$("#penaltyAmount").val();
                if(prepayPenalty==null||prepayPenalty==""){
                    prepayPenalty=0;
                }
                totalAmount=totalAmount+parseFloat(prepayPenalty);

            }
        }

        totalAmount=totalAmount.toFixed(2);
        $("#totalAmountTd").html(totalAmount);
    }

    function notConfig(){
        window.location.href="${path}/biz/repayment";

    }

    function submitConfig(){
        console.log("submitConfig");
        var isPreRepay =${isPreRepay};
        var isSubrogation =${isSubrogation};
        var title ='${title}';
        var repayMethod ='${repayMethod}';


        var penaltyFlag =  $("#penaltyFlag").val();
        var penaltyType =   $("#penaltyType").val();
        var penaltyAmount =   $("#penaltyAmount").val();
        var penaltyDivideRate =   $("#penaltyDivideRate").val();
        var originalPenalty =    $("#originalPenalty").text();


        if(penaltyType==1){
            if(penaltyAmount==null||penaltyAmount==""){
                alert("请输入违约金金额");
                return ;
            }

        }
        if(penaltyDivideRate==null||penaltyDivideRate==""){
            alert("请输入违约金分成");
            return ;
        }else{
            if(penaltyDivideRate>100){
                alert("违约金分成不能大于100");
                return;
            }
        }

        var prepayPenalty =  ${data.repayBudgetVO.prepayPenalty};// 违约金
        var balance = ${data.repayBudgetVO.balance};//账户余额
        var totalAmount =${data.repayBudgetVO.totalAmount};//需还总额
        if($("#penaltyFlag").val()==0){
            totalAmount=totalAmount-parseFloat(prepayPenalty);
            prepayPenalty=0;
        }else {
            if($("#penaltyType").val()==1){
                totalAmount=totalAmount-prepayPenalty;
                prepayPenalty=$("#penaltyAmount").val();
                totalAmount=totalAmount+parseFloat(prepayPenalty);

            }
        }

        totalAmount=totalAmount+parseFloat(${data.repayBudgetVO.serviceFeeNotPay});

        totalAmount=totalAmount.toFixed(2);

        if(totalAmount>balance){
            var less = totalAmount-balance;
            less = less.toFixed(2);
            alert("还款账户余额不足，还差¥" +less + "，请先充值借款账户");
            return;
        }


        bootbox.confirm({
            title: confirmTitle(isPreRepay, isSubrogation, title),
            size: 'small',
            message: confirmMsg(isPreRepay, isSubrogation, title),
            callback: function (result) {
                if(result) {
                    layer.load(0, {shade: [0.4,'#fff',false]}); //进度条
                    $.post(
                        path + "/biz/repayment/doRepay",
                        {
                            "bidId" : ${bidId},
                            "repayMethod" : repayMethod,
                            "isPreRepay" : isPreRepay,
                            "isSubrogation" : isSubrogation,
                            "penaltyFlag":penaltyFlag,
                            "penaltyType":penaltyType==null?2:penaltyType,
                            "penaltyAmount":penaltyAmount,
                            "penaltyDivideRate":penaltyDivideRate,
                            "originalPenalty":originalPenalty
                        },
                        function (data) {
                            var resultCode = data.resultCode;
                            var message = data.message;
                            if(resultCode == "1111"||resultCode == 1111) {
                                if(isPreRepay){
                                    if(isSubrogation){
                                        alert("提前担保代偿成功!");
                                    }else{
                                        alert("提前还款成功!");
                                    }
                                }else{
                                    if(isSubrogation){
                                        alert("担保代偿成功!");
                                    }else{
                                        alert("还款成功!");
                                    }
                                }
                            } else if (resultCode == "500"){
                                alert("还款失败，内部错误。");
                            } else if(message != null){
                                alert(message);
                            }
                            console.log(data);
                            window.location.href="${path}/biz/repayment";

                        }
                    );
                }
            }
        });

    }
    function confirmTitle(isPreRepay, isSubrogation, title) {
        var title = null;
        //提前还款
        if(isPreRepay){
            //担保代偿
            if(isSubrogation){
                title = "提前担保代偿:";
            }else{
                title = "提前还款:";
            }
        }
        return title;
    }

    function confirmMsg(isPreRepay, isSubrogation, title) {
        var msg = null;
        var totalAmount =  ${data.repayBudgetVO.totalAmount};// 需还总额
        var principal =  ${data.repayBudgetVO.principal};// 需还本金
        var interest =  ${data.repayBudgetVO.interest};// 需还利息
        var overduePenalty =  ${data.repayBudgetVO.overduePenalty};// 罚息
        var overdueCommission =  ${data.repayBudgetVO.overdueCommission};// 逾期手续费
        var prepayPenalty =  ${data.repayBudgetVO.prepayPenalty};// 违约金
        var balance =  ${data.repayBudgetVO.balance};// 账户余额
        var guaranteeName =  '${data.repayBudgetVO.guaranteeName}';// 担保账户
        var serviceFee = ${data.repayBudgetVO.serviceFee};//服务费
        var serviceFeeNotPay = ${data.repayBudgetVO.serviceFeeNotPay};//剩余服务费
        var penaltyFlag=$("#penaltyFlag").val()==0?'否':'是';
        if($("#penaltyFlag").val()==0){
            totalAmount=totalAmount-parseFloat(prepayPenalty);
            prepayPenalty=0;
        }else {
            if($("#penaltyType").val()==1){
                totalAmount=totalAmount-prepayPenalty;
                prepayPenalty=$("#penaltyAmount").val();
                totalAmount=totalAmount+parseFloat(prepayPenalty);

            }
        }
        totalAmount=totalAmount.toFixed(2);
        msg = "标的名称&nbsp;&nbsp;:&nbsp;&nbsp;" + title + "<br/>" +
            "需还本金&nbsp;&nbsp;:&nbsp;&nbsp;" + principal + "<br/>" +
            "需还利息&nbsp;&nbsp;:&nbsp;&nbsp;" + interest + "<br/>"+
            "剩余服务费:&nbsp;&nbsp;" + serviceFeeNotPay + "/"+serviceFee+"<br/>"+
        "违约金收取:&nbsp;&nbsp;" + penaltyFlag + "<br/>";
        var tip = null;
        //提前还款
        if(isPreRepay){
            msg = msg + "&nbsp;&nbsp;&nbsp;违约金&nbsp;&nbsp;:&nbsp;&nbsp;" + prepayPenalty + "<br/>";
            //担保代偿
            if(isSubrogation){
                msg = msg + "<br/>" + "担保账户&nbsp;&nbsp;:&nbsp;&nbsp;" + guaranteeName;
                tip = "提前代偿为整个标提前还款，不支持当期提前代偿。";
            }else{
                tip = "提前还款为整个标提前还款，不支持当期提前还款。";
            }
        }
        msg = msg +"违约金分成&nbsp;&nbsp;:&nbsp;&nbsp;" + $("#penaltyDivideRate").val()  + "%<br/>" ;
        msg = msg +"需还总额&nbsp;&nbsp;:&nbsp;&nbsp;" + totalAmount + "<br/>" ;
        msg = msg + "账户余额&nbsp;&nbsp;:&nbsp;&nbsp;" + balance + "<br/><br/>";
        if(tip != null){
            msg = msg + tip;
        }

        return msg;
    }

</script>
