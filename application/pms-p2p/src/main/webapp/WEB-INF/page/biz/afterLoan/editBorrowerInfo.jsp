<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags" %>

<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a>业务管理</a></li>
                <li class="active"><a>贷后管理</a></li>
                <li class="active"><b>编辑页面</b></li>
            </ol>
            <div class="table-responsive">
                <table id="datatable" class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th >用户账户</th>
                        <th >用户名称</th>
                        <th >标的名称</th>
                        <th >借款金额</th>
                        <th >发布期数</th>
                    </tr>
                    </thead>
                        <tr>
                            <td>${loanlist.borrowerAccount}</td>
                            <td>${loanlist.borrowerName}</td>
                            <td>${loanlist.title}</td>
                            <td>${loanlist.borrowMoney}</td>
                            <td>${info.publishTimes}</td>
                        </tr>
                    </tbody>
                    <div class="m-pagination text-center" id="pagination_tab1"></div>
                </table>
            </div>
        </div>
        <div class="col-md-12">

            <form:form class="form-horizontal" role="form"  method="post" id="addInfo"
                       commandName="info">
                <form:hidden path="bidId"/>

                <div class="form-group ">
                    <label class="col-sm-2 control-label text-right">借款资金运用情况：</label>
                    <div class="col-sm-6">
                        <form:textarea path="useDetail" cssClass="form-control" maxlength="40"   rows="3"></form:textarea>
                    </div>
                </div>


                <div class="form-group ">
                    <label class="col-sm-2 control-label text-right">借款人经营状况及财务状况：</label>
                    <div class="col-sm-6">
                        <form:textarea path="financeDetail" cssClass="form-control" maxlength="40"   rows="3"></form:textarea>
                    </div>
                </div>

                <div class="form-group ">
                    <label class="col-sm-2 control-label text-right">借款人还款能力变化情况：</label>
                    <div class="col-sm-6">
                        <form:textarea path="repayAbility" cssClass="form-control" maxlength="40"   rows="3"></form:textarea>
                    </div>
                </div>
                <div class="form-group ">
                    <label class="col-sm-2 control-label text-right">借款人逾期情况：</label>
                    <div class="col-sm-6">
                        <form:textarea path="overdueDetail" cssClass="form-control" maxlength="40"   rows="3"></form:textarea>
                    </div>
                </div>
                <div class="form-group ">
                    <label class="col-sm-2 control-label text-right"> 借款人涉诉情况：</label>
                    <div class="col-sm-6">
                        <form:textarea path="lawsuitDetail" cssClass="form-control" maxlength="40"   rows="3"></form:textarea>
                    </div>
                </div>

                <div class="form-group ">
                    <label class="col-sm-2 control-label text-right">借款人受行政处罚情况：</label>
                    <div class="col-sm-6">
                        <form:textarea path="punishDetail" cssClass="form-control" maxlength="40"   rows="3"></form:textarea>
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <button type="button" onclick="submit_edit()" class="btn btn-primary btn-sm">提交</button>
                        <a type="button" class="btn btn-default btn-sm" href="${path}/biz/afterLoan/aferLoanList">取消</a>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>




<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script>
    /**
     * 提交
     */
    function submit_edit(){
        var formData = $('#addInfo').serialize();
        bootbox.confirm({
            title: '提示',
            size: 'small',
            message: "<span style='font-size: 13px;font-style: inherit'>确定提交？</span>",
            callback: function (result) {
                if (result) {
                    $.ajax({
                        cache : false,
                        type : "POST",
                        data : formData,
                        url : "${path}/biz/afterLoan/saveOrUpdate?timestamp=" + new Date().getTime(),
                        async : true,
                        dataType :"json",
                        error : function() {
                            bootbox.alert({message: "提交超时，请重新登录！！！", title: '错误信息'});
                        },
                        success : function(data, textStatus, XMLHttpRequest) {
                            console.log(data);
                            if(data.success){
                                bootbox.alert({message: "提交成功！！！", title: '操作信息',callback: function() {
                                    window.location.href="${path}/biz/afterLoan/aferLoanList";}});
                            }else{
                                bootbox.alert({message: "提交失败，请稍后再试！！！", title: '错误信息'});
                            }

                        }
                    });
                }
            }
        });




    }

</script>