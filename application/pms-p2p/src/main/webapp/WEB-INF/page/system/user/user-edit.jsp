<%--
  Created by IntelliJ IDEA.
  User: bogle
  Date: 2015/11/10
  Time: 15:28
  To change this template use File | Settings | File Templates.
--%>
<%@page language="java" pageEncoding="utf-8" %>
<%@ page import="com.alibaba.fastjson.JSON" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<link rel="stylesheet" href="/static/component/HoldOn/HoldOn.min.css">
<link rel="stylesheet" href="/static/component/zTree_v3/css/zTreeStyle/zTreeStyle.css" type="text/css">
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
                <li class="active"><a href="${path}/system/index">系统管理</a></li>
                <li class="active"><a href="${path}/system/pmsuser">系统用户</a></li>
                <li class="active"><b>${title}用户</b></li>
            </ol>
            <span class="badge" style="font-size: 18px;">${title}</span>
            <form:form class="form-horizontal" role="form" action="${path}/system/pmsuser/saveOrUpdate"
                       method="post"
                       commandName="user">
                <form:hidden path="id"/>
                <spring:bind path="username">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            登陆账号：
                        </label>

                        <div class="col-sm-6">
                            <form:input path="username" cssClass="form-control input-sm ${user.id > 0 ? 'ignore' : ''}" maxlength="20"  readonly="${user.id > 0 ? true : false}"/>
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <!--如果没有错误时使用默认的提示文字，如果有错误，显示错误信息-->
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="username" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">请输入2~20位的登陆账号</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                <spring:bind path="password">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            登陆密码：
                        </label>

                        <div class="col-sm-6">
                            <form:password path="password" cssClass="form-control input-sm ignores ${user.id > 0 ? 'ignore' : ''}" maxlength="12" id="password"/>
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="password" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">请输入8位以上数字与字母组合的密码</span>
                                </c:otherwise>
                            </c:choose>

                        </div>
                    </div>
                </spring:bind>
                <spring:bind path="repassword">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            确认密码：
                        </label>

                        <div class="col-sm-6">
                            <form:password path="repassword" cssClass="form-control  input-sm ignores ${user.id > 0 ? 'ignore' : ''}" maxlength="12"/>
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="repassword" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">确认密码</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                <spring:bind path="realname">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            真实姓名：
                        </label>
                        <div class="col-sm-6">
                            <form:input path="realname" cssClass="form-control  input-sm" maxlength="10"/>
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="realname" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">请输入2~10位的真实姓名</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                <spring:bind path="phone">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            手机号码：
                        </label>
                        <div class="col-sm-6">
                            <form:input path="phone" cssClass="form-control  input-sm" maxlength="11"/>
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="phone" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">请输入11位的手机号码</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                <spring:bind path="email">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            邮箱：
                        </label>
                        <div class="col-sm-6">
                            <form:input path="email" cssClass="form-control  input-sm" maxlength="30"/>
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="email" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">请输入3~30位的邮箱</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                <spring:bind path="status">
                    <div class="form-group">
                        <label class="col-sm-2 control-label text-right">
                            用户状态：
                        </label>

                        <div class="col-sm-6">
                            <form:select path="status" cssClass="form-control">
                                <form:option value="1">启用</form:option>
                                <form:option value="0">锁定</form:option>
                            </form:select>
                        </div>
                        <div class="col-sm-3 help">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="status" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">请选择用户状态</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                <div class="form-group">
                    <label class="col-sm-2 control-label">部门设置：</label>
                    <div class="col-sm-2">
                        <input type="hidden" id="organizationId" value="${groupMsg.id}" name="groupId"/>
                        <input type="text" id="organizationName" name="organizationName" class="form-control input-sm" value="${groupMsg.name}" placeholder="选择" readonly>
                    </div>
                   <%-- <a id="menuBtn" href="#" style="font-size: 16px">选择</a>--%>
                    <div class="col-sm-3 help">
                        <c:choose>
                            <c:when test="${status.error}">
                                <form:errors path="organizationName" name="organizationName" element="span" cssClass="help-block"/>
                            </c:when>
                        </c:choose>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <button type="submit" class="btn btn-primary btn-sm">提交</button>
                        <a type="button" class="btn btn-default btn-sm" href="${path}/system/pmsuser">取消</a>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>
