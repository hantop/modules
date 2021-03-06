<%--
  Created by IntelliJ IDEA.
  User: bogle
  Date: 2015/11/10
  Time: 15:28
  To change this template use File | Settings | File Templates.
--%>
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
            <span class="badge" style="font-size: 18px;">${title}</span>
            <form:form class="form-horizontal" role="form"
                       method="post"
                       commandName="msg">
                <form:hidden path="id"/>
                <spring:bind path="keyword">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            关键字：
                        </label>

                        <div class="col-sm-6">
                            <form:input path="keyword" cssClass="form-control  input-sm" maxlength="30"/>
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <!--如果没有错误时使用默认的提示文字，如果有错误，显示错误信息-->
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="keyword" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">多个关键字以";"分隔开</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                <spring:bind path="activeProfiles">
                    <div class="form-group has-feedback no-edit">
                        <label class="col-sm-2 control-label text-right">
                            发布环境：
                        </label>

                        <div class="col-sm-6">
                            <label class="radio-inline radio-tab active" data-target="send-text">
                                <form:radiobutton value="test" path="activeProfiles"/>测试
                            </label>

                            <label class="radio-inline radio-tab"
                                   data-target="redirect-url">
                                <form:radiobutton value="prod" path="activeProfiles"/>正式
                            </label>
                        </div>
                        <div class="col-sm-3 help">
                            <!--如果没有错误时使用默认的提示文字，如果有错误，显示错误信息-->
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="activeProfiles" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">请选择发布环境</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                <spring:bind path="content">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            回复内容：
                        </label>
                        <div class="col-sm-6">
                            <form:textarea path="content" cssClass="form-control  input-sm" rows="10"/>
                        </div>
                    </div>
                </spring:bind>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <button type="submit" class="btn btn-primary">提交</button>
                        <a type="button" class="btn btn-default" href="${path}/weixin/msg/index">取消</a>
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
                        <a href="${path}/weixin/msg/edit" class="btn btn-default">继续添加</a>
                        <a href="${path}/weixin/msg/index" class="btn btn-primary">返回列表</a>
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
                            <c:when test="${code == -1}">
                                渠道分类名称不能重复
                            </c:when>
                            <c:when test="${code == -3}">
                                渠道分类名称已经存在
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
                keyword: {
                    required: true,
                    rangelength: [2, 50]
                },
                activeProfiles: {
                    required: true
                }
            },
            messages: {
                name: {
                    required: "回复关键字不能为空",
                    rangelength: $.validator.format("回复关键字字符长度在 {0} 到 {1} 之间.")
                },
                activeProfiles: {
                    required: "请选择发布环境"
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
                HoldOn.open({
                    theme: "sk-cube-grid",
                    message: '请稍等.....',
                    backgroundColor: "#000"
                });
                form.submit();
            },
            ignore: "",
            errorClass: "help-block",
            errorElement: 'span',
            focusInvalid: false,
            focusCleanup: true,
            onkeyup: false
        });
    })(jQuery)
</script>
