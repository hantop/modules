/**   
 * Copyright © 2015 fenlibao.com. All rights reserved.
 * 
 * @Title: AccessoryInfoService.java 
 * @Prject: app-client-api-service
 * @Package: com.fenlibao.p2p.service 
 * @Description: TODO
 * @author: laubrence  
 * @date: 2015-11-9 下午2:42:58 
 * @version: V1.1   
 */
package com.fenlibao.p2p.service;

import java.util.List;

import com.dimeng.p2p.S62.entities.T6232;

/** 
 * @ClassName: AccessoryInfoService 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-9 下午2:42:58  
 */
public interface AccessoryInfoService {
	
	/** 
	 * @Title: getPublicAccessory 
	 * @Description: 获取标的合同信息
	 * @param bidId
	 * @param string
	 * @return
	 * @return: List<T6232>
	 */
	public List<T6232> getPublicAccessory(int bidId, String accessoryType);
}
