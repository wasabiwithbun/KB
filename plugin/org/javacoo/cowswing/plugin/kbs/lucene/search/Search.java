/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.lucene.search;

import java.util.List;

import org.apache.lucene.document.Document;
import org.javacoo.cowswing.plugin.kbs.domain.KnowledgePoint;
import org.javacoo.cowswing.plugin.kbs.service.beans.SearchCriteria;
import org.javacoo.persistence.PaginationSupport;

/**
 * 文档搜索接口
 * <p>说明:</p>
 * <li>定义文档搜索的方法</li>
 * @author DuanYong
 * @since 2013-7-8 上午9:43:16
 * @version 1.0
 */
public interface Search {
	/**
	 * 根据搜索条件搜索文档
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-8 上午9:45:20
	 * @version 1.0
	 * @exception 
	 * @param criteria
	 * @return
	 */
	PaginationSupport<KnowledgePoint> find(SearchCriteria criteria);
	/**
	 * 根据知识点参数查询文档
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-8 上午9:47:12
	 * @version 1.0
	 * @exception 
	 * @param knowledgePoint
	 * @return
	 * @throws Exception
	 */
	Document findKnowledgePoint(KnowledgePoint knowledgePoint) throws Exception;
	/**
	 * 根据模块和主键条件搜索相关资源
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-8 上午9:47:22
	 * @version 1.0
	 * @exception 
	 * @param criteria
	 * @return
	 * @throws Exception
	 */
	List<KnowledgePoint> findCorrelative(SearchCriteria criteria) throws Exception;

}
