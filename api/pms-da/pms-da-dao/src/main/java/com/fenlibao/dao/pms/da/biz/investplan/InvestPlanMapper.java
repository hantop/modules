package com.fenlibao.dao.pms.da.biz.investplan;

import com.fenlibao.model.pms.da.planCenter.InvestPlan;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * 投资计划
 * <p>
 * Created by chenzhixuan on 2017/3/21.
 */
public interface InvestPlanMapper {
    void updateRecommendTime(@Param("id")int id, @Param("flag") boolean flag);

    void updatePlanAuditStatus(@Param("id") int id, @Param("status") int status);

    void updatePlanInvalidStatus(@Param("id") int id, @Param("status") int status);

    void updatePlanReleaseStatus(@Param("id") int id, @Param("status") int status);

    void updatePlanTimingRelease(@Param("id") int id, @Param("relaseTime") Date relaseTime, @Param("displayTime") Date displayTime, @Param("status") int status);

    void updatePlanTermination(@Param("id") int id, @Param("expireTime") Date expireTime, @Param("bearrateDate") Date bearrateDate, @Param("fullTime") Date fullTime, @Param("status") int status);

    void insertPlanTermination(@Param("planId") int planId, @Param("operatorId") int operatorId);

    void updateSticktopTime(@Param("id")int id, @Param("flag") boolean flag);

    void cancelTimingRelease(@Param("id") int id, @Param("status") int status);

    InvestPlan getInvestPlan(@Param("id") int id);

}
