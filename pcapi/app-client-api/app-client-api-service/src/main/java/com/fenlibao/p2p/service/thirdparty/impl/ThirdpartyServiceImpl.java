package com.fenlibao.p2p.service.thirdparty.impl;

import com.fenlibao.p2p.dao.UserInfoDao;
import com.fenlibao.p2p.dao.UserThirdpartyDao;
import com.fenlibao.p2p.dao.bid.BidInfoDao;
import com.fenlibao.p2p.model.entity.bid.BidAgreement;
import com.fenlibao.p2p.model.vo.UserAccountInfoVO;
import com.fenlibao.p2p.model.vo.UserThirdpartyVO;
import com.fenlibao.p2p.service.thirdparty.ThirdpartyService;
import com.fenlibao.p2p.service.user.LoginStateService;
import com.fenlibao.p2p.util.api.ssq.ShangshangqianUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/** 
 * @ClassName: ThirdpartyServiceImpl 
 * @Description: TODO
 * @author: laubrence
 * @date: 2015-11-10 下午8:25:54  
 */
@Service
public class ThirdpartyServiceImpl implements ThirdpartyService {

    @Resource
    private UserThirdpartyDao userThirdpartyDao;

    @Resource
    private UserInfoDao userInfoDao;

    @Resource
    private LoginStateService loginStateService;

    @Resource
    private BidInfoDao bidInfoDao;

    @Override
    public UserThirdpartyVO bind(String openId, String userId, String username, int type) {
        Map map = new HashMap();
        map.put("userId", Integer.valueOf(userId));
        map.put("openId", openId);
        map.put("currentDate", new Date());
        map.put("username", username);
        map.put("type", type);
        map.put("bindStatus", "QY");

        int result = userThirdpartyDao.addUserThirdparty(map);
        if (result > 0) {
            UserThirdpartyVO vo = new UserThirdpartyVO();
            vo.setOpenId(openId);
            vo.setUserId(userId);
            return vo;
        }
        return null;
    }

    @Override
    public int isBindThirdparty(String openId, String userId, int type) {
        Map map = new HashMap();
        map.put("openId", openId);
        map.put("userId", userId);
        map.put("type", type);

        return userThirdpartyDao.isBindThirdparty(map);
    }

    @Override
    public int isBindThirdpartyOpenId(String openId, int type) {
        Map map = new HashMap();
        map.put("openId", openId);
        map.put("type", type);

        return userThirdpartyDao.isBindThirdpartyOpenId(map);
    }

    @Override
    public int isCanAutoLogin(String openId, int type) {
        Map map = new HashMap();
        map.put("openId", openId);
        map.put("type", type);
        return userThirdpartyDao.getUserByOpenId(map);
    }

    @Override
    public int cancelAutoLogin(String openId, int type) {
        Map map = new HashMap();
        map.put("openId", openId);
        map.put("type", type);

        return userThirdpartyDao.cancelAutoLogin(map);
    }

    @Override
    public UserAccountInfoVO getUserInfo(int userId, String clientType, String deviceId) {
        Map map = new HashMap();
        map.put("userId", userId);
        UserAccountInfoVO userAccountInfoVO = userInfoDao.getUserAccountInfo(map);
        if (userAccountInfoVO == null) {
            return null;
        }
        String token = loginStateService.saveLoginState(clientType, userAccountInfoVO);
        userAccountInfoVO.setToken(token);
        return userAccountInfoVO;
    }

    @Override
    public String getFileUrl(Integer bidId) throws Exception{
        BidAgreement bidAgreement = bidInfoDao.getBidAgreement(bidId);
        if(bidAgreement!=null&& !StringUtils.isEmpty(bidAgreement.getNoSensitiveDocId())){
            return ShangshangqianUtil.sdk.ViewContract(bidAgreement.getNoSensitiveSignId(),bidAgreement.getNoSensitiveDocId());
        }else {
            return null;
        }
    }
}
