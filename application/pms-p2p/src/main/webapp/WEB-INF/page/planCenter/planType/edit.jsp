<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<jsp:include page="/WEB-INF/page/inc/jquery-validation.jsp"/>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<style type="text/css">
    textarea{ resize:none; width:200px; height:50px;}
</style>
<!-- ECharts单文件引入 -->
<script src="/static/component/echarts/echarts.js"></script>
<script type="text/javascript">

</script>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/planCenter/index">计划中心</a></li>
                <li class="active"><a href="${path}/planCenter/planType/index">模板管理</a></li>
                <li class="active"><b>修改</b></li>
            </ol>
            <span class="badge" style="font-size: 18px;">${title}</span><br/>
            <form:form class="form-horizontal" role="form" action="${path}/planCenter/planType/edit"
                       method="post" modelAttribute="planType">
                <input type="hidden" name="flag" id="flag" />
                <form:hidden path="id"/>
                <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                        <b class="text-danger">*</b>组合类型：
                    </label>
                    <div class="col-sm-6">
                        <input type="text" style="border: none;" value="消费贷组合">
                    </div>
                </div>
                <spring:bind path="title">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            <b class="text-danger">*</b>组合名称：
                        </label>
                        <div class="col-sm-6">
                            <form:input path="title" cssClass="form-control" maxlength="15" type="text"/>
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="title" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">输入合法的组合名称</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                <spring:bind path="amount">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            <b class="text-danger">*</b>组合限额：
                        </label>
                        <div class="col-sm-6">
                            <form:input path="amount" cssClass="form-control" maxlength="20" type="number"
                                        onkeypress="return (/[\d]/.test(String.fromCharCode(event.keyCode)))" onblur="changeAmount();"/>
                            <font id="amountTip" color="red" size="3px"></font>
                        </div>
                        <div class="col-sm-3 help">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="amount" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">输入合法的组合限额</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                <form:hidden path="cycleType" />
                <spring:bind path="cycle">
                    <div class="form-group" >
                        <label class="col-sm-2 control-label text-right">
                            <b class="text-danger">*</b>组合期限：
                        </label>
                        <div class="col-sm-6">
                            <c:choose>
                                <c:when test="${planType.cycleType == 'm'}">
                                    ${planType.cycle}个月
                                </c:when>
                                <c:otherwise>
                                    ${planType. cycle}天
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                <spring:bind path="rate">
                    <div id="preferencePlan" class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            <b class="text-danger">*</b>组合利率(%)：
                        </label>
                        <div class="col-sm-6" >
                            <form:input path="rate" cssClass="form-control" maxlength="20" type="number"/>
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="rate" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">输入合法的组合利率</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                        <b class="text-danger">*</b>是否新手：
                    </label>
                    <div class="col-sm-6">
                        <form:hidden path="isNoviceBid"/>
                        <c:choose>
                            <c:when test="${planType.isNoviceBid == 'F'}">
                                否
                            </c:when>
                            <c:otherwise>
                                是
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                        营销设置
                    </label>
                    <div class="col-sm-6">
                        <input type="checkbox" id="checkMarketingSetting" name="checkMarketingSetting"/>
                        设置条件
                    </div>
                    <div class="col-sm-6" id="condition" style="display: none">
                        <div class="col-sm-offset-0 col-sm-10">
                            条件限制&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <input class="conditionCheck" type="checkbox" id="checkTotalUserAssets" name="checkTotalUserAssets"/>
                                用户资产总额&nbsp;&nbsp;大于等于
                            <form:input path="totalUserAssets" class="conditionValue" type="number" id="totalUserAssets" name="totalUserAssets" disabled="disabled"
                                        onkeypress="return (/[\d]/.test(String.fromCharCode(event.keyCode)))" onmousemove="validataAmount();"
                                    value="" onblur="autoAddLabel();" />元，可投
                            <br/>
                            <br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <input class="conditionCheck" type="checkbox" id="checkUserInvestingAmount" name="checkUserInvestingAmount" />
                                用户在投金额&nbsp;&nbsp;大于等于
                            <form:input path="userInvestingAmount" class="conditionValue" type="number" id="userInvestingAmount" name="userInvestingAmount" disabled="disabled"
                                        onkeypress="return (/[\d]/.test(String.fromCharCode(event.keyCode)))" onmousemove="validataAmount();"
                                        value="" onblur="autoAddLabel();"/>元，可投
                            <br/>
                            <br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <input class="conditionCheck" type="checkbox" id="checkUserAccumulatedIncome" name="checkUserAccumulatedIncome" />
                                用户累计收益&nbsp;&nbsp;大于等于
                            <form:input path="userAccumulatedIncome" class="conditionValue" type="number" id="userAccumulatedIncome" name="userAccumulatedIncome" disabled="disabled"
                                        onkeypress="return (/[\d]/.test(String.fromCharCode(event.keyCode)))" onmousemove="validataAmount();"
                                        value="" onblur="autoAddLabel();"/>元，可投
                            <br/>
                            <br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <form:checkbox class="conditionCheck" id="targetUser" path="targetUser" onchange="autoAddLabel();"/>
                                仅限集团员工
                            <br/>
                            <br/>
                            <font id="setting">奖励设置&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            加息<form:input path="raisedRate" cssClass="btn-sm" type="number" id="raisedRate" name="raisedRate"
                                          onkeyup= "if( ! /^\d+(\.([0-9]|\d[0-9]))?$/.test(this.value)){alert('只能输入数字，小数点后只能保留两位');this.value='';}"
                                          />&nbsp;%</font>
                            <br/>
                            <br/>
                                自定义标签&nbsp;&nbsp;&nbsp;&nbsp;
                                <form:input path="customLabel1" type="text" cssClass="btn-sm" maxlength="6" name="customLabel1" value=""/>
                                &nbsp;
                                <form:input path="customLabel2" type="text" cssClass="btn-sm" maxlength="6" name="customLabel2" value=""/>
                                &nbsp;
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<p id="customLabelTip" tip></p>
                            <input type="hidden" name="customLabelTip" value=""/>
                            <br/>
                            <br/>
                            条件说明&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <form:textarea path="comment" id="comment" name="comment" maxlength="24" value=""></form:textarea>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <a type="button" class="btn btn-default"
                           href="${path}/planCenter/planType/index">取消</a>&nbsp;&nbsp;&nbsp;&nbsp;
                        <button type="submit" class="btn btn-primary">保存</button>&nbsp;&nbsp;&nbsp;&nbsp;
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>
<c:choose>
    <c:when test="${param.success}">
        <!-- Modal -->
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
            <div class="modal-dialog modal-sm" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel">
                            提示
                        </h4>
                    </div>
                    <div class="modal-body">
                            ${param.success ? '成功' : '失败'}
                    </div>
                    <div class="modal-footer">
                        <a href="${path}/planCenter/planType/index" class="btn btn-primary">确认</a>
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
    <c:when test="${code != null}">
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
                        <c:choose>
                            <c:when test="${code <= -1}">

                            </c:when>
                            <c:otherwise>
                                未知错误，请联系管理员
                            </c:otherwise>
                        </c:choose>
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


