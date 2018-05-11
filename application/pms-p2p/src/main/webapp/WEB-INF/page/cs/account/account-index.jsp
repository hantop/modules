<%--
  Created by IntelliJ IDEA.
  User: bogle
  Date: 2015/11/10
  Time: 15:28
  To change this template use File | Settings | File Templates.
--%>
<%@page language="java" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<link href="/static/component/JqueryPagination/mricode.pagination.css" rel="stylesheet">

<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<script type="text/javascript" src="/static/component/jquery-form/jquery.form.js"></script>
<script type="text/javascript" src="/static/component/JqueryPagination/mricode.pagination.js"></script>
<script type="text/javascript" src="/static/component/JqueryPagination/jsrender.js"></script>
<script type="text/javascript" src="/static/component/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/static/component/datepicker/calendar.js"></script>
<script type="application/javascript">
    (function ($) {
        $.fn.serializeObject = function () {
            "use strict";

            var result = {};
            var extend = function (i, element) {
                var node = result[element.name];

                // If node with same name exists already, need to convert it to an array as it
                // is a multi-value field (i.e., checkboxes)
                if ($.trim(element.value) !== "") {
                    if ('undefined' !== typeof node && node !== null) {
                        if ($.isArray(node)) {
                            node.push(element.value);
                        } else {
                            result[element.name] = [node, element.value];
                        }
                    } else {
                        result[element.name] = element.value;
                    }
                }
            };
            $.each(this.serializeArray(), extend);
            return result;
        };
    })(jQuery);

    Array.prototype.contains = function (obj) {
        var i = this.length;
        while (i--) {
            if (this[i] === obj) {
                return true;
            }
        }
        return false;
    }
</script>

<style type="text/css">
    /**去除bootstrap td 的边框*/
    table.account tr td {
        border: none;
    }
</style>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/cs/index">客户管理</a></li>
                <li class="active"><b>用户信息</b></li>
            </ol>

            <form class="form-inline" method="post" action="${path}/transaction/detailInfo"  id="parent_form" style="display:none">
                <shiro:hasPermission name="accountInfo:search">
                <div class="form-group">
                    <label class="control-label">用户手机号</label>
                </div>
                <div class="form-group">
                    <input path="phoneNum" id="phoneNum" name="phoneNum" value=""/>
                </div>
                    <input type="hidden" name="userId" id="userId" value="" />
                <button type="submit" class="btn btn-primary btn-sm">查询</button>
                </shiro:hasPermission>
                <input type="hidden" name="operator" value="<shiro:principal/>" id="operator">
            </form>

        </div>
        <div class="col-md-12">
            <table class="table table-hover account table-condensed" style="margin-top: 8px;">
                <tr>
                    <td class="text-left">手机号：</td>
                    <td id="td_phoneNum"></td>
                    <td class="text-left">注册时间：</td>
                    <td id="td_registerTime"></td>
                    <td class="text-left">姓名：</td>
                    <td id="td_name"></td>
                    <td class="text-left">身份证号码：</td>
                    <td id="td_idcard"></td>
                </tr>
                <tr>
                    <td class="text-left">账号余额（元）：</td>
                    <td id="td_balance"></td>
                    <td class="text-left">在投金额（元）：</td>
                    <td id="td_investingMoney"></td>
                    <td class="text-left">实名认证：</td>
                    <td id="td_isAuth"></td>
                    <td class="text-left">是否绑卡：</td>
                    <td id="td_isBindcard"></td>
                </tr>
                <tr>
                    <td class="text-left">银行卡号：</td>
                    <td id="td_bankcardNum"></td>
                    <td class="text-left">邀请人手机号：</td>
                    <td id="td_spreadPhonenum"></td>
                    <td class="text-left">邀请人姓名：</td>
                    <td id="td_spreadName"></td>
                    <td class="text-left">渠道来源：</td>
                    <td id="td_channelName"></td>
                </tr>
            </table>
        </div>
        <c:set var="currentUser" value='<%= SecurityUtils.getSubject().getPrincipal().toString() %>'/>
        <div class="col-md-12">
            <ul class="nav nav-tabs">
                <shiro:hasPermission name="rechargeTab:view">
                    <li><a href="#tab1" data-toggle="tab">充值记录</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="rewardTab:view">
                    <li><a href="#tab2" data-toggle="tab">奖励记录</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="withdrawTab:view">
                    <li><a href="#tab3" data-toggle="tab">提现记录</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="inviteTab:view">
                    <li><a href="#tab4" data-toggle="tab">邀请记录</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="incomeTab:view">
                    <li><a href="#tab5" data-toggle="tab">收益记录</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="investTab:view">
                    <li><a href="#tab7" data-toggle="tab">投标记录</a></li>
                </shiro:hasPermission>
                <%--<shiro:hasPermission name="investTab:view">--%>
                    <li><a href="#tab8" data-toggle="tab">计划记录</a></li>
                <%--</shiro:hasPermission>--%>
                <shiro:hasPermission name="transactionTab:view">
                    <li><a href="#tab6" data-toggle="tab">交易流水</a></li>
                </shiro:hasPermission>
            </ul>
            <!-- 选项卡面板 -->
            <div id="tabContent" class="tab-content">
                <shiro:hasPermission name="rechargeTab:view">
                <div class="tab-pane fade" id="tab1">
                    <jsp:include page="recharge-list.jsp"/>
                </div>
                </shiro:hasPermission>
                
                <shiro:hasPermission name="rewardTab:view">
                <div class="tab-pane fade" id="tab2">
                    <jsp:include page="reawrd-list.jsp"/>
                </div>
                </shiro:hasPermission>
                
                <shiro:hasPermission name="withdrawTab:view">
                <div class="tab-pane fade" id="tab3">
                    <jsp:include page="withdrawal-list.jsp"/>
                </div>
                </shiro:hasPermission>
                
                <shiro:hasPermission name="inviteTab:view">
                <div class="tab-pane fade" id="tab4">
                    <jsp:include page="invite-list.jsp"/>
                </div>
                </shiro:hasPermission>
                
                <shiro:hasPermission name="incomeTab:view">
                <div class="tab-pane fade" id="tab5">
                    <jsp:include page="income-list.jsp"/>
                </div>
                </shiro:hasPermission>
                
                <shiro:hasPermission name="investTab:view">
                <div class="tab-pane fade" id="tab7">
                    <jsp:include page="invest-list.jsp"/>
                </div>
                </shiro:hasPermission>

                <%--<shiro:hasPermission name="investTab:view">--%>
                    <div class="tab-pane fade" id="tab8">
                        <jsp:include page="plan-list.jsp"/>
                    </div>
                <%--</shiro:hasPermission>--%>
                
                <shiro:hasPermission name="transactionTab:view">
                <div class="tab-pane fade" id="tab6">
                    <jsp:include page="transaction-list.jsp"/>
                </div>
                </shiro:hasPermission>
            </div>
        </div>
    </div>
