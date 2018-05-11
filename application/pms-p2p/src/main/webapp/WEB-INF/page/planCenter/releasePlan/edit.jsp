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
                <li class="active"><a href="${path}/planCenter/releasePlan/index">计划发布</a></li>
                <li class="active"><b>新增计划</b></li>
            </ol>
            <span class="badge" style="font-size: 18px;">${title}</span><br/>
            <form:form class="form-horizontal" role="form" action="${path}/planCenter/releasePlan/edit"
                       method="post" modelAttribute="planMarketingSetting">
                <input type="hidden" name="flag" id="flag" />
                <form:hidden path="id"/>
                <form:hidden path="status"/>
                <form:hidden path="productType" value="1"/>
                <form:hidden path="overdueRate" value="0.0006" />
                <c:choose>
                    <c:when test="${planMarketingSetting.number != null && planMarketingSetting.number != ''}">
                        <div class="form-group">
                            <label class="col-sm-2 control-label text-right">
                                计划编号:
                            </label>
                            <div class="col-sm-6">
                                <form:input path="number" cssClass="form-control" maxlength="15" type="text" readonly="readonly" disabled="true"/>
                            </div>
                        </div>
                    </c:when>
                </c:choose>
                <spring:bind path="type">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            <b class="text-danger">*</b>计划类型：
                        </label>
                        <div class="col-sm-6">
                            <form:select path="type" cssClass="form-control input-sm" onchange="selectType();">
                                <form:option value="0">请选择</form:option>
                                <form:option value="1">月升计划</form:option>
                                <form:option value="2">省心计划</form:option>
                            </form:select>
                        </div>
                        <div class="col-sm-3 help">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="type" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">请选择计划类型</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                    <label class="col-sm-2 control-label text-right">
                        <b class="text-danger">*</b>系统类型：
                    </label>
                    <div class="col-sm-6">
                        <form:select path="isCG" cssClass="form-control input-sm">
                            <form:option value="0">请选择</form:option>
                            <form:option value="2">存管</form:option>
                            <form:option value="1">普通</form:option>
                        </form:select>
                    </div>
                    <div class="col-sm-3 help">
                        <c:choose>
                            <c:when test="${status.error}">
                                <form:errors path="isCG" element="span" cssClass="help-block"/>
                            </c:when>
                            <c:otherwise>
                                <span class="help-block">请选择计划类型</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <form:hidden path="cycleType" />
                <spring:bind path="monCycleShow">
                    <div id="monPlanCycle" class="form-group has-feedback ${status.error ? 'has-error' : ''}"  style="display: none">
                        <label class="col-sm-2 control-label text-right">
                            <b class="text-danger">*</b>计划期限：
                        </label>
                        <div class="col-sm-6">
                            <form:select id="monCycle" path="monCycleShow" cssClass="cycle form-control input-sm" onchange="selectCycle();print();">
                                <form:option value="0" label="请选择"/>
                                <form:option value="6" label="6个月"/>
                                <form:option value="12" label="12个月"/>
                                <form:option value="18" label="18个月"/>
                                <form:option value="24" label="24个月"/>
                            </form:select>
                        </div>
                        <div class="col-sm-3 help">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="cycle" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">请选择计划期限</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                <spring:bind path="preferenceCycleShow">
                    <div id="preferencePlanCycle" class="form-group has-feedback ${status.error ? 'has-error' : ''}" style="display: none">
                        <label class="col-sm-2 control-label text-right">
                            <b class="text-danger">*</b>计划期限：
                        </label>
                        <div class="col-sm-6">
                            <form:select id="preferenceCycle" path="preferenceCycleShow" cssClass="cycle form-control input-sm" onchange="selectCycle();checkMonth();">
                                <form:option value="0" label="请选择"/>
                                <form:option value="2" label="2天"/>
                                <form:option value="3" label="3天"/>
                                <form:option value="10" label="10天"/>
                                <form:option value="20" label="20天"/>
                                <form:option value="30" label="30天"/>
                                <form:option value="2" label="2个月"/>
                                <form:option value="3" label="3个月"/>
                                <form:option value="6" label="6个月"/>
                                <form:option value="9" label="9个月"/>
                                <form:option value="12" label="12个月"/>
                            </form:select>
                        </div>
                        <div class="col-sm-3 help">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="cycle" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">请选择计划期限</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                    </label>
                    <div class="col-sm-6">
                        <form:checkbox id="novice" path="novice" onclick="changeNovice();"/>新手专享
                    </div>
                </div>
                <spring:bind path="name">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            <b class="text-danger">*</b>计划名称：
                        </label>
                        <div class="col-sm-6">
                            <form:input path="name" cssClass="form-control" maxlength="15" type="text"/>
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="name" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">输入合法的计划名称</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                <spring:bind path="investRate">
                    <div id="preferencePlan" class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            <b class="text-danger">*</b>投资利率(%)：
                        </label>
                        <div class="col-sm-6" >
                            <form:input path="investRate" cssClass="form-control" maxlength="20" type="number" />
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help" id="preferencePlanTip">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="investRate" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">输入合法的投资利率</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                    <%--<div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <div id="monPlan" class="col-sm-6" style="display: none">
                            <div class="col-sm-offset-0 col-sm-10">
                            最低年化利率&nbsp;&nbsp;<form:input path="minYearlyRate" cssClass="input-sm" maxlength="20" type="number" onchange="print();"/>&nbsp;&nbsp;&nbsp;%
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                                <br/>
                                <br/>
                            最高年化利率&nbsp;&nbsp;<form:input path="maxYearlyRate" cssClass="input-sm" maxlength="20" type="number" onchange="print();"/>&nbsp;&nbsp;&nbsp;%
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                                <br/>
                                <br/>&nbsp;&nbsp;&nbsp;
                                利率月增幅&nbsp;&nbsp;<form:input path="moIncreaseRate" cssClass="input-sm" maxlength="20" type="number" onchange="print();"/>&nbsp;&nbsp;&nbsp;%
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                            </div>
                        </div>
                        <div class="col-sm-3 help" id="monPlanTip" style="display: none">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="minYearlyRate" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">输入合法的最低年化利率</span>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="maxYearlyRate" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">输入合法的最高年化利率</span>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="moIncreaseRate" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">利率月增幅</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <!-- ECharts -->
                        <div id="main" class="col-sm-6" style="height:400px;width: 850px;display: none"></div>
                    </div>--%>
                <div class="monPlan form-group has-feedback ${status.error ? 'has-error' : ''}" style="display: none">
                    <label class="col-sm-2 control-label text-right">
                        <b class="text-danger">*</b>投资利率(%)：
                    </label>
                    <div class="col-sm-6 form-group ${status.error ? 'has-error' : ''}">
                        <div class="col-sm-offset-0 col-sm-10">
                            最低年化利率&nbsp;&nbsp;<form:input path="minYearlyRate" cssClass="input-sm" maxlength="20" type="number" onchange="print();"/>&nbsp;&nbsp;&nbsp;%
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-10 help monPlanTip">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="minYearlyRate" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">输入合法的最低年化利率</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
                <div class="monPlan form-group has-feedback ${status.error ? 'has-error' : ''}" style="display: none">
                    <label class="col-sm-2 control-label text-right">
                        <b class="text-danger"></b>
                    </label>
                    <div class="col-sm-6 form-group ${status.error ? 'has-error' : ''}">
                        <div class="col-sm-offset-0 col-sm-10">
                            最高年化利率&nbsp;&nbsp;<form:input path="maxYearlyRate" cssClass="input-sm" maxlength="20" type="number" onchange="print();"/>&nbsp;&nbsp;&nbsp;%
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help monPlanTip">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="maxYearlyRate" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">输入合法的最高年化利率</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
                <div class="monPlan form-group has-feedback ${status.error ? 'has-error' : ''}" style="display: none">
                    <label class="col-sm-2 control-label text-right">
                        <b class="text-danger"></b>
                    </label>
                    <div class="col-sm-6 form-group ${status.error ? 'has-error' : ''}">
                        <div class="col-sm-offset-0 col-sm-10">
                            利率月增幅&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <form:input path="moIncreaseRate" cssClass="input-sm" maxlength="20" type="number" onchange="print();"/>&nbsp;&nbsp;&nbsp;%
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help monPlanTip">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="moIncreaseRate" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">输入合法的利率月增幅</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <!-- ECharts -->
                    <div id="main" class="col-sm-6" style="height:400px;width: 850px;display: none"></div>
                </div>
                <spring:bind path="amount">
                     <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                            <label class="col-sm-2 control-label text-right">
                                <b class="text-danger">*</b>计划金额(元)：
                            </label>
                            <div class="col-sm-6">
                                <form:input path="amount" cssClass="form-control" maxlength="20" onblur="changeAmount();"/>
                                <font id="amountTip" color="red" size="3px"></font>
                            </div>
                         <div class="col-sm-3 help">
                             <c:choose>
                                 <c:when test="${status.error}">
                                     <form:errors path="amount" element="span" cssClass="help-block"/>
                                 </c:when>
                                 <c:otherwise>
                                     <span class="help-block">输入合法的计划金额</span>
                                 </c:otherwise>
                             </c:choose>
                         </div>
                     </div>
                </spring:bind>
                 <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                            还款方式
                    </label>
                    <div class="col-sm-6">
                        <form:hidden path="repayMode" value="YCFQ" />一次结清
                    </div>
                 </div>
                 <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                        筹款期
                    </label>
                    <div class="col-sm-6">
                        <form:hidden path="fundRaisingPeriod" value="48" />48小时
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
                            <form:input path="totalUserAssets" class="conditionValue" type="text" id="totalUserAssets" name="totalUserAssets" disabled="disabled"
                                    value="" onblur="autoAddLabel();" />元，可投
                            <br/>
                            <br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <input class="conditionCheck" type="checkbox" id="checkUserInvestingAmount" name="checkUserInvestingAmount" />
                                用户在投金额&nbsp;&nbsp;大于等于
                            <form:input path="userInvestingAmount" class="conditionValue" type="text" id="userInvestingAmount" name="userInvestingAmount" disabled="disabled"
                                   value="" onblur="autoAddLabel();"/>元，可投
                            <br/>
                            <br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <input class="conditionCheck" type="checkbox" id="checkUserAccumulatedIncome" name="checkUserAccumulatedIncome" />
                                用户累计收益&nbsp;&nbsp;大于等于
                            <form:input path="userAccumulatedIncome" class="conditionValue" type="text" id="userAccumulatedIncome" name="userAccumulatedIncome" disabled="disabled"
                                   value="" onblur="autoAddLabel();"/>元，可投
                            <br/>
                            <br/>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <form:checkbox class="conditionCheck" id="targetUser" path="targetUser" onchange="autoAddLabel();"/>
                                仅限集团员工
                            <br/>
                            <br/>
                            <font id="setting">奖励设置&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            加息<form:input path="raiseRate" cssClass="btn-sm" type="text" id="raiseRate" name="raiseRate" onblur="checkRate();"/>&nbsp;%</font>
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
                    <div class="col-sm-offset-2 col-sm-15">
                        <a type="button" class="btn btn-default"
                           href="${path}/planCenter/releasePlan/index">取消</a>&nbsp;&nbsp;&nbsp;&nbsp;
                        <button type="submit" class="btn btn-primary" onclick="save()">保存</button>&nbsp;&nbsp;&nbsp;&nbsp;
                        <button type="submit" class="btn btn-primary" onclick="saveCon()">保存并提交</button>
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
                        <a href="${path}/planCenter/releasePlan/index" class="btn btn-primary">确认</a>
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
                type: {
                    checkTypeSeleted: true
                },
                isCG: {
                    checkIsCGSeleted: true
                },
                name: {
                    required: true,
                    rangelength: [1, 15]
                },
                preferenceCycleShow: {
                    checkSeleted: true
                },
                monCycleShow: {
                    checkSeleted: true
                },
                investRate: {
                    required:function () {
                        var type = $('#type').val();
                        if(type == 2){
                            return true;
                        }else{
                            return false;
                        }
                    },
                    number: true,
                    min: 0.01,
                    max: 24
                },
                minYearlyRate: {
                    required:function () {
                        var type = $('#type').val();
                        if(type == 1){
                            return true;
                        }else{
                            return false;
                        }
                    },
                    number: true,
                    min: 0.01,
                    max: 24
                },
                maxYearlyRate: {
                    required:function () {
                        var type = $('#type').val();
                        if(type == 1){
                            return true;
                        }else{
                            return false;
                        }
                    },
                    checkMinOrMax:true,
                    number: true,
                    min: 0.01,
                    max: 24
                },
                moIncreaseRate: {
                    required:function () {
                        var type = $('#type').val();
                        if(type == 1){
                            return true;
                        }else{
                            return false;
                        }
                    },
                    number: true,
                    min: 0.01,
                    max: 24
                },
                amount: {
                    required: true,
                    digits: true,
                    min: 100,
                    max: 10000000
                },
                totalUserAssets: {
                    number: true,
                    min: 1,
                    max: 10000000
                },
                userInvestingAmount: {
                    number: true,
                    min: 1,
                    max: 10000000
                },
                userAccumulatedIncome: {
                    number: true,
                    min: 1,
                    max: 10000000
                }
            },
            messages: {
                name: {
                    required: "计划名称不能为空",
                    rangelength: $.validator.format("计划名称字符长度在 {0} 到 {1} 之间.")
                },
                investRate: {
                    required:'投资利率不能为空',
                    number: '投资利率请输入正确的数字',
                    min: '投资利率不能小于0.01',
                    max: '投资利率不超过24'
                },
                minYearlyRate: {
                    required:'最低年化利率不能为空',
                    number: '最低年化利率请输入正确的数字',
                    min: '最低年化利率不能小于0.01',
                    max: '最低年化利率不超过24'
                },
                maxYearlyRate: {
                    required:'最高年化利率不能为空',
                    number: '最高年化利率请输入正确的数字',
                    min: '最高年化利率不能小于0.01',
                    max: '最高年化利率不超过24'
                },
                moIncreaseRate: {
                    required:'利率月增幅不能为空',
                    number: '利率月增幅请输入正确的数字',
                    min: '利率月增幅不能小于0.01',
                    max: '利率月增幅不超过24'
                },
                amount: {
                    required: "计划金额不能为空",
                    digits: '请输入正确的整数',
                    min: '计划金额最少为100元',
                    max: '计划金额最多为1000万元'
                },
                totalUserAssets: {
                    number: '用户资产总额请输入正确的数字1',
                    min: '用户资产总额最少为1元',
                    max: '用户资产总额最多为1000万元'
                },
                userInvestingAmount: {
                    number: '用户在投金额请输入正确的数字',
                    min: '用户在投金额最少为1元',
                    max: '用户在投金额最多为1000万元'
                },
                userAccumulatedIncome: {
                    number: '用户累计收益请输入正确的数字',
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

        jQuery.validator.addMethod("checkTypeSeleted", function() {
            var returnVal = false;
            var type = $('#type').val();
            if(type > 0){
                returnVal = true;
            }
            return returnVal;
        }, "请选择计划类型");

        //校验最低最高年化利率
        jQuery.validator.addMethod("checkMinOrMax", function() {
            var returnVal = false;
            var type = $('#type').val();
            if(type == 1){
                var minYearlyRate = parseFloat($('#minYearlyRate').val());//最低
                var maxYearlyRate = parseFloat($('#maxYearlyRate').val());//最高
                if((minYearlyRate > 0 && maxYearlyRate > 0) && minYearlyRate < maxYearlyRate){
                    returnVal = true;
                }
            }
            return returnVal;
        }, "最低年化利率需小于最高年化利率!!!");

        //期限选择
        jQuery.validator.addMethod("checkSeleted", function() {
            var returnVal = false;
            var type = $('#type').val();
            var cycleValue1 = $('#monCycle option:selected').val();
            var cycleValue2 = $('#preferenceCycle option:selected').val();
            if(type == 1 && cycleValue1 > 0){
                returnVal = true;
            }
            if(type == 2 && cycleValue2 > 0){
                $("#maxYearlyRate").rules("remove", "checkMinOrMax") //移除
                returnVal = true;
            }
            return returnVal;
        }, "请选择有效期限");

        //系统类型选择('1：普通标，2：存管标)
        jQuery.validator.addMethod("checkIsCGSeleted", function() {
            var returnVal = false;
            var isCG = $('#isCG').val();
            if(isCG > 0){
                returnVal = true;
            }
            return returnVal;
        }, "请选择系统类型");

    })(jQuery)

    // echarts路径配置
    require.config({
        paths: {
            echarts: '/static/component/echarts'
        }
    });

    function autoAddLabel(){
        var tip="";
        var num = 0;
        $("#customLabel1").attr("disabled",false);
        $("#customLabel2").attr("disabled",false);
        var customLabel1 = $("#customLabel1").val();
        var customLabel2 = $("#customLabel2").val();
        var isNovice = $('#novice').is(':checked');
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

    //计划类型选择
    function selectType(){
        var name = $('#type option:selected').text();
        var isNovice = $('#novice').is(':checked');
        var nameExtend;
        //月升计划1
        if ($("#type").val() == 1){
            $('#main').show();
            $('#setting').hide();/*月升计划暂不加息*/
            $('#raiseRate').attr("disable", true);
            $('#preferencePlan').hide();
            $('#preferencePlanTip').hide();
            $('#preferencePlanCycle').hide();
            $('#preferencePlanCycle').attr("disable", true);
            $('#raiseRate').attr("disable", true);
            $('.monPlan').show();
            $('.monPlanTip').show();
            $('#monPlanCycle').show();
            nameExtend = $('#monCycle option:selected').text();
        }else {
            //省心计划2
            $('#main').hide();
            $('#setting').show()/*月升计划暂不加息*/
            $('#raiseRate').attr("disable", false);
            $('.monPlan').hide();
            $('.monPlanTip').hide();
            $('#monPlanCycle').hide();
            $('#monPlanCycle').attr("disable", true);
            $('#preferencePlan').show();
            $('#preferencePlanTip').show();
            $('#preferencePlanCycle').show();
            nameExtend = $('#preferenceCycle option:selected').text();
        }
        if(isNovice){
            $('#name').val("新手计划" + nameExtend);
        }else{
            $('#name').val(name + nameExtend);
        }
    }

    //选择周期
    function selectCycle(){
        var nameExtend;
        var cycleType;
        var name = $('#type option:selected').text();
        var isNovice = $('#novice').is(':checked');
        if(!isNovice){
            if ($("#type").val() == 1){
                nameExtend = $('#monCycle option:selected').text();
                $('#name').val(name + nameExtend);
                cycleType = nameExtend.charAt(nameExtend.length - 1);
            }else if($("#type").val() == 2){
                nameExtend = $('#preferenceCycle option:selected').text();
                $('#name').val(name + nameExtend);
                cycleType = nameExtend.charAt(nameExtend.length - 1);
            }else{
                $('#name').val(name);
            }
        }else{
            name = "新手计划";
            if ($("#type").val() == 1){
                nameExtend = $('#monCycle option:selected').text();
                $('#name').val(name + nameExtend);
                cycleType = nameExtend.charAt(nameExtend.length - 1);
            }else if($("#type").val() == 2){
                nameExtend = $('#preferenceCycle option:selected').text();
                $('#name').val(name + nameExtend);
                cycleType = nameExtend.charAt(nameExtend.length - 1);
            }else{
                $('#name').val(name);
            }
        }
        if(cycleType == '天'){
            $('#cycleType').val('d');
        }else if(cycleType == '月'){
            $('#cycleType').val('m');
        }
    }

    //新手专享
    function changeNovice(){
        var name;
        var nameExtend;
        var isNovice = $('#novice').is(':checked');
        var customLabelTip = $('#customLabelTip').val();//标签提示
        if(isNovice){
            name = '新手计划';
            if ($("#type").val() == 1){
                nameExtend = $('#monCycle option:selected').text();
                $('#name').val(name + nameExtend);
            }else if($("#type").val() == 2){
                nameExtend = $('#preferenceCycle option:selected').text();
                $('#name').val(name + nameExtend);
            }else{
                $('#name').val(name);
            }
            autoAddLabel();
        }else{
         name = $('#type option:selected').text();
            if ($("#type").val() == 1){
                nameExtend = $('#monCycle option:selected').text();
                $('#name').val(name + nameExtend);
            }else if($("#type").val() == 2){
                nameExtend = $('#preferenceCycle option:selected').text();
                $('#name').val(name + nameExtend);
            }
            autoAddLabel();
         }
    }

    //柱状图
    function print(){
        $('#main').show();
        var xValue = new Array();
        var yValue = new Array();
        var cycleValue;
        var minYearlyRate = parseFloat($('#minYearlyRate').val());//最低
        var maxYearlyRate = parseFloat($('#maxYearlyRate').val());//最高
        var moIncreaseRate = parseFloat($('#moIncreaseRate').val());//增幅
        if(minYearlyRate >= maxYearlyRate){
            bootbox.alert({size: 'small',message:"最低年化利率需小于最高年化利率!!!"});
            return false;
        }
        if ($("#type").val() == 1){
            cycleValue = $('#monCycle option:selected').val();
        }else if($("#type").val() == 2){
            cycleValue = $('#preferenceCycle option:selected').val();
        }
        if(cycleValue <= 0){
            bootbox.alert({size: 'small',message:"请选择有效的计划期限!!!"});
            return false;
        }
        checkRate();
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
        );
    }

    //计划金额改变
    function changeAmount(){
        var amount = $('#amount').val();
        amount = amount/10000;
        var tip = $('#amountTip').text(amount + "万元");
    }

    //保存（待提交）
    function save() {
        $("#status").val("1");
    }
    //保存并继续(待审核)
    function saveCon() {
        $("#status").val("2");
    }

    function checkMonth(){
        var cycleValue;
        if ($("#type").val() == 1){
            cycleValue = $('#monCycle option:selected').val();
        }else if($("#type").val() == 2){
            cycleValue = $('#preferenceCycle option:selected').val();
        }
        if(cycleValue <= 0){
            bootbox.alert({size: 'small',message:"请选择有效的计划期限!!!"});
            return false;
        }
    }

    //校验计划利率与加息利率
    function checkRate(){
        var maxYearlyRate;
        var investRate;
        var raiseRate = parseFloat($('#raiseRate').val());
        if ($("#type").val() == 1){
            maxYearlyRate = parseFloat($('#maxYearlyRate').val());
            if((maxYearlyRate + raiseRate) > 24){
                bootbox.alert({size: 'small',message:"计划利率与加息利率之和不能超过24%!!!"});
                $('#raiseRate').val("");
                return false;
            }
        }else if($("#type").val() == 2){
            investRate = parseFloat($('#investRate').val());
            if((investRate + raiseRate) > 24){
                bootbox.alert({size: 'small',message:"计划利率与加息利率之和不能超过24%!!!"});
                $('#raiseRate').val("");
                return false;
            }
        }
    }

    $(function() {
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

        var productType = ${planMarketingSetting.type};
        //1月升计划
        if(productType == 1) {
            $('#setting').hide();/*月升计划暂不显示*/
            $('#preferencePlan').hide();
            $('#preferencePlanTip').hide();
            $('#preferencePlanCycle').hide();
            $('#preferencePlanCycle').attr("disable", true);
            $('.monPlan').show();
            $('.monPlanTip').show();
            $('#monPlanCycle').show();
            $('#monPlanCycle').attr("disable", false);
            print();
        }else if(productType == 2){
            $('#setting').show();/*月升计划暂不显示*/
            $('.monPlan').hide();
            $('.monPlanTip').hide();
            $('#monPlanCycle').hide();
            $('#monPlanCycle').attr("disable", true);
            $('#preferencePlan').show();
            $('#preferencePlanTip').show();
            $('#preferencePlanCycle').show();
            $('#preferencePlanCycle').attr("disable", false);
        }

        changeAmount();

        var totalUserAssets = $('#totalUserAssets').val();
        var userInvestingAmount = $('#userInvestingAmount').val();
        var userAccumulatedIncome = $('#userAccumulatedIncome').val();
        var raiseRate = $('#raiseRate').val();
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

        }
    })
</script>