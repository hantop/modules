package com.fenlibao.p2p.model.xinwang.enums.plan;

/**
 * t_user_plan.status
 */
public enum SysUserPlan_Status {
    IN_POSSESSION(1,"持有中"),
    APPLY_FOR_EXIT(2,"申请退出"),
    EXITED(3,"已退出"),
    OVERDUE(4,"已逾期"),
    ;

    protected final Integer code;
    protected final String name;

    SysUserPlan_Status(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static SysUserPlan_Status parse(Integer code) throws Exception{
        SysUserPlan_Status result=null;
        for(SysUserPlan_Status item: SysUserPlan_Status.values()){
            if(item.getCode().equals(code)){
                result=item;
            }
        }
        return result;
    }
}