</div>
<script type="application/javascript">
    $(function () {
        $.ajaxSetup({
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            complete: function (data, TS) {
                //console.info(arguments);
                //对返回的数据data做判断，
                //session过期的话，就location到一个页面
                if(TS !== 'success') {
                    window.location.reload();
                }
            }
        });
        
        	//获取地址栏中的参数
            var url = window.location.search;
            var loc = url.substring(url.lastIndexOf('=')+1, url.length);
            var userId;
            var myreg = /^(13[0-9]|14[0-9]|15[0-9]|165|17[0-9]|18[0-9])[0-9]{8}$/;
            if(!myreg.test(loc))
            {
                $("#userId").val(loc);
                userId = loc;
            }
        	 var index = layer.load(0, {
                shade: [0.4,'#fff',false] //0.1透明度的白色背景
            }); 
            var errorMessage = "查询失败，内部错误";
            var notFoundMessage = "此用户不存在";
            var $this = $('#parent_form');
            $.ajax({
                dataType: "json",
                type: "POST",
                url: path + "/transaction/detailInfo",
                data: {"userId": userId},
                error: function (jqXHR, textStatus, errorThrown) {
                    bootbox.alert({message: "<h3>" + errorMessage + "</h3>"});
                },
                success: function (data) {
                    $("#td_isBindcard").html('-');
                    $("#td_isAuth").html('-');
                    $("#td_phoneNum").html('-');
                    $("#td_registerTime").html('-');
                    $("#td_name").html('-');
                    $("#td_idcard").html('-');
                    $("#td_balance").html('-');
                    $("#td_investingMoney").html('-');
                    $("#td_bankcardNum").html('-');
                    $("#td_spreadPhonenum").html('-');
                    $("#td_spreadName").html('-');
                    $("#td_channelName").html('-');


                    var userId;
                    var spreadName;
                    var spreadPhonenum;
                    var bankcardNum;
                    var investingMoney;
                    var balance;
                    var idcard;
                    var name;
                    var registerTime;
                    var phoneNum;
                    var authStatus;
                    var bankcardStatus;
                    var channelName;
                    var resultCode = data.resultCode;
                    if (resultCode == "0000") {
                        var userDetailInfo = data.userDetailInfo;
                        userId = userDetailInfo.userId;
                        bankcardStatus = userDetailInfo.bankcardStatus;
                        authStatus = userDetailInfo.authStatus;
                        phoneNum = userDetailInfo.phoneNum;
                        registerTime = userDetailInfo.registerTime;
                        name = userDetailInfo.name;
                        idcard = userDetailInfo.idcard;
                        balance = userDetailInfo.balance;
                        investingMoney = userDetailInfo.investingMoney;
                        bankcardNum = userDetailInfo.bankcardNum;
                        spreadPhonenum = userDetailInfo.spreadPhonenum;
                        spreadName = userDetailInfo.spreadName;
                        channelName = userDetailInfo.channelName;
                        if (bankcardStatus == null || bankcardStatus == 'WRZ') {
                            bankcardStatus = "否";
                        } else {
                            bankcardStatus = "是";
                        }
                        if (authStatus == null || authStatus != 'TG') {
                            authStatus = "否";
                        } else {
                            authStatus = "是";
                        }
                        /*// 姓名第一个字后面加星号
                        if (name != null) {
                            name = name.substring(0, 1) + "**";
                        }*/
                        // 邀请人姓名第一个字后面加星号
                        if (spreadName != null) {
                            spreadName = spreadName.substring(0, 1) + "**";
                        }
                        // 邀请人手机号加星号
                        //if(spreadPhonenum != null) {
                        //    spreadPhonenum = spreadPhonenum.substring(0, 7) + "****";
                        //}
                        $($this).data('query', $($this).serializeObject());
                        var tab = $('.nav-tabs li.active > a');
                        if (tab.length == 0) {
                            tab = $('.nav-tabs a[href="#tab1"]');
                        }
                        if (!tab.data('clicked')) {
                            tab.tab('show');
                        } else {
                            var hash = tab.attr('href').substr(1);
                            $('#pagination_' + hash).trigger('tab.page');
                        }
                        $('#parent_form').data('loaded', [tab.attr('href')]);
                        layer.close();
                    } else if (resultCode == "1111") {
                        bootbox.alert({message: "<h3>" + notFoundMessage + "</h3>"});
                    } else {
                        bootbox.alert({message: "<h3>" + errorMessage + "</h3>"});
                    }

                    $("#userId").val(userId);
                    $("#td_isBindcard").html(bankcardStatus);
                    $("#td_isAuth").html(authStatus);
                    $("#td_phoneNum").html(phoneNum);
                    $("#phoneNum").val(phoneNum);
                    $("#td_registerTime").html(registerTime);
                    $("#td_name").html(name);
                    $("#td_idcard").html(idcard);
                    $("#td_balance").html(balance);
                    $("#td_investingMoney").html(investingMoney);
                    $("#td_bankcardNum").html(bankcardNum);
                    $("#td_spreadPhonenum").html(spreadPhonenum);
                    $("#td_spreadName").html(spreadName);
                    $("#td_channelName").html(channelName);
                },
                beforeSend: function (xhr) {
                    var index = layer.load(0, {
                        shade: [0.4,'#fff',false] //0.1透明度的白色背景
                    });
                },
                complete: function () {
                    layer.closeAll();
                }
            });

                layer.closeAll();
                
                
        $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
            // 获取已激活的标签页的名称
            var hash = e.target.hash;
            var loaded = $('#parent_form').data('loaded');
            if (loaded && loaded.contains(hash)) {
                return;
            } else {
                if (!loaded) {
                    loaded = new Array();
                }
                var query = $('#parent_form').data('query')
                if (query) {
                    loaded.push(hash);
                }
                $('#parent_form').data('loaded', loaded);
            }
            var tab = $(hash);
            $('#pagination_' + hash.substr(1)).trigger('tab.page');
            $(e.target).data('clicked', true);
        });
	
         $(window).load( function(){
        	 var index = layer.load(0, {
                 shade: [0.4,'#fff',false] //0.1透明度的白色背景
             }); 
        } ); 

        function currentDate(obj) {
            var myDate = new Date();
            var yy = myDate.getFullYear(); //获取完整的年份(4位,1970-????)
            var mm = myDate.getMonth() + 1; //获取当前月份(0-11,0代表1月)
            var dd = myDate.getDate(); //获取当前日(1-31)
            if (mm <= 9) {
                mm = "0" + mm;
            }
            if (dd <= 9) {
                dd = "0" + dd;
            }
            $(obj).val(yy + '-' + mm + '-' + dd);
        }
        
        function oldDate(obj) {
        	var myDate = new Date();
            var yy = myDate.getFullYear(); //获取完整的年份(4位,1970-????)
            var mm = myDate.getMonth() + 1; //获取当前月份(0-11,0代表1月)
            var dd = '01'; //获取当前日(1-31)
            if (mm > 6) {
				mm = mm - 6;
			}else {
				yy = yy - 1;//年份需要改变
				mm = mm + 6;
			}
            if (mm <= 9) {
                mm = "0" + mm;
            }
            $(obj).val(yy + '-' + mm + '-' + dd);
        }

        currentDate($('input[name="endTime"]'));
        oldDate($('input[name="startTime"]'));
        currentDate($('input[name="endDate"]'));
        oldDate($('input[name="startDate"]'));
    });
</script>
