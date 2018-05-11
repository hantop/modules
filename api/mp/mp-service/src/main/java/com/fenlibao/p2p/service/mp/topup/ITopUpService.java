package com.fenlibao.p2p.service.mp.topup;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.fenlibao.p2p.model.entity.consumption.ConsumptionOrderEntity;
import com.fenlibao.p2p.model.global.HttpResponse;
import com.fenlibao.p2p.model.mp.entity.topup.MobileTopUpErrorRecord;
import com.fenlibao.p2p.model.mp.entity.topup.MobileTopUpOrderInchargeEntity;
import com.fenlibao.p2p.model.mp.entity.topup.MobileTopupOrderEntity;
import com.fenlibao.p2p.model.mp.entity.topup.ParvalueEntity;
import com.fenlibao.p2p.model.mp.enums.topup.ParvalueType;
import com.fenlibao.p2p.model.mp.vo.topup.MobileTopUpCallbackVO;
import com.fenlibao.p2p.model.mp.vo.topup.MobileTopUpOrder;
import com.fenlibao.p2p.model.mp.vo.topup.MobileTopUpOrderRecord;
import com.fenlibao.p2p.model.mp.vo.topup.ParvalueVO;

/**
 * 充值相关service
 * @author yangzengcai
 * @date 2016年2月17日
 */
public interface ITopUpService {

	/**
	 * 手机话费充值
	 * @throws Exception
	 */
	String mobileTopUp(MobileTopUpOrder order) throws Exception;
	
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
	 * @param userId
	 * @param parvalueCode
	 * @param phoneNum
	 * @return 
	 * @throws Exception
	 */
	MobileTopUpOrder addOrder(Integer userId, String parvalueCode, String phoneNum, 
			String integralCode, String integralQty) throws Exception;
	
	/**
	 * 更新充值订单信息
	 */
	String updateOrderInfo(MobileTopUpCallbackVO vo)throws Exception;
	
	void updateOrderInfo(MobileTopUpOrder order,String integralCode,String integralQty,Integer userId,BigDecimal userPayAmount,String orderNum, Integer status)throws Exception;
	/**
	 * 获取订单信息
	 * @param id
	 * @param orderNum
	 * @return
	 */
	public MobileTopupOrderEntity getOrderInfo(Integer id,String orderNum,Integer status);
	
	public MobileTopupOrderEntity getOrderInfoById(Integer id,Integer status);
	
	public MobileTopupOrderEntity getOrderInfoByOrderNum(String orderNum);
	
	/**
	 * 获取days天内充值次数
	 * @param userId
	 * @param days
	 * @return
	 */
	int getFrequency(int userId, int days);
	
	/**
	 * 添加消费订单和易赏充值订单
	 * @return
	 * @throws Exception
	 */
	public boolean checkYishang(String phone) throws Exception;

	/**
	 * 添加消费订单和易赏充值订单
	 * @param parvalueCode 充值面额编码
	 * @param phoneNum 充值手机
	 * @param integralCode 使用积分类型编码
	 * @param integralQty 使用积分数量
	 * @return 
	 * @throws Exception
	 */
	public MobileTopupOrderEntity addYishangOrder(Integer userId,String parvalueCode, 
			String phoneNum, String integralCode, String integralQty) throws Exception;
	
	
	/**
	 * 易赏手机充值
	 * @param consumptionOrderEntity 消费订单id
	 * @return 
	 * @throws Exception
	 */
	public HttpResponse yishangTopUp(ConsumptionOrderEntity consumptionOrderEntity) throws Exception;
	
	
	/**
	 * 易赏手机订单更新
	 * @param order
	 * @param state
	 * @return 
	 * @throws Exception
	 */
	public void updateYishangOrderInfo(MobileTopupOrderEntity order,int state) throws Exception;
	/**
	 * 易赏手机订单更新-回调时
	 * @param order
	 * @return 
	 * @throws Exception
	 */
	public String updateYishangOrderInfo(MobileTopupOrderEntity order) throws Exception;
	
	/**
	 * 易赏手机充值记录
	 * @return
	 * @throws Exception
	 */
	public List<MobileTopUpOrderRecord> getMobileTopupOrderList(int userId, int pageNum);
	
	
	/**
	 * 易赏手机充值中记录列表
	 * @param page
	 * @param limit
	 * @throws Exception
	 */
	public List<MobileTopUpOrderInchargeEntity> yishangQueryStatusList(Integer page,Integer limit) throws Exception;
	
	/**易赏手机充值中记录主动查询
	 * @throws Exception
	 */
	public Map<String,Object> yishangQueryStatus(MobileTopUpOrderInchargeEntity mobileTopUpOrderInchargeEntity) throws Exception;

	/**易赏手机充值中记录查询结果处理
	 * @throws Exception
	 */
	public void yishangQueryStatusResult(Map<String,Object> resultMap) throws Exception ;

	/**易赏--手机充值过程异常记录
	 * @throws Exception
	 */
	public List<MobileTopUpErrorRecord> yishangErrorRecords() throws Exception ;

	/**易赏--获取异常记录
	 * @throws Exception
	 */
	public MobileTopUpErrorRecord getYishangErrorRecord(Map map) throws Exception ;

	/**易赏手机充值异常记录查询结果处理
	 * @throws Exception
	 */
	public void yishangDealErrorRecord(Map<String,Object> resultMap) throws Exception ;
	public Map<String,Object> yishangQueryStatus(MobileTopUpErrorRecord mobileTopUpErrorRecord) throws Exception;

}
