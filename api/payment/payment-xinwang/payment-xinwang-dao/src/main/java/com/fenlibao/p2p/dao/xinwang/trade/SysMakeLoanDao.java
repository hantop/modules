package com.fenlibao.p2p.dao.xinwang.trade;

import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.project.XWProjectInfo;
import com.fenlibao.p2p.model.xinwang.entity.project.XWTenderRecord;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysProjectConfirmTenderInfo;

import java.util.List;
import java.util.Map;

/**
 * 放款
 */
public interface SysMakeLoanDao {
    int saveMakeLoanRequestNo(Map<String,Object> params);
    XWProjectInfo getProjectInfoByMakeLoanRequestNo(String makeLoanRequestNo);
    XWTenderRecord getTenderRecordByMakeLoanRequestNo(String makeLoanRequestNo);
    void createProjectConfirmTenderInfo(SysProjectConfirmTenderInfo projectConfirmTenderInfo);
    Integer getOngoingConfirmTenderOrder(Integer projectId);
    SysProjectConfirmTenderInfo getProjectConfirmTenderInfoByOrderId(Integer orderId);
    List<XWTenderRecord> getAcceptFailTenderRecordList(Integer projectId);
    List<String> getPlatformConfirmTenderFailRequestList(Integer projectId);
    List<XWRequest> getResultConfirmRequest(XWRequest request);
    List<XWTenderRecord> getPlatformConfirmTenderFailTenderList();
    List<Integer> getAutoConfirmTenderProjectList();
    List<SysProjectConfirmTenderInfo> getNotFinishConfirmTenderRecords();
    void createMakeloanListen(Map<String, Object> listen);

}
