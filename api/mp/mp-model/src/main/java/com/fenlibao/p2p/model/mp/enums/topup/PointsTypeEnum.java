package com.fenlibao.p2p.model.mp.enums.topup;

/**
 * 积分类型
 *
 * @date 2017/6/21 14:13
 */
public enum PointsTypeEnum {
    SIGN_IN("SIGN_IN", "签到"),
    MINISO_CONSUME_POINTS("MINISO_CONSUME_POINTS", "线下消费"),
    CASH_EXCHANGE("CASH_EXCHANGE", "兑换现金"),
    PHONE_CHARGE("PHONE_CHARGE", "手机充值"),
    CASH_EXCHANGE_FIVE("CASH_EXCHANGE_FIVE", "兑换现金"),
    PHONE_CHARGE_FIFTY("PHONE_CHARGE_FIFTY", "手机充值"),
    PHONE_CHARGE_ONEHUNDRED("PHONE_CHARGE_ONEHUNDRED", "手机充值"),
    POINTS_LOTTERY("POINTS_LOTTERY", "积分抽奖"),
    PHONE_CHARGE_TEN("PHONE_CHARGE_TEN", "手机充值"),
    PHONE_CHARGE_THIRTY("PHONE_CHARGE_THIRTY", "手机充值"),
    EXPIRE_POINTS("EXPIRE_POINTS", "过期积分");

    protected String typeCode;
    protected String typeName;

    PointsTypeEnum(String typeCode, String typeName) {
        this.typeCode = typeCode;
        this.typeName = typeName;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public String getTypeName() {
        return typeName;
    }
}
