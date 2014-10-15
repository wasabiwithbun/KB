package org.javacoo.cowswing.plugin.kbs.parser.document.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.javacoo.cowswing.plugin.kbs.exception.ParserException;
import org.springframework.stereotype.Component;


/**
 * 类型PPTX的文档
 * <p>
 * 解析类型为PPTX的文档
 * 
 * @author DuanYong
 * @since 2013-07-11
 * @version 1.0
 */
@Component("pptx")
public class PPTXDocumentParserImpl extends AbstractDocumentParser {
	private final static Log logger = LogFactory.getLog(PPTXDocumentParserImpl.class);

	@Override
	protected void parserDocument(String filePath) throws ParserException {
		if(file.length()==0){
			this.content = "";
		}
	        try {
	        	XSLFPowerPointExtractor pptxExtractor = new XSLFPowerPointExtractor(POIXMLDocument.openPackage(filePath));
	            this.content = pptxExtractor.getText(true, true, true);
	            this.title = this.file.getName();
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("解析PPTX文档出错："+e.getMessage(),e);
	            throw new ParserException("解析PPTX文档出错");
	        }
	}

}
