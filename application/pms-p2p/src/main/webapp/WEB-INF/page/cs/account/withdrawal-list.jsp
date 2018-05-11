<%--
  Created by IntelliJ IDEA.
  User: bogle
  Date: 2015/11/10
  Time: 15:28
  To change this template use File | Settings | File Templates.
--%>
<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div class="">
    <div class="col-md-12" id="withdrawal_container1" style="margin-top: 20px;">
        <div class="col-md-12" style="margin-bottom: 10px;">
            <form class="form-inline form-search" role="form" id="withdrawal_query">
                <shiro:hasPermission name="withdrawTab:search">
                <div class="form-group">
                    <label class="control-label">提现日期：</label>
                </div>
                <div class="form-group">
                    <input class="form-control" id="withdrawFormStartTime" name="startTime" readonly="readonly" maxlength="30" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/> --
                </div>
                <div class="form-group">
                    <input class="form-control" id="withdrawFormEndTime" name="endTime" readonly="readonly" maxlength="30" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
                </div>
                    <button type="submit" class="btn btn-primary">查询</button>
                </shiro:hasPermission>
            </form>
            <hr/>
        </div>
        <div class="col-md-10">
            <table id="withdrawal-table" class="table table-striped table-bordered table-hover table-condensed">
                <thead>
                <tr class="success">
                    <th>提现时间</th>
                    <th>到账时间</th>
                    <th>提现金额</th>
                    <th>手续费</th>
                    <th>状态</th>
                    <th>提现银行</th>
                    <th>银行卡号</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
            <div class="m-pagination text-center" id="pagination_tab3"></div>
        </div>
    </div>
</div>

<script id="withdrawal-template" type="text/x-jsrender">
    <tr>
        <td>
            {{:tradeTime}}
         </td>
         <td>
            {{:finishTime}}
         </td>
         <td>
            {{:tradeAmount}}
         </td>
         <td>
             {{:handleAmount}}
         </td>
         <td>
             {{:status}}
         </td>
         <td>
             {{:bankName}}
         </td>
         <td>
             {{:bankShortNum}}
         </td>
    </tr>
</script>
<script type="application/javascript">
    $(function($) {
        $('#withdrawal_container1').parent().on('tab.show',function(e) {
            $("#pagination_tab3").pagination({
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
                        var param1 = $('#parent_form').data('query');
                        var param2 = $('#withdrawal_query').serializeObject();
                        return $.extend({},param1,param2);
                    }(),
                    url: '${path}/transaction/withdrawal/list',
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
                            $("#withdrawal-table tbody").empty().html($("#withdrawal-template").render(data.list));
                    	}
                    } ,
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

        $('#withdrawal_query').submit(function () {
		    var staTime = $("#withdrawFormStartTime").val();
		    var endTime = $("#withdrawFormEndTime").val();
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
			    	$('#pagination_tab3').trigger('tab.page');
			    }
		    }
		    else{
		    	bootbox.alert({message:"<h3>【查询日期不能为空】</h3>"});
		    }
            return false;
        });

        $('#pagination_tab3').on('tab.page',function() {
            var param1 = $('#parent_form').data('query');
            if(!param1) {
                return;
            }
            var param2 = $('#withdrawal_query').serializeObject();
            var tab = $('#withdrawal_container1').parent();
            if(tab.data('pageInit')) {
                $(this).pagination('setPageIndex', 0);
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

