package com.fenlibao.p2p.service.thirdparty.impl;

import com.fenlibao.p2p.dao.entrust.EntrustDao;
import com.fenlibao.p2p.model.entity.user.UnRegUser;
import com.fenlibao.p2p.model.enums.bid.AgreementSignStatusEnum;
import com.fenlibao.p2p.model.vo.entrust.EntrustAgreementVo;
import com.fenlibao.p2p.model.vo.entrust.EntrustUserInfoVo;
import com.fenlibao.p2p.service.thirdparty.EntrustAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zeronx on 2017/7/31.
 *
 */
@Service
public class EntrustAccountServiceImpl implements EntrustAccountService {

    private static final Logger logger = LogManager.getLogger(EntrustAccountServiceImpl.class);

    @Resource
    private EntrustDao entruetDao;

    @Override
    public List<EntrustAgreementVo> getUnSignAgreementList() {
        return entruetDao.getUnSignAgreementList();
    }

    @Override
    public UnRegUser getUnRegUser(int userId) {
        return entruetDao.getUnRegUser(userId);
    }

    @Transactional
    @Override
    public EntrustAgreementVo lockAgreement(int id) {
        Map map = new HashMap();
        map.put("id", id);
        EntrustAgreementVo agreementVo = entruetDao.lockAgreement(map);
        if(agreementVo != null) {
            map.put("signStatus", AgreementSignStatusEnum.QMZ);
            int re = entruetDao.updateUnSignAgreementById(map);
            if (re != 1) {
                return null;
            }
        }
        return agreementVo;
    }

    @Override
    public EntrustUserInfoVo getEntruetUserInfoByUserId(int userId) {
        return entruetDao.getEntruetUserInfoByUserId(userId);
    }

    @Override
    public int updateUnSignAgreementById(int id, AgreementSignStatusEnum statusEnum) {
        Map map = new HashMap();
        map.put("id", id);
        map.put("signStatus", statusEnum);
        return entruetDao.updateUnSignAgreementById(map);
    }

    @Override
    public List<EntrustAgreementVo> getSignFailAgreementList() {
        return entruetDao.getSignFailAgreementList();
    }

    @Transactional
    @Override
    public EntrustAgreementVo lockSignFailAgreement(int id) {
        Map map = new HashMap();
        map.put("id", id);
        EntrustAgreementVo agreementVo = entruetDao.lockSignFailAgreement(map);
        if(agreementVo != null) {
            map.put("signStatus", AgreementSignStatusEnum.QMZ);
            int re = entruetDao.updateUnSignAgreementById(map);
            if (re != 1) {
                return null;
            }
        }
        return agreementVo;
    }
}
