<%--
  Created by IntelliJ IDEA.
  User: Lullaby
  Date: 2015/7/17
  Time: 16:23
  To change this template use File | Settings | File Templates.
--%>
<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<link rel="stylesheet" href="/static/component/HoldOn/HoldOn.min.css">
<jsp:include page="/WEB-INF/page/inc/jquery-validation.jsp"/>
<script type="text/javascript" src="/static/component/HoldOn/HoldOn.min.js"></script>

<link rel="stylesheet" href="/static/component/zTree_v3/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.exedit-3.5.js"></script>

<div class="container">
    <div class="row">
        <div class="col-md-12">
            <span class="badge" style="font-size: 18px;">${title}</span>
            <form id="dataform" class="form-horizontal">
                <div class="form-group has-feedback">
                    角色名称:
                    <input id="roleName" name="roleName" class="form-control input-sm" />
                </div>
                <div class="form-group has-feedback">
                    角色描述:
                    <textarea id="roleDescription" name="roleDescription" class="form-control input-sm"></textarea>
                </div>
                <div class="col-md-3">
                    <div class="panel panel-default">
                        <div class="panel-body">
                            <ul id="tree" class="ztree" style="width:230px; overflow:auto;"></ul>
                        </div>
                    </div>
                </div>
                <input type="hidden" name="id"/>

                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <button type="button" class="btn btn-primary btn-sm" onclick="doAddRole();">提交</button>
                        <a type="button" class="btn btn-default btn-sm" href="${path}/system/pmsrole">取消</a>
                    </div>
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
    var treeObj;

    var setting = {
        data: {
            keep:{
                leaf:true,//叶子节点不能有下级
                parent:true
            }
        },
        view: {
            selectedMulti: false
        },
        check: {
            enable: true
        }
    };

    function getPermitTree(){
        $.ajax({
            url: "${path}/system/pmspermit/permittree",
            type: 'post',
            dataType:"json",
            success:function(data){
                treeObj=$.fn.zTree.init($("#tree"), setting, data);
                // 展开到第三层
                var arr=treeObj.getNodesByParam('level','2',null);
                for(var i=0;i<arr.length;i++){
                    treeObj.expandNode(arr[i], true, false, false);
                }
            }
        });
    }

    $(function(){
        getPermitTree();

    });

    function doAddRole() {
        var zTree = $.fn.zTree.getZTreeObj("tree");
        var nodes = zTree.getCheckedNodes(true);
        var permitIds = new Array();
        var roleName = $.trim($("#roleName").val());
        var roleDescription = $.trim($("#roleDescription").val());
        for (var i = 0, l = nodes.length; i < l; i++) {
            permitIds[i] = nodes[i].id;
//            permitIds.push(nodes[i].id);
        }

        if (nodes.length == 0) {
            alert("请选择权限");
            return false;
        }

        if (roleName && roleDescription) {
            $.ajax({
                method: "post",
                traditional : true,
                url: path + "/system/pmsrole/new",
                dataType: "json",
                data: {
                    'roleName' : roleName,
                    'roleDescription' : roleDescription,
                    'permitIds' : permitIds
                }
            }).success(function (event) {
                if (event.code == 200) {
                    $('#addDiv').modal("hide");
                    $(".modal-body.tips").html("<p class='text-success'><b>新增成功</b></p>");
                    $("#tips").modal("show").on('hide.bs.modal', function (e) {
                        window.location.reload(true);
                        return false;
                    });
                } else if(event.code == 40000) {
                    $('#addDiv').modal("hide");
                    $(".modal-body.tips").html("<p class='text-success'><b>" + event.message + "</b></p>");
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