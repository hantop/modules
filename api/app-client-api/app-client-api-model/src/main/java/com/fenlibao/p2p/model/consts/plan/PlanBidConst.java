package com.fenlibao.p2p.model.consts.plan;

/**
 * @author zeronx on 2017/11/16 16:43.
 * @version 1.0
 */
public class PlanBidConst {

    /** 数据库字段设置是否开启自动发布计划的key */
    public static final String ENABLED_AUTO_RELEASE_PLAN = "CONSUMPTIONLOAN.AUTO_ADD_PLAN_SWITCH";

    /**  是否激活自动发布计划，ON：是、OFF：否 */
    public static final String ENABLED_AUTO_RELEASE_PLAN_ON = "ON";

    /** 新手/省心计划 10天20天30天分布式锁键 */
    public static final String PLAN_LOCK_KEY = "plan_lock_key_";

    /** 是否新手计划 */
    public static final String IS_NEWBIE_PLAN = "S";

    /** 在前端展示的不同天数的新手计划个数 */
    public static final Integer DISPLAY_NEWBIE_COUNTS = 1;

    /** 投资计划时防止用户频繁操错 */
    public static final String INVEST_PLAN_LOCK_USER_KEY = "invest_plan_lock_user_key_";

    /** 投资计划时防止并发问题锁 */
    public static final String INVEST_PLAN_LOCK_PLAN_KEY = "invest_plan_lock_plan_key_";
}
