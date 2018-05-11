]<%--
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
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<link rel="stylesheet" href="/static/component/HoldOn/HoldOn.min.css">
<link rel="stylesheet" href="/static/component/JqueryPagination/mricode.pagination.css">
<script type="text/javascript" src="/static/component/JqueryPagination/mricode.pagination.js"></script>
<script type="text/javascript" src="/static/component/JqueryPagination/jsrender.js"></script>
<script type="text/javascript" src="/static/component/HoldOn/HoldOn.min.js"></script>

<link rel="stylesheet" href="/static/component/bootstrap/css/bootstrap.min.css" type="text/css">
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<link rel="stylesheet" href="/static/component/zTree_v3/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.exedit-3.5.js"></script>
<link rel="stylesheet" href="/static/theme/ztree-diy.css" type="text/css">
<script type="application/javascript">
    (function ($) {
        $.fn.serializeObject = function () {
            "use strict";

            var result = {};
            var extend = function (i, element) {
                var node = result[element.name];

                // If node with same name exists already, need to convert it to an array as it
                // is a multi-value field (i.e., checkboxes)
                if ($.trim(element.value) !== "") {
                    if ('undefined' !== typeof node && node !== null) {
                        if ($.isArray(node)) {
                            node.push(element.value);
                        } else {
                            result[element.name] = [node, element.value];
                        }
                    } else {
                        result[element.name] = element.value;
                    }
                }
            };
            $.each(this.serializeArray(), extend);
            return result;
        };
    })(jQuery);

    Array.prototype.contains = function (obj) {
        var i = this.length;
        while (i--) {
            if (this[i] === obj) {
                return true;
            }
        }
        return false;
    }
</script>

