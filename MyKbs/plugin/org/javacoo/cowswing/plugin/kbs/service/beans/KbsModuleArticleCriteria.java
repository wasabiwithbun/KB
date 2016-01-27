/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.service.beans;

import java.util.Date;
import java.util.List;

import org.javacoo.persistence.PaginationCriteria;

/**
 * 文章模块值对象
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-26 下午2:33:00
 * @version 1.0
 */
public class KbsModuleArticleCriteria extends PaginationCriteria{
	private static final long serialVersionUID = 1L;
	/**文章ID*/
	private Integer id;
	/** ID集合 */
	private List<Integer> idList;
	/**资源所属分类编码*/
	private String typeCode;
	/** 资源所属分类名称 */
	private String typeName;
	/**作者*/
	private String author;
	/**来源*/
	private String origin;
	/**标题*/
	private String title;
	/**关键字*/
	private String keyword;
	/**内容*/
	private String content;
	/**发布时间*/
	private Date releaseDate;
	/**索引状态*/
	private String indexState;
	/**权限*/
	private String purview;
	/**权限集合*/
	private List<String> purviewList;
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
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}
	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
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
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the releaseDate
	 */
	public Date getReleaseDate() {
		return releaseDate;
	}
	/**
	 * @param releaseDate the releaseDate to set
	 */
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
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
	 * @return the purviewList
	 */
	public List<String> getPurviewList() {
		return purviewList;
	}
	/**
	 * @param purviewList the purviewList to set
	 */
	public void setPurviewList(List<String> purviewList) {
		this.purviewList = purviewList;
	}
	
	
}
