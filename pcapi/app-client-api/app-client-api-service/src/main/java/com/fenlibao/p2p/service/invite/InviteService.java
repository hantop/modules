/**   
 * Copyright © 2015 fenlibao.com. All rights reserved.
 * 
 * @Title: InviteService.java 
 * @Prject: app-client-api-service
 * @Package: com.fenlibao.p2p.service.invite 
 * @Description: TODO
 * @author: laubrence  
 * @date: 2015-11-13 下午2:15:32 
 * @version: V1.1   
 */
package com.fenlibao.p2p.service.invite;

import com.fenlibao.p2p.model.entity.invite.BeInviterInfo;
import com.fenlibao.p2p.model.entity.invite.MyAwordInfo;
import com.fenlibao.p2p.model.entity.invite.UserInviteInfo;
import com.fenlibao.p2p.model.vo.invite.MyInviteInfoVO;
import com.fenlibao.p2p.util.paginator.domain.PageBounds;

import java.util.List;

/** 
 * @ClassName: InviteService 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-13 下午2:15:32  
 */
public interface InviteService {

	/**
	 * 被邀请人信息列表
	 * @param userId
	 * @param inviteStartTime
	 * @param inviteEndTime
	 * @param beInviterPhonenum
	 * @param pageBounds
     * @return
     */
	List<BeInviterInfo> getBeinviterInfoList(Integer userId, String inviteStartTime, String inviteEndTime, String beInviterPhonenum, PageBounds pageBounds);

	/**
	 * @Description: 我的奖励记录
	 * @return: List<MyAwordInfo>
	 */
	List<MyAwordInfo> getMyAwordInfoList(int userId, String inviteStartTime, String inviteEndTime, String beInviterPhonenum, PageBounds pageBounds) throws Exception;

	/**
	 * @Title: getMyInviteInfo
	 * @Description: 获取用户的邀请信息的注册人数、投资人数和邀请奖励总额
	 * @param userId
	 * @return
	 * @return: Map<String,Object>
	 */
	MyInviteInfoVO getMyInviteInfo(int userId) throws Exception;


	/**
	 * @Title: getUserInviteInfoList
	 * @Description: 邀请用户信息列表
	 * @param userId
	 * @return
	 * @return: List<UserInviteInfo>
	 */
	List<UserInviteInfo> getUserInviteInfoList(int userId, int pageNum);
}
