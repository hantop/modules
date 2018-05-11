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
<script type="text/javascript" src="/static/component/HoldOn/HoldOn.min.js"></script>
<script type="text/javascript" src="/static/kindeditor-4.1.10/kindeditor.js"></script>

<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">

                <li class="active"><b>网贷知多点</b></li>
            </ol>

        </div>
        <div class="col-md-12">

            <form id="dataform" class="form-inline" method="post" action="${path}/publicity/class/editKnowMore">
                <c:if test="${knowMore.id>0}"><input type="hidden" name="id" value="${knowMore.id}"></c:if>
                <input type="hidden" name="publisherId" value="${knowMore.publisherId}">
                <input  id="allPicUploade" type="hidden" name="allPicUploade"  value='${knowMore.allPicUploade}' />
		<input name="content" id="content" type="hidden"  value='${knowMore.content}'                 />
                <div>
                    <label class="col-sm-2 control-label text-right"><span style="color: red">*</span>文章标题：</label><input maxlength="40" id="title" name="title" value="${knowMore.title}" type="text" style="width: 30%"/>&nbsp;最多40个字
                </div>
                <br>
                <div>
                    <label class="col-sm-2 control-label text-right">位置顺序：</label><input maxlength="40" type="number" name="sorting" value="${knowMore.sorting}" style="width: 10%"/>
                </div>

                <br>
                <div>

                    <label class="col-sm-2 control-label text-right">发布状态：</label>
                    <select class="form-control" id="orderStatusSelectId" name="status" >

                        <option value=0 <c:if test="${knowMore.status == 0}">selected="selected"</c:if> >未发布</option>
                        <option value=1 <c:if test="${knowMore.status == 1}">selected="selected"</c:if> >已发布</option>

                    </select>
                </div>




                <br>
                <div>
                    <label class="col-sm-2 control-label text-right">原文链接：</label><textarea style="width: 60%" name="url"   rows="3">${knowMore.url}</textarea>

                </div>

                <br>
                <div>
                    <label class="col-sm-2 control-label text-right">关键词：</label><textarea  style="width: 60%" name="keyword"  rows="3" maxlength="40">${knowMore.keyword}</textarea>&nbsp;最多40个字

                </div>

                <br>
                <div>
                    <label class="col-sm-2 control-label text-right"><span style="color: red">*</span>文章摘要：</label><textarea name="digest" maxlength="200" id="digest"  style="width: 60%"  rows="3">${knowMore.digest}</textarea>&nbsp;最多200个字

                </div>


                <br>
                <div>
                    <label class="col-sm-2 control-label text-right"><span style="color: red">*</span>文章正文：</label><textarea  maxlength="200" id="kindtextarea"   rows="30" cols="120"></textarea>

                </div>

                <br>
                <div>
                    <label class="col-sm-2 control-label text-right"></label>
                    <button onclick="saveForm()" type="button" class="btn btn-primary btn-sm">确认发布</button>
                    <a type="button" class="btn btn-default btn-sm" href="${path}/publicity/class/knowmoreList?status=-1">返回</a>

                </div>

            </form>
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
            editor = K.create('#kindtextarea', {

                uploadJson : 'qiniu?fromUrl=more&knowId=${knowMore.id}',
                afterCreate : function() {
                    var self = this;
                    K.ctrl(document, 13, function() {
                        self.sync();
                        document.forms['example'].submit();
                    });

                },
                afterUpload: function (url) {
                    $("#allPicUploade").val($("#allPicUploade").val()+","+url);
                }
            });
            K.html('#kindtextarea','${knowMore.content}');
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
        if($("#digest").val()==''){
            alert("请输入文章摘要");
            return;
        }
         if(content==''){
            alert("请输入文章正文");
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
                        url : "${path}/publicity/class/editKnowMore?timestamp=" + new Date().getTime(),
                        async : true,
                        dataType :"json",
                        error : function() {
                            bootbox.alert({message: "提交超时，请重新登录！", title: '错误信息'});
                        },
                        success : function(data, textStatus, XMLHttpRequest) {
                            if(data.success){
                                bootbox.alert({message: "提交成功！", title: '操作信息',callback: function() {
                                    window.location.href="${path}/publicity/class/knowmoreList?status=-1";}});
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