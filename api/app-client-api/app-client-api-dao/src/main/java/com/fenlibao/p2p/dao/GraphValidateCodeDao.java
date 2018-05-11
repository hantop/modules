package com.fenlibao.p2p.dao;

import java.util.Map;

import com.fenlibao.p2p.model.entity.GraphValidateCode;

public interface GraphValidateCodeDao {

	/**
	 * 记录图片上的验证码
	 * @param code
	 */
	public void addGraphCode(GraphValidateCode code);
	
	/**
	 * 获取验证码
	 * @param key
	 * @return
	 */
	public GraphValidateCode getGraphCode(String key);
	
	/**
	 * 更新图形验证码
	 * @param vkey
	 */
	public void updateGraphCode(Map<String, Object> map);
}
