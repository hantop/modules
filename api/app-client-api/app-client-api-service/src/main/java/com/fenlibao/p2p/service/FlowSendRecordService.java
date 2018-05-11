package com.fenlibao.p2p.service;

import java.util.List;

import com.fenlibao.p2p.model.entity.TFlowSendRecord;

public interface FlowSendRecordService {

	/**
	 * 获取指定渠道、已绑定银行卡的用户信息
	 * @param channelCode
	 * @return
	 */
	public List<TFlowSendRecord> getWaitFlowSendRecord(String channelCode);
	
	/**
	 * 添加已发送记录
	 * @param userId         用户ID
	 * @param activityType   活动ID
	 * @param flow           流量大小（单位：M） 没有值则传0
	 * @param sendStatus     发送状态
	 * @param remark         备注
	 */
	public void addFlowSendRecord(int userId,String activityType,int flow,int sendStatus,String requestNo,String remark);

	/** 
	 * @Title: updateSendFlow 
	 * @Description: TODO
	 * @param requestNo
	 * @param sendStatus
	 * @return: void
	 */
	void updateSendFlow(String requestNo, int sendStatus);

	/** 
	 * @Title: genRequestNo 
	 * @Description: 生成流量订单流水号
	 * @param telephone
	 * @return
	 * @return: String
	 */
	String genRequestNo(String telephone);

	/** 
	 * @Title: sendTeleFlow 
	 * @Description: TODO
	 * @param telephone
	 * @param requestNo
	 * @return
	 * @return: String
	 */
	String sendTeleFlow(String telephone, String requestNo);
}
