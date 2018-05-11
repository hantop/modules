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
            <form:form class="form-horizontal" role="form" action="${path}/channel/child/edit-child"
                       method="post"
                       commandName="channel">
                <form:hidden path="id"/>
                <spring:bind path="name">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            渠道名称：
                        </label>

                        <div class="col-sm-6">
                            <form:input path="name" cssClass="form-control input-sm" maxlength="30"/>
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <!--如果没有错误时使用默认的提示文字，如果有错误，显示错误信息-->
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="name" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">请输入2~30位的渠道名称</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                <spring:bind path="parentId">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            渠道分类：
                        </label>

                        <div class="col-sm-6">
                            <form:select path="parentId" cssClass="form-control">
                                <option value="0">请选择渠道分类</option>
                                <c:forEach items="${parent}" var="item">
                                    <form:option value="${item.id}">${item.name}</form:option>
                                </c:forEach>
                            </form:select>
                        </div>
                        <div class="col-sm-3 help">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="parentId" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">请选择渠道分类</span>
                                </c:otherwise>
                            </c:choose>

                        </div>
                    </div>
                </spring:bind>
                <spring:bind path="code">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            渠道编码：
                        </label>

                        <div class="col-sm-6">
                            <form:input path="code" cssClass="form-control  input-sm ${channel.id > 0 ? 'ignore' : ''}"
                                        name="code" maxlength="10" readonly="${channel.id > 0 ? true : false}"/>
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="code" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">输入2~10位的渠道编码</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                <spring:bind path="description">
                    <div class="form-group ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            备注：
                        </label>

                        <div class="col-sm-6">
                            <form:textarea path="description" cssClass="form-control" maxlength="200"
                                           rows="6"></form:textarea>
                        </div>
                        <div class="col-sm-3 help">
                            <c:if test="${status.error}">
                                <form:errors path="description" element="span" cssClass="help-block"/>
                            </c:if>
                        </div>
                    </div>
                </spring:bind>

                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <button type="submit" class="btn btn-primary btn-sm">提交</button>
                        <a type="button" class="btn btn-default btn-sm" href="${path}/channel/child/list-child">取消</a>
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
                        <a href="${path}/channel/child/edit-child" class="btn btn-default btn-sm">继续添加</a>
                        <a href="${path}/channel/child/list-child" class="btn btn-primary btn-sm">返回列表</a>
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
                                渠道名称不能重复
                            </c:when>
                            <c:when test="${code == -2}">
                                渠道编码不能重复
                            </c:when>
                            <c:when test="${code == -3}">
                                渠道名称已经存在
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
                name: {
                    required: true,
                    rangelength: [2, 30]
                },
                parentId: {
                    min: 1
                },
                code: {
                    required: true,
                    rangelength: [2, 10],
                    pattern: '[0-9a-zA-Z]{2,10}'
                }
            },
            messages: {
                name: {
                    required: "渠道名称不能为空",
                    rangelength: $.validator.format("渠道名称字符长度在 {0} 到 {1} 之间.")
                },
                parentId: {
                    min: '请选择渠道分类'
                },
                code: {
                    required: "渠道编码不能为空",
                    rangelength: $.validator.format("渠道编码字符长度在 {0} 到 {1} 之间."),
                    pattern: '编码只能是字母或数字的组合'
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
            ignore: '.ignore',
            errorClass: "help-block",
            errorElement: 'span',
            focusInvalid: false,
            focusCleanup: true,
            onkeyup: false
        });

    })(jQuery)
</script>



