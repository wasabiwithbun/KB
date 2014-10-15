/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.base.constant;

/**
 * 常量定义
 * <p>
 * 说明:
 * </p>
 * <li></li>
 * 
 * @author DuanYong
 * @since 2013-6-1 下午1:43:47
 * @version 1.0
 */
public class Constant {
	public static final String VERSION = "2.0";

	public static final String DOMAIN_NAME = "org.javacoo";

	public static final String PROJECT_NAME = "crawler";

	public static final String PACKAGE_ROOT = DOMAIN_NAME + getDot()
			+ PROJECT_NAME;

	/**
	 * 窗体默认宽度
	 */
	public static final int FRAME_DEFAULT_WIDTH = 900;
	/**
	 * 窗体默认高度
	 */
	public static final int FRAME_DEFAULT_HEIGHT = 615;

	/**
	 * 帮助窗体默认宽度
	 */
	public static final int HELP_FRAME_DEFAULT_WIDTH = 600;
	/**
	 * 帮助窗体默认高度
	 */
	public static final int HELP_FRAME_DEFAULT_HEIGHT = 415;

	public static final String SYSTEM_ROOT_PATH = System.getProperties()
			.getProperty("user.dir");

	public static final String SYSTEM_SEPARATOR = System.getProperties()
			.getProperty("file.separator");

	/**
	 * 国际化配置路径
	 */
	public static final String PACKAGE_I18N = "settings" + getDot() + "i18n";
	/**
	 * 国际化配置名称
	 */
	public static final String I18N_BUNDLE_NAME = PACKAGE_I18N + getDot()
			+ "CowSwingAppLocal";

	/**
	 * 系统图标配置路径
	 */
	public static final String PACKAGE_IMAGE_PATH = "settings" + getDot()
			+ "image";
	/**
	 * 系统图标配置名称
	 */
	public static final String IMAGE_BUNDLE_NAME = PACKAGE_IMAGE_PATH
			+ getDot() + "CowSwingAppResources";

	/**
	 * 系统异常信息配置路径
	 */
	public static final String PACKAGE_EXCEPTION_PATH = "settings" + getDot()
			+ "exception";
	/**
	 * 系统异常信息配置名称
	 */
	public static final String EXCEPTION_BUNDLE_NAME = PACKAGE_EXCEPTION_PATH
			+ getDot() + "CowSwingAppException";

	/**
	 * 资源
	 */
	public static final String PROTOCOL_FILE = "file:";

	public static final String PACKAGE_RESOURSE = "resources";

	public static final String PACKAGE_PLUGINS = "plugins";

	public static final String PACKAGE_DOWNLOAD = "download";
	
	public static final String PACKAGE_UPDATE = "_update";
	
	public static final String PACKAGE_DATA = "data";
	
	public static final String UPDATE_FILE_NAME = "update.update";

	public static final String DEFAULT_SELECT_DIR = SYSTEM_ROOT_PATH
			+ SYSTEM_SEPARATOR + PACKAGE_DOWNLOAD;

	public static final String PACKGE_SOUNDS = SYSTEM_ROOT_PATH
			+ SYSTEM_SEPARATOR + PACKAGE_RESOURSE + SYSTEM_SEPARATOR + "sounds"
			+ SYSTEM_SEPARATOR;
	
	public static final String UPDATE_DIR = SYSTEM_ROOT_PATH
			+ SYSTEM_SEPARATOR + PACKAGE_UPDATE;
	
	public static final String UPDATE_FILE = SYSTEM_ROOT_PATH
			+ SYSTEM_SEPARATOR + PACKAGE_UPDATE + SYSTEM_SEPARATOR + UPDATE_FILE_NAME;

	public static final String PACKAGE_IMAGE = PACKAGE_RESOURSE + getDot()
			+ "icons";

	public static final String PACKAGE_CONFIG = PACKAGE_RESOURSE + getDot()
			+ "config";

	public static final String ICON_DIR = dotToSlash(PACKAGE_IMAGE);

	public static final String DATABASE_PROPERTIES = dotToSlash(PACKAGE_CONFIG)
			+ getSlash() + "CrawlerAppDatabase.properties";

