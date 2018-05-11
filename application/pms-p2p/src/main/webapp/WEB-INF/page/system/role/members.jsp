<%--
  Created by IntelliJ IDEA.
  User: Lullaby
  Date: 2015/7/17
  Time: 16:23
  To change this template use File | Settings | File Templates.
--%>
<%@page language="java" pageEncoding="utf-8" %>

<div class="col-md-12" id="members_container" style="margin-top: 20px;">
    <div class="panel panel-default">
        <div class="panel-body">
            <table id="dynamic-table" class="table table-striped table-bordered table-hover">
                <thead>
                <tr class="success">
                    <th>登陆账号</th>
                    <th>真实姓名</th>
                    <th>部门</th>
                </tr>
                </thead>
                <tbody id="data_tbody">
                </tbody>
            </table>
        </div>
    </div>
    <div class="m-pagination text-center" id="pagination_tab2"></div>
</div>

<script id="recharge-template" type="text/x-jsrender">
    <tr>
        <td>
            {{:username}}
         </td>
         <td>
             {{:realname}}
         </td>
         <td>
             {{:department}}
         </td>
    </tr>
</script>

<script type="text/javascript">

    /* $('a[data-toggle="tab"]').on('show.bs.tab', function (e) {
        // 判断是否点击本标签页
        var targetName = e.target.hash;
        if (targetName == "#tab2") {
            var container = $("#data_tbody");
            // 清空原有列表
            container.html(null);
            // 查询数据
            var zTreeObj = $.fn.zTree.getZTreeObj("roleTree");
            var selectedNodes = zTreeObj.getSelectedNodes();
            if(selectedNodes != null && selectedNodes.length > 0) {
                $.ajax({
                    method: "get",
                    traditional : true,
                    url: path + "/system/pmsrole/members",
                    dataType: "json",
                    data: {
                        'roleId' : selectedNodes[0].id
                    }
                }).success(function (data) {
                    if(data.list!=null){
                        var obj;
                        for (var i = 0; i < data.list.length; i++) {
                            obj = data.list[i];
                            container.append("<tr><td>" + obj.username + "</td><td>" + obj.realname + "</td><td>" + obj.department + "</td></tr>");
                        }
                    }
//                    alert(JSON.stringify(data));
//                    if(data.list!=null){
//                        $("#dynamic-table tbody").empty().html($("#recharge-template").render(data.list));
//                    }
                }).error(function (event) {
                    $('#addDiv').modal("hide");
                    $(".modal-body.tips").html("<p class='text-danger'><b>服务器异常</b></p>");
                    $("#tips").modal("show");
                    return false;
                });
            }
        }
    }) */


    $(function () {
        $('#members_container').parent().on('tab.show', function (e) {
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
                        var zTreeObj = $.fn.zTree.getZTreeObj("roleTree");
                        var selectedNodes = zTreeObj.getSelectedNodes();
                        var id = selectedNodes[0].id;
                        return {"roleId":id};
                    }(),
                    url: path + "/system/pmsrole/members",
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
                        if(data.list!=null){
                            $("#dynamic-table tbody").empty().html($("#recharge-template").render(data.list));
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

        $('#pagination_tab2').on('tab.page',function() {
            var tab = $('#members_container').parent();
            if(tab.data('pageInit')) {
                $(this).pagination('setPageIndex', 0);
                
                var zTreeObj = $.fn.zTree.getZTreeObj("roleTree");
                var selectedNodes = zTreeObj.getSelectedNodes();
                var id = selectedNodes[0].id;
                
                $(this).pagination('setParams', {"roleId":id});
                $(this).pagination('setPageIndex', 0);
                $(this).pagination('remote');
            } else {
                tab.trigger('tab.show');
            }
        });
    });
</script>