package com.fenlibao.p2p.service.bid.impl;

import com.dimeng.util.StringHelper;
import com.fenlibao.p2p.dao.UserInfoDao;
import com.fenlibao.p2p.dao.bid.BidExtendDao;
import com.fenlibao.p2p.model.entity.UserInfo;
import com.fenlibao.p2p.model.global.InterfaceConst;
import com.fenlibao.p2p.model.global.Status;
import com.fenlibao.p2p.model.vo.InvestRecordsVO;
import com.fenlibao.p2p.model.vo.RedPacketVO;
import com.fenlibao.p2p.service.bid.BidExtendService;
import com.fenlibao.p2p.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LouisWang on 2015/8/14.
 */
@Service
public class BidExtendServiceImpl implements BidExtendService {

    @Resource
    private BidExtendDao bidExtendDao;
    @Resource
    private UserInfoDao userInfoDao;

    @Override
    public BigDecimal getHqbEarning(String userId, Date earnDate) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("feeTypes", new int[]{InterfaceConst.TZ_LX, InterfaceConst.TZ_WYJ, InterfaceConst.TZ_FX});
        map.put("status", Status.YH.name());
        map.put("userType", InterfaceConst.USER_TYPE_ZRR);
        map.put("earnDate", earnDate);
        return bidExtendDao.getHqbUserEarnByDate(map);
    }

    /**
     * 申购记录
     * @param bid 标Id
     * @param timestamp 查询的时间戳
     * @return
     * @throws Exception
     */
    @Override
    public List<InvestRecordsVO> getBidInvestRecords(String bid,String timestamp,String isUp) throws Exception {
        List<InvestRecordsVO> investsList = null;
        if(!StringUtils.isEmpty(bid)){
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("bid",Integer.parseInt(bid.trim()));
            params.put("status",Status.F.name());
            params.put("isUp", isUp);
            if(!StringUtils.isBlank(timestamp)){
                Date time = DateUtil.timestampToDate(Long.valueOf(timestamp));
                params.put("time",time);
            }
            investsList =  bidExtendDao.getBidInvestRecords(params);

            if (investsList != null && investsList.size() > 0){
                for (InvestRecordsVO invest : investsList){
                    invest.longTime = invest.getTimestamp().getTime();
                    UserInfo user = getUserInfo(invest.getInvestId());
                    String fullName =user.getFullName();
                    invest.pactName = fullName.substring(0,1) + "**" ;
                   /* String sex = getSex(user.getIdCardEncrypt());
                    if("男".equals(sex)){
                        //+ fullName.substring(fullName.length() - 2, fullName.length())
                        invest.pactName = fullName.substring(0,1) + "***" + "&nbsp;&nbsp;&nbsp;先生" ; //投资人名称

                    }else if("女".equals(sex)){
                        invest.pactName = fullName.substring(0,1) + "***" + "&nbsp;&nbsp;&nbsp;女士" ; //投资人名称
                    }*/
                }
            }
        }
        return investsList;
    }

    @Override
    public Map<String, Object> getRedPackets(int userId, String timestamp, String isUp, String type, String status) throws Throwable {
        List<RedPacketVO> redPacketsList = null;
        if(userId > 0){
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("userId",userId);
            params.put("status",status);
            params.put("isUp", isUp);
            params.put("type",type);
            if(!StringUtils.isBlank(timestamp)){
                Date time = DateUtil.timestampToDate(Long.valueOf(timestamp));
                params.put("time",time);
            }
            redPacketsList = bidExtendDao.getRedPackets(params);
        }
        return null;
    }

    /**
     * 获取用户信息
     * @param userId 用户Id
     * @return UserInfo 用户信息
     */
    @Override
    public UserInfo getUserInfo(Integer userId) throws Exception {
        Map<String, Object> params =new HashMap<String, Object>();
        params.put("userId", userId);
        UserInfo user = userInfoDao.getUserInfo(params);
        return user;
    }

    private String getSex(String sfzh)
    {
        if(StringHelper.isEmpty(sfzh))
        {
            return null;
        }

        String sexSfzh = null;
        String sex = null;
        try {
            sexSfzh = StringHelper.decode(sfzh);
            if(Integer.parseInt(sexSfzh.substring(sexSfzh.length() - 2, sexSfzh.length() - 1)) % 2 != 0){
                sex = "男";
            }
            else{
                sex = "女";
            }
        } catch (Throwable throwable) {
            sex = null;
        }
        return sex;
    }
}
