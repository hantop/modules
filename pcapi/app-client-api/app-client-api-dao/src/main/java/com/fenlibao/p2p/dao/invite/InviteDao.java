/**   
 * Copyright © 2015 fenlibao.com. All rights reserved.
 * 
 * @Title: InviteDao.java 
 * @Prject: app-client-api-dao
 * @Package: com.fenlibao.p2p.dao.invite 
 * @Description: TODO
 * @author: laubrence  
 * @date: 2015-11-13 下午2:18:35 
 * @version: V1.1   
 */
package com.fenlibao.p2p.dao.invite;

import com.fenlibao.p2p.model.entity.invite.BeInviterInfo;
import com.fenlibao.p2p.model.entity.invite.MyAwordInfo;
import com.fenlibao.p2p.model.entity.invite.UserInviteInfo;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/** 
 * @ClassName: InviteDao 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-13 下午2:18:35  
 */
public interface InviteDao {
	/**
	 * 被邀请人信息列表
	 * @param userId
	 * @param startDate
	 * @param endDate
	 * @param beInviterPhonenum
	 * @param pageBounds
     * @return
     */
	List<BeInviterInfo> getBeinviterInfoList(Integer userId, Date startDate, Date endDate, String beInviterPhonenum, PageBounds pageBounds);

	/**
	 * @Description: 我的奖励记录
	 * @return: List<MyAwordInfo>
	 */
	List<MyAwordInfo> getMyAwordInfoList(int userId, Date startDate, Date endDate, String beInviterPhonenum, PageBounds pageBounds);

	/**
	 * @Title: getMyInviteInfo
	 * @Description: 获取我的邀请注册人数，投资人数信息
	 * @param userId
	 * @return
	 * @return: Map<String,Object>
	 */
	Map<String, Object> getMyInviteInfo(int userId);

	/**
	 * 获取邀请奖励总额
	 * @param userId
	 * @return
     */
	BigDecimal getInviteAwardSum(int userId);

	/**
	 * @Title: getUserInviteInfoList
	 * @Description: 获取用户邀请详细信息列表
	 * @param userId
	 * @return
	 * @return: List<UserInviteInfo>
	 */

	List<UserInviteInfo> getUserInviteInfoList(int userId, int pageNum);
}
