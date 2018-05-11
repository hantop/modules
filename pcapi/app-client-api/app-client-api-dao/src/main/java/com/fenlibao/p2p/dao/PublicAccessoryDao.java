package com.fenlibao.p2p.dao;

import java.util.List;
import java.util.Map;

import com.dimeng.p2p.S62.entities.T6232;
import com.fenlibao.p2p.model.entity.bid.BidFiles;

public interface PublicAccessoryDao {

	/**
	 * 获取标的附件
	 * @param map
	 * @return
	 */
	List<T6232> getPublicAccessory(Map<String, Object> map);
	
	/**
	 * 获取标的证明文件
	 * @param map
	 * @return
	 */
	public List<BidFiles> getPublicAccessoryFiles(Map<String, Object> map);
}
