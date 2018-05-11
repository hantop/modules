package com.fenlibao.p2p.model.xinwang.enums.account;

/**
 * 银行编码
 * @date 2017/5/5 17:03
 */
public enum XWBankcode {
    ABOC("ABOC","中国农业银"),
    BJCN("BJCN","北京银行"),
    BKCH("BKCH","中国银行"),
    CIBK("CIBK","中信银行"),
    CMBC("CMBC","招商银行"),
    COMM("COMM","交通银行"),
    EVER("EVER","中国光大银"),
    FJIB("FJIB","兴业银行"),
    GDBK("GDBK","广发银行"),
    HXBK("HXBK","华夏银行"),
    ICBK("ICBK","中国工商银行"),
    MSBC("MSBC","中国民生银行"),
    PCBC("PCBC","中国建设银行"),
    PSBC("PSBC","中国邮政储蓄银行"),
    SPDB("SPDB","浦发银行"),
    SZDB("SZDB","平安银行"),
    ;

    protected final String code;
    protected final String name;

    XWBankcode(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static XWBankcode parse(String code) throws Exception{
        XWBankcode result=null;
        for(XWBankcode item: XWBankcode.values()){
            if(item.getCode().equals(code)){
                result=item;
            }
        }
        return result;
    }
}
