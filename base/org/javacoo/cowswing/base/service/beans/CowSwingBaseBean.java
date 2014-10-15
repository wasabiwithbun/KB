package org.javacoo.cowswing.base.service.beans;

import org.javacoo.cowswing.core.event.AbstractCowSwingObserver;
import org.javacoo.cowswing.core.event.CowSwingEvent;
import org.javacoo.cowswing.core.event.CowSwingObserver;
import org.javacoo.cowswing.core.event.type.CowSwingEventType;

/**
 * 值对象基类
 *@author DuanYong
 *@since 2012-11-28下午9:19:16
 *@version 1.0
 */
public class CowSwingBaseBean {
	/**事件对象*/
	private CowSwingEvent cowSwingEvent;
	/**事件观察者对象*/
	private AbstractCowSwingObserver observer;

	public CowSwingEvent getCowSwingEvent() {
		if(null == cowSwingEvent){
			cowSwingEvent = new CowSwingEvent(this,CowSwingEventType.NoTableChangeEvent);
		}
		return cowSwingEvent;
	}

	public void setCowSwingEvent(CowSwingEvent cowSwingEvent) {
		this.cowSwingEvent = cowSwingEvent;
	}

	/**
	 * @return the observer
	 */
	public AbstractCowSwingObserver getObserver() {
		if(null == observer){
			observer = CowSwingObserver.getInstance();
		}
		return observer;
	}

	/**
	 * @param observer the observer to set
	 */
	public void setObserver(AbstractCowSwingObserver observer) {
		this.observer = observer;
	}

	
	
	

}
