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
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

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

    .modal-content {
        padding: 7px;
    }

    .modal-header-update {
        border: none;
    }

    #updateOriginalName {
        padding-left: 13px;
        color: forestgreen;
        font-weight: bold;
        overflow-wrap: break-word;
        word-wrap: break-word;
        word-break: break-all;
    }

    .table {
        table-layout: fixed;
    }

    .table td {
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }
</style>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}">首页</a></li>
                <li class="active"><a>客户端设置</a></li>
                <li class="active"><b>启动图设置</b></li>
            </ol>
            <form id="dataform" class="form-inline" method="post" action="${path}/client/startup">
                <div class="form-group">
                    <label for="originalName">源文件名称</label>
                    <input type="text" class="form-control input-sm" id="originalName" name="originalName" value="${image.originalName}">
                </div>
                <shiro:hasPermission name="clientStartup:search">
                    <button id="searchBtn" type="button" class="btn btn-primary btn-sm">查询</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="clientStartup:upload">
                    <button type="button" class="btn btn-success btn-sm" onclick="javascript:uploadImage();">上传</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="clientStartup:edit">
                    <button type="button" class="btn btn-warning btn-sm" onclick="javascript:updateImage();">修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="clientStartup:delete">
                    <button type="button" class="btn btn-danger btn-sm" onclick="javascript:deleteImage();">删除</button>
                </shiro:hasPermission>
                <input type="hidden" id="page" name="page">
            </form>
            <hr>
            <div class="table-responsive">
                <table id="datatable" class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th width="2%" class="text-center"><input type="checkbox" onclick="javascript:checkAll(this);"></th>
                        <th width="20%">源文件名称</th>
                        <th width="7%">文件大小</th>
                        <th width="7%">客户端类型</th>
                        <th width="10%">屏幕分辨率类型</th>
                        <th width="7%">是否启用</th>
                        <th width="10%">创建时间</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${list}" var="v">
                        <tr ondblclick="javascript: getStartupImage('${v.id}');">
                            <td class="text-center">
                                <input name="item" type="checkbox" onclick="checkItem(this);" value="${v.id}">
                            </td>
                            <td><a title="${v.originalName}" href="${v.fullPath}" class="fancybox" rel="gallery">${v.originalName}</a></td>
                            <td>${v.fileSize}</td>
                            <td>${v.clientType}</td>
                            <td>${v.screenType}</td>
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
                <h5 class="modal-title text-center text-success" id="gridSystemModalLabel"><b>上传客户端启动图</b></h5>
            </div>
            <div class="row content-row">
                <div class="col-md-6 text-center">
                    <label for="clientType">客户端类型</label>
                    <select id="clientType" class="form-control">
                        <c:forEach items="${enums}" var="v">
                            <c:if test="${v.enumColumn eq 'f_client_type'}">
                                <option value="${v.enumKey}">${v.enumValue}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-6 text-center">
                    <label for="screenType">屏幕分辨率类型</label>
                    <select id="screenType" class="form-control">
                        <c:forEach items="${enums}" var="v">
                            <c:if test="${v.enumColumn eq 'f_screen_type'}">
                                <option value="${v.enumKey}">${v.enumValue}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-12 text-center">
                    <label for="fileStatus">是否启用</label>
                    <select id="fileStatus" class="form-control">
                        <c:forEach items="${enums}" var="v">
                            <c:if test="${v.enumColumn eq 'f_status'}">
                                <option value="${v.enumKey}">${v.enumValue}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div id="uploader">
                <%--<p>Your browser doesn't have Flash, Silverlight or HTML5 support.</p>--%>
                <p>您的浏览器不支持Flash, Silverlight或HTML5</p>
            </div>
        </div>
    </div>
