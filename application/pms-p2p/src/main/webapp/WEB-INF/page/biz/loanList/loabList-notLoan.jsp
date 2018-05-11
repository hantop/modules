<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<div class="">
    <div class="col-md-12" id="notLoan_container" style="margin-top: 20px;">
        <div class="col-md-12" style="margin-bottom: 10px;">
            <form class="form-inline form-search" role="form" id="notLoan_query">
                <div class="form-group">
                    <label>标的名称：</label>
                    <input id="title" name="title" type="text" class="form-control input-sm"
                           maxlength="60" style="width:163px;"
                           value="${loanListForm.title}"/>
                    &nbsp;&nbsp;
                </div>
                <input type="hidden" id="status" name="status" value="YLB">
                <input type="hidden" name="operator" value="<shiro:principal/>" id="operator">
                <shiro:hasPermission name="loanList:search">
                <button type="submit" class="btn btn-primary">查询</button>
                </shiro:hasPermission>
            </form>
            <hr/>
        </div>
        <div class="col-md-12">
            <table id="notLoan-table" class="table table-striped table-bordered table-condensed">
                <thead>
                <tr class="success">
                    <th style="vertical-align:middle; text-align:center;">标的名称</th>
                    <th style="vertical-align:middle; text-align:center;">借款用户</th>
                    <th style="vertical-align:middle; text-align:center;">借款账户</th>
                    <th style="vertical-align:middle; text-align:center;">收款用户</th>
                    <th style="vertical-align:middle; text-align:center;">收款账户</th>
                    <th style="vertical-align:middle; text-align:center;">借款金额(元)</th>
                    <th style="vertical-align:middle; text-align:center;">投资金额(元)</th>
                    <th style="vertical-align:middle; text-align:center;">借款年利率</th>
                    <th style="vertical-align:middle; text-align:center;">投资年利率</th>
                    <th style="vertical-align:middle; text-align:center;">借款期限</th>
                    <th style="vertical-align:middle; text-align:center;">还款方式</th>
                    <th style="vertical-align:middle; text-align:center;">满标时间</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
            <div class="m-pagination text-center" id="pagination_tab2"></div>
        </div>
    </div>
</div>

<script id="notLoan-template" type="text/x-jsrender">
    <tr>
        <td>
            {{:title}}
         </td>
         <td>
             {{:borrowerAccount}}
         </td>
         <td>
            {{:borrowerName}}
         </td>
         <td>
             {{:receiptAccount}}
         </td>
         <td>
            {{:receiptName}}
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
    </tr>
</script>
<script type="application/javascript">
    $(function ($) {
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
                    var param = $('#notLoan_query').serializeObject();
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
                        $("#notLoan-table tbody").empty().html($("#notLoan-template").render(data.list));
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

        $('#notLoan_query').submit(function () {
            $('#pagination_tab2').trigger('tab.page');
            return false;
        });

        $('#pagination_tab2').on('tab.page',function() {
            var param = $('#notLoan_query').serializeObject();
            var tab = $('#notLoan_container');
            $(this).pagination('setPageIndex', 0);
            var param = $.extend({}, param);
            $(this).pagination('setParams', param);
            $(this).pagination('setPageIndex', 0);
            $(this).pagination('remote');
        });
    });

</script>

