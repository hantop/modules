<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<link rel="stylesheet" href="/static/component/HoldOn/HoldOn.min.css">
<link rel="stylesheet" href="/static/component/zTree_v3/css/zTreeStyle/zTreeStyle.css" type="text/css">
<link rel="stylesheet" href="/static/component/bootstrap/css/bootstrap.min.css" type="text/css">
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
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
                <li class="active"><b>借款申请编辑</b></li>
            </ol>
            <span class="badge" style="font-size: 18px;">${title}</span>
            <form:form class="form-horizontal" role="form" action="${path}/biz/loanapplication/edit"
                       method="post"
                       commandName="loanApplication">
                <form:hidden path="id"/>
                <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                        手机号码：
                    </label>
                    <div class="col-sm-6">
                        ${loanApplication.phonenum}
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                        联系人姓名：
                    </label>
                    <div class="col-sm-6">
                        ${loanApplication.contacts}
                    </div>
                </div>
                <spring:bind path="amountRange">
                    <div class="form-group">
                        <label class="col-sm-2 control-label text-right">
                            借款金额：
                        </label>
                        <div class="col-sm-6">
                            <form:select path="amountRange" cssClass="form-control">
                                <c:forEach items="${amountRanges}" var="item">
                                    <form:option value="${item}">${item}</form:option>
                                </c:forEach>
                            </form:select>
                        </div>
                    </div>
                </spring:bind>
                <spring:bind path="districtFullName">
                    <div class="form-group">
                        <label class="col-sm-2 control-label text-right">
                            所在区域：
                        </label>
                        <div class="col-sm-6">
                            <form:input path="districtFullName" cssClass="form-control  input-sm"/>
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <c:choose>
                                <c:when test="${districtFullName.error}">
                                    <form:errors path="districtFullName" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">请输入所在区域</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                <spring:bind path="annualIncome">
                    <div class="form-group">
                        <label class="col-sm-2 control-label text-right">
                            月收入：
                        </label>
                        <div class="col-sm-6">
                            <form:select path="annualIncome" cssClass="form-control">
                                <c:forEach items="${annualIncomes}" var="item">
                                    <form:option value="${item}">${item}</form:option>
                                </c:forEach>
                            </form:select>
                        </div>
                    </div>
                </spring:bind>
                <spring:bind path="hasRoom">
                    <div class="form-group">
                        <label class="col-sm-2 control-label text-right">
                            是否有房：
                        </label>
                        <div class="col-sm-6">
                            <form:select path="hasRoom" cssClass="form-control">
                                <form:option value="1">是</form:option>
                                <form:option value="0">否</form:option>
                            </form:select>
                        </div>
                    </div>
                </spring:bind>
                <spring:bind path="hasCar">
                    <div class="form-group">
                        <label class="col-sm-2 control-label text-right">
                            是否有车：
                        </label>
                        <div class="col-sm-6">
                            <form:select path="hasCar" cssClass="form-control">
                                <form:option value="1">是</form:option>
                                <form:option value="0">否</form:option>
                            </form:select>
                        </div>
                    </div>
                </spring:bind>
                <spring:bind path="processingStatus">
                    <div class="form-group">
                        <label class="col-sm-2 control-label text-right">
                            处理意见：
                        </label>
                        <div class="col-sm-6">
                            <form:select path="processingStatus" onchange="setReasonDiv(this.value)" cssClass="form-control">
                                <form:option value="1">通过</form:option>
                                <form:option value="2">不通过</form:option>
                            </form:select>
                        </div>
                    </div>
                </spring:bind>
                <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                        申请时间：
                    </label>
                    <div class="col-sm-6">
                        <fmt:formatDate value="${loanApplication.createTime}" pattern="yyyy-MM-dd HH:mm:ss" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                        编辑时间：
                    </label>
                    <div class="col-sm-6">
                        <fmt:formatDate value="${loanApplication.updateTime}" pattern="yyyy-MM-dd HH:mm:ss" />
                    </div>
                </div>
                <div id="reasonDiv">
                    <spring:bind path="nopassReason">
                    <div class="form-group">
                        <label class="col-sm-2 control-label text-right">
                            选择理由：
                        </label>
                        <div class="col-sm-6">
                            <form:select path="nopassReason" onchange="setNopassProcessingOpinion(this.value);" cssClass="form-control">
                                <c:forEach items="${nopassReasonTitles}" var="item">
                                    <option value="${item.enumKey}"
                                            <c:if test="${item.enumKey == loanApplication.nopassReason}">
                                                selected
                                            </c:if>
                                    >${item.enumValue}</option>
                                </c:forEach>
                                <option value="0"
                                        sc<c:if test="${loanApplication.nopassReason == null || loanApplication.nopassReason == 0}">
                                            selected
                                        </c:if>
                                >其他</option>
                            </form:select>
                        </div>
                    </div>
                    </spring:bind>
                </div>
                <spring:bind path="processingOpinion">
                <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                    </label>
                    <div class="col-sm-6">
                        <form:textarea id="processingOpinion" path="processingOpinion" rows="6" cssClass="form-control  input-sm"></form:textarea>
                    </div>
                    <div class="col-sm-3 help">
                        <c:choose>
                            <c:when test="${status.error}">
                                <form:errors path="processingOpinion" element="span" cssClass="help-block"/>
                            </c:when>
                            <c:otherwise>
                                <span class="help-block">最多输入1000字</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                </spring:bind>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <a type="button" class="btn btn-default btn-sm" href="${path}/biz/loanapplication">返回</a>
                        <button type="submit" class="btn btn-primary btn-sm">提交</button>
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
                        <a href="${path}/biz/loanapplication" class="btn btn-primary">返回列表</a>
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
                            <c:when test="${code <= 0}">
                                编辑失败，请联系管理员
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


<script type="text/javascript">
    var reasonDiv = $("#reasonDiv");
    var processingOpinion = $("#processingOpinion");
//    $().ready(function(){
    (function ($) {
        if (${loanApplication.processingStatus} == 2) {
            // 初始化不通过理由内容
            if (${loanApplication.nopassReason != 0}) {
                setNopassProcessingOpinion(${loanApplication.nopassReason});
            }
            reasonDiv.show();
        } else {
            reasonDiv.hide();
        }

        // form表单字段验证
        $('form[role="form"]').validate({
            rules: {
                districtFullName : {
                    required : true,
                    rangelength : [3, 30]
                },
                processingOpinion : {
                    required : false,
                    maxlength : 1000
                }
            },
            messages: {
                districtFullName: {
                    required: "所在区域不能为空",
                    rangelength: $.validator.format("所在区域字符长度在 {0} 到 {1} 之间.")
                },
                processingOpinion: {
                    required: "最多输入1000字",
                    rangelength: $.validator.format("最多输入{0}字.")
                }
            },
            /*highlight: function (element, errorClass, validClass) {
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
            },*/
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
    })(jQuery);

    function setReasonDiv(value) {
        if (value == 1) {
            reasonDiv.hide();
            processingOpinion.attr("disabled", false);
            processingOpinion.val("");
        } else {
            reasonDiv.show();
        }
    }

    function setNopassProcessingOpinion(titleId) {
        if (titleId == 0) {
            processingOpinion.val("");
            processingOpinion.attr("disabled", false);
        } else {
            $.ajax({
                type : "get",
                url : path + "/biz/loanapplication/nopassReasonContent",
                dataType : "json",
                data : {
                    key : titleId
                },
                success:function(data) {
                    processingOpinion.val(data);
                    processingOpinion.attr("disabled", true);
                }
            });
        }
    }
</script>