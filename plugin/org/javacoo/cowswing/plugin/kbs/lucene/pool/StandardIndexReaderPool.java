/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.lucene.pool;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.exception.PoolException;
import org.javacoo.cowswing.plugin.kbs.lucene.index.IndexConfigration;
import org.javacoo.cowswing.plugin.kbs.utils.IndexResourceUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * IndexReader对象池接口实现类
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-2 下午4:26:40
 * @version 1.0
 */
@Component("indexReaderPool")
public class StandardIndexReaderPool implements IndexReaderPool{
	/** 日志对象 */
	private final static Log logger = LogFactory.getLog(StandardIndexReaderPool.class);
	/** 索引配置参数 */
	@Resource(name = "indexConfigration")
	private IndexConfigration indexConfigration;
	private String name = "IndexReaderPool";
	/**
	 * 存放IndexReader的Map，Map里存放的都是已经实例化好的IndexReader
	 */
	private Map<String, IndexReader> indexReaderMap = new ConcurrentHashMap<String, IndexReader>();
	/**
	 * 待关闭的IndexReader。indexReader.reopen()之后，会产生新的IndexReader。
	 * 但是旧的IndexReader有可能还被其他线程调用着。
	 * 旧的IndexReader都要放置到staleIndexReadersMap里，5秒之后再释放资源。
	 */
	private Map<Long, IndexReader> staleIndexReadersMap = new ConcurrentHashMap<Long, IndexReader>();
	/**异常编码*/
	private final static String EXCEPTION_CODE = "INDEX_READER_POOL";
	
	/**
	 * 初始化IndexReader池
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-2 下午4:20:28
	 * @version 1.0
	 * @exception PoolException
	 */
	@PostConstruct
	public void init() {
		logger.info(getName() + " 开始初始化IndexReader对象池");
		for (String indexDirName : IndexResourceUtil.getIndexDirs(indexConfigration.getIndexDir())) {
			try {
				IndexReader indexReader = createIndexReader(indexDirName);
				if (indexReader != null)
					indexReaderMap.put(indexDirName, indexReader);
			} catch (IOException e) {// 若初始化时出错，就直接抛错，终止程序再执行下去
				e.printStackTrace();
				//throw new PoolException(EXCEPTION_CODE,e.getMessage());
			}
		}
		logger.info(getName() + " 初始化IndexReader对象池完成");
	}

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
	public IndexReader getIndexReader(String indexDirName) {
		Assert.hasText(indexDirName, "索引目录名称不能为空!");   
		  
        IndexReader indexReader = indexReaderMap.get(indexDirName);   
        if (indexReader != null){
        	return refreshIndexReader(indexDirName, (DirectoryReader) indexReader);  
        }   
        synchronized (indexReaderMap) {   
            if (!indexReaderMap.containsKey(indexDirName)) {   
                try {   
                    indexReader = createIndexReader(indexDirName);   
                } catch (CorruptIndexException e) {   
                	logger.error(indexDirName + ",创建IndexReader失败:"+e.getMessage());   
                } catch (IOException e) {   
                	logger.error(indexDirName+",创建IndexReader失败:"+ e.getMessage());   
                }   
                if (indexReader != null)   
                    indexReaderMap.put(indexDirName, indexReader);   
            }   
        }   
        return indexReaderMap.get(indexDirName); 
	}

