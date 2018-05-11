<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!--My 97 date -->
<script type="text/javascript" src="/static/component/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/static/component/datepicker/calendar.js"></script>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/channel/index">渠道管理</a></li>
                <li class="active"><b>渠道统计</b></li>
            </ol>
            <form id="dataform" class="form-inline" method="post" action="${path}/channel/origin">
                <input type="hidden" id="defaultFirstChannelId" value="${channelStatisticsForm.firstChannelId}">
                <input type="hidden" id="defaultSecondChannelId" value="${channelStatisticsForm.secondChannelId}">
                <input type="hidden" id="page" name="page">
                
                <shiro:hasPermission name="channelStatistics:search">
                <div class="form-group">
                    <label>日期：</label>
                    <input id="beginDate"  name="startDate"  type="text" readonly="readonly" maxlength="20" class="form-control input-sm" style="width:163px;"
                           value="${channelStatisticsForm.startDate}"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd', isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'});"
                           onFocus="WdatePicker({isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'})" />
                    --
                    <input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="form-control input-sm" style="width:163px;"
                           value="${channelStatisticsForm.endDate}"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd', isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'});"
                           onFocus="WdatePicker({isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'})" />
                </div>&nbsp;&nbsp;
                <div class="form-group">
                    一级渠道：
                    <select name="firstChannelId" id="firstChannelId" class="form-control">
                    </select>
                    二级渠道：
                    <select name="secondChannelId" id="secondChannelId" class="form-control">
                    </select>
                </div>&nbsp;&nbsp;
                <div class="form-group" style="margin-top: 15px;">
                    渠道名称：
                    <input id="channelName" name="channelName" type="text" class="form-control input-sm"
                           value="${channelStatisticsForm.channelName}"/>
                    渠道编号：
                    <input id="channelCode" name="channelCode" type="text" class="form-control input-sm"
                           value="${channelStatisticsForm.channelCode}"/>
                </div>&nbsp;&nbsp;
                <button id="searchBtn" type="button" class="btn btn-primary btn-sm"  style="margin-top: 15px;">搜索</button>
                </shiro:hasPermission>
                
                <shiro:hasPermission name="channelStatistics:export">
                     <button id="exportBtn" type="button" class="btn btn-success btn-sm" style="margin-top: 15px;">导出</button>
                </shiro:hasPermission>
            </form>
            <hr style="margin-bottom: 0;">
            <div class="panel panel-default">
                <div class="panel-heading">渠道统计列表</div>
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>渠道来源</th>
                        <th>渠道编号 <img title="对应渠道的渠道编码" width="15px" src="/static/image/help.png"/></th>
                        <th>注册人数 <img title="注册成功的用户数" width="15px" src="/static/image/help.png"/></th>
                        <th>实名验证 <img title="通过该渠道注册的并且通过实名认证的人数" width="15px" src="/static/image/help.png"/></th>
                        <th>充值人数 <img title="通过该渠道注册的并且产生过充值行为的人数" width="15px" src="/static/image/help.png"/></th>
                        <th>投资人数 <img title="通过该渠道注册的并且产生过投资行为的人数" width="15px" src="/static/image/help.png"/></th>
                        <th>充值金额 <img title="对应渠道注册用户充值的总和" width="15px" src="/static/image/help.png"/></th>
                        <th>投资金额 <img title="对应渠道注册用户投资的总和(注意个人也要记录,查看详情时需要)" width="15px" src="/static/image/help.png"/></th>
                        <th>激活红包人数 <img title="(仅针对返现红包)对应渠道注册用户获得过红包奖励的人数,单人激活多个红包只记一个人数" width="15px" src="/static/image/help.png"/></th>
                        <th>激活红包金额 <img title="只统计达到投资额激活并且返现成功红包的金额,注册送的不算在内" width="15px" src="/static/image/help.png"/></th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${channelStatisticses}" var="v" varStatus="i">
                        <tr>
                            <td>${v.name}</td>
                            <td>${v.code}</td>
                            <td>${v.registerCount}</td>
                            <td>${v.authCount}</td>
                            <td>${v.rechargeCount}</td>
                            <td>${v.investCount}</td>
                            <td>${v.rechargeSum}</td>
                            <td>${v.investSum}</td>
                            <td>${v.activeRedpacketCount}</td>
                            <td>${v.activeRedpacketSum}</td>
                            <td>
                                <shiro:hasPermission name="channelStatisticsDetail:view">
                                <a href="${path}/channel/origin/detail?channelId=${v.id}&startDate=${channelStatisticsForm.startDate}&endDate=${channelStatisticsForm.endDate}">查看详情</a>
                                </shiro:hasPermission>
                            </td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <th colspan="2">总计</th>
                        <th>${registerCountTotal}</th>
                        <th>${authCountTotal}</th>
                        <th>${rechargeCountTotal}</th>
                        <th>${investCountTotal}</th>
                        <th>${rechargeSumTotal}</th>
                        <th>${investSumTotal}</th>
                        <th>${activeRedpacketCountTotal}</th>
                        <th>${activeRedpacketSumTotal}</th>
                        <th></th>
                    </tr>
                    </tbody>
                </table>
            </div>
            <tiles:insertDefinition name="paginator"/>
        </div>
    </div>
