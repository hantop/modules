package com.fenlibao.p2p.model.dm.enums;

/**
 * 华兴交易类型（需跳转页面的）
 * Created by zcai on 2016/9/22.
 */
public enum HXTradeType {

    //只有一个编码的直接使用pcCode, 使用时直接getPcCode(), 没必要再搞个客户端

    /**
     * 绑卡
     */
    BK(1, "OGW00044", "OGW00091", "", 1001),
    /**
     * 债权转让
     */
    ZQZR(2, "OGW00061", "OGW00096", "OGW00062", 1002),
    /**
     * 投标
     */
    TB(4, "OGW00052", "OGW00094", "OGW00053", 1003),
    /**
     * 还款
     */
    HK(5, "OGW00067", "OGW00095", "OGW00068", 1004),
    /**
     * 提前还款，华兴没有提前还款，为了区分还款和提前还款，只有业务编码与还款不一样
     */
    TQHK(5, "OGW00067", "OGW00095", "OGW00068", 11004),
    /**
     * 开户
     */
    KH(6, "OGW00042", "OGW00090", "OGW00043", 1005),
    /**
     * 充值
     */
    CZ(7, "OGW00045", "OGW00092", "OGW00046", 1006),
    /**
     * 提现
     */
    TX(8, "OGW00047", "OGW00093", "OGW00048", 1007),
    /**
     * 自动投标授权(只有PC)
     */
    ZDTBSQ(9, "OGW00056", "", "OGW00057", 1008),
    /**
     * 自动还款授权(只有PC)
     */
    ZDHKSQ(10, "OGW00069", "", "OGW00070", 1009),
    /**
     * 发标通知
     */
    FBTZ(0,"OGW00051", "", "", 1010),
    /**
     * 预发布，华兴没有预发布，为了建立预发布订单，只有业务编码与发标通知不一样
     */
    YFB(0, "OGW00051", "", "", 11010),
    /**
     * 流标
     */
    LB(0,"OGW00063", "", "OGW00064", 1011),
    /**
     * 放款
     */
    FK(0,"OGW00065", "", "OGW00066",1013),

    /**
     * 单笔奖励
     */
    DBJL(0, "OGW00076", "", "", 1014),
    /**
     * 还款明细
     */
    HKMX(0, "OGW00074", "", "OGW00075", 1015),
    /**
     * 还款垫付
     */
    HKDF(0, "OGW00073", "", "", 1016)
    ;

    private int code;//交易类型编码
    private String pcCode;//pc端交易码
    private String mCode;//移动端交易码
     private String queryCode;//相应接口查询交易码
    private int busiCode;//业务编码

    HXTradeType(int code, String pcCode, String mCode, String queryCode, int busiCode) {
        this.code = code;
        this.pcCode = pcCode;
        this.mCode = mCode;
        this.queryCode = queryCode;
        this.busiCode = busiCode;
    }

    /**
     * 获取交易类型相应客户端交易码
     * @param type
     * @param clientType
     * @return
     */
    public static String getTradeCode(HXTradeType type, int clientType) {
        if (APPType.isPC(clientType)) {
            return type.getPcCode();
        } else {
            return type.getmCode();
        }
    }

    public static String getQueryCodeByBusiCode(int busiCode) {
        HXTradeType[] tradeTypes = HXTradeType.values();
        for (HXTradeType type : tradeTypes) {
            if (type.getBusiCode() == busiCode) {
                return type.getQueryCode();
            }
        }
        return "";
    }

    public int getCode() {
        return code;
    }

    public String getPcCode() {
        return pcCode;
    }

    public String getmCode() {
        return mCode;
    }

    public String getQueryCode() {
        return queryCode;
    }

    public int getBusiCode() {
        return busiCode;
    }
}
