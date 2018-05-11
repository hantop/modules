package com.fenlibao.model.pms.common.enums;

/**
 * 用户操作行为日志记录
 * .
 */
public enum LogUrlEnum {
    UNBINDBANKCARD("/cs/unbindBankCard", "解绑银行卡", 201),
    UNBINDBANKCARDAUDIT("/cs/unbindBankCard", "解绑银行卡-审核", 202),
    MAKELOAN("/biz/loanList/loan", "放款", 301),
    NOTLOAN("/biz/loanList/notLoan", "流标", 302),
    DOREPAY("/biz/repayment/doRepay", "还款", 303),
    PREDOREPAY("/biz/repayment/doRepay", "提前还款", 304),
    SUBROGATION("/biz/repayment/doRepay", "担保代偿", 305),
    PRESUBROGATION("/biz/repayment/doRepay", "提前担保代偿", 306),
    CASHREDPACKETGRANT("/reward/red-packet/cash-red-packet-grant", "现金红包发送", 401),
    ASYNCGRANT("/reward/red-packet/async-grant", "返现券发送", 402),
    RATECOUPONGRANT("/reward/rateCoupon/async-grant", "加息券发送", 403),
    ACTIVITYCREATE("/marketing/activity/edit", "活动新增", 501),
    ACTIVITYEDIT("/marketing/activity/edit", "活动编辑", 502),
    PHONEREPLACE("/cs/phone/replace", "更改手机号", 601),
    REPLACEMENTRECHARGE("/finance/replacementRecharge/edit", "代充值", 701),
    REPLACEMENTRECHARGEAUDIT("/finance/replacementRecharge/audit", "审核通过", 702),
    REPLACEMENTRECHARGEAUDITNOTPASS("/finance/replacementRecharge/audit", "审核不通过", 703),
    ACCOUNTMANAGEMENTRECHARGE("/finance/accountmanagement/recharge", "充值", 801),
    ACCOUNTMANAGEMENTWITHDRAW("/finance/accountmanagement/withdraw", "提现", 802),
    ACCOUNTMANAGEMENTTRANSFER("/finance/accountmanagement/transfer", "划拨" , 803),
    ACCOUNTMANAGEMENTUNBINDBANKCARD("/finance/accountmanagement/unbindBankcard", "解绑卡", 804),
    ACCOUNTMANAGEMENTBINDBANK("/finance/accountmanagement/bindBank", "绑定卡", 805),
    ;

    private String code;
    private String name;
    private int index;

    LogUrlEnum(String code, String name, int index) {
        this.code = code;
        this.name = name;
        this.index = index;
    }

    public static int length() {
        return LogUrlEnum.values().length;
    }

    public static LogUrlEnum parse(int index) {    //手写的从int到enum的转换函数
        for (LogUrlEnum item : LogUrlEnum.values()) {
            if (item.getIndex() == index) {
                return item;
            }
        }
        return null;
    }

    public static LogUrlEnum parse(String code) {
        for (LogUrlEnum item : LogUrlEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }
}
