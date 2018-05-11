<%@page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!--97 date -->
<script type="text/javascript" src="/static/component/datepicker/WdatePicker.js"></script>
<script type="text/javascript" src="/static/component/datepicker/calendar.js"></script>
<script type="text/javascript" src="/static/component/bootbox/bootbox.js"></script>
<script type="text/javascript" src="/static/component/layer/layer.js"></script>

<div class="container">
    <div class="row">
        <div class="col-md-12">
            <ol class="breadcrumb">
                <li class="active"><a href="${path}/cs/index">客服管理</a></li>
                <li class="active"><b>解绑银行卡</b></li>
                <li class="active"><b>${unbindBankcardForm.userAccount}</b></li>
            </ol>
            <form id="dataform" class="form-inline" method="post">
                <div class="form-group">
                    <label>用户类型：</label>
                </div>
                <div class="form-group">
                    <select class="form-control" id="userType" name="userType" disabled="disabled">
                        <option value="PERSONAL" <c:if test='${unbindBankcardForm.userType == "PERSONAL"}'>selected="selected"</c:if>>个人</option>
                        <option value="ORGANIZATION" <c:if test='${unbindBankcardForm.userType == "ORGANIZATION"}'>selected="selected"</c:if>>企业</option>
                    </select>
                </div>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <div class="form-group">
                    <label>用户账号：</label>
                </div>
                <div class="form-group">
                    <input type="text" readonly="readonly"
                           class="form-control input-sm" maxlength="11"
                           name="userAccount" value="${unbindBankcardForm.userAccount}"
                           style="width: 160px;" placeholder="企业/个人">
                </div>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <div class="form-group">
                    <label>用户名称：</label>
                </div>
                <div class="form-group">
                    <input type="text" readonly="readonly"
                           class="form-control input-sm" maxlength="11"
                           name="userAccount" value="${unbindBankcardForm.userName}"
                           style="width: 160px;" placeholder="企业/个人">
                </div>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <div class="form-group">
                    <label>唯一识别码：</label>
                </div>
                <div class="form-group">
                    <input type="text" readonly="readonly"
                           class="form-control input-sm" maxlength="18"
                           name="uid" value="${unbindBankcardForm.uid}"
                           style="width: 320px;" placeholder="身份证/营业执照/统一社会信用代码"/>
                </div>
            </form>
            <hr style="margin-bottom: 0;">
            <div class="panel panel-default">
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>系统类型</th>
                        <th>账户类型</th>
                        <th>银行卡号</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${list}" var="v" varStatus="vs">
                        <tr>
                            <td>
                                <c:choose>
                                    <c:when test="${v.userRole != '0'}">
                                        存管
                                    </c:when>
                                    <c:otherwise>
                                        普通
                                    </c:otherwise>
                                </c:choose>
                            </td>
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
                            <td>${v.bankNum}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${v.userRole != '0'}">
                                        <c:choose>
                                            <c:when test="${!v.audit}">
                                                <font color="#AAAAAA">审核</font>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="#" onclick="audit(${v.userId}, '${unbindBankcardForm.userAccount}', '${unbindBankcardForm.userName}', '${v.userRole}', '${v.bankNum}');"
                                                   role="button">审核</a>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="#" onclick="unbind(${v.userId}, '${unbindBankcardForm.userName}', '${unbindBankcardForm.userName}', '${v.userRole}', '${v.baofooBindId}', '${v.bankNum}');"
                                           role="button">解绑</a>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    function confirmMsg(userName, userRole) {
        var msg = null;
        var sysType = null;
        var accountType = null;
        var tip = null;
        if(userRole != null && userRole != "0"){
            sysType = "存管";
            tip = "该步骤仅审核用户资料，用户在审核通过后72小时内可解绑银行卡；如有需要，请通知用户。";
            if(userRole == 'INVESTOR'){
                accountType = "投资账户";
            }else if(userRole == 'BORROWERS'){
                accountType = "借款账户";
            }else if(userRole == 'GUARANTEECORP'){
                accountType = "担保账户";
            }
        }else{
            sysType = "普通";
            accountType = "--";
            tip = "操作后直接帮用户解绑银行卡。";
        }
        msg = "用户名称:&nbsp;&nbsp;" + userName + "<br/>" + "系统类型:&nbsp;&nbsp;" + sysType + "<br/>"
                + "账户类型:&nbsp;&nbsp;" + accountType + "<br/><br/>" + tip;
        return msg;
    }

    function audit(userId, userAccount, userName, userRole, bankNum) {
        bootbox.confirm({
            title: '审核',
            size: 'small',
            message: confirmMsg(userName, userRole),
            callback: function (result) {
                if (result) {
                    $.post(
                            path + "/cs/unbindBankCard",
                            {"userId" : userId,
                             "userAccount" : userAccount,
                             "userRole" : userRole,
                             "bankNum" : bankNum},
                            function (data) {
                                if(data == "0000") {
                                    alert("审核成功");
                                    // 刷新页面
                                    location.reload();
                                } else if (data == "1000"){
                                    alert("审核失败，内部错误。");
                                }
                            }
                    );
                }
            }
        });
    }

    function unbind(userId, userAccount, userName, userRole, baofooBindId, bankNum) {
        bootbox.confirm({
            title: '解绑',
            size: 'small',
            message: confirmMsg(userName, userRole),
            callback: function (result) {
                if (result) {
                    $.post(
                            path + "/cs/unbindBankCard",
                            {"userId" : userId,
                             "userAccount" : userAccount,
                             "baofooBindId" : baofooBindId,
                             "bankNum" : bankNum},
                            function (data) {
                                if(data == "0000") {
                                    alert("解绑成功");
                                    // 刷新页面
                                    location.reload();
                                } else if (data == "250131"){
                                    alert("解绑失败，内部错误。");
                                }else if (data == "250130" || data == "2000"){
                                    alert("用户没有绑卡。");
                                }else {
                                    alert("内部错误。");
                                }
                            }
                    );
                }
            }
        });
    }
</script>
