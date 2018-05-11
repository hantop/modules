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
                <li class="active"><b>借款总览</b></li>
            </ol>
            <form id="dataform" class="form-inline" method="post" action="${path}/biz/loanOverview">
                <div class="row" style="margin-left: 5px">
                    <div class="form-group">
                        <label>发标日期：</label>
                        <input id="tenderStartDate" name="tenderStartDate" type="text" readonly="readonly" maxlength="20"
                               class="form-control input-sm startDate" style="width:110px;"
                               value="${loanOverviewForm.tenderStartDate}"
                               onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
                        --
                        <input id="tenderEndDate" name="tenderEndDate" type="text" readonly="readonly" maxlength="20"
                               class="form-control input-sm endDate" style="width:110px;"
                               value="${loanOverviewForm.tenderEndDate}"
                               onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
                        &nbsp;&nbsp;             
                        <label>放款日期：</label>
                        <input id="loanStartDate" name="loanStartDate" type="text" readonly="readonly" maxlength="20"
                               class="form-control input-sm startDate" style="width:110px;"
                               value="${loanOverviewForm.loanStartDate}"
                               onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
                        --
                        <input id="loanEndDate" name="loanEndDate" type="text" readonly="readonly" maxlength="20"
                               class="form-control input-sm endDate" style="width:110px;"
                               value="${loanOverviewForm.loanEndDate}"
                               onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
                        &nbsp;&nbsp;            
                        <label>还款日期：</label>
                        <input id="repayStartDate" name="repayStartDate" type="text" readonly="readonly" maxlength="20"
                               class="form-control input-sm startDate" style="width:110px;"
                               value="${loanOverviewForm.repayStartDate}"
                               onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
                        --
                        <input id="repayEndDate" name="repayEndDate" type="text" readonly="readonly" maxlength="20"
                               class="form-control input-sm endDate" style="width:110px;"
                               value="${loanOverviewForm.repayEndDate}"
                               onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
                    </div>
                </div>
                <div class="row" style="margin-left: 5px;margin-top:10px;">
                    <label>状态</label>
                    <select id="bidStatus" name="bidStatus" class="form-control input-sm"
                            style="width:115px;" value="${loanOverviewForm.bidStatus}">
                        <option value="">全部</option>
                        <option value="DFB"
                                <c:if test='${loanOverviewForm.bidStatus == "DFB"}'>selected="selected"</c:if>>待发布</option>
                        <option value="YFB"
                                <c:if test='${loanOverviewForm.bidStatus == "YFB"}'>selected="selected"</c:if>>预发布</option>
                        <option value="TBZ"
                                <c:if test='${loanOverviewForm.bidStatus == "TBZ"}'>selected="selected"</c:if>>投标中</option>
                        <option value="DFK"
                                <c:if test='${loanOverviewForm.bidStatus == "DFK"}'>selected="selected"</c:if>>待放款</option>
                        <option value="HKZ"
                                <c:if test='${loanOverviewForm.bidStatus == "HKZ"}'>selected="selected"</c:if>>还款中</option>
                        <option value="YJQ"
                                <c:if test='${loanOverviewForm.bidStatus == "YJQ"}'>selected="selected"</c:if>>已结清</option>
                        <option value="YLB"
                                <c:if test='${loanOverviewForm.bidStatus == "YLB"}'>selected="selected"</c:if>>已流标</option>
                        <option value="YZF"
                                <c:if test='${loanOverviewForm.bidStatus == "YZF"}'>selected="selected"</c:if>>已作废</option>
                    </select>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <label>产品类型：</label>
                    <select id="productType" name="productType" class="form-control input-sm"
                            style="width:150px;" value="${loanOverviewForm.productType}">
                        <option value="">全部(不含消费信贷)</option>
                        <c:forEach items="${productTypes}" var="productType">
                            <c:choose>
                                <c:when test="${productType.key eq loanOverviewForm.productType}">
                                    <option value="${productType.key}" selected="selected">${productType.value}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${productType.key}">${productType.value}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <label>系统类型：</label>
                    <select id="sysType" name="sysType" class="form-control input-sm"
                            style="width:115px;" value="${loanOverviewForm.sysType}">
                        <option value="">全部</option>
                        <option value="2" <c:if test='${loanOverviewForm.sysType == "2"}'>selected="selected"</c:if>>存管</option>
                        <option value="1" <c:if test='${loanOverviewForm.sysType == "1"}'>selected="selected"</c:if>>普通</option>
                    </select>
                </div>
                <div class="row" style="margin-left: 5px;margin-top:10px;">
                    <div class="form-group">
                        <label>借款标题：</label>
                        <input id="bidTitle" name="bidTitle" type="text" class="form-control input-sm"
                               maxlength="200" style="width:163px;"
                               value="${loanOverviewForm.bidTitle}"/>
                        &nbsp;&nbsp;
                        <label>借款账户：</label>
                        <input id="borrowerAccount" name="borrowerAccount" type="text" class="form-control input-sm"
                               maxlength="60" style="width:163px;"
                               value="${loanOverviewForm.borrowerAccount}"/>
                        &nbsp;&nbsp;
                        <label>借款人姓名：</label>
                        <input id="borrowerName" name="borrowerName" type="text" class="form-control input-sm"
                               maxlength="30" style="width:163px;"
                               value="${loanOverviewForm.borrowerName}"/>
                        &nbsp;&nbsp;
                    </div>
                    <input type="hidden" id="page" name="page">
                    <input type="hidden" id="fromPage" name="fromPage" value="true">
                    <input type="hidden" id="sort" name="sort" value="${loanOverviewForm.sort}">
                    <shiro:hasPermission name="loanOverview:search">
                        <button type="button" class="btn btn-primary btn-sm" id="searchBtn">搜索</button>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="loanOverview:export">
                        <button type="button" class="btn btn-success btn-sm" onclick="javascript: exportloanOverview();">导出</button>
                    </shiro:hasPermission>
                </div>
            </form>
            <hr style="margin-bottom: 0;">
            <div class="panel panel-default" style="width:2100px!important;">
                <div class="panel-heading">借款信息</div>
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                        <tr class="success">
							<th style="vertical-align:middle; text-align:center;">序号</th>
							<th style="vertical-align:middle; text-align:center;">系统类型</th>
							<th style="vertical-align:middle; text-align:center;">产品类型</th>
							<th style="vertical-align:middle; text-align:center;">借款标题</th>
							<th style="vertical-align:middle; text-align:center;">借款账户</th>
							<th style="vertical-align:middle; text-align:center;">借款人姓名</th>
							<th style="vertical-align:middle; text-align:center;">合同金额(元)</th>
							<th style="vertical-align:middle; text-align:center;">放款金额(元)</th>
							<th style="vertical-align:middle; text-align:center;">应还本金(元)</th>
							<th style="vertical-align:middle; text-align:center;">应还利息(元)</th>
							<th style="vertical-align:middle; text-align:center;">成交服务费<br>(应还利息中已含)</th>
							<th style="vertical-align:middle; text-align:center;">其他费用<br>(逾期/提前还款产生)</th>
							<th style="vertical-align:middle; text-align:center;">借款端利率</th>
							<th style="vertical-align:middle; text-align:center;">投资端利率</th>
							<th style="vertical-align:middle; text-align:center;">期限</th>
							<th style="vertical-align:middle; text-align:center;">发标日期</th>
							<th style="vertical-align:middle; text-align:center;">满标日期</th>
							<th style="vertical-align:middle; text-align:center;"><a href="javascript:void(0);" onclick="sortLoanDateFunction();" style="text-decoration:underline;">&nbsp;放款日期&nbsp;<span id="loanDateIcon" class="glyphicon"></span></a></th>
							<th style="vertical-align:middle; text-align:center;"><a href="javascript:void(0);" onclick="sortRepayDateFunction();" style="text-decoration:underline;">&nbsp;还款日期&nbsp;<span id="repayDateIcon" class="glyphicon"></span></a></th>
							<th style="vertical-align:middle; text-align:center;">还款方式</th>
							<th style="vertical-align:middle; text-align:center;">经办人</th>
							<th style="vertical-align:middle; text-align:center;">状态</th>
							<!-- <th style="vertical-align:middle; text-align:center;">操作</th> -->
						</tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${loanOverviewList}" var="v" varStatus="vs">
                        <tr>
                            <td>${vs.index + 1}</td>
                            <td>
                                <c:if test='${v.sysType == "2"}'>
                                    存管
                                </c:if>
                                <c:if test='${v.sysType == "1"}'>
                                    普通
                                </c:if>
                            </td>
                            <td>${v.productType}</td>
                            <td>${v.bidTitle}</td>
                            <td>${v.borrowerAccount}</td>
                            <td>${v.borrowerName}</td>
                            <td>${v.borrowAmount}</td>
                            <td>${v.loanAmount}</td>
                            <td>${v.principal}</td>
                            <td>${v.interest}</td>
                            <td>${v.transactionFee}</td>
                            <td>${v.otherFee}</td>
                            <td>${v.borrowRate}</td>
                            <td>${v.investRate}</td>
                            <td>${v.cycle}</td>
                            <td>${v.tenderDate}</td>
                            <td>${v.fullDate}</td>
                            <td>${v.loanDate}</td>
                            <td>${v.repayDate}</td>
                            <td>${v.repayMode}</td>
                            <td>${v.operator}</td>
                            <td>${v.bidStatus}</td>
                            <!-- <td><a href="javascript:void(0);">查看</a></td> -->
                        </tr>
                    </c:forEach>
                    <tfoot>
                        <tr>
                            <td>总计</td>
                            <td colspan="6"></td>
                            <td>${totalMap.principalTotal}</td>
                            <td>${totalMap.interestTotal}</td>
                            <td colspan="12"></td>
                        </tr>
                    </tfoot>
                    </tbody>
                </table>
            </div>
            <tiles:insertDefinition name="paginator"/>
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
    $(function(){
    	if("${loanOverviewForm.sort}"=="loanDate"){
    		$("#loanDateIcon").addClass("glyphicon-sort-by-attributes-alt");
    	}
    	else if("${loanOverviewForm.sort}"=="repayDate"){
    		$("#repayDateIcon").addClass("glyphicon-sort-by-attributes-alt");
    	}
    	
    	
        $('#dataform').on('submit',function() {
        	var startDateArray=new Array();
        	var endDateArray=new Array();
        	startDateArray[0]= $('#tenderStartDate').val();
        	endDateArray[0]= $('#tenderEndDate').val();
        	startDateArray[1]= $('#loanStartDate').val();
        	endDateArray[1]= $('#loanEndDate').val();
        	startDateArray[2] = $('#repayStartDate').val();
        	endDateArray[2] = $('#repayEndDate').val();
        	
        	if(startDateArray[0]==''&&endDateArray[0]==''&&startDateArray[1]==''&&endDateArray[1]==''&&startDateArray[2]==''&&endDateArray[2]==''){
            	layer.closeAll();
                $(".modal-body.tips").html("<p class='text-danger'><b>【请设置时间范围】</b></p>");
                $("#tips").modal("show");
                return false;        		
        	}

        	for(var i=0;i<3;i++){
            var staTime = startDateArray[i];
            var endTime = endDateArray[i];
            if(staTime != '' && endTime != ''){
                var d1 = new Date(staTime.replace(/-/g,"/"));
                var d2 = new Date(endTime.replace(/-/g,"/"));

                var beginDateSplit = staTime.split('-');
                var endDateSplit = endTime.split('-');
                var beginDateMonths = parseInt(beginDateSplit[0]) * 12 + parseInt(beginDateSplit[1]);
                var endDateMonths = parseInt(endDateSplit[0]) * 12 + parseInt(endDateSplit[1]);

                if (Date.parse(d1) - Date.parse(d2) > 0) {
                	layer.closeAll();
                    $(".modal-body.tips").html("<p class='text-danger'><b>【结束时间不能早于开始时间】</b></p>");
                    $("#tips").modal("show");
                    return false;
                }
                else if(endDateMonths - beginDateMonths > 6){
                	layer.closeAll();
                    $(".modal-body.tips").html("<p class='text-danger'><b>【跨度月数不能大于6个月】</b></p>");
                    $("#tips").modal("show");   
                    return false;
                }
            }

        	}
        	
            return true;
        });
        
    });


    $('#searchBtn').bind('click', function() {
        var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $("#dataform").submit();
        layer.close();
    });

    function exportloanOverview() {
        var form = $("#dataform");
        form.attr('action',path + "/biz/loanOverview/export");
        form.submit();
        form.attr('action',path + "/biz/loanOverview");
    }

    function pagination(page) {
        $('#page').val(page);
            var index = layer.load(0, {
                shade: [0.4,'#fff',false] //0.1透明度的白色背景
            });
            $("#dataform").submit();
            layer.close();
    }
    
    function sortLoanDateFunction(){
    	if($("#sort").val()=="loanDate"){
    		$("#sort").val("originOrder");
    	}
    	else{
    		$("#sort").val("loanDate");
    	}
    	$("#dataform").submit();
    }
    
    function sortRepayDateFunction(){
    	if($("#sort").val()=="repayDate"){
    		$("#sort").val("originOrder");
    	}
    	else{
    		$("#sort").val("repayDate");
    	}
    	$("#dataform").submit();
    }
</script>
