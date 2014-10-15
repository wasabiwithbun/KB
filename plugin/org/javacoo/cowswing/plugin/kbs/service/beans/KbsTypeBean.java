/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.service.beans;


/**
 * 知识分类值对象
 * <p>
 * 说明:
 * </p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-11 下午2:15:51
 * @version 1.0
 */
public class KbsTypeBean extends KbsBaseBean{
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
	/** 父分类名称 */
	private String parentTypeName;
	/** 扩展分类名称 */
	private String expandTypeName;
	/** 扩展分类代码 */
	private String expandTypeCode;
	/**扩展值*/
	private String expandValue;
	/** 是否是叶子节点 */
	private boolean isLeaf = true;
	
	public KbsTypeBean(){}
	public KbsTypeBean(String typeCode){
		this.typeCode = typeCode;
	}
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
	 * @return the parentTypeName
	 */
	public String getParentTypeName() {
		return parentTypeName;
	}
	/**
	 * @param parentTypeName the parentTypeName to set
	 */
	public void setParentTypeName(String parentTypeName) {
		this.parentTypeName = parentTypeName;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return typeName;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((typeCode == null) ? 0 : typeCode.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KbsTypeBean other = (KbsTypeBean) obj;
		if (typeCode == null) {
			if (other.typeCode != null)
				return false;
		} else if (!typeCode.equals(other.typeCode))
			return false;
		return true;
	}
	
	

}
