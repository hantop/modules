package com.fenlibao.p2p.service.xinwang.credit;

import com.fenlibao.p2p.model.xinwang.entity.credit.BaseCreditInfo;
import com.fenlibao.p2p.model.xinwang.entity.credit.SysTransferInfo;

/**
 * @date 2017/6/1 14:45
 */
public interface SysCreditService {
    /**
     * 获取债权转让记录
     * @param creditId
     * @return
     */
    SysTransferInfo getTransferInfo(int creditId);

    /**
     * 获取债权转让记录
     * @param orderId
     * @return
     */
    SysTransferInfo getInfoByOrder(int orderId);

    /**
     * 获取债权转让基础信息
     * @param creditId
     * @return
     */
    BaseCreditInfo getBaseCreditInfo(int creditId);
}
