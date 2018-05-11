<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!--97 date -->
<script type="text/javascript" src="/static/component/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/static/component/datepicker/calendar.js"></script>
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>

<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/biz/index">业务管理</a></li>
                <li class="active"><b>利息管理费配置</b></li>
            </ol>
            <form id="dataform" class="form-inline" method="post">
                <input type="hidden" id="currentPercentId" name="currentPercentId" value="${interestManagementForm.currentPercentId}">
                <div id="showDiv" class="form-group">
                        <label>利息管理费比例：</label>
                        <input type="text" readonly="readonly"
                               class="form-control input-sm" maxlength="11" id="currentPercentStr"
                               name="currentPercentStr" value="${interestManagementForm.currentPercentStr}"
                               style="width: 160px; border: none"/>
                        <shiro:hasPermission name='interestManagement:change'>
                            <button type="button" class="btn btn-primary btn-sm" id="updateBtn">修改</button>
                        </shiro:hasPermission>
                </div>
                <div id="saveDiv" class="form-group" style="display: none">
                    <label>利息管理费比例：</label>
                    <input type="number" placeholder="请输入0-100的数值" class="form-control input-sm"
                           maxlength="11" id="updatePercentStr" name="updatePercentStr"
                           style="width: 160px;"/>
                    <button type="button" class="btn btn-primary btn-sm" id="saveBtn">保存</button>
                    <button type="button" class="btn btn-primary btn-sm" id="cancelBtn">取消</button>
                </div>
            </form>

            <hr style="margin-bottom: 0;">
            <hr style="margin-bottom: 0;">

            <div class="panel panel-default">
                <div class="panel-heading">修改审核</div>
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>操作时间</th>
                        <th>操作人</th>
                        <th>修改前</th>
                        <th>修改后</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody id="auditInfo">
                    <c:if test="${auditInterestManagementInfo != null}">
                        <input id="auditRecordId" type="hidden" value="${auditInterestManagementInfo.recordId}">
                    <tr>
                        <td>
                            <fmt:formatDate value="${auditInterestManagementInfo.operateTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
                        </td>
                        <td>${auditInterestManagementInfo.operator}</td>
                        <td>${auditInterestManagementInfo.currentPercentStr}</td>
                        <td>${auditInterestManagementInfo.updatePercentStr}</td>
                        <td>
                            <shiro:hasPermission name='interestManagement:pass'>
                                <a href="#" onclick="changeInterestState(${auditInterestManagementInfo.recordId}, ${auditInterestManagementInfo.sid}, '${auditInterestManagementInfo.updatePercentStr}', 1);" role="button">&nbsp;&nbsp;&nbsp;通过&nbsp;</a>
                            </shiro:hasPermission>
                            <shiro:hasPermission name='interestManagement:refuse'>
                                <a href="#" onclick="changeInterestState(${auditInterestManagementInfo.recordId}, ${auditInterestManagementInfo.sid}, '${auditInterestManagementInfo.updatePercentStr}', 2);" role="button">&nbsp;&nbsp;&nbsp;拒绝&nbsp;</a>
                            </shiro:hasPermission>
                            <shiro:hasPermission name='interestManagement:cancel'>
                                <a href="#" onclick="changeInterestState(${auditInterestManagementInfo.recordId}, ${auditInterestManagementInfo.sid}, '${auditInterestManagementInfo.updatePercentStr}', 3);" role="button">&nbsp;&nbsp;&nbsp;作废&nbsp;</a>
                            </shiro:hasPermission>
                        </td>
                    </tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
            <hr style="margin-bottom: 0;">
            <form id="searchform" class="form-inline" method="post" action="${path}/biz/interestManagement">
                <input type="hidden" id="page" name="page">
            </form>
            <hr style="margin-bottom: 0;">
            <div class="panel panel-default">
                <div class="panel-heading">修改记录</div>
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>操作时间</th>
                        <th>操作人</th>
                        <th>修改前</th>
                        <th>修改后</th>
                        <th>状态</th>
                        <th>审核时间</th>
                        <th>审核人</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${list}" var="v" varStatus="vs">
                        <tr>
                            <td>
                                <fmt:formatDate value="${v.operateTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
                            </td>
                            <td>${v.operator}</td>
                            <td>${v.currentPercentStr}</td>
                            <td>${v.updatePercentStr}</td>
                            <td><c:choose>
                                <c:when test="${v.state == 1}">
                                    通过
                                </c:when>
                                <c:when test="${v.state == 2}">
                                    拒绝
                                </c:when>
                                <c:when test="${v.state == 3}">
                                    作废
                                </c:when>
                                <c:otherwise>
                                    -
                                </c:otherwise>
                                </c:choose>
                            </td>
                            <td><fmt:formatDate value="${v.auditTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                            <td>${v.auditor}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <tiles:insertDefinition name="paginator"/>
        </div>
    </div>
