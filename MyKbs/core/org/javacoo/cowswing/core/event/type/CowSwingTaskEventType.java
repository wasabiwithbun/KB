/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.core.event.type;



/**
 * 批量任务事件类型
 *@author DuanYong
 *@since 2012-11-4下午10:19:36
 *@version 1.0
 */
public enum CowSwingTaskEventType implements EventType{
	/** 任务监控Event. */
	TaskTypeStartEvent,TaskTypeIngEvent,TaskTypeEndEvent,TaskTypeAddEvent;

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.core.event.EventType#getEventType()
	 */
	@Override
	public String getEventType() {
		return "CowSwingTaskEventType";
	}
    
	

}
