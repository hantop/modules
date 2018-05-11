package com.fenlibao.p2p.dao;

import com.fenlibao.p2p.model.entity.TProductInfo;

import java.util.List;

public interface TProductInfoDao {

	List<TProductInfo> getProductInfo();
	
	TProductInfo getProductById(int id);
}
