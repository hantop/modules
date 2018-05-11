package com.fenlibao.dao.pms.idmt.group;

import com.fenlibao.model.pms.idmt.group.PmsGroup;
import com.fenlibao.model.pms.idmt.group.PmsGroupForm;
import com.fenlibao.model.pms.idmt.group.PmsUserGroup;
import com.fenlibao.model.pms.idmt.user.PmsUser;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Created by Louis Wang on 2016/1/22.
 */

public interface PmsGroupMapper {

    List<PmsGroup> findAll(Map<String, Object> param, RowBounds bounds);

    PmsGroup findNode(Integer id);

    void updatePmsGroup(PmsGroupForm pmsGroupForm);

    void addPmsGroup(PmsGroup pmsGroup);

    List<PmsUser> findGroupUsers(Map<String, Object> condition, RowBounds bounds);

    void deletePmsGroup(Integer id);

    void deletePmsGroupRelation(Map<String, Object> condition);

    /**
     * 根据用户名获取用户部门信息
     *
     * @param username
     * @return
     */
    PmsGroup findGroupByUsername(String username);

    /**
     * 更新部门信息
     *
     * @param userGroup
     * @return
     */
    int addUserDepartment(PmsUserGroup userGroup);

    /**
     * 删除用户部门信息
     * @param userId
     * @return
     */
    int deleteUserDepartment(int userId);

    /**
     * 根据用户id 获取所在部门
     * @param param
     * @return
     */
    PmsGroup getGroupByUserId(Map<String, Object> param);
}
