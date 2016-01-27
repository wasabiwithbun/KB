/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.event;

import org.javacoo.cowswing.core.event.AbstractCowSwingObserver;

/**
 * 知识库事件观察者
 * 
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-7-25 上午10:25:20
 * @version 1.0
 */
public class KbsEventObserver extends AbstractCowSwingObserver{
	private static KbsEventObserver observer;
	private KbsEventObserver(){
		eventType = KbsEventType.KbsTypeChangeEvent;
	}
	
	public static KbsEventObserver getInstance(){
		if( observer== null){
			observer = new KbsEventObserver();
		}
		return observer;
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.core.event.AbstractCowSwingObserver#getEventType()
	 */
	@Override
	protected String getEventType() {
		return eventType.getEventType();
	}
}
