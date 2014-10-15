package org.javacoo.cowswing.plugin.kbs.parser.document.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.javacoo.cowswing.plugin.kbs.exception.ParserException;
import org.springframework.stereotype.Component;


/**
 * 类型为XLSX的文档
 * <p>
 * 解析类型为XLSX的文档
 * 
 * @author DuanYong
 * @since 2013-07-11
 * @version 1.0
 */
@Component("xlsx")
public class XLSXDocumentParserImpl extends AbstractDocumentParser{
	private final static Log logger = LogFactory.getLog(XLSXDocumentParserImpl.class);
	@Override
	protected void parserDocument(String filePath) throws ParserException {
		if(file.length()==0){
			this.content = "";
		}
	        try {
	        	XSSFExcelExtractor xlsxExtractor = new XSSFExcelExtractor(POIXMLDocument.openPackage(filePath));
	            this.content = xlsxExtractor.getText();
	            this.title = this.file.getName();
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("解析XLSX文档出错："+e.getMessage(),e);
	            throw new ParserException("解析XLSX文档出错");
	        }
		
	}

}
