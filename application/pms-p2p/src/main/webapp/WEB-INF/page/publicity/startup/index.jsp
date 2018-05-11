<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<link rel="stylesheet" type="text/css" href="/static/component/jqueryui/jquery-ui.min.css">
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<link rel="stylesheet" href="/static/component/HoldOn/HoldOn.min.css">
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script type="text/javascript" src="/static/component/HoldOn/HoldOn.min.js"></script>
<style type="text/css">
    .content-row {
        margin-top: 7px !important;
        margin-bottom: 7px !important;
    }

    .table {
        table-layout: fixed;
    }

    .table td {
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }

    .modal-content {
        padding: 7px;
    }

    .updateOriginalName {
        text-indent: 1em;
        font-weight: bold;
        color: darkgray;
    }
</style>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}">首页</a></li>
                <li class="active"><b>启动图设置</b></li>
            </ol>
            <div>
                <form:form id="dataform" class="form-inline" method="post" action="${path}/publicity/startup"
                    commandName="startupImageForm">
                    <div class="form-group">
                        <label for="name">名称</label>
                        <form:input type="text" class="form-control input-sm" path="name" id="name" name="name" value="${startupImageForm.name}"/>
                    </div>
                    <div class="form-group">
                        <label for="clientType">
                            客户端类型：
                        </label>
                        <form:select path="clientType" cssClass="form-control">
                            <form:option value="">
                                全部
                            </form:option>
                            <form:options items="${clientTypes}" itemLabel="name" itemValue="code"/>
                        </form:select>
                    </div>
                    <shiro:hasPermission name="publicityStartup:search">
                        <button id="searchBtn" type="button" class="btn btn-primary btn-sm">查询</button>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="publicityStartup:upload">
                        <a class="btn btn-success btn-sm" href="${path}/publicity/startup/new">新增</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="publicityStartup:delete">
                        <button type="button" class="btn btn-danger btn-sm" onclick="javascript:deleteImages();">删除</button>
                    </shiro:hasPermission>
                    <form type="hidden" id="page" name="page"/>
                </form:form>
            </div>
            <hr>
            <div class="table-responsive">
                <table id="datatable" class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th width="3%" class="text-center"><input type="checkbox" onclick="javascript:checkAll(this);"></th>
                        <th width="5%">序号</th>
                        <th width="30%">名称</th>
                        <th width="20%">处理时间</th>
                        <th width="5%">可用性</th>
                        <th width="17%">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${list}" var="v" varStatus="status">
                        <tr>
                            <td class="text-center">
                                <input name="item" type="checkbox" onclick="checkItem(this);" value="${v.id}">
                            </td>
                            <td>${status.index + 1}</td>
                            <td>${v.name}</td>
                            <td><fmt:formatDate value="${v.updateTime}"
                                                pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                            <td>
                                <c:choose>
                                    <c:when test="${v.status == 1}">
                                        启用
                                    </c:when>
                                    <c:otherwise>
                                        禁用
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <shiro:hasPermission name="publicityStartup:edit">
                                    <a href="${path}/publicity/startup/edit/${v.id}">编辑</a>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="publicityStartup:delete">
                                    <a href="#" onclick="deleteImage(${v.id});return false">删除</a>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="publicityStartup:edit">
                                    <c:choose>
                                        <c:when test="${v.status == 1}">
                                            <a href="#" onclick="setStatus(${v.id}, 0);return false">禁用</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="#" onclick="setStatus(${v.id}, 1);return false">启用</a>
                                        </c:otherwise>
                                    </c:choose>
                                </shiro:hasPermission>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <tiles:insertDefinition name="paginator"/>
        </div>
    </div>
</div>

<div id="confirm" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Tips</h4>
            </div>
            <div class="modal-body">
                <p class="text-danger"><b>确定要删除吗？</b></p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" onclick="javascript: doDeleteImages();">确定</button>
            </div>
        </div>
    </div>
</div>
<div id="tips" class="modal fade">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                <h4 class="modal-title" id="mySmallModalLabel">Tips</h4>
            </div>
            <div class="modal-body tips"></div>
        </div>
    </div>
</div>
<script type="text/javascript" src="/static/component/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript">
    function setStatus(id, status) {
        bootbox.confirm({
            title: '提示',
            size: 'small',
            message: "确认设置" + (status == 1 ? "启用" : "禁用") +"么",
            callback: function (result) {
                if (result) {
                    // ajax请求删除
                    $.ajax({
                        method: "put",
                        url: path + "/publicity/startup/status/",
                        dataType: "json",
                        data : {
                            id : id,
                            status : status
                        }
                    }).success(function (data) {
                        if (result) {
                            bootbox.alert({
                                buttons: {
                                    ok: {
                                        label: 'OK'
                                    }
                                },
                                title: '提示',
                                size: 'small',
                                message: '设置成功',
                                callback: function () {
                                    window.location.reload();
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
                                message: '设置失败'
                            });
                        }
                    });
                }
            }
        })
    }

    function deleteImage(id) {
        bootbox.confirm({
            title: '提示',
            size: 'small',
            message: "确认删除么",
            callback: function (result) {
                if (result) {
                    // ajax请求删除
                    $.ajax({
                        method: "delete",
                        url: path + "/publicity/startup/delete/" + id,
                        dataType: "json"
                    }).success(function (data) {
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
                                    window.location.reload();
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
                    });
                }
            }
        });
    }

    $('#searchBtn').bind('click', function() {
        var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $("#dataform").submit();
        layer.close();
    });

    function checkAll(checkbox) {
        if ($(checkbox).is(":checked")) {
            $("input[name=item]").prop("checked", "checked");
            $("#datatable tr").addClass("warning");
        } else {
            $("input[name=item]").prop("checked", false);
            $("#datatable tr").removeClass("warning");
        }
    }

    function checkItem(checkbox) {
        if ($(checkbox).is(":checked")) {
            jQuery(checkbox).parent().parent().addClass("warning");
        } else {
            jQuery(checkbox).parent().parent().removeClass("warning");
        }
    }

    function deleteImages() {
        var items = $("input[name='item']:checked");
        var length = items.length;
        if (length < 1) {
            $(".modal-body.tips").html("<p class='text-danger'><b>请选中要删除的元素</b></p>");
            $("#tips").modal("show");
            return false;
        } else {
            $("#confirm").modal("show");
            return false;
        }
    }

    function doDeleteImages() {
        $("#confirm").modal("hide");
        var items = $("input[name='item']:checked");
        var length = items.length;
        var ids = "";
        for (var i = 0; i < length; i++) {
            ids += $(items[i]).val() + ",";
        }
        $.ajax({
            method: "delete",
            url: path + "/publicity/startup/delete/" + ids.substring(0, ids.length - 1),
            dataType: "json"
        }).success(function(event) {
            if (event.code == 200) {
                $(".modal-body.tips").html("<p class='text-success'><b>删除成功</b></p>");
                $("#tips").modal("show").on('hide.bs.modal', function (e) {
                    window.location.reload(true);
                });
            }
        }).error(function(event) {
            $(".modal-body.tips").html("<p class='text-danger'><b>服务器异常</b></p>");
            $("#tips").modal("show");
            return false;
        });
    }

    function uploadImage() {
        $('#uploaderDiv').modal("show").on('hide.bs.modal', function (e) {
            window.location.reload(true);
        });
    }

</script>