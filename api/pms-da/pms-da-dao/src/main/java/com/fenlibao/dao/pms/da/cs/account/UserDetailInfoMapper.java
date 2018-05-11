package com.fenlibao.dao.pms.da.cs.account;

import com.fenlibao.model.pms.da.cs.UserDetail;
import com.fenlibao.model.pms.da.cs.account.UserDetailInfo;
import com.fenlibao.model.pms.da.cs.investUser.DueInAmount;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 用户详细信息
 * Created by chenzhixuan on 2015/12/25.
 */
@Repository
public interface UserDetailInfoMapper {

	/**
	 * 根据用户账号获取用户ID和姓名
	 *
	 * @param account
	 * @return
	 */
	Map<String, Object> getUserByAccount(String account);

    /**
     * 获取用户详细信息(客服专员)
     *
     * @param userId
     * @return
     */
    UserDetailInfo getUserDetailInfo(String userId);

    /**
     * 获取用户详细信息(客服主管可以看到银行卡完整信息)
     * @param userId
     * @return
     */
    UserDetailInfo getFullUserDetailInfo(String userId);

	/**
	 * 获取借款人信息
	 * @param userId
	 * @return
     */
	UserDetailInfo getLoanUserInfo(String userId);


    /**
     * 查询用户详细(手机号,姓名,昵称,身份证)
     * @param phoneNum
     * @param name
     * @param idCard
     * @param bounds
     * @return
     */
	List<UserDetail> getUserDetail(
			@Param(value = "phoneNum") String phoneNum,
			@Param(value = "name") String name,
			@Param(value = "idCard") String idCard,
			RowBounds bounds);

	/**
	 * 根据手机,姓名或者电话号码查询用户是否存在
	 * @param phoneNum
	 * @param name
	 * @param idCard
	 * @return
	 */
	List<UserDetailInfo> getUserDetailInfoByPhoneOrNameOrIdCard(
			@Param(value = "phoneNum") String phoneNum,
			@Param(value = "name") String name,
			@Param(value = "idCard") String idCard);

	/**
	 * 根据用户id获取用户基本信息
	 * @param userId
	 * @return
	 */
	UserDetail getUserDetailByUserId(String userId);

	/**
	 * 获取提现冻结金额
	 * @param userId
	 * @param cgMode CG:存管版 PT:普通版
	 * @return
	 */
	BigDecimal getWithdrawFreezeSum(@Param("userId") String userId, @Param("cgMode") String cgMode);

	/**
	 * 获取投资冻结金额
	 * @param userId
	 * @param cgNum 1：普通版 2：存管版
	 * @return
	 */
	BigDecimal getNewTenderFreezeSum(@Param("userId") String userId, @Param("cgNum") int cgNum);

	/**
	 * 获取投资冻结金额
	 * @param userId
	 * @param cgNum 1：普通版 2：存管版
	 * @return
	 */
	DueInAmount getNewDueInAmount(@Param("userId") String userId, @Param("cgNum") int cgNum);

	/**
	 * 获取投资冻结金额
	 * @param userId
	 * @param cgNum 1：普通版 2：存管版
	 * @return
	 */
	DueInAmount getPlanDueInAmount(@Param("userId") String userId, @Param("cgNum") int cgNum);

	/**
	 * 获取投资冻结金额
	 * @param userId
	 * @param cgNum 1：普通版 2：存管版
	 * @return
	 */
	BigDecimal getNewYHGains(@Param("userId") String userId, @Param("cgNum") int cgNum);

	/**
	 * 获取投资冻结金额
	 * @param userId
	 * @param cgNum 1：普通版 2：存管版
	 * @return
	 */
	BigDecimal getPlanYHGains(@Param("userId") String userId, @Param("cgNum") int cgNum);

	/**
	 * 根据用户id 获取用户在投金额
	 * @param userId
	 * @param cgNum 1：普通版 2：存管版
	 * @return
	 */
    BigDecimal getUserInvestingAmount(@Param("userId") String userId, @Param("cgNum") int cgNum);
}
