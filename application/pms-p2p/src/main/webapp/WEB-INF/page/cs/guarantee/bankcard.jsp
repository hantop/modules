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
        <ol class="breadcrumb">
            <li class="active"><a href="${path}/cs/index">客户管理</a></li>
            <li class="active"><a href="${path}/cs/guarantee">担保用户管理</a></li>
            <li class="active"><b>担保用户企业绑卡</b></li>
        </ol>
        <div class="" style="margin-top: 20px;margin-left: 80px;">
            <form:form class="form" id="bindInfo_form"  role="form" action=""
                       method="post"
                       commandName="bindInfo" >
                <form:input type="hidden" path="userId" value="${userId}"/>
                <form:input type="hidden" path="userType" value="ORGANIZATION"/>
                <div class="form-group">
                    <label for="bankCode_select_id">
                        开户银行
                    </label>
                    <form:select path="bankCode" id="bankCode_select_id" class="form-control" style="width:240px;">
                        <form:option value="">请选择</form:option>
                        <form:options items="${bankCodes}" itemLabel="name" itemValue="code"></form:options>
                    </form:select>
                </div>
                <div class="form-group">
                    <label for="bankcardNo_id">
                        银行卡号
                    </label>
                    <form:input id="bankcardNo_id" path="bankcardNo" cssClass="form-control" maxlength="40"  style="width:240px;"/> <%-- input-sm--%>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-1 col-sm-6"  style="margin-top: 12px;">
                        <a type="button" class="btn btn-default btn-sm" href="${path}/cs/guarantee">取消</a>
                        <input type="button" id="submit_bindBank_form" class="btn btn-primary" value="提交" readonly/>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>
<form id="bind_enpcard_form" action="" method="post"></form>
<script type="application/javascript">
    (function ($) {
        $("#submit_bindBank_form").bind('click', function () {
            $.ajax({
                type: "post",
                async: false,
                url: '${path}/cs/guarantee/bindBank',
                data: $("#bindInfo_form").serialize(),
                dataType: "json",
                success: function(data){
                    if (data.code != '200') {
                        bootbox.alert(data.message);
                        return;
                    }
                    $("#bind_enpcard_form").attr('action', data.data.postUrl);
                    $.each(data.data.postParams, function (index, obj) {
                        $('#bind_enpcard_form').append('<input type="hidden" name=\'' + obj.key + '\' value=\'' + obj.value + '\'/>');
                    });
                    $("#bind_enpcard_form").submit(); // 提交到新网
                },
                error: function (e) {
                    console.log(e);
                }
            });
        })
    })(jQuery);
</script>



