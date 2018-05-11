package com.fenlibao.p2p.service;

import com.fenlibao.p2p.model.entity.SpecialAccount;
import com.fenlibao.p2p.model.vo.AdImageVo;
import com.fenlibao.p2p.model.vo.ApkUpdateVo;

import java.util.List;

public interface SpecialAccountService {

	/**
	 * 获取需要短信通知余额预警的手机号码
	 * @param type
	 * @return
	 */
	public List<SpecialAccount> getSpecialPhone(String type) ;

	/**
	 * 发送短信
	 */
	public void sendMessage(String phoneNum,String content) throws Exception;

	/**
	 * 更新超级账户短信发送次数
	 * @param specialAccount
     */
	public void updateSpecialAccountSendCount(SpecialAccount specialAccount);
}
