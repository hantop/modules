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
        <div class="col-sm-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/cs/index">财务管理</a></li>
                <li class="active"><a href="${path}/cs/guarantee">平台账户管理</a></li>
                <li class="active"><b>提现结果</b></li>
            </ol>
        </div>
        <br><br>
        <br><br><br>
        <div class="col-sm-12" style="text-align: center;">
            <span style="font-size: x-large;font-weight: bold;">
				 <span class="glyphicon glyphicon-time" style="color: rgb(20, 171, 255);"></span>
				 提现申请已提交，等待银行处理
            </span>
            <br>
            <span>提现将在审核通过后到账，节假日顺延</span>
        </div>
    </div>
</div>

<script type="application/javascript">
    (function ($) {
    })(jQuery)
</script>