</div>
<script type="text/javascript">

    // 修改按钮
    $("#updateBtn").click(function () {
        var auditRecordIdval = $('#auditRecordId').val();
        if(typeof(auditRecordIdval) == "undefined"){
            $('#showDiv').removeClass("show");
            $('#showDiv').addClass("hide");
            $('#saveDiv').removeClass("bide");
            $('#saveDiv').addClass("show");
        }else {
            bootbox.alert({size: 'small',message:"已存在待审核的记录，请先进行处理!!"});
            return false;
        }

    });

    // 取消按钮
    $("#cancelBtn").click(function () {
        $('#showDiv').removeClass("hide");
        $('#showDiv').addClass("show");
        $('#saveDiv').removeClass("show");
        $('#saveDiv').addClass("hide");
    });

    // 修改利息管理费比例保存按钮
    $("#saveBtn").click(function () {
        // 校验利息管理费比例设置
         var updatePercent = $("#updatePercentStr").val();
         var updatePercentReg = /^[0-9]+([.]{1}[0-9]{1,2})?$/;
         if(updatePercent == ''){
             bootbox.alert({size: 'small',message:"利息管理费比例不能为空！！！"});
             return false;
         }else if(updatePercent < 0 || updatePercent > 100){
             bootbox.alert({size: 'small',message:"请输入0-100的数值!!"});
             return false;
         }else if(!updatePercentReg.test(updatePercent)){
             bootbox.alert({size: 'small',message:"请输入正确的利息管理费比例！！！"});
             return false;
         }
         $("#auditInfo").html('');
         $.post(
             path + "/biz/interestManagement/updateInterest",
             $("#dataform").serialize(),
             function (data) {
                 var sysCode = data.sysCode;
                 if(sysCode == "0001"){
                     bootbox.alert({size: 'small',message:"利息管理费比例设置错误！！！"});
                     return false;
                 }else if(sysCode == "0011"){
                     bootbox.alert({size: 'small',message:"操作异常,请稍后重试！！！"});
                     return false;
                 }else if(sysCode == "0000"){
                     location.reload(true);
                 }
             },
             "json"
         );
     });

    function confirmMsg(percent, state) {
        var msg = null;
        if (state == 1) {
            msg = "是否确定将利息管理费比例调整为" + percent + "？" + "<br/>" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;审核通过后即生效";
        } else if(state == 2){
            msg = "是否确定拒绝该条审核记录？";
        } else if(state == 3){
            msg = "是否确定作废该条审核记录？";
        }
        return msg;
    }

    function changeInterestState(recordId, sid, percent, state) {
        bootbox.confirm({
            size: 'small',
            message: confirmMsg(percent, state),
            callback: function (result) {
                if (result) {
                    $.post(
                            path + "/biz/interestManagement/changeInterestState",
                            {"recordId" : recordId,
                             "id" : sid,
                             "state" : state
                            },
                            function (data) {
                                var code = data.code;
                                if(code == "200"){
                                    bootbox.alert({message: "审核成功", title: '操作信息',callback: function() {
                                        window.location.reload(true);}});
                                }else{
                                    bootbox.alert({message: "审核失败，请稍后再试", title: '错误信息'});
                                }
                            }
                    );
                }
            }
        })
    }

    function pagination(page) {
        $('#page').val(page);
        var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $('#searchform').submit();
        layer.close();
    }
</script>
