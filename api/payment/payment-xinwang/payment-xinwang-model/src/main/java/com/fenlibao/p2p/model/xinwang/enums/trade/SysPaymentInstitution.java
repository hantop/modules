package com.fenlibao.p2p.model.xinwang.enums.trade;

/**
 * @date 2017/6/3 11:19
 */
public enum SysPaymentInstitution {


    LL(900, 101, "连连支付"),//由于之前都是用这个编码，所以继续保留
    BF(1100, 102, "宝付"),
    HX(1000, 103, "华兴存管"),
    XW(1200, 104, "新网存管"),
    ;

    private int code;
    private int channelCode;
    private String name;

    SysPaymentInstitution(int code, int channelCode, String name) {
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

