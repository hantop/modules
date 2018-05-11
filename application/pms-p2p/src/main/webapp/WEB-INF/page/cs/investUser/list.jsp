
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
                <li class="active"><a>客服管理</a></li>
                <li class="active"><b>投资用户</b></li>
            </ol>
            <div>
                <div id="msg"><span style="margin-left:50px;color: red;font-family: 'Arial Black'">${msg}</span></div>
                <form id="dataform" class="form-inline" method="post" action="${path}/cs/investUser/list">
                    <input type="hidden" id="page" name="page">
                    <input type="hidden" id="fromPage" name="fromPage" value="true">

                    <div class="form-group">
                        <label>用户手机号</label>
                        <input type="text" class="form-control input-sm" name="moblie" id="moblie" value="${moblie}">
                    </div>
                    <div class="form-group">
                        <label>用户名称</label>
                        <input type="text" class="form-control input-sm" name="username"  id="username" value="${username}">

                    </div>

                    <div class="form-group">
                        <label>用户身份证号</label>
                        <input type="text" class="form-control input-sm" name="idCard"  id="idCard" value="${idCard}">

                    </div>
                    <button onclick="searchList()" type="button" class="btn btn-primary btn-sm">查询</button>
                </form>
            </div>
            <hr>
            <div class="table-responsive">
                <table id="datatable" class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th >用户ID</th>
                        <th >用户手机号</th>
                        <th >用户名称</th>
                        <th >用户身份证号</th>
                        <th >总账户余额（元）</th>
                        <th >总在投金额</th>
                        <th >操作</th>

                    </tr>
                    </thead>


                    <c:forEach items="${userDetailList}" var="v" varStatus="vs">
                        <tr>
                            <td>${v.userId}</td>
                            <td>${v.phoneNum}</td>
                            <td>${v.name}</td>
                            <td>${v.idCard}</td>
                            <td>
                                 <c:if test="${userDetailInfo.balance == -1414}">0.00</c:if>
                                 <c:if test="${userDetailInfo.balance != -1414}">${userDetailInfo.balance}</c:if>
                            </td>
                            <td>
                                 <c:if test="${userDetailInfo.investingMoney == -1414}">0.00</c:if>
                                 <c:if test="${userDetailInfo.investingMoney != -1414}">${userDetailInfo.investingMoney}</c:if>
                            </td>
                            <td><a href="javascript:detail(${v.userId});">查看</a></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

    </div>
</div>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>

<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script>
    /**
     * 查看
     */
    function detail(userId){
        window.location.href="${path}/cs/investUser/getDetailInfo?userId="+userId;
    }

    /**
     * 搜索
     */
    function searchList(){
        var moblie = $("#moblie").val();
        var username = $("#username").val();
        var idCard = $("#idCard").val();
        if(moblie==""&&username==""&&idCard==""){
            bootbox.alert({message: "<h3>请至少填写一项</h3>", title: '错误信息'});
        }else{
            var index = layer.load(0, {
                shade: [0.4,'#fff',false] //0.1透明度的白色背景
            });
            $("#dataform").submit();
        }
    }

</script>
