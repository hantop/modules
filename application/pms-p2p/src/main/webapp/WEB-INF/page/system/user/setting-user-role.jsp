<%--
  Created by IntelliJ IDEA.
  User: bogle
  Date: 2015/11/10
  Time: 15:28
  To change this template use File | Settings | File Templates.
--%>
<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<link rel="stylesheet" href="/static/component/HoldOn/HoldOn.min.css">
<link rel="stylesheet" href="/static/component/zTree_v3/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.exedit-3.5.js"></script>
<jsp:include page="/WEB-INF/page/inc/jquery-validation.jsp"/>
<script type="text/javascript" src="/static/component/HoldOn/HoldOn.min.js"></script>
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>

<style>
    ul.ztree {margin-top: 10px;border: 1px solid #617775;background: #f0f6e4;width:220px;height:200px;overflow-y:scroll;overflow-x:auto;}
</style>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/system/index">系统管理</a></li>
                <li class="active"><a href="${path}/system/pmsuser">系统用户</a></li>
                <li class="active"><b>角色设置</b></li>
            </ol>
            <form id="dataform" class="form-horizontal">
                <input type="hidden" value="${userId}" name="userId" id="userId"/>
                <div class="form-group">
                    <label class="col-sm-2 control-label">登陆账号：</label>
                    <div class="col-sm-10">
                        <p class="form-control-static">${targetUser.username}</p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">真实姓名：</label>
                    <div class="col-sm-10">
                        <p class="form-control-static">${targetUser.realname}</p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">角色设置：</label>
                    <div class="panel panel-default">
                        <div class="panel-body">
                            <ul id="roleTree" class="ztree" style="width:230px; overflow:auto;"></ul>
                        </div>
                    </div>
                </div>
                <input type="hidden" name="roles" id="roles" />
                <button type="button" class="btn btn-primary col-md-offset-2" onclick="doSubmit();">保存</button>
                <a class="btn btn-default" href="${path}/system/pmsuser">返回</a>
            </form>
        </div>
    </div>
</div>

<div id="menuContent" class="menuContent" style="display:none; position: absolute;">
    <ul id="tree" class="ztree" style="margin-top:0; width:360px;"></ul>
</div>

<script type="application/javascript">
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
            selectedMulti: true
        },
        check: {
            enable: true,
            chkStyle: "checkbox",
            chkboxType: {"Y": ""}
        }
    };

    $(function(){
        init();
    });

    function init(){
        roleTreeObj=$.fn.zTree.init($("#roleTree"), roleTreeSetting, ${roles});
        roleTreeObj.expandAll(true);
    }

    function doSubmit() {
        var nodes = roleTreeObj.getCheckedNodes(true);
        var roleIds = new Array();
        for (var i = 0, l = nodes.length; i < l; i++) {
            roleIds.push(nodes[i].id);
        }
        if (nodes.length == 0) {
            alert("请选择角色");
        } else {
            $.ajax({
                url: path + '/system/pmsuser/settingUserRole',
                method: "post",
                traditional : true,
                dataType: "json",
                data: {
                    'userId': $("#userId").val(),
                    'roleIds': roleIds
                }
            }).success(function (event) {
                bootbox.alert({message: "<h3>设置成功</h3>"});
            }).error(function (event) {
                bootbox.alert({message: "<h3>设置失败</h3>"});
            });
        }
    }
</script>



