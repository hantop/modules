/**   
 * Copyright © 2015 fenlibao.com. All rights reserved.
 * 
 * @Title: AccessoryInfoServiceImpl.java 
 * @Prject: app-client-api-service
 * @Package: com.fenlibao.p2p.service.impl 
 * @Description: TODO
 * @author: laubrence  
 * @date: 2015-11-9 下午2:43:34 
 * @version: V1.1   
 */
package com.fenlibao.p2p.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dimeng.p2p.S62.entities.T6232;
import com.fenlibao.p2p.dao.PublicAccessoryDao;
import com.fenlibao.p2p.service.AccessoryInfoService;

/** 
 * @ClassName: AccessoryInfoServiceImpl 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-9 下午2:43:34  
 */
@Service
public class AccessoryInfoServiceImpl implements AccessoryInfoService {
	
	@Resource
	PublicAccessoryDao publicAccessoryDao;

	/**
	 * @Title: getPublicAccessory
	 * @Description: TODO
	 * @param bidId
	 * @param string
	 * @return 
	 * @see com.fenlibao.p2p.service.AccessoryInfoService#getPublicAccessory(int, java.lang.String) 
	 */
	@Override
	public List<T6232> getPublicAccessory(int bidId, String accessoryType) {

		Map<String, Object> accessoryMap = new HashMap<String, Object>();
		accessoryMap.put("bidId", bidId);
		accessoryMap.put("accessoryType", accessoryType);
		return publicAccessoryDao.getPublicAccessory(accessoryMap);
	}


}
