package com.fenlibao.p2p.service.thirdparty;

import com.fenlibao.p2p.model.vo.UserAccountInfoVO;
import com.fenlibao.p2p.model.vo.UserThirdpartyVO;

/**
 * 微信Service
 *
 * @author chenzhixuan
 */
public interface ThirdpartyService {

    /**
     * 第三方绑定接口
     * @param openId
     * @param userId
     */
	UserThirdpartyVO bind(String openId, String userId, String username, int type);
	
    public int isBindThirdparty(String openId, String userId, int type);
	
	UserAccountInfoVO getUserInfo(int userId, String clientType, String deviceId);

	/** 
	 * @Title: isCanAutoLogin 
	 * @Description: TODO
	 * @param openId
	 * @param type
	 * @return
	 * @return: int
	 */
	int isCanAutoLogin(String openId, int type);

	/** 
	 * @Title: cancelAutoLogin 
	 * @Description: TODO
	 * @param openId
	 * @param type
	 * @return
	 * @return: int
	 */
	int cancelAutoLogin(String openId, int type);

	/** 
	 * @Title: isBindThirdpartyOpenId 
	 * @Description: TODO
	 * @param openId
	 * @param type
	 * @return
	 * @return: int
	 */
	int isBindThirdpartyOpenId(String openId, int type);

	/**
	 * 获取放款后上上签文件地址
	 * @param bidId
	 * @return
     */
	String getFileUrl(Integer bidId) throws Exception;
}
