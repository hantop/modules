<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<script type="text/javascript" src="/static/component/HoldOn/HoldOn.min.js"></script>
<script type="text/javascript" src="/static/component/jquery-form/jquery.form.js"></script>
<script type="text/javascript" src="/static/component/JqueryPagination/mricode.pagination.js"></script>
<script type="text/javascript" src="/static/component/JqueryPagination/jsrender.js"></script>
<script type="text/javascript" src="/static/component/JqueryPagination/moment.min.js"></script>

<link rel="stylesheet" href="/static/component/HoldOn/HoldOn.min.css">
<link rel="stylesheet" href="/static/component/JqueryPagination/mricode.pagination.css">
<div id="jobIn_container">
    <form id="dataform"  method="post" action="${path}/system/pmsgroup/users">
        <div class="form-group">
            <%--<ol class="breadcrumb">
                <li class="active">部门管理</li>
                <li class="active">成员列表</li>
            </ol>--%>
            <table class="table table-striped table-bordered table-condensed">
                <thead>
                <tr class="success">
                    <th>ID</th>
                    <th>用户名</th>
                    <th>姓名</th>
                    <th>电话</th>
                    <th>邮箱</th>
                    <th>创建时间</th>
                </tr>
                </thead>
                <tbody id="jobMembers">
                <c:forEach items="${pmsUsersList}" var="v" varStatus="i">
                    <tr>
                        <td>${v.id}</td>
                        <td>${v.username}</td>
                        <td>${v.realname}</td>
                        <td>${v.phone}</td>
                        <td>${v.email}</td>
                        <td><fmt:formatDate value="${v.createTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                        <td>
                            <a href="${path}/finance/returngold/details?gid=${v.id}&startTime=${returnGoldForm.startTime}&endTime=${returnGoldForm.endTime}">修改</a>
                            <a href="">删除</a>
                            <a href="">成员管理</a>
                        </td>
                    </tr>
                    <%--<input type="hidden" id="id" name="id" value="${v.id}">--%>
                </c:forEach>
                <input type="hidden" id="page" name="page">
                <input type="hidden" id="id" name="id" value="${groupId}">
                </tbody>
            </table>
        </div>
    </form>
    <div class="m-pagination text-center" id="pagination_inJob"></div>
</div>
<script id="jobIn-template" type="text/x-jsrender">
    <tr>
        <td>
            {{:id}}
         </td>
         <td>
             {{:username}}
         </td>
         <td>
             {{:realname}}
         </td>
         <td>
             {{:phone}}
         </td>
         <td>
             {{:email}}
         </td>
         <td>
             {{formatDate:createTime}}
         </td>
    </tr>
</script>
<script type="text/javascript">
    $(function(){
        $('#jobIn_container').parent().on('tab.show',function(e) {
            $("#pagination_inJob").pagination({
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
                        var param1 = {'id':$('#id').val()};
                        var param2 = {'page':$('#page').val()};
                        var param3 = {'dimission':1};
                        return $.extend({},param1,param2,param3);
                    }(),
                    url: '${path}/system/pmsgroup/users/list',
                    beforeSend: function (XMLHttpRequest) {
                        HoldOn.open({
                            theme: "sk-cube-grid",
                            message: '请稍等.....',
                            backgroundColor: "#000"
                        });
                    },
                    totalName: 'total',
                    pageSizeName: 'limit',
                    pageIndexName: 'pageNum',
                    success: function (data, pageIndex) {
                        console.info(data.list);
                        if(data.list){
                            $("#jobMembers").empty().html($("#jobIn-template").render(data.list));
                        }
                    },
                    complete: function (XMLHttpRequest, textStatu) {
                        HoldOn.close();
                    }
                }
            }).on("pageClicked", function (event, pageIndex) {
            }).on('jumpClicked', function (event, pageIndex) {
            }).on('pageSizeChanged', function (event, pageSize) {
            });
            $(this).data('pageInit',true);
        });

        $('#pagination_inJob').on('tab.page',function() {

            var tab = $('#jobIn_container').parent();
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

        $.views.converters({
            // jsrender converters 格式化日期
            formatDate: function (value) {
                return moment(value).format("YYYY-MM-DD HH:mm:ss");
            }
        });
    });
</script>
