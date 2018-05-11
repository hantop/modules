<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="topnav">
    <div class="topnav-logo">
        <img src="/static/image/logo.png"/>
    </div>
    <div class="topnav-nav">
        <ul id="topmenu_container">

        </ul>
    </div>
    <div class="topnav-right">
        <ul>
            <li>
                <span class="icon icon-user"></span>
                <span>hello,<shiro:principal/></span>
                <span hidden="hidden" id="currentUserName"><shiro:principal/></span>
                <span class="icon icon-sort-desc"></span>
             </li>
             <li><a data-toggle="modal" href="#alterPassword" >修改密码</a></li>
             <li class="exit">
                 <a href="${path}/logout"><span class="icon icon-power-off"></span></a>
             </li>
        </ul>
    </div>
</div>

<!-- 模态框（Modal） -->
<div class="modal fade" id="alterPassword" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button id="crossButton" type="button" class="close" 
               data-dismiss="modal" aria-hidden="true">
                  &times;
            </button>
            <h4 class="modal-title" id="myModalLabel">
              修改密码
            </h4>
         </div>
         
         <div class="modal-body">
            <form class="form-horizontal">
                <div class="form-group">
                    <label  class="col-sm-3 control-label">当前密码：</label>
                    <div class="col-sm-4">
                         <input id="oldPassword" type="password" class="form-control" placeholder="请输入旧密码">
                    </div>
                    <label id="password0error" class="help-block"></label>
                </div>
                <div class="form-group">
                    <label  class="col-sm-3 control-label">新密码：</label>
                    <div class="col-sm-4">
                         <input id="password1" type="password" class="form-control password" placeholder="请输入新密码">
                    </div>
                    <label id="password1error" class="help-block"></label>
                </div>
                <div class="form-group">
                    <label  class="col-sm-3 control-label">确认密码：</label>
                    <div class="col-sm-4">
                         <input id="password2" type="password" class="form-control password" placeholder="请输入新密码">
                    </div>
                    <label id="password2error" class="help-block"></label>
                </div>
            </form>
         </div>
         
         <div class="modal-footer">
            <button id="closeButton" type="button" class="btn btn-default" 
               data-dismiss="modal">关闭
            </button>
            <a id="logoutButton" href="${path}/logout" class="btn btn-default" role="button">
                                      退出
            </a>
            <button type="button" onclick="alterPassword();"  class="btn btn-primary">
               提交更改
            </button>
         </div>
      </div><!-- /.modal-content -->
   </div><!-- /.modal -->
</div>

<script>
    var hasError=false;
    var pwdIsRight=false;

	/**
	 * 加载头菜单
	 */
	function initTopmenu() {
		var container = $("#topmenu_container");
		var topmenuHtml = "";
		var topmenu = null;
		$.ajax({
			method: "get",
			traditional : true,
			url: path + "/system/pmsmenu/menu/topmenu",
			dataType: "json"
		}).success(function (data) {
			// 清空头菜单内容
			container.html("");
			for (var i = 0; i < data.length; i++) {
				topmenu = data[i];
				topmenuHtml += "<li>";
				topmenuHtml += "<a href='" + (path + topmenu.permitUrl) + "'>";
				topmenuHtml += topmenu.name;
				topmenuHtml += "</a>";
				topmenuHtml += "</li>";
			}
			container.html(topmenuHtml);
		}).error(function (data) {
			$('#addDiv').modal("hide");
			$(".modal-body.tips").html("<p class='text-danger'><b>服务器异常</b></p>");
			$("#tips").modal("show");
			return false;
		});

	}

    $(function(){
		// 加载头菜单
		initTopmenu();

    	needChangePassword();
    	
    	$('#oldPassword').blur(function(){
    		var oldPassword=$.trim($('#oldPassword').val());
    		if(oldPassword!=""){
    			passwordIsRight(oldPassword);
    		}
    	});
    	
    	$('#password1').blur(function(){
        	$('#password1').parent().parent().removeClass('has-error');
        	$('#password1error').text("");
    	});
    	$('#password2').blur(function(){
        	$('#password2').parent().parent().removeClass('has-error');
        	$('#password2error').text("");
    	});
    	$('.password').blur(function(){
        	var password1=$.trim($('#password1').val());
        	var password2=$.trim($('#password2').val());
        	if(password1==""||password2==""||password1==password2){
        		$('#password2').parent().parent().removeClass('has-error');
        		$('#password2error').text("");
        	}
    	});
    	
    });
    
	function passwordIsRight(oldPassword){
	    $.ajax({
	        url: "${path}/system/pmsuser/passwordIsRight",
	        type: 'post',
	        data: {username:"<%=SecurityUtils.getSubject().getPrincipal().toString()%>",password:oldPassword},
	        dataType:"text",
	        success:function(data){
	        	if(data=="false"){
	        		pwdIsRight=false;
            		$('#oldPassword').parent().parent().addClass('has-error');
            		$('#password0error').text("密码错误");
	        	}
	        	else{
	        		pwdIsRight=true;
	            	$('#oldPassword').parent().parent().removeClass('has-error');
	            	$('#password0error').text("");
	        	}
	        }
	    });
	}
    
    function alterPassword(){
    	var oldPassword=$.trim($('#oldPassword').val());
    	var password1=$.trim($('#password1').val());
    	var password2=$.trim($('#password2').val());
		if(oldPassword==""){
			hasError=true;
    		$('#oldPassword').parent().parent().addClass('has-error');
    		$('#password0error').text("不能为空");
		}
		else{
			if(!pwdIsRight){
				hasError=true;
			}
		}

    	if(password1==""||password2==""){
    		hasError=true;
    		if(password1==""){
        		$('#password1').parent().parent().addClass('has-error');
        		$('#password1error').text("不能为空");
    		}
    		if(password2==""){
        		$('#password2').parent().parent().addClass('has-error');
        		$('#password2error').text("不能为空");
    		}
    	}
    	else{
    		if(password1!=password2){
    			hasError=true;
        		$('#password2').parent().parent().addClass('has-error');
        		$('#password2error').text("密码不一致");
    		}
    	}
    	
        if(!hasError){
		    $.ajax({
		        url: "${path}/system/pmsuser/changePassword",
		        type: 'post',
		        data: {username:"<%=SecurityUtils.getSubject().getPrincipal().toString()%>",password:password1},
		        success:function(){
		        	alert("修改成功");
		        	$('#alterPassword').modal('hide');
		        },
		        error:function(){
		        	alert("修改失败");
		        }
		    });
        }
        hasError=false;
    }
    
    function needChangePassword(){
	    $.ajax({
	        url: "${path}/system/pmsuser/needChangePassword",
	        type: 'post',
	        data: {username:"<%=SecurityUtils.getSubject().getPrincipal().toString()%>"},
	        dataType:"text",
	        success:function(data){
	        	if(data=="true"){
	        		$('#closeButton').css('display','none'); 
	        		$('#crossButton').css('display','none'); 
	        		$('#logoutButton').css('display','inline'); 
	        		$('#alterPassword').modal('show');
	        	}
	        	else{
	        		$('#closeButton').css('display','inline'); 
	        		$('#crossButton').css('display','inline'); 
	        		$('#logoutButton').css('display','none'); 
	        	}
	        },
	        error:function(){
	        }
	    });
    }
</script>