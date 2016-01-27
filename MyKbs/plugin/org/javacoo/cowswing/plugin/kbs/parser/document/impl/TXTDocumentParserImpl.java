package org.javacoo.cowswing.plugin.kbs.parser.document.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javacoo.cowswing.plugin.kbs.exception.ParserException;
import org.springframework.stereotype.Component;



/**
 * 类型为TXT的文档
 * <p>
 * 解析类型为TXT的文档
 * @author DuanYong
 * @since 2010-03-27
 * @version 1.0
 */
@Component("txt")
public class TXTDocumentParserImpl extends AbstractDocumentParser{
	private final static Log logger = LogFactory.getLog(TXTDocumentParserImpl.class);
	@SuppressWarnings("unchecked")
	@Override
	protected void parserDocument(String filePath) throws ParserException {
		if(file.length()==0){
			this.content =  "";
		}
		InputStream in = null;
		try
		{
			in = new FileInputStream(file);
			List<String> lineList = IOUtils.readLines(in, "GB2312");	
			this.content = populateValue(lineList);
			this.title = this.file.getName();
		}
		catch (IOException e)
		{
			logger.error("解析TXT文件失败："+e.getMessage());
			throw new ParserException("解析TXT文件失败");
		}
		finally
		{
			if(null != in)
			{
				try {
					in.close();
					in = null;
				} catch (IOException e) {
					logger.error("解析TXT文件失败："+e.getMessage());
					throw new ParserException("解析TXT文件失败");
				}
			}
		}
	}
	/**
	 * 组装文档内容
	 * @param lineList
	 * @return
	 */
	private String populateValue(List<String> lineList){
		StringBuilder sb = new StringBuilder();
		for(String str : lineList){
			sb.append(str);
		}
		return sb.toString();
	}
	


}
