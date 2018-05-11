<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="format-detection" content="telephone=no">
    <title>连连支付</title>
    <link href="<%=request.getContextPath()%>/theme/css/pay/style.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/theme/js/jquery-1.11.3.min.js"></script>
    <style>
        .info {
            padding: 15px;
            background: #fff url(images/info_bg.png) left bottom repeat-x;
        }

        .table_ui {
            width: 100%;
            margin: 0 auto;
        }

        .table_ui td {
            line-height: 1.5em;
            padding-bottom: 10px;
            vertical-align: top;
        }

        .ft_gray {
            color: #999;
        }

        .slogan {
            overflow: hidden;
            width: 100%;
            height: 19px;
            position: relative;
            margin: 20px 0 5px 0;
        }

        .slogan h3 {
            font-size: 18px;
            line-height: 19px;
            padding-left: 1%;
            color: #4d4d4d;
            position: absolute;
            background: #f2f2f2;
            z-index: 100;
            padding: 0 0.215em;
            font-weight: normal;
            font-family: "微软雅黑";
        }

        .slogan span {
            height: 9px;
            border-bottom: 1px solid #cacaca;
            width: 100%;
            position: absolute
        }

        .warp {
            width: 95%;
            margin: 0 auto;
        }

        .footer {
            text-align: center;
            color: #999;
            padding: 2em 0 1em 0;
        }

        .footer img {
            height: 15px;
            vertical-align: middle;
        }

        .footer span {
            height: 15px;
            font-size: 0.8em;
            line-height: 0.8em;
        }
    </style>
</head>
<body>
<div class="header">
    <a href="javascript:history.go(-1);" class="back">返回</a>
    <h1 class="logo">认证支付web测试</h1>
    <a href="#" class="about">关于</a>
</div>
<section class="info">
    <table border="0" cellspacing="0" cellpadding="0" class="table_ui">
        <tr>
            <td><span class="ft_gray">交易类型：</span></td>
            <td style="text-align: right">充值</td>
        </tr>
        <tr>
            <td width="100"><span class="ft_gray">金额：</span></td>
            <td style="text-align: right">
                <input placeholder="必填" style="height: 24px;" id="amount" name="amount" value="10"/>
            </td>
        </tr>
        <tr>
            <td width="100"><span class="ft_gray">银行卡号：</span></td>
            <td style="text-align: right">
                <input placeholder="首次充值需要填写" style="height: 24px;" id="bankCardNo" name="bankCardNo" value="wu9tjqnVGG2mbCsdoRzigkkEBvu8FQsz8Uesv2A5SnY="/>
            </td>
        </tr>
    </table>
</section>
<section class="slogan">
    <h3>
        请仔细阅读<a href="javascript:void(0)">《网上订购须知》</a>
    </h3>
    <span class="line"></span>
</section>
<section>
    <div class="form_warp">
    <!-- web auth https://cashier.lianlianpay.com/payment/authpay.htm -->
    	<!-- wap auth https://yintong.com.cn/llpayh5/authpay.htm -->
        <form id="pay_form" action="https://cashier.lianlianpay.com/payment/authpay.htm" method="post">
        
        </form>
        <button name="next_btn" class="btn" id="next_btn">连连支付</button>
    </div>
</section>
<footer class="warp footer">
    <img src="<%=request.getContextPath()%>/theme/images/logo.png"/>
    <span>连连支付版权所有 2004-2017 浙B2-20080148</span>
</footer>
<script type="text/javascript">
    $('#next_btn').click(function () {
        var amount = $('#amount').val();
        var bankCardNo = $('#bankCardNo').val();
        if ('' == amount || null == amount) {
            alert('请输入金额！');
            return;
        }
        var data = {
            amount: amount, bankCardNo: bankCardNo,
            userId: 32, isBind: 0,
            version: '1.0.0', deviceId: 1, clientType: 5, screenType: 1, token: 123
        };
        $.ajax({
            url: '<%=request.getContextPath()%>/lianlianPay/recharge/web',
            data: data,
            type: 'post',
            dataType: 'json',
            headers: {'version': '1.0.0'},
            success: function (result) {
                console.info(result);
                if (result.code == '200') {
                    var reqData = result.data.req_data;
                    setFormProperties(reqData)
                } else {
                    alert(result.message);
                }
            }
        });
    });

    function setFormProperties(reqData) {
		for(x in reqData) {
			$('#pay_form').append('<input type="hidden" name=\''+x+'\' value=\''+reqData[x]+'\'/>');//这里要用单引号！！
		}
		$('#pay_form').submit();
	}
</script>
</body>
</html>