<%--
  Created by IntelliJ IDEA.
  User: wangyunjing
  Date: 2015/11/6
  Time: 17:31
  To change this template use File | Settings | File Templates.
--%>
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
        <div class="col-md-12" >
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/statistics/index">统计管理</a></li>
                <li class="active"><b>积分统计</b><img title="符合查询时间内已回款或计划回款的标的,债权转出不含,但包含转入债权的回款" width="15px" src="/static/image/help.png"/>
            </ol>
            <form id="dataform" class="form-inline" method="post" action="${path}/statistics/integrate">
                <div class="form-group">
                    <label>统计日期：</label>
                    <input id="startDate" name="startDate" type="text" readonly="readonly" maxlength="20"
                           class="form-control input-sm" style="width:163px;"
                           value="${integrateForm.startDate}"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
                    --
                    <input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20"
                           class="form-control input-sm" style="width:163px;"
                           value="${integrateForm.endDate}"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
                    &nbsp;
                    <label>商品编码：</label>
                    <input id="productCode" name="productCode" type="text" class="form-control input-sm"
                           value="${integrateForm.productCode}"/>
                    <label>商品名称：</label>
                    <input id="typeName" name="typeName" type="text" class="form-control input-sm"
                           value="${integrateForm.typeName}"/>
                    <input id="cmd" name="cmd" value="${integrateForm.cmd}" type="hidden">
                </div>
                <input type="hidden" id="page" name="page">
                <input type="hidden" id="fromPage" name="fromPage" value="true">
                <shiro:hasPermission name="statisticsintegrate:search">
                    <button  type="button" class="btn btn-primary btn-sm" id="searchBtn">搜索</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="statisticsintegrate:export">
                    <button type="button" class="btn btn-success btn-sm" onclick="javascript: exportintegrate();">导出</button>
                </shiro:hasPermission>
            </form>
            <hr style="margin-bottom: 0;">
            <div class="panel panel-default">
                <div class="panel-heading">积分统计信息</div>
                <table id="integrateTable" class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>商品编码<img title="对应商品的商品编码,没有的则为空(如手机充值)" width="15px" src="/static/image/help.png"/></th>
                        <th>商品名称<img title="对应商品的商品名称" width="15px" src="/static/image/help.png"/></th>
                        <th>剩余库存 <img title="商品的剩余库存(手机充值为剩余金额)" width="15px" src="/static/image/help.png"/></th>
                        <th>商品销量</th>
                        <th>购买人数</th>
                        <th>消耗总积分<button id="sort" type="button" onclick="javascript: sort();"><span class="glyphicon glyphicon-sort"></span></button></th>
                        <th>总现金<img title="卖出该商品所获取的现金数（部分商品需要积分+现金购买）。" width="15px" src="/static/image/help.png"/></th>
                    </tr>
                    </thead>
                    <tbody id="integrate">
                    <c:forEach items="${integrateList}" var="v" varStatus="vs">
                        <tr>
                            <td>${v.productCode}</td>
                            <td>${v.typeName}</td>
                            <td>${v.restStock}</td>
                            <td>${v.saleAmount}</td>
                            <td>${v.purchaseNumber}</td>
                            <td>${v.integrateCost}</td>
                            <td>${v.totalAmount}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <tiles:insertDefinition name="paginator"/>
            <input id="currentpage" type="hidden" value="${number}"/>
            <input id="pagelimit" type="hidden" value="${paginator.pages}"/>
        </div>
    </div>
</div>
<script type="text/javascript">
/* 点击积分可以进行升序降序 */
	function sort(){
		//console.log("dasdsfads" + $("#cmd").val());
		 var term = false;
        var staTime = $("#startDate").val();
        var endTime = $("#endDate").val();

        if (staTime == '' && endTime == '') {
            alert("统计日期不能为空");
            return;
        }

        var startDateSplit = staTime.split('-');
        var endDateSplit = endTime.split('-');
        var startDateMonths = parseInt(startDateSplit[0]) * 12 + parseInt(startDateSplit[1]);
        var endDateMonths = parseInt(endDateSplit[0]) * 12 + parseInt(endDateSplit[1]);
        if (endDateMonths - startDateMonths > 6) {
            alert("【统计起止月份不能大于6个月】");
            return false;
        }

        if (staTime != '' && endTime != '') {
            var d1 = new Date(staTime.replace(/-/g, "/"));
            var d2 = new Date(endTime.replace(/-/g, "/"));
            if (Date.parse(d1) - Date.parse(d2) > 0) {
                alert("【统计日期结束时间不能早于开始时间】");
                return;
            }
        }
         if ($("#cmd").val() == "DESC") {
        	cmd = "ASC";
        }else{
        	cmd = "DESC";
        }
        		$("#cmd").val(cmd);
        console.log(cmd); 
        var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $("#dataform").submit(); 
        layer.close(); 
        
	}
	
	//导出
    function exportintegrate() {
        var form = $("#dataform");
        form.attr('action',path + "/statistics/integrate/export");
        form.submit();
        form.attr('action',path + "/statistics/integrate");
    }

    $('#searchBtn').bind('click', function () {
        var term = false;
        var staTime = $("#startDate").val();
        var endTime = $("#endDate").val();

        if (staTime == '' && endTime == '') {
            alert("统计日期不能为空");
            return;
        }

        var startDateSplit = staTime.split('-');
        var endDateSplit = endTime.split('-');
        var startDateMonths = parseInt(startDateSplit[0]) * 12 + parseInt(startDateSplit[1]);
        var endDateMonths = parseInt(endDateSplit[0]) * 12 + parseInt(endDateSplit[1]);
        if (endDateMonths - startDateMonths > 6) {
            alert("【统计起止月份不能大于6个月】");
            return false;
        }

        if (staTime != '' && endTime != '') {
            var d1 = new Date(staTime.replace(/-/g, "/"));
            var d2 = new Date(endTime.replace(/-/g, "/"));
            if (Date.parse(d1) - Date.parse(d2) > 0) {
                alert("【统计日期结束时间不能早于开始时间】");
                return;
            }
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
        $('#dataform').submit();
        layer.closeAll();
    }
</script>
