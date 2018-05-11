package com.fenlibao.dao.pms.weixin;

import com.fenlibao.model.pms.weixin.RobotMsg;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by Bogle on 2016/3/4.
 */
public interface RobotMsgMapper {

    RobotMsg selectByPrimaryKey(Integer id);

    List<RobotMsg> selectContentMsgByType(RobotMsg robotMsg, RowBounds bounds);

    RobotMsg selectByKey(String key, String env);

    int updateByKey(String key, String content, String env);

    int insertSelective(RobotMsg robotMsg);

    int updateByPrimaryKeySelective(RobotMsg robotMsg);

    int deleteByPrimaryKeys(List<Integer> msgs);
}
