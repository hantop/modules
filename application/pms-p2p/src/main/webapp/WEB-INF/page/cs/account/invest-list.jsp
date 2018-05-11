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
    <div class="col-md-12" id="invest_container1" style="margin-top: 20px;">
        <div class="col-md-12" style="margin-bottom: 10px;">
            <form class="form-inline form-search" role="form" id="invest_query">
                <shiro:hasPermission name="investTab:search">
                <div class="form-group">
                    <label class="control-label">标的名称：</label>
                </div>
                <div class="form-group">
                    <input class="form-control" id="investFormTitle" name="title" maxlength="30" />
                </div>
                <div class="form-group">
                    <label class="control-label">投资日期：</label>
                </div>
                <div class="form-group">
                    <input class="form-control" id="investFormStartTime" name="startTime" readonly="readonly" maxlength="30" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/> --
                </div>
                <div class="form-group">
                    <input class="form-control" id="investFormEndTime" name="endTime" readonly="readonly" maxlength="30" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
                </div>
                <div class="form-group">
                    <label class="control-label">动作：</label>
                    <select id="choseId" name="sold" class="form-control">
        	            <option value="0" >买入</option>
        	            <option value="1" >卖出</option>
                    </select>
                </div>
                &nbsp;&nbsp;
                    <button type="submit" class="btn btn-primary">查询</button>
                </shiro:hasPermission>
            </form>
            <hr/>
        </div>
        <div class="col-md-12" id = "buy">
            <table id="invest-table" class="table table-striped table-bordered table-hover table-condensed">
                <thead>
                <tr class="success">
                    <th>投资时间</th>
                    <th>计息时间</th>
                    <th>到期时间</th>
                    <th>债权本金</th>
                    <th>投资金额</th>
                    <th>债权利息</th>
                    <th>实际收益</th>
                    <th>投资产品</th>
                    <th>状态</th>
                    <th>年利率</th>
                    <th>期限</th>
                    <th>还款方式</th>
                    <th>标的类型</th>
                    <th>投资券</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
                <tfoot>
                     <tr>
                        <td>总计</td>
                        <td></td>
                        <td></td>
                        <td>
                        	<span>${totalRedNumber}</span><!-- 总发送数量 -->
                        </td>
                        <td >
                            <span>${totalActiveCount}</span><!-- 总激活数量 -->
                        </td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                   </tr>
                </tfoot>
            </table>
            <table style = "display: none" id="invest-table-sold" class="table table-striped table-bordered table-hover table-condensed">
                <thead>
                <tr class="success">
                    <th>投资时间</th>
                    <th>计息时间</th>
                    <th>卖出时间</th>
                    <th>债权本金</th>
                    <th>卖出金额</th>
                    <th>实际收益</th>
                    <th>标的名称</th>
                    <th>年利率</th>
                    <th>期限</th>
                    <th>标的类型</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
                <tfoot>
                </tfoot>
            </table>
            <div class="m-pagination text-center" id="pagination_tab7"></div>
        </div>
    </div>
</div>

<script id="invest-template" type="text/x-jsrender">
    <tr>
        <td>
            {{:tradeTime}}
         </td>
         <td>
            {{:sealedTime}}
         </td>
         <td>
            {{:fullTime}}
         </td>
         <td>
            {{:originMoney}}
         </td>
         <td>
            {{:tradeAmount}}
         </td>
         <td>
            {{if origenRate > 0 }}
                {{:origenRate}}
                    {{if bidInterestSum > 0 }}
                        + {{:bidInterestSum}}
                            {{if interestSum > 0 }}
                                + {{:interestSum}}
                            {{/if}}
                    {{else interestSum > 0}}
                        + {{:interestSum}}
                    {{/if}}
            {{/if}}
         </td>
         <td>
             {{:acturalEarn}}
         </td>
         <td>
             {{:name}}
         </td>
         <td>
             {{:status}}
         </td>
         <td>
             {{:rateStr}}
         </td>
         <td>
             {{if loanDays > 0 }}
                {{:loanDays}}天
                {{else month > 0}}
                    {{:month}}个月
                 {{else}}
                    未知错误
             {{/if}}
         </td>
         <td>
             {{:paymentType}}
         </td>
		 <td>
             {{:bidType}}
         </td>
         <td>
            {{if bidScope > 0 }}
                标加息{{:bidScope}}%
                    {{if scope > 0 }}
                        ,加息券{{:scope}}%
                    {{else redPacketMoney > 0}}
                        ,返现{{:redPacketMoney}}元
                    {{/if}}
            {{else scope > 0}}
                加息券{{:scope}}%
                    {{if redPacketMoney > 0 }}
                        ,返现{{:redPacketMoney}}元
                    {{/if}}
            {{else redPacketMoney > 0}}
                返现{{:redPacketMoney}}元
            {{else }}
                -
            {{/if}}
         </td>
    </tr>
