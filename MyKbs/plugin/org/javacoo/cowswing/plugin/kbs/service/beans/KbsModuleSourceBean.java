/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.service.beans;

import java.util.Date;
import java.util.List;

/**
 * 资源模块值对象
 * <p>
 * 说明:
 * </p>
 * <li></li>
 * 
 * @author DuanYong
 * @since 2013-7-15 上午11:27:24
 * @version 1.0
 */
public class KbsModuleSourceBean extends KbsBaseBean {
	/** 资源ID */
	private Integer id;
	/** 资源ID集合 */
	private List<Integer> idList;
	/** 资源所属分类编码 */
	private String typeCode;
	/** 资源所属目录编码 */
	private String dirCode;
	/** 资源名称 */
	private String fileName;
	/** 资源路径 */
	private String filePath;
	/** 资源路径集合 */
	private List<String> filePathList;
	/** 资源标题 */
	private String title;
	/** 资源关键字 */
	private String keyword;
	/** 资源描述 */
	private String descript;
	/** 资源更新时间 */
	private Date uploadDate;
	/** 资源更新时间str */
	private String uploadDateStr;
	/** 资源索引状态 */
	private String indexState;
	/** 资源所属分类名称 */
	private String typeName;
	/** 资源权限 */
	private String purview;
	/** 资源权限 */
	private String purviewStr;
	/** 资源所属用户编码 */
	private String userCode;
	/** 包含子目录 */
	private String childDir;
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
    
	/**
	 * @return the idList
	 */
	public List<Integer> getIdList() {
		return idList;
	}

	/**
	 * @param idList the idList to set
	 */
	public void setIdList(List<Integer> idList) {
		this.idList = idList;
	}

	/**
	 * @return the typeCode
	 */
	public String getTypeCode() {
		return typeCode;
	}

	/**
	 * @param typeCode
	 *            the typeCode to set
	 */
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	/**
	 * @return the dirCode
	 */
	public String getDirCode() {
		return dirCode;
	}

	/**
	 * @param dirCode
	 *            the dirCode to set
	 */
	public void setDirCode(String dirCode) {
		this.dirCode = dirCode;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath
	 *            the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
    
	/**
	 * @return the filePathList
	 */
	public List<String> getFilePathList() {
		return filePathList;
	}

	/**
	 * @param filePathList the filePathList to set
	 */
	public void setFilePathList(List<String> filePathList) {
		this.filePathList = filePathList;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the keyword
	 */
	public String getKeyword() {
		return keyword;
	}

	/**
	 * @param keyword
	 *            the keyword to set
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	/**
	 * @return the descript
	 */
	public String getDescript() {
		return descript;
	}

	/**
	 * @param descript
	 *            the descript to set
	 */
	public void setDescript(String descript) {
		this.descript = descript;
	}

	/**
	 * @return the uploadDate
	 */
	public Date getUploadDate() {
		return uploadDate;
	}

	/**
	 * @param uploadDate
	 *            the uploadDate to set
	 */
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
    
	

	/**
	 * @return the uploadDateStr
	 */
	public String getUploadDateStr() {
		return uploadDateStr;
	}

	/**
	 * @param uploadDateStr the uploadDateStr to set
	 */
	public void setUploadDateStr(String uploadDateStr) {
		this.uploadDateStr = uploadDateStr;
	}

	/**
	 * @return the indexState
	 */
	public String getIndexState() {
		return indexState;
	}

	/**
	 * @param indexState the indexState to set
	 */
	public void setIndexState(String indexState) {
		this.indexState = indexState;
	}

	/**
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @param typeName
	 *            the typeName to set
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * @return the purview
	 */
	public String getPurview() {
		return purview;
	}

	/**
	 * @param purview
	 *            the purview to set
	 */
	public void setPurview(String purview) {
		this.purview = purview;
	}

	/**
	 * @return the userCode
	 */
	public String getUserCode() {
		return userCode;
	}

	/**
	 * @param userCode
	 *            the userCode to set
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	/**
	 * @return the childDir
	 */
	public String getChildDir() {
		return childDir;
	}

	/**
	 * @param childDir the childDir to set
	 */
	public void setChildDir(String childDir) {
		this.childDir = childDir;
	}

	/**
	 * @return the purviewStr
	 */
	public String getPurviewStr() {
		return purviewStr;
	}

	/**
	 * @param purviewStr the purviewStr to set
	 */
	public void setPurviewStr(String purviewStr) {
		this.purviewStr = purviewStr;
	}
    
}
