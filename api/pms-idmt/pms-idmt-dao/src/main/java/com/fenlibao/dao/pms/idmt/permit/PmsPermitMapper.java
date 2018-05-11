package com.fenlibao.dao.pms.idmt.permit;

import com.fenlibao.model.pms.idmt.permit.PermitTreeNode;
import com.fenlibao.model.pms.idmt.permit.vo.PermitVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PmsPermitMapper {
	/**
	 * 根据用户ID获取权限
	 *
	 * @param userId
	 * @return
     */
	List<PermitVO> getPermitsByUser(@Param("userId") Integer userId, @Param("type") String type);

	public List<PermitTreeNode> getPermitTree(Integer id);
	void insertNode(PermitTreeNode node);
	void updateNode(PermitTreeNode node);
	void deleteNode(Integer id);

	List<PermitTreeNode> getNodes(PermitTreeNode node);
}
