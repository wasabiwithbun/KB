/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.lucene.search;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.util.Version;
import org.javacoo.cowswing.plugin.kbs.constant.IndexConfig;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.domain.KnowledgePoint;
import org.javacoo.cowswing.plugin.kbs.exception.IndexException;
import org.javacoo.cowswing.plugin.kbs.lucene.index.IndexConfigration;
import org.javacoo.cowswing.plugin.kbs.lucene.pool.IndexReaderPool;
import org.javacoo.cowswing.plugin.kbs.service.beans.SearchCriteria;
import org.javacoo.cowswing.plugin.kbs.utils.IndexResourceUtil;
import org.javacoo.persistence.PaginationSupport;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * 文档搜索接口实现类
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-8 上午9:48:03
 * @version 1.0
 */
@Component("searchManager")
public class SearchManager implements Search{
	/** 日志对象 */
	private final static Log logger = LogFactory.getLog(SearchManager.class);
	/** 索引配置参数 */
	@Resource(name = "indexConfigration")
	private IndexConfigration indexConfigration;
	/** 分词器 */
	//@Resource(name = "analyzer")
	private Analyzer analyzer = new IKAnalyzer();
	/** IndexReaderPool池 */
	@Resource(name = "indexReaderPool")
	private IndexReaderPool indexReaderPool;
	
	private String[] fields = new String[] {
			IndexConfig.FIELD_CONTENT,
			IndexConfig.FIELD_ORIGIN,
			IndexConfig.FIELD_KEYWORD,
			IndexConfig.FIELD_TITLE,
			IndexConfig.FIELD_DESC };
	// 需要高亮的字段
	private String[] preExtractFields = new String[] {
			IndexConfig.FIELD_CONTENT,
			IndexConfig.FIELD_KEYWORD,
			IndexConfig.FIELD_TITLE,
			IndexConfig.FIELD_DESC,
			IndexConfig.FIELD_COMMENT,
			IndexConfig.FIELD_ANSWER };
	private final static int DIGEST_LENGTH = 200;
	
	
	/**
	 * 根据搜索条件搜索文档
	 * 
	 * @param criteria
	 *            搜索条件bean
	 * @return 文档LIST
	 * @throws IOException
	 */
	public PaginationSupport<KnowledgePoint> find(SearchCriteria criteria) {
		try {
			return findKnowledgePointList(criteria, this.fields);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IndexException("查询索引失败");
		}
	}

	/**
	 * 根据知识点参数查询文档
	 * 
	 * @param knowledgePoint知识点
	 * @return lucene Document对象
	 * @throws Exception
	 */
	public Document findKnowledgePoint(KnowledgePoint knowledgePoint)
			throws Exception { // 显示结果
		IndexReader reader = indexReaderPool.getIndexReader(knowledgePoint.getIndexDirName());
		IndexSearcher searcher = new IndexSearcher(reader); // 检索器
		Term term = new Term(IndexConfig.FIELD_PATH,knowledgePoint.getFilePath());
		Query query = new TermQuery(term); // 检索单元
		logger.info("查询语句：" + term.toString());
		int hitsPerPage = 100;
		TopDocs results = searcher.search(query, 5 * hitsPerPage);
		ScoreDoc[] hits = results.scoreDocs; // 提交检索
		logger.info("搜索结果:");
		if (null == hits || hits.length > 1) {
			throw new Exception("查询失败:索引不存在,或者 索引不唯一");
		}
		Document result = null;

		try {
			result = searcher.doc(hits[0].doc);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new Exception("查询索引失败");
		}
		return result;

	}

