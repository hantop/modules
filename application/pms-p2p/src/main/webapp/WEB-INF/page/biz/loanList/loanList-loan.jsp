<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div class="">
    <div class="col-md-14" id="loan_container" style="margin-top: 20px;">
        <div class="col-md-12" style="margin-bottom: 10px;">
            <form class="form-inline form-search" role="form" id="loan_query">
                <div class="form-group">
                    <label>标的名称：</label>
                    <input id="title" name="title" type="text" class="form-control input-sm"
                           maxlength="60" style="width:163px;"
                           value="${loanListForm.title}"/>
                    &nbsp;&nbsp;
                    <label>借款账号：</label>
                    <input id="borrowerAccount" name="borrowerAccount" type="text" class="form-control input-sm"
                           maxlength="30" style="width:163px;"
                           value="${loanListForm.borrowerAccount}"/>
                    &nbsp;&nbsp;
                    <label>收款账号：</label>
                    <input id="receiptAccount" name="receiptAccount" type="text" class="form-control input-sm"
                           maxlength="200" style="width:163px;"
                           value="${loanListForm.receiptAccount}"/>
                    &nbsp;&nbsp;
                    <label>状态：</label>
                    <select name="status" class="form-control">
                        <option value="">全部</option>
                        <option value="DFK">待放款</option>
                        <option value="1">放款中</option>
                        <option value="0">放款失败</option>
                    </select>
                    &nbsp;&nbsp;
                </div>
                <input type="hidden" name="operator" value="<shiro:principal/>" id="operator">
                <shiro:hasPermission name="loanList:search">
                <button type="submit" class="btn btn-primary">查询</button>
                </shiro:hasPermission>
            </form>
            <hr/>
        </div>
        <div class="col-md-14">
            <table id="loan-table" class="table table-striped table-bordered table-condensed">
                <thead>
                <tr class="success">
                    <th style="vertical-align:middle; text-align:center;">标的名称</th>
                    <th style="vertical-align:middle; text-align:center;">借款用户</th>
                    <th style="vertical-align:middle; text-align:center;">借款账号</th>
                    <th style="vertical-align:middle; text-align:center;">收款用户</th>
                    <th style="vertical-align:middle; text-align:center;">收款账户</th>
                    <th style="vertical-align:middle; text-align:center;">借款金额(元)</th>
                    <th style="vertical-align:middle; text-align:center;">投资金额(元)</th>
                    <th style="vertical-align:middle; text-align:center;">借款年利率</th>
                    <th style="vertical-align:middle; text-align:center;">投资年利率</th>
                    <th style="vertical-align:middle; text-align:center;">借款期限</th>
                    <th style="vertical-align:middle; text-align:center;">还款方式</th>
                    <th style="vertical-align:middle; text-align:center;">满标时间</th>
                    <th style="vertical-align:middle; text-align:center;">状态</th>
                    <th style="vertical-align:middle; text-align:center;">操作</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
            <div class="m-pagination text-center" id="pagination_tab1"></div>
        </div>
    </div>
</div>

<script id="loan-template" type="text/x-jsrender">
    <tr>
        <td>
            {{:title}}
         </td>
         <td>
            {{:borrowerName}}
         </td>
         <td>
            {{:borrowerAccount}}
         </td>
         <td>
            {{if receiptAccount === null || receiptAccount === ''}}
                 -
            {{else}}
                {{:receiptAccount}}
            {{/if}}
         </td>
         <td>
            {{if receiptName === null || receiptName === ''}}
                 -
            {{else}}
                {{:receiptName}}
            {{/if}}
         </td>
         <td>
            {{:borrowMoney}}
         </td>
         <td>
            {{:bidMoney}}
         </td>
         <td>
            {{:borroweRate}}%
         </td>
         <td>
            {{:investRate}}%
         </td>
         <td>
            {{:term}}
         </td>
         <td>
            {{if repayment === 'DEBX'}}
              等额本息
            {{else repayment === 'MYFX'}}
              每月付息,到期还本
            {{else repayment === 'YCFQ'}}
              本息到期一次付清
            {{else repayment === 'DEBJ'}}
              等额本金
            {{/if}}
         </td>
         <td>
            {{:fullDate}}
         </td>
         <td>
            {{:bidStatus}}
         </td>
         <td>
            {{if loan === 1}}
            <shiro:hasPermission name="loanList:loan">
            <a style="text-decoration: underline;"
            href="#" onclick="javascript:confirm({{:bidId}},'{{:title}}','{{:borrowerName}}','{{:bidMoney}}')"
            role="button">放款</a>&nbsp;&nbsp;&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="loanList:notLoan">
            <a style="text-decoration: underline;"
            href="#" onclick="javascript:notConfirm({{:bidId}},'{{:title}}','{{:borrowerName}}','{{:bidMoney}}')"
            role="button">流标</a>
            </shiro:hasPermission>
            {{else}}
            <shiro:hasPermission name="loanList:loan">
            <font color="#AAAAAA">放款</font>&nbsp;&nbsp;&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="loanList:notLoan">
            <font color="#AAAAAA">流标</font>&nbsp;&nbsp;&nbsp;&nbsp;
            </shiro:hasPermission>
            {{/if}}
         </td>
    </tr>
