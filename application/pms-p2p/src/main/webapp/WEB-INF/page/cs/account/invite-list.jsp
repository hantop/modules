<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div class="" id="invitecontainer">
	<div class="col-md-12"  style="margin-top: 20px;">
		<div class="col-md-12" style="margin-bottom: 10px;">
			<form class="form-inline" method="post"
				action="${path}/cs/account/invite" id="inviteform">
				<shiro:hasPermission name="inviteTab:search">
			    <div class="form-group">
                    <label>被邀请人注册日期：</label>
                    <input type="text" class="form-control" id="inviteformStartDate" name="startDate" readonly="readonly" maxlength="20" class="form-control input-sm" style="width:163px;"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"> --
                    <input type="text" class="form-control" id="inviteformEndDate" name="endDate" readonly="readonly" maxlength="20" class="form-control input-sm" style="width:163px;"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});">
                </div>
				<div class="form-group">
					<input type="submit" class="btn btn-primary" value="查询">
				</div>
				</shiro:hasPermission>
				<input type="hidden" id="page" name="page"> 
			</form>
			<hr>
			<table id="invite-table"
				class="table table-striped table-bordered table-condensed table-hover"
				style="margin-top: 20px;">
				<thead>
					<tr class="success">
						<th>被邀请人手机号码</th>
						<th>被邀请人姓名</th>
						<th>被邀请人注册时间</th>
						<th>被邀请人累计投资金额（元）</th>
						<th>被邀请人在投金额（元）</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
				<tfoot>
                </tfoot>
			</table>
           <div class="m-pagination text-center" id="pagination_tab4"></div>
		</div>
	</div>
</div>
<script id="invite-template" type="text/x-jsrender">
    <tr>
        <td>
            {{:phoneNum}}
         </td>
         <td>
             {{:userName}}
         </td>
         <td>
             {{:regTime}}
         </td>
         <td>
             {{:hasBeenInvest == null ? 0 : hasBeenInvest}}
         </td>
         <td>
             {{:investingMoney == null ? 0 : investingMoney}}
         </td>
    </tr>
</script>
<script type="application/javascript">
    $(function ($) {
        $('#invitecontainer').parent().on('tab.show', function (e) {
            $("#pagination_tab4").pagination({
                showFirstLastBtn: true,
                firstBtnText: '首页',
                lastBtnText: '尾页',
                prevBtnText: '上一页',
                nextBtnText: '下一页',
                showInfo: true,
                showJump: false,
                jumpBtnText: '跳转',
                showPageSizes: false,
                pageSize: 10,
                infoFormat: '{start} ~ {end}条，共{total}条',
                noInfoText: '无记录',
                remote: {
                    params: function () {
                        //return $('#parent_form').data('query');
                    	var param1 = $('#parent_form').data('query');
                    	var param2 = $('#inviteform').serializeObject();
                    	var param = $.extend({},param1,param2);
                    	return param;
                    }(),
                    url: '${path}/cs/account/invite',
                    beforeSend: function (XMLHttpRequest) {
                    	var index = layer.load(0, {
                            shade: [0.4,'#fff',false] //0.1透明度的白色背景
                        });
                    },
                    totalName: 'total',
                    pageSizeName: 'limit',
                    pageIndexName: 'pageNum',
                    success: function (data, pageIndex) {
                    	if(data.list!=null){
                    		$("#invite-table tbody").empty().html($("#invite-template").render(data.list));
                    	}
                    },
                    complete: function (XMLHttpRequest, textStatu) {
                        layer.closeAll();
                    }
                }
            }).on("pageClicked", function (event, pageIndex) {
            }).on('jumpClicked', function (event, pageIndex) {
            }).on('pageSizeChanged', function (event, pageSize) {
            });
            $(this).data('pageInit',true);
        });

        $('#inviteform').submit(function () {
		    var staTime = $("#inviteformStartDate").val();
		    var endTime = $("#inviteformEndDate").val();
		    if(staTime != '' && endTime != ''){
		        var d1 = new Date(staTime.replace(/-/g,"/"));
		        var d2 = new Date(endTime.replace(/-/g,"/"));
		        
		        var beginDateSplit = staTime.split('-');
		        var endDateSplit = endTime.split('-');
		        var beginDateMonths = parseInt(beginDateSplit[0]) * 12 + parseInt(beginDateSplit[1]);
		        var endDateMonths = parseInt(endDateSplit[0]) * 12 + parseInt(endDateSplit[1]);
		        
		        if (Date.parse(d1) - Date.parse(d2) > 0) {
                    bootbox.alert({message:"<h3>【结束时间不能早于开始时间】</h3>"});
		        }
		        /* else if(endDateMonths - beginDateMonths > 6){
                    bootbox.alert({message:"<h3>【跨度月数不能大于6个月】</h3>"});
		        } */
			    else{
			    	$('#pagination_tab4').trigger('tab.page');
			    }
		    }
		    else{
                bootbox.alert({message:"<h3>【查询日期不能为空】</h3>"});
		    }
            return false;
        });
        
        $('#pagination_tab4').on('tab.page',function() {
            var param1 = $('#parent_form').data('query')
            if(!param1) {
                return;
            }
            var param2 = $('#inviteform').serializeObject();
            var tab = $('#invitecontainer').parent();
            if(tab.data('pageInit')) {
                var param = $.extend({},param1,param2);
                $(this).pagination('setParams', param);
                $(this).pagination('setPageIndex', 0);
                $(this).pagination('remote');
            } else {
                tab.trigger('tab.show');
            }
        });
    });
</script>