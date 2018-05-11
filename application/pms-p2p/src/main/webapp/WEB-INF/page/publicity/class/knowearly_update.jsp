<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags" %>
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
<script type="text/javascript" src="/static/kindeditor-4.1.10/kindeditor.js"></script>

<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><b>风险早知道</b></li>
            </ol>

        </div>
        <div class="col-md-12">

            <form id="dataform" class="form-inline" method="post" action="${path}/publicity/class/editKnowEarly">
                <c:if test="${knowEarly.id>0}"><input type="hidden" name="id" value="${knowEarly.id}"></c:if>
                <input type="hidden" name="publisherId" value="${knowEarly.publisherId}">
                <input name="picNewName" id="hidden_relativePath_1" type="hidden"  value="${knowEarly.picNewName}"                 />
                <input name="picOldName" id="picOldName" type="hidden"  value="${knowEarly.picOldName}"                 />
                <input name="content" id="content" type="hidden"  value='${knowEarly.content}'                />

                <input  id="tempDeletePic" type="hidden"  value='' />

                <input  id="allPicUploade" type="hidden" name="allPicUploade"  value='${knowEarly.allPicUploade}' />

                <div>
                    <label class="col-sm-2 control-label text-right"><span style="color: red">*</span>文章标题：</label><input maxlength="40" id="title" name="title" value="${knowEarly.title}" type="text" style="width: 30%"/>&nbsp;最多40个字
                </div>

                <br>
                <div>
                    <label class="col-sm-2 control-label text-right"><span style="color: red">*</span>文章来源：</label><input maxlength="40" id="source" name="source" value="${knowEarly.source}" type="text" style="width: 30%"/>&nbsp;最多40个字
                </div>

                <br>
                <div>
                    <label class="col-sm-2 control-label text-right">位置顺序：</label><input type="number" name="sorting" value="${knowEarly.sorting}" style="width: 10%"/>
                </div>

                <br>



                <div>

                    <label class="col-sm-2 control-label text-right">发布状态：</label>
                    <select class="form-control" id="orderStatusSelectId" name="status" >

                        <option value=0 <c:if test="${knowEarly.status == 0}">selected="selected"</c:if> >未发布</option>
                        <option value=1 <c:if test="${knowEarly.status == 1}">selected="selected"</c:if> >已发布</option>

                    </select>
                </div>


                <br>
                <div>
                    <label class="col-sm-2 control-label text-right">原文链接：</label><textarea style="width: 60%" name="url"   rows="3">${knowEarly.url}</textarea>

                </div>

                <br>
                <div>
                    <label class="col-sm-2 control-label text-right"><span style="color: red">*</span>列表页图：</label>
                    <a id="imgFile_1" href="${knowEarly.picServer}${knowEarly.picNewName}">
                        ${knowEarly.picOldName}
                    </a>
                    <a class="btn btn-success btn-sm" href="javascript:uploadImage();">上传</a>
                    <a id="a_delete_1" class="btn btn-danger btn-sm" href="javascript:deleteImage(1)">删除</a>

                    <span style="color: red">（建议尺寸：236×140）</span>
                </div>


                <br>
                <div>
                    <label class="col-sm-2 control-label text-right">关键词：</label><textarea style="width: 60%" name="keyword"  rows="3" maxlength="40">${knowEarly.keyword}</textarea>&nbsp;最多40个字

                </div>

                <br>
                <div>
                    <label class="col-sm-2 control-label text-right">文章摘要：</label><textarea name="digest" maxlength="200" id="digest"  style="width: 60%"  rows="3">${knowEarly.digest}</textarea>&nbsp;最多200个字

                </div>


                <br>
                <div>
                    <label class="col-sm-2 control-label text-right"><span style="color: red">*</span>文章正文：</label><textarea   maxlength="200" id="kindtextarea"   rows="30" cols="120"></textarea>

                </div>

                <br>
                <div>
                    <label class="col-sm-2 control-label text-right"></label>
                    <button onclick="saveForm()" type="button" class="btn btn-primary btn-sm">确认发布</button>
                    <a type="button" class="btn btn-default btn-sm" href="${path}/publicity/class/riskKnowEarlyList?status=-1">返回</a>

                </div>

            </form>
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
                <h5 class="modal-title text-center text-success" id="gridSystemModalLabel"><b>上传列表页图</b></h5>
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


