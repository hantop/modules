<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib uri="/WEB-INF/hasAnyPermissions" prefix="fenlibao"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<link rel="stylesheet" href="/static/component/bootstrap/css/bootstrap.min.css" type="text/css">
<link rel="stylesheet" href="/static/component/HoldOn/HoldOn.min.css">
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>

<link rel="stylesheet" href="/static/component/zTree_v3/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.exedit-3.5.js"></script>
<script type="text/javascript" src="/static/component/HoldOn/HoldOn.min.js"></script>

<div class="container  content-top">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/system/index">微信管理</a></li>
                <li class="active"><b>关键字回复消息管理</b></li>
            </ol>
            <form class="form-inline" method="post" id="dataform">
                <shiro:hasPermission name="weixinkeyword:create">
                    <div class="form-group">
                        <a class="btn btn-warning" href="${path}/weixin/msg/edit" role="button">添加</a>
                    </div>
                </shiro:hasPermission>
                <input type="hidden" id="page" name="page">
            </form>
            <hr/>
            <table class="table table-striped table-bordered table-condensed table-hover">
                <thead>
                <tr class="success">
                    <th>关键字</th>
                    <th>回复内容</th>
                    <th>公众号环境</th>
                    <fenlibao:hasAnyPermissions name="weixinkeyword:edit,weixinkeyword:delete">
                        <th>操作</th>
                    </fenlibao:hasAnyPermissions>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="item" items="${list}">
                    <tr>
                        <td>
                            <c:set var="testStr" value="${item.keyword}"/>
                            <c:choose>
                                <c:when test="${fn:length(testStr) > 18}">
                                    <c:out value="${fn:substring(testStr, 0, 18)}......"/>
                                </c:when>
                                <c:otherwise>
                                    <c:out value="${testStr}"/>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:set var="testStr" value="${item.content}"/>
                            <c:choose>
                                <c:when test="${fn:length(testStr) > 18}">
                                    <c:out value="${fn:substring(testStr, 0, 18)}......"/>
                                </c:when>
                                <c:otherwise>
                                    <c:out value="${testStr}"/>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>${item.activeProfiles == 'prod' ? '正式环境' : '测试环境'}</td>
                        <fenlibao:hasAnyPermissions name="weixinkeyword:edit,weixinkeyword:delete">
                            <td>
                                <shiro:hasPermission name="weixinkeyword:edit">
                                    <a href="${path}/weixin/msg/edit?id=${item.id}">编辑</a>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="weixinkeyword:delete">
                                    <a href="#" role="delete" identity="${item.id}">删除</a>
                                </shiro:hasPermission>
                            </td>
                        </fenlibao:hasAnyPermissions>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <tiles:insertDefinition name="paginator"/>
        </div>
    </div>
</div>
<shiro:hasPermission name="weixinkeyword:delete">
    <script type="application/javascript">
        (function ($) {
            $('a[role="delete"]').on('click', function () {
                var identity = $(this).attr('identity');
                bootbox.confirm({
                    title: '提示',
                    size: 'small',
                    message: "确认删除么",
                    callback: function (result) {
                        if (result) {
                            var page = "${paginator.pageNum}";
                            var id = [identity];
                            $.ajax(
                                    '${path}/weixin/msg/delete',
                                    {
                                        contentType: 'application/json; charset=utf-8',
                                        data: JSON.stringify(id),
                                        dataType: "json",
                                        type: "DELETE",
                                        beforeSend: function (xhr) {
                                            HoldOn.open({
                                                theme: "sk-cube-grid",
                                                message: '请稍等.....',
                                                backgroundColor: "#000"
                                            });
                                        }
                                    }
                            )
                                    .success(function (result) {
                                        if (result) {
                                            bootbox.alert({
                                                buttons: {
                                                    ok: {
                                                        label: 'OK'
                                                    }
                                                },
                                                title: '提示',
                                                size: 'small',
                                                message: '删除成功',
                                                callback: function () {
                                                    pagination(page);
                                                }
                                            });
                                        } else {
                                            bootbox.alert({
                                                buttons: {
                                                    ok: {
                                                        label: 'OK'
                                                    }
                                                },
                                                title: '提示',
                                                size: 'small',
                                                message: '删除失败'
                                            });
                                        }
                                    })
                                    .error(function (jqXHR, textStatus, errorThrown) {
                                        bootbox.alert({
                                            buttons: {
                                                ok: {
                                                    label: 'OK'
                                                }
                                            },
                                            title: '提示',
                                            size: 'small',
                                            message: jqXHR.responseJSON.message
                                        });
                                    })
                                    .complete(function (jqXHR, textStatus) {
                                        HoldOn.close();
                                    });
                        }

                    }
                })
            });
        })(jQuery);
    </script>
</shiro:hasPermission>