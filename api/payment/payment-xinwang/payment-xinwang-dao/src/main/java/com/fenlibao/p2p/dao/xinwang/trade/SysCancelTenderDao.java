package com.fenlibao.p2p.dao.xinwang.trade;

import com.fenlibao.p2p.model.xinwang.entity.project.XWTenderRecord;
import com.fenlibao.p2p.model.xinwang.entity.trade.SysProjectCancelTenderInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/29.
 */
public interface SysCancelTenderDao {
    void createProjectCancelTenderInfo(SysProjectCancelTenderInfo projectCancelTenderInfo);
    SysProjectCancelTenderInfo getProjectCancelTenderInfo(Integer orderId);
    void saveCancelTenderRequestNo(Map<String,Object> params);
    List<XWTenderRecord> getAcceptFailTenderRecordList(Integer projectId);
    List<XWTenderRecord> getPlatformCancelTenderFailList(Integer projectId);
    Integer getOngoingCancelTenderOrder(Integer projectId);
}
