/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.service.beans;

import org.javacoo.persistence.PaginationCriteria;

/**
 * 知识分类查询对象
 * <p>
 * 说明:
 * </p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-11 下午2:15:51
 * @version 1.0
 */
public class KbsTypeCriteria extends PaginationCriteria{
	private static final long serialVersionUID = 1L;
	/** 分类主键 */
	private Integer id;
	/** 所属模块代码 */
	private String moduleCode;
	/** 分类名称 */
	private String typeName;
	/** 分类代码 */
	private String typeCode;
	/** 父分类代码 */
	private String parentTypeCode;
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
