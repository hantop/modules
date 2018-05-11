package com.fenlibao.p2p.model.xinwang.enums.account;

/**
 * 平台
 * @date 2017/5/5 17:03
 */
public enum PTAssetAccountType {
    XW_INVESTOR_WLZH("XW_INVESTOR_WLZH","新网出借人往来账户"),
    XW_INVESTOR_SDZH("XW_INVESTOR_SDZH","新网出借人锁定账户"),
    XW_BORROWERS_WLZH("XW_BORROWERS_WLZH","新网借款人往来账户"),
    XW_BORROWERS_SDZH("XW_BORROWERS_SDZH","新网借款人锁定账户"),
    XW_GUARANTEECORP_WLZH("XW_GUARANTEECORP_WLZH","新网担保机构往来账户"),
    XW_GUARANTEECORP_SDZH("XW_GUARANTEECORP_SDZH","新网担保机构锁定账户"),
    XW_ENTRUST_BORROWERS_WLZH("XW_ENTRUST_BORROWERS_WLZH","新网委托借款人往来账户"),
    XW_ENTRUST_BORROWERS_SDZH("XW_ENTRUST_BORROWERS_SDZH","新网委托借款人锁定账户"),
    ;

    protected final String code;
    protected final String name;

    PTAssetAccountType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static PTAssetAccountType parse(String code) throws Exception{
        PTAssetAccountType result=null;
        for(PTAssetAccountType item: PTAssetAccountType.values()){
            if(item.getCode().equals(code)){
                result=item;
            }
        }
        return result;
    }
}
