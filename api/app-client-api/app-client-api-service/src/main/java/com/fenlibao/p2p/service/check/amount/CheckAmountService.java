package com.fenlibao.p2p.service.check.amount;

import com.fenlibao.p2p.model.check.amount.XwAccount;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zeronx on 2017/8/31.
 */
public interface CheckAmountService {
    List<XwAccount> getAllXwUsers();

    void saveUserAmountError(int userId, String platformUserNo, BigDecimal amount, BigDecimal availableAmount, BigDecimal amount1, BigDecimal freezeAmount, BigDecimal xwBalance);
}
