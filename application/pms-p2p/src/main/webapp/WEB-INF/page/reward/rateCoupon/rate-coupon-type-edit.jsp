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
            <form:form class="form-horizontal" role="form" action="${path}/reward/rateCoupon/rate-coupon-type-edit"
                       method="post"
                       commandName="rateCoupon">
                <form:hidden path="id"/>
                <spring:bind path="couponCode">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            <b class="text-danger">*</b>加息券代码:
                        </label>

                        <div class="col-sm-6">
                            <form:input path="couponCode" cssClass="form-control" maxlength="50"/>
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <!--如果没有错误时使用默认的提示文字，如果有错误，显示错误信息-->
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="couponCode" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">请输入2~50位的加息券代码</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                <spring:bind path="effectDay">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            <b class="text-danger">*</b>加息券有效期(天)：
                        </label>

                        <div class="col-sm-6">
                            <form:input path="effectDay" cssClass="form-control" maxlength="30"/>
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="effectDay" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">请输入正确加息券有效期</span>
                                </c:otherwise>
                            </c:choose>

                        </div>
                    </div>
                </spring:bind>
                <spring:bind path="scope">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            <b class="text-danger">*</b>加息券幅度(%)：
                        </label>

                        <div class="col-sm-6">
                            <form:input path="scope" cssClass="form-control" maxlength="10"/>
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="scope" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">输入正确加息券幅度</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                <label class="col-sm-2 control-label text-right">
                    <b class="text-danger">*</b>投资限额：
                </label>
                <spring:bind path="minInvestMoney">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <div class="col-sm-6">
                            <form:input path="minInvestMoney" cssClass="form-control" cssStyle="width: 100px;" />
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <!--如果没有错误时使用默认的提示文字，如果有错误，显示错误信息-->
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="minInvestMoney" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">输入合法的最低投资限额</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                <label class="col-sm-2 control-label text-right">
                    至
                </label>
                <spring:bind path="maxInvestMoney">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <div class="col-sm-6">
                            <form:input path="maxInvestMoney" cssClass="form-control" cssStyle="width: 100px;" />
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <!--如果没有错误时使用默认的提示文字，如果有错误，显示错误信息-->
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="maxInvestMoney" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">输入合法的最高投资限额</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
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
                            <!--如果没有错误时使用默认的提示文字，如果有错误，显示错误信息-->
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="bidTypeIds" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">请选择对应的产品类型</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                <label class="col-sm-2 control-label text-right">
                    <b class="text-danger">*</b>投资期限
                </label>
                <spring:bind path="minInvestDay">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <div class="col-sm-6">
                            <form:radiobutton id="temp" path="investDeadLineType" value="false"/>
                            不限<br/>
                            <form:radiobutton path="investDeadLineType" value="true" id="radioButton1" onclick="javascript: choose();"/>
                            按天
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <br/>
                            <form:input path="minInvestDay" cssClass="input-sm" maxlength="30"/>&nbsp;天
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <!--如果没有错误时使用默认的提示文字，如果有错误，显示错误信息-->
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="minInvestDay" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">输入合法的投资期限</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                <label class="col-sm-2 control-label text-right">
                    至
                </label>
                <spring:bind path="maxInvestDay">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <div class="col-sm-6">
                            <form:input path="maxInvestDay" cssClass="input-sm" maxlength="30"/>
                            天（大于等于至少于等于）
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <!--如果没有错误时使用默认的提示文字，如果有错误，显示错误信息-->
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="maxInvestDay" element="span" cssClass="help-block"/>
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
                        <button type="submit" class="btn btn-primary btn-sm">提交</button>
                        <a type="button" class="btn btn-default  btn-sm"
                           href="${path}/reward/rateCoupon/rate-coupon-type-list">取消</a>
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
                        <a href="${path}/reward/rateCoupon/rate-coupon-type-edit" class="btn btn-default">继续添加</a>
                        <a href="${path}/reward/rateCoupon/rate-coupon-type-list" class="btn btn-primary">返回列表</a>
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
                            <c:when test="${code == -2}">
                                加息券已发放，不能修改
                            </c:when>
                            <c:when test="${code == -1}">
                                加息券代码不能重复
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


<div id="tips" class="modal fade">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="mySmallModalLabel">Tips</h4>
            </div>
            <div class="modal-body tips"></div>
        </div>
    </div>
</div>

