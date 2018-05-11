
<%@page language="java" pageEncoding="utf-8" %>
<%@ page import="java.util.Date"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


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
                <li class="active"><a>客服管理</a></li>
                <li class="active"><a>存管投资用户信息</a></li>
                <li class="active"><b>计划详情</b></li>
            </ol>


            <jsp:include page="../detailInfo_common.jsp"/>

            <div>
                <form class="form-inline form-search" role="form" id="dataform">
                    <input type="hidden" value="${userId}" name="userId">
                    <input type="hidden" id="page" name="page">
                    <input type="hidden" id="fromPage" name="fromPage" value="true">
                    <div class="form-group">
                        <label class="control-label">名称：</label>
                    </div>
                    <div class="form-group">
                        <input class="form-control"value="${userInvestPlan.name}" id="planFormTitle" name="name" maxlength="30" />
                    </div>
                    &nbsp;&nbsp;
                    <button type="submit" class="btn btn-primary">查询</button>
                </form>
            </div>
            <hr>
            <div class="table-responsive">
                <table id="datatable" class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>名称</th>
                        <th>投资时间</th>
                        <th>计息时间</th>
                        <th>到期时间</th>
                        <th>投资金额</th>
                        <th>年利率</th>
                        <th>计划期限</th>
                        <th>投资收益</th>
                        <th>奖励</th>
                        <th>状态</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <c:forEach items="${list}" var="v" varStatus="vs">
                        <tr >
                            <td >${v.name}</td>
                            <td><fmt:formatDate value="${v.investTime  }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td><fmt:formatDate value="${v.bearrateDate  }" pattern="yyyy-MM-dd"/></td>
                            <td><fmt:formatDate value="${v.expireTime  }" pattern="yyyy-MM-dd"/></td>
                            <td>${v.tradeAmount}</td>
                            <td><fmt:formatNumber value="${(v.investRate + v.bidScope + v.scope) * 100}" pattern="#0.00#"/>%</td>
                            <td>
                                <c:choose>
                                    <c:when test="${v.cycleType=='d'}">${v.cycle}天</c:when>
                                    <c:when test="${v.cycleType=='m'}">${v.cycle}个月</c:when>
                                    <c:otherwise>未知状态</c:otherwise>
                                </c:choose>
                            </td>
                            <td>${v.interestAmount}</td>
                            <td>
                                <c:if test="${v.scope > 0}">
                                    加息券: <fmt:formatNumber value="${v.scope * 100}" pattern="#0.00#"/>%、
                                </c:if>
                                <c:if test="${v.bidScope > 0}">
                                    计划加息: <fmt:formatNumber value="${v.bidScope * 100}" pattern="#0.00#"/>%、
                                </c:if>
                                <c:if test="${v.redPacketMoney > 0}">
                                    返现券:${v.redPacketMoney}元
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${v.settleTime != null}">
                                    <fmt:formatDate value="${v.settleTime}" type="both" dateStyle="long" pattern="yyyy-MM-dd" var="settleTime"></fmt:formatDate>
                                </c:if>
                                <c:if test="${v.expireTime != null}">
                                    <fmt:formatDate value="${v.expireTime}" type="both" dateStyle="long" pattern="yyyy-MM-dd" var="expireTime"></fmt:formatDate>
                                </c:if>
                                <c:if test="${v.applyExitTime != null}">
                                    <fmt:formatDate value="${v.applyExitTime}" type="both" dateStyle="long" pattern="yyyy-MM-dd" var="applyExitTime"></fmt:formatDate>
                                </c:if>
                                <fmt:formatDate value="<%=new Date()%>" type="both" dateStyle="long" pattern="yyyy-MM-dd" var="nowTime"></fmt:formatDate>
                                <c:if test="${v.status == '4'}">投资中</c:if>
                                <c:if test="${v.status == '5' and (nowTime < expireTime)}">收益中</c:if>
                                <c:if test="${v.status == '6' and (settleTime >= expireTime)}">已结清</c:if>
                                <c:if test="${v.status == '5' and (v.applyExitTime != null or (nowTime > expireTime))}">退出中</c:if>
                                <c:if test="${v.status == '6' and (settleTime < expireTime)}">已退出</c:if>
                            </td>
                            <td>
                                <a href="${path}/cs/investUser/getPlanDetail?userId=${v.userId}&planId=${v.recordId}">查看</a>
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
