<%@page language="java" pageEncoding="utf-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div class="col-md-12">
    <form:form id="parent_form" commandName="logInfoForm" method="post" class="form-inline" action="${path}/cs/logInfo/getDetailInfo">
        <div class="form-group">
            <div class="form-group">
                <label>日期：</label>
                <form:input type="text" cssClass="form-control input-sm" id="startTime" path="startTime" readonly="readonly" maxlength="20" style="width:120px;" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'});"/>
                &nbsp;&nbsp;至&nbsp;&nbsp;
                <form:input type="text" cssClass="form-control input-sm" id="endTime" path="endTime" readonly="readonly" maxlength="20" style="width:120px;" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'});"/>
            </div>&nbsp;&nbsp;&nbsp;&nbsp;
            <label class="control-label">用户手机号:</label>
            &nbsp;&nbsp;
            <form:input path="phoneNum" id="phoneNum" value="" class="form-control input-sm" maxlength="30"/>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <label class="control-label">用户姓名:</label>
            &nbsp;&nbsp;
            <form:input path="name" id="name" value="" class="form-control input-sm" maxlength="30"/>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <label class="control-label">用户身份证号:</label>
            &nbsp;&nbsp;
            <form:input path="idCard" id="idCard" value="" class="form-control input-sm" maxlength="30"/>
            &nbsp;&nbsp;
            <form:input path="conductType" id="conductType" value="" hidden="hidden"/>
            <input type="hidden" id="page" name="page">
        </div>
        <br/><br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <button id="searchBtn" type="button" class="btn btn-primary btn-sm">查询</button>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <button id="resetBtn" type="button" class="btn btn-primary btn-sm">重置</button>
    </form:form>
    <hr/>
</div>
<div class="col-md-12">
    <ul class="nav nav-tabs">
        <li <c:if test="${getLoginInfo == 1}">class="active"</c:if> ><a href="#" onclick="getDetailInfo(1)"><label>登录/登出</label></a></li>
        <li <c:if test="${getLoginInfo == 2}">class="active"</c:if> ><a href="#" onclick="getDetailInfo(2)"><label>资金往来</label></a></li>
        <li <c:if test="${getLoginInfo == 3}">class="active"</c:if> ><a href="#" onclick="getDetailInfo(3)"><label>其他</label></a></li>
    </ul>
    <br/>
</div>