	/**
	 * 根据模块和主键条件搜索相关资源
	 * 
	 * @param criteria
	 *            搜索条件bean
	 * @return 文档LIST
	 */
	public List<KnowledgePoint> findCorrelative(SearchCriteria criteria)
			throws Exception {
		Term idTerm = new Term(IndexConfig.FIELD_ID, criteria.getObjectId());
		TermQuery idQuery = new TermQuery(idTerm);
		Term moduleTerm = new Term(IndexConfig.FIELD_MODULE,
				criteria.getModule());
		TermQuery muduleQuery = new TermQuery(moduleTerm);
		BooleanQuery query = new BooleanQuery();
		query.add(idQuery, BooleanClause.Occur.MUST);
		query.add(muduleQuery, BooleanClause.Occur.MUST);
		TopScoreDocCollector collector = TopScoreDocCollector
				.create(100, false);
		MultiReader multiReader = new MultiReader(getIndexReaders(indexConfigration.getIndexDir()));
		IndexSearcher searcher = new IndexSearcher(multiReader);
		searcher.search(query, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		if (hits.length > 1) {
			logger.error("不止一条索引");
			return null;
		} else if (hits.length > 0) {
			multiReader = new MultiReader(getIndexReaders(indexConfigration.getIndexDir()));
			return moreLikeSearch(multiReader,searcher, hits[0].doc);
		} else {
			logger.info("未查找到systemCode:" + criteria.getSystemCode()
					+ " objectId:" + criteria.getObjectId() + " module:"
					+ criteria.getModule() + " 匹配结果");
			return null;
		}
	}

	/**
	 * 从Lucene的Document对象中提取各字段（Field）信息,封装为一个KnowledgePoint
	 * 
	 * @param doc
	 *            Document对象
	 * @return KnowledgePoint
	 */
	private KnowledgePoint populateKnowledgePoint(Document doc) {
		KnowledgePoint point = new KnowledgePoint();
		point.setFileDirs(doc.getField(
				IndexConfig.FIELD_DIRECTORY).stringValue());
		point.setFilePath(doc.getField(IndexConfig.FIELD_PATH)
				.stringValue());
		point.setShowDate(doc.getField(
				IndexConfig.FIELD_FORSHOWDATE).stringValue());
		
		point.setUploadDate(doc.getField(
				IndexConfig.FIELD_MODIFIED).stringValue());
		point.setKeyWord(doc.getField(IndexConfig.FIELD_KEYWORD)
				.stringValue());
		point.setTitle(doc.getField(IndexConfig.FIELD_TITLE)
				.stringValue());
		point.setOrigin(doc.getField(IndexConfig.FIELD_ORIGIN)
				.stringValue());
		point.setId(doc.getField(IndexConfig.FIELD_ID).stringValue());
		point.setModule(doc.getField(IndexConfig.FIELD_MODULE)
				.stringValue());
		point.setState(doc.getField(IndexConfig.FIELD_STATE)
				.stringValue());
		point.setPurview(doc.getField(IndexConfig.FIELD_PURVIEW)
				.stringValue());
		point.setTypeCode(doc
				.getField(IndexConfig.FIELD_TYPECODE)
				.stringValue());
		point.setTypeDesc(doc.getField(IndexConfig.FIELD_TYPEDESC)
				.stringValue());
		point.setUserName(doc
				.getField(IndexConfig.FIELD_USERNAME)
				.stringValue());
		point.setUserCode(doc
				.getField(IndexConfig.FIELD_USERCODE)
				.stringValue());
		point.setAuthor(doc.getField(IndexConfig.FIELD_AUTHOR)
				.stringValue());
		point.setCount(doc.getField(IndexConfig.FIELD_COUNT)
				.stringValue());
		point.setContent(doc.getField(IndexConfig.FIELD_CONTENT)
				.stringValue());
		return point;
	}

	/**
	 * 根据系索引目录查询所有所有目录，并组装为IndexSearcher数组
	 * 
	 * @param indexDirName
	 *            索引文件根目录
	 * @return
	 */
	private IndexReader[] getIndexReaders(String indexDirName) {
		IndexReader reader;
		List<IndexReader> readerList = new ArrayList<IndexReader>();
		List<String> indexDirList = IndexResourceUtil.getIndexDirs(indexDirName);
		for (String indexDirPath : indexDirList) {
			reader = indexReaderPool.getIndexReader(indexDirPath);
			if (null != reader) {
				readerList.add(reader);
			}
		}
		IndexReader[] IndexReaderArray = new IndexReader[readerList.size()];
		for(int i = 0 ;i<readerList.size();i++){
			IndexReaderArray[i] = readerList.get(i);
		}
		return IndexReaderArray;
	}

	

	/**
	 * 根据field数组查询 知识点集合
	 * 
	 * @param criteria
	 *            查询条件
	 * @param fields
	 *            field数
	 * @return 知识点集合
	 * @throws Exception
	 */
	private PaginationSupport<KnowledgePoint> findKnowledgePointList(
			SearchCriteria criteria, String[] fields) {
		PaginationSupport<KnowledgePoint> result = null;
		if (!IndexResourceUtil.isEmptyIndexDir(new File(IndexResourceUtil
				.getDirPath(indexConfigration.getIndexDir(), criteria.getIndexDirName())))) {
			try {
				result = search(criteria, fields);
			} catch (CorruptIndexException e1) {
				e1.printStackTrace();
				logger.error("解析异常", e1);
			} catch (IOException e1) {
				e1.printStackTrace();
				logger.error("查询索引失败", e1);
			} catch (ParseException e) {
				e.printStackTrace();
				logger.error("解析异常", e);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("解析异常", e);
			}
		} else {
			try {
				throw new Exception("索引不存在");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("索引不存在", e);
			}
		}
		return result;
	}

	/**
	 * 得到高亮处理对象
	 * 
	 * @param criteria
	 *            查询条件
	 * @param inputFields
	 *            高亮字段
	 * @param parser
	 *            解析对象
	 * @param query
	 *            查询对象
	 * @return 高亮处理对象
	 * @throws IOException
	 */
	private Highlighter getHighlighter(SearchCriteria criteria,
			String[] inputFields, QueryParser parser, Query query)
			throws IOException {
		Highlighter highlighter = null;
		SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter(
				"<font color='red'>", "</font>");
		highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(
				query));// 只对内容域搜索的关键字加高亮，不对条件中包含的目录和机构加高亮
		highlighter.setTextFragmenter(new SimpleFragmenter(DIGEST_LENGTH));
		return highlighter;
	}

