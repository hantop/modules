package com.fenlibao.service.pms.da.cs;

import com.fenlibao.model.pms.da.cs.BorrowerAccountInfo;
import com.fenlibao.model.pms.da.cs.BussinessInfo;
import com.fenlibao.model.pms.da.cs.form.BorrowerAccountForm;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface BorrowerAccountService {

    /**
     * 借款用户基本信息
     * @param borrowerAccountForm
     * @param bounds
     * @return
     */
    List<BorrowerAccountInfo> getBorrowerAccountInfoList(BorrowerAccountForm borrowerAccountForm, RowBounds bounds);

    /**
     * 获取企业借款人企业信息
     * @param userId
     * @return
     */
    BussinessInfo getBussinessInfoByUserId(Integer userId);
}
