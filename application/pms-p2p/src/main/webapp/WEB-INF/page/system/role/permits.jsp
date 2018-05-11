<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/2/17
  Time: 14:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
    <div class="col-md-12" id="recharge_container" style="margin-top: 20px;">
        <div class="panel panel-default">
            <div class="panel-body">
                <form id="parent_form" class="form-horizontal">
                    <div class="form-group">
                        <label  class="col-sm-2 control-label">角色名称：</label>
                        <div class="col-sm-3">
                            <input id="roleName" type="text" class="form-control" placeholder="请输入角色名称">
                        </div>
                        <label class="col-sm-2 control-label">顺序：</label>
                        <div class="col-sm-2">
                            <input id="sort" type="text" class="form-control" placeholder="请输入顺序">
                        </div>
                        <shiro:hasPermission name="systemRole:commit">
                        <div class="col-sm-2">
                            <input type="hidden" id="id"/>
                            <input type="hidden" id="parentId"/>
                            <input class="btn btn-success" type="button" onclick="doAddOrUpdateRole();" value="提交"/>
                        </div>
                        </shiro:hasPermission>
                    </div>
                    <div class="form-group">
                        <label  class="col-sm-2 control-label">权限：</label>
                        <div>
                            <ul id="permitTree" class="ztree" style="width:230px; overflow:auto;"></ul>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <div class="m-pagination text-center" id="pagination_tab1"></div>
    </div>

<script type="text/javascript">
    // 权限树
    var permitTreeObj;
    var permitSetting = {
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
            data: {id:-1},
            success:function(data){
                permitTreeObj=$.fn.zTree.init($("#permitTree"), permitSetting, data);
                permitTreeObj.expandAll(true);
            }
        });
    }


    function doAddOrUpdateRole() {
        var zTree = $.fn.zTree.getZTreeObj("permitTree");
        var nodes = zTree.getCheckedNodes(true);
        var permitIds = new Array();
        var id = $.trim($("#id").val());
        var parentId = $.trim($("#parentId").val());
        var roleName = $.trim($("#roleName").val());
        var roleDescription = $.trim($("#roleDescription").val());
        var sort = $.trim($("#sort").val());
        // 排序正则
        var regular = /^[0-9]*[1-9][0-9]*$/;

        for (var i = 0, l = nodes.length; i < l; i++) {
            permitIds.push(nodes[i].id);
        }

        if (nodes.length == 0) {
            alert("请选择权限");
            return false;
        }

        if (!regular.test(sort)) {
            alert("排序必须正整数");
            return false;
        }

        if(sort > 999) {
            alert("排序必须在1000以内");
            return false;
        }

        if (roleName) {
            $.ajax({
                method: "post",
                traditional : true,
                url: path + "/system/pmsrole/edit",
                dataType: "json",
                data: {
                    'id' : id,
                    'parentId' : parentId,
                    'roleName' : roleName,
                    'roleDescription' : roleDescription,
                    'sort' : sort,
                    'permitIds' : permitIds
                }
            }).success(function (event) {
                if (event.code == 200) {
                    $('#addDiv').modal("hide");
                    $(".modal-body.tips").html("<p class='text-success'><b>提交成功</b></p>");
                    $("#tips").modal("show");

                    // 初始化角色树，并选中刚刚编辑的角色
                    initRoleTree(event.data.editId);
                } else if(event.code == 40000) {
                    $('#addDiv').modal("hide");
                    $(".modal-body.tips").html("<p class='text-success'><b>" + event.message + "</b></p>");
                    $("#tips").modal("show");
                } else {
                    $(".modal-body.tips").html("<p class='text-success'><b>请求失败</b></p>");
                    $("#tips").modal("show");
                }
            }).error(function (event) {
                $('#addDiv').modal("hide");
                $(".modal-body.tips").html("<p class='text-danger'><b>服务器异常</b></p>");
                $("#tips").modal("show");
            });
        } else {
            $(".modal-body.tips").html("<p class='text-danger'><b>参数不能为空</b></p>");
            $("#tips").modal("show");
        }
    }


</script>