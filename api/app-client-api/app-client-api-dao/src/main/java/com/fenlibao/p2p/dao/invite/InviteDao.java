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

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fenlibao.p2p.model.entity.invite.MyAwordInfo;
import com.fenlibao.p2p.model.entity.invite.UserInviteInfo;

/** 
 * @ClassName: InviteDao 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-13 下午2:18:35  
 */
public interface InviteDao {

	/** 
	 * @Title: getMyInviteInfo 
	 * @Description: 获取我的邀请注册人数，投资人数信息
	 * @param userId
	 * @return
	 * @return: Map<String,Object>
	 */
	Map<String, Object> getMyInviteInfo(int userId);

	/** 
	 * @Title: getUserInviteInfoList 
	 * @Description: 获取用户邀请详细信息列表
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
	 * @return: List<UserInviteInfo>
	 */
	List<UserInviteInfo> getMyInviteInfoList(int userId, int pageNo, int pagesize);
	
	
	/** 
	 * @Description: 我的奖励记录
	 * @return: List<MyAwordInfo>
	 */
	List<MyAwordInfo> getMyAwordInfoList(int userId, int pageNo, int pagesize);
}

