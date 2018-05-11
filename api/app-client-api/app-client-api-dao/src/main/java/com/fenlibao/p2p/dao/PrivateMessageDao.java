package com.fenlibao.p2p.dao;

import com.fenlibao.p2p.model.entity.PrivateMessage;

import java.util.List;
import java.util.Map;

public interface PrivateMessageDao {

	List<PrivateMessage> getMessageByUserId(Map<String, Object> map);

	/**
	 * 添加用户站内信
	 * @param map
	 * @return
	 */
	void addMessage(Map<String, Object> map);

	/**
	 * 添加用户站内信内容
	 * @param map
	 * @return
	 */
	int addMessageContent(Map<String, Object> map);
	
	/**
	 * 获取用户消息数量
	 * @param map
	 * @return
	 */
	int getUserMessageCount(Map<String, Object> map);
	
	/**
	 * 修改消息状态
	 * @param map
	 */
	void updateMessageStatus(Map<String, Object> map);
}
