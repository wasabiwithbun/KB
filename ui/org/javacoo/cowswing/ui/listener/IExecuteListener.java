/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.ui.listener;

/**
 * 执行任务监听接口
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-10-12 下午5:11:00
 * @version 1.0
 */
public interface IExecuteListener {
	/**
	 * 执行任务名称
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-10-12 下午5:30:11
	 * @version 1.0
	 * @exception 
	 * @param executeName
	 */
	void executed(String executeName);
}
