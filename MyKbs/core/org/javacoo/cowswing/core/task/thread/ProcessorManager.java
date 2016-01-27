package org.javacoo.cowswing.core.task.thread;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javacoo.cowswing.base.constant.Constant;
import org.javacoo.cowswing.core.event.CowSwingEvent;
import org.javacoo.cowswing.core.event.CowSwingTaskObserver;
import org.javacoo.cowswing.core.event.type.CowSwingTaskEventType;
import org.javacoo.cowswing.core.task.TaskManagerService;
/**
 * 线程池-开启指定数目的线程执行任务
 * @author javacoo
 * @since 2011-11-10
 */
@SuppressWarnings("rawtypes")
public class ProcessorManager implements Runnable{
	private static Log log =  LogFactory.getLog(ProcessorManager.class);
	/** 任务控制器 */
	private TaskManagerService controller;
	/** 线程池服务类 */
	private ThreadPoolService threadPoolService = new ThreadPoolService(Constant.DEFAULT_POOL_SIZE);
	
	public ProcessorManager(TaskManagerService c) {
		super();
		this.controller = c;
	}
	
	public void run() {
		long tStart = System.currentTimeMillis();
		log.info("主线程："+Thread.currentThread().getName() + "开始...");
		log.info("开启："+Constant.DEFAULT_POOL_SIZE+ "个线程执行任务");
		try {
			CountDownLatch latch = new CountDownLatch(Constant.DEFAULT_POOL_SIZE);
			//开启指定数目线程执行采集内容
			for(int i=0;i<Constant.DEFAULT_POOL_SIZE;i++){
				threadPoolService.execute(new ProcessorRunnableThread(controller,latch));
			}
			latch.await();
			threadPoolService.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally{
			this.controller.shutdown();
			long tEnd = System.currentTimeMillis();
			log.info("主线程："+Thread.currentThread().getName() + "结束...");
			log.info("主线程："+Thread.currentThread().getName() + "总共用时:" + (tEnd - tStart) + "ms");
			Map<String,Object> monitorInfo = new HashMap<String,Object>();
			monitorInfo.put(Constant.TASK_MONITOR_TOTAL, 100);
			monitorInfo.put(Constant.TASK_MONITOR_NUM, 100);
			monitorInfo.put(Constant.TASK_MONITOR_DESC, "任务执行完毕,总共用时:" + (tEnd - tStart) + "ms");
			CowSwingTaskObserver.getInstance().notifyEvents(new CowSwingEvent(this,CowSwingTaskEventType.TaskTypeEndEvent,monitorInfo));
		}
	}
	/**
	 * 取得当前爬虫控制器
	 * @return 当前爬虫控制器
	 */
	public TaskManagerService getController() {
		return controller;
	}
	/**
	 * 立即停止执行所有任务
	 */
	public void shutdownNow(){
		threadPoolService.shutdownNow();
	}
	/**
	 * 取得当前爬虫线程池服务类
	 * @return 当前爬线程池服务类
	 */
	public ThreadPoolService getThreadPoolService() {
		return threadPoolService;
	}

	public void setThreadPoolService(ThreadPoolService threadPoolService) {
		this.threadPoolService = threadPoolService;
	} 
	
}
