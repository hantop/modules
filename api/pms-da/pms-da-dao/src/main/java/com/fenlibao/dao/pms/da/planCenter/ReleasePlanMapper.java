package com.fenlibao.dao.pms.da.planCenter;

import com.fenlibao.model.pms.da.planCenter.PlanMarketingSetting;
import com.fenlibao.model.pms.da.planCenter.form.PlanMarketSettingForm;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by Administrator on 2017/3/21.
 */
public interface ReleasePlanMapper {
    /**
     * 保存计划表
     * @param planMarketingSetting
     * @return
     */
    int insertIntoInvestPlan(PlanMarketingSetting planMarketingSetting);

    /**
     * 更新计划表
     * @param planMarketingSetting
     * @return
     */
    int updateInvestPlanByPrimaryKey(PlanMarketingSetting planMarketingSetting);

    /**
     * 查询计划营销设置
     * @param planMarketingSetting
     * @return
     */
    int getMarketSettingByProductTypeAndId(PlanMarketingSetting planMarketingSetting);

    /**
     * 查询计划自定义标签
     * @param planMarketingSetting
     * @return
     */
    int getProductLabelByProductTypeAndId(PlanMarketingSetting planMarketingSetting);

    /**
     * 保存营销设置表
     * @param planMarketingSetting
     * @return
     */
    int insertIntoMarkeSetting(PlanMarketingSetting planMarketingSetting);

    /**
     * 更新营销设置表
     * @param planMarketingSetting
     * @return
     */
    int updateMarkeSettingByPlanId(PlanMarketingSetting planMarketingSetting);

    /**
     * 保存产品标签表
     * @param planMarketingSetting
     * @return
     */
    int insertIntoProductLabel(PlanMarketingSetting planMarketingSetting);

    /**
     * 更新产品标签表
     * @param planMarketingSetting
     * @return
     */
    int updateProductLabelByPlanId(PlanMarketingSetting planMarketingSetting);

    /**
     * 计划编号
     * @return
     */
    String getMaxPlanNumber();

    /**
     * 列表
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

    /**
     * 删除营销设置表
     * @param id
     * @return
     */
    int deleteMarketSetting(int id);

    /**
     * 删除标签表
     * @param id
     * @return
     */
    int deleteProductLabel(int id);

}
