/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.lucene.pool;

import org.apache.lucene.index.IndexReader;

/**
 * IndexReader对象池接口
 * <p>说明:</p>
 * <li>IndexReader对象池接口</li>
 * @author DuanYong
 * @since 2013-7-2 下午4:17:54
 * @version 1.0
 */
public interface IndexReaderPool {
	/**
	 * 初始化IndexReader池
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-2 下午4:20:28
	 * @version 1.0
	 * @exception PoolException
	 */
	void init();
	/**
	 * 根据索引目录名称得到IndexReader
	 * <p>方法说明:</>
	 * <li>根据索引目录名称得到IndexReader</li>
	 * @author DuanYong
	 * @since 2013-7-2 下午4:20:35
	 * @version 1.0
	 * @exception  PoolException
	 * @param indexDirName 索引目录名称
	 * @return IndexReader
	 */
	IndexReader getIndexReader(String indexDirName);
	/**
	 * 销毁IndexReader
	 * <p>方法说明:</>
	 * <li>销毁IndexReader</li>
	 * @author DuanYong
	 * @since 2013-7-2 下午4:24:23
	 * @version 1.0
	 * @exception
	 */
	void destroy();
    /**
     * 得到IndexReader池的大小
     * <p>方法说明:</>
     * <li></li>
     * @author DuanYong
     * @since 2013-7-2 下午4:24:40
     * @version 1.0
     * @exception 
     * @return
     */
	int size();
}
