<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<jsp:include page="/WEB-INF/page/inc/jquery-validation.jsp"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script type="text/javascript" src="/static/component/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<style>
<!--
.divcss5{position : relative; width: 170px;border:1px solid #000000} 
-->
</style>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <span class="badge" style="font-size: 18px;">${title}</span>
            <form:form class="form-horizontal" role="form" action="${path}/marketing/activity/edit"
                       method="post"
                       modelAttribute="activityForm">
                <form:hidden path="id"/>
                <spring:bind path="code">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            <b class="text-danger">*</b>活动编码:
                        </label>
                        <div class="col-sm-6">
                         <c:choose>
                                <c:when test="${activityForm.status == '活动中'}">
                            <form:input id="code" path="code" readonly="true" cssClass="form-control" maxlength="20" value="${activityForm.code}"/>
                            </c:when>
                            <c:otherwise>
                            <form:input id="code" path="code" cssClass="form-control" maxlength="20" placeholder="请输入数字、字母、下划线或数字字母下划线的组合"/>
                             </c:otherwise>
                         </c:choose>
                            <input type="hidden" id="status" value="${activityForm.status}">
                            <input type="hidden" id="oldTime" value="${activityForm.timeStart}">
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="code" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">请输入2~20位的由字母,数字或下划线"_"组成的活动编码</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                <spring:bind path="name">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            <b class="text-danger">*</b>活动名称：
                        </label>
                        <div class="col-sm-6">
                            <form:input id="name" path="name" cssClass="form-control" maxlength="20" type="text" placeholder="请输入活动名称"/>
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="name" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">输入正确的明确名称</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
               <spring:bind path="timeStart">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            <b class="text-danger">*</b>活动时间：
                        </label>
                        <div class="col-sm-6">
                         <c:choose>
                                <c:when test="${activityForm.status == '活动中'}">
                            <form:input id="timeStart" path="timeStart" readonly="true" style="width: 175px;" class="form-control input-sm" value="${activityForm.timeStart}" onFocus="changeValue()"/> --
                            </c:when>
                            <c:otherwise>
                            <form:input id="timeStart" path="timeStart" readonly="true" style="width: 175px;" class="form-control input-sm" value="${activityForm.timeStart}"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});"/> --
                            </c:otherwise>
                         </c:choose>
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="timeStart" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">请选择活动开始时间</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                <spring:bind path="timeEnd">
                    <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                    <label class="col-sm-2 control-label text-right">
                        </label>
                        <div class="col-sm-6">
                            <form:input  id="timeEnd" path="timeEnd" readonly="true" style="width: 175px;" class="form-control input-sm" value="${activityForm.timeEnd}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});"/>
                            <span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="timeEnd" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">请选择活动结束时间</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </spring:bind>
                 <spring:bind path="couponIds">
                 <div class="form-group has-feedback ${status.error ? 'has-error' : ''}">
                        <label class="col-sm-2 control-label text-right">
                            	<b class="text-danger"></b>活动奖励
                        </label>
                        <div class="col-sm-6">
                         <c:choose>
                                <c:when test="${activityForm.status == '活动中'}">
                       				<input disabled="disabled" type="button" class="btn btn-primary" value="添加加息券"/>
                       				 </c:when>
                            <c:otherwise>
                       				<input type="button" name="activityForm.coupons" class="btn btn-primary appendRow" value="添加加息券"/>
                       				   </c:otherwise>
                         </c:choose>
                       				<input id="ids" tagName="ids" name="ids" type="hidden" value=""/>
                        		<span class="glyphicon form-control-feedback ${status.error ? 'glyphicon-remove' : ''}"></span>
                        </div>
                        <div class="col-sm-3 help">
                            <c:choose>
                                <c:when test="${status.error}">
                                    <form:errors path="couponIds" element="span" cssClass="help-block"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="help-block">点击添加按钮添加对应奖励类型</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                 </div>
                 </spring:bind>
                 <table class="table table-striped table-bordered table-condensed">
						<tr class="success">
							<th></th>
							<th>加息幅度</th>
							<th>投资最小限额</th>
							<th></th>
						</tr>
						<tbody id="edit_table_body">
						  <c:choose>
						 <c:when test="${activityForm.couponIds != null }"> 
								  <c:forEach var="item" items="${activityForm.couponList}">
									<tr>
										<td>
										<c:choose>
                               				 <c:when test="${activityForm.status == '活动中'}">
											<input disabled="disabled" type="button" value="选择加息券" class="btn btn-primary"/>
											</c:when>
                            				<c:otherwise>
											<input type="button" value="选择加息券" class="btn btn-primary searchCoupon"/>
											 </c:otherwise>
                         				</c:choose>
										</td>
										<td>
										<span name="activityForm.couponList.scope" tagName="scope">
										<fmt:formatNumber value="${item.scope}" pattern="#.##" type="number"/>%
										</span>
									
										<input tagName="pid" name="activityForm.couponList.id" type="hidden" value="${item.id}"/>
										<input id="childVal" tagName="childVal" name="activityForm.couponList.chileVal" type="hidden" value="1"/>
										
									</td>
									<td><span name="activityForm.couponList.minInvestMoney" tagName="minInvestMoney">${item.minInvestMoney}元</span></td>
										<c:choose>
                               			<c:when test="${activityForm.status == '活动中'}">
										<td><a href="javascript:;">删除</a></td>
										</c:when>
                            			<c:otherwise>
										<td><a href="javascript:;" class="removeItem">删除</a></td>
										</c:otherwise>
                         				</c:choose>
									</tr>
								 </c:forEach>
							</c:when> 
								<c:otherwise> 
								<tr>
									<td>
										<c:choose>
                               				 <c:when test="${activityForm.status == '活动中'}">
											<input disabled="disabled" type="button" value="选择加息券" class="btn btn-primary"/>
											</c:when>
                            				<c:otherwise>
											<input type="button" value="选择加息券" class="btn btn-primary searchCoupon"/>
											 </c:otherwise>
                         				</c:choose>
	 								</td>
									<td>
										<span name="activityForm.couponList.scope" tagName="scope">
										<fmt:formatNumber value="${scope}" pattern="#.##" type="number"/>
										</span>
										<input tagName="pid" name="activityForm.couponList.id" type="hidden" value="${id}"/>
										<input id="childVal" tagName="childVal" name="activityForm.couponList.chileVal" type="hidden" value=""/>
									</td>
									<td><span name="activityForm.couponList.minInvestMoney" tagName="minInvestMoney">${minInvestMoney}</span></td>
									<td><a href="javascript:void(0);" class="removeItem">删除</a></td>
								</tr>
							</c:otherwise>
						</c:choose>
						</tbody>
					</table>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <button type="submit" class="btn btn-primary">提交</button>
                        <a type="button" class="btn btn-default"
                           href="${path}/marketing/activity">取消</a>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>
