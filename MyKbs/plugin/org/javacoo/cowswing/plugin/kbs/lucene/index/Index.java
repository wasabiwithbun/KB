/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.lucene.index;

import org.javacoo.cowswing.plugin.kbs.domain.KnowledgePoint;
import org.javacoo.cowswing.plugin.kbs.exception.IndexException;



/**
 * 索引管理接口
 * <p>说明:</p>
 * <li>定义管理索引的方法</li>
 * @author DuanYong
 * @since 2013-6-30 下午12:12:01
 * @version 1.0
 */
public interface Index {
	/**
	 * 构建索引
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-6-30 下午12:43:02
	 * @version 1.0
	 * @exception 
	 * @param knowledgePoint 知识点
	 * @param commit 是否立即提交
	 * @throws IndexException
	 */
	void buildIndex(KnowledgePoint knowledgePoint,boolean commit) throws IndexException;
	/**
	 * 更新索引
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-6-30 下午12:44:22
	 * @version 1.0
	 * @exception 
	 * @param knowledgePoint 知识点
	 * @throws Exception
	 */
	void updateIndex(KnowledgePoint knowledgePoint) throws Exception;
	/**
	 * 根据索引目录和模块删除索引
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-28 上午11:42:41
	 * @version 1.0
	 * @exception 
	 * @param indexDirName
	 * @param module
	 * @throws Exception
	 */
	void deleteIndex(String indexDirName,String module) throws IndexException;
	/**
	 * 删除索引
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-6-30 下午4:08:11
	 * @version 1.0
	 * @exception 
	 * @param knowledgePoint 知识点
	 * @throws Exception
	 */
	void deleteIndex(KnowledgePoint knowledgePoint) throws Exception;
	/**
	 * 优化索引
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-6-30 下午4:09:20
	 * @version 1.0
	 * @exception
	 */
	void optimizeIndexByTimmer();
}
