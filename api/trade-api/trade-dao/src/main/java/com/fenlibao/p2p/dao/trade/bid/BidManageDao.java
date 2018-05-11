package com.fenlibao.p2p.dao.trade.bid;

import com.fenlibao.p2p.model.trade.entity.*;
import com.fenlibao.p2p.model.trade.entity.bid.T6504;

import java.util.List;
import java.util.Map;

public interface BidManageDao {
    T6230 getBidById(Integer loanId) throws Exception;
    T6230 getBidByCode(String loanCode) throws Exception;
    void releaseBid(Map<String,Object> params) throws Exception;
    T6231 getBidExInfoById(Integer loanId) throws Exception;
    List<T6250> getTenderRecord(Map<String,Object> params) throws Exception;
    T6238 getBidRateById(Integer loanId) throws Exception;
    T6250 getTenderRecordById(Integer id) throws Exception;
    void updateTenderRecord(Map<String,Object> params)throws Exception;
    void updateBid(Map<String,Object> params);
    void updateBidExInfo(Map<String,Object> params);
    void insertDebt(List<T6251> list);
    List<T6251> getDebts(Map<String,Object> params);
    T6251 getDebtById(int id);
    void insertRepayPlan(List<T6252> list);
    T6252 getAndLockRepayPlan(Map<String,Object> params);
    List<T6252> getRepayPlan(Map<String,Object> params);
    T6252 getRepayPlanById(int id);
    void updateRepayPlan(Map<String,Object> params);
    void updateDebt(Map<String,Object> params);
    List<T6252> getSybjOfDebt(Map<String,Object> params);
    void updateT6252TQH(Map<String,Object> params);
    T6252 getSybjByDebt(Map<String,Object> params);
    T6260 getDebtTransferApply(Map<String,Object> params);
    void updateDebtTransferApply(Map<String,Object> params);
    int getTotalTerms(int F02);
    List<Integer> getSealBidAccounts();

    /**
     * 获取投标订单
     * @param orderId
     * @return
     */
    T6504 getTenderOrder(int orderId);
    /**
     * 添加投标记录
     * @param tenderRecord
     * @return
     * @throws Exception
     */
    void addTenderRecord(T6250 tenderRecord) throws Exception;

    /**
     * 更新投标订单
     * @param orderId
     * @param recordId
     */
    void updateTenderOrder(int orderId, int recordId);

    /**
     * 获取投标待确认的订单数
     * @param bidId
     * @param tenderId
     * @return
     */
    int countTenderOrderOfDQR(int bidId, int tenderId);
}
