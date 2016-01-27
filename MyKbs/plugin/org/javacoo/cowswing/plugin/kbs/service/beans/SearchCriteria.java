/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.service.beans;

import org.javacoo.persistence.PaginationCriteria;

/**
 * 搜索条件对象
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-8 上午9:44:57
 * @version 1.0
 */
public class SearchCriteria extends PaginationCriteria{
	
	private static final long serialVersionUID = 1L;
	/** 搜索条件 */
	private String queryKey;
	/** 对应的数据库主键 */
	private String objectId;
	/** 模块 */
	private String module;
	/** 类型编码 */
	private String typecode;
	/** 系统号 */
	private String systemCode;
	/** 文档目录 */
	private String directory;
	/** 状态 */
	private String state;
	/** 查询域 */
	private String queryField;
	/** 文档来源 */
	private String origin;
	/**索引目录名称*/
	private String indexDirName;
	/**
	 * @return the queryKey
	 */
	public String getQueryKey() {
		return queryKey;
	}
	/**
	 * @param queryKey the queryKey to set
	 */
	public void setQueryKey(String queryKey) {
		this.queryKey = queryKey;
	}
	/**
	 * @return the objectId
	 */
	public String getObjectId() {
		return objectId;
	}
	/**
	 * @param objectId the objectId to set
	 */
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	/**
	 * @return the module
	 */
	public String getModule() {
		return module;
	}
	/**
	 * @param module the module to set
	 */
	public void setModule(String module) {
		this.module = module;
	}
	/**
	 * @return the typecode
	 */
	public String getTypecode() {
		return typecode;
	}
	/**
	 * @param typecode the typecode to set
	 */
	public void setTypecode(String typecode) {
		this.typecode = typecode;
	}
	/**
	 * @return the systemCode
	 */
	public String getSystemCode() {
		return systemCode;
	}
	/**
	 * @param systemCode the systemCode to set
	 */
	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}
	/**
	 * @return the directory
	 */
	public String getDirectory() {
		return directory;
	}
	/**
	 * @param directory the directory to set
	 */
	public void setDirectory(String directory) {
		this.directory = directory;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the queryField
	 */
	public String getQueryField() {
		return queryField;
	}
	/**
	 * @param queryField the queryField to set
	 */
	public void setQueryField(String queryField) {
		this.queryField = queryField;
	}
	
	
	/**
	 * @return the origin
	 */
	public String getOrigin() {
		return origin;
	}
	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	/**
	 * @return the indexDirName
	 */
	public String getIndexDirName() {
		return indexDirName;
	}
	/**
	 * @param indexDirName the indexDirName to set
	 */
	public void setIndexDirName(String indexDirName) {
		this.indexDirName = indexDirName;
	}
	
	
}