	/**
	 * 搜索
	 * 
	 * @param criteria
	 *            查询对象
	 * @param reader
	 *            索引读取对象
	 * @param inputFields
	 *            高亮字段
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	private PaginationSupport<KnowledgePoint> search(SearchCriteria criteria, String[] inputFields)
			throws ParseException, IOException {
		QueryParser parser = null;
		Query query = null;
		Filter filter = null;
		Highlighter highlighter = null;
		// 如果有目录,机构,以及关键字的搜索就用组合查询方式,其中目录和机构是必须满足的所以flags中对应的标志是MUST
		if (inputFields != null && inputFields.length > 1) {
			if (null == criteria.getQueryKey()
					|| !StringUtils.isNotBlank(criteria.getQueryKey())) {
				query = this.populateSimpleQuery(criteria);
			} else {
				criteria.setQueryKey(populateQueryKey("", criteria
						.getQueryKey()));
				query = this.createMutilQuery(criteria);
			}
			parser = new QueryParser(Version.LUCENE_43,IndexConfig.FIELD_CONTENT, analyzer);
			highlighter = getHighlighter(criteria, inputFields, parser, query);
		}
		logger.info("查询语句: " + query.toString() + "关键字="+ criteria.getQueryKey());
		return doPagingSearch(criteria, query, filter, inputFields,
				highlighter);
	}

	/**
	 * 组装简单查询query
	 * <p>
	 * 无查询条件，只根据模块编码或者类型编码查询或者状态编码或者目录编码进行组合查询
	 * 
	 * @param criteria
	 * @return
	 */
	private Query populateSimpleQuery(SearchCriteria criteria)
			throws ParseException {
		// 按目录查询
		QueryParser directoryParser = new QueryParser(Version.LUCENE_43,
				IndexConfig.FIELD_DIRECTORY, analyzer);
		// 按模块查询
		Query moduleQuery = null;
		if (criteria.getModule() != null
				&& (StringUtils.isNotBlank(criteria.getModule()))) {
			if (KbsConstant.SYSTEM_MODULE_SOURCE.equals(criteria.getModule())) {
				Query moduleQuery1 = populateQuery(
						IndexConfig.FIELD_MODULE, criteria
								.getModule());
				Query moduleQuery2 = populateQuery(
						IndexConfig.FIELD_MODULE,
						KbsConstant.SYSTEM_MODULE_SOURCETASK);

				moduleQuery = new BooleanQuery();
				((BooleanQuery) moduleQuery).add(moduleQuery1,
						BooleanClause.Occur.SHOULD);
				((BooleanQuery) moduleQuery).add(moduleQuery2,
						BooleanClause.Occur.SHOULD);
			} else {
				moduleQuery = populateQuery(
						IndexConfig.FIELD_MODULE, criteria
								.getModule());
			}
		}
		// 按分类查询
		Query typecodeQuery = null;
		if (criteria.getTypecode() != null
				&& (StringUtils.isNotBlank(criteria.getTypecode()))) {
			typecodeQuery = new BooleanQuery();
			String[] typecodes = criteria.getTypecode().split(" ");
			for (String typecode : typecodes) {
				((BooleanQuery) typecodeQuery).add(populateQuery(
						IndexConfig.FIELD_TYPECODE, typecode),
						BooleanClause.Occur.SHOULD);
			}
		}
		// 按目录查询
		Query directoryQuery = null;
		if (criteria.getDirectory() != null
				&& (StringUtils.isNotBlank(criteria.getDirectory()))) {
			directoryQuery = directoryParser.parse(criteria.getDirectory());
		}
		// 按状态查询
		Query stateQuery = null;
		if (criteria.getState() != null
				&& (StringUtils.isNotBlank(criteria.getState()))) {
			stateQuery = populateQuery(IndexConfig.FIELD_STATE,
					criteria.getState());
		}
		// 创建各字段组合的逻辑关系
		BooleanQuery booleanQuery = new BooleanQuery();

		// 用lucene逻辑表达为(+module:SOURCE +typecode:QA00000001) "+"表示must,即该匹配必须成立
		if (null != moduleQuery) {
			booleanQuery.add(moduleQuery, BooleanClause.Occur.MUST);
		}
		if (null != typecodeQuery) {
			booleanQuery.add(typecodeQuery, BooleanClause.Occur.MUST);
		}
		if (null != directoryQuery) {
			booleanQuery.add(directoryQuery, BooleanClause.Occur.MUST);
		}
		if (null != stateQuery) {
			booleanQuery.add(stateQuery, BooleanClause.Occur.MUST);
		}
		return getBooleanQuery(booleanQuery);
	}

