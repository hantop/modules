<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<jsp:include page="/WEB-INF/page/inc/jquery-validation.jsp"/>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <span class="badge" style="font-size: 18px;">${title}</span>
            <form:form id="dataform" role="form" class="form-horizontal" modelAttribute="replacementRecharge" action="${path}/finance/replacementRecharge/edit">
                <form:hidden path="userId" id="userId"/>
                <form:hidden path="userRole" id="userRole"/>
                <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                        用户类型：
                    </label>
                    <div class="col-sm-2">
                        <form:select path="userType" id="userType" cssClass="form-control input-sm">
                            <form:option value="">请选择</form:option>
                            <form:options items="${userTypes}" itemLabel="label" itemValue="value"></form:options>
                        </form:select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                        用户账号：
                    </label>
                    <div class="col-sm-2">
                        <form:input path="account" id="account" cssClass="form-control input-sm" class="{required:true, minlength:2}" onblur="detectedUserName();"/>
                    </div>
                    <div class="col-sm-3 help">
                        <c:choose>
                            <c:when test="${status.error}">
                                <form:errors path="account" element="span" cssClass="help-block"/>
                            </c:when>
                            <c:otherwise>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                        用户名称：
                    </label>
                    <div class="col-sm-2 help">
                        <form:input path="userName" id="userName" cssClass="form-control input-sm" readonly="true" cssStyle="border: none"/><%--<font id="userName"></font>--%>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                        充值金额(元)：
                    </label>
                    <div class="col-sm-2">
                        <form:input path="rechargeMoney" id="rechargeMoney" cssClass="form-control input-sm" type="number"/>
                    </div>
                    <div class="col-sm-3 help">
                        <c:choose>
                            <c:when test="${status.error}">
                                <form:errors path="rechargeMoney" element="span" cssClass="help-block"/>
                            </c:when>
                            <c:otherwise>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                            <button type="submit" class="btn btn-primary btn-sm" >提交</button>
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <a type="button" class="btn btn-default btn-sm" href="${path}/finance/replacementRecharge">返回</a>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>
<c:choose>
    <c:when test="${param.success}">
        <!-- Modal -->
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
            <div class="modal-dialog modal-sm" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel">
                            提示
                        </h4>
                    </div>
                    <div class="modal-body">
                            ${param.success ? '成功' : '失败'}
                    </div>
                    <div class="modal-footer">
                        <a href="${path}/finance/replacementRecharge/edit?type=${param.userRole}" class="btn btn-default">继续添加</a>
                        <a href="${path}/finance/replacementRecharge" class="btn btn-primary">返回列表</a>
                    </div>
                </div>
            </div>
        </div>
        <script type="application/javascript">
            (function ($) {
                $("#myModal").modal({
                    keyboard: true,
                    show: true,
                    backdrop: 'static'
                });
            })(jQuery);
        </script>
    </c:when>
    <c:when test="${code != null}">
        <!-- Modal -->
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
            <div class="modal-dialog modal-sm" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title">
                            提示
                        </h4>
                    </div>
                    <div class="modal-body">
                        <c:choose>
                            <c:when test="${code <= -1}">
                                参数异常
                            </c:when>
                            <c:otherwise>
                                未知错误，请联系管理员
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">确定</button>
                    </div>
                </div>
            </div>
        </div>
        <script type="application/javascript">
            (function ($) {
                $("#myModal").modal({
                    keyboard: true,
                    show: true,
                    backdrop: 'static'
                });
            })(jQuery);
        </script>
    </c:when>
</c:choose>

