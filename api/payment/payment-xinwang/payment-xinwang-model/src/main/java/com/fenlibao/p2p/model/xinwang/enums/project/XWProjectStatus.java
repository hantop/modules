package com.fenlibao.p2p.model.xinwang.enums.project;

/**
 * 新网标状态
 * @date 2017/5/5 17:03
 */
public enum XWProjectStatus {
    COLLECTING("COLLECTING","募集中"),
    REPAYING("REPAYING","还款中"),
    FINISH("FINISH","已截标"),
    MISCARRY("MISCARRY","流标"),
    ;

    protected final String code;
    protected final String name;

    XWProjectStatus(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static XWProjectStatus parse(String code) throws Exception{
        XWProjectStatus result=null;
        for(XWProjectStatus item: XWProjectStatus.values()){
            if(item.getCode().equals(code)){
                result=item;
            }
        }
        return result;
    }
}
