<%@page language="java" pageEncoding="utf-8" %>
<%@ page import="com.alibaba.fastjson.JSON" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<link rel="stylesheet" href="/static/component/HoldOn/HoldOn.min.css">
<link rel="stylesheet" href="/static/component/zTree_v3/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.exedit-3.5.js"></script>
<jsp:include page="/WEB-INF/page/inc/jquery-validation.jsp"/>
<script type="text/javascript" src="/static/component/HoldOn/HoldOn.min.js"></script>
<style>
    ul.ztree {margin-top: 10px;border: 1px solid #617775;background: #f0f6e4;width:220px;height:200px;overflow-y:scroll;overflow-x:auto;}
</style>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/biz/index">业务管理</a></li>
                <li class="active"><a href="${path}/biz/loanapplication">借款申请信息</a></li>
                <li class="active"><b>借款申请查看</b></li>
            </ol>
            <span class="badge" style="font-size: 18px;">${title}</span>

            <form:form class="form-horizontal" role="form">
                <div class="form-group">
                    <label class="col-sm-2 text-right">
                        手机号码：
                    </label>
                    <div class="col-sm-6">
                        ${loanApplication.phonenum}
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 text-right">
                        联系人姓名：
                    </label>
                    <div class="col-sm-6">
                        ${loanApplication.contacts}
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 text-right">
                        借款金额：
                    </label>
                    <div class="col-sm-6">
                        ${loanApplication.amountRange}
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 text-right">
                        所在区域：
                    </label>
                    <div class="col-sm-6">
                        ${loanApplication.districtFullName}
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 text-right">
                        月收入：
                    </label>
                    <div class="col-sm-6">
                        ${loanApplication.annualIncome}
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 text-right">
                        是否有房：
                    </label>
                    <div class="col-sm-6">
                        ${loanApplication.hasRoom}
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 text-right">
                        是否有车：
                    </label>
                    <div class="col-sm-6">
                        ${loanApplication.hasCar}
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 text-right">
                        处理意见：
                    </label>
                    <div class="col-sm-6">
                        <c:choose>
                            <c:when test="${loanApplication.processingStatus == 0}">
                                未处理
                            </c:when>
                            <c:when test="${loanApplication.processingStatus == 1}">
                                通过
                            </c:when>
                            <c:when test="${loanApplication.processingStatus == 2}">
                                不通过
                            </c:when>
                        </c:choose>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 text-right">
                        申请时间：
                    </label>
                    <div class="col-sm-6">
                        ${loanApplication.createTime}
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 text-right">
                        编辑时间：
                    </label>
                    <div class="col-sm-6">
                        ${loanApplication.updateTime}
                    </div>
                </div>
                <c:if test="${loanApplication.processingStatus == 2}">
                <div id="reasonDiv">
                    <div class="form-group">
                        <label class="col-sm-2 text-right">
                            选择理由：
                        </label>
                        <div class="col-sm-6">
                            ${loanApplication.nopassReasonTitle}
                        </div>
                    </div>
                </div>
                </c:if>
                <div class="form-group">
                    <label class="col-sm-2 text-right">
                    </label>
                    <div class="col-sm-6">
                        ${loanApplication.processingOpinion}
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <a type="button" class="btn btn-default btn-sm" href="${path}/biz/loanapplication">返回</a>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>
<div id="menuContent" class="menuContent" style="display:none; position: absolute;">
    <ul id="tree" class="ztree" style="margin-top:0; width:160px;"></ul>
</div>

