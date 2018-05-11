
<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<style type="text/css">
    .table {
        table-layout: fixed;
    }

</style>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a>业务管理</a></li>
                <li class="active"><b>贷后管理</b></li>
            </ol>
            <div>
                <form id="dataform" class="form-inline" method="post" action="${path}/biz/afterLoan/aferLoanList">
                    <input type="hidden" id="page" name="page">
                    <input type="hidden" id="fromPage" name="fromPage" value="true">

                    <div class="form-group">
                        <label>用户账户</label>
                        <input type="text" class="form-control input-sm" name="borrowerAccount" id="borrowerAccount" value="${borrowerAccount}">
                    </div>


                    <div class="form-group">
                        <label>用户名称</label>
                        <input type="text" class="form-control input-sm" name="borrowerName"  id="borrowerName" value="${borrowerName}">

                    </div>
                    <button onclick="searchList()" type="button" class="btn btn-primary btn-sm">查询</button>
                </form>
            </div>
            <hr>
            <div class="table-responsive">
                <table id="datatable" class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th >序号</th>
                        <th >用户账户</th>
                        <th >用户名称</th>
                        <th >标的名称</th>
                        <th >借款金额</th>
                        <th >操作</th>

                    </tr>
                    </thead>

                    <c:forEach items="${list}" var="v" varStatus="vs">
                    <tr>
                        <td>${vs.index+1}</td>
                                <td>${v.borrowerAccount}</td>
                                <td>${v.borrowerName}</td>
                                <td>${v.title}</td>
                                <td>${v.borrowMoney}</td>
                                <td><a href="javascript:editInfo('${v.bidId}','${v.borrowerAccount}','${v.borrowerName}','${v.title}','${v.borrowMoney}');">编辑</a></td>
                    </tr>
                    </c:forEach>

                    </tbody>

                    <div class="m-pagination text-center" id="pagination_tab1"></div>
                </table>
            </div>
        </div>
        <tiles:insertDefinition name="paginator"/>
    </div>
</div>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>

<script>
    /**
     * 编辑
     */
    function editInfo(bidId,borrowerAccount,borrowerName,title,borrowMoney){
        window.location.href="../afterLoan/editBorrowerInfo?bidId="+bidId
            +"&borrowerAccount="+borrowerAccount
            +"&borrowerName="+encodeURI(borrowerName)
            +"&title="+encodeURI(title)
            +"&borrowMoney="+borrowMoney;
    }

    /**
     * 搜索
     */
    function searchList(){
        var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $("#dataform").submit();
    }

</script>
