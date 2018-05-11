package com.fenlibao.p2p.service.bid;

import com.fenlibao.p2p.model.enums.VersionTypeEnum;

/**
 * 投标前校验相关
 * @author Mingway.Xu
 * @date 2017/3/1 11:10
 */
public interface PreDoBidService {
    /**
     * 投标前校验用户是否可投
     * @param loanId
     * @param userId
     * @return
     */
    boolean checkCanInvestBid(int loanId, int userId, VersionTypeEnum versionTypeEnum);
}
