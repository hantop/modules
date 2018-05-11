package com.fenlibao.p2p.dao.check.amount;

import com.fenlibao.p2p.model.check.amount.XwAccount;

import java.util.List;
import java.util.Map;

/**
 * Created by zeronx on 2017/8/31.
 */
public interface CheckAmountDao {
    List<XwAccount> getAllXwUsers(Map<String, Object> params);

    int saveUserAmountError(Map<String, Object> params);
}
