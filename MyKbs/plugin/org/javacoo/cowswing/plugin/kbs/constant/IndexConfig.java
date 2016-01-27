/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.constant;

/**
 * 索引文件常量配置
 * <p>说明:</p>
 * <li></li>
 * 
 * @author DuanYong
 * @since 2013-6-30 下午9:50:44
 * @version 1.0
 */
public interface IndexConfig {
	// =============================以下字段用于创建索引docment中指定字段名称=========================
	// =============================公共属性==========================================
	/** 对应数据库ID */
	public final static String FIELD_ID = "id";
	/** 模块 */
	public final static String FIELD_MODULE = "module";
	/** 索引目录名称 */
	public final static String FIELD_INDEXDIRNAMR = "indexDirName";
	/** 文档标题 */
	public final static String FIELD_TITLE = "title";
	/** 文档描述 */
	public final static String FIELD_DESC = "desc";
	/** 文档内容 */
	public final static String FIELD_CONTENT = "content";
	/** 关键字 */
	public final static String FIELD_KEYWORD = "keyWord";
	/** 展示时间 */
	public final static String FIELD_FORSHOWDATE = "showDate";
	/** 类型 */
	public final static String FIELD_TYPECODE = "typeCode";
	/** 类型描述 */
	public final static String FIELD_TYPEDESC = "typeDesc";
	/** 发布者姓名 */
	public final static String FIELD_USERNAME = "userName";
	/** 发布者编码 */
	public final static String FIELD_USERCODE = "userCode";
	/** 权限，是否可全局搜索,可查看,可下载 */
	public final static String FIELD_PURVIEW = "purview";
	/** 状态 */
	public final static String FIELD_STATE = "state";
	/** 数量 */
	public final static String FIELD_COUNT = "count";
	/** 本地文档 */
	public final static String FIELD_ORIGIN_LOCAL = "local";
	/** 网络文档 */
	public final static String FIELD_ORIGIN_NET = "net";
	/** 数据库文档 */
	public final static String FIELD_ORIGIN_DB = "db";
	// ==================================资源====================================
	/** 文档路径 */
	public final static String FIELD_PATH = "path";
	/** 修改时间 */
	public final static String FIELD_MODIFIED = "modified";
	/** 文档目录 */
	public final static String FIELD_DIRECTORY = "directory";
	/** 文档名称 */
	public final static String FIELD_ORIGNAME = "origname";
	/** 文档来源 */
	public final static String FIELD_ORIGIN = "origin";
	// ==================================文章====================================
	/** 作者 */
	public final static String FIELD_AUTHOR = "author";
	/** 评论 */
	public final static String FIELD_COMMENT = "comment";
	// ==================================问答====================================
	/** 答案 */
	public final static String FIELD_ANSWER = "answer";
	
	
	
}
