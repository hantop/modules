package com.fenlibao.p2p.model.xinwang.entity.credit;

import com.fenlibao.p2p.model.xinwang.entity.common.BaseResult;

/**
 * 新网债权转让返回信息
 * @date 2017/6/1 15:26
 */
public class XWTransferResult extends BaseResult {
    String requestNo;
    String debentureStatus;

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getDebentureStatus() {
        return debentureStatus;
    }

    public void setDebentureStatus(String debentureStatus) {
        this.debentureStatus = debentureStatus;
    }
}
