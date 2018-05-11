package com.fenlibao.p2p.model.trade.enums;

/**
 * Created by zcai on 2016/11/2.
 */
public enum PaymentInstitution {


    LL(900, 101, "连连支付"),//由于之前都是用这个编码，所以继续保留
    BF(1100, 102, "宝付"),
    HX(1000, 103, "华兴存管"),
    ;

    private int code;
    private int channelCode;
    private String name;

    PaymentInstitution(int code, int channelCode, String name) {
        this.code = code;
        this.channelCode = channelCode;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getChannelCode() {
        return channelCode;
    }
}
