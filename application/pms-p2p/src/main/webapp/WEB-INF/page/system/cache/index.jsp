<%--
  Created by IntelliJ IDEA.
  User: Lullaby
  Date: 2015/9/23
  Time: 22:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}">首页</a></li>
                <li class="active"><a>缓存管理</a></li>
                <li class="active"><b>系统缓存设置</b></li>
            </ol>
        </div>
    </div>
    <div class="col-md-3">
        <form>
            <div class="form-group">
                <label for="rKey">Key</label>
                <input type="text" class="form-control" id="rKey" placeholder="rKey">
            </div>
            <div class="form-group">
                <label for="rValue">Value</label>
                <input type="text" class="form-control" id="rValue" placeholder="rValue">
            </div>
            <button type="button" class="btn btn-success" disabled="disabled" onclick="javascript: doTest();">Submit</button>
        </form>
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
    function doTest() {
        var rKey = $("#rKey").val();
        var rValue = $("#rValue").val();
        $.ajax({
            method: "post",
            url: path + "/system/cache/test",
            dataType: "json",
            data: {
                key: rKey,
                value: rValue
            }
        }).success(function (event) {
            if (event.code == 200) {
                $(".modal-body.tips").html("<p class='text-success'><b>请求成功</b></p>");
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
</script>