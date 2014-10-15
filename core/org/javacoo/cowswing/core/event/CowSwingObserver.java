/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.core.event;

import org.javacoo.cowswing.core.event.type.CowSwingEventType;




/**
 * 公共事件观察者
 *@author DuanYong
 *@since 2012-11-4下午10:34:22
 *@version 1.0
 */
public class CowSwingObserver extends AbstractCowSwingObserver{
	private static CowSwingObserver observer;
	private CowSwingObserver(){
		eventType = CowSwingEventType.XEventType;
	}
	
	public static CowSwingObserver getInstance(){
		if( observer== null){
			observer = new CowSwingObserver();
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
