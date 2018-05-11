package com.fenlibao.service.pms.idmt.group.impl;

import com.fenlibao.dao.pms.idmt.group.PmsGroupMapper;
import com.fenlibao.dao.pms.idmt.user.PmsUserMapper;
import com.fenlibao.model.pms.idmt.group.PmsGroup;
import com.fenlibao.model.pms.idmt.group.PmsGroupForm;
import com.fenlibao.model.pms.idmt.user.PmsUser;
import com.fenlibao.service.pms.idmt.group.PmsGroupService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Louis Wang on 2016/1/22.
 */

@Service
public class PmsGroupServiceImpl implements PmsGroupService {

    @Resource
    private PmsUserMapper pmsUserMapper;

    @Resource
    private PmsGroupMapper pmsGroupMapper;

    @Override
    public List<PmsGroup> findAll(Integer groupId, RowBounds bounds) {
        Map<String,Object> param = new HashMap();
        param.put("groupId",groupId);
        return pmsGroupMapper.findAll(param,bounds);
    }

    @Override
    public PmsGroup findNode(Integer nodeId) {
        return pmsGroupMapper.findNode(nodeId);
    }

    @Override
    @Transactional
    public void updatePmsGroup(PmsGroupForm pmsGroupForm) {
        pmsGroupMapper.updatePmsGroup(pmsGroupForm);
    }

    @Override
    @Transactional
    public void addPmsGroup(PmsGroupForm pmsGroupForm) {

        if(pmsGroupForm != null){
            PmsGroup parent = pmsGroupMapper.findNode(pmsGroupForm.getNodeId());
            if(parent != null){
                PmsGroup child = new PmsGroup();
                child.setParentId(pmsGroupForm.getNodeId());
                child.setParentIds(parent.makeSelfAsParentIds());
                child.setName(pmsGroupForm.getDeptName());
                child.setStatus("1");
                child.setOrderid(String.valueOf(pmsGroupForm.getDeptSort()));
                child.setMark(pmsGroupForm.getDeptMark());

                pmsGroupMapper.addPmsGroup(child);
            }
        }
    }

    @Override
    public List<PmsUser> findGroupUsers(Integer id, String dimission, RowBounds bounds) {
        Map<String,Object> param = new HashMap<>();
        PmsGroup pg = new PmsGroup();
        pg.setId(id);
        List<PmsGroup> groups = pmsUserMapper.getAllGroups(pg);
        groups.add(pg);
        param.put("list",groups);
        param.put("dimission",dimission);
        return pmsGroupMapper.findGroupUsers(param,bounds);
    }

    @Override
    @Transactional
    public void deletePmsGroup(Integer id) {
        PmsGroup pmsGroup = pmsGroupMapper.findNode(id);
        Map<String,Object> param = new HashMap<>();
        pmsGroupMapper.deletePmsGroup(id);
        String condition = pmsGroup.makeSelfAsParentIds();
        param.put("condition",condition);
        pmsGroupMapper.deletePmsGroupRelation(param);
    }

    @Override
    public PmsGroup getGroupByUserId(Integer id) {
        Map<String,Object> param = new HashMap<>();
        param.put("uid",id);
        return pmsGroupMapper.getGroupByUserId(param);
    }
}
