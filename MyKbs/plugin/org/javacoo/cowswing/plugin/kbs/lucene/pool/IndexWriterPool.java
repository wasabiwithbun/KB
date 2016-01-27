/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.lucene.pool;

import org.apache.lucene.index.IndexWriter;

/**
 * IndexWriter对象池接口
 * <p>说明:</p>
 * <li>IndexWriter对象池接口</li>
 * @author DuanYong
 * @since 2013-7-3 上午9:53:39
 * @version 1.0
 */
public interface IndexWriterPool {
	/**
	 * 初始化
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-3 上午9:54:12
	 * @version 1.0
	 * @exception
	 */
	void init();

	/**
	 * 返回一个indexWriter
	 * <p>方法说明:</>
	 * <li>返回一个indexWriter,indexWriter是线程安全的,允许多个线程同时使用ndexWriter。但一个索引目录只能初始化一个IndexWriter</li>
	 * @author DuanYong
	 * @since 2013-7-3 上午9:54:38
	 * @version 1.0
	 * @exception 
	 * @param indexDirName 索引目录
	 * @return IndexWriter
	 */
	IndexWriter getIndexWriter(String indexDirName);

	/**
	 * 提交索引，只有提交的索引才能被检索的到
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-3 上午9:56:14
	 * @version 1.0
	 * @exception
	 */
	void commit();

	/**
	 * 优化索引，提升检索速度
	 * <p>方法说明:</>
	 * <li>优化索引，提升检索速度</li>
	 * @author DuanYong
	 * @since 2013-7-3 上午9:56:07
	 * @version 1.0
	 * @exception
	 */
	void optimize();

	/**
	 * 重新加载所有的IndexWriter
	 * <p>方法说明:</>
	 * <li>重新加载所有的IndexWriter, IndexWriter不会及时释放哪些在创建索引过程中产生的索引文件碎片，哪怕哪些索引文件已经消失reload()就是为了释放哪些文件句柄，防止进程持有过多的文件句柄</li>
	 * @author DuanYong
	 * @since 2013-7-3 上午9:56:47
	 * @version 1.0
	 * @exception
	 */
	void reload();

    /**
     * 销毁，释放持有的资源。
     * <p>方法说明:</>
     * <li></li>
     * @author DuanYong
     * @since 2013-7-3 上午9:57:27
     * @version 1.0
     * @exception
     */
	void destroy();
	/**
	 * 销毁，释放指定索引目录的资源。
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-27 上午10:01:47
	 * @version 1.0
	 * @exception 
	 * @param indexDirName
	 */
	void destroy(String indexDirName);
}
