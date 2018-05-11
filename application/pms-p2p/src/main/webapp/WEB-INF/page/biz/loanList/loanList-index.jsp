<%@page language="java" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page import="org.apache.shiro.SecurityUtils" %>
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
                <li class="active"><a href="${path}/biz/index">业务管理</a></li>
                <li class="active"><b>放款管理</b></li>
            </ol>
        </div>
        <div class="col-md-12">
            <ul class="nav nav-tabs">
               <shiro:hasPermission name="loanList:view">
                    <li><a href="#tab1" data-toggle="tab">待放款</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="loanList:view">
                    <li><a href="#tab2" data-toggle="tab">已流标</a></li>
                </shiro:hasPermission>
            </ul>
            <!-- 选项卡面板 -->
            <div id="tabContent" class="tab-content">
                <shiro:hasPermission name="loanList:view">
                <div class="tab-pane fade" id="tab1">
                    <jsp:include page="loanList-loan.jsp"/>
                </div>
                </shiro:hasPermission>
                <shiro:hasPermission name="loanList:view">
                <div class="tab-pane fade" id="tab2">
                    <jsp:include page="loabList-notLoan.jsp"/>
                </div>
                </shiro:hasPermission>
            </div>
        </div>
    </div>
</div>
<script type="application/javascript">
    $(function () {
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
            } else {
                var hash = tab.attr('href').substr(1);
                $('#pagination_' + hash).trigger('tab.page');
            }

            $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
                // 获取已激活的标签页的名称
                var hash = e.target.hash;
                /*var loaded = $('#parent_form').data('loaded');
                if (loaded && loaded.contains(hash)) {
                    return;
                } else {
                    if (!loaded) {
                        loaded = new Array();
                    }
                    var query = $('#parent_form').data('query')
                    if (query) {
                        loaded.push(hash);
                    }
                    $('#parent_form').data('loaded', loaded);
                }*/
                var tab = $(hash);
                $('#pagination_' + hash.substr(1)).trigger('tab.page');
                $(e.target).data('clicked', true);
            });

            /*$(window).load( function(){
                var index = layer.load(0, {
                    shade: [0.4,'#fff',false] //0.1透明度的白色背景
                });
                layer.close();
            } );*/
        });
    });
</script>
