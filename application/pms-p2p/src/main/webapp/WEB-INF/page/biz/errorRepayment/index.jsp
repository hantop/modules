<%@ page import="java.util.List" %>
<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<script type="text/javascript" src="/static/component/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/static/component/datepicker/calendar.js"></script>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/biz/index">业务管理</a></li>
                <li class="active"><b>异常还款管理</b></li>
            </ol>
            <form id="dataform" class="form-inline" method="post" action="${path}/biz/errorRepayment">
                <div class="row" style="margin-left: 5px">
                    <div class="form-group">
                        <label>订单ID:</label>
                        <input type="text" id="bidId" name="bidId"/>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <%--<label>借款账号:</label>
                        <input type="text" id="borrowUserAccount" name="borrowUserAccount" value="${repaymentForm.borrowUserAccount}"/>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <label>收款账号:</label>
                        <input type="text" id="receiptAccount" name="receiptAccount" value="${repaymentForm.receiptAccount}">
                        &nbsp;&nbsp;&nbsp;&nbsp;--%>
                        <button type="button" class="btn btn-primary btn-sm" id="searchBtn">异常还款</button>
                    </div>
                </div>
                <div class="row" style="margin-left: 5px;margin-top:10px;">
                </div>
            </form>
            <%--<hr style="margin-bottom: 0;">
            <div class="panel panel-default" style="width:1800px!important;">
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                        <tr class="success">
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
                        <tr>
                            <td>${errorRepayment.title}</td>
                            <td>${errorRepayment.borrowUserName}</td>
                            <td>${errorRepayment.borrowUserAccount}</td>
                            <td>${errorRepayment.receiptName}</td>
                            <td>${errorRepayment.receiptAccount}</td>
                            <td>
                                <c:if test="${errorRepayment.repayMethod == 'DEBX'}">
                                    等额本息
                                </c:if>
                                <c:if test="${errorRepayment.repayMethod == 'MYFX'}">
                                    每月付息,到期还本
                                </c:if>
                                <c:if test="${errorRepayment.repayMethod == 'YCFQ'}">
                                    本息到期一次付清
                                </c:if>
                                <c:if test="${errorRepayment.repayMethod == 'DEBJ'}">
                                    等额本金
                                </c:if>
                            </td>
                            <td>${errorRepayment.repayMoney}</td>
                            <td>${errorRepayment.termDisplay}</td>
                            <td>${errorRepayment.distanceRefund}</td>
                            <td>
                                <fmt:formatDate value="${v.repayDay}"
                                                pattern="yyyy-MM-dd"></fmt:formatDate>
                            </td>
                            <td>${errorRepayment.status}</td>
                            <td>
                                <c:if test="${errorRepayment.bidId > 0}">
                                <a href="#" onclick="errorRepay(${errorRepayment.bidId}, '${errorRepayment.title}');">异常还款</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                </c:if>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>--%>
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
        var bidId = $('#bidId').val();
        errorRepay(bidId);
        /*var index = layer.load(0, {
         shade: [0.4,'#fff',false] //0.1透明度的白色背景
         });
         $("#dataform").submit();
         layer.close();*/
    });

    // 异常还款
    function errorRepay(bidId){
        bootbox.confirm({
            title: '异常还款',
            size: 'small',
            message: '确定异常还款?',
            callback: function (result) {
                if(result) {
                    $.post(
                            path + "/biz/errorRepayment/doErrorRepay",
                            {
                                "bidId" : bidId
                            },
                            function (data) {
                                var resultCode = data.resultCode;
                                var message = data.message;
                                if(resultCode == "0000") {
                                    alert("操作异常还款成功!");
                                } else if (resultCode == "500"){
                                    alert("操作异常还款失败，内部错误。");
                                } else if(message != null){
                                    alert(message);
                                }
                                // 刷新页面
                                location.reload();
                            }
                    );
                }
            }
        });
    }

</script>
