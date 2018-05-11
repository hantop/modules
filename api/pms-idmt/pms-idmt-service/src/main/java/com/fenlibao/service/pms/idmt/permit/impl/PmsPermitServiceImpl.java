package com.fenlibao.service.pms.idmt.permit.impl;

import com.fenlibao.dao.pms.idmt.permit.PmsPermitMapper;
import com.fenlibao.model.pms.idmt.permit.PermitTreeNode;
import com.fenlibao.model.pms.idmt.permit.vo.PermitVO;
import com.fenlibao.service.pms.idmt.permit.PmsPermitService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PmsPermitServiceImpl implements PmsPermitService{

    @Resource
    private PmsPermitMapper pmsPermitMapper;

	@Override
	public List<PermitVO> getPermitsByUser(Integer userId, String type) {
		return pmsPermitMapper.getPermitsByUser(userId, type);
	}

	@Override
	public List<PermitTreeNode> getPermitTree(Integer id) {
		return pmsPermitMapper.getPermitTree(id);
	}

	@Override
	public void saveNode(PermitTreeNode node) {
		if(node.getId()==null){
			pmsPermitMapper.insertNode(node);
		}
		else{
			pmsPermitMapper.updateNode(node);
		}
	}

	@Override
	public void deleteNode(Integer id) {
		pmsPermitMapper.deleteNode(id);
	}

	@Override
	public List<PermitTreeNode> getNodes(PermitTreeNode node) {
		return pmsPermitMapper.getNodes(node);
	}
    
}
