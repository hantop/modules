package com.fenlibao.service.pms.da.planCenter;

import com.fenlibao.model.pms.da.planCenter.PlanType;
import com.fenlibao.model.pms.da.planCenter.form.PlanTypeForm;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by Administrator on 2017/3/21.
 */
public interface PlanTypeService {
    /**
     * 保存/更新计划
     * @param planType
     * @return
     */
    int saveOrUpdatePlan(PlanType planType) throws Exception;

    /**
     * 启用/停用
     * @param id
     * @param status
     * @return
     * @throws Exception
     */
    int updatePlanTypeStatus(int id, String status) throws Exception;

    /**
     * 列表数据
     * @param planTypeForm
     * @return
     */
    List<PlanType> findPlanTypePager(PlanTypeForm planTypeForm, RowBounds bounds);

    /**
     * 计划模板详情
     * @param id
     * @return
     */
    PlanType findPlanTypeById(int id);
}
