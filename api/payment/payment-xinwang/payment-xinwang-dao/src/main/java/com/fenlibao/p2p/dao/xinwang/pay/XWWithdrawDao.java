package com.fenlibao.p2p.dao.xinwang.pay;

import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.pay.SysWithdrawApply;
import com.fenlibao.p2p.model.xinwang.entity.pay.XWWithdrawRequest;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/9.
 */
public interface XWWithdrawDao {
    /**
     * 只获取一个提现申请成功的提现申请订单,用于判断是否需要有续费
     * @param userId
     * @return
     */
    Integer getSuccessApplyId(Integer userId);

    /**
     * 插入t6130
     * @param withdrawApply
     */
    void createWithdrawApply(SysWithdrawApply withdrawApply);

    /**
     *  t_xw_withdraw
     */
    void createWithdrawRequest(XWWithdrawRequest withdrawRequest);

    /**
     *  t_xw_withdraw
     */
    void updateWithdrawRequest(Map<String,Object> params);

    /**
     * 更新t6130
     * @param params
     */
    void updateWithdrawApply(Map<String,Object> params);

    /**
     * 获取用户提现页面超时请求，定时器确认是否真的没有受理成功
     */
    List<XWRequest> getPageExpiredRequest();

    /**
     * 考虑到要对账确认，查询所有待确认超过36小时的请求
     * @return
     */
    List<XWRequest> getResultConfirmRequest();
}
