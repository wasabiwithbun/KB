package org.javacoo.cowswing.plugin.kbs.parser.document.impl;

import java.io.File;

import org.javacoo.cowswing.plugin.kbs.exception.ParserException;
import org.javacoo.cowswing.plugin.kbs.parser.document.DocumentParser;

/**
 * 文档解析抽象类
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-9 上午10:10:52
 * @version 1.0
 */
public abstract class AbstractDocumentParser implements DocumentParser {
	/** 文件对象 */
	protected File file;
	/** 文档内容 */
	protected String content = "";
	/** 文档标题 */
	protected String title = "";

	/**
	 * 取得文档内容
	 * 
	 * @return 文档内容
	 */
	public String getContent() {
		return this.content;
	}

	/**
	 * 取得文档标题
	 * 
	 * @return 文档标题
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * 解析文档
	 * 
	 * @param 文档路径
	 * @throws Exception
	 */
	public final void parser(String filePath) throws ParserException {
		this.file = new File(filePath);
		parserDocument(filePath);
	}

	/**
	 * 子类实现方法，解析文档
	 */
	protected abstract void parserDocument(String filePath) throws ParserException;

	/**
	 *解析解析HTML代码
	 */
	public void parserHtmlCode(String content) throws ParserException {
	}
}
