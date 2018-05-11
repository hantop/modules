
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
</style>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a>客服管理</a></li>
                <li class="active"><a>存管投资用户信息</a></li>
                <li class="active"><b>计划详情</b></li>
            </ol>

            <div>
                <form class="form-inline form-search" role="form" id="dataform"   action="${path}/cs/investUser/getPlanDetail">
                    <input type="hidden" value="${userId}" name="userId">
                    <input type="hidden" id="page" name="page">
                    <input type="hidden" id="fromPage" name="fromPage" value="true">
                </form>
            </div>


            <div class="table-responsive">
                <table id="datatable" class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>标的类型</th>
                        <th>标的名称</th>
                        <th>匹配金额</th>
                        <th>年利率</th>
                        <th>标的期限</th>
                        <th>还款方式</th>
                        <th>匹配时间</th>
                        <th>退出时间</th>
                        <th>标的状态</th>

                    </tr>
                    </thead>


                    <c:forEach items="${details}" var="v" varStatus="vs">
                        <tr >
                            <td>${v.bidType}</td>
                            <td>${v.bidName}</td>
                            <td>${v.matchAmount}</td>

                            <td><fmt:formatNumber value="${v.rate*100}" pattern="#0.00#"/>%</td>

                            <td>
                                <c:choose>
                                    <c:when test="${v.loanDays>0}">${v.loanDays}天</c:when>
                                    <c:when test="${v.loanMonths>0}">${v.loanMonths}个月</c:when>
                                    <c:otherwise>未知状态</c:otherwise>
                                </c:choose>
                            </td>

                            <td>
                                <c:choose>
                                    <c:when test="${v.paybackWay=='DEBX'}">等额本息</c:when>
                                    <c:when test="${v.paybackWay=='MYFX'}">每月付息,到期还本</c:when>
                                    <c:when test="${v.paybackWay=='YCFQ'}">本息到期一次付清</c:when>
                                    <c:when test="${v.paybackWay=='DEBJ'}">等额本金</c:when>
                                    <c:otherwise>未知状态</c:otherwise>
                                </c:choose>
                            </td>

                            <td><fmt:formatDate value="${v.matchTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>

                            <td>
                                <c:if test="${v.bidStatus == 'YJQ'}">
                                    <fmt:formatDate value="${v.exitTime}" pattern="yyyy-MM-dd"/>
                                </c:if>
                            </td>

                            <td>
                                <c:choose>
                                    <c:when test="${v.bidStatus=='SQZ'}">申请中</c:when>
                                    <c:when test="${v.bidStatus=='DSH'}">待审核</c:when>
                                    <c:when test="${v.bidStatus=='DFB'}">待发布</c:when>
                                    <c:when test="${v.bidStatus=='YFB'}">预发布</c:when>
                                    <c:when test="${v.bidStatus=='TBZ'}">投标中</c:when>
                                    <c:when test="${v.bidStatus=='DFK'}">待放款</c:when>
                                    <c:when test="${v.bidStatus=='HKZ'}">还款中</c:when>
                                    <c:when test="${v.bidStatus=='YJQ'}">已结清</c:when>
                                    <c:when test="${v.bidStatus=='YLB'}">已流标</c:when>
                                    <c:when test="${v.bidStatus=='YDF'}">已垫付</c:when>
                                    <c:when test="${v.bidStatus=='YZF'}">已作废</c:when>
                                    <c:when test="${v.bidStatus=='DFZ'}">垫付中</c:when>
                                    <c:otherwise>未知状态</c:otherwise>
                                </c:choose>
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
     * 查看
     */
    function detail(userId){
        window.location.href="${path}/cs/investUser/getDetailInfo?userId="+userId;
    }
    /**
     * 搜索
     */
    function searchList(){
        var moblie = $("#moblie").val();
        var username = $("#username").val();
        var idCard = $("#idCard").val();
        if(moblie==""&&username==""&&idCard==""){
            bootbox.alert({message: "<h3>请至少填写一项</h3>", title: '错误信息'});
        }else{
            var index = layer.load(0, {
                shade: [0.4,'#fff',false] //0.1透明度的白色背景
            });
            $("#dataform").submit();
        }
    }

</script>
