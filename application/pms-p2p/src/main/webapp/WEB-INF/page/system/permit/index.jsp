<%--
  Created by IntelliJ IDEA.
  User: Lullaby
  Date: 2015/7/17
  Time: 16:24
  To change this template use File | Settings | File Templates.
--%>
<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
  <link rel="stylesheet" href="/static/component/bootstrap/css/bootstrap.min.css" type="text/css">
  <script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>

  <link rel="stylesheet" href="/static/component/zTree_v3/css/zTreeStyle/zTreeStyle.css" type="text/css">
  <script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.core-3.5.js"></script>
  <script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.excheck-3.5.js"></script>
  <script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.exedit-3.5.js"></script>
  
  <link rel="stylesheet" href="/static/theme/ztree-diy.css" type="text/css">
  <script type="text/javascript" >
	var treeObj;
	
	var setting = {
		data: {
			keep:{
				leaf:true,//叶子节点不能有下级
				parent:true //父节点即使被删除所有子节点依然是父节点状态
			}
		},
		view: {
		    selectedMulti: false, //是否可以多选
		    addHoverDom: addHoverDom, //显示自定义控件
			removeHoverDom: removeHoverDom  //隐藏自定义控件
		},
		callback: {
			onClick: nodeClick,  //点击节点事件
			beforeRemove: nodeBeforeRemove  //删除节点之前
		},
		edit:{
			enable:true,  //节点可编辑
			showRenameBtn:false,  //重命名按钮显示
			showRemoveBtn:<shiro:hasPermission name="permission:delete">true</shiro:hasPermission><shiro:lacksPermission name="permission:delete">false</shiro:lacksPermission>,    //删除按钮显示
			drag:{
				isCopy:false,
				isMove:false
			}
		},
		async: {
			enable: true,
			url: "${path}/system/pmspermit/permittree",
			type: "post",
			dataType: "json",
			autoParam: ["id"]
		}
	};
	
	function getPermitTree(){
		$.ajax({
		    url: "${path}/system/pmspermit/permittree",
		    type: 'post',
		    data: {id:-1},
		    dataType:"json",
		    success:function(data){
		    	treeObj=$.fn.zTree.init($("#tree"), setting, data);
		    	//只展开到第二层
		    	var arr=treeObj.getNodesByParam('level','1',null);
		    	for(var i=0;i<arr.length;i++){
		    		treeObj.expandNode(arr[i], true, false, false);
		    	}
		    }
		});
	}
	
	function nodeClick(event, treeId, treeNode){
		clearError();
		//alert(JSON.stringify(treeNode));
		$('#nodeId').val(treeNode.id);
		$('#nodePid').val(treeNode.pId);
		$('#nodeNamee').val(treeNode.name);
		$('#nodeCode').val(treeNode.code);
		$('#nodePermitUrl').val(treeNode.permitUrl);
		$('#nodeType').empty();
		var typeText="";
		if(treeNode.type=="root"){
			typeText="根节点";
		}
		else if(treeNode.type=="topmenu"){
			typeText="顶部菜单";
		}
		else if(treeNode.type=="leftmenu"){
			typeText="左侧菜单";
		}
		if(treeNode.isParent&&treeNode.type!='linkbutton'){
			$('#nodeType').append("<option value='"+treeNode.type+"'>"+typeText+"</option>");
			$('#nodeType').attr("disabled",true);
		}
		else{
			$('#nodeType').append("<option value=''>请选择类型</option>");
			$('#nodeType').append("<option value='linkbutton'>跳转按钮</option>");
			$('#nodeType').append("<option value='functionbutton'>功能按钮</option>");
			$('#nodeType').append("<option value='column'>字段</option>");
			$('#nodeType').attr("disabled",false);
		}
	    $('#nodeType').val(treeNode.type);
		$('#nodeSort').val(treeNode.sort);
		$('#nodeDescription').val(treeNode.description);
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
            bootbox.confirm("确认删除？", function (result) {  
                if(result) {  
        		    $.ajax({
        		        url: "${path}/system/pmspermit/deletenode",
        		        type: 'post',
        		        data: {id:treeNode.id},
        		        dataType:"json",
        		        success:function(data){
        		        	clearForm();
			            	var pNode = treeObj.getNodeByParam("id", treeNode.pId, null);
			            	treeObj.reAsyncChildNodes(pNode, "refresh");
        					bootbox.alert({
        						buttons: {
        							ok: {
        								label: 'OK'
        							}
        						},
        						title: '提示',
        						size: 'small',
        						message: data.message,
        			            callback: function() {  

        			            }
        					});
        		        }
        		    });
                } 
            });
            return false;
		}
	}
	
	var newCount = 1;
	function addHoverDom(treeId, treeNode) {
		<shiro:hasPermission name="permission:create">
		var sObj = $("#" + treeNode.tId + "_span");
		if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0||!treeNode.isParent) return;
		var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
			+ "' title='add' onfocus='this.blur();'></span>";
		sObj.after(addStr);
		var btn = $("#addBtn_"+treeNode.tId);
		if (btn) btn.bind("click", function(){
			var zTree = $.fn.zTree.getZTreeObj("tree");
			var permitType;
			var isParent=false;
			if(treeNode.type=="root"){
				permitType="topmenu";
				isParent=true;
			}
			else if(treeNode.type=="topmenu"){
				permitType="leftmenu";
				isParent=true;
			}
			else{
				permitType="";
			}
			zTree.addNodes(treeNode, {id:null, pId:treeNode.id, name:"new node" + (newCount++),code:null,type:permitType,sort:null,isParent:isParent});
			return false;
		});
		</shiro:hasPermission>
	};
	
	function removeHoverDom(treeId, treeNode) {
		$("#addBtn_"+treeNode.tId).unbind().remove();
	};
	
	function saveNode(){
		var nodes = treeObj.getSelectedNodes();
		if(nodes.length>0){
		    var nodeId=$('#nodeId').val();
		    var nodePid=$('#nodePid').val();
		    var nodeNamee=$('#nodeNamee').val();
		    var nodeCode=$('#nodeCode').val();
		    var nodePermitUrl=$('#nodePermitUrl').val();
		    var nodeIsParent=nodes[0].isParent;
		    var nodeType=$('#nodeType').val();
		    var nodeSort=$('#nodeSort').val();
		    var nodeDescription=$('#nodeDescription').val();
		    var hasError=checkInput(nodeNamee,nodeCode,nodeType);
		    if(!hasError){
		      $.ajax({
		        url: "${path}/system/pmspermit/savenode",
		        type: 'post',
		        data: {id:nodeId,pid:nodePid,name:nodeNamee,code:nodeCode,type:nodeType,sort:nodeSort,description:nodeDescription,isParent:nodeIsParent,permitUrl:nodePermitUrl},
		        dataType:"json",
		        success:function(data){
		        	clearForm();
					bootbox.alert({
						buttons: {
							ok: {
								label: 'OK'
							}
						},
						title: '提示',
						size: 'small',
						message: data.message,
			            callback: function() {  
			            	//location.reload();
			            	if(data.status=="OK"){
				            	var pNode = treeObj.getNodeByParam("id", nodePid, null);
				            	treeObj.reAsyncChildNodes(pNode, "refresh");
			            	}
			            }
					});
		        }
		    });
		   }

		}
	}
	
	function clearForm(){
		$('#nodeId').val("");
		$('#nodePid').val("");
		$('#nodeNamee').val("");
		$('#nodeCode').val("");
		$('#nodePermitUrl').val("");
		$('#nodeType').empty();
		$('#nodeSort').val("");
		$('#nodeDescription').val("");
	}
	
	function clearError(){
		$('.help-block').text("");
		$('.form-group').removeClass('has-error');
	}
	
	function checkInput(nodeNamee,nodeCode,nodeType){
		clearError();
		var hasError=false;
		if($.trim(nodeNamee)==""){
			hasError=true;
    		$('#nodeNamee').parent().parent().addClass('has-error');
    		$('#nodeNameeError').text("不能为空");
		}
		if($.trim(nodeCode)==""){
			hasError=true;
    		$('#nodeCode').parent().parent().addClass('has-error');
    		$('#nodeCodeError').text("不能为空");
		}
		if(nodeType==""){
			hasError=true;
    		$('#nodeType').parent().parent().addClass('has-error');
    		$('#nodeTypeError').text("不能为空");
		}
		return hasError;
	}
	
	$(function(){
		getPermitTree();
		
	});
	
  </script>
  <div class="container  content-top">
     <div class="row">
         <div class="col-md-12">
             <ol class="breadcrumb">
                 <li class="active"><a href="${path}/system/index">系统管理</a></li>
                 <li class="active"><b>系统权限</b></li>
             </ol>
             <hr/>
             <div class="col-md-3">
                 <div class="panel panel-default">
                     <div class="panel-body">
                         <ul id="tree" class="ztree" style="width:230px; overflow:auto;"></ul>
                     </div>
                 </div>
             </div>
             <div class="col-md-9">
                 <div class="panel panel-default">
                     <div class="panel-body">
                         <form class="form-horizontal">
                              <div class="form-group">
                                  <label class="col-sm-2 control-label">权限ID：</label>
                                  <div class="col-sm-2">
                                      <input id="nodeId" type="text" class="form-control" disabled>
                                  </div>
                              </div>
                              <div class="form-group">
                                  <label  class="col-sm-2 control-label">权限PID：</label>
                                  <div class="col-sm-2">
                                      <input id="nodePid" type="text" class="form-control" disabled>
                                  </div>
                              </div>
                              <div class="form-group">
                                  <label  class="col-sm-2 control-label">权限名称：</label>
                                  <div class="col-sm-3">
                                      <input id="nodeNamee" type="text" class="form-control" placeholder="请输入权限名称">
                                  </div>
                                  <label id="nodeNameeError" class="help-block"></label>
                              </div>
                              <div class="form-group">
                                  <label  class="col-sm-2 control-label">权限代码：</label>
                                  <div class="col-sm-4">
                                      <input id="nodeCode" type="text" class="form-control" placeholder="请输入权限代码">
                                  </div>
                                  <label id="nodeCodeError" class="help-block"></label>
                              </div>
                              <div class="form-group">
                                  <label  class="col-sm-2 control-label">权限URL：</label>
                                  <div class="col-sm-4">
                                      <input id="nodePermitUrl" type="text" class="form-control" placeholder="请输入权限URL">
                                  </div>
                                  <label id="nodePermitUrlError" class="help-block"></label>
                              </div>
                              <div class="form-group">
                                  <label  class="col-sm-2 control-label">权限类型：</label>
                                  <div class="col-sm-2">
                                      <select id="nodeType" class="form-control">
                                      </select>
                                  </div>
                                  <label id="nodeTypeError" class="help-block"></label>
                              </div>
                              <div class="form-group">
                                  <label  class="col-sm-2 control-label">顺序：</label>
                                  <div class="col-sm-2">
                                      <input id="nodeSort" type="text" class="form-control" placeholder="请输入顺序">
                                  </div>
                              </div>
                              <div class="form-group">
                                  <label  class="col-sm-2 control-label">描述：</label>
                                  <div class="col-sm-4">
                                      <textarea id="nodeDescription" class="form-control" ></textarea>
                                  </div>
                              </div>
                              <shiro:hasPermission name="permission:commit">
                              <div class="form-group">
                                  <div class="col-sm-2"></div>
                                  <div class="col-sm-2">
                                        <input  class="btn btn-success" type="button" onclick="saveNode()" value="提交"/>
                                  </div>
                              </div>
                              </shiro:hasPermission>
                         </form>
                     </div>
                 </div>
             </div>
         </div>
     </div>
  </div>

