/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.core.constant;

/**
 * 
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-6-17 下午3:24:23
 * @version 1.0
 */
public interface CoreConstant {
	
	public static final String PLUGIN_PROPERTIES_KY_PATH = "path";
	public static final String PLUGIN_PROPERTIES_KY_ID = "Id";
	public static final String PLUGIN_PROPERTIES_KY_NAME = "Name";
	public static final String PLUGIN_PROPERTIES_KY_VERSION = "Version";
	public static final String PLUGIN_PROPERTIES_KY_PROVIDER = "Provider";
	public static final String PLUGIN_PROPERTIES_KY_BEANXMLPATH = "BeanXmlPath";
	public static final String PLUGIN_PROPERTIES_KY_REQUIRES = "Requires";
	public static final String PLUGIN_PROPERTIES_KY_ACTIVE = "Active";
	public static final String PLUGIN_PROPERTIES_KY_LOGOIMAGEPATH = "LogoImagePath";
	/**网络动作类型--发送监听请求*/
	public final static String NET_ACTION_TYPE_LISTEN = "LISTEN";
	/**网络动作类型--监听请求返回*/
	public final static String NET_ACTION_TYPE_LISTENRETURN = "LISTENRETURN";
	/**网络动作类型--查询对方是否在线*/
	public final static String NET_ACTION_ARE_YOU_STILL_ALIVE = "AREYOUSTILLALIVE";
	/**网络动作类型--查询对方是否在线返回*/
	public final static String NET_ACTION_I_AM_ALIVE = "IAMALIVE";
	/**网络动作类型--添加*/
	public final static String NET_ACTION_TYPE_ADD = "ADD";
	/**网络动作类型--删除*/
	public final static String NET_ACTION_TYPE_DEL = "DEL";
	/**网络动作类型--刷新请求*/
	public final static String NET_ACTION_TYPE_REFRESH = "REFRESH";
	/**网络动作类型--刷新返回*/
	public final static String NET_ACTION_TYPE_REBACK = "REBACK";
	/**网络动作类型--私聊信息*/
	public final static String NET_ACTION_TYPE_PERSONMSG = "PERSONMSG";
	/**网络动作类型--公共信息*/
	public final static String NET_ACTION_TYPE_COMMONMSG = "COMMONMSG";
	/**网络动作类型--个人视频申请信息*/
	public final static String NET_ACTION_TYPE_PERSON_APPLY_VIDEO = "PERSON_APPLY_VIDEO";
	/**网络动作类型--个人视频同意信息*/
	public final static String NET_ACTION_TYPE_PERSON_AGREE_VIDEO = "PERSON_AGREE_VIDEO";
	/**网络动作类型--个人视频拒绝信息*/
	public final static String NET_ACTION_TYPE_PERSON_REFUSE_VIDEO = "PERSON_REFUSE_VIDEO";
	/**网络动作类型--个人视频断开信息*/
	public final static String NET_ACTION_TYPE_PERSON_CANCEL_VIDEO = "PERSON_CANCEL_VIDEO";
	/**网络动作类型--个人视频未响应开信息*/
	public final static String NET_ACTION_TYPE_PERSON_NO_RESPONSE_VIDEO = "PERSON_NO_RESPONSE_VIDEO";
	/**网络动作类型--个人视频忙碌信息*/
	public final static String NET_ACTION_TYPE_PERSON_BUSY_VIDEO = "PERSON_BUSY_VIDEO";
	/**网络动作类型--搜索*/
	public final static String NET_ACTION_TYPE_SEARCH = "SEARCH";
	/**网络动作类型--搜索返回*/
	public final static String NET_ACTION_TYPE_SEARCH_RETURN = "SEARCH_RETURN";
	/**网络动作类型--上线*/
	public final static String NET_ACTION_TYPE_ONLINE = "ONLINE";
	/**网络动作类型--上线返回*/
	public final static String NET_ACTION_TYPE_ONLINEBCAK = "ONLINEBCAK";
	/**网络动作类型--下线*/
	public final static String NET_ACTION_TYPE_OUTLINE = "OUTLINE";
	/**网络动作类型--查看*/
	public final static String NET_ACTION_TYPE_VIEW = "VIEW";
	/**网络动作类型--查看返回*/
	public final static String NET_ACTION_TYPE_VIEW_RETURN = "VIEW_RETURN";
	/**网络动作类型--下载*/
	public final static String NET_ACTION_TYPE_DOWNLOAD = "DOWNLOAD";
	/**网络动作类型--下载返回*/
	public final static String NET_ACTION_TYPE_DOWNLOAD_RETURN = "DOWNLOAD_RETURN";
	/**网络动作类型--聊天下载返回*/
	public final static String NET_ACTION_TYPE_CHAT_DOWNLOAD_RETURN = "CHAT_DOWNLOAD_RETURN";
	/**网络动作类型--同意下载*/
	public final static String NET_ACTION_TYPE_DOWNLOAD_AGREE = "DOWNLOAD_AGREE";
	/**网络动作类型--中断下载*/
	public final static String NET_ACTION_TYPE_DOWNLOAD_BREAK = "DOWNLOAD_BREAK";
	/**网络动作类型--单文件*/
	public final static String NET_ACTION_TYPE_DOWNLOAD_ONLYFILE = "ONLYFILE";
	/**网络动作类型--结束传输文件夹*/
	public final static String NET_ACTION_TYPE_DOWNLOAD_ENDFOLDERT = "ENDFOLDERT";
	/**网络动作类型--开始传输文件夹*/
	public final static String NET_ACTION_TYPE_DOWNLOAD_BEGINFOLDERT = "BEGINFOLDERT";
	/**网络动作类型--开始传输文件*/
	public final static String NET_ACTION_TYPE_DOWNLOAD_BEGINFILET = "BEGINFILET";
	/**网络动作类型--对话请求*/
	public final static String NET_ACTION_TYPE_CHATREQ = "CHATREQ";
	/**网络动作类型--多人视频加入*/
	public final static String NET_ACTION_TYPE_MULTI_VIDEO_IN = "MULTI_VIDEO_IN";
	/**网络动作类型--多人视频退出*/
	public final static String NET_ACTION_TYPE_MULTI_VIDEO_OUT = "MULTI_VIDEO_OUT";
	
}
