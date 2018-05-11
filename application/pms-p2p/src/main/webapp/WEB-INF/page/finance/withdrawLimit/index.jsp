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
                <li class="active"><b>用户提现设置</b></li>
            </ol>
            <div id="msg"><span style="margin-left:50px;color: red;font-family: 'Arial Black'">${msg}</span></div>
      <form id="dataform" class="form-inline"  method="post">
        <div class="form-group">
            <shiro:hasPermission name="withdrawLimit:searchUser">
            <label>用户手机号：</label>
            <input type="text"
                 class="form-control input-sm" maxlength="11" id="phoneNum"
                 name="phoneNum" value="${replacePhoneForm.phoneNum}"
                 style="width: 160px;" />
                <button type="button" class="btn btn-primary btn-sm" id="searchBtn">查询</button>
            </shiro:hasPermission>
        </div>
      </form>

        <hr style="margin-bottom: 0;">
        <hr style="margin-bottom: 0;">

        <div class="panel panel-default" id="repalceMain">
            <%--<hr style="margin-bottom: 0;">--%>
            <div class="panel-heading">用户提现金额限制</div>
            <table class="table table-striped table-bordered table-condensed">
                <thead>
                <tr class="success" style="color: #8B3E2F;">
                    <th>用户手机号</th>
                    <th>身份证号</th>
                    <th>限制提现金额</th>
                    <th>备注</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody id="repalceInfo">
                </tbody>
            </table>
        </div>

      <hr style="margin-bottom: 0;">
      <hr style="margin-bottom: 0;">

      <form id="searchform" class="form-inline" method="post"
            action="${path}/finance/withdrawLimit">
        <div class="form-group">
          <label>操作日期：</label>
          <input name="startTime"
                 type="text" readonly="readonly" maxlength="20"
                 class="form-control input-sm" style="width: 163px;"
                 value="${withdrawLimitForm.startTime}"
                 onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
          --
          <input name="endTime"
                 type="text" readonly="readonly" maxlength="20"
                 class="form-control input-sm" style="width: 163px;"
                 value="${withdrawLimitForm.endTime}"
                 onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
        </div>
        &nbsp;&nbsp;

        <div class="form-group">
          <label>用户手机号码：</label>
          <input name="phoneNum"
                 type="text"
                 class="form-control input-sm"
                 maxlength="11"
                 value="${withdrawLimitForm.phoneNum}">
        </div>
        &nbsp;&nbsp;

        <div class="form-group">
          <label>操作人：</label>
          <input name="operator"
                 type="text"
                 class="form-control input-sm"
                 value="${withdrawLimitForm.operator}">
        </div>
        &nbsp;&nbsp;
        <div class="form-group">
          <label class="col-sm-1"></label>
          <input type="hidden" id="page" name="page">
          <shiro:hasPermission name="withdrawLimit:search">
              <button id="sButton" type="button" class="btn btn-primary btn-sm">搜索</button>
         </shiro:hasPermission>
          <shiro:hasPermission name="withdrawLimit:export">
              <button type="button" class="btn btn-success btn-sm" id="exportBtn">导出</button>
          </shiro:hasPermission>
        </div>

      </form>

      <hr style="margin-bottom: 0;">

      <div class="panel panel-default">
        <table class="table table-striped table-bordered table-condensed">
          <thead>
          <tr class="success">
            <th>用户手机号</th>
            <th>提现金额限制</th>
            <th>操作日期</th>
            <th>操作人</th>
          </tr>
          </thead>
          <tbody>
          <c:forEach items="${list}" var="v" varStatus="vs">
            <tr>
              <td>${v.phoneNum}</td>
              <td>${v.limitMoney}</td>
              <td>
                <fmt:formatDate value="${v.creatTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
              </td>
              <td>${v.operator}</td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </div>
      <tiles:insertDefinition name="paginator"/>
    </div>
  </div>
</div>
<c:choose>
    <c:when test="${param.success}">
        <!-- Modal -->
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
            <div class="modal-dialog modal-sm" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel">
                            提示
                        </h4>
                    </div>
                    <div class="modal-body">
                            ${param.success ? '保存成功' : '保存失败'}
                    </div>
                    <div class="modal-footer">
                        <a href="${path}/finance/withdrawLimit" class="btn btn-default">确定</a>
                    </div>
                </div>
            </div>
        </div>
        <script type="application/javascript">
            (function ($) {
                $("#myModal").modal({
                    keyboard: true,
                    show: true,
                    backdrop: 'static'
                });
            })(jQuery);
        </script>
    </c:when>
    <c:when test="${temp != null}">
        <!-- Modal -->
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
            <div class="modal-dialog modal-sm" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title">
                            提示
                        </h4>
                    </div>
                    <div class="modal-body">
                        <c:choose>
                            <c:when test="${temp <= -1}">
                                清正确输入信息
                            </c:when>
                            <c:otherwise>
                                未知错误，请联系管理员
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">确定</button>
                    </div>
                </div>
            </div>
        </div>
        <script type="application/javascript">
            (function ($) {
                $("#myModal").modal({
                    keyboard: true,
                    show: true,
                    backdrop: 'static'
                });
            })(jQuery);
        </script>
    </c:when>
