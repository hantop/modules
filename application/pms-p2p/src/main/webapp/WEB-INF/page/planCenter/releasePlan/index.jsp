<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!--My 97 date -->
<script type="text/javascript" src="/static/component/datepicker/calendar.js"></script>
<link rel="stylesheet" href="/static/component/HoldOn/HoldOn.min.css">
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script type="text/javascript" src="/static/component/HoldOn/HoldOn.min.js"></script>
<script type="text/javascript" src="/static/component/jquery-form/jquery.form.js"></script>
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script type="text/javascript" src="/static/component/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/static/component/HoldOn/HoldOn.min.js"></script>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>

<link rel="stylesheet" href="/static/component/HoldOn/HoldOn.min.css">
<style type="text/css">
    .red_font {
        color: red;
    }
</style>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/planCenter/index">计划中心</a></li>
                <li class="active"><b>计划发布</b></li>
            </ol>
                <shiro:hasPermission name="releasePlan:create">
                <div class="form-group">
                    <a class="btn btn-info" href="${path}/planCenter/releasePlan/edit"
                       role="button">创建计划</a>
                </div><br/>
               </shiro:hasPermission>
            <form id="dataform" class="form-inline" method="post" action="${path}/planCenter/releasePlan/index">
                <div class="form-group">
                    <label>名称：</label>
                    <input id="name" name="name" value="${planMarketSettingForm.name}" type="text" class="form-control input-sm"/>
                </div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <div class="form-group">
                    <label>处理时间：</label>
                    <input id="beginDate"  name="timeStart"  type="text" readonly="readonly" maxlength="20" class="form-control input-sm" style="width:163px;"
                           value="${planMarketSettingForm.timeStart}"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd', isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'});"
                           onFocus="WdatePicker({isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'})" />
                    --
                    <input id="endDate" name="timeEnd" type="text" readonly="readonly" maxlength="20" class="form-control input-sm" style="width:163px;"
                           value="${planMarketSettingForm.timeEnd}"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd', isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'});"
                           onFocus="WdatePicker({isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'})" />
                </div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <div class="form-group">
                    <label>计划类型：</label>
                    <select id="productType" name="productType" class="form-control input-sm"
                            style="width:115px;" value="${planMarketSettingForm.productType}">
                        <option value="0">全部</option>
                        <option value="1"
                                <c:if test='${planMarketSettingForm.productType == 1}'>selected="selected"</c:if>>月升计划</option>
                        <option value="2"
                                <c:if test='${planMarketSettingForm.productType == 2}'>selected="selected"</c:if>>省心计划</option>
                    </select>
                </div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <div class="row" style="margin-top: 10px;">
                <div class="col-md-12">
                <div class="form-group">
                    <label>状态</label>
                    <select id="status" name="status" class="form-control input-sm"
                            style="width:115px;" value="${planMarketSettingForm.status}">
                        <option value="0"
                                <c:if test='${planMarketSettingForm.status == 0}'>selected="selected"</c:if>>全部</option>
                        <option value="1"
                                <c:if test='${planMarketSettingForm.status == 1}'>selected="selected"</c:if>>待提交</option>
                        <option value="2"
                                <c:if test='${planMarketSettingForm.status == 2}'>selected="selected"</c:if>>待审核</option>
                        <option value="3"
                                <c:if test='${planMarketSettingForm.status == 3}'>selected="selected"</c:if>>待发布</option>
                        <option value="-1"
                                <c:if test='${planMarketSettingForm.status == -1}'>selected="selected"</c:if>>已发布</option>
                    </select>
                </div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <div class="form-group">
                    <label>系统类型：</label>
                    <select id="isCG" name="isCG" class="form-control input-sm"
                            style="width:115px;" value="${planMarketSettingForm.isCG}">
                        <option value="0">全部</option>
                        <option value="1"
                                <c:if test='${planMarketSettingForm.isCG == 1}'>selected="selected"</c:if>>普通</option>
                        <option value="2"
                                <c:if test='${planMarketSettingForm.isCG == 2}'>selected="selected"</c:if>>存管</option>
                    </select>
                </div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="hidden" id="page" name="page">
                <shiro:hasPermission name="releasePlan:view">
                    <button id="searchBtn" type="submit" class="btn btn-primary btn-sm">搜索</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="releasePlan:export">
                    <button class="btn btn-primary btn-sm export" type="button"  role="button">导出</button>
                </shiro:hasPermission>
                </div>
                </div>
            </form>
            <hr style="margin-bottom: 0;">
            <div class="panel panel-default" style="width:1300px!important;">
                <div class="panel-heading">计划列表</div>
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>系统类型</th>
                        <th>计划名称</th>
                        <th>计划金额(元)</th>
                        <th>计划期限</th>
                        <th>投资利率</th>
                        <th>利率月增幅/加息</th>
                        <th>处理时间</th>
                        <th>状态</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    	<c:forEach var="item" items="${list}">
                            <tr>
                                <td>
                                    <c:choose>
                                        <c:when test="${item.isCG == 1}">
                                            普通
                                        </c:when>
                                        <c:when test="${item.isCG == 2}">
                                            存管
                                        </c:when>
                                    </c:choose>
                                </td>
                                <td>${item.name}</td>
                                <td>${item.amount}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${item.cycleType == 'm'}">
                                            ${item.cycle}个月
                                        </c:when>
                                        <c:when test="${item.cycleType == 'd'}">
                                            ${item.cycle}天
                                        </c:when>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${item.investRate > 0}">
                                            <fmt:formatNumber value="${item.investRate}" pattern="#.##" type="number"/>%
                                        </c:when>
                                        <c:otherwise>
                                            <fmt:formatNumber value="${item.minYearlyRate}" pattern="#.##" type="number"/>%
                                            -
                                            <fmt:formatNumber value="${item.maxYearlyRate}" pattern="#.##" type="number"/>%
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${item.type == 1 && item.raiseRate != null}">
                                            <fmt:formatNumber value="${item.moIncreaseRate}" pattern="#.##" type="number"/>%
                                            /<fmt:formatNumber value="${item.raiseRate}" pattern="#.##" type="number"/>%
                                        </c:when>
                                        <c:when test="${item.type == 1 && item.raiseRate == null}">
                                            <fmt:formatNumber value="${item.moIncreaseRate}" pattern="#.##" type="number"/>%
                                        </c:when>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${item.type == 2 && item.raiseRate != null}">
                                            <fmt:formatNumber value="${item.raiseRate}" pattern="#.##" type="number"/>%
                                        </c:when>
                                        <c:when test="${item.type == 2 && item.raiseRate == null}">
                                            -
                                        </c:when>
                                    </c:choose>
                                </td>
                                <td><fmt:formatDate value="${item.updateTime}"
										pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
								</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${item.status == 1}">
                                            待提交
                                        </c:when>
                                        <c:when test="${item.status == 2}">
                                            待审核
                                        </c:when>
                                        <c:when test="${item.status == 3}">
                                            待发布
                                        </c:when>
                                        <c:when test="${item.status == 4}">
                                            投资中
                                        </c:when>
                                        <c:when test="${item.status == 5}">
                                            还款中
                                        </c:when>
                                        <c:when test="${item.status == 6}">
                                            已结清
                                        </c:when>
                                        <c:when test="${item.status == 7}">
                                            已作废
                                        </c:when>
                                        <c:when test="${item.status == 8}">
                                            预发布
                                        </c:when>
                                        <c:when test="${item.status == 9}">
                                            已流计划
                                        </c:when>
                                        <c:otherwise>
                                            -
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <shiro:hasPermission name="releasePlan:create">
                                        <a class="glyphicon glyphicon-eye-open" href="${path}/planCenter/releasePlan/view?id=${item.id}" role="button">查看</a>
                                    <c:choose>
                                        <c:when test="${item.status == 1}">
                                            <a class="glyphicon glyphicon-pencil" href="${path}/planCenter/releasePlan/edit?id=${item.id}" role="button">编辑</a>
                                        </c:when>
                                        <c:otherwise>
                                            <font color="#AAAAAA">&nbsp;&nbsp;&nbsp;编辑&nbsp;</font>
                                        </c:otherwise>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${item.status == 2}">
                                            <a href="${path}/planCenter/releasePlan/audit?id=${item.id}" role="button">&nbsp;&nbsp;&nbsp;审核&nbsp;</a>
                                        </c:when>
                                        <c:otherwise>
                                            <font color="#AAAAAA">&nbsp;&nbsp;&nbsp;审核&nbsp;</font>
                                        </c:otherwise>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${item.status == 3}">
                                            <c:choose>
                                                <c:when test="${item.type == 1}">
                                                    <a href="#" onclick="release(1, ${item.id}, '${item.name}', ${item.minYearlyRate}, ${item.maxYearlyRate}, ${item.moIncreaseRate}, null);" role="button">&nbsp;&nbsp;&nbsp;发布&nbsp;</a>
                                                    <a href="#" onclick="invalid(1, ${item.id}, '${item.name}', ${item.minYearlyRate}, ${item.maxYearlyRate}, ${item.moIncreaseRate}, null);" role="button">&nbsp;&nbsp;&nbsp;作废&nbsp;</a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="#" onclick="release(2, ${item.id}, '${item.name}', null, null, null, ${item.investRate});" role="button">&nbsp;&nbsp;&nbsp;发布&nbsp;</a>
                                                    <a href="#" onclick="invalid(2, ${item.id}, '${item.name}', null, null, null, ${item.investRate});" role="button">&nbsp;&nbsp;&nbsp;作废&nbsp;</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            <font color="#AAAAAA">&nbsp;&nbsp;&nbsp;发布&nbsp;</font>
                                            <font color="#AAAAAA">&nbsp;&nbsp;&nbsp;作废&nbsp;</font>
                                        </c:otherwise>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${item.status == 4 || item.status == 3 || item.status == 8}">
                                            <c:choose>
                                                <c:when test="${item.sticktopTime == null}">
                                                    <c:choose>
                                                        <c:when test="${item.type == 1}">
                                                            <a href="#" onclick="sticktop(1, ${item.id}, '${item.name}', ${item.minYearlyRate}, ${item.maxYearlyRate}, ${item.moIncreaseRate}, null);" role="button">&nbsp;&nbsp;&nbsp;列表置顶&nbsp;</a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a href="#" onclick="sticktop(2, ${item.id}, '${item.name}', null, null, null, ${item.investRate});" role="button">&nbsp;&nbsp;&nbsp;列表置顶&nbsp;</a>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:choose>
                                                        <c:when test="${item.type == 1}">
                                                            <a href="#" onclick="cancelSticktop(1, ${item.id}, '${item.name}', ${item.minYearlyRate}, ${item.maxYearlyRate}, ${item.moIncreaseRate}, null);" role="button">&nbsp;&nbsp;&nbsp;取消列表置顶&nbsp;</a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a href="#" onclick="cancelSticktop(2, ${item.id}, '${item.name}', null, null, null, ${item.investRate});" role="button">&nbsp;&nbsp;&nbsp;取消列表置顶&nbsp;</a>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:otherwise>
                                            </c:choose>
                                            <c:choose>
                                                <c:when test="${item.recommendTime == null}">
                                                    <c:choose>
                                                        <c:when test="${item.type == 1}">
                                                            <a href="#" onclick="recommend(1, ${item.id}, '${item.name}', ${item.minYearlyRate}, ${item.maxYearlyRate}, ${item.moIncreaseRate}, null);" role="button">&nbsp;&nbsp;&nbsp;首页推荐&nbsp;</a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a href="#" onclick="recommend(2, ${item.id}, '${item.name}', null, null, null, ${item.investRate});" role="button">&nbsp;&nbsp;&nbsp;首页推荐&nbsp;</a>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:choose>
                                                        <c:when test="${item.type == 1}">
                                                            <a href="#" onclick="cancelRecommend(1, ${item.id}, '${item.name}', ${item.minYearlyRate}, ${item.maxYearlyRate}, ${item.moIncreaseRate}, null);" role="button">&nbsp;&nbsp;&nbsp;取消首页推荐&nbsp;</a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a href="#" onclick="cancelRecommend(2, ${item.id}, '${item.name}', null, null, null, ${item.investRate});" role="button">&nbsp;&nbsp;&nbsp;取消首页推荐&nbsp;</a>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:otherwise>
                                            </c:choose>
                                            <c:choose>
                                                <c:when test="${item.type == 1}">
                                                    <a href="#" onclick="termination(1, ${item.id}, '${item.name}', ${item.minYearlyRate}, ${item.maxYearlyRate}, ${item.moIncreaseRate}, null);" role="button">&nbsp;&nbsp;&nbsp;终止&nbsp;</a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="#" onclick="termination(2, ${item.id}, '${item.name}', null, null, null, ${item.investRate});" role="button">&nbsp;&nbsp;&nbsp;终止&nbsp;</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            <font color="#AAAAAA">&nbsp;&nbsp;&nbsp;列表置顶&nbsp;</font>
                                            <font color="#AAAAAA">&nbsp;&nbsp;&nbsp;首页推荐&nbsp;</font>
                                            <font color="#AAAAAA">&nbsp;&nbsp;&nbsp;终止&nbsp;</font>
                                        </c:otherwise>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${item.status == 3 && item.releaseTime == null}">
                                            <a href="#" onclick="showiTmingReleaseDiv(${item.id});" role="button">&nbsp;&nbsp;&nbsp;定时发布&nbsp;</a>
                                        </c:when>
                                        <c:when test="${item.status == 8 }">
                                            <c:choose>
                                                <c:when test="${item.type == 1}">
                                                    <a href="#" onclick="cancelTimingRelease(1, ${item.id}, '${item.name}', ${item.minYearlyRate}, ${item.maxYearlyRate}, ${item.moIncreaseRate}, null);" role="button">&nbsp;&nbsp;&nbsp;取消定时&nbsp;</a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="#" onclick="cancelTimingRelease(2, ${item.id}, '${item.name}', null, null, null, ${item.investRate});" role="button">&nbsp;&nbsp;&nbsp;取消定时&nbsp;</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>
                                            <font color="#AAAAAA">&nbsp;&nbsp;&nbsp;定时发布&nbsp;</font>
                                        </c:otherwise>
                                    </c:choose>
                                    </shiro:hasPermission>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
            <tiles:insertDefinition name="paginator"/>
        </div>
    </div>
