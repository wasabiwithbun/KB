package org.javacoo.cowswing.core.task.frontier;

import org.javacoo.cowswing.core.task.TaskManagerService;
import org.javacoo.cowswing.core.task.data.Task;

/**
 *  边界控制器  接口
 * 主要是初始化任务队列，以备线程控制器（ProcessorThreadPool）开启的任务执行线程（ProcessorThread）使用
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-7-24 上午10:35:30
 * @version 1.0
 */
public interface Frontier<T> {
	/**
	 * 初始化
	 * <li>初始化任务队列</li>
	 * @param s 任务服务对象
	 * @param t 任务对象
	 */
	void initialize(TaskManagerService<T> s,T root);
	/**
	 * 取得下一个任务
	 * @return 下一个任务
	 */
	Task<T> next();
	/**
	 * 添加任务队列
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-7-26 上午11:11:10
	 * @version 1.0
	 * @exception 
	 * @param task
	 */
	void addTask(Task<T> task);
	/**
	 * 是否为空
	 * @return
	 */
	boolean isEmpty();
	/**
	 * 完成任务
	 * @param task 任务
	 */
	void finished(Task<T> task);
	/**
	 * 取得任务总数
	 * @return 任务总数
	 */
   int getTaskSize();
   /**
	 * 取得未执行任务总数
	 * @return 未执行任务总数
	 */
   int getUnExecTaskNum();
   /**
	 * 取得已执行任务总数
	 * @return 已执行任务总数
	 */
   int getExecTaskNum();
   /**
	 * 销毁对象
	 */
	void destory();

}
