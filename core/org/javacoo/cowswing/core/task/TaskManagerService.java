/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.core.task;

import org.javacoo.cowswing.core.task.frontier.Frontier;
import org.javacoo.cowswing.core.task.processor.ProcessorChain;
import org.javacoo.cowswing.core.task.thread.ProcessorManager;

/**
 * 任务处理服务类
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-7-24 上午10:21:32
 * @version 1.0
 */
public interface TaskManagerService<T> {
	/**
	 * 取得第一个处理链
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-7-24 上午10:46:25
	 * @version 1.0
	 * @exception 
	 * @return
	 */
	ProcessorChain<T> getFirstProcessorChain();
	/**
	 * 取得当前状态
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-7-24 上午10:46:44
	 * @version 1.0
	 * @exception 
	 * @return
	 */
	String getState();
	/**
	 * 设置当前状态
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-7-26 下午1:04:47
	 * @version 1.0
	 * @exception 
	 * @param state
	 */
	void setState(String state);
	/**
	 * 取得边界控制器
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-7-24 上午10:46:56
	 * @version 1.0
	 * @exception 
	 * @return
	 */
	Frontier<T> getFrontier();
	/**
	 * 取得休眠时间
	 * <p>方法说明:</>
	 * <li>毫秒</li>
	 * @author DuanYong
	 * @since 2014-7-24 上午10:52:06
	 * @version 1.0
	 * @exception 
	 * @return
	 */
	long getSleepTime();
	/**
	 * 取得线程控制器
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-7-24 上午10:54:11
	 * @version 1.0
	 * @exception 
	 * @return
	 */
	ProcessorManager getProcessorManager();
	/**
	 * 停止任务
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-7-24 上午10:55:56
	 * @version 1.0
	 * @exception
	 */
	void shutdown();
	/**
	 * 开始任务
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-7-24 下午3:32:31
	 * @version 1.0
	 * @exception 
	 * @param t 任务对象
	 */
	void start(T t);
}
