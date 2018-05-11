package com.fenlibao.dao.pms.common.message.privatemessage;


import com.fenlibao.model.pms.common.message.privatemessage.PrivateMessage;

import java.util.List;
import java.util.Map;

public interface PrivateMessageMapper {

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
