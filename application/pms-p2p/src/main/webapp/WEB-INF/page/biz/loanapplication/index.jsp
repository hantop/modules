<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!--My 97 date -->
<script type="text/javascript" src="/static/component/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/static/component/datepicker/calendar.js"></script>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/biz/index">业务管理</a></li>
                <li class="active"><b>借款申请信息</b></li>
            </ol>
            <form id="dataform" class="form-inline" method="post" action="${path}/biz/loanapplication">
                <div class="form-group">
                    <label>手机号：</label>
                    <input id="phonenum" name="phonenum" type="text" class="form-control input-sm"
                           maxlength="11" style="width:163px;"
                           value="${loanApplicationForm.phonenum}"/>
                    &nbsp;
                    <label>申请日期：</label>
                    <input id="startDate" name="startDate" type="text" readonly="readonly" maxlength="20"
                           class="form-control input-sm" style="width:163px;"
                           value="${loanApplicationForm.startDate}"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
                    --
                    <input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20"
                           class="form-control input-sm" style="width:163px;"
                           value="${loanApplicationForm.endDate}"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
                    &nbsp;
                    <label>状态</label>
                    <select id="processingStatus" name="processingStatus" class="form-control input-sm"
                            style="width:115px;" value="${loanApplicationForm.processingStatus}">
                        <option value="">全部</option>
                        <option value="0"
                                <c:if test='${loanApplicationForm.processingStatus == "0"}'>selected="selected"</c:if>>未处理</option>
                        <option value="1"
                                <c:if test='${loanApplicationForm.processingStatus == "1"}'>selected="selected"</c:if>>通过</option>
                        <option value="2"
                                <c:if test='${loanApplicationForm.processingStatus == "2"}'>selected="selected"</c:if>>不通过</option>
                    </select>
                </div>
                <input type="hidden" id="page" name="page">
                <input type="hidden" id="fromPage" name="fromPage" value="true">
                <shiro:hasPermission name="loanapplication:search">
                    <button type="button" class="btn btn-primary btn-sm" id="searchBtn">搜索</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="loanapplication:export">
                    <button type="button" class="btn btn-success btn-sm" onclick="javascript: exportLoanApplication();">导出</button>
                </shiro:hasPermission>
            </form>
            <hr style="margin-bottom: 0;">
            <div class="panel panel-default">
                <div class="panel-heading">借款申请信息</div>
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>序号</th>
                        <th>联系人姓名</th>
                        <th>借款金额</th>
                        <th>手机号码</th>
                        <th>所在区域</th>
                        <th>月收入</th>
                        <th>是否有车</th>
                        <th>是否有房</th>
                        <th>状态</th>
                        <th>申请时间</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${loanApplicationList}" var="v" varStatus="vs">
                        <tr>
                            <td>${vs.index + 1}</td>
                            <td>${v.contacts}</td>
                            <td>${v.amountRange}</td>
                            <td>${v.phonenum}</td>
                            <td>${v.districtFullName}</td>
                            <td>${v.annualIncome}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${v.hasCar == 1}">
                                        是
                                    </c:when>
                                    <c:otherwise>
                                        否
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${v.hasRoom == 1}">
                                        是
                                    </c:when>
                                    <c:otherwise>
                                        否
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${v.processingStatus == 1}">
                                        通过
                                    </c:when>
                                    <c:when test="${v.processingStatus == 2}">
                                        不通过
                                    </c:when>
                                    <c:otherwise>
                                        未处理
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td><fmt:formatDate value="${v.createTime}"
                                                pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                            <td>
                                <c:if test="${v.processingStatus == 0}">
                                    <a href="#" onclick="showPassDiv(${v.id});">通过</a>
                                    <a href="#" onclick="showNopassDiv(${v.id})">不通过</a>
                                </c:if>
                                <a href="${path}/biz/loanapplication/edit?id=${v.id}">编辑</a>
                                <a href="${path}/biz/loanapplication/view?id=${v.id}">查看</a>
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

<div id="passDiv" data-backdrop="static" class="modal fade" role="dialog">
    <div class="modal-dialog plupload modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title text-center text-success"><b>通过</b></h5>
            </div>
            <form>
                <div class="form-group">
                    <textarea id="passProcessingOpinion" rows="6" class="form-control"></textarea>
                </div>
                <div class="form-group text-center">
                    <input type="hidden" id="passId">
                    <button type="button" class="btn btn-success" onclick="javascript: doPass();">确定</button>
                    <button type="button" class="btn btn-warning" onclick="javascript: $('#passDiv').modal('hide');">取消</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div id="nopassDiv" data-backdrop="static" class="modal fade" role="dialog">
    <div class="modal-dialog plupload modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h5 class="modal-title text-center text-success"><b>不通过</b></h5>
            </div>
            <form>
                <div class="form-group">
                    <label>选择原因</label>
                    <select id="nopassReason" onchange="setNopassProcessingOpinion(this.value);">
                        <c:forEach items="${nopassReasonTitles}" var="item">
                            <option value="${item.enumKey}"
                            <c:if test="${item.enumKey == v.nopassReason}">
                                selected
                            </c:if>
                            >${item.enumValue}</option>
                        </c:forEach>
                        <option value="0"
                        <c:if test="${v.nopassReason == null || v.nopassReason == 0}">
                            selected
                        </c:if>
                        >其他</option>
                    </select>
                </div>
                <div class="form-group">
                    <textarea id="nopassProcessingOpinion" rows="6" class="form-control"></textarea>
                </div>
                <div class="form-group text-center">
                    <input type="hidden" id="nopassId">
                    <button type="button" class="btn btn-success" onclick="javascript: doNopass();">确定</button>
                    <button type="button" class="btn btn-warning" onclick="javascript: $('#nopassDiv').modal('hide');">取消</button>
                </div>
            </form>
        </div>
    </div>
