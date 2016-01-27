package org.javacoo.cowswing.core.task.processor;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javacoo.cowswing.base.constant.Constant;
import org.javacoo.cowswing.core.event.CowSwingEvent;
import org.javacoo.cowswing.core.event.CowSwingTaskObserver;
import org.javacoo.cowswing.core.event.type.CowSwingTaskEventType;
import org.javacoo.cowswing.core.task.data.Task;


/**
 * 任务处理器接口抽象实现类
 * 
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-7-24 下午2:18:30
 * @version 1.0
 * @param <T> 任务对象
 */
public abstract class AbstractProcessor<T> implements Processor<T>{

	protected static Log log =  LogFactory.getLog(AbstractProcessor.class);
	/**默认下一个处理器*/
	private Processor<T> defaultNextProcessor = null;
	/**监控信息*/
	private Map<String,Object> monitorInfo = new HashMap<String,Object>();
	/**任务开始时间*/
	protected long startIndex;
	/**任务结束时间*/
	protected long endIndex;
	public AbstractProcessor(){
	}
	public void process(Task<T> task) {
		task.setNextProcessor(getDefaultNextProcessor());
		startIndex = System.currentTimeMillis();
		innerProcess(task);
		endIndex = System.currentTimeMillis();
		monitorInfo.put(Constant.TASK_MONITOR_TOTAL, task.getCurrTaskTotalNum());
		monitorInfo.put(Constant.TASK_MONITOR_NUM, task.getTaskNum());
		monitorInfo.put(Constant.TASK_MONITOR_DESC, logInfo());
		CowSwingTaskObserver.getInstance().notifyEvents(new CowSwingEvent(this,CowSwingTaskEventType.TaskTypeIngEvent,monitorInfo));
	}
	/**
	 * 具体处理任务细节由子类实现
	 */
	protected abstract void innerProcess(Task<T> task);
	/**
	 * 监控日志信息
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-7-24 下午5:59:22
	 * @version 1.0
	 * @exception 
	 * @return
	 */
	protected  String logInfo(){
		return "当前任务执行完成，耗时："+(endIndex - startIndex)+"毫秒";
	}
	
	public Processor<T> getDefaultNextProcessor() {
		return defaultNextProcessor;
	}

	public void setDefaultNextProcessor(Processor<T> defaultNextProcessor) {
		this.defaultNextProcessor = defaultNextProcessor;
	}
	
	

}