</div>
<script type="text/javascript">
    // 导出
    $("#exportBtn").click(function () {
                var cloneForm = $(this).parents('form.form-inline').clone(true);
                $(cloneForm).attr('action', '${path}/channel/origin/export').submit();
                return false;
            }
    );

    /**
     * 如果文本框有值，清空下拉框的值
     */
    function makeFormPropties() {
        var channelName = $.trim($("#channelName").val());
        var channelCode = $.trim($("#channelCode").val());
        if ((channelName != null && channelName != '') ||
                (channelCode != null && channelCode != '')) {
            $("#firstChannelId").val(null);
            $("#secondChannelId").val(null);
        }
        $("#channelName").val(channelName);
        $("#channelCode").val(channelCode);
    }
    $("#searchBtn").bind("click", function() {
        var beginDate = $("#beginDate").val();
        var endDate = $("#endDate").val();
        var beginDateSplit = beginDate.split('-');
        var endDateSplit = endDate.split('-');
        var beginDateMonths = parseInt(beginDateSplit[0]) * 12 + parseInt(beginDateSplit[1]);
        var endDateMonths = parseInt(endDateSplit[0]) * 12 + parseInt(endDateSplit[1]);
        if(beginDate > endDate) {
            alert("【开始日期不能晚于结束日期】");
            return false;
        }
        if(endDateMonths - beginDateMonths > 6) {
            alert("【统计起止月份不能大于6个月】");
            return false;
        }

        var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $("#dataform").submit();
        layer.close();

        // 如果文本框有值，清空下拉框的值
        makeFormPropties();

    });

    function pagination(page) {
        $('#page').val(page);
        var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $('#dataform').submit();
        layer.close();
    }

    function getChannelsByParent(parentId) {
        $.ajax({
            method: "get",
            url: path + "/channel/getChannelsByParent",
            dataType: "json",
            data : {
                parentId : parentId
            }
        }).success(function (data) {
            $("#secondChannelId").append("<option value=''>全部</option>");
            $.each(data, function(i, item){
                $("#secondChannelId").append(
                    "<option value='" + item.id + "'>" + item.name + "</option>"
                );
            });
            if(parentId == $("#defaultFirstChannelId").val()) {
                $("#secondChannelId").val($("#defaultSecondChannelId").val());
            } else {
                $("#secondChannelId").selectedIndex = 0;
            }
        });
    }

    $().ready(function(){
        $.get(
                path + "/channel/firstChannels",
                function (data) {
                    $("#firstChannelId").append("<option value=''>全部</option>");
                    $.each(data, function(i, item){
                        $("#firstChannelId").append(
                                "<option value='" + item.id + "'>" + item.name + "</option>"
                        );
                    });
                    var defaultFirstChannelId = $("#defaultFirstChannelId").val();
                    $("#firstChannelId").val(defaultFirstChannelId);
                    if(defaultFirstChannelId == null || defaultFirstChannelId == '') {
                        $("#secondChannelId").append("<option value=''>全部</option>");
                    } else {
                        // 初始化二级渠道
                        getChannelsByParent(defaultFirstChannelId);
                    }
                    // 绑定一级二级渠道onchange事件
                    $("#firstChannelId").change(function () {
                        $("#secondChannelId").html("");
                        if(this.value == null || this.value == '') {
                            $("#secondChannelId").append("<option value=''>全部</option>");
                        } else {
                            getChannelsByParent($("#firstChannelId").val());
                        }
                    });
                },
                "json"
        );

    });
</script>