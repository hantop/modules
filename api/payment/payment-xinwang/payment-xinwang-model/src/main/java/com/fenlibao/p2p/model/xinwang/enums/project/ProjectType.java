package com.fenlibao.p2p.model.xinwang.enums.project;

/**
 * 标的类型
 */
public enum ProjectType {
    STANDARDPOWDER("STANDARDPOWDER", "散标"),
    ENTRUST_PAY("ENTRUST_PAY", "委托支付类标的"),
    ;

    protected final String code;
    protected final String name;

    ProjectType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
