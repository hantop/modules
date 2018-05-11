<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<link rel="stylesheet" href="/static/component/zTree_v3/css/zTreeStyle/zTreeStyle.css" type="text/css">
<link rel="stylesheet" href="/static/component/bootstrap/css/bootstrap.min.css" type="text/css">
<script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="/static/component/zTree_v3/js/jquery.ztree.exedit-3.5.js"></script>
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>

<div class="container  content-top" id="userMember">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/system/index">系统管理</a></li>
                <li class="active"><b>组织管理</b></li>
            </ol>
            <hr/>
            <div class="col-md-3">
                <div class="panel panel-default">
                    <div class="panel-body">
                        <ul id="tree" class="ztree" style="width:230px; overflow:auto;"></ul>
                    </div>
                </div>
            </div>
            <div class="col-md-9" id="maintain">
                <div class="panel panel-default">
                    <div class="panel-body">
                        <!-- Nav tabs -->
                        <ul id="nav-tabs" class="nav nav-tabs" role="tablist">
                            <li class=""><a href="#inJob" aria-controls="inJob" role="tab" data-toggle="tab">在职人员</a></li>
                            <li><a href="#outJob" aria-controls="outJob" role="tab" data-toggle="tab">离职人员</a></li>
                        </ul>
                        <!-- Tab panes -->
                        <div class="tab-content">
                            <div role="tabpanel" class="tab-pane " id="inJob">
                                <jsp:include page="jobIn-list.jsp"/>
                            </div>
                            <div role="tabpanel" class="tab-pane" id="outJob">
                                <jsp:include page="jobOut-list.jsp"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <jsp:include page="tree.jsp"></jsp:include>
        </div>
    </div>
</div>
<script>
    $(function(){
        var tab = $('.nav-tabs a[href="#inJob"]');
        tab.tab('show');
        $('#pagination_inJob').trigger('tab.page');

        $('#nav-tabs a').click(function (e) {
            e.preventDefault();
            $(this).tab('show');
        });
        $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
            // 获取已激活的标签页的名称
            var hash = e.target.hash;
            var loaded = $('#userMember').data('loaded');
            if (loaded && loaded.contains(hash)) {
                return;
            } else {
                if (!loaded) {
                    loaded = new Array();
                }
                loaded.push(hash);
                $('#userMember').data('loaded', loaded);
            }
            var tab = $(hash);
            $('#pagination_' + hash.substr(1)).trigger('tab.page');
            $(e.target).data('clicked', true);
        })
    });
</script>