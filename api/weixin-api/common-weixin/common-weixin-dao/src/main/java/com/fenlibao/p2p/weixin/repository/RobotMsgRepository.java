package com.fenlibao.p2p.weixin.repository;

import com.fenlibao.p2p.weixin.domain.RobotMsg;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 机器人自动回复消息
 */
public interface RobotMsgRepository extends CrudRepository<RobotMsg, Long> {

    /**
     * 返回所有的回复内容信息
     * @return
     * @param activeProfiles
     */
    @Query(value = "select * from weixin_robot_msg where active_profiles = :activeProfiles and type=:type", nativeQuery = true)
    @Cacheable(value = "wechat:keyword:text", keyGenerator = "keyGenerator")
    List<RobotMsg> findActiveProfilesAndType(@Param("activeProfiles") String activeProfiles,@Param("type") String type);
}