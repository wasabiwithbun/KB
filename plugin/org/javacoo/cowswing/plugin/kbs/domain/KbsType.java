package org.javacoo.cowswing.plugin.kbs.domain;

import java.io.Serializable;

/**
 * 知识分类持久对象
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-11 上午10:40:59
 * @version 1.0
 */
public class KbsType implements Serializable{
	private static final long serialVersionUID = 1L;
    /**分类主键*/
	private Integer id;
	/**所属模块代码*/
	private String moduleCode;
	/**分类名称*/
	private String typeName;
	/**分类代码*/
	private String typeCode;
	/**父分类代码*/
	private String parentTypeCode;
	/** 扩展分类名称 */
	private String expandTypeName;
	/** 扩展分类代码 */
	private String expandTypeCode;
	/**扩展值*/
	private String expandValue;
	/** 是否是叶子节点 */
	private boolean isLeaf;
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the moduleCode
	 */
	public String getModuleCode() {
		return moduleCode;
	}
	/**
	 * @param moduleCode the moduleCode to set
	 */
	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
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
	 * @return the parentTypeCode
	 */
	public String getParentTypeCode() {
		return parentTypeCode;
	}
	/**
	 * @param parentTypeCode the parentTypeCode to set
	 */
	public void setParentTypeCode(String parentTypeCode) {
		this.parentTypeCode = parentTypeCode;
	}
	
	/**
	 * @return the expandTypeName
	 */
	public String getExpandTypeName() {
		return expandTypeName;
	}
	/**
	 * @param expandTypeName the expandTypeName to set
	 */
	public void setExpandTypeName(String expandTypeName) {
		this.expandTypeName = expandTypeName;
	}
	/**
	 * @return the expandTypeCode
	 */
	public String getExpandTypeCode() {
		return expandTypeCode;
	}
	/**
	 * @param expandTypeCode the expandTypeCode to set
	 */
	public void setExpandTypeCode(String expandTypeCode) {
		this.expandTypeCode = expandTypeCode;
	}
	/**
	 * @return the expandValue
	 */
	public String getExpandValue() {
		return expandValue;
	}
	/**
	 * @param expandValue the expandValue to set
	 */
	public void setExpandValue(String expandValue) {
		this.expandValue = expandValue;
	}
	/**
	 * @return the isLeaf
	 */
	public boolean isLeaf() {
		return isLeaf;
	}
	/**
	 * @param isLeaf the isLeaf to set
	 */
	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
    
	

}