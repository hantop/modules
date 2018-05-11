package com.fenlibao.p2p.service.activity;

import com.fenlibao.p2p.model.entity.activity.MoneyTreeRegisterCheckEntity;

/**
 * Created by xiao on 2017/4/27.
 */
public interface ActivityExtService {

    /**
     * 检查并生成分利果
     *
     * @param moneyTreeRegisterCheckEntity
     */
    void createFruitAndSendMsg(MoneyTreeRegisterCheckEntity moneyTreeRegisterCheckEntity);
}
