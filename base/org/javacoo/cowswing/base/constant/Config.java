package org.javacoo.cowswing.base.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置参数
 * @author javacoo
 * @since 2012-02-29
 */
public abstract class Config {
	/**
	 * 配置参数MAP
	 */
	public static Map<String, Map<String, String>> COWSWING_CONFIG_MAP = new HashMap<String, Map<String,String>>();
	/**
	 * 配置参数文件路径MAP
	 */
	public static Map<String, String> COWSWING_CONFIG_RESOURCES_PATH_MAP = new HashMap<String, String>();
	
	//---------------------------核心配置-----------------------------------------
	/**
	 * 核心配置key
	 */
	public static String COWSWING_CONFIG_KEY_CORE = "appCore";
	/**系统classLoaderPath*/
	public static String COWSWING_CONFIG_KEY_CORE_CLASS_LOADER_PATH = "classLoaderPath";
	
	
	//---------------------------界面基本配置-----------------------------------------
	/**
	 * 界面基本配置KEY
	 */
	public static String COWSWING_CONFIG_KEY_INIT = "appInitConfig";
	/**主窗体宽*/
	public static String COWSWING_CONFIG_KEY_INIT_FRAME_WIDTH = "frameWidth";
	/**主窗体高*/
	public static String COWSWING_CONFIG_KEY_INIT_FRAME_HEIGHT= "frameHeight";
	/**显示广告*/
	public static String COWSWING_CONFIG_KEY_INIT_SHOW_AD = "showAdvertisement";
	/**显示广告路径*/
	public static String COWSWING_CONFIG_KEY_INIT_SHOW_AD_PATH = "showAdvertisementPath";
	/**播放音乐*/
	public static String COWSWING_CONFIG_KEY_INIT_SHOW_MUSIC = "showMusic";
	/**进入主界面欢迎音乐名称*/
	public static String COWSWING_CONFIG_KEY_INIT_SHOW_MUSIC_NAME_WELCOME = "welocmeMusicName";
	/**消息提醒音乐名称*/
	public static String COWSWING_CONFIG_KEY_INIT_SHOW_MUSIC_NAME_MSG = "msgMusicName";
	
	//---------------------------程序版本信息-----------------------------------------
	/**
	 * 程序版本信息配置KEY
	 */
	public static String COWSWING_CONFIG_KEY_VERSION = "appVersionConfig";
	/**丑牛主版本信息*/
	public static String COWSWING_CONFIG_KEY_VERSION_VERSION = "version";
	/**版本作者*/
	public static String COWSWING_CONFIG_KEY_VERSION_AUTHOR = "author";
	/**版本日期*/
	public static String COWSWING_CONFIG_KEY_VERSION_DATE = "date";
	/**版本信息描述*/
	public static String COWSWING_CONFIG_KEY_VERSION_INFO = "info";
	/**版本更新列表字符串*/
	public static String COWSWING_CONFIG_KEY_VERSION_UPDATE_LIST = "updateList";
	
	
}
