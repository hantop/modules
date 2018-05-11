package com.fenlibao.p2p.dao.xinwang.project;

import com.fenlibao.p2p.model.xinwang.bo.InvestorBO;
import com.fenlibao.p2p.model.xinwang.bo.XWTenderBO;
import com.fenlibao.p2p.model.xinwang.entity.project.*;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysTenderRecord;
import com.fenlibao.p2p.model.xinwang.enums.entrust.AuthorizeStatus;
import com.fenlibao.p2p.model.xinwang.enums.project.PTProjectState;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/19.
 */
public interface XWProjectDao {
    XWProjectInfo getProjectInfoById(Integer loanId);
    void establishProject(Map<String,Object> params);
    List<XWTenderRecord> getTenderRecord(Map<String,Object> params);
    XWProjectRate getProjectRateById(Integer loanId);
    XWTenderRecord getTenderRecordById(Integer id);
    void insertTenderEntity(XWTenderEntity tenderEntity);
    void updateTenderRecordById(Map<String,Object> params);
    void updateProjectExtraInfo(Map<String,Object> params);
    void batchInsertRepaymentPlan(List<XWRepaymentPlan> repaymentPlan);
    int updateProjectInfo(Map<String,Object> params);
    XWProjectExtraInfo getProjectExtraInfo(Integer projectId);
    void setEntrustInfo(Integer loadId, String entrustedPlatformUserNo, String requestNo, AuthorizeStatus status);
    List<Integer> getPreEstablishProject();
    void projectJoinPlan(Integer projectId);
    Integer lockRepayStatus(Integer projectId);

    /**
     * 获取需要投资的列表
     * @return
     */
    List<XWTenderBO> getSendTender();

    void updateXWTenderSend(Integer id,Boolean send);

    List<SysTenderRecord> getSysTenderRecord(SysTenderRecord tenderRecord);

    List<Integer> getBidByStatus(PTProjectState state);


    int updateProjectStatus(Map<String, Object> projectInfoParams);

    InvestorBO getInvestorBO(Integer id);


    /**
     * 保存提前还款的违约金配置
     * @param xwProjectPrepaymentConfig
     */
    void saveXWProjectPrepaymentConfig(XWProjectPrepaymentConfig xwProjectPrepaymentConfig);

    /**
     * 获取利息管理费比例
     * @param state
     * @return
     */
    BigDecimal getInterestPercent(Integer state);

    int updateInterestPercent(Integer loanId, BigDecimal percent);

    /**
     * 已操作了流标但标的仍然处于待放款状态记录
     * @param state
     * @return
     */
    List<Integer> getCancelBidsByStatus(PTProjectState state);

}