<script type="application/javascript">

    (function ($) {
        $('form[role="form"]').validate({
            rules: {
                account: {
                    required: true
                },
                rechargeMoney: {
                    required: true,
                    number: true,
                    min: 0,
                    max: 5000000
                },
                userName :  {
                    required: true
                }
            },
            messages: {
                account: {
                    required: "请输入正确的账号."
                },
                rechargeMoney: {
                    required: "充值金额不能为空",
                    number: '请输入正确的充值金额',
                    min: '充值金额不能为负数',
                    max: '充值金额上限为500万/次'
                },
                userName : {
                    required: "未查找到该用户名."
                }
            },
            highlight: function (element, errorClass, validClass) {
                //空方法，去除为input添加错误样式
                //未通过的元素添加效果
                var formGroup = $(element).parents('div.form-group');
                formGroup.addClass('has-error').removeClass('has-success')
                formGroup.find('span.glyphicon.form-control-feedback').addClass('glyphicon-remove').removeClass('glyphicon-ok');
                return false;
            },
            unhighlight: function (element, errorClass, validClass) {
                //去除未通过的元素的效果
                var formGroup = $(element).parents('div.form-group');
                formGroup.addClass('has-success').removeClass('has-error')
                formGroup.find('span.glyphicon.form-control-feedback').addClass('glyphicon-ok').removeClass('glyphicon-remove');
            },
            errorPlacement: function (error, element) {
                var formGroup = $(element).parents('div.form-group');
                error.appendTo(formGroup.find('div.help').empty());
            },
            success: function (element) {
                var formGroup = $(element).parents('div.form-group');
                formGroup.find('.help-block').empty().html('正确');
            },
            submitHandler: function (form) {
                var index = layer.load(0, {
                    shade: [0.4,'#fff',false] //0.1透明度的白色背景
                });
                form.submit();
                layer.close();
            },
            ignore: '.ignore',
            errorClass: "help-block",
            errorElement: 'span',
            focusInvalid: false,
            focusCleanup: true,
            onkeyup: false
        });
    })(jQuery)

    function detectedUserName() {
        var url = window.location.search;
        var userRole = url.substring(url.lastIndexOf('=')+1, url.length);
        var userType = $("#userType option:selected").val();
        if(userType == null || userType == ""){
            bootbox.alert({size: 'small',message:"请选择账户类型。"});
        }
        if(!(userRole == "GUARANTEECORP" || userRole == "BORROWERS" || userRole == "INVESTOR")){
            bootbox.alert({size: 'small',message:"无效的请求地址。"});
        }else {
            var account = $("#account").val();
            if(account != null && account != ""){
                $.post(
                        path + "/finance/replacementRecharge/detectedUserName",
                        {
                            "userType" : userType,
                            "account" : account,
                            "userRole" : userRole
                        },
                        function (data) {
                            var userRole = data.userRole;
                            var sysCode = data.code;
                            console.log(sysCode);
                            var userName = $("#userName").text();
                            var userId = $("#userId").val();
                            if(sysCode == "0000") {
                                bootbox.alert({size: 'small',message:"该账号尚未注册分利宝，请先注册分利宝。"});
                            }else if(sysCode == "0010" || sysCode == "0100"){// 用户角色与当前入口参数不一致
                                if(userRole == "GUARANTEECORP"){
                                    bootbox.alert({size: 'small',message:"该账号尚未开通担保账户，请先开通担保账户。"});
                                }else if(userRole == "BORROWERS"){
                                    bootbox.alert({size: 'small',message:"该账号尚未开通借款账户，请先开通借款账户。"});
                                }else if(userRole == "INVESTOR"){
                                    bootbox.alert({size: 'small',message:"该账号尚未开通投资账户，请先开通投资账户。"});
                                }
                            }else{
                                var userRechargeAuthVO;
                                if(data.userRechargeAuthVO != null){
                                    userRechargeAuthVO = data.userRechargeAuthVO;
                                    $("#userId").val(userRechargeAuthVO.userId);
                                    $("#userRole").val(userRechargeAuthVO.userRole);
                                    $("#userName").val(userRechargeAuthVO.userName);
                                }
                            }
                        },
                        "json"
                );
            }else{
                bootbox.alert({size: 'small',message:"请输入正确的账号！！！"});
            }
        }
    }
</script>