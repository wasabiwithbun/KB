package org.javacoo.cowswing.plugin.kbs.parser.document;

import org.javacoo.cowswing.plugin.kbs.exception.ParserException;

/**
 * 文档解析器接口
 * <p>说明:</p>
 * <li>定义解析不同类型文档所需要的公共方法</li>
 * @author DuanYong
 * @since 2013-7-9 上午9:33:12
 * @version 1.0
 */
public interface DocumentParser {
	/**
	 * 解析文档
	 * 
	 * @param 文档路径
	 */
	void parser(String filePath) throws ParserException;

	public void parserHtmlCode(String content) throws ParserException;

	/**
	 * 取得文档内容
	 * 
	 * @return 文档内容
	 */
	String getContent();

	/**
	 * 取得文档标题
	 * 
	 * @return 文档标题
	 */
	String getTitle();

}
