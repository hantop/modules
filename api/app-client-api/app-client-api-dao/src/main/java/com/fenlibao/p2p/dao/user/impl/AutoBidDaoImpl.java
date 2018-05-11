package com.fenlibao.p2p.dao.user.impl;

import com.fenlibao.p2p.dao.user.AutoBidDao;
import com.fenlibao.p2p.model.consts.user.AutoBidConst;
import com.fenlibao.p2p.model.consts.user.BidTime;
import com.fenlibao.p2p.model.entity.user.UserAutobidSetting;
import com.fenlibao.p2p.model.vo.user.AutobidSettingDetailVO;
import com.fenlibao.p2p.model.vo.user.AutobidSettingVO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户自动投设置
 * @author Mingway.Xu
 * @date 2016/11/14 14:52
 */
@Repository
public class AutoBidDaoImpl implements AutoBidDao {
    @Resource
    private SqlSession sqlSession;

    private static final String MAPPER = "UserAutoBidMapper.";

    /**
     * 保存自动投标设置
     * @param autobidSetting
     */
    public int insertAutoBidSetting(UserAutobidSetting autobidSetting){
        Map<String,Object> map = new HashMap<String,Object>();

        map.put("userId", autobidSetting.getUserId());
        map.put("interestRate", autobidSetting.getInterestRate());
        map.put("timeMin", autobidSetting.getTimeMin());
        map.put("minMark", autobidSetting.getMinMark());
        map.put("maxMark", autobidSetting.getMaxMark());
        map.put("timeMax", autobidSetting.getTimeMax());
        map.put("bidType", autobidSetting.getBidType());
        map.put("repaymentMode", autobidSetting.getRepaymentMode());
        map.put("reserve", autobidSetting.getReserve());
        map.put("validityMod", autobidSetting.getValidityMod());
        map.put("startTime", autobidSetting.getStartTime());
        if (null != autobidSetting.getEndTime()) {
            map.put("endTime", autobidSetting.getEndTime());
        }


        return sqlSession.insert(MAPPER+"insertAutoBidSetting", map);
    }

    /**
     *查询用户当前启用设置
     * @param uid
     * @return
     */
    public int selectUserAutobidSettingId(int uid){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userId", uid);
        map.put("active", AutoBidConst.AUTO_BID_ACTIVE);

        return sqlSession.selectOne(MAPPER+"selectUserAutobidSettingId", map);
    }

    /**
     * 启用自动投标设置
     * @param uid
     * @param sid
     * @param active
     * @return
     */
    public int updateAutobidSetting(int uid, int sid, int active){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userId", uid);
        map.put("id", sid);
        map.put("active", active);

        return sqlSession.update(MAPPER+"activeAutobidSetting", map);
    }

    /**
     *关闭当前用户的自动投标设置
     * @param uid
     * @return
     */
    public int updateSettingByUserId(int uid){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userId", uid);

        return sqlSession.update(MAPPER+"updateSettingByUserId", map);
    }


    /**
     * 获取用户设置列表
     * @param uid
     * @return
     */
    public List<AutobidSettingVO> getAutobidSettingList(int uid){
        return sqlSession.selectList(MAPPER+"getAutobidSettingList",uid);
    }

    /**
     * 获取用户设置详情
     * @param uid
     * @param sid
     * @return
     */
    public AutobidSettingDetailVO getAutobidSettingDetail(int uid, int sid){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userId", uid);
        map.put("id", sid);

        return sqlSession.selectOne(MAPPER+"getAutobidSettingDetail",map);
    }

    /**
     * 删除设置 逻辑删除
     * @param uid
     * @param sid
     * @return
     */
    public int deleteAutobidSetting(int uid, int sid){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userId", uid);
        map.put("id", sid);

        return sqlSession.update(MAPPER+"deleteAutobidSetting",map);
    }

    /**
     * 获取第一个合理的自动投标规则
     *
     * @return
     */
    @Override
    public UserAutobidSetting getFirstRationalRole(Timestamp dbTime) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("dbTime",dbTime);
        return sqlSession.selectOne(MAPPER + "getFirstRationalRole", map);
    }

    /**
     * 更新规则排序，将指定规则置于队尾
     */
    @Override
    public int updateRoleRank(Map map) {
        return this.sqlSession.update(MAPPER + "updateRoleRank", map);
    }

    /**
     * 更新配标成功时间
     */
    @Override
    public int updateRoleLastBidTime(Map map) {
        return this.sqlSession.update(MAPPER + "updateRoleLastBidTime", map);
    }

    /**
     * 获取数据库当前时间
     */
    @Override
    public Timestamp getDBCurrentTime() {
        return this.sqlSession.selectOne(MAPPER + "getDBCurrentTime");
    }

    /**
     * 获取当前规则匹配等待时长
     */
    @Override
    public int getLastBidDateDiff(int id) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("id",id);
        return this.sqlSession.selectOne(MAPPER + "getLastBidDateDiff", map);
    }


    /**
     *获取当前设置是否启用
     * @param sid
     * @return
     */
    public int selectStatusBySId(int sid){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("id", sid);
        return this.sqlSession.selectOne(MAPPER + "selectStatusBySId",map);
    }

    public List<BidTime> getBidTime(){
        return this.sqlSession.selectList(MAPPER + "getBidTime");
    }

    public int updateAutobidSetting(UserAutobidSetting autobidSetting, Integer sid) {
        Map<String,Object> map = new HashMap<String,Object>();

        map.put("userId", autobidSetting.getUserId());
        map.put("interestRate", autobidSetting.getInterestRate());
        map.put("timeMin", autobidSetting.getTimeMin());
        map.put("minMark", autobidSetting.getMinMark());
        map.put("maxMark", autobidSetting.getMaxMark());
        map.put("timeMax", autobidSetting.getTimeMax());
        map.put("bidType", autobidSetting.getBidType());
        map.put("repaymentMode", autobidSetting.getRepaymentMode());
        map.put("reserve", autobidSetting.getReserve());
        map.put("validityMod", autobidSetting.getValidityMod());
        map.put("startTime", autobidSetting.getStartTime());
        if (null != autobidSetting.getEndTime()) {
            map.put("endTime", autobidSetting.getEndTime());
        }

        map.put("id", sid);

        return sqlSession.update(MAPPER+"updateAutobidSettingById", map);
    }

    /**
     *查询用户自动投标设置数量
     * @param userId
     * @return
     */
    public int selectSettingNumByUserId(int userId,int deleteFlag){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userId", userId);
        map.put("deleteFlag", deleteFlag);
        int num = sqlSession.selectOne(MAPPER+"selectSettingNumByUserId", map);
        return num;
    }

    /**
     * 根据投标数字和单位查询投标期限的主键
     * @param num
     * @param mark
     * @return
     */
    public Map<String,Object> getIdByNumAndMark(Integer num, String mark){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("num", num);
        map.put("mark", mark);
        return sqlSession.selectOne(MAPPER+"getIdByNumAndMark", map);
    }

    /**
     *查询用户自动投标设置数
     * @param userId
     * @return
     */
    public int selectSettingNumByUserId(int userId){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userId", userId);
        int num = sqlSession.selectOne(MAPPER+"selectSettingNumByUserId", map);
        return num;
    }

    /**
     * 获取用户的激活设置数量
     * @param userId
     * @return
     */
    @Override
    public int getActiveSet(int userId) {
        return sqlSession.selectOne(MAPPER+"getActiveSet", userId);
    }
}
