<%@ page import="java.util.List" %>
<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!--My 97 date -->
<script type="text/javascript" src="/static/component/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/static/component/datepicker/calendar.js"></script>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/biz/index">业务管理</a></li>
                <li class="active"><b>还款管理</b></li>
            </ol>
            <form id="dataform" class="form-inline" method="post" action="${path}/biz/repayment">
                <div class="row" style="margin-left: 5px">
                    <div class="form-group">
                        <label>标的名称:</label>
                        <input type="text" id="title" name="title" value="${repaymentForm.title}"/>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <label>借款账号:</label>
                        <input type="text" id="borrowUserAccount" name="borrowUserAccount" value="${repaymentForm.borrowUserAccount}"/>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <label>收款账号:</label>
                        <input type="text" id="receiptAccount" name="receiptAccount" value="${repaymentForm.receiptAccount}">
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <label>还款日：</label>
                        <input id="repayDayStart" name="repayDayStart" type="text" readonly="readonly" maxlength="20"
                               class="form-control input-sm startDate" style="width:110px;"
                               onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" value="${repaymentForm.repayDayStart}"/>
                        --
                        <input id="repayDayEnd" name="repayDayEnd" type="text" readonly="readonly" maxlength="20"
                               class="form-control input-sm endDate" style="width:110px;"
                               onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" value="${repaymentForm.repayDayEnd}"/>
                        <input type="hidden" id="page" name="page">
                        <shiro:hasPermission name="repayment:search">
                        <button type="button" class="btn btn-primary btn-sm" id="searchBtn">查询</button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="repayment:export">
                        <button type="button" class="btn btn-success btn-sm export">导出</button>
                        </shiro:hasPermission>
                    </div>
                </div>
                <div class="row" style="margin-left: 5px;margin-top:10px;">
                </div>
            </form>
            <hr style="margin-bottom: 0;">
            <div class="panel panel-default" style="width:1800px!important;">
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                        <tr class="success">
							<th>序号</th>
                            <th>标的名称</th>
                            <th>借款用户</th>
                            <th>借款账号</th>
                            <th>收款用户</th>
                            <th>收款账号</th>
                            <th>还款方式</th>
                            <th>借款金额（元）</th>
                            <th>本期期数</th>
                            <th>距离还款日</th>
                            <th>还款日</th>
                            <th>状态</th>
                            <th>操作</th>
						</tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${list}" var="v" varStatus="vs">
                        <tr>
                            <td>${vs.index + 1}</td>
                            <td>${v.title}</td>
                            <td>${v.borrowUserName}</td>
                            <td>${v.borrowUserAccount}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${v.receiptName != null}">
                                        ${v.receiptName}
                                    </c:when>
                                    <c:otherwise>
                                        -
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${v.receiptAccount != null}">
                                        ${v.receiptAccount}
                                    </c:when>
                                    <c:otherwise>
                                        -
                                    </c:otherwise>
                                </c:choose></td>
                            <td>
                                <c:if test="${v.repayMethod == 'DEBX'}">
                                    等额本息
                                </c:if>
                                <c:if test="${v.repayMethod == 'MYFX'}">
                                    每月付息,到期还本
                                </c:if>
                                <c:if test="${v.repayMethod == 'YCFQ'}">
                                    本息到期一次付清
                                </c:if>
                                <c:if test="${v.repayMethod == 'DEBJ'}">
                                    等额本金
                                </c:if>
                            </td>
                            <td>${v.repayMoney}</td>
                            <td>${v.termDisplay}</td>
                            <td>${v.distanceRefund}</td>
                            <td>
                                <fmt:formatDate value="${v.repayDay}"
                                                pattern="yyyy-MM-dd"></fmt:formatDate>
                            </td>
                            <td>${v.status}</td>
                            <td>
                                <%--<c:if test="${v.repaying == 0}">
                                <a href="#" onclick="repay(${v.bidId}, '${v.title}', '${v.repayMethod}', true, false);">&nbsp;&nbsp;&nbsp;提前还款</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                <a href="#" onclick="repay(${v.bidId}, '${v.title}', '${v.repayMethod}', false, false);">还款</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                <a href="#" onclick="repay(${v.bidId}, '${v.title}', '${v.repayMethod}', true, true);">提前担保代偿</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                <a href="#" onclick="repay(${v.bidId}, '${v.title}', '${v.repayMethod}', false, true);">担保代偿</a>
                                </c:if>
                                <c:if test="${v.repaying == 1}">
                                    <font color="#AAAAAA">&nbsp;&nbsp;&nbsp;提前还款&nbsp;</font>
                                    <font color="#AAAAAA">&nbsp;&nbsp;&nbsp;还款&nbsp;</font>
                                    <font color="#AAAAAA">&nbsp;&nbsp;&nbsp;提前担保代偿&nbsp;</font>
                                    <font color="#AAAAAA">&nbsp;&nbsp;&nbsp;担保代偿&nbsp;</font>
                                </c:if>--%>
                                <shiro:hasPermission name="repayment:doRepay">
                                    <c:if test="${v.repaying == 0}">
                                        <c:choose>
                                            <c:when test="${v.prerepay}">

                                                <a href="#" onclick="prepayment(${v.bidId}, '${v.title}', '${v.repayMethod}', true, false,'${v.distanceRefund}','${v.repayDay}');">&nbsp;&nbsp;&nbsp;提前还款</a>&nbsp;&nbsp;&nbsp;&nbsp;

                                                <font color="#AAAAAA">&nbsp;&nbsp;&nbsp;还款&nbsp;</font>&nbsp;&nbsp;&nbsp;&nbsp;
                                                <a href="#" onclick="prepayment(${v.bidId}, '${v.title}', '${v.repayMethod}', true, true,'${v.distanceRefund}','${v.repayDay}');">&nbsp;&nbsp;&nbsp;提前担保代偿</a>&nbsp;&nbsp;&nbsp;&nbsp;

                                                <font color="#AAAAAA">&nbsp;&nbsp;&nbsp;担保代偿&nbsp;</font>
                                            </c:when>
                                            <c:otherwise>
                                                <font color="#AAAAAA">&nbsp;&nbsp;&nbsp;提前还款&nbsp;</font>&nbsp;&nbsp;&nbsp;&nbsp;
                                                <a href="#" onclick="repay(${v.bidId}, '${v.title}', '${v.repayMethod}', false, false);">&nbsp;&nbsp;&nbsp;还款</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                                <font color="#AAAAAA">&nbsp;&nbsp;&nbsp;提前担保代偿&nbsp;</font>&nbsp;&nbsp;&nbsp;&nbsp;
                                                <a href="#" onclick="repay(${v.bidId}, '${v.title}', '${v.repayMethod}', false, true);">&nbsp;&nbsp;&nbsp;担保代偿</a>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                                    <c:if test="${v.repaying == 1}">
                                        <c:if test="${v.orderState == null}">
                                            <c:choose>
                                                <c:when test="${v.prerepay}">
                                                    <a href="#" onclick="repay(${v.bidId}, '${v.title}', '${v.repayMethod}', true, false);">&nbsp;&nbsp;&nbsp;提前还款</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <font color="#AAAAAA">&nbsp;&nbsp;&nbsp;还款&nbsp;</font>&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <a href="#" onclick="repay(${v.bidId}, '${v.title}', '${v.repayMethod}', true, true);">&nbsp;&nbsp;&nbsp;提前担保代偿</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <font color="#AAAAAA">&nbsp;&nbsp;&nbsp;担保代偿&nbsp;</font>
                                                </c:when>
                                                <c:otherwise>
                                                    <font color="#AAAAAA">&nbsp;&nbsp;&nbsp;提前还款&nbsp;</font>&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <a href="#" onclick="repay(${v.bidId}, '${v.title}', '${v.repayMethod}', false, false);">&nbsp;&nbsp;&nbsp;还款</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <font color="#AAAAAA">&nbsp;&nbsp;&nbsp;提前担保代偿&nbsp;</font>&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <a href="#" onclick="repay(${v.bidId}, '${v.title}', '${v.repayMethod}', false, true);">&nbsp;&nbsp;&nbsp;担保代偿</a>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:if>
                                        <c:if test="${v.orderState != null}">
                                            <font color="#AAAAAA">&nbsp;&nbsp;&nbsp;提前还款</font>&nbsp;&nbsp;&nbsp;&nbsp;
                                            <font color="#AAAAAA">&nbsp;&nbsp;&nbsp;还款&nbsp;</font>&nbsp;&nbsp;&nbsp;&nbsp;
                                            <font color="#AAAAAA">&nbsp;&nbsp;&nbsp;提前担保代偿&nbsp;</font>&nbsp;&nbsp;&nbsp;&nbsp;
                                            <font color="#AAAAAA">&nbsp;&nbsp;&nbsp;担保代偿&nbsp;</font>
                                        </c:if>
                                    </c:if>

                                </shiro:hasPermission>
