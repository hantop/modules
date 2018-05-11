package com.fenlibao.p2p.dao;

import com.fenlibao.p2p.model.entity.SmsValidcode;
import com.fenlibao.p2p.model.entity.SpecialAccount;

import java.util.List;
import java.util.Map;

public interface SpecialAccountDao {

	/**
	 * 获取需要短信通知余额预警的手机号码
	 * @param type
	 * @return
     */
	public List<SpecialAccount> getSpecialPhone(String type) ;

	/**
	 * 更新超级账户短信发送次数
	 * @param specialAccount
     */
	public void updateSpecialAccountSendCount(SpecialAccount specialAccount);
}
