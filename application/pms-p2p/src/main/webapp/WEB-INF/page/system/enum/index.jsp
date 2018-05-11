<%--
  Created by IntelliJ IDEA.
  User: Lullaby
  Date: 2015/7/24
  Time: 16:26
  To change this template use File | Settings | File Templates.
--%>
<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<style type="text/css">
    .modal-dialog.plupload {
        margin: 10% auto !important;
    }

    .plupload_view_list .plupload_file {
        width: 99.8% !important;
    }

    #uploader_dropbox .plupload_filelist_content {
        width: 99.8% !important;
    }

    .modal-content {
        padding: 0 10px 0 10px;
    }
</style>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}">首页</a></li>
                <li class="active"><a>客户端设置</a></li>
                <li class="active"><b>系统枚举管理</b></li>
            </ol>
            <div>
                <form id="dataform" class="form-inline" method="post" action="${path}/setting/enum">
                    <div class="form-group">
                        <label for="enumTable">枚举表名</label>
                        <input type="text" class="form-control input-sm" id="enumTable" name="enumTable" value="${tenum.enumTable}">
                    </div>
                    <div class="form-group">
                        <label for="enumColumn">枚举字段名</label>
                        <input type="text" class="form-control input-sm" id="enumColumn" name="enumColumn" value="${tenum.enumColumn}">
                    </div>
                    <shiro:hasPermission name="settingEnum:search">
                        <button id="searchBtn" type="button" class="btn btn-primary btn-sm">查询</button>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="settingEnum:create">
                        <button type="button" class="btn btn-success btn-sm" onclick="javascript: $('#addDiv').modal('show');">新增</button>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="settingEnum:update">
                        <button type="button" class="btn btn-warning btn-sm" onclick="javascript: prepareUpdateEnum();">修改</button>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="settingEnum:delete">
                        <button type="button" class="btn btn-danger btn-sm" onclick="javascript:deleteEnum();">删除</button>
                    </shiro:hasPermission>
                    <input type="hidden" id="page" name="page">
                </form>
            </div>
            <hr>
            <div class="table-responsive">
                <table id="datatable" class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th class="text-center" width="50"><input type="checkbox" onclick="javascript:checkAll(this);"></th>
                        <th>枚举表名</th>
                        <th>枚举字段名</th>
                        <th>枚举值</th>
                        <th>枚举描述</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${list}" var="v">
                        <tr ondblclick="javascript: updateEnum('${v.id}');">
                            <td class="text-center">
                                <input name="item" type="checkbox" onclick="checkItem(this);" value="${v.id}">
                            </td>
                            <td>${v.enumTable}</td>
                            <td>${v.enumColumn}</td>
                            <td>${v.enumKey}</td>
                            <td>${v.enumValue}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <div>
                <tiles:insertDefinition name="paginator"/>
            </div>
        </div>
    </div>
</div>
<div id="updateDiv" data-backdrop="static" class="modal fade" role="dialog" aria-labelledby="gridSystemModalLabel">
    <div class="modal-dialog plupload modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title text-center text-success" id="gridSystemModalLabel"><b>修改枚举</b></h5>
            </div>
            <form id="updateForm">
                <input type="hidden" id="updateId">
                <div class="form-group">
                    <label for="enumTableUpdate">枚举表明</label>
                    <input id="enumTableUpdate" type="text" class="form-control">
                </div>
                <div class="form-group">
                    <label for="enumColumnUpdate">枚举字段名</label>
                    <input id="enumColumnUpdate" type="text" class="form-control">
                </div>
                <div class="form-group">
                    <label for="enumKeyUpdate">枚举值</label>
                    <input id="enumKeyUpdate" type="text" class="form-control">
                </div>
                <div class="form-group">
                    <label for="enumValueUpdate">枚举描述</label>
                    <input id="enumValueUpdate" type="text" class="form-control">
                </div>
                <div class="form-group text-center">
                    <button type="button" class="btn btn-success" onclick="javascript: doUpdateEnum();">确定</button>
                    <button type="button" class="btn btn-warning" onclick="javascript: $('#updateDiv').modal('hide');">取消</button>
                </div>
            </form>
        </div>
    </div>
</div>
<div id="addDiv" data-backdrop="static" class="modal fade" role="dialog" aria-labelledby="gridSystemModalLabelAdd">
    <div class="modal-dialog plupload modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title text-center text-success" id="gridSystemModalLabelAdd"><b>新增枚举</b></h5>
            </div>
            <form id="addForm">
                <div class="form-group">
                    <label for="enumTableNew">枚举表明</label>
                    <input id="enumTableNew" type="text" class="form-control">
                </div>
                <div class="form-group">
                    <label for="enumColumnNew">枚举字段名</label>
                    <input id="enumColumnNew" type="text" class="form-control">
                </div>
                <div class="form-group">
                    <label for="enumKeyNew">枚举值</label>
                    <input id="enumKeyNew" type="text" class="form-control">
                </div>
                <div class="form-group">
                    <label for="enumValueNew">枚举描述</label>
                    <input id="enumValueNew" type="text" class="form-control">
                </div>
                <div class="form-group text-center">
                    <button type="button" class="btn btn-success" onclick="javascript: doAddEnum();">确定</button>
                    <button type="button" class="btn btn-warning" onclick="javascript: $('#addDiv').modal('hide');">取消</button>
                </div>
            </form>
        </div>
    </div>
