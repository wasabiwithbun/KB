package org.javacoo.cowswing.core.task.data;

import org.javacoo.cowswing.core.task.TaskManagerService;
import org.javacoo.cowswing.core.task.processor.Processor;
import org.javacoo.cowswing.core.task.processor.ProcessorChain;


/**
 * 处理任务
 * 
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-7-24 下午2:21:42
 * @version 1.0
 * @param <T>任务对象
 */
public class Task<T> {
	/**任务控制器*/
	private TaskManagerService<T> controller;
	/**当前任务总数:用于监控*/
	private Integer currTaskTotalNum;
	/**所属计划编号:用于监控*/
	private Integer planNum;
	/**任务编号:用于监控*/
	private Integer taskNum;
	/**任务对象*/
	private T taskObj;
	/**是否完成*/
	private boolean finished;
	/**下一个处理器*/
	private transient Processor<T> nextProcessor;
	/**下一个处理器链*/
	private transient ProcessorChain<T> nextProcessorChain;
	
    
	/**
	 * @return the controller
	 */
	public TaskManagerService<T> getController() {
		return controller;
	}

	/**
	 * @param controller the controller to set
	 */
	public void setController(TaskManagerService<T> controller) {
		this.controller = controller;
	}

	public Processor<T> getNextProcessor() {
		return nextProcessor;
	}

	public void setNextProcessor(Processor<T> nextProcessor) {
		this.nextProcessor = nextProcessor;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public ProcessorChain<T> getNextProcessorChain() {
		return nextProcessorChain;
	}

	public void setNextProcessorChain(ProcessorChain<T> nextProcessorChain) {
		this.nextProcessorChain = nextProcessorChain;
	}



	public Integer getPlanNum() {
		return planNum;
	}

	public void setPlanNum(Integer planNum) {
		this.planNum = planNum;
	}

	public Integer getTaskNum() {
		return taskNum;
	}

	public void setTaskNum(Integer taskNum) {
		this.taskNum = taskNum;
	}

	public Integer getCurrTaskTotalNum() {
		return currTaskTotalNum;
	}

	public void setCurrTaskTotalNum(Integer currTaskTotalNum) {
		this.currTaskTotalNum = currTaskTotalNum;
	}
	
	/**
	 * @return the taskObj
	 */
	public T getTaskObj() {
		return taskObj;
	}

	/**
	 * @param taskObj the taskObj to set
	 */
	public void setTaskObj(T taskObj) {
		this.taskObj = taskObj;
	}

	/**
	 * 完成任务,并清理
	 */
	public void finished(){
		setFinished(true);
		setNextProcessor(null);
		setNextProcessorChain(null);
	}

	
	
    
}
