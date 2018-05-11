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
<link rel="stylesheet" type="text/css" href="/static/component/fancybox/source/jquery.fancybox.css">
<link rel="stylesheet" type="text/css" href="/static/component/plupload/js/jquery.ui.plupload/css/jquery.ui.plupload.css">
<link rel="stylesheet" type="text/css" href="/static/component/jqueryui/jquery-ui.min.css">
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
                <li class="active"><a>客户端设置</a></li>
                <li class="active"><b>广告图设置</b></li>
            </ol>
            <div>
                <form id="dataform" class="form-inline" method="post" action="${path}/client/advert">
                    <div class="form-group">
                        <label for="originalName">源文件名称</label>
                        <input type="text" class="form-control input-sm" id="originalName" name="originalName" value="${image.originalName}">
                    </div>
                    <shiro:hasPermission name="clientAdvert:search">
                        <button id="searchBtn" type="button" class="btn btn-primary btn-sm">查询</button>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="clientAdvert:upload">
                        <a class="btn btn-success btn-sm" href="javascript:uploadImage();">上传</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="clientAdvert:edit">
                        <a class="btn btn-warning btn-sm" href="javascript:updateImage();">修改</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="clientAdvert:delete">
                        <button type="button" class="btn btn-danger btn-sm" onclick="javascript:deleteImage();">删除</button>
                    </shiro:hasPermission>
                    <input type="hidden" id="page" name="page">
                </form>
            </div>
            <hr>
            <div class="table-responsive">
                <table id="datatable" class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th width="3%" class="text-center"><input type="checkbox" onclick="javascript:checkAll(this);"></th>
                        <th width="20%">源文件名称</th>
                        <th width="6%">文件大小</th>
                        <th width="24%">广告链接</th>
                        <th width="7%">客户端类型</th>
                        <th width="12%">屏幕分辨率</th>
                        <th width="7%">客户端版本</th>
                        <th width="6%">优先级</th>
                        <th width="6%">广告类型</th>
                        <th width="6%">是否启用</th>
                        <th width="12%">创建时间</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${list}" var="v">
                        <tr ondblclick="javascript: getAdvertImage(${v.id});">
                            <td class="text-center">
                                <input name="item" type="checkbox" onclick="checkItem(this);" value="${v.id}">
                            </td>
                            <td><a title="${v.originalName}" href="${v.fullPath}" class="fancybox" rel="gallery">${v.originalName}</a></td>
                            <td>${v.fileSize}</td>
                            <td><a target="_blank" href="${v.responseLink}" title="${v.responseLink}">${v.responseLink}</a></td>
                            <td>${v.clientType}</td>
                            <td>${v.screenType}</td>
                            <td>${v.clientVersion}</td>
                            <td>${v.priority}</td>
                            <td>${v.advertType}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${v.status == '1'}">
                                        <span style="color: green;font-weight: bold;">${v.statusDesc}</span>
                                    </c:when>
                                    <c:when test="${v.status == '2'}">
                                        <span style="color: red;font-weight: bold;">${v.statusDesc}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span style="color: #FFA400;font-weight: bold;">${v.statusDesc}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${v.createTime}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <tiles:insertDefinition name="paginator"/>
        </div>
    </div>