	/**
	 * 分页查询
	 * 
	 * @param criteria
	 *            查询对象
	 * @param reader
	 *            索引读取对象
	 * @param query
	 *            查询对象
	 * @param orgFilter
	 *            过滤器
	 * @param inputFields
	 *            高亮字段
	 * @param highlighter
	 *            高亮对象
	 * @return
	 * @throws IOException
	 */
	private PaginationSupport<KnowledgePoint> doPagingSearch(SearchCriteria criteria, Query query, Filter orgFilter,
			String[] inputFields, Highlighter highlighter) throws IOException {
		// 多个索引的多线程搜索,对搜索后的结果进行合并，剔除重复的信息
		MultiReader multiReader = new MultiReader(getIndexReaders(indexConfigration.getIndexDir()));
		IndexSearcher searcher = new IndexSearcher(multiReader);
		// 默认查出结果大小
		int totalResult = KbsConstant.DEFAULT_PAGE_NUM
				* criteria.getPageSize();
		// 如果继续查询 开始记录数 大于结果集记录数 则 在原有结果集上加上每页显示记录数
		if (criteria.getStartIndex() >= totalResult) {
			totalResult = criteria.getStartIndex() + criteria.getPageSize();
		}
		logger.info("查询条件:" + query);
		TopScoreDocCollector collector = TopScoreDocCollector.create(
				totalResult, false);
		if (orgFilter == null) {
			searcher.search(query, collector);
		} else {
			searcher.search(query, orgFilter, collector);
		}
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		int numTotalHits = collector.getTotalHits();
		logger.info(" 匹配结果："+numTotalHits);
		int start = criteria.getStartIndex();
		int end = Math.min(numTotalHits, criteria.getPageSize());
		// 最后一页处理
		if (end > hits.length) {
			collector = TopScoreDocCollector.create(numTotalHits, false);
			searcher.search(query, collector);
			hits = collector.topDocs().scoreDocs;
		}
		end = Math.min(hits.length, start + criteria.getPageSize());
		List<KnowledgePoint> result = new ArrayList<KnowledgePoint>();
		Document doc;
		for (int i = start; i < end; i++) {
			doc = searcher.doc(hits[i].doc);
			// 组装知识点对象
			KnowledgePoint point = populateKnowledgePoint(doc);
			// 高亮处理
			point.setContextDigest(highlight(doc, inputFields, highlighter,
					criteria.getSystemCode()));
			result.add(point);
		}
		return new PaginationSupport(result, numTotalHits,criteria.getStartIndex(),criteria.getPageSize());
	}

	/**
	 * 高亮处理
	 * 
	 * @param doc
	 *            Lucene的Document对象
	 * @param fields
	 *            高亮显示的字段数组
	 * @param highlighter
	 *            高亮处理对象
	 * @param systemCode
	 *            机构号
	 */
	private String highlight(Document doc, String[] fields,
			Highlighter highlighter, String systemCode) {
		String digestStr = "";
		if (null != fields && fields.length > 1) {
			try {
				digestStr = highlightMatchWorks(doc, preExtractFields,
						highlighter, systemCode);
			} catch (IOException e) {
				logger.debug("搜索信息高亮处理出错 " + e.getMessage());
				throw new IndexException("搜索信息高亮处理出错");
			} catch (InvalidTokenOffsetsException e) {
				logger.debug("搜索信息高亮处理出错 " + e.getMessage());
				throw new IndexException("搜索信息高亮处理出错");
			}
		}
		return digestStr;
	}

