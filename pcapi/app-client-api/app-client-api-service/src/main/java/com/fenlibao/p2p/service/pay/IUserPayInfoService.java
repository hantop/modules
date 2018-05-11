package com.fenlibao.p2p.service.pay;

import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6141;
import com.dimeng.p2p.S61.entities.T6161;

public interface IUserPayInfoService {

	public T6141 selectT6141(int userId) throws Throwable;
	
	public String getRiskItem(int userId) throws Throwable;
	
	public T6110 selectT6110(int userId) throws Throwable ;
	
	public T6161 selectT6161(int userId) throws Throwable ;

	public String getAccountName(int userId) throws Throwable;
}
