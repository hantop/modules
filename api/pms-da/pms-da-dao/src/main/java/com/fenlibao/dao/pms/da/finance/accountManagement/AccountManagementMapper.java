package com.fenlibao.dao.pms.da.finance.accountManagement;

import com.fenlibao.model.pms.da.cs.account.Transaction;
import com.fenlibao.model.pms.da.finance.T6101Extend;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/26.
 */

public interface AccountManagementMapper {

     List<T6101Extend> findList();

     List<Transaction> findTradeHistory(@Param(value = "flowId")int flowId ,
                                        @Param(value = "startDate")Date startDate,
                                        @Param(value = "endDate")Date endDate,
                                        RowBounds bounds);

     /**
      * 获取资金账户id
      * @param accountType
      * @return
      */
     int getFlowId(@Param(value = "userId")int userId, @Param(value = "accountType")String accountType);

     /**
      * 获取交易类型
      * @return
      */
     List<Map<String, Object>> getTradeTypeMap();

}
