<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!--My 97 date -->
<script type="text/javascript" src="/static/component/datepicker/calendar.js"></script>
<link rel="stylesheet" href="/static/component/HoldOn/HoldOn.min.css">
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script type="text/javascript" src="/static/component/HoldOn/HoldOn.min.js"></script>
<script type="text/javascript" src="/static/component/jquery-form/jquery.form.js"></script>
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script type="text/javascript" src="/static/component/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/static/component/HoldOn/HoldOn.min.js"></script>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>

<link rel="stylesheet" href="/static/component/HoldOn/HoldOn.min.css">
<style type="text/css">
    .red_font {
        color: red;
    }
</style>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/marketing/index">营销管理</a></li>
                <li class="active"><b>电话绩效详情</b><img title="1.联系名单中有投资行为的用户;2.用户投资时间在客服联系结束后15天内, 同一号码多次联系的按最后次联系的结束时间计算;3.需要为已放款的投资;4.同一号码被多个客服联系的情况, 两个客服均会显示该条。" width="15px" src="/static/image/help.png"/></li>
            </ol>
            <label>名单导入：</label><button type="button" class="btn btn-success btn-sm" id="importButton">导入表格</button>
            <a type="button" class="btn btn-primary btn-sm" href="/static/template/13556101111_奥巴马_二月份电销.xls">下载表格模板</a>
            <hr>
            <form id="dataform" class="form-inline" method="post" action="${path}/marketing/tmr">
                <div class="form-group">
                    <label>导入日期：</label>
                    <input id="beginDate"  name="startTime"  type="text" readonly="readonly" maxlength="20" class="form-control input-sm" style="width:163px;"
                           value="${tmrPerformanceForm.startTime}"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd', isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'});"
                           onFocus="WdatePicker({isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'})" />
                    --
                    <input id="endDate" name="endTime" type="text" readonly="readonly" maxlength="20" class="form-control input-sm" style="width:163px;"
                           value="${tmrPerformanceForm.endTime}"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd', isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'});"
                           onFocus="WdatePicker({isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'})" />
                </div>&nbsp;&nbsp;
                <div class="form-group">
                    <label>姓名：</label>
                    <input id="tmrName" name="tmrName" value="${tmrPerformanceForm.tmrName}" type="text" class="form-control input-sm"/>
                </div>&nbsp;&nbsp;
                <div class="form-group">
                    <label>名单名称：</label>
                    <input id="fileName" name="fileName" value="${tmrPerformanceForm.fileName}" type="text" class="form-control input-sm"/>
                </div>&nbsp;&nbsp;
                <input type="hidden" id="page" name="page">
                    <shiro:hasPermission name="marketing:search">
                        <button id="searchBtn" type="button" class="btn btn-primary btn-sm">搜索</button>
                    </shiro:hasPermission>
            </form>
            <hr style="margin-bottom: 0;">
            <div class="panel panel-default">
                <div class="panel-heading">名单列表</div>
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>姓名</th>
                        <th>坐席编号</th>
                        <th>名单名称</th>
                        <th>名单人数</th>
                        <th>导入日期</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${list}" var="v" varStatus="i">
                        <tr>
                            <td>${v.tmrName}</td>
                            <td>${v.tmrNumber}</td>
                            <td>${v.fileName}</td>
                            <td>${v.numbers}</td>
                            <td><fmt:formatDate value="${v.createtime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                            <td>
                                    <shiro:hasPermission name="marketing:detail">
                                        <a href="${path}/marketing/tmr/detail?id=${v.id}">查看详情</a>
                                    </shiro:hasPermission>
                                &nbsp;&nbsp;<a href="${path}/marketing/tmr/trouble?id=${v.id}">异常名单</a>
                                <shiro:hasPermission name="marketing:calculate">
                                    <c:if test="${v.dispose eq false}">
                                        &nbsp;&nbsp;<a href="javascript:void(0)" onclick="javascript:calculatePerformance(${v.id})">计算</a>
                                    </c:if>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="marketing:delete">
                                    &nbsp;&nbsp;
                                    <a href="javascript:void(0)" onclick="javascript:deletePerformance(${v.id})">删除</a>
                                </shiro:hasPermission>

                            </td>
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
    $(function(){

        $('#importButton').click(function() {
            var file = $('<input type="file" name="file"/>');
            file.trigger('click');
            var operator = $('<input type="text" name="operator" value="' + $('#currentUserName').text() + '"/>')
            var form = $("<form method='post' enctype='multipart/form-data'></form>");
            $(form).append(file).append(operator);
            $(file).on('change',function() {
                var filepath = $(file).val();
                var extStart = filepath.lastIndexOf(".");
                var ext = filepath.substring(extStart, filepath.length).toUpperCase();
                if (ext != ".XLS" && ext != ".XLSX") {
                    $(file).val("");
                    bootbox.alert({message:"<h3>【文件限于xls,xlsx格式】</h3>"});
                    HoldOn.close();
                    return false;
                }
                bootbox.confirm({
                    title: '提示',
                    size: 'small',
                    message: "确认导入吗？",
                    callback: function (result) {
                        if (result) {
                            form.ajaxSubmit({
                                url: '${path}/marketing/tmr/import',
                                beforeSubmit: function (arr, $form, options) {
                                    HoldOn.open({
                                        theme: "sk-cube-grid",
                                        message: '请稍等.....',
                                        backgroundColor: "#000"
                                    });
                                },
                                success: function (data) {
                                        var msg = data.status;
                                        if(msg == "success"){
                                            var regCount = data.regCount;
                                            var regTips = "";
                                            var repeCount = data.repeCount;
                                            var repeTips = "";
                                            if(regCount > 0){
                                                regTips = "<span style='font-size: larger;color: brown'>"+regCount+" 条未注册</span>" ;
                                            }
                                            if(repeCount > 0){
                                                repeTips = "<span style='font-size: larger;color: brown'>"+repeCount+" 条存在重复</span>" ;
                                            }
                                            var message = "<span style='font-weight: bold;color: red;font-size: 20px'>导入成功！！</span>";

                                            if(regTips != '' || repeTips != ''){
                                                var troubles = "名单中有" + "<br/>" + regTips + "<br/>" + repeTips + "<br/>" + "异常项目不会被处理，请在异常名单中查看";
                                                message += "<br/>" + troubles;
                                            }

                                            bootbox.confirm({
                                                title: '提示',
                                                size: 'small',
                                                message: message,
                                                callback: function (result) {
                                                    window.location.reload(true);
                                                }
                                            });
                                        }else {
                                            var error = "";
                                            error += "<p class='text-danger'>" + data.status + "</p>";
                                            bootbox.alert({message: error, title: '错误信息'});
                                        }
                                },
                                complete: function (xhr, status) {
                                    delete file;
                                    delete operator;
                                    delete form;
                                    HoldOn.close();
                                }
                            });
                        }
                    }
                });
            });

        });
    });

    $("#searchBtn").bind("click", function() {
        var beginDate = $("#beginDate").val();
        var endDate = $("#endDate").val();
        var beginDateSplit = beginDate.split('-');
        var endDateSplit = endDate.split('-');
        var beginDateMonths = parseInt(beginDateSplit[0]) * 12 + parseInt(beginDateSplit[1]);
        var endDateMonths = parseInt(endDateSplit[0]) * 12 + parseInt(endDateSplit[1]);
        if(beginDate > endDate) {
            bootbox.alert({message: "【开始日期不能晚于结束日期】", title: '错误信息'});
            return false;
        }
        if(endDateMonths - beginDateMonths > 6) {
            bootbox.alert({message: "【统计起止月份不能大于6个月】", title: '错误信息'});
            return false;
        }
        var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $("#dataform").submit();
        layer.close();
    });

    function pagination(page) {
        $('#page').val(page);
        var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $("#dataform").submit();
        layer.close();
    }

    function calculatePerformance(pId){
        bootbox.confirm({
            title: '提示',
            size: 'small',
            message: "<span style='font-size: 19px;font-style: inherit'>确认计算？</span>",
            callback: function (result) {
                if (result) {
                    HoldOn.open({
                        theme: "sk-cube-grid",
                        message: '请稍等.....',
                        backgroundColor: "#000"
                    });
                    $.post(
                            path + "/marketing/tmr/calculate",
                            {"id" : pId},
                            function (data) {
                                HoldOn.close();
                                var res = data.codeStatus;
                                if(res == "success" || res == "noValue"){
                                    bootbox.alert({message: "数据已生产，请查看详情！！！", title: '成功信息',callback: function() {
                                        window.location.reload(true);}});
                                }
                                else{
                                    bootbox.alert({message: "系统错误，请稍后再试！！！", title: '错误信息'});
                                }
                            }
                    );
                }
            }
        });
    }

    function deletePerformance(pId){
        bootbox.confirm({
            title: '提示',
            size: 'small',
            message: "<span style='font-size: 19px;font-style: inherit'>确认删除？</span>",
            callback: function (result) {
                if (result) {
                    $.post(
                            path + "/marketing/tmr/delete",
                            {"id" : pId},
                            function (data) {
                                var res = data.codeStatus;
                                if(res == "success"){
                                    bootbox.alert({message: "删除成功！！！", title: '操作信息',callback: function() {
                                        window.location.reload(true);}});
                                }else{
                                    bootbox.alert({message: "删除失败，请稍后再试！！！", title: '错误信息'});
                                }
                            }
                    );
                }
            }
        });
    }

    function exportReturnGold(obj){

        /*var form = document.forms[0];
         form.action = path + "/finance/returngold/export";
         form.submit();
         form.action = path + "/finance/returngold"*/
        var cloneForm = $(obj).parents('form.form-inline').clone(true);
        $(cloneForm).attr('action', '${path}/finance/returngold/export').submit();
        return false;
    }
</script>