<c:choose>
    <c:when test="${param.success}">
        <!-- Modal -->
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
            <div class="modal-dialog modal-sm" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel">
                            提示
                        </h4>
                    </div>
                    <div class="modal-body">
                            ${param.success ? '成功' : '失败'}
                    </div>
                    <div class="modal-footer">
                        <a href="${path}/marketing/activity/edit" class="btn btn-default">继续添加</a>
                        <a href="${path}/marketing/activity" class="btn btn-primary">返回列表</a>
                    </div>
                </div>
            </div>
        </div>
        <script type="application/javascript">
            (function ($) {
                $("#myModal").modal({
                    keyboard: true,
                    show: true,
                    backdrop: 'static'
                });
            })(jQuery);
        </script>
    </c:when>
    <c:when test="${code != null}">
        <!-- Modal -->
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
            <div class="modal-dialog modal-sm" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title">
                            提示
                        </h4>
                    </div>
                    <div class="modal-body">
                        <c:choose>
                            <c:when test="${code <= -1}">
                                活动编码不能重复
                            </c:when>
                            <c:otherwise>
                                未知错误，请联系管理员
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">确定</button>
                    </div>
                </div>
            </div>
        </div>
        <script type="application/javascript">
            (function ($) {
                $("#myModal").modal({
                    keyboard: true,
                    show: true,
                    backdrop: 'static'
                });
            })(jQuery);
        </script>
    </c:when>