<div id="menuContent" class="menuContent" style="display:none; position: absolute;">
    <ul id="tree" class="ztree" style="margin-top:0; width:160px;"></ul>
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
                        <a href="${path}/system/pmsuser/saveOrUpdate" class="btn btn-default btn-sm">继续添加</a>
                        <a href="${path}/system/pmsuser" class="btn btn-primary btn-sm">返回列表</a>
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
                                ${error}
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
        var treeObj;

        var setting = {
            view: {
                dblClickExpand: false,
                selectedMulti: false
            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            callback: {
                onClick: onClick
            }
        };

        var zNodes = [
            <c:forEach items="${groupTree}" var="o">
            { id:${o.id}, pId:${o.parentId}, name:"${o.name}", orderId:"${o.available}", marks:"${o.mark}", open:${o.rootNode}},
            </c:forEach>
        ];

        treeObj = $.fn.zTree.init($("#tree"), setting, zNodes);
        $("#menuBtn,#organizationName").click(showMenu);

        function onClick(e, treeId, treeNode) {
            $("#organizationId").val(treeNode.id);
            $("#organizationName").val(treeNode.name);
            hideMenu();
        }

        function showMenu() {
            var cityObj = $("#organizationName");
            var cityOffset = $("#organizationName").offset();
            $("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");

            $("body").bind("mousedown", onBodyDown);
        }

        function onBodyDown(event) {
            if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
                hideMenu();
            }
        }

        function hideMenu() {
            $("#menuContent").fadeOut("fast");
            $("body").unbind("mousedown", onBodyDown);
        }

        var id = "${user.id}";
        if(id > 0) {
            $(".ignores").on('keyup',function() {
                var val = $(this).val();
                if(val == ''&& !$(this).hasClass('ignore')) {
                    $(this).addClass('ignore')
                } else if($(this).hasClass('ignore')) {
                    $(this).removeClass('ignore');
                }
            });
        }

        $('form[role="form"]').validate({
            rules: {
                username: {
                    required: true,
                    rangelength: [2, 20]
                },
                password: {
                    required: true,
                    regexPassword:true,
                },
                repassword: {
                    required: true,
                    equalTo:'#password'
                },
                realname: {
                    required: true,
                    rangelength: [2, 10]
                },
                phone: {
                    required: false,
                    rangelength: [11, 11]
                },
                email: {
                    required: false,
                    rangelength: [3, 30]
                },
                organizationName:{
                    required: true,
                    rangelength: [2, 20]
                }
            },
            messages: {
                username: {
                    required: "登陆账号不能为空",
                    rangelength: $.validator.format("登陆账号字符长度在 {0} 到 {1} 之间.")
                },
                password: {
                    required: "登陆密码不能为空",
                    regexPassword:"必须是8位以上的数字与字母组合，大小写敏感",
                },
                repassword: {
                    required: "重复密码不能为空",
                    equalTo: '密码和重复密码不一致'
                },
                realname: {
                    required: "真实姓名不能为空",
                    rangelength: $.validator.format("真实姓名字符长度在 {0} 到 {1} 之间.")
                },
                phone: {
                    required: "手机号不能为空",
                    rangelength: $.validator.format("手机号字符长度必须在 {0} 位.")
                },
                email: {
                    required: "邮箱不能为空",
                    rangelength: $.validator.format("邮箱字符长度在 {0} 到 {1} 之间.")
                },
                organizationName: {
                    required: "部门设置不能为空",
                    rangelength: $.validator.format("部门设置符长度在 {0} 到 {1} 之间.")
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
    jQuery.validator.addMethod("regexPassword", function(value, element) {
        return this.optional(element) || /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,32}$/.test(value);
    }, "");
</script>



