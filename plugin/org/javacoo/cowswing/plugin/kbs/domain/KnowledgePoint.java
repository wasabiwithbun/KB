/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.domain;

/**
 * 知识点对象
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-6-30 下午12:18:43
 * @version 1.0
 */
public class KnowledgePoint {
	/** 主键 */
	private String id;
	/**索引目录名称*/
	private String indexDirName;
	/** 模块*/
	private String module = "";
	/** 标题 */
	private String title = "";
	/** 显示时间 */
	private String showDate = "";
	/** 关键字 */
	private String keyWord = "";
	/** 操作类型 */
	private String operateFlag = "";
	/** 内容 */
	private String content = "";
	/** 描述 */
	private String desc = "";
	/** 类型编码 */
	private String typeCode = "";
	/** 类型描述 */
	private String typeDesc = "";
	/** 用于保存与搜索相关的预览摘要 */
	private String contextDigest = "";
	/**发布者姓名*/
	private String userName = "";
	/**发布者编码*/
	private String userCode = "";
	/**权限,是否可全局搜索,可查看,可下载*/
	private String purview = "";
	/**状态*/
	private String state = "";
	/**数量*/
	private String count = "";
	//资源模块
	/** 文档路径 */
	private String filePath = "";
	/** 源文件名 */
	private String originName = "";
	/** 更新时间 */
	private String uploadDate = "";
	/** 文件夹名称 */
	private String fileDirs = "";
	/** 文档来源类型，本地，网络，数据库 */
	private String origin = "";
	//文章模块
	/**文章源作者*/
	private String author = "";
	/**评论*/
	private String comment = "";
	//问答模块
	/**回答*/
	private String answer = "";
	
	private boolean isBigFile = false;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	 * @return the showDate
	 */
	public String getShowDate() {
		return showDate;
	}
	/**
	 * @param showDate the showDate to set
	 */
	public void setShowDate(String showDate) {
		this.showDate = showDate;
	}
	/**
	 * @return the keyWord
	 */
	public String getKeyWord() {
		return keyWord;
	}
	/**
	 * @param keyWord the keyWord to set
	 */
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	/**
	 * @return the operateFlag
	 */
	public String getOperateFlag() {
		return operateFlag;
	}
	/**
	 * @param operateFlag the operateFlag to set
	 */
	public void setOperateFlag(String operateFlag) {
		this.operateFlag = operateFlag;
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
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
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
	 * @return the typeDesc
	 */
	public String getTypeDesc() {
		return typeDesc;
	}
	/**
	 * @param typeDesc the typeDesc to set
	 */
	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}
	/**
	 * @return the contextDigest
	 */
	public String getContextDigest() {
		return contextDigest;
	}
	/**
	 * @param contextDigest the contextDigest to set
	 */
	public void setContextDigest(String contextDigest) {
		this.contextDigest = contextDigest;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
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
	 * @return the count
	 */
	public String getCount() {
		return count;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(String count) {
		this.count = count;
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
	 * @return the originName
	 */
	public String getOriginName() {
		return originName;
	}
	/**
	 * @param originName the originName to set
	 */
	public void setOriginName(String originName) {
		this.originName = originName;
	}
	/**
	 * @return the uploadDate
	 */
	public String getUploadDate() {
		return uploadDate;
	}
	/**
	 * @param uploadDate the uploadDate to set
	 */
	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}
	/**
	 * @return the fileDirs
	 */
	public String getFileDirs() {
		return fileDirs;
	}
	/**
	 * @param fileDirs the fileDirs to set
	 */
	public void setFileDirs(String fileDirs) {
		this.fileDirs = "dir_"+fileDirs;
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
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	/**
	 * @return the answer
	 */
	public String getAnswer() {
		return answer;
	}
	/**
	 * @param answer the answer to set
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	/**
	 * @return the isBigFile
	 */
	public boolean isBigFile() {
		return isBigFile;
	}
	/**
	 * @param isBigFile the isBigFile to set
	 */
	public void setBigFile(boolean isBigFile) {
		this.isBigFile = isBigFile;
	}
	
	
}
