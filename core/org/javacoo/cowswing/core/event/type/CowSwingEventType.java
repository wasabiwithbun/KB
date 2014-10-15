/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.core.event.type;



/**
 * 事件类型
 *@author DuanYong
 *@since 2012-11-4下午10:19:36
 *@version 1.0
 */
public enum CowSwingEventType implements EventType{
	/** 监控Event. */
	NoTableChangeEvent,
	/**发现网络数据*/
	NetDataChangeEvent,NetUserOnlineEvent,NetUserOnlineInfoEvent,NetUserOutlineEvent,NetSearceEvent,NetViewEvent,NetViewReturnEvent,NetDownLoadReturnEvent,NetDownLoadSuccess,NetDownLoadBeak,NetDownLoadIng,NetChatDownLoadReturnEvent,NetChatDownLoadSuccess,NetChatDownLoadBeak,NetChatDownLoadIng,NetChatSendIng,NetSendIng,NetChatDownLoadReturnReceiveingEvent,NetChatDownLoadReturnSendingEvent,NetChatFileReceiveingReqEvent,NetListenReturn,NetPersonMsg,NetPersonApplyVideo,NetPersonAgreeVideo,NetPersonRefuseVideo,NetPersonCancelVideo,NetPersonNoResponseVideo,NetPersonBusyVideo,NetCommonMsg,NetRefresh,NetDownloadRate,NetMultiVideoIn,NetMultiVideoOut,
	/** 监控Event. */
	ProgressChangeEvent,
	/** 数据校验*/
	ValidDataChangeEvent,
	/** 加载缓存*/
	CacheDataChangeEvent,CacheConfigChangeEvent,
	/** 系统启动*/
	SystemSatrtChangeEvent,SystemSpringInitComplateEvent,
	/** 系统退出*/
	SystemExitChangeEvent,
	XEventType;

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.core.event.EventType#getEventType()
	 */
	@Override
	public String getEventType() {
		return "CowSwingEventType";
	}
    
	

}
