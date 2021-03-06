package com.fenlibao.p2p.model.global;

public enum Status {

	/** 
     * 通过
     */
    TG("通过"),

    /** 
     * 不通过
     */
    BTG("不通过"),
    
    //-------标的状态-----
    /**
     * 预发布
     */
    YFB("预发布"),
    /** 
     * 投标中
     */
    TBZ("出借中"),

    /** 
     * 待放款
     */
    DFK("待放款"),

    /** 
     * 还款中
     */
    HKZ("还款中"),

    /** 
     * 已结清
     */
    YJQ("已结清"),

    /** 
     * 已流标
     */
    YLB("已流标"),

    /** 
     * 已作废
     */
    YZF("已作废"),

    /** 
     * 已垫付
     */
    YDF("已垫付"),
     /** 
     * 垫付中
     */
    DFZ("垫付中"),
    /** 
     * 转让中
     */
    ZRZ("转让中"),
    /** 
     * 待审核
     */
    DSH("待审核"),
	//-------标的状态-----
	
	//-------还款状态--------
	/** 
     * 未还
     */
    WH("未还"),

    /** 
     * 已还
     */
    YH("已还"),
    
    /** 
     * 垫付
     */
    DF("垫付"),

    /**
     * 提前还款
     */
    TQH("提前还款"),
    
    S("是"),
    
    F("否"),
	//-------还款状态-------
    
    //-------订单状态-------
    /** 
     * 待提交
     */
    DTJ("待提交"),

    /** 
     * 已提交
     */
    YTJ("已提交"),

    /** 
     * 待确认
     */
    DQR("待确认"),

    /** 
     * 成功
     */
    CG("成功"),

    /** 
     * 失败
     */
    SB("失败"),
    //---------订单状态------

    // --------实名认证--------
    /**
     * 已设置
     */
    YSZ("已设置"),
    /**
     * 未设置
     */
    WSZ("未设置"),
    // --------实名认证--------

    // --------兴趣类型--------
    /**
     * 理财
     */
    LC("出借"),

    /**
     * 借款
     */
    JK("借款"),
    // --------兴趣类型--------

    // --------银行卡认证状态---
    /**
     * 已认证（只可充值）
     */
    YRZ("已认证"),
    /**
     * 未认证
     */
    WRZ("未认证"),
    /**
     * 可提现和充值
     */
    KTX("可提现和充值"),
    // --------银行卡认证状态---

    //--------个人消息状态----------
    WD("未读"),
    
    YD("已读"),
    
    SC("删除"),
	//--------个人消息状态----------
    
    //--------微信绑定状态 start----------
    UNBIND("未绑定"),
    
    BINDED("已绑定");
	//--------微信绑定状态 end----------
	
    protected final String chineseName;

    private Status(String chineseName){
        this.chineseName = chineseName;
    }
    /**
     * 获取中文名称.
     * 
     * @return {@link String}
     */
    public String getChineseName() {
        return chineseName;
    }
}
