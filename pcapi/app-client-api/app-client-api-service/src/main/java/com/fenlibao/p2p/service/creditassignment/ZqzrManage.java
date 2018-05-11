package com.fenlibao.p2p.service.creditassignment;

import com.fenlibao.p2p.model.entity.creditassignment.AddTransfer;
import com.fenlibao.p2p.model.entity.creditassignment.Zqzrlb;
import com.fenlibao.p2p.model.vo.creditassignment.ApplyforDetailVO;
import com.fenlibao.p2p.model.vo.creditassignment.ApplyforDetailVO_131;
import com.fenlibao.p2p.util.Pager;

public abstract interface ZqzrManage{
	

	/**
	 * 转让债权
	 * @param zcbId
	 * @throws Throwable
	 */
	public void transfer(AddTransfer addTransfer)throws Throwable;
	
	/**
	 * 债权转让申请列表
	 * @param status  申请状态
	 * @param time    申请债权转让创建时间
	 * @return
	 */
	public Pager<Zqzrlb> applyforList(String status, int curpage, int limit, String isTransfer, String timestamp);
	
	/**
	 * 债权转让申请详情
	 * @param status      申请状态
	 * @param applyforId  申请债权转让ID
	 * @return
	 */
	public ApplyforDetailVO applyforDetail(String status,int applyforId,String isTransfer);
	
	/**
	 * 债权转让申请次数
	 * @param creditId
	 * @return
	 */
	public int getCreditassignmentCount(int creditId);
	
	/**
	 * 债权转让成功记录
	 * @param applyforId  债权转让申请ID
	 * @return
	 */
	public int getRecordCount(int applyforId);

	/**
	 * 债权转让申请详情
	 * @param status      申请状态
	 * @param applyforId  申请债权转让ID
	 * @return
	 * @throws Exception 
	 */
	public ApplyforDetailVO_131 applyforDetail_131(String status,int applyforId,String isTransfer) throws Exception;
	
}
