package com.fenlibao.p2p.dao.xinwang.trade;

import com.fenlibao.p2p.model.xinwang.entity.trade.ExceptionRepay;
import com.fenlibao.p2p.model.xinwang.entity.trade.XWExceptionRepayPO;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/18.
 */
public interface ExceptionRepayDao {
    public List<ExceptionRepay> getDebtUsers();

    public void saveExceptionRepayRequestNo(Map<String, Object> saveRequestNoParams);

    public XWExceptionRepayPO findExceptionRepayPoByRequestNo(String requestNo);

    public void updateExceptionRepay(Map<String, Object> exceptionRepayPO);
}
