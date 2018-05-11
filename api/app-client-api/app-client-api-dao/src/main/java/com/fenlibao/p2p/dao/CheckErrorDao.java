package com.fenlibao.p2p.dao;

import com.fenlibao.p2p.model.entity.CheckError;

import java.util.Map;

public interface CheckErrorDao {

	public int matchVerifyCodeErrorCount(Map map);
	
	public void insertMatchVerifyCodeError(CheckError checkError);
}
