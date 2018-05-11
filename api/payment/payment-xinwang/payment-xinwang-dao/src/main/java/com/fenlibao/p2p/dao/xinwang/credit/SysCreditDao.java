package com.fenlibao.p2p.dao.xinwang.credit;

import com.fenlibao.p2p.model.xinwang.entity.credit.BaseCreditInfo;
import com.fenlibao.p2p.model.xinwang.entity.credit.SysCredit;
import com.fenlibao.p2p.model.xinwang.entity.credit.SysCreditTransferApply;
import com.fenlibao.p2p.model.xinwang.entity.credit.SysTransferInfo;

import java.util.List;
import java.util.Map;

/**
 * @date 2017/6/1 14:53
 */
public interface SysCreditDao {
    /**
     * 获取购买债权订单中的信息
     * @param orderId
     * @return
     */
    SysTransferInfo getTransferInfoByOrder(int orderId);

    /**
     * 创建债权t6251
     * @param credit
     */
    void createCredit(SysCredit credit);

    /**
     * 获取债权信息
     * @return
     */
    List<SysCredit> getCreditInfoByProjectId(Integer projectId);

    /**
     * 获取债权信息
     * @param id
     * @return
     */
    SysCredit getCreditInfoById(Integer id);

    /**
     * 更新债权信息t6251
     * @param params
     */
    void updateCreditInfoById(Map<String,Object> params);

    /**
     * 获取债权转让申请信息
     * @return
     */
    SysCreditTransferApply getTransferingCreditByCreditId(Integer creditId);

    /**
     * 更新债权转让申请t6260
     * @param params
     */
    void updateCreditTransferApplyById(Map<String,Object> params);

    /**
     * 获取债权信息
     * @param creditId
     * @return
     */
    SysTransferInfo getTransferInfo(int creditId);

    /**
     * 获取债权基础信息
     * @param creditId
     * @return
     */
    BaseCreditInfo getBaseCreditInfo(int creditId);
}
