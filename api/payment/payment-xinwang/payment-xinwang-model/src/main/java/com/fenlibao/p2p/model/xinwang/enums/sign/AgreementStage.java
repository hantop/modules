package com.fenlibao.p2p.model.xinwang.enums.sign;

/**
 * 合同文件状态
 */
public enum AgreementStage {
    /**
     * 未生成
     */
    WSC(0),
    /**
     * 可生成
     */
    KSC(1),
    /**
     * 可签名
     */
    KQM(2),
    /**
     * 已签名
     */
    YQM(3);
    private int code;

    AgreementStage(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
    public static AgreementStage parse(int code) {
        AgreementStage result = null;
        for (AgreementStage agreementStage : AgreementStage.values()) {
            if (agreementStage.getCode() == code) {
                result = agreementStage;
            }
        }
        return result;
    }
}
