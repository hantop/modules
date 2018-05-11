package com.fenlibao.p2p.service.thirdparty;

import com.alibaba.fastjson.JSONObject;
import com.fenlibao.p2p.model.entity.bid.BidExtendRelatedGuaranteeInfo;
import com.fenlibao.thirdparty.shangshangqian.vo.RegisterUser;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 *上上签注册与ca
 */
public interface ShangshangqianRegisterService extends Callable{

	/**
	 * 注册
	 * @param ru
	 * @return
	 * @throws Exception
	 */
	JSONObject regUser(RegisterUser ru) throws Exception;


	/**
	 * 一个对象一个线程开启
	 */
	public Future objectExe(Object object, Callable callable) throws Exception;


	/**
	 * 是否在第三方平台注册
	 * @param email
	 * @param platform
	 * @return
	 */
	boolean isRegisterThirdParty(String email, int platform);

	/**
	 * 获取一个标的的投资人用户列表
	 * @param bidId
	 * @return
	 */
	List<Integer> getInvestorUserIdList(int bidId);
}