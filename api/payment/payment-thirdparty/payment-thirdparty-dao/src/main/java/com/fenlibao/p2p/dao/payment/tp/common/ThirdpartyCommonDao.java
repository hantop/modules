package com.fenlibao.p2p.dao.payment.tp.common;

import com.fenlibao.p2p.model.payment.tp.common.entity.PayExtend;

public interface ThirdpartyCommonDao {
	public PayExtend getPayExtend(int userId);
    public void insertPayExtend(PayExtend payExtend);
    public void updatePayExtend(PayExtend payExtend);
}