<script type="application/javascript">
    (function ($) {
        $('form[role="form"]').validate({
            rules: {
                title: {
                    required: true,
                    rangelength: [1, 15]
                },
                rate: {
                    required:true,
                    number: true,
                    min: 0.01,
                    max: 24,
                    minNumber: $("#rate").val()
                },
                amount: {
                    required: true,
                    digits: true,
                    min: 100,
                    max: 10000000
                },
                totalUserAssets: {
                    digits: true,
                    min: 1,
                    max: 10000000
                },
                userInvestingAmount: {
                    digits: true,
                    min: 1,
                    max: 10000000
                },
                userAccumulatedIncome: {
                    digits: true,
                    min: 1,
                    max: 10000000
                }
            },
            messages: {
                title: {
                    required: "组合名称不能为空",
                    rangelength: $.validator.format("组合名称字符长度在 {0} 到 {1} 之间.")
                },
                rate: {
                    required:'组合利率不能为空',
                    number: '组合利率请输入正确的数字',
                    min: '组合利率不能小于0.01',
                    max: '组合利率不超过24'
                },
                amount: {
                    required: "组合金额不能为空",
                    digits: '请输入正确的整数',
                    min: '组合金额最少为100元',
                    max: '组合金额最多为1000万元'
                },
                totalUserAssets: {
                    digits: '请输入正确的数字',
                    min: '用户资产总额最少为1元',
                    max: '用户资产总额最多为1000万元'
                },
                userInvestingAmount: {
                    digits: '请输入正确的数字',
                    min: '用户在投金额最少为1元',
                    max: '用户在投金额最多为1000万元'
                },
                userAccumulatedIncome: {
                    digits: '请输入正确的数字',
                    min: '用户累计收益最少为1元',
                    max: '用户累计收益最多为1000万元'
                }
            },
            highlight: function (element, errorClass, validClass) {
                //空方法，去除为input添加错误样式
                //未通过的元素添加效果
                var formGroup = $(element).parents('div.form-group');
                formGroup.addClass('has-error').removeClass('has-success')
                formGroup.find('span.glyphicon.form-control-feedback').addClass('glyphicon-remove').removeClass('glyphicon-ok');
                return false;
            },
            unhighlight: function (element, errorClass, validClass) {
                //去除未通过的元素的效果
                var formGroup = $(element).parents('div.form-group');
                formGroup.addClass('has-success').removeClass('has-error')
                formGroup.find('span.glyphicon.form-control-feedback').addClass('glyphicon-ok').removeClass('glyphicon-remove');
            },
            errorPlacement: function (error, element) {
                var formGroup = $(element).parents('div.form-group');
                error.appendTo(formGroup.find('div.help').empty());
            },
            success: function (element) {
                var formGroup = $(element).parents('div.form-group');
                formGroup.find('.help-block').empty().html('正确');
            },
            submitHandler: function (form) {
            	var index = layer.load(0, {
                    shade: [0.4,'#fff',false] //0.1透明度的白色背景
                });
                	form.submit();
                	layer.close();
            },
            ignore: '.ignore',
            errorClass: "help-block",
            errorElement: 'span',
            focusInvalid: false,
            focusCleanup: true,
            onkeyup: false
        });

        jQuery.validator.addMethod("minNumber",function(value, element){
            var returnVal = true;
            inputZ=value;
            var ArrMen= inputZ.split(".");    //截取字符串
            if(ArrMen.length==2){
                if(ArrMen[1].length>2){    //判断小数点后面的字符串长度
                    returnVal = false;
                    return false;
                }
            }
            return returnVal;
        },"小数点后最多为两位");         //验证错误信息

    })(jQuery)

    function autoAddLabel(){
        var tip="";
        var num = 0;
        $("#customLabel1").attr("disabled",false);
        $("#customLabel2").attr("disabled",false);
        var customLabel1 = $("#customLabel1").val();
        var customLabel2 = $("#customLabel2").val();
        var isNoviceBid = $('#isNoviceBid').val();
        var isNovice;
        if (isNoviceBid != null && isNoviceBid == 'S'){
            isNovice = true;
        }else {
            isNovice = false;
        }
        var totalUserAssets = $('#totalUserAssets').val();
        var userInvestingAmount = $('#userInvestingAmount').val();
        var userAccumulatedIncome = $('#userAccumulatedIncome').val();
        var targetUser = $('#targetUser').is(':checked');
        if(isNovice || totalUserAssets != "" || userInvestingAmount != "" || userAccumulatedIncome != "" || targetUser){
            if(totalUserAssets != "" || userInvestingAmount != "" || userAccumulatedIncome != "" || targetUser){
                tip = tip + "“大客户专享”";
                num = num + 1;
            }
            if(isNovice){
                tip = tip + "“新手专享”";
                num = num + 1;
            }
        }
        if(num >= 2){
            tip = "已自动添加" + tip + "标签.";
            $("#customLabel1").attr("disabled",true);
            $("#customLabel2").attr("disabled",true);
        }else if(num == 0){
            tip = "还可添加2个标签."
        }else{
            tip = "已自动添加" + tip + "标签.还可输入" + num + "个标签.";
            $("#customLabel2").attr("disabled",true);
        }
        $('#customLabelTip').text(tip);
        $("input[name='customLabelTip']").val(tip);
    }

    //计划金额改变
    function changeAmount(){
        var amount = $('#amount').val();
        amount = amount/10000;
        var tip;
        if(!isNaN(amount)){
            tip = $('#amountTip').text(amount + "万元");
        }else {
            tip = $('#amountTip').text("请输入正确的数字!");
        }
    }
    
    function addTimingSetting() {
        var timingSettingA = $('#timingSettingA');
    }

    function validataAmount(){
        var totalUserAssets = $('#totalUserAssets').val();
        var userInvestingAmount = $('#userInvestingAmount').val();
        var userAccumulatedIncome = $('#userAccumulatedIncome').val();
        if(totalUserAssets != null && totalUserAssets > 100000000){
            $('#totalUserAssets').val("");
            alert("用户资产总额不能大于100000000");
        }else if (userInvestingAmount != null && userInvestingAmount > 100000000){
            $('#userInvestingAmount').val("");
            alert("用户在投金额不能大于100000000");
        }else  if (userAccumulatedIncome != null && userAccumulatedIncome > 100000000){
            $('#userAccumulatedIncome').val("");
            alert("用户累计收益不能大于100000000");
        }
    }

    $(function() {
        //定时发布点击事件
        $('#timingSetting').click(function () {
            var timingSetting = $('#timingSetting').is(':checked');
            if (timingSetting){
                $('#timingSettingDiv').show();
            }
        });

        //营销设置点击事件
        $('#checkMarketingSetting').click(function () {
            autoAddLabel();
            var hasChk = $('#checkMarketingSetting').is(':checked');
            if (hasChk) {
                $('#checkMarketingSetting').val("true");
                $('#condition').show();
                $('.conditionValue').val("");
                $('.conditionValue').attr("disabled", true);
                $('#customLabelTip').show();//标签提示语
            }
            else {
                $('#checkMarketingSetting').val("false");
                $('.conditionCheck').attr("checked", false);
                $('.conditionValue').val("");
                $('.conditionValue').attr("disabled", true);
                $('.conditionValue').next('p').html("");
                $('.conditionValue').next('p').hide();
                $('#customLabelTip').hide();//标签提示语
                $('#condition').hide();
                $('#targetUserCheckbox').attr("checked", false);
                $('#targetUser').val("0");
                $('#raisedRate').val("0");
                $('#customLabel1').val("");
                $('#customLabel1').attr("disabled", true);
                $('#customLabel2').val("");
                $('#customLabel2').attr("disabled", true);
            }
        });

        $('.conditionCheck').click(function(){
            var hasChk = $(this).is(':checked');
            if(hasChk){
                $(this).next('.conditionValue').attr("disabled",false);
            }
            else{
                $(this).next('.conditionValue').attr("disabled",true);
                $(this).next('.conditionValue').val("");
                $(this).next('p').html("");
                $(this).next('p').hide();
            }
        });

        changeAmount();

        var totalUserAssets = $('#totalUserAssets').val();
        var userInvestingAmount = $('#userInvestingAmount').val();
        var userAccumulatedIncome = $('#userAccumulatedIncome').val();
        var raiseRate = $('#raisedRate').val();
        var targetUser = $('#targetUser').is(':checked');
        var customLabel1 = $('#customLabel1').val();
        var customLabel2 = $('#customLabel2').val();
        var comment = $('#comment').val();

        //营销设置
        if((raiseRate != null && raiseRate != "") || (totalUserAssets != null && totalUserAssets != "") || (userInvestingAmount != null && userInvestingAmount != "") ||
                (userAccumulatedIncome != null && userAccumulatedIncome != "") || targetUser ||
                (customLabel1 != null && customLabel1 != "") || (customLabel1 != null && customLabel2 != "") ||
                (comment != null && comment != "")){
            $('#checkMarketingSetting').attr("checked", true);
            $('#condition').show();
            autoAddLabel();
            var totalUserAssets = $('#totalUserAssets').val();
            var userInvestingAmount = $('#userInvestingAmount').val();
            var userAccumulatedIncome = $('#userAccumulatedIncome').val();
            if(totalUserAssets != null && totalUserAssets != '') {
                $('#checkTotalUserAssets').attr("checked", true);
            }else{
                $('#checkTotalUserAssets').attr("checked", false);
                $('#totalUserAssets').attr("disabled", true);
            }
            if(userInvestingAmount != null && userInvestingAmount != '') {
                $('#checkUserInvestingAmount').attr("checked", true);
            }else{
                $('#checkUserInvestingAmount').attr("checked", false);
                $('#userInvestingAmount').attr("disabled", true);
            }
            if(userAccumulatedIncome != null && userAccumulatedIncome != '') {
                $('#checkUserAccumulatedIncome').attr("checked", true);
            }else{
                $('#checkUserAccumulatedIncome').attr("checked", false);
                $('#userAccumulatedIncome').attr("disabled", true);
            }
        };
    })
</script>