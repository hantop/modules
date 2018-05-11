/**   
 * Copyright © 2016 fenlibao.com. All rights reserved.
 * 
 * @Title: PointsType.java 
 * @Prject: mp-model
 * @Package: com.fenlibao.p2p.model.mp.entity 
 * @Description: TODO
 * @author: laubrence  
 * @date: 2016-2-19 下午3:25:59 
 * @version: V1.1   
 */
package com.fenlibao.p2p.model.mp.entity;

/** 
 * @ClassName: PointsType 
 * @Description: TODO
 * @author: laubrence
 * @date: 2016-2-19 下午3:25:59  
 */
public class PointsType {
	
	public int id; //积分类型id
	
	public int parentId; //积分类型父id
	
	public String typeName; //积分类型名称
	
	public String typeCode; //积分类型编码
	
	public int changeType; //积分变动类型
	
	public int isRoot; //是否跟节点

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the parentId
	 */
	public int getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * @return the typeCode
	 */
	public String getTypeCode() {
		return typeCode;
	}

	/**
	 * @param typeCode the typeCode to set
	 */
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	/**
	 * @return the changeType
	 */
	public int getChangeType() {
		return changeType;
	}

	/**
	 * @param changeType the changeType to set
	 */
	public void setChangeType(int changeType) {
		this.changeType = changeType;
	}

	/**
	 * @return the isRoot
	 */
	public int getIsRoot() {
		return isRoot;
	}

	/**
	 * @param isRoot the isRoot to set
	 */
	public void setIsRoot(int isRoot) {
		this.isRoot = isRoot;
	}
	
}