</script>
<script id="invest-template-foot" type="text/x-jsrender">
    <tr>
        <td>
			总计
         </td>
         <td>
         </td>
         <td>
         </td>
         <td>
            {{:origrienMoneyTotal}}
         </td>
         <td>
            {{:investMoneyTotal}}
         </td>
         <td>
         </td>
         <td>
         </td>
         <td>
         </td>
         <td>
         </td>
         <td>
         </td>
         <td>
         </td>
         <td>
         </td>
         <td>
         </td>
		 <td>
         </td>
    </tr>
</script>
<script id="invest-template-sold" type="text/x-jsrender">
    <tr>
        <td>
            {{:tradeTime}}
         </td>
         <td>
            {{:sealedTime}}
         </td>
		 <td>
            {{:fullTime}}
         </td>
         <td>
            {{:originMoney}}
         </td>
         <td>
            {{:saleMoney}}
         </td>
         <td>
            {{:acturalEarn}}
         </td>
         <td>
             {{:name}}
         </td>
         <td>
             {{:rateStr}}
         </td>
         <td>
             {{if loanDays > 0 }}
                {{:loanDays}}天
                {{else month > 0}}
                    {{:month}}个月
                 {{else}}
                    未知错误
             {{/if}}

         </td>
		 <td>
             {{:bidType}}
         </td>
    </tr>
</script>
<script id="invest-template-sold-foot" type="text/x-jsrender">
    <tr>
        <td>
            	总计
         </td>
         <td>
         </td>
		 <td>
         </td>
         <td>
            {{:origrienMoneyTotal}}
         </td>
         <td>
            {{:investMoneyTotal}}
         </td>
         <td>
         </td>
         <td>
         </td>
         <td>
         </td>
         <td>
         </td>
		 <td>
         </td>
    </tr>
</script>
<script type="application/javascript">
    $(function($) {
        $('#invest_container1').parent().on('tab.show',function(e) {
            $("#pagination_tab7").pagination({
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
                        var param2 = $('#invest_query').serializeObject();
                        return $.extend({},param1,param2);
                    }(),
                    url: '${path}/transaction/invest/list',
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
                        	if ($('#choseId option:selected').val() == 0) {
                        		$("#invest-table").css('display','table');
                        		$("#invest-table-sold").css('display','none');
                             $("#invest-table tbody").empty().html($("#invest-template").render(data.list));
                             if (data.list[0] != null) {
                             	$("#invest-table tfoot").empty().html($("#invest-template-foot").render(data.list[0]));
							}else {
								$("#invest-table tfoot").empty().html($("#invest-template-foot").render(data.list));
							}
							}else {
								$("#invest-table").css('display','none');
								$("#invest-table-sold").css('display','table');
								$("#invest-table-sold tbody").empty().html($("#invest-template-sold").render(data.list));
								if (data.list[0] != null) {
									$("#invest-table-sold tfoot").empty().html($("#invest-template-sold-foot").render(data.list[0]));
								}else {
								$("#invest-table-sold tfoot").empty().html($("#invest-template-sold-foot").render(data.list));
								}
							}
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

        $('#invest_query').submit(function () {
		    var staTime = $("#investFormStartTime").val();
		    var endTime = $("#investFormEndTime").val();
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
			    	$('#pagination_tab7').trigger('tab.page');
			    }
		    }
		    else{
		    	bootbox.alert({message:"<h3>【查询日期不能为空】</h3>"});
		    }
            return false;
        });

        $('#pagination_tab7').on('tab.page',function() {
            var param1 = $('#parent_form').data('query');
            if(!param1) {
                return;
            }
            var param2 = $('#invest_query').serializeObject();
            var tab = $('#invest_container1').parent();
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

