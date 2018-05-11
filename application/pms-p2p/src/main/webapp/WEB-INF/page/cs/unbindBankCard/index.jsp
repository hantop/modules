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
            </ol>
            <div id="msg"><span style="margin-left:50px;color: red;font-family: 'Arial Black'">${msg}</span></div>
            <form id="dataform" class="form-inline" method="post" action="${path}/cs/userUnbindBankCards">
                <div class="form-group">
                    <label>用户类型：</label>
                </div>
                <div class="form-group">
                    <select class="form-control" id="userType" name="userType">
                        <option value="PERSONAL" <c:if test='${unbindBankcardForm.userType == "PERSONAL"}'>selected="selected"</c:if>>个人</option>
                        <option value="ORGANIZATION" <c:if test='${unbindBankcardForm.userType == "ORGANIZATION"}'>selected="selected"</c:if>>企业</option>
                    </select>
                </div>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <div class="form-group">
                    <label>用户账号：</label>
                </div>
                <div class="form-group">
                    <input type="text"
                           class="form-control input-sm" maxlength="20"
                           id="userAccount" name="userAccount" value="${unbindBankcardForm.userAccount}"
                           style="width: 160px;" placeholder="企业/个人">
                </div>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <div class="form-group">
                    <label>唯一识别码：</label>
                </div>
                <div class="form-group">
                    <input type="text"
                           class="form-control input-sm" maxlength="18"
                           id="uid" name="uid" value="${unbindBankcardForm.uid}"
                           style="width: 320px;" placeholder="身份证/营业执照/统一社会信用代码"/>
                </div>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <div class="form-group">
                    <label class="col-sm-1"></label>
                    <shiro:hasPermission name="unbindBankCard:unbind">
                        <button type="button" class="btn btn-primary btn-sm" id="unbindBtn">解绑</button>
                    </shiro:hasPermission>
                    <button type="button" class="btn btn-success btn-sm"  id="cleanContentBtn">重填</button>
                </div>
            </form>

            <hr style="margin-bottom: 0;">
            <hr style="margin-bottom: 0;">

            <form id="searchform" class="form-inline" method="post"
                  action="${path}/cs/unbindBankCards">
                <div class="form-group">
                    <label>解绑日期：</label>
                    <input name="unbindStartTime"
                           type="text" readonly="readonly" maxlength="20"
                           class="form-control input-sm" style="width: 163px;"
                           value="${unbindBankcardSearchForm.unbindStartTime}"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd', isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'});"
                           onFocus="WdatePicker({isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'})"/>
                    --
                    <input name="unbindEndTime"
                           type="text" readonly="readonly" maxlength="20"
                           class="form-control input-sm" style="width: 163px;"
                           value="${unbindBankcardSearchForm.unbindEndTime}"
                           onclick="WdatePicker({dateFmt:'yyyy-MM-dd', isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'});"
                           onFocus="WdatePicker({isShowClear:false, readOnly:true, maxDate:'%y-%M-%d :%m:%s'})"/>
                </div>
                &nbsp;&nbsp;

                <div class="form-group">
                    <label>用户账号：</label>
                    <input name="userAccount"
                           type="text"
                           class="form-control input-sm"
                           value="${unbindBankcardSearchForm.userAccount}">
                </div>
                &nbsp;&nbsp;

                <div class="form-group">
                    <label>操作人：</label>
                    <input name="operator"
                           type="text"
                           class="form-control input-sm"
                           value="${unbindBankcardSearchForm.operator}">
                </div>
                &nbsp;&nbsp;

                <div class="form-group">
                    <label class="col-sm-1"></label>
                    <input type="hidden" id="page" name="page">
                    <shiro:hasPermission name="unbindBankCard:search">
                        <button id="searchBtn" type="button" class="btn btn-primary btn-sm">搜索</button>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="unbindBankCard:export">
                        <button type="button" class="btn btn-success btn-sm" id="exportBtn">导出</button>
                    </shiro:hasPermission>
                </div>

            </form>

            <hr style="margin-bottom: 0;">

            <div class="panel panel-default">
                <div class="panel-heading">解绑记录</div>
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr class="success">
                        <th>解绑日期</th>
                        <th>操作人</th>
                        <th>用户账号</th>
                        <th>解绑账户</th>
                        <th>解绑银行卡</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${list}" var="v" varStatus="vs">
                        <tr>
                            <td>
                                <fmt:formatDate value="${v.unbindTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
                            </td>
                            <td>${v.operator}</td>
                            <td>${v.userAccount}</td>
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
                            <td>${v.bankcardNo}</td>
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
    $('#unbindBtn').bind('click', function() {
        var userAccount = $("#userAccount").val();
        var uid = $("#uid").val();
        if ((userAccount != null && userAccount != '') && (uid != null && uid != '')) {
            var index = layer.load(0, {
                shade: [0.4,'#fff',false] //0.1透明度的白色背景
            });
            $("#dataform").submit();
            layer.close();
        } else  {
            bootbox.alert({message: "<h3>用户账号以及唯一识别码不能为空!</h3>"});
        }
    });

    $('#searchBtn').bind('click', function() {
        var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $("#searchform").submit();
        layer.close();
    });

    // 导出
    $("#exportBtn").click(function () {
//            var form = document.forms[1];
//            form.action = path + "/cs/export";
//            form.submit();
//            form.action = path + "/cs/unbindBankCards"
            var cloneForm = $("#searchform").clone(true);
            $(cloneForm).attr('action', '${path}/cs/export').submit();
        }
    );

    // 重填
    $("#cleanContentBtn").click(function () {
            $("#dataform input[name='userAccount']").val("");
            $("#dataform input[name='uid']").val("");
        }
    );

    function pagination(page) {
        $('#page').val(page);
        var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $('#searchform').submit();
        layer.close();
    }
</script>
