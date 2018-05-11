<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<script type="text/javascript" src="/static/component/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/static/component/datepicker/calendar.js"></script>
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>

<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/finance/index">财务管理</a></li>
                <li class="active"><b>代充值管理</b></li>
            </ol>
            <div id="msg"><span style="margin-left:50px;color: red;font-family: 'Arial Black'">${msg}</span></div>
            <form:form id="dataform" commandName="replacementRechargeForm" method="post" class="form-inline" action="${path}/finance/replacementRecharge">
                <div class="form-group">
                    <label>用户账号：</label>
                    <form:input value="" type="text" cssClass="form-control input-sm" id="account" path="account" maxlength="20" style="width:110px;" />
                </div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <div class="form-group">
                    <label>用户类型：</label>
                    <form:select path="userType" cssClass="form-control">
                        <form:option value="">全部</form:option>
                        <form:options items="${userTypes}" itemLabel="label" itemValue="value"></form:options>
                    </form:select>
                </div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <div class="form-group">
                    <label>账户类型：</label>
                    <form:select path="userRole" cssClass="form-control">
                        <form:option value="">全部</form:option>
                        <form:option value="BORROWERS">借款用户</form:option>
                        <form:option value="GUARANTEECORP">担保用户</form:option>
                        <form:option value="INVESTOR">投资用户</form:option>
                    </form:select>
                </div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <div class="form-group">
                    <label>状态：</label>
                    <form:select path="status" cssClass="form-control">
                        <form:option value="">全部</form:option>
                        <form:option value="2">待审核</form:option>
                        <form:option value="11">已成功</form:option>
                        <form:option value="10">已失败</form:option>
                        <form:option value="0">审核不通过</form:option>
                    </form:select>
                </div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="hidden" id="page" name="page">
                <input id="principal" type="hidden" value="<shiro:principal/>">
                <shiro:hasPermission name="replacementRecharge:search">
                    <button type="button" class="btn btn-primary btn-sm" id="searchBtn">搜索</button>
                </shiro:hasPermission>
            </form:form>
            <hr style="margin-bottom: 0;">
            代充值账户余额: &nbsp;&nbsp;<label><font size="3px">${replacementRechargeAccountBalance}元</font></label>
            <hr style="margin-bottom: 0;">
            <form:form class="form-inline" >
                <shiro:hasPermission name="BORROWERS:edit">
                    <div class="form-group">
                        <a class="btn btn-primary btn-sm"
                           href="${path}/finance/replacementRecharge/edit?type=BORROWERS"
                           role="button">借款账户充值</a>
                    </div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                </shiro:hasPermission>

                <shiro:hasPermission name="GUARANTEECORP:edit">
                    <div class="form-group">
                        <a class="btn btn-primary btn-sm"
                           href="${path}/finance/replacementRecharge/edit?type=GUARANTEECORP"
                           role="button">担保账户充值</a>
                    </div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                </shiro:hasPermission>

                <shiro:hasPermission name="INVESTOR:edit">
                    <div class="form-group">
                        <a class="btn btn-primary btn-sm"
                           href="${path}/finance/replacementRecharge/edit?type=INVESTOR"
                           role="button">投资账户充值</a>
                    </div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                </shiro:hasPermission>
            </form:form>
            <hr style="margin-bottom: 0;">
            <div class="panel panel-default">
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>用户类型</th>
                        <th>用户账号</th>
                        <th>用户名称</th>
                        <th>账户类型</th>
                        <th>充值金额(元)</th>
                        <th>充值人</th>
                        <th>审核人</th>
                        <th>充值时间</th>
                        <th>审核时间</th>
                        <th>状态</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${list}" var="v" varStatus="vs">
                        <tr>
                            <td>
                                <c:choose>
                                    <c:when test="${v.userType == 'PERSONAL'}">
                                        个人
                                    </c:when>
                                    <c:otherwise>
                                        企业
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${v.account}</td>
                            <td>${v.userName}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${v.userRole == 'INVESTOR'}">
                                        投资账户
                                    </c:when>
                                    <c:when test="${v.userRole == 'BORROWERS'}">
                                        借款账户
                                    </c:when>
                                    <c:when test="${v.userRole == 'GUARANTEECORP'}">
                                        担保账户
                                    </c:when>
                                    <c:otherwise>
                                        -
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${v.rechargeMoney}</td>
                            <td>${v.rechargeUserName}</td>
                            <td>${v.auditUserName}</td>
                            <td>
                                <fmt:formatDate value="${v.rechargeTime}"
                                                pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
                            </td>
                            <td>
                                <fmt:formatDate value="${v.auditTime}"
                                                pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${v.status == '0'}">
                                        审核不通过
                                    </c:when>
                                    <c:when test="${v.status == '1'}">
                                        <c:choose>
                                            <c:when test="${v.orderState == 'CG'}">
                                                充值成功
                                            </c:when>
                                            <c:when test="${v.orderState == 'SB'}">
                                                充值失败
                                            </c:when>
                                        </c:choose>
                                    </c:when>
                                    <c:when test="${v.status == '2'}">
                                        待审核
                                    </c:when>
                                    <c:otherwise>
                                        -
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <shiro:hasPermission name="replacementRecharge:audit">
                                    <c:choose>
                                        <c:when test="${v.status == '2'}">
                                            <a id="aButton" href="#" onclick="audit(0, ${v.id}, ${v.userId}, '${v.userType}', '${v.userName}', '${v.userRole}', '${v.rechargeMoney}', '${v.rechargeUserName}');"
                                               role="button">审核不通过</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                            <a href="#" onclick="audit(1, ${v.id}, ${v.userId}, '${v.userType}', '${v.userName}', '${v.userRole}', '${v.rechargeMoney}', '${v.rechargeUserName}');"
                                               role="button">审核通过</a>
                                        </c:when>
                                        <c:otherwise>
                                            <font color="#AAAAAA">审核不通过</font>&nbsp;&nbsp;&nbsp;&nbsp;
                                            <font color="#AAAAAA">审核通过</font>
                                        </c:otherwise>
                                    </c:choose>
                                </shiro:hasPermission>
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
<script type="text/javascript">

    function confirmMsg(userType, userName, userRole, rechargeMoney) {
        var msg = null;
        var accountType = null;
        if(userType != null){
            if(userType == 'PERSONAL'){
                userType = "个人";
            }else if(userType == 'ORGANIZATION'){
                userType = "企业";
            }else{
                userType = "未知";
            }
        }
        if(userRole != null){
            if(userRole == 'INVESTOR'){
                accountType = "投资账户";
            }else if(userRole == 'BORROWERS'){
                accountType = "借款账户";
            }else if(userRole == 'GUARANTEECORP'){
                accountType = "担保账户";
            }
        }
        msg = "用户类型:&nbsp;&nbsp;" + userType + "<br/>" + "用户名称:&nbsp;&nbsp;" + userName + "<br/>"
                + "账户类型:&nbsp;&nbsp;" + accountType + "<br/>" + "充值金额:&nbsp;&nbsp;" + "￥" +  rechargeMoney + "<br/>";
        return msg;
    }

    function confirmTitle(flag) {
        var title = null;
        if(flag == 0){
            title = "审核不通过:";
        }else{
            title = "审核通过:";
        }
        return title;
    }

    function audit(flag, id, userId, userType, userName, userRole, rechargeMoney, rechargeUserName) {
        var principal = $('#principal').val();
        if(rechargeUserName == principal){
            alert("同一个账号不能同时充值和审核。");
            return false;
        }
        bootbox.confirm({
            title: confirmTitle(flag),
            size: 'small',
            message: confirmMsg(userType, userName, userRole, rechargeMoney),
            callback: function (result) {
                var index = layer.load(0, {
                    shade: [0.4,'#fff',false] //0.1透明度的白色背景
                });
                if (result) {
                    $.post(
                            path + "/finance/replacementRecharge/audit",
                            {"id" : id,
                                "flag" : flag,
                                "userId" : userId,
                                "userRole" : userRole,
                                "rechargeMoney" : rechargeMoney},
                            function (data) {
                                var code = data.code;
                                var message = data.message;
                                if(code == "1000"){
                                    alert("同一个账号不能同时充值和审核。");
                                }else if(code == "success" || code == "fail"){
                                    alert("审核通过,请留意充值状态");
                                }else if(code == "0000"){
                                    alert("审核不通过成功");
                                }else if(code == "3333"){
                                    alert("请勿重复操作!");
                                }else if(code == "500"){
                                    if(flag == 1){
                                        alert("审核通过操作失败");
                                    }else{
                                        alert("审核不通过操作失败");
                                    }
                                }else if(code == "2222"){
                                    alert("充值金额不能大于账户余额。");
                                }else {
                                    alert("内部错误");
                                }
                                // 刷新页面
                                window.location.reload();
                            }
                    );
                }
                layer.closeAll();
            }
        });

    }

    $('#searchBtn').bind('click', function() {
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
        layer.close();
    }
</script>
