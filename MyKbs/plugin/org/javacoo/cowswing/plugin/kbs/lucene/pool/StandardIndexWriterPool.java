/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.lucene.pool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.LogDocMergePolicy;
import org.apache.lucene.index.LogMergePolicy;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.exception.PoolException;
import org.javacoo.cowswing.plugin.kbs.lucene.index.IndexConfigration;
import org.javacoo.cowswing.plugin.kbs.utils.IndexResourceUtil;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * IndexWriter对象池接口 实现类
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-3 上午9:53:10
 * @version 1.0
 */
@Component("indexWriterPool")
public class StandardIndexWriterPool implements IndexWriterPool{
	/** 日志对象 */
	private final static Log logger = LogFactory.getLog(StandardIndexWriterPool.class);
	/** 索引配置参数 */
	@Resource(name = "indexConfigration")
	private IndexConfigration indexConfigration;
	/** 分词器 */
	//@Resource(name = "analyzer")
	private Analyzer analyzer = new IKAnalyzer();
    /**索引根目录*/
	private String indexRootDirectory = KbsConstant.SYSTEM_ROOT_PATH;
	/**索引根目录*/
	private String name = "IndexWriterPool";
	private Map<String, IndexWriter> indexWriterMap = new ConcurrentHashMap<String, IndexWriter>();   
	  
