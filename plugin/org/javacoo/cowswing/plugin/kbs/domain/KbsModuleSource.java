package org.javacoo.cowswing.plugin.kbs.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 * 资源模块
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-11 上午10:55:16
 * @version 1.0
 */
public class KbsModuleSource implements Serializable{
	private static final long serialVersionUID = 1L;
    /**资源ID*/
	private Integer id;
	/** 资源ID集合 */
	private List<Integer> idList;
	/**资源所属分类编码*/
	private String typeCode;
	/**资源所属目录编码*/
	private String dirCode;
	/**资源名称*/
	private String fileName;
	/**资源路径*/
	private String filePath;
	/**资源标题*/
	private String title;
	/**资源关键字*/
	private String keyword;
	/**资源描述*/
	private String descript;
	/**资源更新时间*/
	private Date uploadDate;
	/**资源索引状态*/
	private String indexState;
	/**资源所属分类名称*/
    private String typeName;
    /**资源权限*/
    private String purview;
    /**资源所属用户编码*/
    private String userCode;
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
	 * @param typeCode the typeCode to set
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
	 * @param dirCode the dirCode to set
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
	 * @param fileName the fileName to set
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
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
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
	 * @param keyword the keyword to set
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
	 * @param descript the descript to set
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
	 * @param uploadDate the uploadDate to set
	 */
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
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
	 * @param typeName the typeName to set
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
	 * @param purview the purview to set
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
	 * @param userCode the userCode to set
	 */
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	
    
    
    
}