</div>

<div id="timingReleaseDiv" data-backdrop="static" class="modal fade" role="dialog">
    <div class="modal-dialog plupload modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title text-center text-success"><b>定时发布</b></h5>
            </div>
            <form>
                <div class="form-group">
                    <label>发布时间</label>
                    <input id="releaseTime" name="releaseTime" type="text" readonly="readonly" maxlength="20"
                           class="form-control input-sm" style="width:163px;"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});"/>
                </div>
                <div class="form-group">
                    <label>显示时间</label>
                    <%--<textarea id="nopassProcessingOpinion" rows="6" class="form-control"></textarea>--%>
                    发布前<input id="displayBefore" name="displayBefore" type="text" class="form-control input-sm" style="width:50px;" />小时
                </div>
                <div class="form-group text-center">
                    <input type="hidden" id="planId">
                    <button type="button" class="btn btn-success" onclick="javascript: doTimingRelease();">确定</button>
                    <button type="button" class="btn btn-warning" onclick="javascript: $('#timingReleaseDiv').modal('hide');">取消</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div id="tips" class="modal fade">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="mySmallModalLabel">Tips</h4>
            </div>
            <div class="modal-body tips"></div>
        </div>
    </div>
</div>

<script type="text/javascript">
    (function ($) {
        $(".export").on('click', function () {
            var cloneForm = $(this).parents('form.form-inline').clone(true);
            $(document.body).append(cloneForm);
            $(cloneForm).attr('action', '${path}/planCenter/releasePlan/export').submit();
            return false;
        });
    })(jQuery);

    function pagination(page) {
        $('#page').val(page);
        var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $("#dataform").submit();
        layer.close();
    }


    function confirmMsg(type, name, minRate, maxRate, increaseRate, investRate) {
        var msg = null;
        if (type == 1) {
            msg = "计划名称:" + name + "<br/>" + "投资利率:" + minRate + "%-" + maxRate + "%" + "<br/>" + "利率月增幅/加息:" + increaseRate + "%";
        } else {
            msg = "计划名称:" + name + "<br/>" + "投资利率:" + investRate + "%";
        }
        return msg;
    }

    function release(type, id, name, minRate, maxRate, increaseRate, investRate) {
        bootbox.confirm({
            title: '发布',
            size: 'small',
            message: confirmMsg(type, name, minRate, maxRate, increaseRate, investRate),
            callback: function (result) {
                if (result) {
                    $.post(
                            path + "/planCenter/releasePlan/release",
                            {"id" : id},
                            function (data) {
                                var code = data.code;
                                if(code == "200"){
                                    bootbox.alert({message: "发布成功", title: '操作信息',callback: function() {
                                        window.location.reload(true);}});
                                }else{
                                    bootbox.alert({message: "发布失败，请稍后再试", title: '错误信息'});
                                }
                            }
                    );
                }
            }
        })
    }

    function invalid(type, id, name, minRate, maxRate, increaseRate, investRate) {
        bootbox.confirm({
            title: '作废',
            size: 'small',
            message: confirmMsg(type, name, minRate, maxRate, increaseRate, investRate),
            callback: function (result) {
                if (result) {
                    $.post(
                            path + "/planCenter/releasePlan/invalid",
                            {"id" : id},
                            function (data) {
                                var code = data.code;
                                if(code == "200"){
                                    bootbox.alert({message: "作废成功", title: '操作信息',callback: function() {
                                        window.location.reload(true);}});
                                }else{
                                    bootbox.alert({message: "作废失败，请稍后再试", title: '错误信息'});
                                }
                            }
                    );
                }
            }
        })
    }

    function sticktop(type, id, name, minRate, maxRate, increaseRate, investRate) {
        bootbox.confirm({
            title: '列表置顶',
            size: 'small',
            message: confirmMsg(type, name, minRate, maxRate, increaseRate, investRate),
            callback: function (result) {
                if (result) {
                    $.post(
                            path + "/planCenter/releasePlan/sticktop",
                            {"id" : id},
                            function (data) {
                                var code = data.code;
                                if(code == "200"){
                                    bootbox.alert({message: "列表置顶成功", title: '操作信息',callback: function() {
                                        window.location.reload(true);}});
                                }else{
                                    bootbox.alert({message: "列表置顶失败，请稍后再试", title: '错误信息'});
                                }
                            }
                    );
                }
            }
        });
    }

    function recommend(type, id, name, minRate, maxRate, increaseRate, investRate) {
        bootbox.confirm({
            title: '首页推荐',
            size: 'small',
            message: confirmMsg(type, name, minRate, maxRate, increaseRate, investRate),
            callback: function (result) {
                if (result) {
                    $.post(
                            path + "/planCenter/releasePlan/recommend",
                            {"id" : id},
                            function (data) {
                                var code = data.code;
                                if(code == "200"){
                                    bootbox.alert({message: "首页推荐成功", title: '操作信息',callback: function() {
                                        window.location.reload(true);}});
                                }else{
                                    bootbox.alert({message: "首页推荐失败，请稍后再试", title: '错误信息'});
                                }
                            }
                    );
                }
            }
        });
    }

    function cancelRecommend(type, id, name, minRate, maxRate, increaseRate, investRate) {
        bootbox.confirm({
            title: '取消首页推荐',
            size: 'small',
            message: confirmMsg(type, name, minRate, maxRate, increaseRate, investRate),
            callback: function (result) {
                if (result) {
                    $.post(
                            path + "/planCenter/releasePlan/cancelRecommend",
                            {"id" : id},
                            function (data) {
                                var code = data.code;
                                if(code == "200"){
                                    bootbox.alert({message: "取消首页推荐成功", title: '操作信息',callback: function() {
                                        window.location.reload(true);}});
                                }else{
                                    bootbox.alert({message: "取消首页推荐失败，请稍后再试", title: '错误信息'});
                                }
                            }
                    );
                }
            }
        });
    }

    function cancelSticktop(type, id, name, minRate, maxRate, increaseRate, investRate) {
        bootbox.confirm({
            title: '取消列表置顶',
            size: 'small',
            message: confirmMsg(type, name, minRate, maxRate, increaseRate, investRate),
            callback: function (result) {
                if (result) {
                    $.post(
                            path + "/planCenter/releasePlan/cancelSticktop",
                            {"id" : id},
                            function (data) {
                                var code = data.code;
                                if(code == "200"){
                                    bootbox.alert({message: "取消列表置顶成功", title: '操作信息',callback: function() {
                                        window.location.reload(true);}});
                                }else{
                                    bootbox.alert({message: "取消列表置顶失败，请稍后再试", title: '错误信息'});
                                }
                            }
                    );
                }
            }
        });
    }

    function termination(type, id, name, minRate, maxRate, increaseRate, investRate) {
        bootbox.confirm({
            title: '终止',
            size: 'small',
            message: confirmMsg(type, name, minRate, maxRate, increaseRate, investRate),
            callback: function (result) {
                if (result) {
                    $.post(
                            path + "/planCenter/releasePlan/termination",
                            {"id" : id},
                            function (data) {
                                var code = data.code;
                                if(code == "200"){
                                    bootbox.alert({message: "终止成功", title: '操作信息',callback: function() {
                                        window.location.reload(true);}});
                                }else{
                                    bootbox.alert({message: "终止失败，请稍后再试", title: '错误信息'});
                                }
                            }
                    );
                }
            }
        });
    }

    function timingRelease(type, id, name, minRate, maxRate, increaseRate, investRate) {
        bootbox.confirm({
            title: '定时发布',
            size: 'small',
            message: confirmMsg(type, name, minRate, maxRate, increaseRate, investRate),
            callback: function (result) {
                if (result) {
                    $.post(
                            path + "/planCenter/releasePlan/timingRelease",
                            {"id" : id},
                            function (data) {
                                var code = data.code;
                                if(code == "200"){
                                    bootbox.alert({message: "定时发布成功", title: '操作信息',callback: function() {
                                        window.location.reload(true);}});
                                }else{
                                    bootbox.alert({message: "定时发布失败，请稍后再试", title: '错误信息'});
                                }
                            }
                    );
                }
            }
        });
    }

    function cancelTimingRelease(type, id, name, minRate, maxRate, increaseRate, investRate) {
        bootbox.confirm({
            title: '取消定时发布',
            size: 'small',
            message: confirmMsg(type, name, minRate, maxRate, increaseRate, investRate),
            callback: function (result) {
                if (result) {
                    $.post(
                            path + "/planCenter/releasePlan/cancelTimingRelease",
                            {"id" : id},
                            function (data) {
                                var code = data.code;
                                if(code == "200"){
                                    bootbox.alert({message: "取消定时发布成功", title: '操作信息',callback: function() {
                                        window.location.reload(true);}});
                                }else{
                                    bootbox.alert({message: "取消定时发布失败，请稍后再试", title: '错误信息'});
                                }
                            }
                    );
                }
            }
        });
    }

    function showiTmingReleaseDiv(id){
        $('#planId').val(id);
        $('#timingReleaseDiv').modal('show');
    }


    function doTimingRelease() {
        var id = $("#planId").val();
        var releaseTime = $.trim($("#releaseTime").val());
        var displayBefore = $.trim($("#displayBefore").val());

        if (id) {
            $.ajax({
                method: "post",
                url: path + "/planCenter/releasePlan/timingRelease",
                dataType: "json",
                data: {
                    id : id,
                    releaseTime : releaseTime,
                    displayBefore : displayBefore
                }
            }).success(function (event) {
                if (event.code == 200) {
                    $('#timingReleaseDiv').modal("hide");
                    $(".modal-body.tips").html("<p class='text-success'><b>操作成功</b></p>");
                    $("#tips").modal("show").on('hide.bs.modal', function (e) {
                        window.location.reload(true);
                        return false;
                    });
                } else {
                    $(".modal-body.tips").html("<p class='text-success'><b>请求失败</b></p>");
                    $("#tips").modal("show");
                    return false;
                }
            }).error(function (event) {
                $('#timingReleaseDiv').modal("hide");
                $(".modal-body.tips").html("<p class='text-danger'><b>服务器异常</b></p>");
                $("#tips").modal("show");
                return false;
            });
        } else {
            $(".modal-body.tips").html("<p class='text-danger'><b>参数不能为空</b></p>");
            $("#tips").modal("show");
            return false;
        }
    }
</script>