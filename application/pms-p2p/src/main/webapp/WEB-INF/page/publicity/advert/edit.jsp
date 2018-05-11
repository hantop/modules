<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<link rel="stylesheet" href="/static/component/HoldOn/HoldOn.min.css">
<link rel="stylesheet" href="/static/component/zTree_v3/css/zTreeStyle/zTreeStyle.css" type="text/css">
<link rel="stylesheet" href="/static/component/bootstrap/css/bootstrap.min.css" type="text/css">
<link rel="stylesheet" type="text/css" href="/static/component/plupload/js/jquery.ui.plupload/css/jquery.ui.plupload.css">
<link rel="stylesheet" type="text/css" href="/static/component/jqueryui/jquery-ui.min.css">
<link rel="stylesheet" type="text/css" href="/static/component/fancybox/source/jquery.fancybox.css">


<script type="text/javascript" src="/static/component/jqueryui/jquery-ui.min.js"></script>
<script type="text/javascript" src="/static/component/plupload/js/plupload.full.min.js"></script>
<script type="text/javascript" src="/static/component/plupload/js/jquery.ui.plupload/jquery.ui.plupload.min.js"></script>
<script type="text/javascript" src="/static/component/plupload/js/i18n/zh_CN.js"></script>
<script type="text/javascript" src="/static/component/fancybox/source/jquery.fancybox.pack.js"></script>

<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.exedit-3.5.js"></script>
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<jsp:include page="/WEB-INF/page/inc/jquery-validation.jsp"/>
<script type="text/javascript" src="/static/component/HoldOn/HoldOn.min.js"></script>
<style>
    ul.ztree {margin-top: 10px;border: 1px solid #617775;background: #f0f6e4;width:220px;height:200px;overflow-y:scroll;overflow-x:auto;}
    .modal-dialog.plupload {
        margin: 10% auto !important;
    }

    .plupload_view_list .plupload_file {
        width: 99.8% !important;
    }

    #uploader_dropbox .plupload_filelist_content {
        width: 99.8% !important;
    }
