/**   
 * Copyright © 2015 fenlibao.com. All rights reserved.
 * 
 * @Title: AccessTokenServiceImpl.java 
 * @Prject: app-client-api-service
 * @Package: com.fenlibao.p2p.service.thirdparty.impl 
 * @Description: TODO
 * @author: laubrence  
 * @date: 2015-11-11 下午7:22:20 
 * @version: V1.1   
 */
package com.fenlibao.p2p.service.thirdparty.impl;

import java.util.Date;

import javax.annotation.Resource;

import com.fenlibao.p2p.util.DateUtil;
import org.springframework.stereotype.Service;

import com.fenlibao.p2p.common.util.token.TokenUtil;
import com.fenlibao.p2p.dao.WebAccessDao;
import com.fenlibao.p2p.model.entity.AccessToken;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.service.thirdparty.WebAccessService;

/** 
 * @ClassName: AccessTokenServiceImpl 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-11 下午7:22:20  
 */
@Service
public class WebAccessServiceImpl implements WebAccessService {
	
	@Resource
	WebAccessDao webAcessDao;
	
	/**
	 * @Title: buildAccessToken
	 * @Description: 创建验证标识码
	 * @param userId
	 * @return
	 * @throws Exception 
	 */
	@Override
	public String buildAccessToken(int userId, String clientType, String targetClientType) throws Exception{
		String accessToken = createAccessToken();
		long createTime = DateUtil.dateToTimestampToSec(DateUtil.nowDate());
		long expireTime = DateUtil.dateToTimestampToSec(DateUtil.nowDate()) + InterfaceConst.accessTime ;
		Date createDatetime = DateUtil.timestampToDateBySec(createTime);
		Date expireDatetime = DateUtil.timestampToDateBySec(expireTime);
		int isSuccess = webAcessDao.insertAccessToken(userId, clientType, targetClientType, accessToken, createDatetime, expireDatetime);
		if(isSuccess>0) {
			return accessToken;
		}
		return null;
	}

    /**
     * 获取AccessToken
     * @param accessToken
     * @return
     * @throws Exception
     */
	@Override
	public AccessToken getAccessToken(String accessToken) throws Exception{
        return webAcessDao.getAccessToken(accessToken);
	}

    @Override
    public int updateVaildAccessToken(int recordId) {
        int isVerified = InterfaceConst.LOGIC_YES;
        Date nowDatetime = DateUtil.nowDate();
        return webAcessDao.updateVaildAccessToken(recordId,isVerified,nowDatetime);
    }

    /**
	 * @Title: createAccessToken 
	 * @Description: 生成全球唯一标识
	 * @return
	 * @return: String
	 */
	public String createAccessToken(){
		return TokenUtil.createToken();
	}

	
}