</div>
<div id="updateDiv" data-backdrop="static" class="modal fade" role="dialog" aria-labelledby="gridSystemModalLabel">
    <div class="modal-dialog plupload modal-sm" role="document">
        <div class="modal-content modal-content-update">
            <div class="modal-header modal-header-update">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title text-center text-warning"><b>修改启动图</b></h5>
            </div>
            <form id="updateForm">
                <input type="hidden" id="updateId">
                <div class="form-group">
                    <label for="updateOriginalName">源文件名称</label>
                    <div id="updateOriginalName"></div>
                </div>
                <div class="form-group">
                    <label for="updateClientType">客户端类型</label>
                    <select id="updateClientType" class="form-control">
                        <c:forEach items="${enums}" var="v">
                            <c:if test="${v.enumColumn eq 'f_client_type'}">
                                <option value="${v.enumKey}">${v.enumValue}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="updateScreenType">屏幕分辨率类型</label>
                    <select id="updateScreenType" class="form-control">
                        <c:forEach items="${enums}" var="v">
                            <c:if test="${v.enumColumn eq 'f_screen_type'}">
                                <option value="${v.enumKey}">${v.enumValue}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="updateFileStatus">是否启用</label>
                    <select id="updateFileStatus" class="form-control">
                        <c:forEach items="${enums}" var="v">
                            <c:if test="${v.enumColumn eq 'f_status'}">
                                <option value="${v.enumKey}">${v.enumValue}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group text-center">
                    <button type="button" class="btn btn-success" onclick="javascript: doUpdateStartupImage();">确定</button>
                    <button type="button" class="btn btn-warning" onclick="javascript: $('#updateDiv').modal('hide');">取消</button>
                </div>
            </form>
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
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
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
            url: $("#path").val() + "/client/startup/delete/qiniu",
            dataType: "json",
            data: {
                ids: ids.substring(0, ids.length - 1)
            }
        }).success(function(event) {
            if (event.code == 200) {
                $(".modal-body.tips").html("<p class='text-success'><b>删除成功</b></p>");
                $("#tips").modal("show").on('hide.bs.modal', function (e) {
                    window.location.reload(true);
                });;
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
                    uploader.settings.multipart_params = {
                        clientType: $("#clientType").val(),
                        screenType: $("#screenType").val(),
                        fileStatus: $("#fileStatus").val()
                    }
                }
            },
            runtimes: 'html5,flash,silverlight,html4',
            url: $("#path").val() + '/client/startup/upload/qiniu',
            max_file_size: '5mb',
            max_file_count: 10,
//            chunk_size: '1mb',
//            resize : {
//                width : 900,
//                height : 200,
//                quality : 90,
//                crop: true
//            },
            filters: [
                {title: "Image files", extensions: "jpg,jpeg,gif,png,bmp"},
//                {title : "Zip files", extensions : "zip,avi"}
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
    function updateImage() {
        var item = $("input[name='item']:checked");
        var length = item.length;
        if (length == 1) {
            getStartupImage($(item[0]).val());
        } else {
            $(".modal-body.tips").html("<p class='text-warning'><b>请选中一条记录</b></p>");
            $("#tips").modal("show");
            return false;
        }
    }
    function getStartupImage(id) {
        $.ajax({
            method: "get",
            url: path + "/client/startup/item/" + id,
            dataType: "json"
        }).success(function (result) {
            $("#updateId").val(result.id);
            $('#updateOriginalName').text(result.originalName);
            $("#updateClientType").val(result.clientType);
            $("#updateScreenType").val(result.screenType);
            $("#updateFileStatus").val(result.status);
            $('#updateDiv').modal("show");
        }).error(function (event) {
            $(".modal-body.tips").html("<p class='text-danger'><b>服务器异常</b></p>");
            $("#tips").modal("show");
            return false;
        });
    }
    function doUpdateStartupImage() {
        var updateId = $("#updateId").val();
        var updateClientType = $("#updateClientType").val();
        var updateScreenType = $("#updateScreenType").val();
        var updateFileStatus = $("#updateFileStatus").val();
        $.ajax({
            method: "put",
            url: path + "/client/startup/update",
            dataType: "json",
            data: {
                id: updateId,
                clientType: updateClientType,
                screenType: updateScreenType,
                status: updateFileStatus
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
</script>