</div>
<div id="uploaderDiv" data-backdrop="static" class="modal fade" role="dialog" aria-labelledby="gridSystemModalLabel">
    <div class="modal-dialog plupload" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h5 class="modal-title text-center text-success" id="gridSystemModalLabel"><b>上传客户端广告图片</b></h5>
            </div>
            <div class="row content-row">
                <div class="col-md-6 text-center">
                    <label for="clientType">客户端类型</label>
                    <select id="clientType" class="form-control">
                        <c:forEach items="${enums}" var="v">
                            <c:if test="${v.enumColumn eq 'client_type'}">
                                <option value="${v.enumKey}">${v.enumValue}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-6 text-center">
                    <label for="screenType">屏幕分辨率</label>
                    <select id="screenType" class="form-control">
                        <c:forEach items="${enums}" var="v">
                            <c:if test="${v.enumColumn eq 'screen_type'}">
                                <option value="${v.enumKey}">${v.enumValue}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-6 text-center">
                    <label for="clientVersion">客户端版本</label>
                    <select id="clientVersion" class="form-control">
                        <c:forEach items="${enums}" var="v">
                            <c:if test="${v.enumColumn eq 'client_version'}">
                                <option value="${v.enumKey}">${v.enumValue}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-6 text-center">
                    <label for="fileStatus">是否启用</label>
                    <select id="fileStatus" class="form-control">
                        <c:forEach items="${enums}" var="v">
                            <c:if test="${v.enumColumn eq 'status'}">
                                <option value="${v.enumKey}">${v.enumValue}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-6 text-center">
                    <label for="priority">优先级</label>
                    <input id="priority" type="number" class="form-control">
                </div>
                <div class="col-md-6 text-center">
                    <label for="responseLink">广告链接</label>
                    <input id="responseLink" type="text" class="form-control" maxlength="1024">
                </div>
                <div class="col-md-6 text-center">
                    <label for="advertType">广告类型</label>
                    <select id="advertType" class="form-control">
                        <c:forEach items="${enums}" var="v">
                            <c:if test="${v.enumColumn eq 'advert_type'}">
                                <option value="${v.enumKey}">${v.enumValue}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div id="uploader">
                <p>您的浏览器不支持Flash, Silverlight或HTML5</p>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="updateDiv" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog plupload" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title text-warning" id="myModalLabel"><b>修改广告图</b></h5>
            </div>
            <div class="modal-body">
                <form id="updateForm">
                    <input type="hidden" id="updateId">
                    <div class="row content-row">
                        <div class="col-md-12">
                            <div class="form-group">
                                <label for="updateOriginalName">源文件名称</label>
                                <div id="updateOriginalName" class="updateOriginalName"></div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="updateClientType">客户端类型</label>
                                <select id="updateClientType" class="form-control">
                                    <c:forEach items="${enums}" var="v">
                                        <c:if test="${v.enumColumn eq 'client_type'}">
                                            <option value="${v.enumKey}">${v.enumValue}</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="updateScreenType">屏幕分辨率</label>
                                <select id="updateScreenType" class="form-control">
                                    <c:forEach items="${enums}" var="v">
                                        <c:if test="${v.enumColumn eq 'screen_type'}">
                                            <option value="${v.enumKey}">${v.enumValue}</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="updateClientVersion">客户端版本</label>
                                <select id="updateClientVersion" class="form-control">
                                    <c:forEach items="${enums}" var="v">
                                        <c:if test="${v.enumColumn eq 'client_version'}">
                                            <option value="${v.enumKey}">${v.enumValue}</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="updateStatus">是否启用</label>
                                <select id="updateStatus" class="form-control">
                                    <c:forEach items="${enums}" var="v">
                                        <c:if test="${v.enumColumn eq 'status'}">
                                            <option value="${v.enumKey}">${v.enumValue}</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="updatePriority">优先级</label>
                                <input id="updatePriority" type="number" class="form-control">
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="updateResponseLink">广告链接</label>
                                <input id="updateResponseLink" type="text" class="form-control" maxlength="1024">
                            </div>
                        </div>
                        <div class="col-md-6 text-center">
                            <label for="updateAdvertType">广告类型</label>
                            <select id="updateAdvertType" class="form-control">
                                <c:forEach items="${enums}" var="v">
                                    <c:if test="${v.enumColumn eq 'advert_type'}">
                                        <option value="${v.enumKey}">${v.enumValue}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success" onclick="javascript: doUpdateAdvertImage();">确定</button>
                <button type="button" class="btn btn-warning" data-dismiss="modal">取消</button>
            </div>
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
                <button type="button" class="btn btn-primary" onclick="javascript: doDeleteImage();">确定</button>
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
<script type="text/javascript" src="/static/component/fancybox/source/jquery.fancybox.pack.js"></script>
<script type="text/javascript" src="/static/component/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="/static/component/plupload/js/plupload.full.min.js"></script>
<script type="text/javascript" src="/static/component/plupload/js/jquery.ui.plupload/jquery.ui.plupload.min.js"></script>
<script type="text/javascript" src="/static/component/plupload/js/i18n/zh_CN.js"></script>
<script type="text/javascript">
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
    function updateImage() {
        var item = $("input[name='item']:checked");
        var length = item.length;
        if (length == 1) {
            getAdvertImage($(item[0]).val());
        } else {
            $(".modal-body.tips").html("<p class='text-warning'><b>请选中一条记录</b></p>");
            $("#tips").modal("show");
            return false;
        }
    }
    function getAdvertImage(id) {
        $.ajax({
            method: "get",
            url: path + "/client/advert/item/" + id,
            dataType: "json"
        }).success(function (result) {
            $("#updateId").val(result.id);
            $('#updateOriginalName').text(result.originalName);
            $("#updateClientType").val(result.clientType);
            $("#updateScreenType").val(result.screenType);
            $("#updateClientVersion").val(result.clientVersion);
            $("#updateStatus").val(result.status);
            $("#updatePriority").val(result.priority);
            $("#updateResponseLink").val(result.responseLink);
            $("#updateAdvertType").val(result.advertType);
            $('#updateDiv').modal("show");
        }).error(function (event) {
            $(".modal-body.tips").html("<p class='text-danger'><b>服务器异常</b></p>");
            $("#tips").modal("show");
            return false;
        });
    }
    function doUpdateAdvertImage() {
        var updatePriority = $("#updatePriority").val();
        var updateResponseLink = $.trim($("#updateResponseLink").val());
        if (!updatePriority || !updateResponseLink) {
            $(".modal-body.tips").html("<p class='text-warning'><b>优先级和广告链接不能为空</b></p>");
            $("#tips").modal("show");
            return false;
        }
        if (updatePriority < -128 || updatePriority > 127) {
            $(".modal-body.tips").html("<p class='text-warning'><b>优先级在[-128, 127]之间</b></p>");
            $("#tips").modal("show");
            return false;
        }
        var updateId = $("#updateId").val();
        var updateClientType = $("#updateClientType").val();
        var updateScreenType = $("#updateScreenType").val();
        var updateClientVersion = $("#updateClientVersion").val();
        var updateStatus = $("#updateStatus").val();
        var updateAdvertType = $("#updateAdvertType").val();
        $.ajax({
            method: "put",
            url: path + "/client/advert/update",
            dataType: "json",
            data: {
                id: updateId,
                clientType: updateClientType,
                screenType: updateScreenType,
                clientVersion: updateClientVersion,
                status: updateStatus,
                priority: updatePriority,
                responseLink: updateResponseLink,
                advertType: updateAdvertType
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
                $(".modal-body.tips").html("<p class='text-danger'><b>请求失败</b></p>");
                $("#tips").modal("show");
                return false;
            }
        }).error(function (event) {
            $('#updateDiv').modal("hide");
            $(".modal-body.tips").html("<p class='text-danger'><b>服务器异常</b></p>");
            $("#tips").modal("show");
            return false;
        });
    }
    function deleteImage() {
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
    function doDeleteImage() {
        $("#confirm").modal("hide");
        var items = $("input[name='item']:checked");
        var length = items.length;
        var ids = "";
        for (var i = 0; i < length; i++) {
            ids += $(items[i]).val() + ",";
        }
        $.ajax({
            method: "post",
            url: $("#path").val() + "/client/advert/delete/qiniu",
            dataType: "json",
            data: {
                ids: ids.substring(0, ids.length - 1)
            }
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
    $(document).ready(function () {
        $(".fancybox").fancybox({
            closeBtn: false,
            nextClick: true,
            loop: false,
            openEffect: "elastic",
            closeEffect: "elastic",
            nextEffect: "elastic",
            prevEffect: "elastic"
        });
    });
    function uploadImage() {
        $('#uploaderDiv').modal("show").on('hide.bs.modal', function (e) {
            window.location.reload(true);
        });
    }
    $(function () {
        $("#uploader").plupload({
            init: {
                BeforeUpload: function (uploader, file) {
                    var priority = $.trim($("#priority").val());
                    var responseLink = $.trim($("#responseLink").val());
                    if (!(priority && responseLink)) {
                        $(".modal-body.tips").html("<p class='text-warning'><b>优先级和广告链接不能为空</b></p>");
                        $("#tips").modal("show");
                        return false;
                    }
                    if (priority < -128 || priority > 127) {
                        $(".modal-body.tips").html("<p class='text-warning'><b>优先级在[-128, 127]之间</b></p>");
                        $("#tips").modal("show");
                        return false;
                    }
                    uploader.settings.multipart_params = {
                        clientType: $("#clientType").val(),
                        screenType: $("#screenType").val(),
                        clientVersion: $("#clientVersion").val(),
                        fileStatus: $("#fileStatus").val(),
                        priority: priority,
                        responseLink: responseLink,
                        advertType: $("#advertType").val()
                    }
                }
            },
            runtimes: 'html5,flash,silverlight,html4',
            url: $("#path").val() + '/client/advert/upload/qiniu',
            max_file_size: '3mb',
            max_file_count: 10,
            filters: [
                {title: "Image files", extensions: "jpg,jpeg,gif,png,bmp"},
            ],
            rename: true,
            sortable: true,
            dragdrop: true,
            views: {
                list: true,
                thumbs: true,
                active: 'thumbs'
            },
            flash_swf_url: '../../js/Moxie.swf',
            silverlight_xap_url: '../../js/Moxie.xap'
        });
    });
</script>