<div class="container  content-top">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/system/index">系统角色</a></li>
                <li class="active"><b>角色管理</b></li>
            </ol>
            <hr/>
            <div class="col-md-3">
                <div class="panel panel-default">
                    <div class="panel-body">
                        <ul id="roleTree" class="ztree" style="width:230px; overflow:auto;"></ul>
                    </div>
                </div>
            </div>
            <div class="col-md-9">
                <ul class="nav nav-tabs">
                    <li><a href="#tab1" data-toggle="tab">权限</a></li>
                    <li><a href="#tab2" data-toggle="tab">成员查看</a></li>
                </ul>
                <!-- 选项卡面板 -->
                <div id="tabContent" class="tab-content">
                    <div class="tab-pane fade" id="tab1">
                        <jsp:include page="permits.jsp"/>
                    </div>
                    <div class="tab-pane fade" id="tab2">
                        <jsp:include page="members.jsp"/>
                    </div>
                </div>
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
    $(function () {
        $.ajaxSetup({
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            complete: function (data, TS) {
                console.info(arguments);
                //对返回的数据data做判断，
                //session过期的话，就location到一个页面
                if(TS !== 'success') {
                    window.location.reload();
                }
            }
        });
        
        
        $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
            // 获取已激活的标签页的名称
            var hash = e.target.hash;
            var loaded = $('#roleTree').data('loaded');
            if (loaded && loaded.contains(hash)) {
                return;
            } else {
                if (!loaded) {
                    loaded = new Array();
                }
                loaded.push(hash);
                $('#roleTree').data('loaded', loaded);
            }
            var tab = $(hash);
            $('#pagination_' + hash.substr(1)).trigger('tab.page');
            $(e.target).data('clicked', true);
        });
    });

    // 角色树
    var roleTreeObj;
    var roleTreeSetting = {
        data: {
            keep:{
                leaf:false,//叶子节点可以有下级
                parent:true
            }
        },
        view: {
            selectedMulti: false,
            addHoverDom: addHoverDom,
            removeHoverDom: removeHoverDom
        },
        callback: {
            onClick: nodeClick,
            beforeRemove: nodeBeforeRemove,
            beforeDrop: beforeDrop
        },
        edit:{
            enable:true,
            showRenameBtn:false,
            showRemoveBtn:<shiro:hasPermission name="systemRole:delete">true</shiro:hasPermission><shiro:lacksPermission name="systemRole:delete">false</shiro:lacksPermission>,
            // 设置为只能拖放到节点内
            drag : {
                prev : false,
                inner : true,
                next : false
            }
        }
    };

    function beforeDrop(treeId, treeNodes, targetNode, moveType) {
        // 获取被拖放的节点
        var moveNodeId = treeNodes[0].id;
        // 目标节点ID
        var targetNodeId;
        // 禁止拖放到根节点
        if(targetNode == null) {
            bootbox.alert({
                buttons: {
                    ok: {
                        label: 'OK'
                    }
                },
                title: '提示',
                size: 'small',
                message: "不能把角色设置为超级管理员",
                callback: function() {
                    window.location.reload(true);
                }
            });
        } else {
            targetNodeId = targetNode.id;
            bootbox.confirm({
                title: '提示',
                size: 'small',
                message: "确认移动么",
                callback: function (result) {
                    if (result) {
                        // 移动角色
                        $.ajax({
                            method: "put",
                            url: path + "/system/pmsrole/move",
                            dataType: "json",
                            traditional : true,
                            data : {"id" : moveNodeId, "parentId" : targetNodeId}
                        }).success(function (event) {
                            if (event.code == 200) {
                                bootbox.alert({
                                    buttons: {
                                        ok: {
                                            label: 'OK'
                                        }
                                    },
                                    title: '提示',
                                    size: 'small',
                                    message: "移动成功",
                                    callback: function() {
                                        window.location.reload(true);
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
                                    message: '请求失败',
                                    callback: function() {
                                        window.location.reload(true);
                                    }
                                });
                            }
                        }).error(function (event) {
                            bootbox.alert({
                                buttons: {
                                    ok: {
                                        label: 'OK'
                                    }
                                },
                                title: '提示',
                                size: 'small',
                                message: '服务器异常',
                                callback: function() {
                                    window.location.reload(true);
                                }
                            });
                        });
                    }
                }
            });
        }
        return targetNode ? targetNode.drop !== false : true;
    }

    function initRoleTree(editId){
        $.ajax({
            url: "${path}/system/pmsrole/roletree",
            type: 'post',
            dataType:"json",
            success:function(data){
                roleTreeObj=$.fn.zTree.init($("#roleTree"), roleTreeSetting, data);
                roleTreeObj.expandAll(true);
                // 默认选中第一个节点
                var nodes = roleTreeObj.getNodes();
                if (nodes != null && nodes.length > 0) {
                    var treeNode;
                    if (editId != null) {
                        treeNode = $.fn.zTree.getZTreeObj('roleTree').getNodeByParam("id", editId, null);
                    } else {
                        treeNode = nodes[0];
                    }
                    roleTreeObj.selectNode(treeNode);
                    initTab();
                    // 设置角色属性
                    setRoleProperties(treeNode);
                    // ajax请求角色的权限
                    setPermitTreeByRoleId(treeNode);
                }


            }
        });
    }
    
    function initTab() {
        var tab = $('.nav-tabs li.active > a');
        if (tab.length == 0) {
            tab = $('.nav-tabs a[href="#tab1"]');
        }
        if (!tab.data('clicked')) {
            tab.tab('show');
        } else {
            var hash = tab.attr('href').substr(1);
            $('#pagination_' + hash).trigger('tab.page');
        }
        $('#roleTree').data('loaded', [tab.attr('href')]);
    }

    function nodeClick(event, treeId, treeNode){
//        alert(JSON.stringify(treeNode));
        // 进入第一个tab
        //var tab = $('.nav-tabs a[href="#tab1"]');
        //tab.tab('show');
        initTab();

        // 设置角色属性
        setRoleProperties(treeNode);
        // ajax请求角色的权限
        setPermitTreeByRoleId(treeNode);
    }

    function setRoleProperties(treeNode) {
        var parentId = treeNode.pId;
        var roleId = treeNode.id;
        $('#id').val(roleId);
        $('#parentId').val(parentId);
        $('#roleName').val(treeNode.name);
        $('#sort').val(treeNode.sort);
        $('#roleDescription').val(treeNode.description);
    }

    function setPermitTreeByRoleId(treeNode) {
        var parentId = treeNode.pId;
        var roleId = treeNode.id;
        var _roleId = roleId;
        if(_roleId == null) {
            _roleId = -1;
        }
        $.ajax({
            url: "${path}/system/pmspermit/permittreebyroleid",
            type: 'post',
            dataType:"json",
            data: {'roleId' : _roleId, 'parentId' : parentId},
            success:function(data){
                var node;
                var nodeObj;
                $.fn.zTree.destroy("permitTree");
                permitTreeObj=$.fn.zTree.init($("#permitTree"), permitSetting, data);
                permitTreeObj.expandAll(true);
            }
        });
    }

    function clearForm(){
        $('#id').val("");
        $('#parentId').val("");
        $('#roleName').val("");
        $('#sort').val("");
        $('#roleDescription').val("");
    }

    function nodeBeforeRemove(treeId, treeNode){
        //如果有子节点
        if(treeNode.children!=null&&treeNode.children.length!=0){
            bootbox.alert({
                buttons: {
                    ok: {
                        label: 'OK'
                    }
                },
                title: '提示',
                size: 'small',
                message: "请先删除子节点"
            });
            return false;
        }
        //如果没有子节点
        else{
            bootbox.confirm({
                title: '提示',
                size: 'small',
                message: "确认删除么",
                callback: function (result) {
                    if (result) {
                        // 删除角色
                        $.ajax({
                            method: "delete",
                            url: path + "/system/pmsrole/" + treeNode.id,
                            dataType: "json"
                        }).success(function (event) {
                            if (event.code == 200) {
                                bootbox.alert({
                                    buttons: {
                                        ok: {
                                            label: 'OK'
                                        }
                                    },
                                    title: '提示',
                                    size: 'small',
                                    message: "删除成功",
                                    callback: function() {
                                        window.location.reload(true);
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
                                    message: '请求失败',
                                    callback: function() {
                                        window.location.reload(true);
                                    }
                                });
                            }
                        }).error(function (event) {
                            bootbox.alert({
                                buttons: {
                                    ok: {
                                        label: 'OK'
                                    }
                                },
                                title: '提示',
                                size: 'small',
                                message: '服务器异常',
                                callback: function() {
                                    window.location.reload(true);
                                }
                            });
                        });
                    }
                }
            });
            return false;
        }
    }

    var newCount = 1;
    function addHoverDom(treeId, treeNode) {
    	<shiro:hasPermission name="systemRole:create">
        var sObj = $("#" + treeNode.tId + "_span");
        if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
        var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
                + "' title='add' onfocus='this.blur();'></span>";
        sObj.after(addStr);
        var btn = $("#addBtn_"+treeNode.tId);
        if (btn) btn.bind("click", function(){
            var zTree = $.fn.zTree.getZTreeObj("roleTree");
            zTree.addNodes(treeNode, {id:null, pId:treeNode.id, name:"new node" + (newCount++),code:null,sort:null});
            return false;
        });
        </shiro:hasPermission>
    };

    function removeHoverDom(treeId, treeNode) {
        $("#addBtn_"+treeNode.tId).unbind().remove();
    };

    $(function(){
        initRoleTree();
    });

    function pagination(page) {
        $('#page').val(page);
        $('#parent_form').submit();
    }
</script>