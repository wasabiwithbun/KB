package org.javacoo.cowswing.plugin.kbs.parser;

import org.javacoo.cowswing.plugin.kbs.exception.ParserException;
import org.javacoo.cowswing.plugin.kbs.parser.document.DocumentParser;

/**
 * 文档解析器接口
 * <p>说明:</p>
 * <li>根据不同文档类型，返回相应解析器</li>
 * @author DuanYong
 * @since 2013-7-9 上午9:24:42
 * @version 1.0
 */
public interface DocumentParserFactory {
	/**
	 * 根据文档类型返回相应文档解析器
	 * @param docType 文档类型
	 * @return 文档解析器
	 * @throws ParserException 
	 */
	DocumentParser getDocumentParser(String docType) throws ParserException;
}
