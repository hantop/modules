<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div class="" id="incomecontainer">
    <div class="col-md-12"  style="margin-top: 20px;">
        <div class="col-md-12" style="margin-bottom: 10px;">
			<form class="form-inline" method="post"
				action="${path}/cs/account/income" id="incomeform">
				<shiro:hasPermission name="incomeTab:search">
			    <div class="form-group">
                    <label>收益日期：</label>
                    <input type="text" class="form-control" id="incomeformStartDate" name="startDate" readonly="readonly" maxlength="20" class="form-control input-sm" style="width:163px;"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"> --
                    <input type="text" class="form-control" id="incomeformEndDate" name="endDate" readonly="readonly" maxlength="20" class="form-control input-sm" style="width:163px;"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});">
                </div>
                
				<div class="form-group">
					<input type="submit" class="btn btn-primary" value="查询">
				</div>
				</shiro:hasPermission>
				<input type="hidden" id="page" name="page"> 
			</form>
			<hr>
			<table id="income-table"
				class="table table-striped table-bordered table-condensed table-hover"
				style="margin-top: 20px;">
				<thead>
					<tr class="success">
						<th>收益时间</th>
						<th>投资收益（元）</th>
						<th>收益来源</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
				<tfoot>
                </tfoot>
			</table>
           <div class="m-pagination text-center" id="pagination_tab5"></div>
		</div>
	</div>
</div>
<script id="income-template" type="text/x-jsrender">
    <tr>
        <td>
            {{:time}}
         </td>
         <td>
             {{:income}}
         </td>
         <td>
             {{:source}}
         </td>
    </tr>
</script>
<script type="application/javascript">
    $(function ($) {
        $('#incomecontainer').parent().on('tab.show', function (e) {
            $("#pagination_tab5").pagination({
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
                    	var param2 = $('#incomeform').serializeObject();
                    	var param = $.extend({},param1,param2);
                    	return param;
                    }(),
                    url: '${path}/cs/account/income',
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
                    		$("#income-table tbody").empty().html($("#income-template").render(data.list));
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

        $('#incomeform').submit(function () {
		    var staTime = $("#incomeformStartDate").val();
		    var endTime = $("#incomeformEndDate").val();
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
		        //不做半年时间控制
		        /* else if(endDateMonths - beginDateMonths > 6){
                    bootbox.alert({message:"<h3>【跨度月数不能大于6个月】</h3>"});
		        } */
			    else{
			    	$('#pagination_tab5').trigger('tab.page');
			    }
		    }
		    else{
                bootbox.alert({message:"<h3>【查询日期不能为空】</h3>"});
		    }
            return false;
        });
        
        $('#pagination_tab5').on('tab.page',function() {
            var param1 = $('#parent_form').data('query');
            if(!param1) {
                return;
            }
            var param2 = $('#incomeform').serializeObject();
            var tab = $('#incomecontainer').parent();
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