</script>
<script type="application/javascript">
    $(function ($) {
        $("#pagination_tab1").pagination({
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
                    var param = $('#loan_query').serializeObject();
                    return $.extend({},param);
                }(),
                url: '${path}/biz/loanList',
                beforeSend: function (XMLHttpRequest) {
                    var index = layer.load(0, {
                        shade: [0.4,'#fff',false] //0.1透明度的白色背景
                    });
                },
                totalName: 'total',
                pageSizeName: 'limit',
                pageIndexName: 'pageNum',
                success: function (data, pageIndex) {
                    if(data.list!=null){
                        $("#loan-table tbody").empty().html($("#loan-template").render(data.list));
                    }
                },
                complete: function (XMLHttpRequest, textStatu) {
                    layer.closeAll();
                }
            }
        }).on("pageClicked", function (event, pageIndex) {
        }).on('jumpClicked', function (event, pageIndex) {
        }).on('pageSizeChanged', function (event, pageSize) {
        });

        $('#loan_query').submit(function () {
            $('#pagination_tab1').trigger('tab.page');
            return false;
        });

        $('#pagination_tab1').on('tab.page',function() {
            var param = $('#loan_query').serializeObject();
            var tab = $('#loan_container');
            $(this).pagination('setPageIndex', 0);
            var param = $.extend({}, param);
            $(this).pagination('setParams', param);
            $(this).pagination('setPageIndex', 0);
            $(this).pagination('remote');
        });
    });

    function confirm(bid, title, accountName, bidMoney){
        bootbox.confirm({
            title : '放款确认:',
            size : 'small',
            message :
            "<span style='font-weight: bold;font-size: 15px'>标的名称:</span>&nbsp;&nbsp;&nbsp;<span style='font-weight: bold;font-size: 15px'>"+title+"</span>" +
            "<br/><span style='font-weight: bold;font-size: 15px'>借款人:</span>&nbsp;&nbsp;&nbsp;<span style='font-weight: bold;font-size: 15px'>"+accountName+"</span>" +
            "<br/><span style='font-weight: bold;font-size: 15px'>投标金额:</span>&nbsp;&nbsp;&nbsp;<span style='font-weight: bold;font-size: 15px'>"+bidMoney+"</span>" ,
            callback : function(result) {
                if(result){
                    var data = {bId: bid};
                    var index = layer.load(0, {
                        shade: [0.4,'#fff',false] //0.1透明度的白色背景
                    });
                    $.post(
                            path + "/biz/loanList/loan",
                            {
                                "bId" : bid
                            },
                            function (data) {
                                layer.closeAll();
                                var code = data.code;
                                var message = data.message;
                                if (code == '200' || code == '2000') {
                                    bootbox.alert({size: 'small',message:"放款成功！", callback: function () {
                                        window.location.reload();
                                    }});
                                } else {
                                    bootbox.alert({size: 'small',message:message, callback: function () {
                                        window.location.reload();
                                    }});
                                }
                            }
                    );
                }
            }
        });
    }

    function notConfirm(bid, title, accountName, bidMoney){
        bootbox.confirm({
            title : '流标确认:',
            size : 'small',
            message :
            "<span style='font-weight: bold;font-size: 15px'>标的名称:</span>&nbsp;&nbsp;&nbsp;<span style='font-weight: bold;font-size: 15px'>"+title+"</span>" +
            "<br/><span style='font-weight: bold;font-size: 15px'>借款人:</span>&nbsp;&nbsp;&nbsp;<span style='font-weight: bold;font-size: 15px'>"+accountName+"</span>" +
            "<br/><span style='font-weight: bold;font-size: 15px'>投标金额:</span>&nbsp;&nbsp;&nbsp;<span style='font-weight: bold;font-size: 15px'>"+bidMoney+"</span>" ,
            callback : function(result) {
                if(result){
                    $.post(
                            path + "/biz/loanList/notLoan",
                            {
                                "bId" : bid
                            },
                            function (data) {
                                var code = data.code;
                                var message = data.message;
                                if (code == '200' || code == '2000') {
                                    bootbox.alert({size: 'small',message:"流标成功！", callback: function () {
                                        window.location.reload();
                                    }});
                                } else {
                                    bootbox.alert({size: 'small',message:message, callback: function () {
                                        window.location.reload();
                                    }});
                                }
                            }
                    );
                }
            }
        });
    }
</script>

