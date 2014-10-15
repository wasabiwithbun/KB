/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.view.panel;

/**
 * 
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-9-23 上午11:14:42
 * @version 1.0
 */
public interface ITypeTree {
	/**
	 * 展开或者折叠所有节点
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-23 上午11:16:15
	 * @version 1.0
	 * @exception 
	 * @param expandAll true 为展开所有,false为折叠所有
	 */
	void expandTree(boolean expandAll);
	/**
	 * 初始化方法
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-23 上午11:16:51
	 * @version 1.0
	 * @exception
	 */
	void init();
}
