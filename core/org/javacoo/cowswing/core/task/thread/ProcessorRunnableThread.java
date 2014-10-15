package org.javacoo.cowswing.core.task.thread;

import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javacoo.cowswing.base.constant.Constant;
import org.javacoo.cowswing.core.task.TaskManagerService;
import org.javacoo.cowswing.core.task.data.Task;
import org.javacoo.cowswing.core.task.processor.Processor;

/**
 * 处理线程-从边界控制器获取任务并执行
 * <li>把任务交给任务处理链处理</li>
 * @author javacoo
 * @since 2011-11-10
 */
@SuppressWarnings("rawtypes")
public class ProcessorRunnableThread implements Runnable {
	private static Log log =  LogFactory.getLog(ProcessorRunnableThread.class);
	/**任务控制器*/
	private TaskManagerService controller;
	private CountDownLatch latch;

	public ProcessorRunnableThread(TaskManagerService controller,CountDownLatch latch) {
		this.controller = controller;
		this.latch = latch;
	}
	public void run() {
		try {
			Task currentTask = null;
			while(!Thread.interrupted() && !this.controller.getProcessorManager().getThreadPoolService().isShutdown()){
				if(!this.controller.getFrontier().isEmpty()){
					currentTask = this.controller.getFrontier().next();
					processorTask(currentTask);
					this.controller.getFrontier().finished(currentTask);
				}
				if(this.controller.getFrontier().isEmpty() && !checkContinue()){
					break;
				}
				log.info("======================任务子线程："+Thread.currentThread().getName() + "休眠："+this.controller.getSleepTime()+"毫秒");
				Thread.sleep(this.controller.getSleepTime());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			log.info("======================任务子线程："+Thread.currentThread().getName() + "结束.");
			latch.countDown();
		}
	}
	
	/**
	 * 处理任务
	 * @param currentTask
	 */
	
	@SuppressWarnings("unchecked")
	private void processorTask(Task currentTask) {
		if(null == currentTask){
			return;
		}
		currentTask.setNextProcessorChain(this.controller.getFirstProcessorChain());
		//依次执行处理链
		while (currentTask.getNextProcessorChain() != null  && !Thread.interrupted()) {
			currentTask.setNextProcessor(currentTask.getNextProcessorChain().getFirstProcessor());
			currentTask.setNextProcessorChain(currentTask.getNextProcessorChain().getNextProcessorChain());
			//依次执行每个处理器
			while (currentTask.getNextProcessor() != null  && !Thread.interrupted()) {
				Processor currentProcessor = currentTask.getNextProcessor();
				currentProcessor.process(currentTask);
			}
		}
	}
	/**
	 * 检查爬虫状态是否为运行中
	 * @return
	 */
	private boolean checkContinue(){
		return Constant.TASK_STATE_RUNNING.equals(this.controller.getState());
	}

}