</div>
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
<script type="text/javascript">
    $("#searchBtn").bind("click", function() {
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
        return false;
    }
    function checkItem(checkbox) {
        if ($(checkbox).is(":checked")) {
            jQuery(checkbox).parent().parent().addClass("warning");
        } else {
            jQuery(checkbox).parent().parent().removeClass("warning");
        }
        return false;
    }
    function deleteEnum() {
        var items = $("input[name='item']:checked");
        var length = items.length;
        if (length < 1) {
            $(".modal-body.tips").html("<p class='text-warning'><b>请选中要删除的元素</b></p>");
            $("#tips").modal("show");
            return false;
        } else {
            var ids = "";
            for (var i = 0; i < length; i++) {
                ids += $(items[i]).val() + ",";
            }
            $.ajax({
                method: "delete",
                url: path + "/setting/enum/" + ids.substring(0, ids.length - 1),
                dataType: "json",
            }).success(function (event) {
                if (event.code == 200) {
                    $(".modal-body.tips").html("<p class='text-success'><b>删除成功</b></p>");
                    $("#tips").modal("show").on('hide.bs.modal', function (e) {
                        window.location.reload(true);
                        return false;
                    });
                } else {
                    $(".modal-body.tips").html("<p class='text-danger'><b>请求失败</b></p>");
                    $("#tips").modal("show");
                    return false;
                }
            }).error(function (event) {
                $(".modal-body.tips").html("<p class='text-danger'><b>服务器异常</b></p>");
                $("#tips").modal("show");
                return false;
            });
        }
    }
    function prepareUpdateEnum() {
        var item = $("input[name='item']:checked");
        var length = item.length;
        if (length == 1) {
            updateEnum($(item[0]).val());
        } else {
            $(".modal-body.tips").html("<p class='text-warning'><b>请选中一条记录</b></p>");
            $("#tips").modal("show");
            return false;
        }
    }
    function updateEnum(id) {
        $.ajax({
            method: "get",
            url: path + "/setting/enum/detail",
            dataType: "json",
            data: {
                id: id
            }
        }).success(function (event) {
            $("#updateId").val(event.id);
            $("#enumTableUpdate").val(event.enumTable);
            $("#enumColumnUpdate").val(event.enumColumn);
            $("#enumKeyUpdate").val(event.enumKey);
            $("#enumValueUpdate").val(event.enumValue);
            $('#updateDiv').modal("show");
            return false;
        }).error(function (event) {
            $(".modal-body.tips").html("<p class='text-danger'><b>服务器异常</b></p>");
            $("#tips").modal("show");
            return false;
        });
    }
    function doUpdateEnum() {
        var enumTableUpdate = $.trim($("#enumTableUpdate").val());
        var enumColumnUpdate = $.trim($("#enumColumnUpdate").val());
        var enumKeyUpdate = $.trim($("#enumKeyUpdate").val());
        var enumValueUpdate = $.trim($("#enumValueUpdate").val());
        if (enumTableUpdate && enumColumnUpdate && enumKeyUpdate && enumValueUpdate) {
            $.ajax({
                method: "put",
                url: path + "/setting/enum",
                dataType: "json",
                data: {
                    id: $("#updateId").val(),
                    enumTable: enumTableUpdate,
                    enumColumn: enumColumnUpdate,
                    enumKey: enumKeyUpdate,
                    enumValue: enumValueUpdate
                }
            }).success(function (event) {
                if (event.code == 200) {
                    $('#updateDiv').modal("hide");
                    $(".modal-body.tips").html("<p class='text-success'><b>修改成功</b></p>");
                    $("#tips").modal("show").on('hide.bs.modal', function (e) {
                        window.location.reload(true);
                        return false;
                    });
                } else {
                    $(".modal-body.tips").html("<p class='text-success'><b>请求失败</b></p>");
                    $("#tips").modal("show");
                    return false;
                }
            }).error(function (event) {
                $('#updateDiv').modal("hide");
                $(".modal-body.tips").html("<p class='text-danger'><b>服务器异常</b></p>");
                $("#tips").modal("show");
                return false;
            });
        } else {
            $(".modal-body.tips").html("<p class='text-danger'><b>参数不能为空</b></p>");
            $("#tips").modal("show");
            return false;
        }
    }
    function doAddEnum() {
        var enumTableNew = $.trim($("#enumTableNew").val());
        var enumColumnNew = $.trim($("#enumColumnNew").val());
        var enumKeyNew = $.trim($("#enumKeyNew").val());
        var enumValueNew = $.trim($("#enumValueNew").val());
        if (enumTableNew && enumColumnNew && enumKeyNew && enumValueNew) {
            $.ajax({
                method: "post",
                url: path + "/setting/enum/new",
                dataType: "json",
                data: {
                    enumTable: enumTableNew,
                    enumColumn: enumColumnNew,
                    enumKey: enumKeyNew,
                    enumValue: enumValueNew
                }
            }).success(function (event) {
                if (event.code == 200) {
                    $('#addDiv').modal("hide");
                    $(".modal-body.tips").html("<p class='text-success'><b>新增成功</b></p>");
                    $("#tips").modal("show").on('hide.bs.modal', function (e) {
                        window.location.reload(true);
                        return false;
                    });
                } else {
                    $(".modal-body.tips").html("<p class='text-success'><b>请求失败</b></p>");
                    $("#tips").modal("show");
                    return false;
                }
            }).error(function (event) {
                $('#addDiv').modal("hide");
                $(".modal-body.tips").html("<p class='text-danger'><b>服务器异常</b></p>");
                $("#tips").modal("show");
                return false;
            });
        } else {
            $(".modal-body.tips").html("<p class='text-danger'><b>参数不能为空</b></p>");
            $("#tips").modal("show");
            return false;
        }
    }
</script>