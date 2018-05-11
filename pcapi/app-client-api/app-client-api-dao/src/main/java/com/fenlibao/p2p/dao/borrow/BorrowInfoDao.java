package com.fenlibao.p2p.dao.borrow;

import com.fenlibao.p2p.model.entity.borrow.*;
import com.fenlibao.p2p.model.entity.finacing.RepaymentBidInfo;
import com.fenlibao.p2p.model.entity.finacing.RepaymentInfo;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 借款相关
 * Created by xiao on 2016/12/26.
 */
public interface BorrowInfoDao {

    /**
     * 获取借款列表 还款中和逾期
     *
     * @return
     */
    List<BorrowInfo> getBorrowInfoList(Map<String, Object> map, PageBounds pageBounds);

    /**
     * 获取借款列表 已还清
     *
     * @return
     */
    List<BorrowInfo> getYHQBorrowInfoList(Map<String, Object> map, PageBounds pageBounds);


    /**
     * 获取未还统计
     *
     * @return
     */
    BorrowStaticsInfo getStayRepayStatics(Map<String, Object> map);

    /**
     * 获取当前期还款信息
     *
     * @return
     */
    RepayInfo getCurrentRepayStatics(Map<String, Object> map);

    /**
     * 获取提前还款信息
     *
     * @return
     */
    List<ForwardRepayInfo> getPreRepayStatics(Map<String, Object> map);

    /**
     * 还款计划
     * @param map
     * @param pageBounds
     * @return
     */
    List<RepaymentInfo> repaymentList(Map<String, Object> map, PageBounds pageBounds);

    /**
     * 还款标详情
     * @return
     */
    RepaymentBidInfo getRepaymentBidInfo(Map<String, Object> map);

    /**
     * 获取本期需要还款金额
     * @param map
     * @return
     */
    BigDecimal getRefundAmount(Map<String,Object> map);

    /**
     * 获取新网借款用户余额
     * @param userId
     * @return
     */
    BigDecimal getBorrowerBalance(Integer userId);

    /**
     * 获取新网借款用户冻结资金
     * @param userId
     * @return
     */
    BigDecimal getBorrowerFrozen(Integer userId);

    BigDecimal getBorrowTotal(Integer userId);

    /**
     * 还款计划列表明细
     * @param map
     * @param pageBounds
     * @return
     */
    List<RepaymentInfo> repaymentByTypeList(Map<String, Object> map, PageBounds pageBounds);

    BorrowerDetail getBorrowerDetail(Map<String, Object> map);
}
