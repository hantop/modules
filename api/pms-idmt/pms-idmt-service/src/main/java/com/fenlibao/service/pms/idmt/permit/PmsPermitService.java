package com.fenlibao.service.pms.idmt.permit;

import com.fenlibao.model.pms.idmt.permit.PermitTreeNode;
import com.fenlibao.model.pms.idmt.permit.vo.PermitVO;

import java.util.List;

public interface PmsPermitService {
    /**
     * 根据用户ID获取权限
     *
     * @param userId
     * @param type
     * @return
     */
    List<PermitVO> getPermitsByUser(Integer userId, String type);

    List<PermitTreeNode> getPermitTree(Integer id);
    void saveNode(PermitTreeNode node);
    void deleteNode(Integer id);
    List<PermitTreeNode> getNodes(PermitTreeNode node);
}
