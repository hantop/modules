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
                <li class="active"><a href="${path}/planCenter/releasePlan/index">计划发布</a></li>
                <li class="active"><b>查看计划</b></li>
            </ol>
            <form:form class="form-horizontal" role="form" action=""
                       method="post" modelAttribute="planMarketingSetting">
                        <div class="form-group">
                            <label class="col-sm-2 control-label text-right">
                                计划编号:
                            </label>
                            <div class="col-sm-6">
                                <label class="col-sm control-label text-right" style="font-weight: normal">
                                   ${planMarketingSetting.number}
                                </label>
                                <%--<span class="control-label">${planMarketingSetting.number}</span>--%>
                            </div>
                        </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label text-right">
                            计划类型：
                        </label>
                        <div class="col-sm-6">
                            <c:choose>
                                <c:when test="${planMarketingSetting.type == 1}">
                                <label class="col-sm control-label text-right" style="font-weight: normal">月升计划</label>
                                </c:when>
                                <c:when test="${planMarketingSetting.type == 2}">
                                <label class="col-sm control-label text-right" style="font-weight: normal">省心计划</label>
                                </c:when>
                            </c:choose>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label text-right">
                            系统类型：
                        </label>
                        <div class="col-sm-6">
                            <c:choose>
                                <c:when test="${planMarketingSetting.isCG == 1}">
                                <label class="col-sm control-label text-right" style="font-weight: normal">普通</label>
                                </c:when>
                                <c:when test="${planMarketingSetting.isCG == 2}">
                                <label class="col-sm control-label text-right" style="font-weight: normal">存管</label>
                                </c:when>
                            </c:choose>
                        </div>
                    </div>
                    <div class="form-group">
                        <input id="cycleType" type="hidden" value="${planMarketingSetting.cycleType}">
                        <label class="col-sm-2 control-label text-right">
                            计划期限：
                        </label>
                        <div id="monPlanCycle" class="col-sm-6" style="display: none">
                            <c:choose>
                                <c:when test="${planMarketingSetting.cycle == 6}">
                                <label class="col-sm control-label text-right" style="font-weight: normal">6个月</label>
                                </c:when>
                                <c:when test="${planMarketingSetting.cycle == 12}">
                                <label class="col-sm control-label text-right" style="font-weight: normal">12个月</label>
                                </c:when>
                                <c:when test="${planMarketingSetting.cycle == 18}">
                                <label class="col-sm control-label text-right" style="font-weight: normal">18个月</label>
                                </c:when>
                                <c:when test="${planMarketingSetting.cycle == 24}">
                                <label class="col-sm control-label text-right" style="font-weight: normal">24个月</label>
                                </c:when>
                            </c:choose>
                        </div>
                        <div id="preferencePlanCycle" class="col-sm-6" style="display: none">
                            <c:choose>
                                <c:when test="${planMarketingSetting.cycle == 10}">
                                <label class="col-sm control-label text-right" style="font-weight: normal">10天</label>
                                </c:when>
                                <c:when test="${planMarketingSetting.cycle == 20}">
                                <label class="col-sm control-label text-right" style="font-weight: normal">20天</label>
                                </c:when>
                                <c:when test="${planMarketingSetting.cycle == 30}">
                                <label class="col-sm control-label text-right" style="font-weight: normal">30天</label>
                                </c:when>
                                <c:when test="${planMarketingSetting.cycle == 2}">
                                <label class="col-sm control-label text-right" style="font-weight: normal">2个月</label>
                                </c:when>
                                <c:when test="${planMarketingSetting.cycle == 3}">
                                <label class="col-sm control-label text-right" style="font-weight: normal">3个月</label>
                                </c:when>
                                <c:when test="${planMarketingSetting.cycle == 6}">
                                <label class="col-sm control-label text-right" style="font-weight: normal">6个月</label>
                                </c:when>
                                <c:when test="${planMarketingSetting.cycle == 9}">
                                <label class="col-sm control-label text-right" style="font-weight: normal">9个月</label>
                                </c:when>
                                <c:when test="${planMarketingSetting.cycle == 12}">
                                <label class="col-sm control-label text-right" style="font-weight: normal">12个月</label>
                                </c:when>
                            </c:choose>
                        </div>
                    </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                    </label>
                    <div class="col-sm-6">
                        <input id="novice" disabled="disabled" type="checkbox" <c:if test='${planMarketingSetting.novice}'>checked="checked"</c:if>/>新手专享
                    </div>
                </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label text-right">
                            <b class="text-danger">*</b>计划名称：
                        </label>
                        <div class="col-sm-6">
                            <label class="col-sm control-label text-right" style="font-weight: normal">
                            ${planMarketingSetting.name}${planMarketingSetting.number}
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label text-right">
                            <b class="text-danger">*</b>投资利率：
                        </label>
                        <div id="preferencePlan" class="col-sm-6" >
                            <label class="col-sm control-label text-right" style="font-weight: normal">
                                <fmt:formatNumber value="${planMarketingSetting.investRate}" pattern="#.##" type="number"/>%
                            </label>
                        </div>
                        <div id="monPlan" class="col-sm-6" style="display: none">
                            <div class="col-sm-offset-0 col-sm-10">
                            最低年化利率&nbsp;&nbsp;
                                <label class="col-sm control-label text-right" style="font-weight: normal">
                                    <fmt:formatNumber value="${planMarketingSetting.minYearlyRate}" pattern="#.##" type="number"/>%
                                </label>
                                <br/>
                                <br/>
                            最高年化利率&nbsp;&nbsp;
                                <label class="col-sm control-label text-right" style="font-weight: normal">
                                    <fmt:formatNumber value="${planMarketingSetting.maxYearlyRate}" pattern="#.##" type="number"/>%
                                </label>
                                <br/>
                                <br/>&nbsp;&nbsp;&nbsp;
                                利率月增幅&nbsp;&nbsp;
                                <label class="col-sm control-label text-right" style="font-weight: normal">
                                    <fmt:formatNumber value="${planMarketingSetting.moIncreaseRate}" pattern="#.##" type="number"/>%
                                </label>
                            </div>
                        </div>
                        <!-- ECharts -->
                        <div id="main" class="col-sm-6" style="height:400px;width: 850px;display: none"></div>
                    </div>
                     <div class="form-group">
                            <label class="col-sm-2 control-label text-right">
                                <b class="text-danger">*</b>计划金额：
                            </label>
                            <div class="col-sm-6">
                                <label class="col-sm control-label text-right" style="font-weight: normal">${planMarketingSetting.amount}</label>
                                <br/><font id="amountTip" color="red" size="3px"></font>
                            </div>
                     </div>
                 <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                            还款方式
                    </label>
                    <div class="col-sm-6">
                        <c:choose>
                            <c:when test="${planMarketingSetting.repayMode == 'DEBX'}">
                            <label class="col-sm control-label text-right" style="font-weight: normal">等额本息</label>
                            </c:when>
                            <c:when test="${planMarketingSetting.repayMode == 'MYFX'}">
                            <label class="col-sm control-label text-right" style="font-weight: normal">每月付息,到期还本</label>
                            </c:when>
                            <c:when test="${planMarketingSetting.repayMode == 'YCFQ'}">
                            <label class="col-sm control-label text-right" style="font-weight: normal">一次结清</label>
                            </c:when>
                            <c:when test="${planMarketingSetting.repayMode == 'DEBJ'}">
                            <label class="col-sm control-label text-right" style="font-weight: normal">等额本金</label>
                            </c:when>
                        </c:choose>
                    </div>
                 </div>
                 <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                        筹款期
                    </label>
                    <div class="col-sm-6">
                        <label class="col-sm control-label text-right" style="font-weight: normal">
                            ${planMarketingSetting.fundRaisingPeriod}小时
                        </label>
                    </div>
                 </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                        营销设置
                    </label>
                    <div class="col-sm-6">
                        <input disabled="disabled" type="checkbox" id="checkMarketingSetting" name="checkMarketingSetting"/>
                        设置条件
                    </div>
                    <div class="col-sm-6" id="condition" style="display: none">
                        <div class="col-sm-offset-0 col-sm-10">
                            条件限制&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <c:choose>
                                <c:when test="${planMarketingSetting.totalUserAssets != null}">
                                    <input clsaa="conditionCheck" type="checkbox" id="checkTotalUserAssets" name="checkTotalUserAssets" checked="checked" disabled="disabled"/>
                                    用户资产总额&nbsp;&nbsp;大于等于${planMarketingSetting.totalUserAssets}元，可投
                                    <input type="hidden" class="conditionValue" value="${planMarketingSetting.totalUserAssets}">
                                </c:when>
                                <c:otherwise>
                                    <input clsaa="conditionCheck" type="checkbox" id="checkUserInvestingAmount" name="checkUserInvestingAmount" disabled="disabled"/>
                                    用户资产总额
                                </c:otherwise>
                            </c:choose>
                            <br/>
                            <br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <c:choose>
                                <c:when test="${planMarketingSetting.userInvestingAmount != null}">
                                    <input clsaa="conditionCheck" type="checkbox" id="checkUserInvestingAmount" name="checkUserInvestingAmount" checked="checked" disabled="disabled"/>
                                    用户在投金额&nbsp;&nbsp;大于等于${planMarketingSetting.userInvestingAmount}元，可投
                                    <input type="hidden" class="conditionValue" value="${planMarketingSetting.userInvestingAmount}">
                                </c:when>
                                <c:otherwise>
                                    <input clsaa="conditionCheck" type="checkbox" id="checkUserInvestingAmount" name="checkUserInvestingAmount" disabled="disabled"/>
                                    用户在投金额
                                </c:otherwise>
                            </c:choose>
                            <br/>
                            <br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <c:choose>
                                <c:when test="${planMarketingSetting.userAccumulatedIncome != null}">
                                    <input clsaa="conditionCheck" type="checkbox" id="checkUserAccumulatedIncome" name="checkUserAccumulatedIncome" checked="checked" disabled="disabled"/>
                                    用户累计收益&nbsp;&nbsp;大于等于${planMarketingSetting.userAccumulatedIncome}元，可投
                                    <input type="hidden" class="conditionValue" value="${planMarketingSetting.userAccumulatedIncome}">
                                </c:when>
                                <c:otherwise>
                                    <input clsaa="conditionCheck" type="checkbox" id="checkUserAccumulatedIncome" name="checkUserAccumulatedIncome" disabled="disabled"/>
                                    累计收益
                                </c:otherwise>
                            </c:choose>
                            <br/>
                            <br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <input clsaa="conditionCheck" type="checkbox" <c:if test='${planMarketingSetting.targetUser}'>checked="checked"</c:if> disabled="disabled"/>
                                仅限集团员工
                            <br/>
                            <br/>
                            <c:choose>
                                <c:when test="${planMarketingSetting.type != 1 && planMarketingSetting.raiseRate != null && planMarketingSetting.raiseRate != ''}">
                                    加息 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <input type="hidden" class="conditionValue" value="${planMarketingSetting.raiseRate}">
                                    <fmt:formatNumber value="${planMarketingSetting.raiseRate}" pattern="#.##" type="number"/>%
                                </c:when>
                                <c:when test="${planMarketingSetting.type != 1}">
                                    加息&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                </c:when>
                            </c:choose>
                            <br/>
                            <br/>
                            自定义标签&nbsp;&nbsp;&nbsp;&nbsp;<font id="label"></font>
                            <br/>
                            <br/>
                            <c:choose>
                            <c:when test="${planMarketingSetting.comment != null && planMarketingSetting.comment != ''}">
                            条件说明&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <input type="hidden" class="conditionValue" value="${planMarketingSetting.comment}">
                                ${planMarketingSetting.comment}
                            </c:when>
                            <c:otherwise>
                                条件说明&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-15">
                        <a type="button" class="btn btn-default"
                           href="${path}/planCenter/releasePlan/index">关闭</a>&nbsp;&nbsp;&nbsp;&nbsp;
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>
<script type="application/javascript">
    // echarts路径配置
    require.config({
        paths: {
            echarts: '/static/component/echarts'
        }
    });

    $(function() {
        var productType = ${planMarketingSetting.type};
        //1月升计划
        if(productType == 1){
            $('#preferencePlan').hide();
            $('#preferencePlanCycle').hide();
            $('#monPlan').show();
            $('#monPlanCycle').show();
            //柱状图
            $('#main').show();
            var xValue = new Array();
            var yValue = new Array();
            var minYearlyRate;//最低
            var maxYearlyRate;//最高
            var moIncreaseRate;//增幅
            if(${planMarketingSetting.minYearlyRateStr != null && planMarketingSetting.minYearlyRateStr != ""}){
                minYearlyRate = parseFloat(${planMarketingSetting.minYearlyRateStr});
            }
            if(${planMarketingSetting.maxYearlyRateStr != null && planMarketingSetting.maxYearlyRateStr != ""}){
                maxYearlyRate = parseFloat(${planMarketingSetting.maxYearlyRateStr});
            }
            if(${planMarketingSetting.moIncreaseRateStr != null && planMarketingSetting.moIncreaseRateStr != ""}){
                moIncreaseRate = parseFloat(${planMarketingSetting.moIncreaseRateStr});
            }
            //var cycleValue = parseFloat($('#cycle option:selected').val());//选择期限
            var cycleValue = ${planMarketingSetting.cycle};
            for (var i = 1; i <= cycleValue; i++){
                xValue.push(i + '月');
            }
            if(maxYearlyRate > 0 && minYearlyRate > 0 && moIncreaseRate > 0){
                var times = Math.floor((maxYearlyRate - minYearlyRate)/moIncreaseRate);
                for (var j = 0; j <= times; j++){
                    if(minYearlyRate < maxYearlyRate){
                        yValue.push(minYearlyRate.toFixed(2));
                        minYearlyRate = minYearlyRate + moIncreaseRate;
                    }
                }
                if(times < cycleValue){
                    var timed = cycleValue - times;
                    for(var i = 0; i <= timed; i++){
                        yValue.push(maxYearlyRate);
                    }
                }
            }
            // 使用
            require(
                    [
                        'echarts',
                        'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
                    ],
                    function (ec) {
                        // 基于准备好的dom，初始化echarts图表
                        var myChart = ec.init(document.getElementById('main'));
                        var option = {
                            tooltip: {
                                show: true
                            },
                            legend: {
                                data:['']
                            },
                            xAxis : [
                                {
                                    type : 'category',
                                    splitLine:{
                                        show:false
                                    },
                                    data :xValue,
                                    axisLabel:{
                                        //X轴刻度配置
                                        interval:0 //0：表示全部显示不间隔；auto:表示自动根据刻度个数和宽度自动设置间隔个数
                                    }
                                }
                            ],
                            yAxis : [
                                {
                                    type : 'value',
                                    axisLabel : {
                                        formatter: '{value} %'
                                    },
                                }
                            ],
                            series : [
                                {
                                    "name":"投资利率",
                                    "type":"bar",
                                    "barWidth" : 15,
                                    "data":yValue
                                }
                            ]
                        };
                        // 为echarts对象加载数据
                        myChart.setOption(option);
                    }
            )
        }else if(productType == 2){
            $('#monPlan').hide();
            $('#monPlanCycle').hide();
            $('#preferencePlan').show();
            $('#preferencePlanCycle').show();
        }

        if(${planMarketingSetting.amount != null}){
            var amountTip = ${planMarketingSetting.amount};
            amountTip = amountTip/10000;
            $('#amountTip').html(amountTip + "万元")
        }

        //营销设置点击事件
        $('#checkMarketingSetting').click(function () {
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
                $('#targetUserValue').val("0");
                $('#rateInterestForBid').val();
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

        //营销设置
        if(${(planMarketingSetting.raiseRate != null && planMarketingSetting.raiseRate != "")
        || (planMarketingSetting.totalUserAssets != null && planMarketingSetting.totalUserAssets != "")
        || (planMarketingSetting.userInvestingAmount != null && planMarketingSetting.userInvestingAmount != "")
        || (planMarketingSetting.userAccumulatedIncome != null && planMarketingSetting.userAccumulatedIncome != "")
        || planMarketingSetting.targetUser
        || (planMarketingSetting.customLabel1 != null && planMarketingSetting.customLabel1 != "")
        || (planMarketingSetting.customLabel2 != null && planMarketingSetting.customLabel2 != "")
        || (planMarketingSetting.comment != null && planMarketingSetting.comment != "")}){
            $('#checkMarketingSetting').attr("checked", true);
            $('#condition').show();
            var label = $('#label').text();
            var num = 0;
            while (num < 1)
            {
                if(${(planMarketingSetting.totalUserAssets != null && planMarketingSetting.totalUserAssets != "")
                            || (planMarketingSetting.userInvestingAmount != null && planMarketingSetting.userInvestingAmount != "")
                            || (planMarketingSetting.userAccumulatedIncome != null && planMarketingSetting.userAccumulatedIncome != "")
                            || planMarketingSetting.targetUser || planMarketingSetting.novice
                            || (planMarketingSetting.customLabel1 != null && planMarketingSetting.customLabel1 != "")
                            || (planMarketingSetting.customLabel2 != null && planMarketingSetting.customLabel2 != "")}){

                    if(${planMarketingSetting.novice}){
                        label = label + "新手专享" + '&nbsp;&nbsp;';
                        num = num + 1;
                        if (num == 2)
                            break;
                    }
                    if(${(planMarketingSetting.totalUserAssets != null && planMarketingSetting.totalUserAssets != "")
                            || (planMarketingSetting.userInvestingAmount != null && planMarketingSetting.userInvestingAmount != "")
                            || (planMarketingSetting.userAccumulatedIncome != null && planMarketingSetting.userAccumulatedIncome != "")
                            || planMarketingSetting.targetUser}){
                        label = label +  "大客户专享" + '&nbsp;&nbsp;';
                        num = num + 1;
                        if (num == 2)
                            break;
                    }
                    if(${planMarketingSetting.customLabel1 != null && planMarketingSetting.customLabel1 != ""}){
                        label = label +  '${planMarketingSetting.customLabel1}' + '&nbsp;&nbsp;';
                        num = num + 1;
                        if (num == 2)
                            break;
                    }
                    if(${planMarketingSetting.customLabel2 != null && planMarketingSetting.customLabel2 != ""}){
                        label = label +  '${planMarketingSetting.customLabel2}' + '&nbsp;&nbsp;';
                        num = num + 1;
                        if (num == 2)
                            break;
                    }
                }else{
                    num = num + 1;
                    if (num == 2)
                        break;
                }
            }
            $('#label').html(label);
        }
    })
</script>