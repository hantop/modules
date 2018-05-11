package com.fenlibao.p2p.model.xinwang.enums.account;

/**
 * 用户角色
 * @date 2017/5/5 17:03
 */
public enum  UserRole {
    GUARANTEECORP("GUARANTEECORP","担保机构"),
    INVESTOR("INVESTOR","出借人"),
    BORROWERS("BORROWERS","借款人"),
    INTERMEDIATOR("INTERMEDIATOR", " 居间人"),
    COLLABORATOR("COLLABORATOR","合作机构"),
    SUPPLIER("SUPPLIER","供应商"),
    PLATFORM_COMPENSATORY("PLATFORM_COMPENSATORY","平台代偿账户"),
    PLATFORM_MARKETING("PLATFORM_MARKETING","平台营销款账户"),
    PLATFORM_PROFIT("PLATFORM_PROFIT","平台分润账户"),
    PLATFORM_INCOME("PLATFORM_INCOME","平台收入账户"),
    PLATFORM_INTEREST("PLATFORM_INTEREST","平台派息账户"),
    PLATFORM_ALTERNATIVE_RECHARGE("PLATFORM_ALTERNATIVE_RECHARGE","平台代充值账户"),
    PLATFORM_FUNDS_TRANSFER("PLATFORM_FUNDS_TRANSFER","平台总账户"),
    PLATFORM_URGENT("PLATFORM_URGENT","平台垫资账户"),
    ENTRUST_BORROWERS("ENTRUST_BORROWERS", "委托借款人"),
    ;

    protected final String code;
    protected final String name;

    UserRole(String code,String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static UserRole parse(String code) throws Exception{
        UserRole result=null;
        for(UserRole item:UserRole.values()){
            if(item.getCode().equals(code)){
                result=item;
            }
        }
        return result;
    }
}
