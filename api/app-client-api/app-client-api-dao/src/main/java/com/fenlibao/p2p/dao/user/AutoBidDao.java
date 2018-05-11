package com.fenlibao.p2p.dao.user;

import com.fenlibao.p2p.model.consts.user.BidTime;
import com.fenlibao.p2p.model.entity.user.UserAutobidSetting;
import com.fenlibao.p2p.model.vo.user.AutobidSettingDetailVO;
import com.fenlibao.p2p.model.vo.user.AutobidSettingVO;

import java.util.List;

import java.sql.Timestamp;
import java.util.Map;

/**
 *  用户自动投设置
 * @author Mingway.Xu
 * @date 2016/11/14 14:51
 */
public interface AutoBidDao {
    /**
     * 保存自动投标设置
     * @param autobidSetting
     */
    int insertAutoBidSetting(UserAutobidSetting autobidSetting);

    /**
     *查询用户当前启用设置
     * @param uid
     * @return
     */
    int selectUserAutobidSettingId(int uid);

    /**
     * 启用自动投标设置
     * @param uid
     * @param sid
     * @param active
     * @return
     */
    int updateAutobidSetting(int uid, int sid, int active);

    /**
     *关闭当前用户的自动投标设置
     * @param uid
     * @return
     */
    int updateSettingByUserId(int uid);

    /**
     * 获取用户设置列表
     * @param uid
     * @return
     */
    List<AutobidSettingVO> getAutobidSettingList(int uid);

    /**
     * 获取用户设置详情
     * @param uid
     * @param sid
     * @return
     */
    AutobidSettingDetailVO getAutobidSettingDetail(int uid, int sid);

    /**
     * 删除设置
     * @param uid
     * @param sid
     * @return
     */
    int deleteAutobidSetting(int uid, int sid);
    /**
     * 获取第一个合理的自动投标规则
     *
     * @return
     */
    UserAutobidSetting getFirstRationalRole(Timestamp dbTime);

    /**
     * 更新规则排序，将指定规则置于队尾
     */
    int updateRoleRank(Map map);

    /**
     * 更新配标成功时间
     */
    int updateRoleLastBidTime(Map map);

    /**
     * 获取数据库当前时间
     */
    Timestamp getDBCurrentTime();

    /**
     * 获取当前规则匹配等待时长
     */
    int getLastBidDateDiff(int id);




    /**
     *获取当前设置是否启用
     * @param sid
     * @return
     */
    int selectStatusBySId(int sid);

    /**
     * 获取投标最小最大时间期限的单位
     * @return
     */
    List<BidTime> getBidTime();

    /**
     * 启用自动投标设置
     * @param userAutobidSetting
     * @param sid
     * @return
     */
    int updateAutobidSetting(UserAutobidSetting userAutobidSetting, Integer sid);

    /**
     *查询用户自动投标设置数
     * @param userId
     * @return
     */
    int selectSettingNumByUserId(int userId,int deleteFlag);

    /**
     * 根据投标数字和单位查询投标期限的主键
     * @param integer
     * @param minMark
     * @return
     */
    Map<String,Object> getIdByNumAndMark(Integer integer, String minMark);

    /**
     *查询用户自动投标设置数
     * @param userId
     * @return
     */
    int selectSettingNumByUserId(int userId);

    /**
     * 获取用户正在执行的设置
     * @param userId
     * @return
     */
    int getActiveSet(int userId);
}
