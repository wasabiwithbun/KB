package org.javacoo.cowswing.plugin.kbs.parser;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javacoo.cowswing.plugin.kbs.exception.ParserException;
import org.javacoo.cowswing.plugin.kbs.parser.document.DocumentParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * 文档解析器接口，实现类
 * <p>说明:</p>
 * <li>根据不同文档类型，返回相应解析器</li>
 * @author DuanYong
 * @since 2013-7-9 上午9:24:12
 * @version 1.0
 */
@Component("parserFactory")
public class DocumentParserFactoryImpl implements DocumentParserFactory{
	/**文档解析器配置参数bean*/
	@Resource(name = "documentParserConfigration")
	private DocumentParserConfigration documentConfiration;
	/**自动组装文档解析器MAP*/
	@Autowired
	private Map<String,DocumentParser> documentParserMap;
	private final static Log logger = LogFactory.getLog(DocumentParserFactoryImpl.class);
	/**
	 * 根据文档类型返回相应文档解析器
	 * @param docType 文档类型
	 * @return 文档解析器
	 * @throws Exception 
	 */
	public DocumentParser getDocumentParser(String docType) throws ParserException {
		checkDocumentType(docType);
		return documentParserMap.get(docType);
	}
	
	/**
	 * 文档类型检查
	 * @param docType 待检查的文档类型
	 * @throws Exception 
	 */
	private void checkDocumentType(String docType) throws ParserException{
		if(!documentConfiration.getFileContentType().contains(docType)){
			logger.error("不支持该文档类型:"+docType);
			throw new ParserException("文档解析异常:不支持该文档类型:"+docType);
		}
	}
	
	public void setDocumentConfiration(
			DocumentParserConfigration documentConfiration) {
		this.documentConfiration = documentConfiration;
	}
	
	
	

}
