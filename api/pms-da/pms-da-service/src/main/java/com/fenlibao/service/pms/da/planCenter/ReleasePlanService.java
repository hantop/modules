package com.fenlibao.service.pms.da.planCenter;

import com.fenlibao.model.pms.da.planCenter.PlanMarketingSetting;
import com.fenlibao.model.pms.da.planCenter.form.PlanMarketSettingForm;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by Administrator on 2017/3/21.
 */
public interface ReleasePlanService {
    /**
     * 保存/更新计划
     * @param planMarketingSetting
     * @return
     */
    int saveOrUpdatePlan(PlanMarketingSetting planMarketingSetting) throws Exception;

    /**
     * 保存/更新营销设置表
     * @param planMarketingSetting
     * @return
     */
    int saveOrUpdateMarketSetting(PlanMarketingSetting planMarketingSetting) throws Exception;

    /**
     * 保存/更新标签表
     * @param planMarketingSetting
     * @return
     */
    int saveOrUpdateProductLabel(PlanMarketingSetting planMarketingSetting) throws Exception;

    /**
     * 列表数据
     * @param planMarketSettingForm
     * @return
     */
    List<PlanMarketingSetting> findPlanMarketingSettingPager(PlanMarketSettingForm planMarketSettingForm, RowBounds bounds);

    /**
     * 计划详情
     * @param id
     * @return
     */
    PlanMarketingSetting findPlanMarketingSettingById(int id);
}
