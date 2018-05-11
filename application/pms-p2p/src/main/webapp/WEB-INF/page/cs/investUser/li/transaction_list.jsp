
<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<script type="text/javascript" src="/static/component/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/static/component/datepicker/calendar.js"></script>
<style type="text/css">
    .table {
        table-layout: fixed;
    }
    /**去除bootstrap td 的边框*/
    table.account tr td {
        border: none;
    }
</style>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a>客服管理</a></li>
                <li class="active"><b>投资用户</b></li>
            </ol>

            <jsp:include page="../detailInfo_common.jsp"/>

            <div>
                <form class="form-inline form-search" role="form" id="dataform" action="${path}/cs/investUser/getTransactionList">
                    <input type="hidden" value="${userId}" name="userId">
                    <input type="hidden" id="page" name="page">
                    <input type="hidden" id="fromPage" name="fromPage" value="true">

                    <div class="form-group">
                        <label class="control-label">创建日期：</label>
                    </div>
                    <div class="form-group">
                        <input class="form-control" id="rechargeFormStartTime" name="startTime1" value="${startTime1}" readonly="readonly" maxlength="30" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/> 至
                    </div>
                    <div class="form-group">
                        <input class="form-control" id="rechargeFormEndTime" name="endTime1" value="${endTime1}" readonly="readonly" maxlength="30" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
                    </div>

                    <div class="form-group">
                        <label>订单号：</label>
                    </div>
                    <div class="form-group">
                        <input class="form-control" id="planFormTitle" name="orderNum" value="${tradeForm.orderNum}"/>
                    </div>

                    <div class="form-group">
                        <label>交易类型：</label>
                    </div>
                    <div class="form-group">
                        <select class="form-control" id="orderTypeSelectId" name="orderType" >
                            <option value="" <c:if test="${tradeForm.orderType == ''}">selected="selected"</c:if> >全部</option>
                            <c:forEach items="${tradeTypes}" var="r">
                                <option value="${r.id}" <c:if test="${tradeForm.orderType == r.id}">selected="selected"</c:if> >${r.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group">
                        <label>订单状态：</label>
                    </div>
                    <div class="form-group">
                        <select class="form-control" id="orderStatusSelectId" name="orderStatus" >
                            <option value="" <c:if test="${tradeForm.orderStatus == ''}">selected="selected"</c:if> >全部</option>
                            <option value="ING" <c:if test="${tradeForm.orderStatus == 'ING'}">selected="selected"</c:if> >处理中</option>
                            <option value="CG" <c:if test="${tradeForm.orderStatus == 'CG'}">selected="selected"</c:if> >成功</option>
                            <option value="SB" <c:if test="${tradeForm.orderStatus == 'SB'}">selected="selected"</c:if> >失败</option>
                        </select>
                    </div>

                    <button type="submit" class="btn btn-primary">查询</button>
                    <button type="button" class="btn btn-success btn-sm" id="exportBtn">导出</button>
                </form>
            </div>
            <hr>
            <div class="table-responsive">
                <table id="datatable" class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>创建时间</th>
                        <th>交易类型</th>
                        <th>交易金额</th>
                        <th>账户余额（元）</th>
                        <th>备注</th>
                        <th>订单状态</th>
                        <th>订单号</th>
                        <th>完成时间</th>
                    </tr>
                    </thead>
                    <c:forEach items="${list}" var="item" varStatus="vs">
                        <tr >
                            <td >
                                <c:if test="${item.startTime != null}">
                                    <fmt:formatDate value="${item.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </c:if>
                            </td>
                            <td> ${item.orderTypeName} </td>
                            <td> ${item.amount} </td>
                            <td> ${item.balance} </td>
                            <td> ${item.remark} </td>
                            <td> ${item.orderStatusName} </td>
                            <td> ${item.orderNum} </td>
                            <td>
                                <c:if test="${item.endTime != null}">
                                    <fmt:formatDate value="${item.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        <tiles:insertDefinition name="paginator"/>
    </div>
</div>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>

<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script>
    /**
     * 搜索
     */
    function searchList(){
        var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $("#dataform").submit();
    }
    // 导出
    $("#exportBtn").click(function () {
            var cloneForm = $("#dataform").clone(true);
            $(document.body).append(cloneForm);
            $(cloneForm).attr('action', '${path}/cs/investUser/export').submit();
            return false;
        }
    );
</script>
