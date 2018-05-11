package com.fenlibao.p2p.service.user;

import com.fenlibao.p2p.model.consts.user.BidTime;
import com.fenlibao.p2p.model.entity.user.UserAutobidSetting;
import com.fenlibao.p2p.model.vo.user.AutobidSettingDetailVO;
import com.fenlibao.p2p.model.vo.user.AutobidSettingVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Mingway.Xu
 * @date 2016/11/14 10:19
 */
public interface AutoBidService {
    /**
     * 获取年化率参数
     * @return
     */
    List<BigDecimal> getInterestRates();

    /**
     * 获取投资期限参数
     * @return
     */
    List<String> getBidDays();

    /**
     * 保存自动投标设置
     * @param autobidSetting
     */
    int insertAutoBidSetting(UserAutobidSetting autobidSetting);

    /**
     * 开启用户自动投标设置
     * @param userId
     * @param settingId
     * @return
     */
    int updateAutobidSetting(String userId, String settingId);

    /**
     * 查询用户当前启用设置
     * @param uid
     * @return
     */
    int selectUserAutobidSettingId(int uid);

    /**
     * 关闭当前用户的自动投标设置
     * @param userId
     */
    int updateSettingByUserId(int userId);

    /**
     * 获取用户设置列表
     * @param userId
     * @return
     */
    List<AutobidSettingVO> getAutobidSettingList(String userId);

    /**
     * 获取用户设置详情
     * @param userId
     * @param settingId
     * @return
     */
    AutobidSettingDetailVO getAutobidSettingDetail(String userId, String settingId);

    /**
     * 删除设置
     * @param userId
     * @param settingId
     * @return
     */
    int deleteAutobidSetting(String userId, String settingId);

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
     *启用自动投标设置
     * @param userAutobidSetting
     * @param integer
     * @return
     */
    int updateAutobidSetting(UserAutobidSetting userAutobidSetting, Integer integer);

    /**
     * 查询该用户是否还有其他自动投标设置
     * @param userId
     * @return
     */
    boolean selectSettingNumByUserId(int userId,int deleteFlag);

    /**
     * 检查传入的投标时间是否合法
     * @param timeMin
     * @param minMark
     * @param timeMax
     * @param minMark1
     * @return
     */
    boolean checkBidTimeCorrect(String timeMin, String minMark, String timeMax, String minMark1);

    /**
     * 查询该用户是否还有其他自动投标设置
     * @param userId
     * @return
     */
    boolean selectSettingNumByUserId(int userId);

    /**
     * 检查用户自动投标的设置状态
     * @param userId
     * @return
     */
    int checkUserAutoBid(int userId);
}