</c:choose>
<script type="text/javascript">  
    $('#sButton').bind('click', function() {
        var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $("#searchform").submit();
        layer.close();
    });
    
  // 导出
  $("#exportBtn").click(function () {
            var cloneForm = $(this).parents('form.form-inline').clone(true);
            $(cloneForm).attr('action', '${path}/finance/withdrawLimit/export').submit();
            return false;
          }
  );
  
 $("#searchBtn").click(function () {

     var telPhone = $("#phoneNum").val();
     if(telPhone == ''){
         bootbox.alert({size: 'small',message:"手机号码不能空！！！"});
         return false;
     }

     if(telPhone != ''){
         var myreg = /^(13[0-9]|14[0-9]|15[0-9]|165|17[0-9]|18[0-9])[0-9]{8}$/;
         if(!myreg.test(telPhone))
         {
             bootbox.alert({size: 'small',message:"请输入有效的手机号码！！！"});
             $("#phoneNum").focus();
             return false;
         }
     }
       $("#repalceInfo").html('');
   $.post(
           path + "/cs/phone/search",
           $("#dataform").serialize(),
           function (data) {
             var sysCode = data.sysCode;
             if(sysCode == "0000"){
                     var regCode = data.regCode;
                     if(regCode == '0'){
                         bootbox.alert({size: 'small',message:"该手机号码还未注册过！！！"});
                         return false;
                     }

                     var authCode = data.authCode;
                     if(authCode == "1"){
                         var phoneNum = data.phoneNum;
                         var idCard = data.idcard;
                         var htmlContent = "<tr><td>"+phoneNum+"</td><td>"+idCard+"</td>"
                                           +'<td><input id="limitMoney" name="limitMoney" type="number" class="form-control" placeholder="限制金额" aria-describedby="basic-addon1" maxlength="11" /></td>'
                                           +'<td><input id="remark" name="remark" type="text" class="form-control"  aria-describedby="basic-addon1" maxlength="11" /></td>'
                                           +'<td><shiro:hasPermission name="withdrawLimit:save"><button id="saveBtn" name="saveBtn" type="button" class="btn btn-primary btn-danger" onclick="save('+data.phoneNum+',\''+data.idcard+'\');">确定</button></shiro:hasPermission></td></tr>'
                                           +'<input type="hidden" id="idPhone" name="idPhone" value="'+data.userId+'"/>';
                         $("#repalceInfo").html(htmlContent);
                     }else{
                         bootbox.alert({size: 'small',message:"该手机号码还未进行实名认证！！！"});
                         return false;
                     }
             }else {
                 bootbox.alert({size: 'small',message:"服务器繁忙，请稍后再试！！！"});
             }
           },
           "json"
   );
 });
 
 function save(pa,ba){
	 var telPhone = $("#phoneNum").val();
     var limitMoney = $("#limitMoney").val();    //限制金额
     var remark = $("#remark").val();    //备注
     if(limitMoney == ''){
         bootbox.alert({size: 'small',message:"请填写提现限制金额！！！"});
         return false;
     }

     var tip = '';
     bootbox.confirm({
                         title: '用户提现限制',
                         size: 'small',
                         message:
                         "<span style='font-weight: bold'>用户手机号:</span><span style='font-weight: bold;color: red;font-size: 20px'>"+pa+"</span>" +
                         "<br/><span style='font-weight: bold'>身份证号:</span><span style='font-weight: bold'>"+ba+"</span>" +
                         "<br/><span style='font-weight: bold'>提现金额:</span><span style='font-weight: bold;color: red;font-size: 20px'>"+limitMoney+"</span>" + tip,
                         callback: function (result) {
                             if(result) {
                                 $.post(
                                         path + "/finance/withdrawLimit/save",
                                         {
                                             "limitMoney" : limitMoney,
                                             "remark" : remark,
                                             "userId" : $("#idPhone").val(),
                                             "telPhone" : telPhone
                                         },
                                         function (data) {
                                             var regCode = data.regCode;
                                             var verifyCode = data.verifyCode;
                                             var doCode = data.doCode;

                                             if(doCode != '1001'){
                                                 bootbox.alert({size: 'small',message:"提现金额设置成功！！！",callback: function () {
                                                     window.location.reload();
                                                 }});
                                             }else {
                                                 bootbox.alert({size: 'small',message:"服务器繁忙，请稍后再尝试！！！"});
                                                 return false;
                                             }
                                         }
                                 );
                             }
                         }
                     });
 }
  
 function pagination(page) {
    $('#page').val(page);
          var index = layer.load(0, {
              shade: [0.4,'#fff',false] //0.1透明度的白色背景
          });
    $('#searchform').submit();
      layer.close();
  }
</script>