	/**
	 * 该方法实现对指定field中与检索信息匹配的内容进行高亮显示
	 * 
	 * @param doc
	 *            Lucene的Document对象
	 * @param fields
	 *            高亮显示的字段数组
	 * @param highlighter
	 *            高亮处理对象
	 * @param systemCode
	 *            机构号
	 * @return
	 * @throws IOException
	 * @throws InvalidTokenOffsetsException
	 */
	private String highlightMatchWorks(Document doc, String[] fields,
			Highlighter highlighter, String systemCode) throws IOException,
			InvalidTokenOffsetsException {
		String digestStr = "";
		for (int i = 0; i < fields.length; i++) {
			try {
				TokenStream tokenStream = analyzer.tokenStream(fields[i],new StringReader(doc.get(fields[i])));
				digestStr = highlighter.getBestFragment(tokenStream, doc
						.get(fields[i]));
				tokenStream.close();
				if (digestStr != null) {
					return digestStr;
				}
			} catch (NullPointerException ex) {
				logger.debug("ex=" + ex.getMessage());
				// 出现该异常表示关键字不在当前field中，继续查找下一个field
				continue;
			}
		}
		return digestStr;
	}

	/**
	 * 组装多字段多条件组合查询条件的Query
	 * 
	 * @param criteria
	 *            查询条件
	 * @return 多条件组合查询条件的Query
	 * @throws ParseException
	 * @throws IOException
	 */
	private Query createMutilQuery(SearchCriteria criteria)
			throws ParseException, IOException {
		// analyer必须与创建索引所使用的analyzer一致
		// Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);

		// 定义解析各Field对应的Querypaser
		// 按内容查询
		QueryParser contentParser = new QueryParser(Version.LUCENE_43,
				IndexConfig.FIELD_CONTENT, analyzer);
		// 按描述查询
		QueryParser descParser = new QueryParser(Version.LUCENE_43,
				IndexConfig.FIELD_DESC, analyzer);
		// 按关键字查询
		QueryParser keywordParser = new QueryParser(Version.LUCENE_43,
				IndexConfig.FIELD_KEYWORD, analyzer);
		// 按标题查询
		QueryParser titleParser = new QueryParser(Version.LUCENE_43,
				IndexConfig.FIELD_TITLE, analyzer);
		// 按文件来源查询
		QueryParser originParser = new QueryParser(Version.LUCENE_43,
				IndexConfig.FIELD_ORIGIN, analyzer);
		// 按回答内容查询
		QueryParser answerParser = new QueryParser(Version.LUCENE_43,
				IndexConfig.FIELD_ANSWER, analyzer);
		// 按评论内容查询
		QueryParser commentParser = new QueryParser(Version.LUCENE_43,
				IndexConfig.FIELD_COMMENT, analyzer);
		// 按类型描述查询
		QueryParser typemsgParser = new QueryParser(Version.LUCENE_43,
				IndexConfig.FIELD_TYPEDESC, analyzer);
		// 按目录查询
		QueryParser directoryParser = new QueryParser(Version.LUCENE_43,
				IndexConfig.FIELD_DIRECTORY, analyzer);
        // 按模块查询
		Query moduleQuery = null;
		if (criteria.getModule() != null
				&& (StringUtils.isNotBlank(criteria.getModule()))) {
			if (criteria.getModule().equals(
					KbsConstant.SYSTEM_MODULE_SOURCE)) {
				Query moduleQuery1 = populateQuery(
						IndexConfig.FIELD_MODULE, criteria
								.getModule());
				Query moduleQuery2 = populateQuery(
						IndexConfig.FIELD_MODULE,
						KbsConstant.SYSTEM_MODULE_SOURCETASK);

				moduleQuery = new BooleanQuery();
				((BooleanQuery) moduleQuery).add(moduleQuery1,
						BooleanClause.Occur.SHOULD);
				((BooleanQuery) moduleQuery).add(moduleQuery2,
						BooleanClause.Occur.SHOULD);
			} else {
				moduleQuery = populateQuery(
						IndexConfig.FIELD_MODULE, criteria
								.getModule());
			}
		}
		// 按类别编码查询
		Query typecodeQuery = null;
		if (criteria.getTypecode() != null
				&& (StringUtils.isNotBlank(criteria.getTypecode()))) {
			typecodeQuery = new BooleanQuery();
			String[] typecodes = criteria.getTypecode().split(" ");
			for (String typecode : typecodes) {
				((BooleanQuery) typecodeQuery).add(populateQuery(
						IndexConfig.FIELD_TYPECODE, typecode),
						BooleanClause.Occur.SHOULD);
			}
		}
		// 按状态查询
		Query stateQuery = null;
		if (criteria.getState() != null
				&& (StringUtils.isNotBlank(criteria.getState()))) {
			stateQuery = populateQuery(IndexConfig.FIELD_STATE,
					criteria.getState());
		}
		// 按文档目录查询
		Query directoryQuery = null;
		if (criteria.getDirectory() != null
				&& (StringUtils.isNotBlank(criteria.getDirectory()))) {
			directoryQuery = directoryParser.parse(criteria.getDirectory());
		}
		Query contentQuery = contentParser.parse(criteria.getQueryKey());
		Query descQuery = descParser.parse(criteria.getQueryKey());
		Query keywordQuery = keywordParser.parse(criteria.getQueryKey());
		Query titleQuery = titleParser.parse(criteria.getQueryKey());
		Query originQuery = originParser.parse(criteria.getQueryKey());
		Query answerQuery = answerParser.parse(criteria.getQueryKey());
		Query commentQuery = commentParser.parse(criteria.getQueryKey());
		Query typemsgQuery = typemsgParser.parse(criteria.getQueryKey());

		// 创建各字段组合的逻辑关系
		BooleanQuery booleanQuery = new BooleanQuery();
		BooleanQuery orgContentQuery = new BooleanQuery();
		BooleanQuery orgDescQuery = new BooleanQuery();
		BooleanQuery orgKeywordQuery = new BooleanQuery();
		BooleanQuery orgTtileQuery = new BooleanQuery();
		BooleanQuery orgOrigNameQuery = new BooleanQuery();
		BooleanQuery orgOriginQuery = new BooleanQuery();
		BooleanQuery answerBoolQuery = new BooleanQuery();
		BooleanQuery commentBoolQuery = new BooleanQuery();
		BooleanQuery typemsgBoolQuery = new BooleanQuery();

		// query1定义orgcode字段与content字段的逻辑关系，orgQuery和contentQuery都设置为MUST,表示在这两个query都成立的情况下query1才成立
		// 用lucene逻辑表达为(+orgcode:10002002 +content:"测 试") "+"表示must,即该匹配必须成立

		orgContentQuery.add(contentQuery, BooleanClause.Occur.MUST);

		orgDescQuery.add(descQuery, BooleanClause.Occur.MUST);

		// 用lucene逻辑表达为(+orgcode:10002002 +keyword:"测 试")

		orgKeywordQuery.add(keywordQuery, BooleanClause.Occur.MUST);

		// (+orgcode:10002002 +title:"测 试")
		orgTtileQuery.add(titleQuery, BooleanClause.Occur.MUST);
		// (+orgcode:10002002 +origname:"测 试")

		orgOriginQuery.add(originQuery, BooleanClause.Occur.MUST);

		answerBoolQuery.add(answerQuery, BooleanClause.Occur.MUST);

		commentBoolQuery.add(commentQuery, BooleanClause.Occur.MUST);

		typemsgBoolQuery.add(typemsgQuery, BooleanClause.Occur.MUST);

		if (moduleQuery != null) {
			orgContentQuery.add(moduleQuery, BooleanClause.Occur.MUST);
			orgDescQuery.add(moduleQuery, BooleanClause.Occur.MUST);
			orgKeywordQuery.add(moduleQuery, BooleanClause.Occur.MUST);
			orgTtileQuery.add(moduleQuery, BooleanClause.Occur.MUST);
			orgOrigNameQuery.add(moduleQuery, BooleanClause.Occur.MUST);
			orgOriginQuery.add(moduleQuery, BooleanClause.Occur.MUST);
			answerBoolQuery.add(moduleQuery, BooleanClause.Occur.MUST);
			commentBoolQuery.add(moduleQuery, BooleanClause.Occur.MUST);
			typemsgBoolQuery.add(moduleQuery, BooleanClause.Occur.MUST);
		}
		if (typecodeQuery != null) {
			orgContentQuery.add(typecodeQuery, BooleanClause.Occur.MUST);
			orgDescQuery.add(typecodeQuery, BooleanClause.Occur.MUST);
			orgKeywordQuery.add(typecodeQuery, BooleanClause.Occur.MUST);
			orgTtileQuery.add(typecodeQuery, BooleanClause.Occur.MUST);
			orgOrigNameQuery.add(typecodeQuery, BooleanClause.Occur.MUST);
			orgOriginQuery.add(typecodeQuery, BooleanClause.Occur.MUST);
			answerBoolQuery.add(typecodeQuery, BooleanClause.Occur.MUST);
			commentBoolQuery.add(typecodeQuery, BooleanClause.Occur.MUST);
			typemsgBoolQuery.add(typecodeQuery, BooleanClause.Occur.MUST);
		}
		if (stateQuery != null) {
			orgContentQuery.add(stateQuery, BooleanClause.Occur.MUST);
			orgDescQuery.add(stateQuery, BooleanClause.Occur.MUST);
			orgKeywordQuery.add(stateQuery, BooleanClause.Occur.MUST);
			orgTtileQuery.add(stateQuery, BooleanClause.Occur.MUST);
			orgOrigNameQuery.add(stateQuery, BooleanClause.Occur.MUST);
			orgOriginQuery.add(stateQuery, BooleanClause.Occur.MUST);
			answerBoolQuery.add(stateQuery, BooleanClause.Occur.MUST);
			commentBoolQuery.add(stateQuery, BooleanClause.Occur.MUST);
			typemsgBoolQuery.add(stateQuery, BooleanClause.Occur.MUST);
		}

		if (directoryQuery != null) {
			orgContentQuery.add(directoryQuery, BooleanClause.Occur.MUST);
			orgDescQuery.add(directoryQuery, BooleanClause.Occur.MUST);
			orgKeywordQuery.add(directoryQuery, BooleanClause.Occur.MUST);
			orgTtileQuery.add(directoryQuery, BooleanClause.Occur.MUST);
			orgOrigNameQuery.add(directoryQuery, BooleanClause.Occur.MUST);
			orgOriginQuery.add(directoryQuery, BooleanClause.Occur.MUST);
			typemsgBoolQuery.add(directoryQuery, BooleanClause.Occur.MUST);
		}

		// query在这里表示更高一层的逻辑组合，在以上子逻辑条件已定义的情况下表示，只要有其中一个子BooleanQuery成立，那
		// 么该query就成立 注意query 类型也为BooleanQuery,以下用luece逻辑表达式表示为
		// (+orgcode:10002002 +content:"测 试") (+orgcode:10002002 +keyword:"测 试")
		// (+orgcode:10002002 +title:"测 试") (+orgcode:10002002 +origname:"测 试")
		// 它表示为在机构号一定(MUST)的情况下，对输入的关键字可能出现的多个field进行搜索，当只要关键字在其中一个或多feild出现,那么就提取相应的docment对象
		booleanQuery.add(orgContentQuery, BooleanClause.Occur.SHOULD);
		booleanQuery.add(orgDescQuery, BooleanClause.Occur.SHOULD);
		booleanQuery.add(orgKeywordQuery, BooleanClause.Occur.SHOULD);
		booleanQuery.add(orgTtileQuery, BooleanClause.Occur.SHOULD);
		booleanQuery.add(orgOrigNameQuery, BooleanClause.Occur.SHOULD);
		booleanQuery.add(orgOriginQuery, BooleanClause.Occur.SHOULD);
		booleanQuery.add(answerBoolQuery, BooleanClause.Occur.SHOULD);
		booleanQuery.add(commentBoolQuery, BooleanClause.Occur.SHOULD);
		booleanQuery.add(typemsgBoolQuery, BooleanClause.Occur.SHOULD);
		
		return getBooleanQuery(booleanQuery);

	}

