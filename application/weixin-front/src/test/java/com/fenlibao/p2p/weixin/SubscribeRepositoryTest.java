package com.fenlibao.p2p.weixin;

import com.fenlibao.p2p.weixin.domain.Subscribe;
import com.fenlibao.p2p.weixin.repository.SubscribeRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;

/**
 * Created by Bogle on 2015/11/26.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(Application.class)
//@WebAppConfiguration
public class SubscribeRepositoryTest {

    @Autowired
    private SubscribeRepository subscribeRepository;

//    @Test
    public void testSubscribeRepository() {
        Subscribe subscribe = new Subscribe();
        subscribe.setOpendId("odEb6svImr1hPa62nlD_ROUBCfno");
        subscribe.setEventKey("eventkey");
        subscribe.setSubscribeTime(new Timestamp(System.currentTimeMillis()));
        subscribeRepository.save(subscribe);
    }

//    @Test
    public void testFindSubscribeRepository() {
        Subscribe subscribe = subscribeRepository.findByOpendId("oWMXht_7-xA_7MRVvDaKu1wm0DrM");
        System.out.println(subscribe);
    }
}
