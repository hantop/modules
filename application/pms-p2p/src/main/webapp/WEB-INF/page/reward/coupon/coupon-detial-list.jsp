<%@page language="java" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/reward/index">奖励管理</a></li>
                <li class="active"><a href="${path}/reward/red-packet/back-voucher-list">加息券管理</a></li>
                <li class="active"><b>发送详情</b></li>
            </ol>
            <c:if test="${fn:length(statisticsList) > 0}">
                <div>
                    <table class="table">
                        <c:set value="0" var="totalCount" />
                        <c:set value="0" var="totalSum" />
                        <c:forEach items="${statisticsList}" var="item">
                            <c:set value="${totalCount + item.grantCount}" var="totalCount" />
                            <c:set value="${totalSum + item.grantSum}" var="totalSum" />
                        </c:forEach>
                        <tr>
                            <td class="text-danger">总计</td>
                            <td class="text-danger">发送数：${totalCount}</td>
                            <td class="text-danger">发送金额（元）：${totalSum}</td>
                        </tr>
                        <c:forEach var="item" items="${statisticsList}">
                        <tr>
                            <td>加息劵：${item.activityCode}</td>
                            <td>发送数：${item.grantCount}</td>
                            <td>发送金额（元）：${item.grantSum}</td>
                        </tr>
                        </c:forEach>
                    </table>
                    <hr>
                </div>
            </c:if>
            <form:form class="form-inline" method="post" action="${path}/reward/red-packet/back-voucher-detial-list"
                       id="dataform" commandName="userRedpackets">
                <input type="hidden" id="page" name="page">
                <input type="hidden" name="grantId" value="${userRedpackets.grantId}">
                <input type="hidden" name="granted" value="${granted}">
                
                <shiro:hasPermission name="cashBackVoucherDetail:search">
                <div class="form-group">
                    <label>加息券类型：</label>
                    <form:select path="activityCode" cssClass="form-control">
                        <option value="">全部</option>
                        <c:forEach var="item" items="${activityCodes}">
                            <form:option value="${item.activityCode}">${item.activityCode}</form:option>
                        </c:forEach>
                    </form:select>
                </div>
                <div class="form-group">
                    <label>发送状态：</label>
                    <form:select path="grantStatus" cssClass="form-control">
                        <form:option value="-1">全部</form:option>
                        <form:option value="0">未发送</form:option>
                        <form:option value="1">已发放</form:option>
                        <form:option value="3">已作废</form:option>
                    </form:select>
                </div>
                <button type="submit" class="btn btn-primary">搜索</button>
                </shiro:hasPermission>
                
                <shiro:hasPermission name="cashBackVoucherDetail:export">
                <button class="btn btn-info export" type="button"  role="button">导出</button>
                </shiro:hasPermission>
                <a class="btn btn-warning" href="${path}/reward/red-packet/back-voucher-list"  role="button">返回</a>
            </form:form>
        <hr>
        
        </div>
        <div class="col-md-6" >
            <table class="table table-striped table-bordered table-condensed">
                <thead>
                <tr class="success">
                    <th>用户手机号</th>
                    <th>加息券类型</th>
                    <th>发送状态</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${list== null || fn:length(list) == 0}">
                        <%-- <tr>
                             <td colspan="4">无数据</td>
                         </tr>--%>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="item" items="${list}">
                            <tr>
                                <td>${item.phone}</td>
                                <td>${item.activityCode}</td>
                                <td>${item.grantStatus == 0 ? '未发放' : item.grantStatus == 1 ? '已发放' : item.grantStatus == 3 ? '已作废' : '错误状态'}</td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
            <tiles:insertDefinition name="paginator"/>
        </div>
    </div>
</div>

<script type="application/javascript">
    (function ($) {
        $(".export").on('click', function () {
            var cloneForm = $(this).parents('form.form-inline').clone(true);
            $(cloneForm).attr('action', '${path}/reward/red-packet/back-voucher-detial-export').submit();
            return false;
        });
    })(jQuery);
</script>