/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.event;

import org.javacoo.cowswing.core.event.type.EventType;

/**
 * 知识库EVENTTYPE
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-7-23 下午4:08:41
 * @version 1.0
 */
public enum KbsEventType implements EventType{
	/** 监控Event. */
	KbsTypeChangeEvent,KbsTypeUpdateEvent,
	/** 资源Event. */
	KbsModuleSourceChangeEvent,KbsModuleSourceAddEvent,KbsModuleSourceUpdateEvent,KbsModuleSourceDeleteEvent,
	/** 文章Event. */
	KbsModuleArticleChangeEvent,KbsModuleArticleAddEvent,KbsModuleArticleUpdateEvent,KbsModuleArticleDeleteEvent;
	public boolean isAlso(KbsEventType aEventType){
		if (aEventType == KbsEventType.KbsTypeChangeEvent) {
			switch (this) {
			case KbsTypeChangeEvent:
				return true;
			case KbsTypeUpdateEvent:
				return true;
			default:
				return false;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.core.event.EventType#getEventType()
	 */
	@Override
	public String getEventType() {
		return "KbsEventType";
	}
	
}
