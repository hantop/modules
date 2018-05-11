
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
                <li class="active"><b>投资用户</b></li>
            </ol>
            <div>
                <form class="form-inline form-search" role="form" id="dataform"  action="${path}/cs/investUser/getRightsList">
                    <input type="hidden" value="${userId}" name="userId">
                    <input type="hidden" id="page" name="page">
                    <input type="hidden" id="fromPage" name="fromPage" value="true">


                    <div class="form-group">
                    <label class="control-label">标的名称：</label>
                    </div>
                    <div class="form-group">
                        <input class="form-control" name="productName" maxlength="30" value="${investRecord.productName}"/>
                    </div>


                    <div class="form-group">
                        <label class="control-label">创建日期：</label>
                    </div>
                    <div class="form-group">
                        <input class="form-control"  name="startTime" value="${startTime}" readonly="readonly" maxlength="30" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/> 至
                    </div>
                    <div class="form-group">
                        <input class="form-control" name="endTime" value="${endTime}" readonly="readonly" maxlength="30" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
                    </div>

                    <div class="form-group">
                        <label>标的状态：</label>
                    </div>
                    <div class="form-group">
                        <select class="form-control" name="bidStatus" >
                            <option value=""   <c:if test="${investRecord.bidStatus==''}">selected="selected"</c:if> >全部</option>
                            <option value="TBZ" <c:if test="${investRecord.bidStatus=='TBZ'}">selected="selected"</c:if> >投资中</option>
                            <option value="DFK" <c:if test="${investRecord.bidStatus=='DFK'}">selected="selected"</c:if> >待放款</option>
                            <option value="HKZ" <c:if test="${investRecord.bidStatus=='HKZ'}">selected="selected"</c:if> >还款中</option>
                            <option value="YJQ" <c:if test="${investRecord.bidStatus=='YJQ'}">selected="selected"</c:if> >已结清</option>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-primary">查询</button>
                </form>
            </div>
            <hr>
            <div class="table-responsive">
                <table id="datatable" class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>投资时间</th>
                        <th>计息时间</th>
                        <th>卖出时间</th>
                        <th>债权本金</th>
                        <th>卖出金额</th>
                        <th>手续费</th>
                        <th>实际收益</th>
                        <th>投资产品</th>
                        <th>年利率</th>
                        <th>期限</th>
                        <th>标的类型</th>

                    </tr>
                    </thead>


                    <c:forEach items="${rightsRecordList}" var="v" varStatus="vs">
                        <tr >
                            <td><fmt:formatDate value="${v.investTime  }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td><fmt:formatDate value="${v.sealedTime  }" pattern="yyyy-MM-dd"/></td>
                            <td><fmt:formatDate value="${v.soldTime  }" pattern="yyyy-MM-dd"/></td>
                            <td>${v.rightsMoney}</td>
                            <td>${v.soldAmount}</td>
                            <td>??</td>
                            <td>实际收益</td>
                            <td>${v.productName}</td>
                            <td><fmt:formatNumber value="${v.yearRate*100}" pattern="#0.00#"/>%</td>
                            <td>
                                <c:choose>
                                    <c:when test="${v.loanDays>0}">${v.loanDays}天</c:when>
                                    <c:when test="${v.loanMonths>0}">${v.loanMonths}个月</c:when>
                                    <c:otherwise>未知状态</c:otherwise>
                                </c:choose>
                            </td>

                            <td>${v.bidType}</td>

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
