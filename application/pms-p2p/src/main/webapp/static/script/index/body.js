function formatMoney(money) {
    // 保留两位小数
    return money.toFixed(2) + "元";
}

function getPlatformstatistics(historyType) {
    var personSuffix = "人";
    $.get(
        path + "/index/platformstatistics",
        {
            historyType : historyType
        },
        function (data) {
            $("#registerNum").html(data.registerNum + personSuffix);
            $("#tenderNum").html(data.tenderNum + personSuffix);
            $("#transferinNum").html(data.transferinNum + personSuffix);
            $("#rechargeMoney").html(formatMoney(data.rechargeMoney));
            $("#tenderMoney").html(formatMoney(data.tenderMoney));
            $("#transferinMoney").html(formatMoney(data.transferinMoney));
            $("#withdrawMoney").html(formatMoney(data.withdrawMoney));
            $("#earningsMoney").html(formatMoney(data.earningsMoney));
            $("#firstInvestNum").html(data.firstInvestNum + personSuffix);
            if (historyType == "yesterday") {
                $("#hideshow").hide();
            } else {
                $("#hideshow").show();
                $("#loanMoney").html(formatMoney(data.loanMoney));
                $("#turnoverFee").html(formatMoney(data.turnoverFee));
            }
        },
        "json"
    );
}
$().ready(function(){
    getPlatformstatistics("yesterday");
});
