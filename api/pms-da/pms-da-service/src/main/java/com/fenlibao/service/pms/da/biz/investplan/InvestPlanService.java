package com.fenlibao.service.pms.da.biz.investplan;

import java.util.Date;

/**
 * 投资计划
 * <p>
 * Created by chenzhixuan on 2017/3/21.
 */
public interface InvestPlanService {
    /**
     * 取消推荐置顶
     *
     * @param planId
     */
    void cancelRecommend(int planId);

    /**
     * 推荐置顶
     *
     * @param planId
     */
    void recommend(int planId);

    /**
     * 审核
     *
     * @param planId 计划ID
     * @param isPass 是否通过
     */
    void audit(int planId, boolean isPass);

    /**
     * 作废
     *
     * @param planId 计划ID
     */
    void invalid(int planId);

    /**
     * 发布
     *
     * @param planId
     */
    void release(int planId);

    /**
     * 定时发布
     *
     * @param planId      计划ID
     * @param relaseTime  发布时间
     * @param displayTime 显示时间
     */
    void timingRelease(int planId, Date relaseTime, Date displayTime);

    /**
     * 终止
     * @param planId     计划ID
     * @param operatorId 操作人ID
     */
    void termination(int planId, int operatorId) throws Exception;

    /**
     * 置顶
     *
     * @param planId 计划ID
     */
    void sticktop(int planId);

    /**
     * 取消置顶
     *
     * @param planId 计划ID
     */
    void cancelSticktop(int planId);

    /**
     * 取消定时发布
     *
     * @param id
     * @param status
     */
    void cancelTimingRelease(int id, int status);
}
