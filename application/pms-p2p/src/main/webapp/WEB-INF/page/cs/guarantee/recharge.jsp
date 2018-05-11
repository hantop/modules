<%--
  Created by IntelliJ IDEA.
  User: bogle
  Date: 2015/11/10
  Time: 15:28
  To change this template use File | Settings | File Templates.
--%>
<%@page language="java" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<link href="/static/component/JqueryPagination/mricode.pagination.css" rel="stylesheet">

<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<script type="text/javascript" src="/static/component/jquery-form/jquery.form.js"></script>
<script type="text/javascript" src="/static/component/JqueryPagination/mricode.pagination.js"></script>
<script type="text/javascript" src="/static/component/JqueryPagination/jsrender.js"></script>
<script type="text/javascript" src="/static/component/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/static/component/datepicker/calendar.js"></script>
<script type="application/javascript">
    (function ($) {
        $.fn.serializeObject = function () {
            "use strict";

            var result = {};
            var extend = function (i, element) {
                var node = result[element.name];

                // If node with same name exists already, need to convert it to an array as it
                // is a multi-value field (i.e., checkboxes)
                if ($.trim(element.value) !== "") {
                    if ('undefined' !== typeof node && node !== null) {
                        if ($.isArray(node)) {
                            node.push(element.value);
                        } else {
                            result[element.name] = [node, element.value];
                        }
                    } else {
                        result[element.name] = element.value;
                    }
                }
            };
            $.each(this.serializeArray(), extend);
            return result;
        };
    })(jQuery);

    Array.prototype.contains = function (obj) {
        var i = this.length;
        while (i--) {
            if (this[i] === obj) {
                return true;
            }
        }
        return false;
    }
</script>

<style type="text/css">
    /**去除bootstrap td 的边框*/
    table.account tr td {
        border: none;
    }
</style>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/cs/index">客户管理</a></li>
                <li class="active"><a href="${path}/cs/guarantee">担保用户管理</a></li>
                <li class="active"><b>担保用户充值</b></li>
            </ol>
        </div>
        <div class="col-md-8">
            <ul class="nav nav-tabs">
                <%--<shiro:hasPermission name="rechargeTab:view">--%>
                    <li><a href="#tab1" data-toggle="tab">网银充值</a></li>
                <%--</shiro:hasPermission>--%>
                    <c:if test="${userType == 'PERSONAL'}">
                    <%--<shiro:hasPermission name="rewardTab:view">--%>
                        <li><a href="#tab2" data-toggle="tab">快捷充值</a></li>
                    <%--</shiro:hasPermission>--%>
                    </c:if>
                    <span style="display: inline-block; float: right;margin: 10px 120px 0 0;">
                        账户余额:<strong>￥${balance}</strong>
                    </span>
            </ul>
            <div id="tabContent" class="tab-content">
                <%--<shiro:hasPermission name="rechargeTab:view">--%>
                <div class="tab-pane fade" id="tab1">
                    <jsp:include page="online-recharge.jsp"/>
                </div>
                <%--</shiro:hasPermission>--%>
                <c:if test="${userType == 'PERSONAL'}">
                    <%--<shiro:hasPermission name="rewardTab:view">--%>
                    <div class="tab-pane fade" id="tab2">
                        <jsp:include page="fast-recharge.jsp"/>
                    </div>
                    <%--</shiro:hasPermission>--%>
                </c:if>
            </div>
        </div>
    </div>
</div>
<c:choose>
    <c:when test="${!hasUser}">
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
                        没有此用户信息
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
    $(function () {
        $.ajaxSetup({
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            complete: function (data, TS) {
                //console.info(arguments);
                //对返回的数据data做判断，
                //session过期的话，就location到一个页面
                if(TS !== 'success') {
                    window.location.reload();
                }
            }
        });
        var tab = $('.nav-tabs li.active > a');
        if (tab.length == 0) {
            tab = $('.nav-tabs a[href="#tab1"]');
        }
        if (!tab.data('clicked')) {
            tab.tab('show');
        }
    });
</script>
