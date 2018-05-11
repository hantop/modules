<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<link rel="stylesheet" href="/static/component/HoldOn/HoldOn.min.css">
<jsp:include page="/WEB-INF/page/inc/jquery-validation.jsp"/>
<script type="text/javascript" src="/static/component/HoldOn/HoldOn.min.js"></script>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/cs/index">客户管理</a></li>
                <li class="active"><a href="${path}/cs/guarantor">账户管理</a></li>
                <li class="active"><b>创建担保户</b></li>
            </ol>
        </div>
        <div class="col-md-12" style="margin-top: 20px;">
            <span class="badge" style="font-size: 18px;">${title}</span>
            <form:form class="form-horizontal" id="organization_form"  role="form" action="${path}/cs/guarantor/view"
                       method="get"
                       commandName="userLessInfo" >
                <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                    <label class="col-sm-2 control-label text-right">
                        用户类型：
                    </label>
                    <div class="col-sm-6">
                        <form:select path="userType" name="userType" id="guarantor_usertype_select_id" class="form-control">
                            <form:option value="">请选择</form:option>
                            <form:option value="ORGANIZATION">企业</form:option>
                            <form:option value="PERSONAL">个人</form:option>
                        </form:select>
                    </div>
                    <div class="col-sm-3 help">
                         <span class="help-block">请选择用户类型</span>
                    </div>
                </div>
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            用户账号：
                        </label>
                        <div class="col-sm-6">
                            <form:input path="account" name="account" id="guarantor_account_input_id" cssClass="form-control input-sm" maxlength="40"/> <%-- input-sm--%>
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <%--<div class="col-sm-3 help">--%>
                            <%--<!--如果没有错误时使用默认的提示文字，如果有错误，显示错误信息-->--%>
                            <%--<span class="help-block">请输入2~40位的用户账号</span>--%>
                        <%--</div>--%>
                        <span style="color: red" id = "account_help_block_span_id"></span>
                    </div>
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            <%--用户名称：--%>
                        </label>
                        <div class="col-sm-6">
                            <label id="guarantor_name_label_id">
                            </label>
                            <%--<input name="name" id="name" cssClass="form-control" maxlength="30" style="width: 240px;margin-top: 7px;" />--%>
                        </div>
                        <div class="col-sm-3 help">
                        </div>
                    </div>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <a type="button" class="btn btn-default btn-sm" href="${path}/cs/guarantor">取消</a>
                        <%--<a type="button" id="guarantor_nextstep_a_id" class="btn btn-primary btn-sm" href="#">下一步</a>--%>
                        <input id="guarantor_nextstep_btn_id" class="btn btn-primary" value="下一步" readonly/>
                        <%--<a type="button" class="btn btn-default btn-sm" href="${path}/cs/guarantor">取消</a>--%>
                        <%--<button type="submit" class="btn btn-primary btn-sm">下一步</button>--%>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>
<input id="guarantor_user_id" name="userId" type="hidden" value=""/>
<form id="personal_form" action="" method="post"></form>
<script type="application/javascript">
    (function ($) {
        $('form[role="form"]').validate({
            rules: {
                userType: {
                    required: true,
                },
                account: {
                    required: true,
                    rangelength: [2, 40],
                    pattern: '[0-9a-zA-Z]{2,40}'
                }
            },
            messages: {
                userType: {
                    pattern: '请选择用户类型'
                },
                account: {
                    required: "请输入用户账号",
                    rangelength: $.validator.format("账号字符长度在 {0} 到 {1} 之间."),
                    pattern: '账号码只能是字母或数字的组合'
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
//                var formGroup = $(element).parents('div.form-group');
//                formGroup.find('.help-block').empty().html('正确');
            },
            submitHandler: function (form) {
                HoldOn.open({
                    theme: "sk-cube-grid",
                    message: '请稍等.....',
                    backgroundColor: "#000"
                });
                form.submit();
            },
            ignore: '.ignore',
            errorClass: "help-block",
            errorElement: 'span',
            focusInvalid: false,
            focusCleanup: true,
            onkeyup: false
        });

        $("#guarantor_nextstep_btn_id").attr("disabled", "true"); //.removeAttr("disabled");//启用按钮

        $("#guarantor_usertype_select_id").bind('change', function () {
            $("#guarantor_account_input_id").val("");
            $("#guarantor_user_id").val("");
            $("#guarantor_nextstep_btn_id").attr("disabled", "true");
            $("#account_help_block_span_id").text("");
        })
        /**
         * 用户账号绑定失去焦点事件，校验该账号是否已注册分利宝
         */
        $("#guarantor_account_input_id").bind('blur', function () {
            $("#guarantor_nextstep_btn_id").attr("disabled", "true");
            $("#account_help_block_span_id").text("");
            var isPass = $("#organization_form").valid();
            if (!isPass) {
                return;
            }
            var userType = $("#guarantor_usertype_select_id").val();
            var userTypeText = $("#guarantor_usertype_select_id").find("option:selected").text();
            var account = $("#guarantor_account_input_id").val();
            $.ajax({
                type: "GET",
                url: "${path}/cs/guarantor/validAccount",
                data: {accountType: userType,account:account},
                dataType: "json",
                success: function(data){
                    if (data.isRegister && !data.isRegXw) {
                        $("#guarantor_user_id").val(data.userId);
                        $("#guarantor_nextstep_btn_id").removeAttr("disabled");//启用按钮
                    } else {
                        if (data.isRegXw) {
                            $("#account_help_block_span_id").text(data.xwStatusMsg);
                        } else {
                            $("#account_help_block_span_id").text('该账号尚未注册分利宝，请先注册分利宝');
                        }
                    }
                },
                error: function (e) {
                    console.log(e);
                }
            });
        });
        /**
         * 点击下一步路由跳转到个人新网注册或企业信息录入 （个人或企业）
         */
        $("#guarantor_nextstep_btn_id").bind('click', function () {
            var userType = $("#guarantor_usertype_select_id").val();
            if (userType == "PERSONAL") {
                doSubmitPersonal();
            } else if (userType == "ORGANIZATION") {
                doInputEnterpriseInfo();
            }
        });
        /**
         * 个人用户
         */
        function doSubmitPersonal() {
            var userId = $("#guarantor_user_id").val();
            $.ajax({
                type: "get",
                async: false,
                url: "${path}/cs/guarantor/submitPersonal",
                data: {userId: userId},
                dataType: "json",
                success: function(data){
                    if (data.code != '200') {
                        alert(data.message);
                        return;
                    }
                    $("#personal_form").attr('action', data.data.postUrl);
                    $.each(data.data.postParams, function (index, obj) {
                        $('#personal_form').append('<input type="hidden" name=\'' + obj.key + '\' value=\'' + obj.value + '\'/>');
                    });
                    $("#personal_form").submit(); // 提交到新网
                },
                error: function (e) {
                    console.log(e);
                }
            });
        }
        /**
         * 企业用户
         */
        function doInputEnterpriseInfo() {
            var userId = $("#guarantor_user_id").val();
            var form = $('#organization_form');
            form.append('<input type="hidden" name="userId" value=\'' + userId + '\'/>');
            form.append('<input type="hidden" name="accountType" value="GUARANTEECORP" />');
            form.append('<input type="hidden" name="auditStatus" value="WAIT" />');
            $("#organization_form").submit();
        }
    })(jQuery)
</script>



