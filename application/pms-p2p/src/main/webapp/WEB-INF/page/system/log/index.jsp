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
                <li class="active"><a href="${path}/system/index">系统管理</a></li>
                <li class="active"><b>系统日志</b></li>
            </ol>
            <form id="dataform" class="form-inline" method="post" action="${path}/system/pmslog">
                <input type="hidden" id="page" name="page">
                <input type="hidden" id="defaultStatus" value="${defaultStatus}">
                <shiro:hasPermission name="channelStatistics:search">
                <div class="form-group">
                    <label>日期：</label>
                    <input id="beginDate"  name="startDate"  type="text" readonly="readonly" maxlength="20" class="form-control input-sm" style="width:163px;"
                           value="${logForm.startDate}"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd', isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'});"
                           onFocus="WdatePicker({isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'})" />
                    --
                    <input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="form-control input-sm" style="width:163px;"
                           value="${logForm.endDate}"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd', isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'});"
                           onFocus="WdatePicker({isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'})" />
                </div>&nbsp;&nbsp;
                <div class="form-group" style="margin-top: 1px;">
                    登录账号：
                    <input id="userName" name="userName" type="text" class="form-control input-sm"
                           value="${logForm.userName}"/>
                    真实姓名：
                    <input id="realName" name="realName" type="text" class="form-control input-sm"
                           value="${logForm.realName}"/>
                </div>&nbsp;&nbsp;
                    <div class="form-group">
                         状态：
                        <select name="status" id="status" class="form-control">
                                <option value="1">登录</option>
                                <option value="2">银行卡</option>
                                <option value="3">业务管理</option>
                                <option value="4">奖励管理</option>
                                <option value="5">营销活动</option>
                                <option value="6">更改手机号</option>
                                <option value="7">代充值管理</option>
                                <option value="8">平台账户管理</option>
                        </select>
                    </div>&nbsp;&nbsp;


                <button id="searchBtn" type="button" class="btn btn-primary btn-sm"  style="margin-top: 1px;">搜索</button>
                </shiro:hasPermission>
                
                <shiro:hasPermission name="channelStatistics:export">
                     <button id="exportBtn" style="display: none" type="button" class="btn btn-success btn-sm" style="margin-top: 1px;">导出</button>
                </shiro:hasPermission>

            </form>
            <hr style="margin-bottom: 0;">
            <div class="panel panel-default">
                <div class="panel-heading">日志列表</div>
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>时间 </th>
                        <th>登录账号 </th>
                        <th>真实姓名 </th>
                        <th>手机号码 </th>
                        <th>地址 </th>
                        <th>操作 </th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${pmsLogList}" var="v" varStatus="i">
                            <td><fmt:formatDate value="${v.time}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                            <td>${v.userName}</td>
                            <td>${v.realName!=null?v.realName:""}</td>
                            <td>${v.phone}</td>
                            <td>${v.ip}</td>
                            <td>${v.operation}</td>
                        </tr>
                    </c:forEach>

                    </tbody>
                </table>
            </div>
            <tiles:insertDefinition name="paginator"/>
        </div>
    </div>
</div>
<script type="text/javascript">
    window.onload = function(){
        if ($("#status").val() == '1') {
            $('#exportBtn').show();
        }else {
            $('#exportBtn').hide();
        }

    }
    $('#status').on("change",function(){
        if ($("option:selected",this).val() == '1') {
            $('#exportBtn').show();
        }else {
            $('#exportBtn').hide();
        }
    })



    // 导出
    $("#exportBtn").click(function () {
                var cloneForm = $(this).parents('form.form-inline').clone(true);
                $(cloneForm).attr('action', '${path}/system/pmslog/export').submit();
                return false;
            }
    );

    /**
     * 如果文本框有值，清空下拉框的值
     */
    function makeFormPropties() {
        var userName = $.trim($("#userName").val());
        var realName = $.trim($("#realName").val());
        if ((userName != null && userName != '') ||
                (realName != null && realName != '')) {
            $("#status").val(null);
        }
        $("#userName").val(userName);
        $("#realName").val(realName);
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
    $().ready(function(){
        if($("#defaultStatus").val()!=null&&$("#defaultStatus").val()!="") {
            $("#status").val($("#defaultStatus").val());
        }
    });

</script>