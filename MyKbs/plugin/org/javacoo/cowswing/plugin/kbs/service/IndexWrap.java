/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.service;

import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleArticleBean;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleSourceBean;

/**
 * 索引管理包装类接口
 * <p>说明:</p>
 * <li>文章，资源索引包装类</li>
 * @author DuanYong
 * @since 2013-9-28 上午11:11:42
 * @version 1.0
 */
public interface IndexWrap {
	/**
	 * 构建文章索引
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-28 上午11:14:44
	 * @version 1.0
	 * @exception 
	 * @param kbsModuleArticleBean
	 */
	void buildArticleIndex(KbsModuleArticleBean kbsModuleArticleBean,boolean commit);
	/**
	 * 更新文章索引
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-28 上午11:28:48
	 * @version 1.0
	 * @exception 
	 * @param kbsModuleArticleBean
	 */
	void updateArticleIndex(KbsModuleArticleBean kbsModuleArticleBean);
	/**
	 * 构建资源索引
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-28 上午11:15:00
	 * @version 1.0
	 * @exception 
	 * @param kbsModuleSourceBean
	 */
	void buildSourceIndex(KbsModuleSourceBean kbsModuleSourceBean,boolean commit);
	/**
	 * 重建文章索引
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-28 上午11:16:38
	 * @version 1.0
	 * @exception 
	 */
	void reBuildArticleIndex();
	/**
	 * 重建资源索引
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-28 上午11:17:18
	 * @version 1.0
	 * @exception
	 */
	void reBuildSourceIndex();
	/**
	 * 重建索引索引
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-28 下午12:17:05
	 * @version 1.0
	 * @exception
	 */
	void reBuildAllIndex();
}
