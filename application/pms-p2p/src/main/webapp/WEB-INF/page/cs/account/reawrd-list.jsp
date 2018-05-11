<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<script type="text/javascript" src="/static/component/JqueryPagination/moment.min.js"></script>
<div class="">
    <div class="col-md-12" id="reawrd_container" style="margin-top: 20px;">
        <div class="col-md-12" style="margin-bottom: 10px;">
            <form class="form-inline form-search" role="form" id="reawrd_query">
                <div class="row">
                    <div class="col-md-12">
                        <shiro:hasPermission name="rewardTab:search">
                            <div class="form-group">
                                <label class="control-label">奖励发放日期：</label>
                            </div>
                            <div class="form-group">
                                <input class="form-control" id="reawrdFormStartTime" name="startTime" readonly="readonly" maxlength="30" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/> --
                            </div>
                            <div class="form-group">
                                <input class="form-control" id="reawrdFormEndTime" name="endTime" readonly="readonly" maxlength="30" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
                            </div>
                            <div class="form-group">
                                <label class="control-label">奖励类型:</label>
                                <select class="form-control" name="awardType" id="awardType" onchange="showHideDiv(this);">
                                    <option value="0" selected>返现券</option>
                                    <option value="1">现金</option>
                                    <option value="2">体验金</option>
                                    <option value="3">加息券</option>
                                </select>
                            </div>
                            <%--<input type="hidden" name="userId" id="userId" value="${userDetailInfo.userId}" />--%>
                            <button type="submit" class="btn btn-primary">查询</button>
                        </shiro:hasPermission>
                    </div>
                </div>

                <div class="row" style="margin-top: 10px;">
                    <div class="col-md-12">
                        <div class="form-group" id="reawrdDivStatus">
                            <label class="control-label">状态:</label>
                            <select class="form-control" name="cashBackStatus" id="cashBackStatus">
                                <option value="0" selected>全部</option>
                                <option value="1">未使用</option>
                                <option value="2">已使用</option>
                                <option value="3">过期</option>
                            </select>
                        </div>
                        <div class="form-group" id="reawrdDivMoney">
                            <label>金额：</label>
                            <input value="" type="text" cssClass="form-control  input-sm" id="minInvestMoney" name="minInvestMoney" />
                            --
                            <input type="text" cssClass="form-control input-sm" id="maxInvestMoney" name="maxInvestMoney" />
                        </div>
                    </div>
                </div>
            </form>
            <hr/>
        </div>

        <div class="col-md-8">
            <table id="reward-table-fxq" class="table table-striped table-bordered table-hover" style="display:block;">
                <thead>
                <tr class="success">
                    <th>获取时间</th>
                    <th>激活时间</th>
                    <th>返现券代码</th>
                    <th>金额</th>
                    <th>激活门槛</th>
                    <th>有效期</th>
                    <th>产品类型</th>
                    <th>投资期限</th>
                    <th>状态</th>
                    <th>来源</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
            <table id="reward-table-xj" class="table table-striped table-bordered table-hover" style="display: none">
                <thead>
                <tr class="success">
                    <th>发送时间</th>
                    <th>金额（元）</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
            <table id="reward-table-tyj" class="table table-striped table-bordered table-hover" style="display: none">
                <thead>
                <tr class="success">
                    <th>发送时间</th>
                    <th>金额（元）</th>
                    <th>利息</th>
                    <th>年化</th>
                    <th>有效期(天)</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
            <table id="reward-table-jxz" class="table table-striped table-bordered table-hover" style="display: none">
                <thead>
                <tr class="success">
                    <th>获取时间</th>
                    <th>加息券代码</th>
                    <th>加息幅度</th>
                    <th>投资限额(元)</th>
                    <th>有效期(天)</th>
                    <th>投资期限(天)</th>
                    <th>产品类型</th>
                    <th>状态</th>
                    <th>获取渠道</th>
                    <th>激活时间</th>
                    <th>标的名称</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
            <div class="m-pagination text-center" id="pagination_tab2"></div>
        </div>
    </div>
