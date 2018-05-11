package com.fenlibao.p2p.service.user.impl;

import com.fenlibao.p2p.dao.user.AutoBidDao;
import com.fenlibao.p2p.model.consts.user.AutoBidConst;
import com.fenlibao.p2p.model.consts.user.BidTime;
import com.fenlibao.p2p.model.entity.user.UserAutobidSetting;
import com.fenlibao.p2p.model.vo.user.AutobidSettingDetailVO;
import com.fenlibao.p2p.model.vo.user.AutobidSettingVO;
import com.fenlibao.p2p.service.user.AutoBidService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 用户自动投标相关服务类
 * @author Mingway.Xu
 * @date 2016/11/14 10:20
 */
@Service
public class AutoBidServiceImpl implements AutoBidService {
    @Resource
    private AutoBidDao autoBidDao;
    /**
     * 获取年化率参数
     * @return
     */
    public List<BigDecimal> getInterestRates(){

        return AutoBidConst.getInterestRates();
    }

    /**
     * 获取投资期限参数
     * @return
     */
    public List<String> getBidDays(){
        List<String> bidTime = null;
        bidTime = AutoBidConst.getBidTime();

        return bidTime;
    }

    /**
     * 保存自动投标设置
     * @param autobidSetting
     */
    public int insertAutoBidSetting(UserAutobidSetting autobidSetting){
        return autoBidDao.insertAutoBidSetting(autobidSetting);
    }

    /**
     * 开启用户自动投标设置
     * @param userId
     * @param settingId
     * @return
     */
    public int updateAutobidSetting(String userId, String settingId){
        int uid = Integer.valueOf(userId);
        int sid = Integer.valueOf(settingId);
        int activeValue = this.selectStatusBySId(sid);
        int active = 0;
        if (activeValue == 1) {
            active = 0;
        } else {
            active = 1;
        }
        this.updateSettingByUserId(uid);
        return autoBidDao.updateAutobidSetting(uid, sid, active);
    }

    /**
     * 查询用户当前启用设置
     * @param uid
     * @return
     */
    public int selectUserAutobidSettingId(int uid){

        return autoBidDao.selectUserAutobidSettingId(uid);
    }

    /**
     * 关闭当前用户的自动投标设置
     * @param uid
     */
    public int updateSettingByUserId(int uid){

        return autoBidDao.updateSettingByUserId(uid);
    }

    /**
     * 获取用户设置列表
     * @param userId
     * @return
     */
    public List<AutobidSettingVO> getAutobidSettingList(String userId){
        int uid = Integer.valueOf(userId);
        return autoBidDao.getAutobidSettingList(uid);
    }

    /**
     * 获取用户设置详情
     * @param userId
     * @param settingId
     * @return
     */
    public AutobidSettingDetailVO getAutobidSettingDetail(String userId, String settingId){
        int uid = Integer.valueOf(userId);
        int sid = Integer.valueOf(settingId);

        return autoBidDao.getAutobidSettingDetail(uid,sid);
    }

    /**
     * 删除设置
     * @param userId
     * @param settingId
     * @return
     */
    public int deleteAutobidSetting(String userId, String settingId){
        int uid = Integer.valueOf(userId);
        int sid = Integer.valueOf(settingId);

        return autoBidDao.deleteAutobidSetting(uid,sid);
    }

    /**
     *获取当前设置是否启用
     * @param sid
     * @return
     */
    public int selectStatusBySId(int sid){
        return autoBidDao.selectStatusBySId(sid);
    }

    /**
     * 获取投标最小最大时间期限的单位
     * @return
     */
    public List<BidTime> getBidTime(){
        return autoBidDao.getBidTime();
    }

    /**
     * 启用自动投标设置
     * @param userAutobidSetting
     * @param sid
     * @return
     */
    public int updateAutobidSetting(UserAutobidSetting userAutobidSetting, Integer sid){

        return autoBidDao.updateAutobidSetting(userAutobidSetting,sid);
    }

    /**
     * 查询该用户是否还有其他自动投标设置
     * @param userId
     * @return
     */
    public boolean selectSettingNumByUserId(int userId,int deleteFlag){
        //如果表中有记录(>0)返回true(有记录)
        return 0 < autoBidDao.selectSettingNumByUserId(userId,deleteFlag)?true:false;
    }
    /**
     * 查询该用户是否有自动投标设置
     * @param userId
     * @return
     */
    public boolean selectSettingNumByUserId(int userId){
        //如果表中有记录(>0)返回true(有记录)
        return 0 <  autoBidDao.selectSettingNumByUserId(userId)?true:false;
    }

    /**
     * 检查传入的投标时间是否合法
     * @param timeMin
     * @param minMark
     * @param timeMax
     * @param maxMark
     * @return
     */
    public boolean checkBidTimeCorrect(String timeMin, String minMark, String timeMax, String maxMark){
        Map<String,Object> minMap = autoBidDao.getIdByNumAndMark(Integer.valueOf(timeMin), minMark);
        Map<String,Object> maxMap = autoBidDao.getIdByNumAndMark(Integer.valueOf(timeMax), maxMark);
        if(0 == Long.valueOf((Long) minMap.get("count")) || 0 == Long.valueOf((Long) maxMap.get("count"))){
            return false;
        }

        long min = (long) minMap.get("id");
        long max = (long) maxMap.get("id");

        if (1 == min || 1 == max) {
            return true;
        }

        return min <= max ? true:false;
    }

    /**
     * 检查用户自动投标的设置状态
     * @param userId
     * @return
     */
    public int checkUserAutoBid(int userId){
    //查询用户是否有设置过自动投标的设置
        boolean flag = this.selectSettingNumByUserId(userId);
        int status = 0;
        if (flag) {
            if (this.selectSettingNumByUserId(userId, AutoBidConst.NO_DELETE_FLAG)) {
                //设置了
                status = AutoBidConst.HAVE_AUTOSET_NO_DELETE;

            } else {
                if (this.selectSettingNumByUserId(userId, AutoBidConst.DELETE_FLAG)) {
                    //设置过已经删除
                    status = AutoBidConst.HAVE_AUTOSET_DELETE;
                }
            }

        }else {
            status = AutoBidConst.HAVE_NEVER_AUTOSET;
        }
        return status;
    }

}
