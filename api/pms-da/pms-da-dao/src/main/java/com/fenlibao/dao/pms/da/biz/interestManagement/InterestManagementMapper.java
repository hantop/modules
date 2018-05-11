package com.fenlibao.dao.pms.da.biz.interestManagement;

import com.fenlibao.model.pms.da.biz.form.InterestManagementForm;
import com.fenlibao.model.pms.da.biz.interestManagement.InterestManagementRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


public interface InterestManagementMapper {

    /*操作记录*/
    List<InterestManagementRecord> getInterestManagementRecordList(RowBounds bounds);

    /*当前已启用利息管理费比例*/
    InterestManagementForm getInterestManagementForm(@Param("state") Integer state);

    /*新增利息管理费比例(待审核)*/
    int addInterestManagement(Map<String, Object> param);

    /*利息管理费操作记录(关联利息管理费主表,需要记录前后变化)*/
    void saveInterestManagementRecord(Map<String, Object> paramMap);

    /*查询待审核利息管理费及其记录*/
    InterestManagementRecord getAuditInterestManagementInfo();

    /*查询关联利息管理费比例*/
    BigDecimal getCurrentPercentByPid(@Param("id") int id);

    /*修改利息管理费比例状态*/
    void changeInterestState(@Param("id") int id, @Param("state") int state);

    /*修改操作记录*/
    void updateInterestManagementRecord(@Param("recordId") int id, @Param("state") int state, @Param("auditor") String auditor);

    /*查询修改前记录*/
    List<Map<String, Object>> getCurrentPercents(@Param("pids") List<Integer> pids);
}