</div>

<script id="reawrd-template-fxq" type="text/x-jsrender">
    <tr>
        <td>{{formatDate:createTime}}</td>
        <td>{{if status === 2}}{{formatDate:updateTime}}{{/if}}</td>
        <td>{{:activityCode}}</td>
        <td>{{:redMoney}}</td>
        <td>{{:investMoney}}</td>
        <td>{{formatDay:validTime}}</td>
        <td>暂无</td>
        <td>暂无</td>
        <td>{{if status === 2}}
              已使用
            {{else status === 3}}
              已过期
            {{else}}
              未使用
            {{/if}}
        </td>
        <td>{{:remarks}}</td>
    </tr>
</script>
<script id="reawrd-template-xj" type="text/x-jsrender">
    <tr>
        <td>
            {{formatDate:reawrdTime}}
         </td>
         <td>
             {{:reawrdMoney}}
         </td>
    </tr>
</script>

<script id="reawrd-template-tyj" type="text/x-jsrender">
    <tr>
        <td>
            {{formatDate:reawrdTime}}
         </td>
         <td>
             {{:experienceGold}}
         </td>
         <td>
             {{:reawrdMoney}}
         </td>
         <td>
             {{:yearYield}}%
         </td>
         <td>
             {{:effectDay}}
         </td>
    </tr>
</script>

<script id="reawrd-template-jxz" type="text/x-jsrender">
    <tr>
        <td>
            {{formatDate:createTime}}
         </td>
         <td>
             {{:couponCode}}
         </td>
         <td>
             {{:scope}}%
         </td>
         <td>
             {{:minInvestMoney}} - {{:maxInvestMoney}}
         </td>
         <td>
             {{:effectDay}}天
         </td>
         <td>
             {{if minInvestDay > 0 && maxInvestDay > 0}}
                {{:minInvestDay}}-{{:maxInvestDay}}天
             {{else}}
                不限
             {{/if}}
         </td>
         <td>
             {{:bidTypeStr}}
         </td>
         <td>
            {{if couponStatus === 1}}
                未使用
            {{else}}
                已使用
            {{/if}}
         </td>
         <td>
            {{:channelName}}
         </td>
         <td>
            {{if couponStatus === 1}}
                未使用
            {{else couponStatus === 2}}
                {{formatDate:activateTime}}
            {{else}}
                —
            {{/if}}
         </td>
         <td>
            {{if couponStatus === 1}}

            {{else couponStatus === 2}}
                {{:bidName}}
            {{else}}
                —
            {{/if}}
         </td>
    </tr>
