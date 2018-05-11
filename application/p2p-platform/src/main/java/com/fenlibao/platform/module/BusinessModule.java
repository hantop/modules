package com.fenlibao.platform.module;


import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.dao.xinwang.common.impl.XWRequestDaoImpl;
import com.fenlibao.platform.service.CommonService;
import com.fenlibao.platform.service.RedisService;
import com.fenlibao.platform.service.TestService;
import com.fenlibao.platform.service.impl.CommonServiceImpl;
import com.fenlibao.platform.service.impl.RedisServiceImpl;
import com.fenlibao.platform.service.impl.TestServiceImpl;
import com.fenlibao.platform.service.integral.IntegralService;
import com.fenlibao.platform.service.integral.impl.IntegralServiceImpl;
import com.fenlibao.platform.service.member.MemberService;
import com.fenlibao.platform.service.member.MessageService;
import com.fenlibao.platform.service.member.impl.MemberServiceImpl;
import com.fenlibao.platform.service.member.impl.MessageServiceImpl;
import com.fenlibao.platform.service.queqianme.QueqianmeService;
import com.fenlibao.platform.service.queqianme.impl.QueqianmeServiceImpl;
import com.fenlibao.platform.service.thirdparty.BidInfoService;
import com.fenlibao.platform.service.thirdparty.TPUserService;
import com.fenlibao.platform.service.thirdparty.XWEntrustImportUserService;
import com.fenlibao.platform.service.thirdparty.impl.BidInfoServiceImpl;
import com.fenlibao.platform.service.thirdparty.impl.TPUserServiceImpl;
import com.fenlibao.platform.service.thirdparty.impl.XWEntrustImportUserServiceImpl;
import com.google.inject.AbstractModule;

/**
 * Created by Lullaby on 2016/2/25.
 */
public class BusinessModule extends AbstractModule {

    protected void configure() {
        bind(TestService.class).to(TestServiceImpl.class);
        bind(CommonService.class).to(CommonServiceImpl.class);
        bind(RedisService.class).to(RedisServiceImpl.class);
        bind(MemberService.class).to(MemberServiceImpl.class);
        bind(MessageService.class).to(MessageServiceImpl.class);
        bind(IntegralService.class).to(IntegralServiceImpl.class);
        bind(TPUserService.class).to(TPUserServiceImpl.class);
        bind(BidInfoService.class).to(BidInfoServiceImpl.class);
        bind(QueqianmeService.class).to(QueqianmeServiceImpl.class);
        bind(XWEntrustImportUserService.class).to(XWEntrustImportUserServiceImpl.class);
        bind(XWRequestDao.class).to(XWRequestDaoImpl.class);
    }

}
