package com.fenlibao.service.pms.da.biz.interestManagement;

import com.fenlibao.model.pms.da.biz.form.InterestManagementForm;
import com.fenlibao.model.pms.da.biz.interestManagement.InterestManagementRecord;
import org.apache.ibatis.session.RowBounds;

import java.util.List;


public interface InterestManagementService {

    /**
     * 历史操作记录
     * @param rowBounds
     * @return
     */
    List<InterestManagementRecord> getInterestManagementRecordList(RowBounds rowBounds);

    /**
     * 查询当前启用利息管理费比例
     *
     * @return
     */
    InterestManagementForm getInterestManagementForm(Integer state);

    /**
     * 查询待审核利息管理费记录
     *
     * @return
     */
    InterestManagementRecord getAuditInterestManagementInfo();

    /**
     * 修改利息管理费逻辑
     *
     * @param interestManagementForm
     * @return
     */
    void updateInterestManagementLogic(InterestManagementForm interestManagementForm);

    /**
     * 新增操作记录
     * @param interestManagementRecord
     */
    void saveInterestManagementRecord(InterestManagementRecord interestManagementRecord);

    /**
     * 修改状态
     *
     * @param id
     * @param recordId
     * @param state
     */
    void changeInterestState(int recordId, int id, int state);

}