</script>
<script type="application/javascript">
    $(function ($) {
        $('#reawrd_container').parent().on('tab.show', function (e) {
            $("#pagination_tab2").pagination({
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
                        var param2 = $('#reawrd_query').serializeObject();
                        return $.extend({},param1,param2);
                    }(),
                    url: '${path}/transaction/reawrd/list',
                    beforeSend: function (XMLHttpRequest) {
                    	var index = layer.load(0, {
                            shade: [0.4,'#fff',false] //0.1透明度的白色背景
                        });
                    },
                    totalName: 'total',
                    pageSizeName: 'limit',
                    pageIndexName: 'pageNum',
                    success: function (data, pageIndex) {
                        var list = data.list;
                        /*var reawrdRecord;
                        var reawrdType;
                        for(var i = 0; i < list.length; i++) {
                            reawrdRecord = list[i];
                            reawrdType = reawrdRecord.reawrdType;
                            if(reawrdType == 1) {
                                reawrdType = "返现红包";
                            } else if(reawrdType == 2) {
                                reawrdType = "现金红包";
                            } else {
                                reawrdType = "体验金利息";
                            }
                            reawrdRecord.reawrdType = reawrdType;
                        }*/
                        var reawrdType = $("#awardType").val();
                        if(reawrdType == '0'){    //返现券
                            $("#reward-table-xj").attr('style', 'display:none');//隐藏;
                            $("#reward-table-tyj").attr('style', 'display:none');
                            $("#reward-table-jxz").attr('style', 'display:none');
                            $("#reward-table-fxq").attr('style', 'display:');//显示;
                            $("#reward-table-fxq tbody").empty().html($("#reawrd-template-fxq").render(list));
                        }else if(reawrdType == '1'){   //现金
                            $("#reward-table-fxq").attr('style', 'display:none');
                            $("#reward-table-tyj").attr('style', 'display:none');
                            $("#reward-table-jxz").attr('style', 'display:none');
                            $("#reward-table-xj").attr('style', 'display:');
                            $("#reward-table-xj tbody").empty().html($("#reawrd-template-xj").render(list));
                        }else if(reawrdType == '2'){   //体验金
                            $("#reward-table-fxq").attr('style', 'display:none');
                            $("#reward-table-xj").attr('style', 'display:none');
                            $("#reward-table-jxz").attr('style', 'display:none');
                            $("#reward-table-tyj").attr('style', 'display:');
                            $("#reward-table-tyj tbody").empty().html($("#reawrd-template-tyj").render(list));
                        }else if(reawrdType == '3'){   //加息券
                            $("#reward-table-fxq").attr('style', 'display:none');
                            $("#reward-table-xj").attr('style', 'display:none');
                            $("#reward-table-tyj").attr('style', 'display:none');
                            $("#reward-table-jxz").attr('style', 'display:');
                            $("#reward-table-jxz tbody").empty().html($("#reawrd-template-jxz").render(list));
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

        $('#reawrd_query').submit(function () {
		    var staTime = $("#reawrdFormStartTime").val();
		    var endTime = $("#reawrdFormEndTime").val();
		    if(staTime != '' && endTime != ''){
		        var d1 = new Date(staTime.replace(/-/g,"/"));
		        var d2 = new Date(endTime.replace(/-/g,"/"));
		        if (Date.parse(d1) - Date.parse(d2) > 0) {
                    bootbox.alert({message:"<h3>【结束时间不能早于开始时间】</h3>"});
		        }
			    else{
			    	$('#pagination_tab2').trigger('tab.page');
			    }
		    }
		    else{
		    	$('#pagination_tab2').trigger('tab.page');
		    }

            var minInvestMoney =  $.trim($("#minInvestMoney").val());
            var maxInvestMoney = $.trim($("#maxInvestMoney").val());
            var reg = /^\d+(\.\d+)?$/;
            if(minInvestMoney != ""){
                if(!reg.test(minInvestMoney))
                {
                    bootbox.alert({size: 'small',message:"累计投资金额只能输入数字！！！"});
                }else {
                    $('#pagination_tab2').trigger('tab.page');
                }
            }else {
                $('#pagination_tab2').trigger('tab.page');
            }
            if(maxInvestMoney != ""){
                if(!reg.test(maxInvestMoney))
                {
                    bootbox.alert({size: 'small',message:"累计投资金额只能输入数字！！！"});
                }else {
                    $('#pagination_tab2').trigger('tab.page');
                }
            }else {
                $('#pagination_tab2').trigger('tab.page');
            }
            return false;
        });

        $('#pagination_tab2').on('tab.page',function() {
            var param1 = $('#parent_form').data('query');
            if(!param1) {
                return;
            }
            var param2 = $('#reawrd_query').serializeObject();
            var tab = $('#reawrd_container').parent();
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
    
    function showHideDiv() {

        var reawrdSelect = $("#awardType").val();
        if(reawrdSelect == '0'){
            $('#reawrdDivStatus').attr('style', 'display:');
            $('#reawrdDivMoney').attr('style', 'display:');
        }else {
            $('#reawrdDivStatus').attr('style', 'display:none');
            $('#reawrdDivMoney').attr('style', 'display:none');
        }
    }

    $.views.converters({
        // jsrender converters 格式化日期
        formatDate: function (value) {
            if(value != null){
                return moment(value).format("YYYY-MM-DD HH:mm");
            }
        },
        formatDay: function (value) {
            if(value != null){
                return moment(value).format("YYYY-MM-DD");
            }
        }
    });
</script>

