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

import java.util.List;
import java.util.Map;

import com.fenlibao.p2p.model.entity.invite.MyAwordInfo;
import com.fenlibao.p2p.model.entity.invite.UserInviteInfo;

/** 
 * @ClassName: InviteService 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-13 下午2:15:32  
 */
public interface InviteService {

	/** 
	 * @Title: getMyInviteInfo 
	 * @Description: 获取用户的邀请信息的注册人数和投资人数
	 * @param userId
	 * @return
	 * @return: Map<String,Object>
	 */
	Map<String, Object> getMyInviteInfo(int userId);

	/** 
	 * @Title: getUserInviteInfoList 
	 * @Description: 邀请用户信息列表
	 * @param userId
	 * @return
	 * @return: List<UserInviteInfo>
	 */
	List<UserInviteInfo> getUserInviteInfoList(int userId, int pageNum);
	
	
	/** 
	 * @Description: 统计我的邀请注册人数，获奖
	 * @param userId
	 * @return
	 * @return: Map<String,Object>
	 */
	Map<String, Object> countMyInviteInfo(int userId);
	/** 
	 * @Description: 获取用户邀请注册信息列表
	 * @param userId
	 * @return
	 * @return: List<UserInviteInfo>
	 */
	List<UserInviteInfo> getMyInviteInfoList(int userId, int pageNo, int pagesize);
	
	/** 
	 * @Description: 我的奖励记录
	 * @return: List<MyAwordInfo>
	 */
	List<MyAwordInfo> getMyAwordInfoList(int userId, int pageNo, int pagesize);
}
