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
                <li class="active"><b>发标管理</b></li>
            </ol>
            <form id="dataform" class="form-inline" method="post" action="${path}/biz/loanmanage">
                <div class="row" style="margin-left: 5px">
                    <div class="form-group">
                        <label>合同编号:</label>
                        <input type="text" id="contractNO" name="contractNO" value="${loanManageForm.contractNO}"/>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <label>标的名称:</label>
                        <input type="text" id="title" name="title" value="${loanManageForm.title}"/>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <label>借款账户</label>
                        <input type="text" id="account" name="account" value="${loanManageForm.account}">
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <label>处理时间：</label>
                        <input id="createTimeStart" name="createTimeStart" type="text" readonly="readonly" maxlength="20"
                               class="form-control input-sm startDate" style="width:110px;"
                               onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" value="${loanManageForm.createTimeStart}"/>
                        --
                        <input id="createTimeEnd" name="createTimeEnd" type="text" readonly="readonly" maxlength="20"
                               class="form-control input-sm endDate" style="width:110px;"
                               onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" value="${loanManageForm.createTimeEnd}"/>
                        <br/>
                        <br/>
                        <label>标的类型</label>
                        <select id="bidType" name="bidType" class="form-control input-sm">
                            <option value="">全部</option>
                            <c:forEach items="${bidTypes}" var="bidType">
                                <c:choose>
                                    <c:when test="${bidType.id eq loanManageForm.bidType}">
                                        <option value="${bidType.id}" selected="selected">${bidType.typeName}</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${bidType.id}">${bidType.typeName}</option>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </select>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <label>账户类型</label>
                        <select id="userType" name="userType" class="form-control input-sm">
                            <option value="">全部</option>
                            <option value="PERSONAL"
                                    <c:if test='${loanManageForm.userType == "PERSONAL"}'>selected="selected"</c:if>>个人</option>
                            <option value="ORGANIZATION"
                                    <c:if test='${loanManageForm.userType == "ORGANIZATION"}'>selected="selected"</c:if>>企业</option>
                        </select>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <label>状态</label>
                        <select id="status" name="status" class="form-control input-sm" value="${loanManageForm.status}">
                            <option value="">全部</option>
                            <option value="SQZ" <c:if test='${loanManageForm.status == "SQZ"}'>selected="selected"</c:if>>申请中</option>
                            <option value="DSH" <c:if test='${loanManageForm.status == "DSH"}'>selected="selected"</c:if>>待审核</option>
                            <option value="DFB" <c:if test='${loanManageForm.status == "DFB"}'>selected="selected"</c:if>>待发布</option>
                            <option value="YFB" <c:if test='${loanManageForm.status == "YFB"}'>selected="selected"</c:if>>预发布</option>
                            <option value="TBZ" <c:if test='${loanManageForm.status == "TBZ"}'>selected="selected"</c:if>>投标中</option>
                            <option value="DFK" <c:if test='${loanManageForm.status == "DFK"}'>selected="selected"</c:if>>待放款</option>
                            <option value="HKZ" <c:if test='${loanManageForm.status == "HKZ"}'>selected="selected"</c:if>>还款中</option>
                            <option value="YJQ" <c:if test='${loanManageForm.status == "YJQ"}'>selected="selected"</c:if>>已结清</option>
                            <option value="YLB" <c:if test='${loanManageForm.status == "YLB"}'>selected="selected"</c:if>>已流标</option>
                            <option value="YZF" <c:if test='${loanManageForm.status == "YZF"}'>selected="selected"</c:if>>已作废</option>
                        </select>
                        <input type="hidden" id="page" name="page">
                        <input type="hidden" id="fromPage" name="fromPage" value="true">
                        &nbsp;
                        &nbsp;
                        <shiro:hasPermission name="loanmanage:search">
                            <button type="button" class="btn btn-primary btn-sm" id="searchBtn">查询</button>
                        </shiro:hasPermission>
                    </div>

                </div>
                <div class="row" style="margin-left: 5px;margin-top:10px;">
                </div>
            </form>
            <hr style="margin-bottom: 0;">
            <div class="panel panel-default" style="width:2000px!important;">
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                        <tr class="success">
							<th>序号</th>
                            <th>合同编号</th>
                            <th>标的名称</th>
                            <th>借款人</th>
                            <th>借款账户</th>
                            <th>借款金额（元）</th>
                            <th>投标金额（元）</th>
                            <th>年利率</th>
                            <th>期限</th>
                            <th>处理时间</th>
                            <th>显示标及可投标时间</th>
                            <th>还款方式</th>
                            <th>状态</th>
                            <th>操作</th>
						</tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${list}" var="v" varStatus="vs">
                        <tr>
                            <td>${vs.index + 1}</td>
                            <td>${v.contractNO}</td>
                            <td>${v.title}</td>
                            <td>${v.name}</td>
                            <td>${v.account}</td>
                            <td>${v.bidAmount}</td>
                            <td>${v.tenderAmount}</td>
                            <td>${v.rate}%</td>
                            <td>
                                <c:choose>
                                    <c:when test="${v.day > 0}">
                                        ${v.day}天
                                    </c:when>
                                    <c:otherwise>
                                        ${v.month}月
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td><fmt:formatDate value="${v.applyTime}"
                                                pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
                            </td>
                            <td>
                                <c:if test="${v.displayTime != null}">
                                    <div>
                                        显示时间&nbsp;&nbsp;<fmt:formatDate value="${v.displayTime}"
                                                     pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
                                    </div>
                                </c:if>
                                <c:if test="${v.bidTime != null}">
                                    <div>
                                        可投时间&nbsp;&nbsp;<fmt:formatDate value="${v.bidTime}"
                                                        pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
                                    </div>
                                </c:if>
                             </td>
                                <td>
                                    <c:if test="${v.repayment == 'DEBX'}">
                                        等额本息
                                    </c:if>
                                    <c:if test="${v.repayment == 'MYFX'}">
                                        每月付息,到期还本
                                    </c:if>
                                    <c:if test="${v.repayment == 'YCFQ'}">
                                        本息到期一次付清
                                    </c:if>
                                    <c:if test="${v.repayment == 'DEBJ'}">
                                        等额本金
                                    </c:if>
                                </td>
                                <td>
                                    <c:if test="${v.status == 'SQZ'}">
                                        申请中
                                    </c:if>
                                    <c:if test="${v.status == 'DSH'}">
                                        待审核
                                    </c:if>
                                    <c:if test="${v.status == 'DFB'}">
                                        待发布
                                    </c:if>
                                    <c:if test="${v.status == 'YFB'}">
                                        预发布
                                    </c:if>
                                    <c:if test="${v.status == 'TBZ'}">
                                        投标中
                                    </c:if>
                                    <c:if test="${v.status == 'DFK'}">
                                        待放款
                                    </c:if>
                                    <c:if test="${v.status == 'HKZ'}">
                                        还款中
                                    </c:if>
                                    <c:if test="${v.status == 'YJQ'}">
                                        已结清
                                    </c:if>
                                    <c:if test="${v.status == 'YLB'}">
                                        已流标
                                    </c:if>
                                    <c:if test="${v.status == 'YZF'}">
                                        已作废
                                    </c:if>
                                    <c:if test="${v.status == 'YDC'}">
                                        已代偿
                                    </c:if>
                                </td>
                                <td>
                                    <shiro:hasPermission name="loanmanage:release">
                                    <c:choose>
                                        <c:when test="${v.status == 'DFB' && v.isRelease == 1}">
                                            <a href="#" onclick="fbBid(${v.bidId}, '${v.title}', '${v.account}', '${v.bidAmount}', '${v.rate}');">发布</a>&nbsp;&nbsp;
                                        </c:when>
                                        <c:otherwise>
                                            <font color="#AAAAAA">发布</font>&nbsp;&nbsp;
                                        </c:otherwise>
                                    </c:choose>
                                    </shiro:hasPermission>
                                    <shiro:hasPermission name="loanmanage:sealedBidding">
                                    <c:choose>
                                        <c:when test="${v.status == 'TBZ'}">
                                            <a href="#" onclick="sealedBidding(${v.bidId}, '${v.title}', '${v.account}', '${v.bidAmount}', '${v.rate}');">封标</a>&nbsp;&nbsp;
                                        </c:when>
                                        <c:otherwise>
                                            <font color="#AAAAAA">封标</font>&nbsp;&nbsp;
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
    $(function(){

    });

    function fbBid(loanId, bName, account, money, rate){
        bootbox.confirm({
            title: '发布确认',
            size: 'small',
            message:
            "<span style='font-weight: bold'>标的名称:&nbsp;</span><span style='font-weight: bold'>"+bName+"</span>" +
            "<br/><span style='font-weight: bold'>借款账户:&nbsp;</span><span style='font-weight: bold'>"+account+"</span>" +
            "<br/><span style='font-weight: bold'>借款金额:&nbsp;</span><span style='font-weight: bold'>"+money+"</span>" +
            "<br/><span style='font-weight: bold'>年利率:&nbsp;</span><span style='font-weight: bold'>"+rate+"</span>",
            callback: function (result) {
                if(result) {
                    $.post(
                            path + "/biz/loanmanage/release/",
                            {"loanId" : loanId},
                            function (data) {
                                var code = data.code;
                                var message = data.message;
                                if (code == '200' || code == '2000') {
                                    bootbox.alert({size: 'small',message:"发布成功！",callback: function () {
                                        window.location.reload();
                                    }});
                                } else {
                                    bootbox.alert({size: 'small',message:message});
                                }
                            }
                    );
                }
            }
        });
    }

    function sealedBidding(loanId, bName, account, money, rate){
        bootbox.confirm({
            title: '封标确认',
            size: 'small',
            message:
            "<span style='font-weight: bold'>标的名称:&nbsp;</span><span style='font-weight: bold'>"+bName+"</span>" +
            "<br/><span style='font-weight: bold'>借款账户:&nbsp;</span><span style='font-weight: bold'>"+account+"</span>" +
            "<br/><span style='font-weight: bold'>借款金额:&nbsp;</span><span style='font-weight: bold'>"+money+"</span>" +
            "<br/><span style='font-weight: bold'>年利率:&nbsp;</span><span style='font-weight: bold'>"+rate+"</span>",
            callback: function (result) {
                if(result) {
                    $.post(
                            path + "/biz/loanmanage/sealedBidding/",
                            {"loanId" : loanId},
                            function (data) {
                                var code = data.code;
                                var message = data.message;
                                if (code == '200') {
                                    bootbox.alert({size: 'small',message:"封标成功！",callback: function () {
                                        window.location.reload();
                                    }});
                                } else {
                                    bootbox.alert({size: 'small',message:message});
                                }
                            }
                    );
                }
            }
        });
    }

    $('#searchBtn').bind('click', function() {
        var options=$("#userType option:selected");
        console.log(options.val());
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

</script>
