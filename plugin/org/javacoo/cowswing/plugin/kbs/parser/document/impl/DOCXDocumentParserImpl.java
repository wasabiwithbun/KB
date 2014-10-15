package org.javacoo.cowswing.plugin.kbs.parser.document.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.javacoo.cowswing.plugin.kbs.exception.ParserException;
import org.springframework.stereotype.Component;

/**
 * 类型为DOCX的文档
 * <p>
 * 解析类型为DOCX的文档
 * 
 * @author DuanYong
 * @since 2013-07-11
 * @version 1.0
 */
@Component("docx")
public class DOCXDocumentParserImpl extends AbstractDocumentParser{
	private final Log logger = LogFactory.getLog(DOCXDocumentParserImpl.class);
	@Override
	protected void parserDocument(String filePath) throws ParserException {
		if(this.file.length()==0){
			this.content = "";
		}
		try {
			XWPFWordExtractor wordxExtractor = new XWPFWordExtractor(POIXMLDocument.openPackage(filePath));
			this.content = wordxExtractor.getText();
			this.title = this.file.getName();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("解析DOCX文档出错："+e.getMessage());
			throw new ParserException("解析DOCX文档出错");
		}
	}

}
