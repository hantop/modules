package com.fenlibao.p2p.dao;

import java.util.Map;

import com.fenlibao.p2p.model.entity.UserThirdparty;

/**
 * Created by chenzhixuan on 2015/8/21.
 */
public interface UserThirdpartyDao {

    /**
     * 新增用户第三方账户
     * @param paramMap
     * @return
     */
	int addUserThirdparty(Map map);
	
	/**
	 * 判断是否已经绑定
	 */
	int isBindThirdparty(Map map);
	
	/**
	 * 判断是否已经绑定openid
	 */
	int isBindThirdpartyOpenId(Map map);

	/**
	 * 判断用户是否已绑定
	 * @param map
	 * @return
	 */
	int getUserByOpenId(Map map);

	int cancelAutoLogin(Map map);
}
