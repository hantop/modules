package com.fenlibao.p2p.service.xinwang.common.impl;

import com.fenlibao.p2p.dao.xinwang.common.PTCommonDao;
import com.fenlibao.p2p.model.xinwang.enums.common.PTLetterState;
import com.fenlibao.p2p.service.xinwang.common.PTCommonService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/5/22.
 */
@Service
public class PTCommonServiceImpl implements PTCommonService{

    @Resource
    PTCommonDao ptCommonDao;

    @Override
    public void sendLetter(int userId, String title, String content) throws Exception {
        long letterId = ptCommonDao.insertT6123(userId, title, PTLetterState.WD.name());
        if(letterId>0){
            ptCommonDao.insertT6124(letterId, content);
        }
    }

    @Override
    public void sendMsg(String mobile, String content, int type) throws Exception {
        Integer msgId=ptCommonDao.insertT1040(type, content);
        if(msgId!=null){
            if(msgId>0){
                ptCommonDao.insertT1041(msgId, mobile);
            }
        }
    }
}
