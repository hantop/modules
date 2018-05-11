package com.fenlibao.service.pms.da.biz.errorManagement;

import com.fenlibao.model.pms.da.biz.viewobject.ErrorEntrustPayVO;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface ErrorManagementService {

    /**
     * 异常列表(目前统计的是委托支付授权失败的异常情况)
     * @param bounds
     * @return
     */
    List<ErrorEntrustPayVO> getErrorEntrustPayList(RowBounds bounds);

    /**
     * 重新发起委托支付授权
     * @param loanIds
     * @return
     */
    String handelError(List<String> loanIds) throws Exception;

}
