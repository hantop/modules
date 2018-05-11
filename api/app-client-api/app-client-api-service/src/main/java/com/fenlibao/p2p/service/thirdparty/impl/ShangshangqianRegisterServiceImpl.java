package com.fenlibao.p2p.service.thirdparty.impl;

import cn.bestsign.sdk.integration.Constants;
import com.alibaba.fastjson.JSONObject;
import com.fenlibao.p2p.dao.user.ThirdPartyUserDao;
import com.fenlibao.p2p.util.api.ssq.ShangshangqianUtil;
import com.fenlibao.p2p.util.bean.ShangshangqianRegisterUser;
import com.fenlibao.p2p.service.thirdparty.AbstractForThreadPoolExecutor;
import com.fenlibao.p2p.service.thirdparty.ShangshangqianRegisterService;
import com.fenlibao.thirdparty.shangshangqian.vo.RegisterUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/14.
 */
@Service
public class ShangshangqianRegisterServiceImpl extends AbstractForThreadPoolExecutor implements ShangshangqianRegisterService{
    private static final Logger logger = LogManager.getLogger(ShangshangqianRegisterServiceImpl.class);

    @Resource
    private ThirdPartyUserDao thirdPartyUserDao;

    @Transactional
    @Override
    public Object doTracsation(Object o) throws Exception{
        Integer code = -1;
        try {
            ShangshangqianRegisterUser ssqru = (ShangshangqianRegisterUser)o;
            logger.info("注册上上签请求参数:" + ssqru);
            JSONObject result = ShangshangqianUtil.regUser(ssqru.getUserType() == 1 ? Constants.USER_TYPE.PERSONAL : Constants.USER_TYPE.ENTERPRISE, ssqru.getEmail(), ssqru.getMobile(), ssqru.getName());
            logger.info("注册上上签返回结果:" + result);
            result =result.getJSONObject("response");
            JSONObject content =result.getJSONObject("content");
            String pfUid = (String) content.get("uid");
            JSONObject info = result.getJSONObject("info");
            if ("100000".equals((String)info.get("uid"))){
                Map map = new HashMap();
                map.put("userId", ssqru.getUserId());
                map.put("phone", ssqru.getMobile());
                map.put("email", ssqru.getEmail());
                map.put("platform", 1);
                map.put("platformUserId", pfUid);
                thirdPartyUserDao.addSSQRegUser(map);
                code = 0;
            }
        }catch (Exception e){
            logger.info("注册上上签发生异常:" + o);
            e.printStackTrace();
        }
        return code;
    }

    @Transactional
    @Override
    public JSONObject regUser(RegisterUser ru) throws Exception {
        return null;
    }

    @Override
    public boolean isRegisterThirdParty(String email, int platform) {
        boolean flag = false;
        Integer ssqCount = thirdPartyUserDao.countRegisterThirdParty(email, platform);
        if(ssqCount != null && ssqCount > 0){
            flag = true;
        }
        return flag;
    }

    @Override
    public List<Integer> getInvestorUserIdList(int bidId) {
        return thirdPartyUserDao.getInvestorUserIdList(bidId);
    }
}