     @PostConstruct
     public void init() {   
    	 logger.info(getName()+"开始初始化");   
         synchronized (indexWriterMap) {   
             for (String indexDirName : IndexResourceUtil.getIndexDirs(indexConfigration.getIndexDir())) {   
                 indexWriterMap.put(indexDirName, createIndexWriter(indexDirName));  
                 logger.info("初始化索引目录:"+indexDirName);   
             }   
         }   
         logger.info(getName()+"初始化完成!");   
     }   
   
       
     public IndexWriter getIndexWriter(String indexDirName) {   
         if (!indexWriterMap.containsKey(indexDirName)) {   
             synchronized (indexWriterMap) {   
                 if (!indexWriterMap.containsKey(indexDirName)) {   
                     indexWriterMap.put(indexDirName, createIndexWriter(indexDirName));   
                     logger.info("索引目录"+indexDirName+",添加新 IndexWriter 对象到池中,大小是："+size());   
                 }   
             }   
         }   
         return indexWriterMap.get(indexDirName);   
     }   
   
       
     private IndexWriter createIndexWriter(String indexDirName) {   
         final String indexDirPath = getIndexDirPath(indexDirName);  
         logger.info("索引创建路径："+indexDirPath);  
         boolean create = IndexResourceUtil.isEmptyIndexDir(new File(indexDirPath));   
         try {   
        	 IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_43, analyzer);
        	 Directory dir = FSDirectory.open(new File(indexDirPath));
        	 LogMergePolicy mergePolicy = new LogDocMergePolicy();
             //索引基本配置
             //设置segment添加文档(Document)时的合并频率
             //值较小,建立索引的速度就较慢
             //值较大,建立索引的速度就较快,>10适合批量建立索引
             mergePolicy.setMergeFactor(KbsConstant.MERGE_FACTOR);
             //设置segment最大合并文档(Document)数
             //值较小有利于追加索引的速度
             //值较大,适合批量建立索引和更快的搜索
             mergePolicy.setMaxMergeDocs(KbsConstant.MAX_MERGE_DOCS);
             //启用复合式索引文件格式,合并多个segment
             mergePolicy.setUseCompoundFile(true);
             iwc.setMaxBufferedDocs(10000);
             iwc.setMergePolicy(mergePolicy);
             iwc.setRAMBufferSizeMB(50);
             ///设置索引的打开模式 创建或者添加索引
             iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
             //如果索引文件被锁，解锁索引文件
             if(IndexWriter.isLocked(dir)){  
                 IndexWriter.unlock(dir);  
             }
        	 if (create) {
        	     iwc.setOpenMode(OpenMode.CREATE);
        	 } else {
        	    iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
        	 }
        	 IndexWriter indexWriter = new IndexWriter(dir, iwc);  
        	//最开始创建索引时必须先提交，不然引起读取方法报错
        	 indexWriter.commit();
             return indexWriter;   
         } catch (Exception e) {   
             throw new PoolException(e.getMessage());   
         }   
     }   
       
     public void commit() {   
    	 logger.info("开始提交池中所有索引,当前池大小："+size());   
         synchronized (indexWriterMap) {   
             Iterator<Entry<String, IndexWriter>> iterator = indexWriterMap.entrySet().iterator();   
             while (iterator.hasNext()) {   
                 Entry<String, IndexWriter> entry = iterator.next();   
                 IndexWriter indexWriter = entry.getValue();   
                 try {   
                     indexWriter.commit();   
                 } catch (Exception e) {   
                	 logger.error("提交所有失败，索引键："+entry.getKey()+",错误描述："+ e.getMessage());   
                     destoryIndexWriter(iterator, indexWriter);   
                 }   
             }   
         }   
         logger.info("索引提交完成,当前大小："+size());   
     }   
   
       
     public void optimize() {   
    	 logger.info("开始索引优化:"+System.currentTimeMillis());   
         synchronized (indexWriterMap) {   
             Iterator<Entry<String, IndexWriter>> iterator = indexWriterMap.entrySet().iterator();   
             while (iterator.hasNext()) {   
                 Entry<String, IndexWriter> entry = iterator.next();   
                 IndexWriter indexWriter = entry.getValue();   
                 try {   
                     indexWriter.commit();   
                     //indexWriter.optimize(KbsConstant.DEFAULT_MAX_NUM_SEGMENTS);   
                 } catch (Exception e) {   
                	 logger.error("索引优化失败，索引键："+entry.getKey()+",错误描述："+ e.getMessage());   
                     destoryIndexWriter(iterator, indexWriter);   
                 }   
             }   
         }   
         logger.info("索引优化结束："+System.currentTimeMillis());   
     }   
   
       
     public void reload() {   
    	 logger.info("重新加载索引开始:"+System.currentTimeMillis());   
         // 需要重新加载的索引目录列表   
         List<String> indexDirNameList = new ArrayList<String>();   
         synchronized (indexWriterMap) {   
             Iterator<Entry<String, IndexWriter>> iterator = indexWriterMap.entrySet().iterator();   
             while (iterator.hasNext()) {   
                 Entry<String, IndexWriter> entry = iterator.next();   
                 indexDirNameList.add(entry.getKey());   
                 IndexWriter indexWriter = entry.getValue();   
                 try {   
                     indexWriter.commit();   
                 } catch (Exception e) {   
                	 logger.error("重新加载索引失败，索引键："+entry.getKey()+",错误描述："+ e.getMessage());   
                 } finally {   
                     destoryIndexWriter(iterator, indexWriter);   
                 }   
             }   
   
             for (String indexDirName : indexDirNameList) {   
                 indexWriterMap.put(indexDirName, createIndexWriter(indexDirName));   
             }   
         }   
         logger.info("重新加载索引完成:"+System.currentTimeMillis());    
     }   
       
     private void destoryIndexWriter(Iterator<Entry<String, IndexWriter>> iterator, IndexWriter indexWriter) {   
         try {   
             indexWriter.close();   
         } catch (CorruptIndexException e) {   
        	 logger.error("销毁索引失败，错误描述："+ e.getMessage());   
         } catch (IOException e) {  
        	 logger.error("销毁索引失败，错误描述："+ e.getMessage());   
         }   
         iterator.remove();   
         logger.info("销毁索引成功,当前池大小:"+size());   
     }   
     @PreDestroy
     public void destroy() {   
         synchronized (indexWriterMap) {   
             Iterator<Entry<String, IndexWriter>> iterator = indexWriterMap.entrySet().iterator();   
             while (iterator.hasNext()) {   
                 Entry<String, IndexWriter> entry = iterator.next();   
                 IndexWriter indexWriter = entry.getValue();   
                 try {   
                     indexWriter.commit();   
                     indexWriter.close();   
                 } catch (Exception e) {   
                	 logger.error("销毁索引失败，索引键："+entry.getKey()+",错误描述："+ e.getMessage());   
                     destoryIndexWriter(iterator, indexWriter);   
                 }   
             }   
             indexWriterMap = null;   
             logger.info("销毁索引完成"+getName());   
         }   
     }   
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
     public void destroy(String indexDirName) {   
         synchronized (indexWriterMap) {  
        	 if(StringUtils.isBlank(indexDirName) || !indexWriterMap.containsKey(indexDirName)){
        		 return;
        	 }
        	 IndexWriter indexWriter = indexWriterMap.get(indexDirName);
             try {   
            	 indexWriter = indexWriterMap.get(indexDirName);
            	 indexWriter.deleteAll();
                 indexWriter.commit();   
                 indexWriter.close(); 
                 indexWriterMap.remove(indexDirName);
             } catch (Exception e) {   
            	 e.printStackTrace();
            	 indexWriterMap.remove(indexDirName);
            	 logger.error("销毁索引失败，索引目录："+indexDirName+",错误描述："+ e.getMessage());   
             }   
             logger.info("销毁定索引目录完成:"+indexDirName);   
         }   
     }   
   
     private String getIndexDirPath(String indexDirName) {   
         return (new StringBuffer(indexConfigration.getIndexDir()).append(File.separatorChar).append(indexDirName)).toString();   
     }   
   
     public int size() {   
         return this.indexWriterMap.size();   
     }   
   
     public String getName() {   
         return name;   
     }   
   
     @Override  
     public String toString() {   
         ToStringBuilder builder = new ToStringBuilder(this);   
         builder.append("name", this.name);   
         builder.append("indexRootDirectory", this.indexRootDirectory);   
         builder.append("size", this.size());   
         builder.append("IndexWriter Set", indexWriterMap.keySet());   
         return builder.toString();   
     }   
   
       
     public void setName(String name) {   
         this.name = name;   
     }   
     public void setIndexRootDirectory(String indexRootDirectory) {   
         this.indexRootDirectory = indexRootDirectory;   
     }   
     
     public static void main(String[] args) throws IOException{
    	 IndexWriterPool indexWriterPool = new StandardIndexWriterPool();
    	 indexWriterPool.init();
 	}
     
}
