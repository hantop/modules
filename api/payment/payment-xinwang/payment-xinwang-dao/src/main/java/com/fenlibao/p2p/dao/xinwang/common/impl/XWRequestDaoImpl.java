package com.fenlibao.p2p.dao.xinwang.common.impl;

import com.fenlibao.p2p.dao.xinwang.common.XWRequestDao;
import com.fenlibao.p2p.model.xinwang.entity.common.XWRequest;
import com.fenlibao.p2p.model.xinwang.entity.common.XWResponseMessage;
import com.fenlibao.p2p.model.xinwang.enums.XinwangInterfaceName;
import com.fenlibao.p2p.model.xinwang.enums.order.XWOrderStatus;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/12.
 */
@Repository
public class XWRequestDaoImpl implements XWRequestDao {

    @Resource
    private SqlSession sqlSession;
    private final String MAPPER = "XWRequestMapper.";

    @Override
    public void createRequest(XWRequest request){
        sqlSession.insert(MAPPER + "createRequest",request);
    }

    @Override
    public void saveRequestMessage(XWResponseMessage message){
        sqlSession.insert(MAPPER + "saveRequestMessage",message);
    }

    @Override
    public void saveResponseMessage(XWResponseMessage responseMessage){
        sqlSession.update(MAPPER + "saveResponseMessage",responseMessage);
    }

    @Override
    public void updateRequest(XWRequest request){
        sqlSession.update(MAPPER + "updateRequest",request);
    }

    @Override
    public XWRequest getByRequestNo(String requestNo) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("requestNo", requestNo);
        return sqlSession.selectOne(MAPPER + "getByRequestNo", params);
    }

    @Override
    public List<String> getOrderNeedComfired(XinwangInterfaceName interfaceName, Date requestTime) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("interfaceName", interfaceName);
        params.put("requestTime", requestTime);
        return sqlSession.selectList(MAPPER + "getOrderNeedComfired", params);
    }

    @Override
    public List<XWRequest> getUnbindOrder(XinwangInterfaceName interfaceName, XWOrderStatus orderStatus, Date requestTime) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("interfaceName", interfaceName);
        params.put("orderStatus", orderStatus);
        params.put("requestTime", requestTime);
        return sqlSession.selectList(MAPPER + "getUnbindOrder", params);
    }

    @Override
    public int updateRequestStatus(Map<String,Object> params) {
        return sqlSession.update(MAPPER + "updateRequestStatus",params);
    }
}
