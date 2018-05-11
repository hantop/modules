package com.fenlibao.service.pms.da.cs.account;

import com.fenlibao.model.pms.da.cs.UserDetail;
import com.fenlibao.model.pms.da.cs.account.UserDetailInfo;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * 用户详细信息
 * Created by chenzhixuan on 2015/12/25.
 */
public interface UserDetailInfoService {

	/**
	 * 根据用户账号获取用户ID
	 *
	 * @param account
	 * @return
	 */
    Map<String, Object> getUserByAccount(String account);

    /**
     * 获取用户详细信息
     * @param userId
     * @return
     */
    UserDetailInfo getUserDetailInfo(String userId);

    /**
     * 客服主管可以获取用户的详细信息(银行卡完整显示)
     * @param userId
     * @return
     */
	UserDetailInfo getFullUserDetailInfo(String userId);

	/**
	 * 根据电话,姓名,身份证查询用户信息列表
	 * @param phoneNum
	 * @param name
	 * @param idCard
	 * @param bounds
	 * @return
	 */
	List<UserDetail> getUserDetail(String phoneNum, String name, String idCard, RowBounds bounds);

	/**
	 * 根据手机,姓名或者电话号码查询用户是否存在
	 * @param phoneNum
	 * @param name
	 * @param idCard
	 * @return
	 */
	List<UserDetailInfo> getUserDetailInfoByPhoneOrNameOrIdCard(String phoneNum, String name, String idCard);
}
