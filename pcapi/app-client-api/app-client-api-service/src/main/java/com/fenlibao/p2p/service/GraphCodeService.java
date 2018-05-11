package com.fenlibao.p2p.service;

import com.fenlibao.p2p.model.entity.GraphValidateCode;

public interface GraphCodeService {

	/**
	 * 添加图形验证码
	 * @param code
	 */
	public void addGraphCode(GraphValidateCode code);
	
	/**
	 * 获取图形验证码
	 * @param key
	 * @return
	 */
	public GraphValidateCode getGraphValidateCode(String key);
	
	/**
	 * 更新图形验证码
	 * @param key
	 */
	public void updateGraphCode(String key,int status);
}
