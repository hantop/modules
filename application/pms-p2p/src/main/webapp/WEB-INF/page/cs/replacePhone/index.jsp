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
        <li class="active"><b>更换手机号码</b></li>
      </ol>
      <form id="dataform" class="form-inline" method="post">
        <div class="form-group">
            <shiro:hasPermission name="replacePhone:searchUser">  
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
            <div class="panel-heading">手机解绑</div>
            <table class="table table-striped table-bordered table-condensed">
                <thead>
                <tr class="success" style="color: #8B3E2F;">
                    <th>用户手机号</th>
                    <th>用户身份证号</th>
                    <th>请填写新手机号码</th>
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
            action="${path}/cs/phone">
        <div class="form-group">
          <label>更换日期：</label>
          <input name="unbindStartTime"
                 type="text" readonly="readonly" maxlength="20"
                 class="form-control input-sm" style="width: 163px;"
                 value="${replacePhoneForm.unbindStartTime}"
                 onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
          --
          <input name="unbindEndTime"
                 type="text" readonly="readonly" maxlength="20"
                 class="form-control input-sm" style="width: 163px;"
                 value="${replacePhoneForm.unbindEndTime}"
                 onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
        </div>
        &nbsp;&nbsp;

        <div class="form-group">
          <label>用户手机号码：</label>
          <input name="phoneNum"
                 type="text"
                 class="form-control input-sm"
                 maxlength="11"
                 value="${replacePhoneForm.phoneNum}">
        </div>
        &nbsp;&nbsp;

        <div class="form-group">
          <label>操作人：</label>
          <input name="operator"
                 type="text"
                 class="form-control input-sm"
                 value="${replacePhoneForm.operator}">
        </div>
        &nbsp;&nbsp;

        <div class="form-group">
          <label class="col-sm-1"></label>
          <input type="hidden" id="page" name="page">
          <shiro:hasPermission name="replacePhone:searchRecord">
              <button id="sButton" type="button" class="btn btn-primary btn-sm">搜索</button>
          </shiro:hasPermission>
          <shiro:hasPermission name="replacePhone:export">
              <button type="button" class="btn btn-success btn-sm" id="exportBtn">导出</button>
          </shiro:hasPermission>
        </div>

      </form>

      <hr style="margin-bottom: 0;">

      <div class="panel panel-default">
        <table class="table table-striped table-bordered table-condensed">
          <thead>
          <tr class="success">
            <th>更换日期</th>
            <th>操作人</th>
            <th>原手机号码</th>
            <th>新手机号码</th>
          </tr>
          </thead>
          <tbody>
          <c:forEach items="${list}" var="v" varStatus="vs">
            <tr>
              <td>
                <fmt:formatDate value="${v.operatorTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
              </td>
              <td>${v.operator}</td>
              <td>${v.oldPhone}</td>
              <td>${v.newPhone}</td>
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
    $('#sButton').bind('click', function() {
        var index = layer.load(0, {
            shade: [0.4,'#fff',false] //0.1透明度的白色背景
        });
        $("#searchform").submit();
        layer.close();
    });

  // 导出
  $("#exportBtn").click(function () {
            /*var form = document.forms[1];
            form.action = path + "/cs/phone/export";
            form.submit();
            form.action = path + "/cs/phone"*/
            var cloneForm = $(this).parents('form.form-inline').clone(true);
            $(cloneForm).attr('action', '${path}/cs/phone/export').submit();
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
          //var myreg = /^(((13[0-9]{1})|159|153)+\d{8})$/;
          var myreg = /^(1)[0-9]{10}$/;
          if(!myreg.test(telPhone))
          {
              bootbox.alert({size: 'small',message:"请输入有效的手机号码！！！"});
              $("#phoneNum").focus();
              return false;
          }
      }
        $("#repalceInfo").html('');
  //    $("#repalceMain").hide();
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
                                            +'<td><input id="rePhone" name="rePhone" type="text" class="form-control" placeholder="手机号码" aria-describedby="basic-addon1" maxlength="11" /></td>'
                                            +'<td><shiro:hasPermission name="replacePhone:replace"><button id="replaceBtn" name="replaceBtn" type="button" class="btn btn-primary btn-danger" onclick="replacePhone('+data.phoneNum+',\''+data.idcard+'\');">更换手机号码</button></shiro:hasPermission></td></tr>'
                                            +'<input type="hidden" id="idPhone" name="idPhone" value="'+data.userId+'"/>';
                          $("#repalceInfo").html(htmlContent);
                      //    $("#repalceMain").show();

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

  function replacePhone(pa,ba){
      var rePhone = $("#rePhone").val();    //新的手机号码
      if(rePhone == ''){
          bootbox.alert({size: 'small',message:"请填写新手机号码！！！"});
          return false;
      }

       if(rePhone != ''){
           //var myreg = /^(((13[0-9]{1})|159|153)+\d{8})$/;
           var myreg = /^(1)[0-9]{10}$/;
           if(!myreg.test(rePhone))
           {
               bootbox.alert({size: 'small',message:"请输入有效的手机号码！！！"});
               $("#phoneNum").focus();
               return false;
           }
       }

      if(pa == rePhone){
          bootbox.alert({size: 'small',message:"更换手机号码不能和原号码相同！！！"});
          return false;
      }

      var tip = '';
      $.post(
              path + "/cs/phone/auth",
              {"rePhone" : rePhone},
              function (data) {
                  if(data.doCode == '1004'){
                      bootbox.alert({size: 'small',message:"要更换的手机已经注册实名认证过！！！"});
                  }else {
                      if(data.doCode == '1000'){
                          tip = "<br/><span style='font-weight: bold;color: rgb(175,18,88);'>注意:</span><span style='font-weight: bold;color: rgb(20,68,106);'>该手机注册未实名认证</span>";
                      }
                      bootbox.confirm({
                          title: '更换手机号码',
                          size: 'small',
                          message:
                          "<span style='font-weight: bold'>用户原手机号:</span><span style='font-weight: bold;color: red;font-size: 20px'>"+pa+"</span>" +
                          "<br/><span style='font-weight: bold'>身份证号:</span><span style='font-weight: bold'>"+ba+"</span>" +
                          "<br/><span style='font-weight: bold'>新的手机号码:</span><span style='font-weight: bold;color: red;font-size: 20px'>"+rePhone+"</span>" + tip,
                          callback: function (result) {
                              if(result) {
                                  $.post(
                                          path + "/cs/phone/replace",
                                          {
                                              "rePhone" : rePhone,
                                              "userId" : $("#idPhone").val(),
                                              "oldPhone" : pa
                                          },
                                          function (data) {
                                              var regCode = data.regCode;
                                              var verifyCode = data.verifyCode;
                                              var doCode = data.doCode;

                                              if(regCode == '1'){
                                                  bootbox.alert({size: 'small',message:"要更换的手机已经注册实名认证过！！！"});
                                                  return false;
                                              }
                                              if(verifyCode == '1'){
                                                  bootbox.alert({size: 'small',message:"原手机号码不正确！！！"});
                                                  return false;
                                              }
                                              if(doCode != '1001'){
                                                  bootbox.alert({size: 'small',message:"更换手机号码成功！！！",callback: function () {
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
              },
              "json"
      );

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