<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script type="text/javascript">


    $(function () {
        if(${knowEarly.id<0}){
            $("#a_delete_1").hide();
        }
        var editor;
        KindEditor.ready(function(K) {
            editor = K.create('#kindtextarea',{
                uploadJson : 'qiniu?fromUrl=early&knowId=${knowEarly.id}',
                afterCreate : function() {

                    var self = this;

                    K.ctrl(document, 13, function() {

                        console.log(99999);

                        self.sync();
                        document.forms['example'].submit();
                    });

                },
                afterUpload: function (url) {
                    $("#allPicUploade").val($("#allPicUploade").val()+","+url);
                }

            });
            K.html('#kindtextarea','${knowEarly.content}');
        });



    });
    function saveForm(){

        var content = $(document.getElementsByTagName('iframe')[0].contentWindow.document.body).html();
        console.log(content);

        $("#content").val(content);
        if($("#title").val()==''){
            alert("请输入标题");
            return;
        }

        if($("#source").val()==''){
            alert("请输入文章来源");
            return;
        }

//        if($("#digest").val()==''){
//            alert("请输入文章摘要");
//            return;
//        }

        if(content==''){
            alert("请输入文章正文");
            return;
        }

        if($("#hidden_relativePath_1").val()==''){
            alert("请上传列表页图");
            return;
        }

        bootbox.confirm({
            title: '提示',
            size: 'small',
            message: "<span style='font-size: 13px;font-style: inherit'>确定提交？</span>",
            callback: function (result) {

                if (result) {
                    $.ajax({
                        cache : false,
                        type : "POST",
                        data : $('#dataform').serialize(),
                        url : "${path}/publicity/class/editKnowEarly?timestamp=" + new Date().getTime(),
                        async : true,
                        dataType :"json",
                        error : function() {
                            bootbox.alert({message: "提交超时，请重新登录！", title: '错误信息'});
                        },
                        success : function(data, textStatus, XMLHttpRequest) {
                            if(data.success){

                                deleteImageQiniu($("#tempDeletePic").val());// 删除七牛存储的文件
                                bootbox.alert({message: "提交成功！", title: '操作信息',callback: function() {
                                    window.location.href="${path}/publicity/class/riskKnowEarlyList?status=-1";}});
                            }else{
                                bootbox.alert({message: data.errorMsg, title: '错误信息'});
                            }

                        }
                    });
                }
            }
        });
    }




</script>



<script type="text/javascript">
    //图片相关的js

    // 当前分辨率类型ID
    var currentScreenTypeId=1;


    //打开上传对话框
    function uploadImage() {
        $('#uploaderDiv').modal("show").on('hide.bs.modal', function (e) {
        });
    }

    //删除图片按钮
    function deleteImage(id) {

        var hiddenOriginalFilenames = $("#hidden_originalFilename_" + id);
        var hiddenRelativePaths = $("#hidden_relativePath_" + id);
        var aImgFile = $("#imgFile_" + id);
        var aDelete = $("#a_delete_" + id);
        var relativePath = $("#hidden_relativePath_" + id).val();
        hiddenOriginalFilenames.val(null);
        hiddenRelativePaths.val(null);


        aImgFile.hide();
        aImgFile.attr("href", null);
        aImgFile.html(null);
        aDelete.hide();
        $("#tempDeletePic").val($("#tempDeletePic").val()+","+relativePath);//存放临时删除的文件名
    }

    /**
     * 删除七牛存储的文件
     * @param value
     */
    function deleteImageQiniu(value) {
        // ajax请求删除
        $.ajax({
            method: "get",
            url: path + "/publicity/know/class/delete/qiniu/",
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
                    screenType: 1
                }
            },
            FileUploaded : function (uploader, file, response) {
                var response = JSON.parse(response.response);
                if (response.code == "200") {
                    var data = response.data;
                    var hiddenOriginalFilenames = $("#hidden_originalFilename_1" );
                    var hiddenRelativePaths = $("#hidden_relativePath_1" );

                    if(hiddenRelativePaths.val()!=''){//重复上传，删除前面一次传的
                        console.log("重复上传，删除"+hiddenRelativePaths.val());

                        $("#tempDeletePic").val($("#tempDeletePic").val()+","+hiddenRelativePaths.val());//存放临时删除的文件名
                    }


                    var aImgFile = $("#imgFile_1" );
                    var aDelete = $("#a_delete_1" );

                    hiddenOriginalFilenames.val(data.originalFilename);
                    hiddenRelativePaths.val(data.relativePath);

                    $("#picOldName").val(data.originalFilename);
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
        url: $("#path").val() + '/publicity/class/uploadListPic',
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




</script>