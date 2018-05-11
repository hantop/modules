<%--
  Created by IntelliJ IDEA.
  User: bogle
  Date: 2015/11/10
  Time: 15:28
  To change this template use File | Settings | File Templates.
--%>
<%@page language="java" pageEncoding="utf-8" %>
<%@ page import="com.alibaba.fastjson.JSON" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
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
                <li class="active"><b>部门设置</b></li>
            </ol>
            <form:form class="form-horizontal" action="${path}/system/pmsuser/settingDepartment"
                       method="post" id="deptForm"
                       commandName="targetInfo"
                    >
                <input type="hidden" value="${id}" name="userId"/>
                <div class="form-group">
                    <label class="col-sm-2 control-label">登陆账号：</label>
                    <div class="col-sm-10">
                        <p class="form-control-static">${targetInfo['targetUser'].username}</p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">真实姓名：</label>
                    <div class="col-sm-10">
                        <p class="form-control-static">${targetInfo['targetUser'].realname}</p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">部门设置：</label>
                    <div class="col-sm-2">
                        <input type="hidden" id="organizationId" value="${targetInfo['targetGroup'].id}" name="groupId"/>
                        <input type="text" id="organizationName" class="form-control input-sm" value="${targetInfo['targetGroup'].name}" readonly>
                    </div>
                    <a id="menuBtn" href="#" style="font-size: 16px;">选择</a>
                </div>
                <button type="submit" class="btn btn-primary col-md-offset-2">保存</button>
                <a class="btn btn-default" href="${path}/system/pmsuser">返回</a>
            </form:form>
        </div>
    </div>
</div>

<div id="menuContent" class="menuContent" style="display:none; position: absolute;">
    <ul id="tree" class="ztree" style="margin-top:0; width:160px;"></ul>
</div>

<c:if test="${param.success}">
    <script type="application/javascript">
        (function($) {
            bootbox.alert({message:"<h3>设置成功</h3>"});
        })(jQuery);
    </script>
</c:if>

<script type="application/javascript">
    (function ($) {

        var setting = {
            view: {
                dblClickExpand: false,
                selectedMulti: false
            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            callback: {
                onClick: onClick
            }
        };

        var zNodes =<%=JSON.toJSONString(request.getAttribute("groups"))%>;


        function onClick(e, treeId, treeNode) {
            var zTree = $.fn.zTree.getZTreeObj("tree"),
                    nodes = zTree.getSelectedNodes(),
                    id = "",
                    name = "";
            nodes.sort(function compare(a,b){return a.id-b.id;});
            for (var i=0, l=nodes.length; i<l; i++) {
                id += nodes[i].id + ",";
                name += nodes[i].name + ",";
            }
            if (id.length > 0 ) id = id.substring(0, id.length-1);
            if (name.length > 0 ) name = name.substring(0, name.length-1);
            $("#organizationId").val(id);
            $("#organizationName").val(name);
            hideMenu();
        }


        function hideMenu() {
            $("#menuContent").fadeOut("fast");
            $("body").unbind("mousedown", onBodyDown);
        }


        function onBodyDown(event) {
            if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
                hideMenu();
            }
        }


        function showMenu() {
            var cityObj = $("#organizationName");
            var cityOffset = $("#organizationName").offset();
            $("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");

            $("body").bind("mousedown", onBodyDown);
        }

        $.fn.zTree.init($("#tree"), setting, zNodes);
        $("#menuBtn,#organizationName").click(showMenu);


        $("#deptForm").on('submit',function() {
            var val = $("#organizationName").val();
            if($.trim(val) == '') {
                bootbox.alert({message:"<h3>请选择部门</h3>"});
                return false;
            }
            return true;
        });


    })(jQuery)
</script>



