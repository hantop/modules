<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<link rel="stylesheet" href="/static/component/zTree_v3/css/zTreeStyle/zTreeStyle.css" type="text/css">
<link rel="stylesheet" href="/static/component/bootstrap/css/bootstrap.min.css" type="text/css">
<script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.exedit-3.5.js"></script>
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<link rel="stylesheet" href="/static/theme/ztree-diy.css" type="text/css">

<div class="col-md-9" id="deptContent" style="display: none">
    <div class="panel panel-default">
        <div class="panel-body">
            <form class="form-horizontal" id="deptForm">
                <div class="form-group">
                    <label class="col-sm-2 control-label">上级部门ID：</label>
                    <div class="col-sm-2">
                        <%--<select class="form-control" name="deptParent" id="deptParent">
                            <c:forEach items="${pmsGroupTree}" var="dept">
                                <option value="${dept.id}">${dept.name}</option>
                            </c:forEach>
                        </select>--%>
                            <input id="deptId" name="deptId" type="text" class="form-control" disabled>
                    </div>
                </div>
                <div class="form-group">
                    <label  class="col-sm-2 control-label"><span style="color: red">*</span>部门名称：</label>
                    <div class="col-sm-2">
                        <input id="deptName" name="deptName" type="text" class="form-control" value="" >
                    </div>
                </div>
                <div class="form-group">
                    <label  class="col-sm-2 control-label"><span style="color: red">*</span>部门排序：</label>
                    <div class="col-sm-3">
                        <input id="deptSort" name="deptSort" type="text" class="form-control" value="">
                    </div>
                </div>
                <div class="form-group">
                    <label  class="col-sm-2 control-label">描述：</label>
                    <div class="col-sm-3">
                        <textarea class="form-control" rows="3" id="deptMark" name="deptMark"></textarea>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-2"></div>
                    <div class="col-sm-2">
                        <input type="hidden" id="doType" name="doType" value=""/>
                        <input type="hidden" id="nodeId" name="nodeId"/>
                        <shiro:hasPermission name="organization:commit">
                            <input  class="btn btn-success" type="button" onclick="deptManage();" value="提交"/>
                        </shiro:hasPermission>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<%--<div class="col-md-9" id="addChildDept" style="display: none">
    <div class="panel panel-default">
        <div class="panel-body">
            <form class="form-horizontal" id="childDeptForm">
                <div class="form-group">
                    <label class="col-sm-2 control-label">上级部门：</label>
                    <label class="control-label" id="childDept"></label>
                </div>
                <div class="form-group">
                    <label  class="col-sm-2 control-label"><span style="color: red">*</span>部门名称：</label>
                    <div class="col-sm-2">
                        <input id="childDeptName" name="childDeptName" type="text" class="form-control" value="" >
                    </div>
                </div>
                <div class="form-group">
                    <label  class="col-sm-2 control-label"><span style="color: red">*</span>部门排序：</label>
                    <div class="col-sm-3">
                        <input id="childDeptSort" name="childDeptSort" type="text" class="form-control" value="">
                    </div>
                </div>
                <div class="form-group">
                    <label  class="col-sm-2 control-label"><span style="color: red">*</span>描述：</label>
                    <div class="col-sm-3">
                        <textarea class="form-control" rows="3" id="childDeptMark" name="childDeptMark"></textarea>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-2"></div>
                    <div class="col-sm-2">
                        <input type="hidden" id="pid" name="pid" value=""/>
                        <input  class="btn btn-success" type="button" onclick="updateChildDeptContent();" value="提交"/>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>--%>
