var tips = [
    "用户名或密码不能为空",
    "请输入验证码"
];

var reload=0;

$(document).ready(function () {
    $("form").keydown(function (event) {
        if (event.keyCode == 13) {
            login();
        }
    })

    $(".btn.btn-default").click(function () {
        login();
    });

    function login() {
        var username = $.trim($("#username").val());
        var password = $.trim($("#password").val());
        var captcha = $.trim($("#captcha").val());
        if (test(username, password, captcha)) {
            doLogin(username, password, captcha);
        }
    }

    function test(username, password, captcha) {
        if (!username || !password) {
            $(".text-warning").text(tips[0]);
            $('#tips').modal('show');
            return false;
        }
        //if (!captcha) {
        //    $(".text-warning").text(tips[1]);
        //    $('#tips').modal('show');
        //    return false;
        //}
        return true;
    }

    function doLogin(username, password, captcha) {
        $("#btn-submit").prop("disabled", true);
        $("#loading").removeClass("hidden");
        jQuery.ajax({
            url: "login",
            method: "post",
            async: true,
            dataType: "json",
            data: {
                username: username,
                password: password,
                captcha: captcha
            },
            success: function (data) {
                if (data.code == 200) {
                    $("#loading").addClass("hidden");
                    window.location.href = data.url;
                    return false;
                } else {
                    $("#btn-submit").prop("disabled", false);
                    $("#loading").addClass("hidden");
                    $(".text-warning").text(data.message);
					if(data.url=="error"){
						reload=1;
					}
                    $('#tips').modal('show');
                    return false;
                }
            },
            error: function () {
                $("#btn-submit").prop("disabled", false);
                $("#loading").addClass("hidden");
                window.alert("Internal server error");
                return false;
            }
        });
    }

    $('input[data-text]').focus(function () {
        $(this).nextAll('input[data-value]').show().focus();
    });

    $('input[data-value]').blur(function () {
        if ($.trim($(this).val()) == "") {
            $(this).hide();
            $(this).prevAll('input[data-text]').show();
        }
    });
});

$('#kaptcha').click(function () {
    $(this).fadeOut().attr('src', 'captcha').fadeIn();
});

$(".btn.btn-warning").click(function () {
    $("#tips").modal('hide');
});

$('#tips').on('hide.bs.modal', function () {
 if(reload==1){
	 location.reload();
 }
})