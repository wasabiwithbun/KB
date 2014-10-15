/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.constant;


/**
 * 知识库常量定义
 * <p>
 * 说明:
 * </p>
 * <li></li>
 * 
 * @author DuanYong
 * @since 2013-6-19 上午9:16:16
 * @version 1.0
 */
public interface KbsConstant {
	/** 系统根目录 */
	public static final String SYSTEM_ROOT_PATH = System.getProperties().getProperty("user.dir");
	/** 系统分隔符 */
	public static final String SYSTEM_SEPARATOR = System.getProperties().getProperty("file.separator");
	/** 系统行分隔符 */
	public final static String SYSTEM_LINE_SEPARATOR = System.getProperty("line.separator");
	/** 下划线分隔符 */
	public static final String SYSTEM_LINE_SPITE = "_";
	/** 斜线分隔符 */
	public static final String SYSTEM_SLANT_LINE_SPITE = "/";
	/** 反斜线分隔符 */
	public static final String SYSTEM_AGA_SLANT_LINE_SPITE = "\\";
	/** 源文件存放文件夹名称 */
	public final static String DEFAULT_SOURCE_DIR = "source";
	/** 索引文件存放文件夹名称 */
	public final static String DEFAULT_INDEX_DIR = "index";
	/** 索引文件存放文件夹默认模块名称 */
	public final static String DEFAULT_INDEX_MODULE_DIR = "default";
	/** 默认搜索页数 */
	public final static int DEFAULT_PAGE_NUM = 5;
	/** 低版本的IndexReader的存活时间 5s */
	public final static int STALE_INDEXREADER_SURVIVAL_TIME = 5000;
	/** 设置segment添加文档(Document)时的合并频率 */
	public final static int MERGE_FACTOR = 5;
	/** 设置segment最大合并文档(Document)数 */
	public final static int MAX_MERGE_DOCS = 1000;

	/**索引优化后文件段的数量，数量越大，优化效率越低*/
	public static final int DEFAULT_MAX_NUM_SEGMENTS = 1;
	// 搜索权限
	public static String SEARCH_PURVIEW_CATEGORY = "searchPurviewEnum";// 搜索权限
	/**私有权限*/
	public static String SEARCH_PURVIEW_PERSON = "person";// 私有
	/**搜索权限*/
	public static String SEARCH_PURVIEW_SEARCH = "search";// 搜索
	/**查看权限*/
	public static String SEARCH_PURVIEW_VIEW = "view";// 查看
	/**下载权限*/
	public static String SEARCH_PURVIEW_DOWNLOAD = "download";// 下载
	
	/**来源-原创*/
	public static String ORIGIN_PERSON = "original";// 原创
	/**来源-网络*/
	public static String ORIGIN_NETWORK = "network";// 网络
	/**来源-本地*/
	public static String ORIGIN_LOCAL= "local";// 本地
	/** 编码*/
	public final static String ENCODING = "UTF-8";
	// 系统模块
	/**分类模块*/
	public static String SYSTEM_MODULE_SORT= "SORT";
	/**资源管理模块*/
	public static String SYSTEM_MODULE_SOURCE = "SOURCE"; 
	/**问答模块*/
	public static String SYSTEM_MODULE_QA = "QUESTIONANSWER";
	/**内容管理模块*/
	public static String SYSTEM_MODULE_ARTICLE = "ARTICLE";
	
	public static String SYSTEM_REBUILD_INDEX = "rebuildindex"; // 重建索引
	public static String SYSTEM_MODULE_SOURCEDOC = "source_doc"; // 文档管理
	public static String SYSTEM_MODULE_SOURCETASK = "source_task"; // 索引任务管理
	/**HTTP*/
	public final static String HTTP = "http://";
	
	/**知识分类树根节点编码*/
	public final static String KBS_TYPE_ROOT_CODE = "knowledge";
	/**知识分类节点编码*/
	public final static String KBS_TYPE_CHILD_DEFAULT_CODE = "knowledge_default";
	/**通讯录节点编码*/
	public final static String KBS_TYPE_CHILD_ADDRESS_CODE = "knowledge_address";
	/**书签节点编码*/
	public final static String KBS_TYPE_CHILD_BOOKMARK_CODE = "knowledge_bookmark";
	/**属性节点编码*/
	public final static String KBS_TYPE_CHILD_ATTRIBUT_CODE = "attribute";
	/**editor编码*/
	public final static String KBS_TYPE_CHILD_EDITOR_CODE = "editor";
	/**分类属性输入控件类型-文本输入框编码*/
	public final static String KBS_TYPE_CHILD_ATTRIBUT_CMP_TYPE_TEXT_CODE = "text";
	/**分类属性输入控件类型-文本输入框名称*/
	public final static String KBS_TYPE_CHILD_ATTRIBUT_CMP_TYPE_TEXT_NAME = "文本输入框";
	/**分类属性输入控件类型-文本输入域编码*/
	public final static String KBS_TYPE_CHILD_ATTRIBUT_CMP_TYPE_TEXTAREA_CODE = "textarea";
	/**分类属性输入控件类型-文本输入域名称*/
	public final static String KBS_TYPE_CHILD_ATTRIBUT_CMP_TYPE_TEXTAREA_NAME = "文本输入域";
	/**分类属性输入控件类型-日期选择框编码*/
	public final static String KBS_TYPE_CHILD_ATTRIBUT_CMP_TYPE_DATE_CODE = "date";
	/**分类属性输入控件类型-日期选择框名称*/
	public final static String KBS_TYPE_CHILD_ATTRIBUT_CMP_TYPE_DATE_NAME = "日期选择框";
	/**分类属性输入控件类型-单选按钮编码*/
	public final static String KBS_TYPE_CHILD_ATTRIBUT_CMP_TYPE_RADIOBOX_CODE = "radiobox";
	/**分类属性输入控件类型-单选按钮名称*/
	public final static String KBS_TYPE_CHILD_ATTRIBUT_CMP_TYPE_RADIOBOX_NAME = "单选按钮";
	/**分类属性输入控件类型-多选按钮编码*/
	public final static String KBS_TYPE_CHILD_ATTRIBUT_CMP_TYPE_CHECKBOX_CODE = "checkbox";
	/**分类属性输入控件类型-多选按钮名称*/
	public final static String KBS_TYPE_CHILD_ATTRIBUT_CMP_TYPE_CHECKBOX_NAME = "多选按钮";
	
