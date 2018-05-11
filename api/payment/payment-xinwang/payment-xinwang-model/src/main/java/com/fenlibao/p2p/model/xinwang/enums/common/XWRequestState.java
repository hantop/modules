package com.fenlibao.p2p.model.xinwang.enums.common;

/**
 * Created by Administrator on 2017/5/12.
 */
public enum XWRequestState {
    DTJ("DTJ","待提交"),
    DQR("DQR","待确认"),
    CG("CG","成功"),
    SB("SB","失败"),
    ;

    protected final String code;
    protected final String name;

    XWRequestState(String code,String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static XWRequestState parse(String code) throws Exception{
        XWRequestState result=null;
        for(XWRequestState item:XWRequestState.values()){
            if(item.getCode().equals(code)){
                result=item;
            }
        }
        return result;
    }
}