	public static final String MIN_DAY = "1900-01-01";

	public static final String MAX_DAY = "9999-12-31";

	public static final String YES = "true";

	public static final String NO = "false";


	public static final String TASK_STATUS_STOP = "stop";// 停止

	public static final String TASK_STATUS_RUN = "run";// 执行中

	public static final String TASK_STATUS_PAUSE = "pause";// 暂停

	public static final String TASK_STATUS_COMPLETE = "complete";// 完成
	
	public static final String ACTION_LISTENER = "actionListener";

	public static final String OPTION_TYPE_ADD = "ADD";

	public static final String OPTION_TYPE_MODIFY = "MODIFY";

	public static final String OPTION_TYPE_EXECUTE = "EXECUTE";

	public static final int PAGE_LIMIT = 20;// 每页显示记录条数
	
	public static final String HTTP = "http://";
	public static final String HTTPS = "https://";

	public static final String TEMP_DIR = "temp";

	/** 配置类型：1 数据库配置 */
	public static final int CRAWLER_CONFIG_TYPE_DATABASE = 1;
	/** 配置类型：2 FTP配置 */
	public static final int CRAWLER_CONFIG_TYPE_FTP = 2;
	/** 默认开始日期 */
	public static final String DEFAULT_START_DATE = "2009-02-07";

	/** 系统监控面板索引值-内存 */
	public static final int SYSTEM_TABPANEL_INDEX_MEMORY = 1;
	/** 系统监控面板索引值-采集 */
	public static final int SYSTEM_TABPANEL_INDEX_GATHER = 2;
	/** 系统监控面板索引值-入库 */
	public static final int SYSTEM_TABPANEL_INDEX_SAVE = 3;
	/** 系统监控面板索引值-FTP */
	public static final int SYSTEM_TABPANEL_INDEX_FTP = 4;
	
	/** 属性配置文件中key:柜员号 */
	public static final String FILE_PROPERTY_KEY_USER_IDS = "userIds";
	/** 属性配置文件中key:最后一次登录用户ID */
	public static final String FILE_PROPERTY_KEY_LAST_USER_ID = "lastUserId";
	/** 属性配置文件中key:自动启动初始化标志 */
	public static final String FILE_PROPERTY_KEY_IS_AUTO_RUN= "isAutoRun";
	/** 属性配置文件中key:自动登录初始化标志 */
	public static final String FILE_PROPERTY_KEY_AUTO_LOGIN= "autoLogin";
	
	/**开启线程数*/
	public static final int DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors();
	/**一个任务的超时时间，单位为毫秒*/
	public static final int DEFAULT_TASK_TIMEOUT = 5000;
	/**任务状态-初始状态*/
	public final static String TASK_STATE_ORIGINAL = "original";
	/**任务状态-准备就绪*/
	public final static String TASK_STATE_READY = "ready";
	/**任务状态-运行中*/
	public final static String TASK_STATE_RUNNING = "running";
	/**任务状态-暂停*/
	public final static String TASK_STATE_PAUSE = "pause";
	
	public final static String TASK_MONITOR_TOTAL = "total";
	
	public final static String TASK_MONITOR_NUM = "num";
	
	public final static String TASK_MONITOR_DESC = "desc";
	/** 编码*/
	public final static String ENCODING_GBK = "GBK";
	/** 读取缓存*/
	public final static int  BUFFER_SIZE = 1024*1024;
	/** IP正则表达式*/
	public final static String NET_IP_REGEX = "^(2[2-3]{1}[4-9]{1})\\.(\\d{1,2}|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d{2}|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d{2}|2[0-4]\\d|25[0-5])$";
	
	/**
	 * 返回.
	 * <p>
	 * 方法说明:
	 * </p>
	 * 
	 * @auther DuanYong
	 * @since 2012-11-4 下午3:47:59
	 * @return String
	 */
	public static String getDot() {
		return ".";
	}

	public static String dotToSlash(String oldString) {
		return oldString.replace(getDot(), getSlash());
	}

	public static String getSlash() {
		return System.getProperty("file.separator");
	}

	private Constant() {
	}
}
