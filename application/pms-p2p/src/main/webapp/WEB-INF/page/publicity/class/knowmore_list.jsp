<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<link rel="stylesheet" type="text/css" href="/static/component/jqueryui/jquery-ui.min.css">
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<link rel="stylesheet" href="/static/component/HoldOn/HoldOn.min.css">
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script type="text/javascript" src="/static/component/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/static/component/datepicker/calendar.js"></script>

<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="#">网贷知多点</a></li>

            </ol>
            <div>
                <form id="dataform" class="form-inline" method="post" action="${path}/publicity/class/knowmoreList">
                    <input type="hidden" id="page" name="page">
                    <input type="hidden" id="fromPage" name="fromPage" value="true">

                    <div class="form-group">
                        <label>文章标题</label>
                        <input type="text" class="form-control input-sm" name="title" value="${knowMore.title}">
                    </div>


                    <div class="form-group">
                        <label>&nbsp; 发布者</label>
                        <input type="text" class="form-control input-sm" name="publisherId"  value="${knowMore.publisherId}">
                    </div>

                    <div class="form-group">
                        <label>&nbsp; 修改时间</label>
                        <input name="startTime"  readonly="readonly" class="form-control input-sm startDate" style="width:110px;"
                               onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" value="${startTime}"/>
                        至
                        <input name="endTime" readonly="readonly" class="form-control input-sm startDate" style="width:110px;"
                               onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" value="${endTime}"/>
                    </div>


                    <div class="form-group">
                        <label>&nbsp;发布状态：</label>
                    </div>
                    <div class="form-group">
                        <select class="form-control"  name="status" >
                            <option value="-1" <c:if test="${knowMore.status == -1}">selected="selected"</c:if> >全部</option>
                            <option value="1" <c:if test="${knowMore.status == 1}">selected="selected"</c:if> >已发布</option>
                            <option value="0" <c:if test="${knowMore.status == 0}">selected="selected"</c:if> >未发布</option>

                        </select>
                    </div>
                    <br/>
                    <br/>
                    <button onclick="searchList()" type="button" class="btn btn-primary btn-sm">查询</button>
                    <shiro:hasPermission name="knowMore:edit">
                        <a class="btn btn-success btn-sm" href="${path}/publicity/class/toUpateKnowMore?id=-1">新增</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="knowMore:delete">
                        <button type="button" class="btn btn-danger btn-sm" onclick="deleteIds(0);">删除</button>
                    </shiro:hasPermission>
                </form>



            </div>
            <hr>
            <div class="table-responsive">
                <table id="datatable" class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th width="5%" class="text-center"><input type="checkbox" onclick="javascript:checkAll(this);"></th>
                        <th width="5%">序号</th>
                        <th width="11%">标题</th>
                        <th width="11%">发布状态</th>
                        <th width="11%">最后修改时间</th>
                        <th width="11%">位置排序</th>
                        <th width="11%">发布者</th>
                        <th width="11%">操作</th>
                    </tr>
                    </thead>

                    <c:forEach items="${knowMoreList}" var="v" varStatus="vs">
                        <tr>

                            <td class="text-center">
                                <input name="item" type="checkbox" onclick="checkItem(this);" value="${v.id}">
                            </td>
                            <td>${vs.index+1}</td>
                            <td>${v.title}</td>
                            <td>${v.status==1?'已发布':'未发布'}</td>
                            <td><fmt:formatDate value="${v.updateTime}" pattern="yyyy-MM-dd  HH:mm:ss" /></td>
                            <td>${v.sorting}</td>
                            <td>${v.publisherId}</td>
                            <td>
                                <shiro:hasPermission name="knowMore:edit">
                                <a href="${path}/publicity/class/toUpateKnowMore?id=${v.id}">修改</a>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="knowMore:delete">
                                <a onclick="deleteIds(${v.id})">删除</a>
                                </shiro:hasPermission>
                            </td>
                        </tr>
                    </c:forEach>


                </table>
            </div>
            <tiles:insertDefinition name="paginator"/>
        </div>
    </div>
</div>

<script>

    /**
     * 搜索
     */
    function searchList(){
        var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $("#dataform").submit();
    }

    function checkAll(checkbox) {
        if ($(checkbox).is(":checked")) {
            $("input[name=item]").prop("checked", "checked");
            $("#datatable tr").addClass("warning");
        } else {
            $("input[name=item]").prop("checked", false);
            $("#datatable tr").removeClass("warning");
        }
    }

    function checkItem(checkbox) {
        if ($(checkbox).is(":checked")) {
            jQuery(checkbox).parent().parent().addClass("warning");
        } else {
            jQuery(checkbox).parent().parent().removeClass("warning");
        }
    }

    function deleteIds(id){
        var ids = "";
        if(id==0){
            var items = $("input[name='item']:checked");
            var length = items.length;
            for (var i = 0; i < length; i++) {
                ids += $(items[i]).val() + ",";
            }
        }else{
            ids=id;
        }
        if(ids==''){
            alert('请选择要删除的数据');
            return;
        }

        bootbox.confirm({
            title: '删除',
            size: 'small',
            message: "<span style='font-size: 13px;font-style: inherit'>确定删除？</span>",
            callback: function (result) {
                if (result) {
                    $.ajax({
                        cache : false,
                        type : "POST",
                        data : {ids:ids},
                        url : "${path}/publicity/class/deleteKnowMore?timestamp=" + new Date().getTime(),
                        async : true,
                        dataType :"json",
                        error : function() {
                            bootbox.alert({message: "提交超时，请重新登录！！！", title: '错误信息'});
                        },
                        success : function(data, textStatus, XMLHttpRequest) {
                            if(data.success){
                                bootbox.alert({message: "删除成功！", title: '操作信息',callback: function() {
                                    window.location.href="${path}/publicity/class/knowmoreList?status=-1";}});
                            }else{
                                bootbox.alert({message: data.errorMsg, title: '错误信息'});
                            }
                        }
                    });
                }
            }
        });



    }
</script>
