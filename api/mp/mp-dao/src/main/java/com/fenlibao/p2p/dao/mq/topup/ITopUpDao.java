package com.fenlibao.p2p.dao.mq.topup;

import java.util.List;
import java.util.Map;

import com.fenlibao.p2p.model.mp.entity.topup.MobileTopUpErrorRecord;
import com.fenlibao.p2p.model.mp.entity.topup.MobileTopUpOrderInchargeEntity;
import com.fenlibao.p2p.model.mp.entity.topup.MobileTopupOrderEntity;
import com.fenlibao.p2p.model.mp.entity.topup.ParvalueEntity;
import com.fenlibao.p2p.model.mp.enums.topup.ParvalueType;
import com.fenlibao.p2p.model.mp.vo.topup.MobileTopUpOrderRecord;
import com.fenlibao.p2p.model.mp.vo.topup.ParvalueVO;

public interface ITopUpDao {

	/**
	 * 获取充值面值
	 * @param type
	 * @return
	 */
	List<ParvalueVO> getParvalue(ParvalueType type);
	
	/**
	 * 根据面值code获取面值信息
	 * @param code
	 * @return
	 */
	ParvalueEntity getParvalueByCode(String code);
	
	/**
	 * 添加充值订单
	 * @param order
	 * @throws Exception
	 */
	void addOrder(MobileTopupOrderEntity order) throws Exception;
	/**
	 * 添加充值订单
	 * @param order
	 * @throws Exception
	 */
	void addYishangOrder(MobileTopupOrderEntity order) throws Exception;
	
	/**
	 * 获取充值订单且锁定
	 * @param id
	 * @return
	 */
	MobileTopupOrderEntity getOrder(Map<String,Object> map);
	
	/**
	 * 更新充值订单信息
	 * @param order
	 */
	void updateOrder(MobileTopupOrderEntity order);
	
	/**
	 * 获取充值面额
	 * @param map
	 * @return
	 */
	List<ParvalueVO> getParvalue(Map<String,Object> map); 
	
	/**
	 * 获取days天内充值次数
	 * @param userId
	 * @param days
	 * @return
	 */
	int getFrequency(int userId, int days);
	
	/**
	 * 获取当前月充值次数
	 * @param userId
	 * @param days
	 * @return
	 */
	int getFrequencyByMonth(int userId);
	/**
	 * 获取充值记录
	 * @param userId
	 * @param pageNum
	 * @return
	 */
	public List<MobileTopUpOrderRecord> getMobileTopupOrderList(int userId, int pageNum);
	
	/**
	 * 获取充值中的记录
	 * @return
	 */
	public List<MobileTopUpOrderInchargeEntity> getOrderListInCharge(Integer page,Integer limit);
	/**
	 * @title 手机充值异常记录表
	 * @return
	 */
	int addTopupErrorRecord(MobileTopupOrderEntity entity);

	/**易赏--手机充值过程异常记录
	 * @throws Exception
	 */
	public List<MobileTopUpErrorRecord> yishangErrorRecords() throws Exception ;

	/**易赏--获取异常记录
	 * @throws Exception
	 */
	public MobileTopUpErrorRecord getYishangErrorRecord(Map map) throws Exception ;

	/**
	 * 易赏手机充值异常记录更新
	 * @throws Exception
	 */
	public int updateErrorRecords(MobileTopUpErrorRecord errorRecord) throws Exception;

	/**
	 * 统计当前月充值面值
	 * @param userId
	 * @return
	 */
	Integer getUserTopUpSameMonth(int userId);
}
