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
            <form:form class="form-horizontal" role="form" action="${path}/reward/red-packet/back-voucher-type-edit"
                       method="post"
                       modelAttribute="redPacket">
                <form:hidden path="id"/>
                <spring:bind path="activityCode">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            <b class="text-danger">*</b>返现券代码:
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
                                    <span class="help-block">请输入2~50位的返现券代码</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                <spring:bind path="effectDay">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            <b class="text-danger">*</b>返现券有效期(天)：
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
                                    <span class="help-block">请输入正确的返现券有效期</span>
                                </c:otherwise>
                            </c:choose>

                        </div>
                    </div>
                </spring:bind>
                <spring:bind path="investMoney">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            <b class="text-danger">*</b>返现券门槛(元)：
                        </label>
                        <div class="col-sm-6">
                            <form:input path="investMoney" cssClass="form-control" maxlength="10" type="number"/>
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="investMoney" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">输入正确返现券门槛</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                <spring:bind path="redMoney">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            <b class="text-danger">*</b>返现券金额(元)：
                        </label>
                        <div class="col-sm-6">
                            <form:input path="redMoney" cssClass="form-control" maxlength="20" type="number"/>
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="redMoney" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">输入合法的返现券金额</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                 <div class="form-group">
                        <label class="col-sm-2 control-label text-right">
                            	来源：
                        </label>
                        <div class="col-sm-6">
                            <form:input path="remarks" value="" cssClass="form-control" maxlength="20" />
                        </div>
                 </div>
                 <!-- PS:以下选项如不选择默认不限制 -->
                 <spring:bind path="bidTypeIds">
                 <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            	<b class="text-danger">*</b>可用产品类型
                        </label>
                        <div class="col-sm-6">
                         	 	<form:checkboxes delimiter="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" path="bidTypeIds" items="${bidTypes}" itemLabel="typeName" itemValue="id"/>
                        		<span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="bidTypeIds" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">请选择对应的标的类型限制,默认全选</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                 </div>
                 </spring:bind>
                  <spring:bind path="investDeadLine">
                 <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            	<b class="text-danger">*</b>投资期限
                        </label>
                        <div class="col-sm-6">
                            	<form:radiobutton id="temp" path="investDeadLineType" value="false"/>
                            	不限<br/>
                            	<form:radiobutton path="investDeadLineType" value="true" id="radioButton1" onclick="javascript: choose();"/>
                            	按天
                            	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            	大于等于&nbsp;
                            	<form:input path="investDeadLine" cssClass="input-sm" maxlength="30" type="number" id="investDL" />
                            	&nbsp;天
                            	<span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="investDeadLine" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">输入合法的投资期限</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                 </div>
                 </spring:bind>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <button type="submit" class="btn btn-primary">提交</button>
                        <a type="button" class="btn btn-default"
                           href="${path}/reward/red-packet/back-voucher-type-list">取消</a>
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
                        <a href="${path}/reward/red-packet/back-voucher-type-edit" class="btn btn-default">继续添加</a>
                        <a href="${path}/reward/red-packet/back-voucher-type-list" class="btn btn-primary">返回列表</a>
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
                                返现券代码不能重复
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
                    max: 30
                },
                investMoney: {
                    required: true,
                    number: true,
                    min: 0
                },
                redMoney: {
                    required: true,
                    number: true,
                    min: 0,
                    max: 1000
                }
            },
            messages: {
                activityCode: {
                    required: "返现券代码不能为空",
                    rangelength: $.validator.format("返现券代码字符长度在 {0} 到 {1} 之间.")
                },
                effectDay: {
                    required: "返现券有效期不能为空",
                    digits: '请输入正确的数字',
                    min: '返现券有效期不能小于1天',
                    max: '返现券有效期不超过30天'
                },
                investMoney: {
                    required: "返现券门槛不能为空",
                    number: '请输入正确的数字',
                    min: '返现券门槛不能为负数'
                },
                redMoney: {
                    required: "返现券金额不能为空",
                    number: '请输入正确的数字',
                    min: '返现券金额不能为负数',
                    max: '返现券金额不能超过1000元'
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
</script>