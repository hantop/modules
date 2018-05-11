<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<script type="text/javascript" src="/static/component/HoldOn/HoldOn.min.js"></script>
<script type="text/javascript" src="/static/component/jquery-form/jquery.form.js"></script>
<script type="text/javascript" src="/static/component/JqueryPagination/mricode.pagination.js"></script>
<script type="text/javascript" src="/static/component/JqueryPagination/jsrender.js"></script>

<link rel="stylesheet" href="/static/component/HoldOn/HoldOn.min.css">
<link rel="stylesheet" href="/static/component/JqueryPagination/mricode.pagination.css">
<div id="jobOut_container">
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
                <tbody id="jobOutMembers">
                <input type="hidden" id="page" name="page">
                <input type="hidden" id="id" name="id" value="${groupId}">
                </tbody>
            </table>
        </div>
    </form>
    <div class="m-pagination text-center" id="pagination_outJob"></div>
</div>
<script id="jobOut-template" type="text/x-jsrender">
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
        $('#jobOut_container').parent().on('tab.show',function(e) {
            $("#pagination_outJob").pagination({
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
                        var param3 = {'dimission':0};
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
                            $("#jobOutMembers").empty().html($("#jobOut-template").render(data.list));
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

        $('#pagination_outJob').on('tab.page',function() {
            var tab = $('#pagination_outJob').parent();
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
