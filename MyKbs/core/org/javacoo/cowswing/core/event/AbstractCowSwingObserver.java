/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.core.event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.javacoo.cowswing.core.event.type.EventType;

/**
 * 抽象事件观察者
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-7-25 上午9:34:23
 * @version 1.0
 */
public abstract class AbstractCowSwingObserver {
	protected Logger logger = Logger.getLogger(this.getClass());
	/**监听对象集合MAP，按监听事件类型，KEY 为监听事件类型,VALUE 为事件对象集合*/
	protected Map<String,CopyOnWriteArrayList<CowSwingListener>> repositoryMap = new ConcurrentHashMap<String, CopyOnWriteArrayList<CowSwingListener>>();
    /**事件类型*/
	protected EventType eventType;
	/**
	 * 添加监听对象
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-7-25 上午9:37:52
	 * @version 1.0
	 * @exception  
	 * @param crawlerListener监听对象
	 */
	public final void addCrawlerListener(CowSwingListener crawlerListener){
		if(!repositoryMap.containsKey(getEventType())){
			repositoryMap.put(getEventType(), new CopyOnWriteArrayList<CowSwingListener>());
		}
		repositoryMap.get(getEventType()).add(crawlerListener);
	}
	/**
	 * 删除注册的监听对象
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-7-25 上午10:22:12
	 * @version 1.0
	 * @exception 
	 * @param crawlerListener
	 */
	public final void removeCrawlerListener(CowSwingListener crawlerListener) {
		if(!repositoryMap.containsKey(getEventType())){
			repositoryMap.get(getEventType()).remove(crawlerListener);
		}
	}
	/**
	 * 执行事件通知
	 * <p>方法说明:</>
	 * <li>按事件类型分别通知</li>
	 * @author DuanYong
	 * @since 2014-7-25 上午9:53:08
	 * @version 1.0
	 * @exception 
	 * @param crawlerEvent
	 */
	@SuppressWarnings("unchecked")
	public void notifyEvents(CowSwingEvent crawlerEvent) {
		synchronized (this) {
			if(repositoryMap.containsKey(crawlerEvent.getEventType().getEventType())){
				CopyOnWriteArrayList<CowSwingListener> tempList = null;
				tempList = (CopyOnWriteArrayList<CowSwingListener>) repositoryMap.get(crawlerEvent.getEventType().getEventType()).clone();
				logger.info("触发:"+crawlerEvent.getEventType().getEventType()+"事件通知");
				logger.info("当前:"+crawlerEvent.getEventType().getEventType()+"事件注册对象数: " + tempList.size());
				if(CollectionUtils.isNotEmpty(tempList)){
					int size = tempList.size();
					for(CowSwingListener listener : tempList){
						logger.info("执行:"+crawlerEvent.getEventType().getEventType()+"事件 "+ (size--)+ " = " + listener);
						listener.update(crawlerEvent);
					}
				}
			}
		}
	}
	/**
	 * 取得当前监听对象事件类型
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-7-25 上午9:49:50
	 * @version 1.0
	 * @exception 
	 * @return 事件类型
	 */
	abstract protected String getEventType();
	
}
