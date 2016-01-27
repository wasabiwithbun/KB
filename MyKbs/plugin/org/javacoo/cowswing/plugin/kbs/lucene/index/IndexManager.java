/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.lucene.index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.javacoo.cowswing.plugin.kbs.constant.IndexConfig;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.domain.KnowledgePoint;
import org.javacoo.cowswing.plugin.kbs.exception.IndexException;
import org.javacoo.cowswing.plugin.kbs.exception.ParserException;
import org.javacoo.cowswing.plugin.kbs.lucene.pool.IndexWriterPool;
import org.javacoo.cowswing.plugin.kbs.parser.DocumentParserFactory;
import org.javacoo.cowswing.plugin.kbs.parser.document.DocumentParser;
import org.javacoo.cowswing.plugin.kbs.utils.IndexResourceUtil;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * 索引管理接口实现类
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-2 下午4:01:59
 * @version 1.0
 */
@Component("indexManager")
public class IndexManager implements Index{
	/** 日志对象 */
	private final static Log logger = LogFactory.getLog(IndexManager.class);
	/** 索引配置参数 */
	@Resource(name = "indexConfigration")
	private IndexConfigration indexConfigration;
	/** 分词器 */
	//@Resource(name = "analyzer")
	private Analyzer analyzer = new IKAnalyzer();
	/** IndexWriter对象池 */
	@Resource(name = "indexWriterPool")
	private IndexWriterPool indexWriterPool;
	/** 文档解析器工厂 */
	@Resource(name = "parserFactory")
	private DocumentParserFactory parserFactory;
	/** 不同目录 知识点列表map 主要用于错误日志 */
	private Map<String, ArrayList<KnowledgePoint>> tempKnowledgePointListMap = new ConcurrentHashMap<String, ArrayList<KnowledgePoint>>();
	/** 不同索引目录存放内存索引的RAMDirectory MAP */
	private Map<String, RAMDirectory> ramDirectoryMap = new ConcurrentHashMap<String,RAMDirectory>();
	/** 不同索引目录 存储于内存的IndexWriter MAP */
	private Map<String, IndexWriter> ramWriterMap = new ConcurrentHashMap<String,IndexWriter>();
	/** 锁对象 */
	public static Object lock = new Object();
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
	 * @throws Exception
	 */
	@Override
	public void buildIndex(KnowledgePoint knowledgePoint,boolean commit) throws IndexException {
		synchronized (lock) {
			if (null != knowledgePoint) {
				saveIndexToMemory(knowledgePoint,commit);
				optimizeIndex(knowledgePoint.getIndexDirName());
			} else {
				try {
					saveIndexToDisk();
				} catch (Exception e) {
					logger.error("创建索引失败 :"+e.getMessage());
					throw new IndexException("创建索引失败 :" + e.getMessage());
				}
			}
		}
	}

	/**
	 * 索引文件数量达到设置数量后优化索引
	 * 
	 */
	private void optimizeIndex(String indexDirName) {
		if(StringUtils.isBlank(indexDirName)){
			return;
		}
		logger.info("开始优化索引");
		try {
			IndexWriter indexWriter = indexWriterPool.getIndexWriter(indexDirName);
			indexWriter.deleteUnusedFiles();
		} catch (IOException e) {
			logger.error("优化索引失败:"+e.getMessage());
			throw new IndexException("优化索引失败:"+e.getMessage());
		}
	}
	