<%--给测试开权限
                                    <a href="#" onclick="prepayment(${v.bidId}, '${v.title}', '${v.repayMethod}', true, false,'${v.distanceRefund}','${v.repayDay}');">&nbsp;&nbsp;&nbsp;测试提前还款</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                    <a href="#" onclick="prepayment(${v.bidId}, '${v.title}', '${v.repayMethod}', true, true,'${v.distanceRefund}','${v.repayDay}');">&nbsp;&nbsp;&nbsp;测试提前担保代偿</a>&nbsp;&nbsp;&nbsp;&nbsp;

                                    <a href="#" onclick="repay(${v.bidId}, '${v.title}', '${v.repayMethod}', false, false);">&nbsp;&nbsp;&nbsp;测试正常还款</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                    <a href="#" onclick="repay(${v.bidId}, '${v.title}', '${v.repayMethod}', false, true);">&nbsp;&nbsp;&nbsp;测试正常担保代偿</a>
--%>

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

    $('#searchBtn').bind('click', function() {
        var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $("#dataform").submit();
        layer.close();
    });

    function pagination(page) {
        $('#page').val(page);
        var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $("#dataform").submit();
        layer.close();
    }

    function confirmTitle(isPreRepay, isSubrogation, title, data) {
        var title = null;
        //提前还款
        if(isPreRepay){
            //担保代偿
            if(isSubrogation){
                title = "提前担保代偿:";
            }else{
                title = "提前还款:";
            }
        }else{
            if(isSubrogation){
                title = "担保代偿:";
            }else{
                title = "还款:";
            }
        }
        return title;
    }

    function confirmMsg(isPreRepay, isSubrogation, title, data) {
        var msg = null;
        if(data != null){
            var repayBudgetVO = data.repayBudgetVO;
            var totalAmount = repayBudgetVO.totalAmount;// 需还总额
            var principal = repayBudgetVO.principal;// 需还本金
            var interest = repayBudgetVO.interest;// 需还利息
            var overduePenalty = repayBudgetVO.overduePenalty;// 罚息
            var overdueCommission = repayBudgetVO.overdueCommission;// 逾期手续费
            var prepayPenalty = repayBudgetVO.prepayPenalty;// 违约金
            var balance = repayBudgetVO.balance;// 账户余额
            var guaranteeName = repayBudgetVO.guaranteeName;// 担保账户
            var serviceFeeNotPay = repayBudgetVO.serviceFeeNotPay;//待收服务费
            msg = "标的名称&nbsp;&nbsp;:&nbsp;&nbsp;" + title + "<br/>" +
                  "需还本金&nbsp;&nbsp;:&nbsp;&nbsp;" + principal + "<br/>" +
                  "需还利息&nbsp;&nbsp;:&nbsp;&nbsp;" + interest + "<br/>"+
                  "利息管理费&nbsp;&nbsp;:&nbsp;&nbsp;" + repayBudgetVO.interestManagementFee + "<br/>"+
                  "待收服务费&nbsp;&nbsp;:&nbsp;&nbsp;" + serviceFeeNotPay + "<br/>";
            var tip = null;
            //提前还款
            if(isPreRepay){
                msg = msg + "违约金&nbsp;&nbsp;:&nbsp;&nbsp;" + prepayPenalty + "<br/>";
                //担保代偿
                if(isSubrogation){
                    msg = msg + "<br/>" + "担保账户&nbsp;&nbsp;:&nbsp;&nbsp;" + guaranteeName;
                    tip = "提前代偿为整个标提前还款，不支持当期提前代偿。";
                }else{
                    tip = "提前还款为整个标提前还款，不支持当期提前还款。";
                }
            }else{
                msg = msg + "逾期罚息&nbsp;&nbsp;:&nbsp;&nbsp;" + overduePenalty + "<br/>";
                msg = msg + "逾期手续费&nbsp;&nbsp;:&nbsp;&nbsp;" + overdueCommission + "<br/>";
                if(isSubrogation){
                    msg = msg + "<br/>" + "担保账户&nbsp;&nbsp;:&nbsp;&nbsp;" + guaranteeName;
                }
            }

            msg = msg + "<br/>" + "需还总金额&nbsp;&nbsp;:&nbsp;&nbsp;" + totalAmount + "";
            msg = msg + "<br/>" + "还款账户余额&nbsp;&nbsp;:&nbsp;&nbsp;" + balance + "<br/><br/>";
            if(tip != null){
                msg = msg + tip;
            }
        }
        return msg;
    }

    // 还款
    function repay(bidId, title, repayMethod, isPreRepay, isSubrogation){
        $.post(
                path + "/biz/repayment/repayDetailInfo",
                {
                    "bidId" : bidId,
                    "repayMethod" : repayMethod,
                    "isPreRepay" : isPreRepay,
                    "isSubrogation" : isSubrogation
                },
                function (data) {
                    var repayBudgetVO = data.repayBudgetVO;
                    var message = data.message;
                    if(message != null){
                        alert(message);
                        return;
                    }
                    if(repayBudgetVO != null) {
                        bootbox.confirm({
                            title: confirmTitle(isPreRepay, isSubrogation, title, data),
                            size: 'small',
                            message: confirmMsg(isPreRepay, isSubrogation, title, data),
                            callback: function (result) {
                                if(result) {

                                    layer.load(0, {shade: [0.4,'#fff',false]}); //进度条

                                    $.post(
                                            path + "/biz/repayment/doRepay",
                                            {
                                                "bidId" : bidId,
                                                "repayMethod" : repayMethod,
                                                "isPreRepay" : isPreRepay,
                                                "isSubrogation" : isSubrogation
                                            },
                                            function (data) {
                                                var resultCode = data.resultCode;
                                                var message = data.message;
                                                if(resultCode == "0000"||resultCode == "1111"||resultCode == 1111) {
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

                                                location.reload();
                                            }
                                    );
                                }
                            }
                        });
                    }
                },
                "json"
        );
    }

    (function ($) {
        $(".export").on('click', function () {
            var cloneForm = $("#dataform").clone(true);
            $(document.body).append(cloneForm);
            $(cloneForm).attr('action', '${path}/biz/repayment/export').submit();
            return false;
        });
    })(jQuery);


    /**
     * 提前还款
     * @param bidId
     * @param title
     * @param repayMethod
     * @param isPreRepay
     * @param isSubrogation
     * @param distanceRefund
     * @param repayDay
     */
    function prepayment(bidId, title, repayMethod, isPreRepay, isSubrogation,distanceRefund,repayDay){
        var url = path + "/biz/repayment/configPrepayment";
        var data ={
            "bidId" : bidId,
            "repayMethod" : repayMethod,
            "isPreRepay" : isPreRepay,
            "isSubrogation" : isSubrogation
        }
        window.location.href=url+"?a="+1
            +"&bidId="+bidId
            +"&title="+title
            +"&repayMethod="+repayMethod
            +"&isPreRepay="+isPreRepay
            +"&isSubrogation="+isSubrogation
            +"&distanceRefund="+distanceRefund
        +"&repayDay="+repayDay;
    }

</script>
