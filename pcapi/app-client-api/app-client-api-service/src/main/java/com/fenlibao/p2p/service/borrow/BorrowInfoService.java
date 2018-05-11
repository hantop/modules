package com.fenlibao.p2p.service.borrow;

import com.fenlibao.p2p.model.entity.borrow.BorrowInfo;
import com.fenlibao.p2p.model.entity.borrow.BorrowerDetail;
import com.fenlibao.p2p.model.entity.borrow.RepayInfo;
import com.fenlibao.p2p.model.entity.finacing.BorrowAccountInfo;
import com.fenlibao.p2p.model.entity.finacing.RepaymentBidInfo;
import com.fenlibao.p2p.model.entity.finacing.RepaymentInfo;
import com.fenlibao.p2p.model.entity.borrow.BorrowStaticsInfo;
import com.fenlibao.p2p.model.vo.borrow.ForwardRepayInfoVO;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 借款相关
 * Created by xiao on 2016/12/26.
 */
public interface BorrowInfoService {

    /**
     * 获取借款列表 还款中和逾期
     *
     * @param userId
     * @param status
     * @return
     */
    List<BorrowInfo> getBorrowInfoList(int userId, String status, PageBounds pageBounds);

    /**
     * 获取借款列表 已还清
     *
     * @param userId
     * @return
     */
    List<BorrowInfo> getYHQBorrowInfoList(int userId, PageBounds pageBounds);

    /**
     * 获取未还统计
     *
     * @param userId
     * @return
     */
    BorrowStaticsInfo getStayRepayStatics(int userId);

    /**
     * 获取未还统计
     *
     * @param userId
     * @return
     */
    RepayInfo getCurrentRepayStatics(int userId, String bid);

    /**
     * 提前还款信息
     * @param userId
     * @param bidId
     * @return
     */
    ForwardRepayInfoVO getPreRepayStatics(int userId, String bidId) throws Exception;

    /**
     * 还款计划
     * @param userId
     * @param bidId
     * @return
     */
    List<RepaymentInfo> repaymentList(int userId,String bidId,PageBounds pageBounds,String tradeType,String status);

    /**
     * 还款标信息
     * @param userId
     * @param bidId
     * @return
     */
    RepaymentBidInfo getRepaymentBidInfo(int userId,String bidId);

    /**
     * 获取本期需要还款金额
     * @param map
     * @return
     */
    BigDecimal getRefundAmount(Map<String,Object> map);

    BorrowAccountInfo getBorrowerInfo(int userId);

    /**
     * 还款计划列表明细
     * @param userId
     * @param bidId
     * @return
     */
    List<RepaymentInfo> repaymentByTypeList(int userId,String bidId,PageBounds pageBounds,String tradeType,String status);

    /**
     * 查询借款人信息
     * @param bidId
     * @return
     */
    BorrowerDetail getBorrowerDetail(int bidId);
}
