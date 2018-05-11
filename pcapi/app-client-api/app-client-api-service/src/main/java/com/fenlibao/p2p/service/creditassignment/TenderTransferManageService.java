package com.fenlibao.p2p.service.creditassignment;


/**
 * 债权转入购买
 */
public abstract interface TenderTransferManageService {

	/**
	 * 用户主动购买债权
	 * 
	 * @param transferId
	 *            债权转入申请ID
	 * @return 订单ID
	 * @throws Throwable
	 */
	public int purchase(final int transferId,final int accountId) throws Throwable;

	/** 
	 * @Title: zqzrAutoCancel 
	 * @Description: 债权转让自动下架
	 * @throws Throwable
	 * @return: void
	 */
	void zqzrAutoCancel() throws Throwable ;

	/** 
	 * @Title: cancel 
	 * @Description: 债权转让撤销
	 * @param zcbId
	 * @param userId
	 * @throws Exception
	 * @return: void
	 */
	String cancel(int zcbId, int userId) throws Exception;

	/**
	 * @Description 判断用户是否可以购买该债权
	 * @param transferId
	 * @param accountId
	 * @return
	 * @throws Exception
	 */
	boolean isUserCanPurchase(int transferId, int accountId) throws Exception;


}