	/**
	 * 根据索引目录和模块删除索引
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-28 上午10:48:58
	 * @version 1.0
	 * @exception 
	 * @param indexDirName
	 * @param module
	 * @throws IndexException
	 */
	public void deleteIndex(String indexDirName,String module)throws IndexException {
		if(StringUtils.isBlank(indexDirName)){
			return;
		}
		synchronized (lock) {
			try {
				saveIndexToDisk(indexDirName);
				if(StringUtils.isBlank(module)){
					indexWriterPool.destroy(indexDirName);	
				}else{
					IndexWriter FSDWriter = indexWriterPool.getIndexWriter(indexDirName);
					Term moduleTerm = new Term(IndexConfig.FIELD_MODULE,module);
					FSDWriter.deleteDocuments(moduleTerm);
					FSDWriter.commit();
				}
				ramWriterMap.remove(indexDirName);
				ramDirectoryMap.remove(indexDirName);
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new IndexException("删除索引目录：" + indexDirName + " 的索引失败:"
						+ e.getMessage());
			}
		}
	}


	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.plugin.kbs.lucene.index.Index#updateIndex(org.javacoo.cowswing.plugin.kbs.domain.KnowledgePoint)
	 */
	@Override
	public void updateIndex(KnowledgePoint knowledgePoint) throws Exception {
		synchronized (lock) {
			logger.info("开始更新索引:" + knowledgePoint.getTitle());
			Document doc = getDocument(knowledgePoint);
			IndexWriter FSDWriter = indexWriterPool.getIndexWriter(knowledgePoint.getIndexDirName());
			deleteIndex(knowledgePoint);
			FSDWriter.addDocument(doc);
			FSDWriter.commit();
		}
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.plugin.kbs.lucene.index.Index#deleteIndex(org.javacoo.cowswing.plugin.kbs.domain.KnowledgePoint)
	 */
	@Override
	public void deleteIndex(KnowledgePoint knowledgePoint) throws Exception {
		synchronized (lock) {
			//根据索引目录查找IndexWriter
			IndexWriter FSDWriter = indexWriterPool.getIndexWriter(knowledgePoint.getIndexDirName());
			//组装查询条件
			BooleanQuery query = new BooleanQuery();
			//根据ID查询
			if(StringUtils.isNotBlank(knowledgePoint.getId())){
				Term idTerm = new Term(IndexConfig.FIELD_ID,knowledgePoint.getId());
				TermQuery idQuery = new TermQuery(idTerm);
				query.add(idQuery, BooleanClause.Occur.MUST);
			}
			//根据文件目录查询
			if(StringUtils.isNotBlank(knowledgePoint.getFileDirs())){
				logger.info("根据文件目录删除索引:"+knowledgePoint.getFileDirs());
				Term dirTerm = new Term(IndexConfig.FIELD_DIRECTORY,knowledgePoint.getFileDirs());
				TermQuery dirQuery = new TermQuery(dirTerm);
				query.add(dirQuery, BooleanClause.Occur.MUST);
			}
			//根据模块查询
			Term moduleTerm = new Term(IndexConfig.FIELD_MODULE,knowledgePoint.getModule());
			TermQuery muduleQuery = new TermQuery(moduleTerm);
			query.add(muduleQuery, BooleanClause.Occur.MUST);
			
			try {
				FSDWriter.deleteDocuments(query);
				FSDWriter.commit();
				logger.info("删除索引成功!");
			} catch (Exception e) {
				throw new Exception("删除索引失败:" + e.getMessage());
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.plugin.kbs.lucene.index.Index#optimizeIndexByTimmer()
	 */
	@Override
	public void optimizeIndexByTimmer() {
		// TODO Auto-generated method stub
		
	}
	
	

	/**
	 * 保存索引至内存
	 * <p>
	 * 当内存中索引达到一定大小时将其写入硬盘，建立索引
	 * 
	 * @param knowledgePoint
	 *            知识点
	 * @param commit
	 *            是否立即提交
	 * @throws Exception
	 */
	private void saveIndexToMemory(KnowledgePoint knowledgePoint,boolean commit)
			throws IndexException {
		try {
			if(KbsConstant.SYSTEM_MODULE_SOURCE.equals(knowledgePoint.getModule())){
				parserDocument(knowledgePoint);
			}
			Document document = getDocument(knowledgePoint);
			getRAMWriter(knowledgePoint.getIndexDirName()).addDocument(document);
			// 内存中索引大小
			long cuurSize = getMemorySize();
			//如果不是立即提交，并且当前内存中索引大小 小于指定大小
			//当存放索引的内存使用大于指定值时将其写入硬盘；采用此方法的目的是 通过内存缓冲避免频繁的IO操作，提高索引创建性能；
			if(!commit && (cuurSize < indexConfigration.getRamMaxSize() * 1024 * 1024)){
				logger.info("当前内存中索引大小:" + cuurSize / (1024 * 1024 * 1.0) + "M");
				logger.info("配置文件中，写入磁盘大小:" + indexConfigration.getRamMaxSize()
						+ "M");
				return;
			}
			saveIndexToDisk();
		}catch (Exception e) {
			logger.error("建立索引失败：" +e.getMessage());
			throw new IndexException("建立索引失败：" + e.getMessage());
		}
	}

	/**
	 * 得到当前内存中 索引总大小
	 * 
	 * @return 索引总大小
	 */
	private long getMemorySize() {
		long cuurSize = 0;
		for (String key : ramWriterMap.keySet()) {
			cuurSize += ramWriterMap.get(key).ramSizeInBytes();
		}
		return cuurSize;
	}



	/**
	 * 将内存中索引直接写入磁盘
	 * <p>
	 * 由于采用先写入内存，再写入磁盘的建立索引方式，当最后 内存中索引大小 小于 写入磁盘所需大小时，造成文档索引不全
	 */
	private void saveIndexToDisk() {
		logger.debug("开始提交索引");
		for (String key : ramWriterMap.keySet()) {
			if (ramWriterMap.get(key).ramSizeInBytes() > 0) {
				memoryToDisk(key);
			}
		}
		logger.debug("提交索引成功");
	}

	/**
	 * 将指定目录的内存中索引直接写入磁盘
	 * <p>
	 * 由于采用先写入内存，再写入磁盘的建立索引方式，当最后 内存中索引大小 小于 写入磁盘所需大小时，造成文档索引不全
	 */
	private void saveIndexToDisk(String indexDirName) {
		logger.debug("开始提交索引:" + indexDirName);
		if (ramWriterMap != null
				&& ramWriterMap.get(indexDirName) != null) {
			for (String ramKey : ramWriterMap.keySet()) {
				if (ramWriterMap.get(ramKey).ramSizeInBytes() > 0) {
					memoryToDisk(ramKey);
				}
			}
		}
		logger.debug("提交索引成功");
	}

	/**
	 * 将内存索引写入磁盘
	 * <p>方法说明:</>
	 * <li>当存放索引的内存使用大于0时将其写入硬盘；采用此方法的目的是 通过内存缓冲避免频繁的IO操作，提高索引创建性能；</li>
	 * @author DuanYong
	 * @since 2013-7-9 下午4:09:50
	 * @version 1.0
	 * @exception 
	 * @param indexDirName
	 */
	private void memoryToDisk(String indexDirName) {
		logger.info("开始存储于磁盘,目录名称=" + indexDirName + " size="
				+ getRAMWriter(indexDirName).ramSizeInBytes()
				/ (1024 * 1024 * 1.0) + "M");
		try {
			closeRAMWriter(indexDirName);
			// 创建存储于磁盘的IndexWriter
			IndexWriter FSDWriter = indexWriterPool.getIndexWriter(indexDirName);
			FSDWriter.addIndexes(getRAMDirectory(indexDirName));
			FSDWriter.commit();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			logger.debug("索引目录为:" + indexDirName + " 写入磁盘失败");
		} finally {
			// 重置
			resetTempKnowledgePointListMap(indexDirName);
		}
		// 创建存储于内存的IndexWriter
		createRAMWriter(indexDirName);
		logger.info("存储于磁盘结束");
	}

	/**
	 * 根据索引目录 得到RAMWriter
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-9 下午2:59:03
	 * @version 1.0
	 * @exception 
	 * @param indexDirName
	 * @return
	 */
	private IndexWriter getRAMWriter(String indexDirName) {
		if (null == ramWriterMap.get(indexDirName)) {
			createRAMWriter(indexDirName);
			return ramWriterMap.get(indexDirName);
		}
		IndexWriter ramIndexWriter = ramWriterMap.get(indexDirName);
		if (ramIndexWriter.getAnalyzer() == analyzer) {
			return ramIndexWriter;
		} else {
			try {
				ramIndexWriter.close();
				IndexWriter FSDWriter = indexWriterPool.getIndexWriter(indexDirName);
				FSDWriter.commit();
			} catch (Exception e) {
				logger.error("关闭失败：" + e.getMessage(), e);
				logger.debug("目录为:" + indexDirName + " 写入磁盘失败");
			} finally {
				// 重置
				resetTempKnowledgePointListMap(indexDirName);
			}
			createRAMWriter(indexDirName);
			return ramWriterMap.get(indexDirName);
		}

	}

	/**
	 * 根据索引目录 得到RAMDirectory
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-9 下午2:38:41
	 * @version 1.0
	 * @exception 
	 * @param indexDirName
	 * @return
	 */
	private RAMDirectory getRAMDirectory(String indexDirName) {
		if (null == ramDirectoryMap.get(indexDirName)) {
			createRAMDirectory(indexDirName);
		}
		return ramDirectoryMap.get(indexDirName);
	}

	/**
	 * 组装索引文档
	 * 
	 * @param knowledgePoint
	 *            知识点 对象
	 * @return lucene文档对象
	 */
	private Document getDocument(KnowledgePoint knowledgePoint) {
		Document document = new Document();
		// 公共域
		// 对应数据库主键
		document.add(new StringField(IndexConfig.FIELD_ID,
				knowledgePoint.getId().toString(), Field.Store.YES));
		// 模块
		document.add(new StringField(IndexConfig.FIELD_MODULE,
				knowledgePoint.getModule(), Field.Store.YES));
		// 标题
		document.add(new TextField(IndexConfig.FIELD_TITLE,
				knowledgePoint.getTitle(), Field.Store.YES));
		// 关键字
		document.add(new TextField(IndexConfig.FIELD_KEYWORD,
				knowledgePoint.getKeyWord(), Field.Store.YES));
		// 索引目录名称
		document.add(new TextField(IndexConfig.FIELD_INDEXDIRNAMR,
				knowledgePoint.getIndexDirName(), Field.Store.YES));
		// forShowDate主要保存前端展示用时间及格式
		document.add(new TextField(IndexConfig.FIELD_FORSHOWDATE,
				knowledgePoint.getShowDate(), Field.Store.YES));
		// content字段保存文档的内容,为了实现可预览需在索引中保存文档的全部文字内容
		document.add(new TextField(IndexConfig.FIELD_CONTENT,
				knowledgePoint.getContent(), Field.Store.YES));
		// desc字段保存文档的描述
		document.add(new TextField(IndexConfig.FIELD_DESC,
				knowledgePoint.getDesc(), Field.Store.YES));
		// 权限，是否可全局搜索
		document.add(new TextField(IndexConfig.FIELD_PURVIEW,
				knowledgePoint.getPurview(), Field.Store.YES));
		// 类型
		if(StringUtils.isNotBlank(knowledgePoint.getTypeCode())){
			if(knowledgePoint.getTypeCode().contains(KbsConstant.SYSTEM_LINE_SPITE)){
				String[] types = knowledgePoint.getTypeCode().split(KbsConstant.SYSTEM_LINE_SPITE);
				for(String type : types){
					document.add(new TextField(IndexConfig.FIELD_TYPECODE,type, Field.Store.YES));
				}
			}else if(knowledgePoint.getTypeCode().contains(KbsConstant.SYSTEM_AGA_SLANT_LINE_SPITE)){
				List<String> types = new ArrayList<String>();
				spiltType(types,knowledgePoint.getTypeCode());
				for(String type : types){
					document.add(new TextField(IndexConfig.FIELD_TYPECODE,type, Field.Store.YES));
				}
			}else{
				document.add(new TextField(IndexConfig.FIELD_TYPECODE,knowledgePoint.getTypeCode(), Field.Store.YES));
			}
		}
		// 类型描述
		document.add(new TextField(IndexConfig.FIELD_TYPEDESC,
				knowledgePoint.getTypeDesc(), Field.Store.YES));
		// 发布者姓名
		document.add(new TextField(IndexConfig.FIELD_USERNAME,
				knowledgePoint.getUserName(), Field.Store.YES));
		// 发布者编码
		document.add(new TextField(IndexConfig.FIELD_USERCODE,
				knowledgePoint.getUserCode(), Field.Store.YES));
		// 状态
		document.add(new TextField(IndexConfig.FIELD_STATE,
				knowledgePoint.getState(), Field.Store.YES));
		// 数量
		document.add(new TextField(IndexConfig.FIELD_COUNT,
				knowledgePoint.getCount(), Field.Store.YES));

		// 资源
		// 用"path"字段来保存文件的存放路径,该字段不需要切分所以但可以被检索所以用not_analyzed
		document.add(new StringField(IndexConfig.FIELD_PATH,
				knowledgePoint.getFilePath(), Field.Store.YES));
		// modified 保存文档的最近修改日期,可用于按日期排序
		document.add(new TextField(IndexConfig.FIELD_MODIFIED,
				knowledgePoint.getUploadDate(), Field.Store.YES));
		// 文件夹
		document.add(new TextField(IndexConfig.FIELD_DIRECTORY,
				knowledgePoint.getFileDirs(), Field.Store.YES));
		// 文档来源
		document.add(new TextField(IndexConfig.FIELD_ORIGIN,
				knowledgePoint.getOrigin(), Field.Store.YES));
		// 用origname保存原文件名
		document.add(new TextField(IndexConfig.FIELD_ORIGNAME,
				knowledgePoint.getOriginName(), Field.Store.YES));
		// 文章
		// 作者
		document.add(new TextField(IndexConfig.FIELD_AUTHOR,
				knowledgePoint.getAuthor(), Field.Store.YES));
		// 保存评论
		document.add(new TextField(IndexConfig.FIELD_COMMENT,
				knowledgePoint.getComment(), Field.Store.YES));
		// 问答
		// 答案
		document.add(new TextField(IndexConfig.FIELD_ANSWER,
				knowledgePoint.getAnswer(), Field.Store.YES));

		return document;
	}
    private void spiltType(List<String> list,String type){
    	int pos = type.indexOf(KbsConstant.SYSTEM_AGA_SLANT_LINE_SPITE);
    	if(pos != -1){
    		String start = type.substring(0, pos);
        	list.add(start);
        	String end = type.substring(pos + 1, type.length());
        	int endPos = end.indexOf(KbsConstant.SYSTEM_AGA_SLANT_LINE_SPITE);
        	if(endPos != -1){
        		spiltType(list,end);
        	}else{
        		list.add(end);
        	}
    	}
    }
	/**
	 * 重建索引目录对应的RAMWriter
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-9 下午2:24:51
	 * @version 1.0
	 * @exception 
	 * @param indexDirName
	 */
	private void createRAMWriter(String indexDirName) {
		try {
			// 如果存在，则新建
			if (StringUtils.isNotBlank(indexDirName)) {
				// 新建RAMDirectory
				createRAMDirectory(indexDirName);
				IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_43, analyzer);
				iwc.setMaxBufferedDocs(Integer.MAX_VALUE);
				iwc.setRAMBufferSizeMB(IndexWriterConfig.DISABLE_AUTO_FLUSH);
				iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
				IndexWriter ramIndexWriter = new IndexWriter(getRAMDirectory(indexDirName), iwc);
				ramWriterMap = new ConcurrentHashMap<String, IndexWriter>();
				ramWriterMap.put(indexDirName, ramIndexWriter);
			}
			logger.debug("初始化-----RAMWriterMap");
		} catch (CorruptIndexException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new IndexException("建立RAMWriter索引失败");
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new IndexException("建立RAMWriter索引失败");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new IndexException("建立RAMWriter索引失败");
		}
	}

	/**
	 * 重置指定索引目录下 记录的知识点集合
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-9 下午4:04:54
	 * @version 1.0
	 * @exception 
	 * @param indexDirName
	 */
	private void resetTempKnowledgePointListMap(String indexDirName) {
		logger.debug("重置索引目录：" + indexDirName + "知识点集合");
		setTempKnowledgePointListMap(indexDirName,new ArrayList<KnowledgePoint>());
	}

	/**
	 * 设置指定机构的知识点列表
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-9 下午3:15:45
	 * @version 1.0
	 * @exception 
	 * @param indexDirName
	 * @param knowledgePointList
	 */
	private void setTempKnowledgePointListMap(String indexDirName, ArrayList<KnowledgePoint> knowledgePointList) {
		tempKnowledgePointListMap = new ConcurrentHashMap<String, ArrayList<KnowledgePoint>>();
		tempKnowledgePointListMap.put(indexDirName, knowledgePointList);
	}

	/**
	 * 重建索引目录对应的RAMDirectory
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-9 下午2:36:34
	 * @version 1.0
	 * @exception 
	 * @param indexDirName
	 */
	private void createRAMDirectory(String indexDirName) {
		// 如果存在，则新建
		if (StringUtils.isNotBlank(indexDirName)) {
			ramDirectoryMap = new ConcurrentHashMap<String, RAMDirectory>();
			ramDirectoryMap.put(indexDirName, new RAMDirectory());
		}
		logger.debug("初始化-----systemRAMDirectoryMap");
	}

	/**
	 * 关闭
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-9 下午2:57:35
	 * @version 1.0
	 * @exception 
	 * @param indexDirName
	 */
	private void closeRAMWriter(String indexDirName) {
		try {
			getRAMWriter(indexDirName).close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new IndexException("关闭RAMWriter索引失败");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new IndexException("关闭RAMWriter索引失败");
		}
	}

	/**
	 * 解析文档
	 * 
	 * @param knowledgePoint
	 * @throws Exception
	 */
	private void parserDocument(KnowledgePoint knowledgePoint){
		// 取得文件类型
		String fileType = IndexResourceUtil.getDocType(knowledgePoint.getFilePath());
		logger.info("解析文档类型为："+fileType);
		//如果是大文件
		if(knowledgePoint.isBigFile()){
			logger.error("大文件，将文件名作为内容保存");
			knowledgePoint.setContent(FilenameUtils.getName(knowledgePoint.getFilePath()));
		}else{
			// 根据文档类型取得对应解析器
			DocumentParser documentParser = null;
			// 如果文件类型不是已有的文本类型，就不需要解析
			if (indexConfigration.getFileContentType().contains(fileType)) {
				// 根据文档类型取得对应解析器
				documentParser = parserFactory.getDocumentParser(fileType);
			} else {
				//按照普通文本类型来解析，如果报错就不再解析
				documentParser = parserFactory.getDocumentParser("txt");
			}
			try {
				// 设置文档路径
				if(KbsConstant.ORIGIN_LOCAL.equals(knowledgePoint.getOrigin())){
					documentParser.parser(knowledgePoint.getFilePath());
				}else{
					documentParser.parser(IndexResourceUtil.getAbsolutePath(knowledgePoint.getFilePath()));
				}
				knowledgePoint.setContent(documentParser.getContent());
				logger.info("文档解析成功");
				logger.debug("文档内容："+knowledgePoint.getContent());
			}catch(ParserException e){
				logger.error("文档解析失败，将文件名作为内容保存:"+e.getExceptionMessage());
				knowledgePoint.setContent(FilenameUtils.getName(knowledgePoint.getFilePath()));
			}catch(Exception e){
				logger.error("其他异常导致文档解析失败，将文件名作为内容保存:"+e.getMessage());
				knowledgePoint.setContent(FilenameUtils.getName(knowledgePoint.getFilePath()));
			}
		}
	}
	

	/**
	 * 初始化
	 */
	public void initIndex() {
		createRAMWriter("");
	}

	/**
	 * @return the indexConfigration
	 */
	public IndexConfigration getIndexConfigration() {
		return indexConfigration;
	}
    
}
