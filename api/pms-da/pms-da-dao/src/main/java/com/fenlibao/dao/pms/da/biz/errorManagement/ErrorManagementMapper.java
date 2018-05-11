package com.fenlibao.dao.pms.da.biz.errorManagement;

import com.fenlibao.model.pms.da.biz.viewobject.ErrorEntrustPayVO;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/11.
 */
public interface ErrorManagementMapper {
    /**
     * 委托支付授权失败列表
     *
     * @param bounds
     * @return
     */
    List<ErrorEntrustPayVO> getErrorEntrustPayList(RowBounds bounds);

    /**
     * 获取标的审核状态
     * @param loanIds
     * @return
     */
    List<Map<String, Object>> getRequestNoByLoanId(List<String> loanIds);
}