</style>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/publicity/index">宣传管理</a></li>
                <li class="active"><a href="${path}/publicity/advert">广告图管理</a></li>
                <li class="active"><b>广告图${isNew ? "新增" : "编辑"}</b></li>
            </ol>
            <span class="badge" style="font-size: 18px;">${title}</span>
            <form:form id="dataform" role="form" class="form-horizontal" modelAttribute="advertImage">
                <form:hidden path="id" />
                <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                        名称：
                    </label>
                    <div class="col-sm-6 help">
                        <form:input path="name" cssClass="form-control input-sm" class="{required:true, minlength:2}"/>
                    </div>

                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                        优先级：
                    </label>
                    <div class="col-sm-6">
                        <form:input path="priority" cssClass="form-control input-sm"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                        广告类型：
                    </label>
                    <div class="col-sm-6">
                        <form:select path="advertType" cssClass="form-control input-sm">
                            <form:options items="${advertTypes}" itemLabel="enumValue" itemValue="enumKey"></form:options>
                        </form:select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                        广告链接：
                    </label>
                    <div class="col-sm-6">
                        <form:input path="adWebUrl" cssClass="form-control input-sm"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                        客户端版本：
                    </label>
                    <div class="col-sm-6">
                        <form:checkboxes path="versions" items="${versions}" itemLabel="enumValue" itemValue="enumKey"></form:checkboxes>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                        是否需要登陆：
                    </label>
                    <div class="col-sm-6">
                        <form:select path="needLogin" cssClass="form-control input-sm">
                            <form:option value="0">否</form:option>
                            <form:option value="1">是</form:option>
                        </form:select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                        可用性：
                    </label>
                    <div class="col-sm-6">
                        <form:select path="status" cssClass="form-control input-sm">
                            <form:option value="0">禁用</form:option>
                            <form:option value="1">启用</form:option>
                        </form:select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                        客户端类型：
                    </label>
                    <div class="col-sm-6">
                        <form:checkboxes path="clientTypes" items="${clientTypes}" itemLabel="name" itemValue="code"></form:checkboxes>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                        系统类型：
                    </label>
                    <div class="col-sm-6">
                        <form:radiobuttons path="systemType" items="${systemTypeEnums}" itemLabel="label" itemValue="value"></form:radiobuttons>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label text-right">
                        广告图设置：
                    </label>
                </div>

                <c:forEach items="${screenTypes}" var="screenType" varStatus="vs">
                    <c:set var="screenTypeId" value="${screenType.id}"/>
                    <c:set var="index" value="${vs.index}"/>
                    <div class='form-group'>
                        <label class='col-sm-2 control-label text-right'>
                            <c:if test="${index == 0}">
                                <font style="color: red;">
                                    *
                                </font>
                            </c:if>
                            ${screenType.name}
                        </label>
                        <c:choose>
                            <c:when test="${advertImage.adImgFileMap != null && advertImage.adImgFileMap.get(screenTypeId) != null}">
                                <a id="imgFile_${screenTypeId}" href="${advertImage.adImgFileMap.get(screenTypeId).fullPath}"
                                   class="fancybox col-sm-2 control-label text-right" rel="gallery">
                                        ${advertImage.adImgFileMap.get(screenTypeId).originalName}
                                </a>
                                <input id="hidden_publishName_${screenTypeId}" type="hidden" value="${advertImage.adImgFileMap.get(screenTypeId).publishName}"/>
                                <input id="hidden_originalFilename_${screenTypeId}" type="hidden" value="${advertImage.adImgFileMap.get(screenTypeId).originalName}"/>
                                <input name="hidden_relativePath_${screenTypeId}" id="hidden_relativePath_${screenTypeId}" type="hidden"
                                       value="${advertImage.adImgFileMap.get(screenTypeId).relativePath}"/>
                                <input id="hidden_fileSize_${screenTypeId}" type="hidden" value="${advertImage.adImgFileMap.get(screenTypeId).fileSize}"/>
                                <input id="hidden_fileType_${screenTypeId}" type="hidden" value="${advertImage.adImgFileMap.get(screenTypeId).fileType}"/>
                                <input id="hidden_screenType_${screenTypeId}" type="hidden" value="${screenTypeId}"/>
                                <div class="col-sm-6">
                                    <a class="btn btn-success btn-sm" href="javascript:uploadImage(${screenTypeId});">上传</a>
                                    <a id="a_delete_${screenTypeId}" class="btn btn-danger btn-sm" href="javascript:deleteImage(${screenTypeId})">删除</a>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <a id="imgFile_${screenTypeId}" style="display: none" class="fancybox col-sm-2 control-label text-right" rel="gallery"></a>
                                <input id="hidden_publishName_${screenTypeId}" type="hidden"/>
                                <input id="hidden_originalFilename_${screenTypeId}" type="hidden"/>
                                <input id="hidden_relativePath_${screenTypeId}" type="hidden"/>
                                <input id="hidden_fileSize_${screenTypeId}" type="hidden"/>
                                <input id="hidden_fileType_${screenTypeId}" type="hidden"/>
                                <input id="hidden_screenType_${screenTypeId}" type="hidden"/>
                                <div class="col-sm-6">
                                    <a class="btn btn-success btn-sm" href="javascript:uploadImage(${screenTypeId});">上传</a>
                                    <a id="a_delete_${screenTypeId}" style="display: none" class="btn btn-danger btn-sm" href="javascript:deleteImage(${screenTypeId})">删除</a>
                                </div>
                            </c:otherwise>
                        </c:choose>

                    </div>
                </c:forEach>

                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <shiro:hasPermission name="publicityAdvert:edit">
                            <button type="submit" class="btn btn-primary btn-sm" >提交</button>
                        </shiro:hasPermission>
                        <a type="button" class="btn btn-default btn-sm" href="${path}/publicity/advert">返回</a>
                    </div>
                </div>
            </form:form>
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
            <div id="uploader">
                <p>您的浏览器不支持Flash, Silverlight或HTML5</p>
            </div>
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
    // 当前分辨率类型ID
    var currentScreenTypeId;

    function doFormSumit() {
        var hiddenPublishNames = $("input[id^='hidden_publishName_']");
        var hiddenOriginalFilenames = $("input[id^='hidden_originalFilename_']");
        var hiddenRelativePaths = $("input[id^='hidden_relativePath_']");
        var hiddenFileSizes = $("input[id^='hidden_fileSize_']");
        var hiddenFileTypes = $("input[id^='hidden_fileType_']");
        var hiddenScreenTypes = $("input[id^='hidden_screenType_']");

        var publishNames = "";
        var originalNames = "";
        var relativePaths = "";
        var fileSizes = "";
        var fileTypes = "";
        var screenTypes = "";

        hiddenScreenTypes.each(function (index, domEle) {
            // domEle == this
            var screenType = $(domEle).val();
            if (screenType != null && screenType != '') {
                var publishName = $(hiddenPublishNames.get(index)).val();
                var originalName = $(hiddenOriginalFilenames.get(index)).val();
                var relativePath = $(hiddenRelativePaths.get(index)).val();
                var fileSize = $(hiddenFileSizes.get(index)).val();
                var fileType = $(hiddenFileTypes.get(index)).val();

                publishNames += "&publishNames=" + publishName;
                originalNames += "&originalNames=" + originalName;
                relativePaths += "&relativePaths=" + relativePath;
                fileSizes += "&fileSizes=" + fileSize;
                fileTypes += "&fileTypes=" + fileType;
                screenTypes += "&screenTypes=" + screenType;
            }
        });

        $.ajax({
            method: "post",
            url: path + "/publicity/advert/edit",
            dataType: "json",
            data : $("#dataform").serialize() + publishNames + originalNames + relativePaths + fileSizes + fileTypes + screenTypes
        }).success(function (data) {
            if (data.code == "200") {
                $(".modal-body.tips").html("<p class='text-success'><b>操作成功</b></p>");
                $("#tips").modal("show").on('hide.bs.modal', function (e) {
                    window.location.href = path + "/publicity/advert";
                });
            } else {
                $(".modal-body.tips").html("<p class='text-success'><b>" + data.message + "</b></p>");
                $("#tips").modal("show");
            }
        });
    }

    function uploadImage(screenTypeId) {
        currentScreenTypeId = screenTypeId;
        $('#uploaderDiv').modal("show").on('hide.bs.modal', function (e) {

        });
    }

    function deleteImages(relativePaths) {
        // 删除七牛存储的文件
        deleteImageQiniu(relativePaths);
    }

    function deleteImage(id) {
        var result = bootbox.confirm({
            title: '提示',
            size: 'small',
            message: "<span style='font-size: 13px;font-style: inherit'>是否删除？</span>",
            callback: function (result) {
                if (result) {
                    var hiddenScreenTypes = $("#hidden_screenType_" + id);
                    var hiddenPublishName = $("#hidden_publishName_" + id);
                    var hiddenOriginalFilenames = $("#hidden_originalFilename_" + id);
                    var hiddenRelativePaths = $("#hidden_relativePath_" + id);
                    var hiddenFileSizes = $("#hidden_fileSize_" + id);
                    var hiddenFileTypes = $("#hidden_fileType_" + id);
                    var aImgFile = $("#imgFile_" + id);
                    var aDelete = $("#a_delete_" + id);
                    var relativePath = $("#hidden_relativePath_" + id).val();
                    hiddenScreenTypes.val(null);
                    hiddenPublishName.val(null);
                    hiddenOriginalFilenames.val(null);
                    hiddenRelativePaths.val(null);
                    hiddenFileSizes.val(null);
                    hiddenFileTypes.val(null);
                    aImgFile.hide();
                    aImgFile.attr("href", null);
                    aImgFile.html(null);
                    aDelete.hide();
                    // 删除七牛存储的文件
                    deleteImageQiniu(relativePath);
                }
            }
        });
    }

    /**
     * 删除七牛存储的文件
     * @param value
     */
    function deleteImageQiniu(value) {
        // ajax请求删除
        $.ajax({
            method: "get",
            url: path + "/publicity/advert/delete/qiniu/",
            dataType: "json",
            data : {
                relativePaths : value
            }
        }).success(function (data) {

        });
    }

    $("#uploader").plupload({
        init : {
            BeforeUpload: function (uploader, file) {
                uploader.settings.multipart_params = {
                    screenType: currentScreenTypeId
                }
            },
            FileUploaded : function (uploader, file, response) {
                var response = JSON.parse(response.response);
                if (response.code == "200") {
                    var data = response.data;
                    var hiddenScreenTypes = $("#hidden_screenType_" + currentScreenTypeId);
                    var hiddenPublishName = $("#hidden_publishName_" + currentScreenTypeId);
                    var hiddenOriginalFilenames = $("#hidden_originalFilename_" + currentScreenTypeId);
                    var hiddenRelativePaths = $("#hidden_relativePath_" + currentScreenTypeId);
                    var hiddenFileSizes = $("#hidden_fileSize_" + currentScreenTypeId);
                    var hiddenFileTypes = $("#hidden_fileType_" + currentScreenTypeId);
                    var aImgFile = $("#imgFile_" + currentScreenTypeId);
                    var aDelete = $("#a_delete_" + currentScreenTypeId);
                    hiddenScreenTypes.val(currentScreenTypeId);
                    hiddenPublishName.val(data.publishName);
                    hiddenOriginalFilenames.val(data.originalFilename);
                    hiddenRelativePaths.val(data.relativePath);
                    hiddenFileSizes.val(data.fileSize);
                    hiddenFileTypes.val(data.fileType);
                    aImgFile.show();
                    aImgFile.attr("href", data.fullPath);
                    aImgFile.html(data.originalFilename);
                    // 显示删除a标签
                    aDelete.show();

                    // 提示上传完成
                    $(".modal-body.tips").html("<p class='text-success'><b>操作成功</b></p>");
                    $("#tips").modal("show").on('hide.bs.modal', function (e) {
                        $('#uploaderDiv').modal("hide");
                        // 删除上传控件上的文件
                        uploader.removeFile(file.id);
                    });
                } else {
                    // 提示上传失败
                    $(".modal-body.tips").html("<p class='text-success'><b>操作失败" + response.message + "</b></p>");
                    $("#tips").modal("show");
                }
            }
        },
        runtimes: 'html5,flash,silverlight,html4',
        url: $("#path").val() + '/publicity/advert/upload/qiniu',
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

    (function ($) {
        // form表单字段验证
        $("#dataform").validate({
            rules: {
                name : {
                    required : true,
                    maxlength : 30
                },
                adWebUrl : {
                    required : true,
                    maxlength : 1024
                },
                priority : {
                    required : true,
                    digits : true,
                    max : 100
                },
                versions : {
                    required : true
                },
                clientTypes : {
                    required : true
                }
            },
            messages: {
                name: {
                    required: "名称不能为空",
                    maxlength: $.validator.format("最多输入{0}字.")
                },
                adWebUrl: {
                    required: "广告链接不能为空",
                    maxlength: $.validator.format("最多输入{0}字.")
                },
                priority : {
                    required : "优先级必须输入正整数",
                    digits : "优先级必须输入正整数",
                    max : "优先级必须小于{0}"
                },
                versions: {
                    required: "请选择客户端版本"
                },
                clientTypes: {
                    required: "请选择客户端类型"
                }
            },
            submitHandler: function(form) {
                var val = $("#hidden_relativePath_${screenTypes.get(0).id}").val();
                if (val == null || val == '') {
                    $(".modal-body.tips").html("<p class='text-success'><b>请上传通用图片</b></p>");
                    $("#tips").modal("show");
                } else {
                    // 表单ajax提交
                    doFormSumit();
                }
            }
        });
    })(jQuery);

    $(".fancybox").fancybox({
        closeBtn: false,
        nextClick: true,
        loop: false,
        openEffect: "elastic",
        closeEffect: "elastic",
        nextEffect: "elastic",
        prevEffect: "elastic"
    })
</script>