</div>

<div id="tips" class="modal fade">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="mySmallModalLabel">Tips</h4>
            </div>
            <div class="modal-body tips"></div>
        </div>
    </div>
</div>

<script type="text/javascript">
    function setNopassProcessingOpinion(titleId) {
        var nopassProcessingOpinion = $("#nopassProcessingOpinion");
        if (titleId == 0) {
            nopassProcessingOpinion.val("");
            nopassProcessingOpinion.attr("disabled", false);
        } else {
            $.ajax({
                type : "get",
                url : path + "/biz/loanapplication/nopassReasonContent",
                dataType : "json",
                data : {
                    key : titleId
                },
                success:function(data) {
                    nopassProcessingOpinion.val(data);
                    nopassProcessingOpinion.attr("disabled", true);
                }/*,
                 error : function() {
                 alert("异常！");
                 }*/
            });
        }
    }

    function showPassDiv(id){
        $('#passId').val(id);
        $('#passDiv').modal('show');
    }

    function showNopassDiv(id){
        $('#nopassId').val(id);
        $('#nopassDiv').modal('show');
    }

    $('#searchBtn').bind('click', function() {
        var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $("#dataform").submit();
        layer.close();
    });

    function exportLoanApplication() {
        var form = $("#dataform");
        form.attr('action',path + "/biz/loanapplication/export");
        form.submit();
        form.attr('action',path + "/biz/loanapplication");
    }

    function pagination(page) {
        $('#page').val(page);
            var index = layer.load(0, {
                shade: [0.4,'#fff',false] //0.1透明度的白色背景
            });
            $("#dataform").submit();
            layer.close();
    }

    function doPass() {
        var id = $("#passId").val();
        var processingOpinion = $.trim($("#passProcessingOpinion").val());
        if (processingOpinion.length > 1000) {
            $(".modal-body.tips").html("<p class='text-danger'><b>最多输入1000字</b></p>");
            $("#tips").modal("show");
        } else {
            if (id) {
                $.ajax({
                    method: "post",
                    url: path + "/biz/loanapplication/pass",
                    dataType: "json",
                    data: {
                        id : id,
                        processingOpinion : processingOpinion
                    }
                }).success(function (event) {
                    if (event.code == 200) {
                        $('#passDiv').modal("hide");
                        $(".modal-body.tips").html("<p class='text-success'><b>操作成功</b></p>");
                        $("#tips").modal("show").on('hide.bs.modal', function (e) {
                            window.location.reload(true);
                            return false;
                        });
                    } else {
                        $(".modal-body.tips").html("<p class='text-success'><b>请求失败</b></p>");
                        $("#tips").modal("show");
                        return false;
                    }
                }).error(function (event) {
                    $('#passDiv').modal("hide");
                    $(".modal-body.tips").html("<p class='text-danger'><b>服务器异常</b></p>");
                    $("#tips").modal("show");
                    return false;
                });
            } else {
                $(".modal-body.tips").html("<p class='text-danger'><b>参数不能为空</b></p>");
                $("#tips").modal("show");
                return false;
            }
        }

    }

    function doNopass() {
        var id = $("#nopassId").val();
        var processingOpinion = $.trim($("#nopassProcessingOpinion").val());
        var nopassReason = $("#nopassReason").val();
        if (id && processingOpinion) {
            $.ajax({
                method: "post",
                url: path + "/biz/loanapplication/nopass",
                dataType: "json",
                data: {
                    id : id,
                    processingOpinion : processingOpinion,
                    nopassReason : nopassReason
                }
            }).success(function (event) {
                if (event.code == 200) {
                    $('#nopassDiv').modal("hide");
                    $(".modal-body.tips").html("<p class='text-success'><b>操作成功</b></p>");
                    $("#tips").modal("show").on('hide.bs.modal', function (e) {
                        window.location.reload(true);
                        return false;
                    });
                } else {
                    $(".modal-body.tips").html("<p class='text-success'><b>请求失败</b></p>");
                    $("#tips").modal("show");
                    return false;
                }
            }).error(function (event) {
                $('#nopassDiv').modal("hide");
                $(".modal-body.tips").html("<p class='text-danger'><b>服务器异常</b></p>");
                $("#tips").modal("show");
                return false;
            });
        } else {
            $(".modal-body.tips").html("<p class='text-danger'><b>参数不能为空</b></p>");
            $("#tips").modal("show");
            return false;
        }
    }

</script>
