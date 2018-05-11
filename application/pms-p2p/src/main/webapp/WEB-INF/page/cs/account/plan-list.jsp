<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div class="">
    <div class="col-md-12" id="plan_container1" style="margin-top: 20px;">
        <div class="col-md-12" style="margin-bottom: 10px;">
            <form class="form-inline form-search" role="form" id="plan_query">
                <%--<shiro:hasPermission name="planTab:search">--%>
                <div class="form-group">
                    <label class="control-label">名称：</label>
                </div>
                <div class="form-group">
                    <input class="form-control" id="planFormTitle" name="title" maxlength="30" />
                </div>
                &nbsp;&nbsp;
                    <button type="submit" class="btn btn-primary">查询</button>
               <%-- </shiro:hasPermission>--%>
            </form>
            <hr/>
        </div>
        <div class="col-md-12">
            <table id="plan-table" class="table table-striped table-bordered table-hover table-condensed">
                <thead>
                <tr class="success">
                    <th>名称</th>
                    <th>投资时间</th>
                    <th>计息时间</th>
                    <th>到期时间</th>
                    <th>投资金额</th>
                    <th>年利率</th>
                    <th>计划期限</th>
                    <th>投资收益</th>
                    <th>奖励</th>
                    <th>状态</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
            <div class="m-pagination text-center" id="pagination_tab8"></div>
        </div>
    </div>
</div>

<script id="plan-template" type="text/x-jsrender">
    <tr>
        <td>
            {{:name}}
         </td>
         <td>
            {{:investTime}}
         </td>
         <td>
            {{:bearrateDate}}
         </td>
         <td>
            {{:expireTime}}
         </td>
         <td>
            {{:tradeAmount}}
         </td>
         <td>
            {{if type === 2 }}
                {{:investRate}}%
                {{else type === 1}}
                    {{:minYearlyRate}}% - {{:maxYearlyRate}}%
                 {{else}}
                    未知错误
             {{/if}}
         </td>
         <td>
            {{if cycleType === 'd' }}
                {{:cycle}}天
                {{else cycleType === 'm'}}
                    {{:cycle}}个月
                 {{else}}
                    未知错误
             {{/if}}
         </td>
         <td>
            {{if origenRate > 0 }}
                {{:origenRate}}
                    {{if planInterate > 0 }}
                        + {{:planInterate}}
                            {{if interest > 0 }}
                                + {{:interest}}
                                    {{if paymentInterate > 0 }}
                                        + {{:paymentInterate}}
                                    {{else penalty > 0}}
                                        + {{:penalty}}
                                    {{/if}}
                            {{/if}}
                    {{else interest > 0}}
                        + {{:interest}}
                            {{if paymentInterate > 0 }}
                                + {{:paymentInterate}}
                            {{else penalty > 0}}
                                + {{:penalty}}
                            {{/if}}
                    {{else}}
                        {{if paymentInterate > 0 }}
                            + {{:paymentInterate}}
                        {{else penalty > 0}}
                            + {{:penalty}}
                        {{/if}}
                    {{/if}}
            {{/if}}
         </td>
         <td>
            {{if bidScope > 0 }}
                标加息{{:bidScope}}%
                    {{if scope > 0 }}
                        ,加息券{{:scope}}%
                    {{/if}}
                    {{if redPacketMoney > 0 }}
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
         <td>
            {{if status === "4" || status === "TBZ"}}
                投资中
            {{else status === "5" || status === "HKZ"}}
                收益中
            {{else status === "6" || status === "YJQ"}}
                已结清
            {{else status === "DFK"}}
                待放款
            {{else}}
                {{:status}}
            {{/if}}
         </td>
         <td>
             <a href="${path}/transaction/plan-detail?userId={{:userId}}&planType={{:planType}}&planId={{:planId}}&recordId={{:recordId}}">查看</a>
         </td>
    </tr>
</script>
<script type="application/javascript">
    $(function($) {
        $('#plan_container1').parent().on('tab.show',function(e) {
            $("#pagination_tab8").pagination({
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
                        var param2 = $('#plan_query').serializeObject();
                        return $.extend({},param1,param2);
                    }(),
                    url: '${path}/transaction/plan/list',
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
                             $("#plan-table tbody").empty().html($("#plan-template").render(data.list));
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

        $('#plan_query').submit(function () {
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
			    else{
			    	$('#pagination_tab8').trigger('tab.page');
			    }
		    }
            return false;
        });

        $('#pagination_tab8').on('tab.page',function() {
            var param1 = $('#parent_form').data('query');
            if(!param1) {
                return;
            }
            var param2 = $('#plan_query').serializeObject();
            var tab = $('#plan_container1').parent();
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

