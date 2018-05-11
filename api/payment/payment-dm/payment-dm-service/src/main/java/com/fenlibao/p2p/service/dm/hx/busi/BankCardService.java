package com.fenlibao.p2p.service.dm.hx.busi;

import com.fenlibao.p2p.model.dm.vo.HXAccountInfo;
import com.fenlibao.p2p.service.dm.hx.HXOrderProcess;

/**
 * Created by zcai on 2016/10/25.
 */
public interface BankCardService extends HXOrderProcess {

    /**
     * 绑卡
     * @return 封装好的华兴报文
     * @throws Exception
     */
    String bindCard(HXAccountInfo accountInfo, int clientType, String uri) throws Exception;

}
