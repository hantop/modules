package com.fenlibao.p2p.dao.creditassignment;

import java.util.List;
import java.util.Map;

import com.fenlibao.p2p.model.entity.creditassignment.Zqzrlb;

public interface CreditAssigmentDao {

	/**
	 * 债权转让信息
	 * @param map
	 * @return
	 */
    public List<Zqzrlb> getCreditassignmentApplyforList(Map<String,Object> map);
    
    /**
     * 债权转让数量
     * @param map
     * @return
     */
    public int getCreditassignmentApplyforCount(Map<String,Object> map);
    
    /**
     * 债权转让申请次数
     * @param map
     * @return
     */
    public int getCreditassignmentCount(Map<String,Object> map);
    
    /**
     * 获取债权转让成功记录
     * @param map
     * @return
     */
    public int getRecord(Map<String,Object> map);

}
