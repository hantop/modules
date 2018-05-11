package com.fenlibao.p2p.model.enums.bid;

/**
 * WQM:未签名，QMZ:签名中，YQM:已签名
 * by：kris
 */
public enum AgreementSignStatusEnum {
    WQM("未签名"),
    QMZ("签名中"),
    YQM("已签名"),
    QMSB("签名失败"),
    ;

    private String name;

    AgreementSignStatusEnum( String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public static int length() {
        return BidTypeEnum.values().length;
    }
}