	/**本地磁盘根目录*/
	public final static String KBS_LOCAL_ROOT_CODE = "local_root";
	
	public final static String KBS_LOCAL_SEETING_SURR_DIR_KEY = "currDir";
	public final static String KBS_LOCAL_SEETING_SURR_DIYFILETYPE_KEY = "diyFileType";
	public final static String KBS_LOCAL_SEETING_SURR_MAXFILESIZE_KEY = "maxFileSize";
	/**用户缓存属性配置文件中key:本地监听目录 */
	public static final String KBS_LOCAL_FILE_PROPERTY_KEY_LISTENER_DIRS = "listenerDirs";
	
	
	
	
	
	

	// ---------------------------基本配置-----------------------------------------
	/**
	 * 知识库基本配置KEY
	 */
	public static String COWSWING_KBS_CONFIG_KEY_INIT = "kbsInitConfig";
	/** 内存存放文档最大数 */
	public static String COWSWING_KBS_CONFIG_KEY_INIT_RAM_MAX_SIZE = "ramMaxSize";
	/** 索引文件最大大小 */
	public static String COWSWING_KBS_CONFIG_KEY_INIT_MAX_FILE_SIZE = "maxFileSize";
	/** 文档类型*/
	public static String COWSWING_KBS_CONFIG_KEY_INIT_FILE_CONTENT_TYPE = "fileContentType";
	/** 普通文本类型*/
	public static String COWSWING_KBS_CONFIG_KEY_INIT_FILE_CONTENT_SIMPLE_TYPE = "simpleFileContentType";
	
	/**---------------------知识分类SQLMAP---------------------------------------*/

	public static final String SQLMAP_ID_INSERT_KBS_TYPE = "insertKbsType";// 插入内容资源SQLMAP

	public static final String SQLMAP_ID_UPDATE_KBS_TYPE= "updateKbsType";// 更新内容资源SQLMAP

	public static final String SQLMAP_ID_DELETE_BY_ID_KBS_TYPE = "deleteKbsTypeById";// 根据ID删除内容SQLMAP
	
	public static final String SQLMAP_ID_DELETE_BY_PARENTTYPECODE_KBS_TYPE = "deleteKbsTypeByParentTypeCode";// 根据父分类编码删除内容SQLMAP

	public static final String SQLMAP_ID_GET_KBS_TYPE = "getKbsTypeByTypeCode";// 查询内容SQLMAP

	public static final String SQLMAP_ID_GET_LIST_KBS_TYPE = "getKbsTypeList";// 查询内容列表SQLMAP
	
	/**---------------------资源模块SQLMAP---------------------------------------*/

	public static final String SQLMAP_ID_INSERT_KBS_MODULE_SOURCE = "insertKbsModuleSource";// 插入内容资源SQLMAP

	public static final String SQLMAP_ID_UPDATE_KBS_MODULE_SOURCE= "updateKbsModuleSource";// 更新内容资源SQLMAP

	public static final String SQLMAP_ID_DELETE_BY_IDS_KBS_MODULE_SOURCE = "deleteKbsModuleSourceByIds";// 根据ID删除内容SQLMAP

	public static final String SQLMAP_ID_GET_KBS_MODULE_SOURCE = "getKbsModuleSourceById";// 查询内容SQLMAP

	public static final String SQLMAP_ID_GET_LIST_KBS_MODULE_SOURCE = "getKbsModuleSourceList";// 查询内容列表SQLMAP

	/**---------------------文章模块SQLMAP---------------------------------------*/

	public static final String SQLMAP_ID_INSERT_KBS_MODULE_ARTICLE = "insertKbsModuleArticle";// 插入内容SQLMAP

	public static final String SQLMAP_ID_UPDATE_KBS_MODULE_ARTICLE= "updateKbsModuleArticle";// 更新内容SQLMAP

	public static final String SQLMAP_ID_DELETE_BY_IDS_KBS_MODULE_ARTICLE = "deleteKbsModuleArticleByIds";// 根据ID删除内容SQLMAP

	public static final String SQLMAP_ID_GET_KBS_MODULE_ARTICLE = "getKbsModuleArticleById";// 查询内容SQLMAP

	public static final String SQLMAP_ID_GET_LIST_KBS_MODULE_ARTICLE = "getKbsModuleArticleList";// 查询内容列表SQLMAP

	
	

}
