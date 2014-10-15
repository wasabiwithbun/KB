package org.javacoo.cowswing.core.task.processor;

import org.javacoo.cowswing.core.task.data.Task;


/**
 *  任务处理器接口
 * 
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-7-24 下午2:17:33
 * @version 1.0
 * @param <T> 任务对象
 */
public interface Processor<T> {
	/**
	 * 处理任务
	 * @param task 任务
	 */
	void process(Task<T> task);
}
