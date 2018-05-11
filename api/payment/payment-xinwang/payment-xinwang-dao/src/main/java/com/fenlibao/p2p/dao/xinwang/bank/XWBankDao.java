package com.fenlibao.p2p.dao.xinwang.bank;

import com.fenlibao.p2p.model.xinwang.entity.account.XWBankInfo;

/**
 * @date 2017/6/2 14:24
 */
public interface XWBankDao {

    XWBankInfo getBankInfo(String bankCode);
}
