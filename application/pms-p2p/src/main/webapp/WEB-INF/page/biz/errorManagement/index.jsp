<%@ page import="java.util.List" %>
<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/biz/index">业务管理</a></li>
                <li class="active"><b>异常管理</b></li>
            </ol>
            <form id="dataform" class="form-inline" method="post" action="${path}/biz/errorManagement">
                <div class="row" style="margin-left: 5px">
                    <button type="button" class="btn btn-primary btn-sm" onclick="javascript:handelBatchError();">批量处理</button>
                </div>
                <div class="row" style="margin-left: 5px;margin-top:10px;">
                </div>
            </form>
            <hr style="margin-bottom: 0;">
            <div class="panel panel-default">
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                        <tr class="success">
                            <th><input type="checkbox" onclick="javascript:checkAll(this);">全选</th>
                            <th>标的编号</th>
                            <th>标的名称</th>
                            <th>错误原因</th>
                            <th>报错时间</th>
                            <th>操作</th>
						</tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${errorEntrustPayVOList}" var="v" varStatus="vs">
                        <tr>
                            <td>
                                <input name="item" type="checkbox" onclick="checkItem(this);" value="${v.loanId}">
                            </td>
                            <td>${v.bidNo}</td>
                            <td>${v.bidTitle}</td>
                            <td>${v.errorReson}</td>
                            <td>
                                <fmt:formatDate value="${v.errorTime}"
                                                pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                            <td>
                                <a href="#" onclick="handelSingleError(${v.loanId});">处理</a>&nbsp;&nbsp;&nbsp;&nbsp;
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
                <p class="text-danger"><b>确定要异常处理吗？</b></p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary" onclick="javascript: doHandelError();">确定</button>
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

    function handelBatchError() {
        var items = $("input[name='item']:checked");
        var length = items.length;
        if (length < 1) {
            $(".modal-body.tips").html("<p class='text-danger'><b>请选中要处理的选项</b></p>");
            $("#tips").modal("show");
            return false;
        } else {
            $("#confirm").modal("show");
            return false;
        }
    }

    function handelSingleError(loanId) {
        bootbox.confirm({
            title: '提示',
            size: 'small',
            message: "确定要异常处理吗？",
            callback: function (result) {
                if (result) {
                    // ajax请求异常处理
                    $.ajax({
                        method: "post",
                        url: path + "/biz/errorManagement/handelError/" + id,
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
                                message: '处理成功',
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
                                message: '处理失败'
                            });
                        }
                    });
                }
            }
        })
    }

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
            url: path + "/publicity/advert/delete/" + ids.substring(0, ids.length - 1),
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
