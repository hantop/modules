<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<jsp:include page="/WEB-INF/page/inc/jquery-validation.jsp"/>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <span class="badge" style="font-size: 18px;">${title}</span>
            <form:form class="form-horizontal" role="form" action="${path}/reward/experienceGold/experience-gold-edit"
                       method="post"
                       commandName="experienceGold">
                <form:hidden path="id"/>
                <spring:bind path="activityCode">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            <b class="text-danger">*</b>体验金代码:
                        </label>

                        <div class="col-sm-6">
                            <form:input path="activityCode" cssClass="form-control" maxlength="50"/>
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <!--如果没有错误时使用默认的提示文字，如果有错误，显示错误信息-->
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="activityCode" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">请输入2~50位的体验金代码</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                <spring:bind path="effectDay">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            <b class="text-danger">*</b>体验金有效期(天)：
                        </label>

                        <div class="col-sm-6">
                            <form:input path="effectDay" cssClass="form-control" maxlength="30" type="number"/>
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="effectDay" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">请输入正确体验金有效期</span>
                                </c:otherwise>
                            </c:choose>

                        </div>
                    </div>
                </spring:bind>
                <spring:bind path="experienceGold">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            <b class="text-danger">*</b>体验金金额：
                        </label>

                        <div class="col-sm-6">
                            <form:input path="experienceGold" cssClass="form-control" maxlength="10" type="number"/>
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="experienceGold" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">输入正确体验金金额</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                <spring:bind path="yearYield">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            <b class="text-danger">*</b>年化收益(%)：
                        </label>

                        <div class="col-sm-6">
                            <form:input path="yearYield" cssClass="form-control" maxlength="20" type="number"/>
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <!--如果没有错误时使用默认的提示文字，如果有错误，显示错误信息-->
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="yearYield" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">输入合法的年化收益</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                <spring:bind path="expGoldComment">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            来源:
                        </label>

                        <div class="col-sm-6">
                            <form:input path="expGoldComment" cssClass="form-control" maxlength="50"/>
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <!--如果没有错误时使用默认的提示文字，如果有错误，显示错误信息-->
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="expGoldComment" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">请输入25个中文字内</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <button type="submit" class="btn btn-primary btn-sm">提交</button>
                        <a type="button" class="btn btn-default  btn-sm"
                           href="${path}/reward/experienceGold/experience-gold-list">取消</a>
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
                        <a href="${path}/reward/experienceGold/experience-gold-edit" class="btn btn-default">继续添加</a>
                        <a href="${path}/reward/experienceGold/experience-gold-list" class="btn btn-primary">返回列表</a>
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
                                体验金代码不能重复
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
                activityCode: {
                    required: true,
                    rangelength: [2, 50]
                },
                effectDay: {
                    required: true,
                    digits: true,
                    min: 0,
                    max: 15
                },
                experienceGold: {
                    required: true,
                    number: true,
                    min: 0,
                    max: 500000,
                },
                yearYield: {
                    required: true,
                    number: true,
                    min: 0,
                    max: 6
                }
            },
            messages: {
                activityCode: {
                    required: "体验金代码不能为空",
                    rangelength: $.validator.format("体验金代码字符长度在 {0} 到 {1} 之间.")
                },
                effectDay: {
                    required: "体验金有效期不能为空",
                    digits: '请输入正确的数字',
                    min: '体验金有效期天数不能小于1天',
                    max: '体验金有效期不能超过15天'
                },
                experienceGold: {
                    required: '体验金金额不能为空',
                    number: '请输入正确的数字',
                    min: '体验金额不能为负数',
                    max: '体验金额不能超过50万元'
                },
                yearYield: {
                    required: "年化收益不能为空",
                    number: '请输入正确的数字',
                    min: '年化收益不能为负数',
                    max: '年化收益不能超过6%'
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
                layer.closeAll();
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





