</div>
</div>
</div>
<script>
    var treeObj;
    var btnDel = $("btnDel").val();
    var setting = {
        data: {
            simpleData: {
                enable: true
            },
            keep:{
                leaf:false,//叶子节点有下级
                parent:true //父节点即使被删除所有子节点依然是父节点状态
            }
        },
        view: {
            selectedMulti: false, //是否可以多选
            <shiro:hasPermission name="organization:create">
                addHoverDom: addHoverDom, //显示自定义控件
            </shiro:hasPermission>

            removeHoverDom: removeHoverDom  //隐藏自定义控件
        },
        edit:{
            enable:true,  //节点可编辑
            <shiro:hasPermission name="organization:delete">
                showRemoveBtn:true,    //删除按钮显示
            </shiro:hasPermission>
            <shiro:lacksPermission name="organization:delete">
            showRemoveBtn:false,    //删除按钮显示
            </shiro:lacksPermission>
            showRenameBtn:false,  //重命名按钮显示
        },
        callback : {
            onClick : function(event, treeId, treeNode) {
                //如果是顶级菜单，父id 的值为null
                if(treeNode.pId){
                    if(treeNode.id){		//如果id 为空则是新增,不为空则修改
                        $("#maintain").hide();
                        $("#nodeId").val(treeNode.id);
                        $("#deptId").val(treeNode.pId);
                        $("#deptName").val(treeNode.name);
                        $("#deptSort").val(treeNode.orderId);
                        $("#deptMark").val(treeNode.marks);
                        $("#doType").val('update');
                        $("#deptContent").show();
                    }else{
                        $("#maintain").hide();
                        var node = treeObj.getNodeByParam("pId", null, null);
                        $("#nodeId").val(treeNode.pId );
                        $("#deptId").val(treeNode.pId);
                        $("#deptName").val('');
                        $("#deptSort").val('');
                        $("#deptMark").val('');
                        $("#doType").val('add');
                        $("#deptContent").show();
                    }
                }else{
                    window.location.href = path + "/system/pmsgroup";
                }
            },
            beforeRemove: deptDel
        }
    };
    var nodes = [
        <c:forEach items="${pmsGroupTree}" var="o">
        { id:${o.id}, pId:${o.parentId}, name:"${o.name}", orderId:"${o.available}", marks:"${o.mark}", open:${o.rootNode}},
        </c:forEach>
    ];

    var newCount = 1;
    function addHoverDom(treeId, treeNode) {
        var sObj = $("#" + treeNode.tId + "_span");
        if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
        var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
                + "' title='add' onfocus='this.blur();'></span>";
        sObj.after(addStr);
        var btn = $("#addBtn_"+treeNode.tId);
        if (btn) btn.bind("click", function(){
            treeObj.addNodes(treeNode, {id:null, pId:treeNode.id, name:"new node" + (newCount++),code:null,sort:null,marks:null,pName:treeNode.name});
            return false;
        });
    };
    function removeHoverDom(treeId, treeNode) {
        $("#addBtn_"+treeNode.tId).unbind().remove();
    };

    $(document).ready(function(){
        treeObj = $.fn.zTree.init($("#tree"), setting, nodes);
    });

    function deptDel(treeId, treeNode){
        bootbox.confirm("确认删除？",function(result){
            if(result){
                $.ajax({
                    url: path + "/system/pmsgroup/delete",
                    data: {id:  treeNode.id},
                    type: 'post',
                    dataType:"json",
                    success:function(data){
                        bootbox.alert({
                            size: 'small',
                            message:"删除成功！！！",
                            callback: function() {
                                window.location.reload(true);}
                        });
                    },
                    error: function(e) {
                        bootbox.alert({size: 'small',message:"服务器繁忙，请稍后再试！！！",callback: function() {
                            window.location.reload(true);}});
                        return false;
                    }
                });
            }
        });
        return false;
    }

    function updateDeptContent(){
        var deptName = $('#deptName').val();
        var deptSort = $('#deptSort').val();
        if(deptName == ''){
            bootbox.alert({size: 'small',message:"部门名称必须输入！！！"});
            return;
        }
        if(deptSort == ''){
            bootbox.alert({size: 'small',message:"部门排序必须输入！！！"});
            return;
        }
        var param = $('#deptForm').serialize();
        $.ajax({
            url: path + "/system/pmsgroup/update",
            data: param,
            type: 'post',
            dataType:"json",
            success:function(data){
                var returnMsg = data.msg;
                if(returnMsg == 'success'){
                    bootbox.alert({size: 'small',message:"修改成功！！！",callback: function() {
                        window.location.reload(true);}
                    });
                }else{
                    bootbox.alert({size: 'small',message:"服务器繁忙，请稍后再试！！！",callback: function() {
                        window.location.reload(true);}
                    });
                }
            },
            error: function(e) {
                bootbox.alert({size: 'small',message:"服务器繁忙，请稍后再试！！！",callback: function() {
                    window.location.reload(true);}
                });
                return false;
            }
        });
    }

    function deptManage(){
        var type = $("#doType").val();
        if(type == 'add'){
            addDeptContent();
        }else if(type == 'update'){
            updateDeptContent();
        }
    }

    $("#addChildBtn").click(function () {
        var nodeId = $("#nodeId").val();
        $.ajax({
            url: path + "/system/pmsgroup/findNode",
            data: {nodeId: nodeId},
            type: 'post',
            dataType:"json",
            success:function(data){
                var deptName = data.name;
                $("#childDept").html(deptName);
                $("#pid").val(data.id);
                $("#deptContent").hide();
                $("#addChildDept").show();
            },
            error: function(e) {
                bootbox.alert({size: 'small',message:"服务器繁忙，请稍后再试！！！",callback: function() {
                    window.location.reload(true);}
                });
                return false;
            }
        });
    });

    function addDeptContent(){
        var deptName = $('#deptName').val();
        var deptSort = $('#deptSort').val();
        if(deptName == ''){
            bootbox.alert({size: 'small',message:"部门名称必须输入！！！"});
            return;
        }
        if(deptSort == ''){
            bootbox.alert({size: 'small',message:"部门排序必须输入！！！"});
            return;
        }
        var param = $('#deptForm').serialize();
        $.ajax({
            url: path + "/system/pmsgroup/add",
            data: param,
            type: 'post',
            dataType:"json",
            success:function(data){
                var returnMsg = data.msg;
                if(returnMsg == 'success'){
                    bootbox.alert({size: 'small',message:"部门添加成功！！！",callback: function() {
                        window.location.reload(true);}
                    });
                }else{
                    bootbox.alert({size: 'small',message:"服务器繁忙，请稍后再试！！！",callback: function() {
                        window.location.reload(true);}
                    });
                }
            },
            error: function(e) {
                bootbox.alert({size: 'small',message:"服务器繁忙，请稍后再试！！！",callback: function() {
                    window.location.reload(true);}
                });
                return false;
            }
        });
    }
</script>