<script type="application/javascript">
    $(function(){
        var scope = $("#scope").val();
        if (scope != null && scope != '') {
            // 截断到小数后2位
            $("#scope").val((parseFloat(scope).toFixed(2)));
        }
    });

    $.validator.addMethod("compareInvestMoneyMethod",function(value, element, params){
        var minInvestMoney;
        var maxInvestMoney;
        var maxInvestMoneyStr = $("#maxInvestMoney").val();
        if ((!value) || (!maxInvestMoneyStr)) {
            return true;
        }
        minInvestMoney = parseInt(value);
        maxInvestMoney = parseInt(maxInvestMoneyStr);
        if (value <= maxInvestMoney) {
            return true;
        } else {
            return false;
        }
    },"最低投资限额不能大于最高投资限额");

    $.validator.addMethod("compareInvestDayMethod",function(value, element, params){
        var maxInvestDay;
        var minInvestDay;
        var maxInvestDayStr = $("#maxInvestDay").val();
        if ((!value) || (!maxInvestDayStr)) {
            return true;
        }
        minInvestDay = parseInt(value);
        maxInvestDay = parseInt(maxInvestDayStr);
        if (value <= maxInvestDay) {
            return true;
        } else {
            return false;
        }
    },"最低投资期限不能大于最高投资期限");

    $.validator.addMethod("verifyDecimalMethod", function(value, element) {
        return this.optional(element) || /^\d+(\.\d{1,2})?$/.test(value);
    }, "小数位不能超过二位");

    (function ($) {
        $('form[role="form"]').validate({
            rules: {
                couponCode: {
                    required: true,
                    rangelength: [2, 50]
                },
                bidTypeIds: {
                    required: true
                },
                effectDay: {
                    required: true,
                    digits: true,
                    min: 1,
                    max: 180
                },
                scope: {
                    required: true,
                    number: true,
                    min: 0.30,
                    max: 100.00,
                    verifyDecimalMethod : ""
                },
                minInvestMoney: {
                    required: false,
                    digits: true,
                    min: 100,
                    max: 10000000,
                    compareInvestMoneyMethod : ""
                },
                maxInvestMoney: {
                    required: false,
                    digits: true,
                    min: 100,
                    max: 10000000
                },
                minInvestDay: {
                    required: false,
                    digits: true,
                    min: 1,
                    max: 2000,
                    compareInvestDayMethod : ""
                },
                maxInvestDay: {
                    required: false,
                    digits: true,
                    min: 1,
                    max: 2000
                }
            },
            messages: {
                couponCode: {
                    required: "加息券代码不能为空",
                    rangelength: $.validator.format("加息券代码字符长度在 {0} 到 {1} 之间.")
                },
                bidTypeIds: {
                    required: "请选择对应的产品类型"
                },
                effectDay: {
                    required: "加息券有效期不能为空",
                    digits: '请输入正确的整数',
                    min: '加息券有效期天数不能小于{0}天',
                    max: '加息券有效期不能超过{0}天'
                },
                scope: {
                    required: '加息券幅度不能为空',
                    number: '请输入正确的数字',
                    min: '加息券幅度不能低于{0}',
                    max: '加息券幅度不能超过{0}'
                },
                minInvestMoney: {
                    required: "最低投资限额不能为空",
                    digits: '请输入正确的整数',
                    min: '最低投资限额不能低于{0}',
                    max: '最低投资限额不能超过{0}'
                },
                maxInvestMoney: {
                    required: "最高投资限额不能为空",
                    digits: '请输入正确的整数',
                    min: '最高投资限额不能低于{0}',
                    max: '最高投资限额不能超过{0}'
                },
                minInvestDay: {
                    required: "最低投资期限不能为空",
                    digits: '请输入正确的整数',
                    min: '最低投资期限不能低于{0}',
                    max: '最低投资期限不能超过{0}'
                },
                maxInvestDay: {
                    required: "最高投资期限不能为空",
                    digits: '请输入正确的整数',
                    min: '最高投资期限不能低于{0}',
                    max: '最高投资期限不能超过{0}'
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
                var minInvestMoney = $("#minInvestMoney").val();
                var maxInvestMoney = $("#maxInvestMoney").val();
                if (!minInvestMoney) {
                    $(".modal-body.tips").html("<p class='text-success'><b>最小投资限额不能为空</b></p>");
                    $("#tips").modal("show");
                    return false;
                }
                if (!maxInvestMoney) {
                    $(".modal-body.tips").html("<p class='text-success'><b>最大投资限额不能为空</b></p>");
                    $("#tips").modal("show");
                    return false;
                }
                var minInvestDay = $("#minInvestDay").val();
                var maxInvestDay = $("#maxInvestDay").val();
                var investDeadLineType = $('input:radio[name=investDeadLineType]:checked').val();
                if (investDeadLineType == "true") {
                    if (!minInvestDay) {
                        $(".modal-body.tips").html("<p class='text-success'><b>最小投资期限不能为空</b></p>");
                        $("#tips").modal("show");
                        return false;
                    }
                    if (!maxInvestDay) {
                        $(".modal-body.tips").html("<p class='text-success'><b>最大投资期限不能为空</b></p>");
                        $("#tips").modal("show");
                        return false;
                    }
                }
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