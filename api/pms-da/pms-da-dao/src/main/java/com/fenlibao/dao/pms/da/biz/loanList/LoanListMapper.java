package com.fenlibao.dao.pms.da.biz.loanList;

import com.fenlibao.model.pms.da.biz.LoanList;
import com.fenlibao.model.pms.da.biz.form.LoanListForm;
import com.fenlibao.model.pms.da.biz.viewobject.EntrustPayBid;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/17.
 */
public interface LoanListMapper {

    List<LoanList> getLoanList(
            LoanListForm loanListForm,
            RowBounds bounds);

    /**
     * 查询标的状态
     * @param loanId
     * @return
     */
    String getBidStatus(@Param("loanId") int loanId);

    /**
     * 查询当前放款订单状态
     * @param bidIds
     * @return
     */
    List<Map<String, Object>> getOrderStatus(@Param("bidIds") List<Integer> bidIds);

    /**
     * 委托支付标相关信息
     * @param loanId
     * @return
     */
    EntrustPayBid getEntrustPayBidByLoanId(@Param("loanId") int loanId);

    /**
     * 获取用户详细信息
     * @param userIds
     * @return
     */
    List<Map<String, Object>> getUserDetail (@Param("userIds") List<Integer> userIds);

    /**
     * 获取标的放款订单状态
     * @param loanIds
     * @return
     */
    List<Map<String, Object>> getFlowBidOrderState (@Param("loanIds") List<Integer> loanIds);

}
