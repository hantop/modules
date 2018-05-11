package com.fenlibao.p2p.service.xinwang.bank;

import com.fenlibao.p2p.model.xinwang.entity.account.XWBankInfo;

/**
 * @date 2017/6/2 14:23
 */
public interface XWBankService {
    XWBankInfo getBankInfo(String bankCode);
}