	/**
	 * 根据查询域和值组装查询Query
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	private Query populateQuery(String field, String value) {
		Term term = new Term(field, value);
		Query query = new TermQuery(term);
		return query;
	}

	/**
	 * 根据document的id获取这个field的Term Vector,找出相关的索引信息
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-8 下午9:48:48
	 * @version 1.0
	 * @exception 
	 * @param reader
	 * @param id
	 * @return
	 * @throws IOException
	 */
	private List<KnowledgePoint> moreLikeSearch(MultiReader reader,IndexSearcher searcher, int id)
			throws IOException {
		// 根据这个document的id获取这个field的Term Vector
		// 信息，就是这个field分词之后在这个field里的频率、位置、等信息
		Fields vectors = reader.getTermVectors(id);

		BooleanQuery morelikeQuery = new BooleanQuery();// 模糊查询的query
		String tempField = "";
		for(Iterator<String> it = vectors.iterator();it.hasNext();){
			tempField = it.next();
			TermQuery tq = new TermQuery(new Term(tempField)); // 获取每个term保存的Token
			morelikeQuery.add(tq, BooleanClause.Occur.SHOULD);
		}
		BooleanQuery allQuery = getBooleanQuery(morelikeQuery);
		logger.info("模糊查询条件:" + allQuery);
		TopScoreDocCollector collector = TopScoreDocCollector
				.create(100, false);
		searcher.search(allQuery, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		List<KnowledgePoint> knowledgePoints = new ArrayList<KnowledgePoint>();
		KnowledgePoint knowledgePoint = null;
		for (int i = 0; i < hits.length; i++) {
			Document doc = searcher.doc(hits[i].doc);
			if (hits[i].doc != id) {
				knowledgePoint = populateKnowledgePoint(doc);
				knowledgePoints.add(knowledgePoint);
				logger.info(knowledgePoint.getTitle() + " 分数:" + hits[i].score);
			}
		}

		return knowledgePoints;
	}
	
	private BooleanQuery getBooleanQuery(BooleanQuery morelikeQuery){
		BooleanQuery personAllQuery = new BooleanQuery();
		TermQuery purviewPersonQuery = new TermQuery(new Term(
				IndexConfig.FIELD_PURVIEW,
				KbsConstant.SEARCH_PURVIEW_PERSON));
		personAllQuery.add(purviewPersonQuery, BooleanClause.Occur.MUST);
		personAllQuery.add(morelikeQuery, BooleanClause.Occur.MUST);
		
		BooleanQuery searchAllQuery = new BooleanQuery();// 搜索权限查询的query
		TermQuery purviewSearchQuery = new TermQuery(new Term(
				IndexConfig.FIELD_PURVIEW,
				KbsConstant.SEARCH_PURVIEW_SEARCH));
		searchAllQuery.add(purviewSearchQuery, BooleanClause.Occur.MUST);
		searchAllQuery.add(morelikeQuery, BooleanClause.Occur.MUST);
		
		BooleanQuery viewAllQuery = new BooleanQuery();// 查看权限查询的query
		TermQuery purviewViewQuery = new TermQuery(new Term(
				IndexConfig.FIELD_PURVIEW,
				KbsConstant.SEARCH_PURVIEW_VIEW));
		viewAllQuery.add(purviewViewQuery, BooleanClause.Occur.MUST);
		viewAllQuery.add(morelikeQuery, BooleanClause.Occur.MUST);
		
		BooleanQuery downloadAllQuery = new BooleanQuery();// 下载权限查询的query
		TermQuery purviewDownloadQuery = new TermQuery(new Term(
				IndexConfig.FIELD_PURVIEW,
				KbsConstant.SEARCH_PURVIEW_DOWNLOAD));
		downloadAllQuery.add(purviewDownloadQuery, BooleanClause.Occur.MUST);
		downloadAllQuery.add(morelikeQuery, BooleanClause.Occur.MUST);

		BooleanQuery allQuery = new BooleanQuery();// 总的query
		// 查询满足模糊查询并且同时满足搜索权限，或则满足模糊查询并且同时满足查看权限，或则满足模糊查询并且同时满足下载权限
		allQuery.add(personAllQuery, BooleanClause.Occur.SHOULD);
		allQuery.add(searchAllQuery, BooleanClause.Occur.SHOULD);
		allQuery.add(viewAllQuery, BooleanClause.Occur.SHOULD);
		allQuery.add(downloadAllQuery, BooleanClause.Occur.SHOULD);
		return allQuery;
	}

	/**
	 * 对查询关键字分词后以空格
	 * 
	 * @param field
	 *            要处理的域
	 * @param queryKey
	 *            查询关键字
	 * @param systemCode
	 *            系统号
	 * @return 处理后的查询关键字
	 */
	private String populateQueryKey(String field, String queryKey) throws IOException {
		TokenStream tokenStream = analyzer.tokenStream(field, new StringReader(queryKey.trim()));
		CharTermAttribute attribute = tokenStream.getAttribute(CharTermAttribute.class);
		tokenStream.reset();//不添加会显示空指针错误
		String key = "";
		while (tokenStream.incrementToken()) {
			key += attribute.toString() + " ";
		}
		tokenStream.close();
		tokenStream.end();
		return key.trim();
	}
	
	
	public static void main(String[] args) throws IOException{
		Analyzer analyzer = new IKAnalyzer();
		TokenStream tokenStream = analyzer.tokenStream("啊大", new StringReader("你好啊，我们很好骗，菜单成都"));
		CharTermAttribute attribute = tokenStream.getAttribute(CharTermAttribute.class);
		tokenStream.reset();//不添加会显示空指针错误
		String key = "";
		while (tokenStream.incrementToken()) {
			key += attribute.toString() + " ";
		}
		tokenStream.close();
		tokenStream.end();
		System.out.println(key.trim());
	} 
}