</c:choose>


<script type="application/javascript">

 $("#edit_table_body").on("click",".searchCoupon",function(){
	 var father = $(this).parents('tr').index()
	
		//打开一个模式窗口(google浏览器不支持)
		// if (showModalDialog != undefined) {
			/*  var ret=window.showModalDialog("couponList");
			if(ret){
				//查询出当前选择的值
				var _row=$(this).closest("tr");
				_row.find("[tagName=scope]").html(ret.scope + "%");
				_row.find("[tagName=pid]").val(ret.id);
				_row.find("[tagName=minInvestMoney]").html(ret.minInvestMoney);
			}  */
		//}else { 
		window.open("couponList?id=" + father, null, "width=1800,height=900,toolbar=no,scrollbars=no,menubar=no,screenX=100,screenY=100"); 
		/* } */
	}).on("click",".removeItem",function(){
		//删除明细操作
		if($("#edit_table_body tr").length>1){
			//列表中有数据的时候才删
			$(this).closest("tr").remove();
		}else{
			//当前只有一行数据的时候,就只是清空当前的那一行的内容
			clearRow($(this).closest("tr"));
		}
	});
 
	//添加一行新的加息券
	$(".appendRow").click(function(){
		//获取匹配的第一个元素;    clone()一个布尔值（true 或者 false）指示事件处理函数是否会被复制。V1.5以上版本默认值是：false
		var _row=$("#edit_table_body tr:first").clone();
		//清空这一行的信息
		clearRow(_row);
	});
	
	function get(ret,num){
		if (ret) {
			$("#edit_table_body tr").eq(num).find('[tagName=scope]').text(ret.scope + "%");
			$("#edit_table_body tr").eq(num).find('[tagName=pid]').val(ret.id);
			$("#edit_table_body tr").eq(num).find('[tagName=minInvestMoney]').text(ret.minInvestMoney + "元");
			/* var _row = $(this).closest("tr");
			_row.find("[tagName=scope]").html(ret.scope + "%");
			_row.find("[tagName=pid]").val(ret.id);
			_row.find("[tagName=minInvestMoney]").html(ret.minInvestMoney); */
		}
	}
	
	function clearRow(_row){
		//将当前的这一行的信息设置为空(清空)
		_row.find("[tagName=pid]").val("");//值为空
		_row.find("[tagName=scope]").html("");
		_row.find("[tagName=minInvestMoney]").html("");
		//拷贝之后的拼接到当前的table中
		_row.appendTo($("#edit_table_body"));
	}
	
	function changeValue(){
		var status = $("#status").val();
   	 	if (status == '活动中') {
	    	bootbox.alert({message:"<h3>【活动正在进行中,不能修改开始时间】</h3>"});
		}
	}
 
    (function ($) {
        $('form[role="form"]').validate({
            rules: {
                code: {
                    required: true,
                    rangelength: [2, 20],
                    myreg: true
                },
                name: {
                    required: true,
                    rangelength: [2, 20]
                },
                timeStart: {
                    required: true
                },
                timeEnd: {
                    required: true
                }
            },
            messages: {
                code: {
                    required: "活动编码不能为空",
                    rangelength: $.validator.format("活动编码字符长度在 {0} 到 {1} 之间.")
                },
                name: {
                    required: "活动名称不能为空",
                    rangelength: $.validator.format("活动编码字符长度在 {0} 到 {1} 之间.")
                },
                timeStart: {
                    required: "活动开始日期不能为空"
                },
                timeEnd: {
                    required: "活动结束日期不能为空"
                }
            },
            highlight: function (element, errorClass, validClass) {
                //空方法，去除为input添加错误样式
                //未通过的元素添加效果
                var formGroup = $(element).parents('div.form-group');
                formGroup.addClass('has-error').removeClass('has-success')
                formGroup.find('span.glyphicon.form-control-feedback').addClass('glyphicon-remove').removeClass('glyphicon-ok');
                return false;
            },
            unhighlight: function (element, errorClass, validClass) {
                //去除未通过的元素的效果
                var formGroup = $(element).parents('div.form-group');
                formGroup.addClass('has-success').removeClass('has-error')
                formGroup.find('span.glyphicon.form-control-feedback').addClass('glyphicon-ok').removeClass('glyphicon-remove');
            },
            errorPlacement: function (error, element) {
                var formGroup = $(element).parents('div.form-group');
                error.appendTo(formGroup.find('div.help').empty());
            },
            success: function (element) {
                var formGroup = $(element).parents('div.form-group');
                formGroup.find('.help-block').empty().html('正确');
            },
            submitHandler: function (form) {
            	 var status = $("#status").val();
            	 var oldTime = $("#oldTime").val();
            	 var staTime = $("#timeStart").val();
     		    var endTime = $("#timeEnd").val();
     		    
     		    if(staTime != '' && endTime != ''){
     		    	var d = new Date();
     		        var d1 = new Date(staTime.replace(/-/g,"/"));
     		        var d2 = new Date(endTime.replace(/-/g,"/"));
     		        
     		        var beginDateSplit = staTime.split('-');
     		        var endDateSplit = endTime.split('-');
     		        
     		       /* if (d - Date.parse(d1) > 0) {
                       bootbox.alert({message:"<h3>【开始时间不能早于当前时间】</h3>"});
                       return false;
   		       		 } */
     		        
     		        if (Date.parse(d1) - Date.parse(d2) > 0) {
                         bootbox.alert({message:"<h3>【结束时间不能早于开始时间】</h3>"});
                         return false;
     		        }
     		    }
            	//去掉不合法的数据行,不需要保存(遍历所有)
    			$("#edit_table_body tr").each(function(index,row){
    				var _row=$(row);
    				//当前的加息券id为null,移除掉,不保存
    				if(index > 0 && !_row.find("[tagName=pid]").val()){
    					_row.remove();
    				}
    			});
            	var id;
            	var ids = new Array();
            	var _a = $("#ids").val();
    			$("#edit_table_body tr").each(function(index,row){ 
    				//每一行数据
    				var _row=$(row);
    				//匹配给定的属性是某个特定值的元素返回值:[attribute=value]
    				 var a =  _row.find("[tagName=pid]").attr("name","couponList["+index+"].id");
    				id = a.val();
    				_row.find("[tagName=scope]").attr("name","couponList["+index+"].scope");
    				_row.find("[tagName=minInvestMoney]").attr("name","couponList["+index+"].minInvestMoney");
    				ids.push(id);
    			});
    			for (var i = 0; i < ids.length; i++) {
					_a += ids[i] + ","
				}
    			/* if (_a == null || _a == "" || _a == ",") {
   				 bootbox.alert({message:"<h3>【请至少选择一种加息券】</h3>"});
                    return false;
				}else{ */
					$("#ids").val(_a);
	            	var index = layer.load(0, {
	                    shade: [0.4,'#fff',false] //0.1透明度的白色背景
	                }); 
	                	form.submit();
	                	layer.close();
				//}
            },
            ignore: '.ignore',
            errorClass: "help-block",
            errorElement: 'span',
            focusInvalid: false,
            focusCleanup: true,
            onkeyup: false
        });
        
      //自定义正则表达示验证方法  
        $.validator.addMethod("myreg",function(value,element,params){  
        	    var myreg = /^[A-Za-z0-9_]*$/g;  
                return this.optional(element)||(myreg.test(value));  
            },'请输入数字,字母或数字字母组合,可添加下划线"_"！');  

    })(jQuery)
</script>