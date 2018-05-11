package com.fenlibao.service.pms.idmt.group;

import com.fenlibao.model.pms.idmt.group.PmsGroup;
import com.fenlibao.model.pms.idmt.group.PmsGroupForm;
import com.fenlibao.model.pms.idmt.user.PmsUser;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * Created by Louis Wang on 2016/1/22.
 */

public interface PmsGroupService {

    List<PmsGroup> findAll(Integer groupId, RowBounds bounds);

    PmsGroup findNode(Integer nodeId);

    void updatePmsGroup(PmsGroupForm pmsGroupForm);

    void addPmsGroup(PmsGroupForm pmsGroupForm);

    List<PmsUser> findGroupUsers(Integer id, String dimission, RowBounds bounds);

    void deletePmsGroup(Integer id);

    PmsGroup getGroupByUserId(Integer id);

}
