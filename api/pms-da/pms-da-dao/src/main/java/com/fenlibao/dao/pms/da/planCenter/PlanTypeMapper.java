package com.fenlibao.dao.pms.da.planCenter;

import com.fenlibao.model.pms.da.planCenter.PlanType;
import com.fenlibao.model.pms.da.planCenter.form.PlanTypeForm;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by Administrator on 2017/3/21.
 */
public interface PlanTypeMapper {

    /**
     * 更新计划模板
     * @param PlanType
     * @return
     */
    int updatePlanTypeByPrimaryKey(PlanType PlanType);

    /**
     * 设置停用/启用
     * @param id
     * @param status
     * @return
     */
    int setStatus(@Param("id") int id, @Param("status") String status);

    /**
     * 列表
     * @param PlanTypeForm
     * @return
     */
    List<PlanType> findPlanTypePager(PlanTypeForm PlanTypeForm, RowBounds bounds);

    /**
     * 计划模板详情
     * @param id
     * @return
     */
    PlanType findPlanTypeById(int id);

}