	/**
	 * 刷新指定的indexReader--加载新的索引数据,若产生新的indexReader，
	 * 则在indexReaderMap里替换旧的indexReader
	 * 
	 * @param indexDirName
	 * @param indexReader
	 * @return {@link IndexReader}
	 */
	private synchronized IndexReader refreshIndexReader(String indexDirName, DirectoryReader oldReader) {
		 try {   
	            closeStaleIndexReaders(staleIndexReadersMap);   
	            logger.info("老IndexReader对象的hashCode值是 "+ oldReader.hashCode());  
	            IndexReader newIndexReader = DirectoryReader.openIfChanged(oldReader);
	            if(null != newIndexReader){  
	                staleIndexReadersMap.put(System.currentTimeMillis(), oldReader);   
	                logger.info("老 IndexReader对象的hashCode值是:"+ oldReader.hashCode());   
	                // replace old version IndexReader with newIndexReader   
	                indexReaderMap.put(indexDirName, newIndexReader);   
	                logger.info("新IndexReader对象的hashCode值是:"+newIndexReader.hashCode());   
	            } 
	        } catch (Exception e) {   
	        	logger.info("刷新IndexReader异常:"+e.getMessage());   
	        }   
	        // return newest IndexReader   
	        return indexReaderMap.get(indexDirName);  
	}

	/**
	 * 关闭所有低版本的IndexReaders
	 * 
	 * @param staleIndexReadersMap
	 */
	private void closeStaleIndexReaders(Map<Long, IndexReader> staleIndexReadersMap) {
		 Iterator<Entry<Long, IndexReader>> entryIterator = staleIndexReadersMap.entrySet().iterator();   
	        while (entryIterator.hasNext()) {   
	            Entry<Long, IndexReader> entry = entryIterator.next();   
	            if ((System.currentTimeMillis() - entry.getKey()) >= KbsConstant.STALE_INDEXREADER_SURVIVAL_TIME) {   
	                try {   
	                    entry.getValue().close();   
	                    logger.info("hashCode值为："+entry.getValue().hashCode()+"的 IndexReader 对象已经被关闭!");   
	                } catch (IOException e) {   
	                	logger.error("关闭 IndexReader对象发生IOException 异常："+e.getMessage());   
	                } finally {   
	                    entryIterator.remove();   
	                    logger.info("已经将hashCode值为:" + entry.getValue().hashCode()+"的IndexReader对象从池中删除!");   
	                }   
	            }   
	        }   
	}
	@PreDestroy
	public void destroy() {
		 Iterator<Entry<String, IndexReader>> iterator = indexReaderMap.entrySet().iterator();   
	        while (iterator.hasNext()) {   
	            Entry<String, IndexReader> entry = iterator.next();   
	            IndexReader indexReader = entry.getValue();   
	            try {   
	                indexReader.close();   
	                indexReader = null;   
	            } catch (IOException e) {   
	            	logger.info("销毁 索引目录名称为："+entry.getKey()+" 的  IndexReader 对象发生IOException异常! ");   
	            }   
	        }   
	        indexReaderMap.clear();   
	        logger.info(getName()+" 已经被销毁!");    
	}

	
	/**
	 * 根据索引目录名实例化{@link IndexReader},有可能返回null，调用者需要判断返回的{@link IndexReader}
	 * 是否为null
	 * 
	 * @param indexDirName
	 * @return {@link IndexReader}
	 *         返回indexDirName对应的IndexReader，如果对应的目录不存在就返回null，
	 */
	private IndexReader createIndexReader(String indexDirName)
			throws CorruptIndexException, IOException {
		File indexFile = new File(IndexResourceUtil.getDirPath(indexConfigration.getIndexDir(), indexDirName));
		if (IndexResourceUtil.isEmptyIndexDir(indexFile)) {
			logger.warn("警告!!索引目录:" + indexDirName + "是为空,没有索引文件!");
			return null;
		}
		if (indexFile.exists() && indexFile.isDirectory()) {// 判断索引目录是否存在。
			return DirectoryReader.open(FSDirectory.open(indexFile));
		}
		return null;
	}

	public int size() {
		return indexReaderMap.size(); 
	}

	@Override
	public String toString() {
		return (new StringBuilder().append("name").append(getName()).append(
				"indexRootDirectory").append("size").append(size()).append(
				"indexReader Set").append(indexReaderMap.keySet())).toString();
	}

	public String getName() {
		return name;
	}
	

	public void setName(String name) {
		this.name = name;
	}

	

	public void setIndexConfigration(IndexConfigration indexConfigration) {
		this.indexConfigration = indexConfigration;
	}

}
