package org.javacoo.cowswing.plugin.kbs.parser.document.impl;

import java.io.FileInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.javacoo.cowswing.plugin.kbs.exception.ParserException;
import org.springframework.stereotype.Component;


/**
 * 类型为XLS的文档
 * <p>
 * 解析类型为XLS的文档
 * 
 * @author DuanYong
 * @since 2010-03-27
 * @version 1.0
 */
@Component("xls")
public class XLSDocumentParserImpl extends AbstractDocumentParser{
	private final static Log logger = LogFactory.getLog(XLSDocumentParserImpl.class);
	@Override
	protected void parserDocument(String filePath) throws ParserException {
		if(file.length()==0){
			this.content = "";
		}
	        try {
	            ExcelExtractor xlsExtractor = new ExcelExtractor(new POIFSFileSystem(new FileInputStream(this.file)));
	            this.content = xlsExtractor.getText();
	            this.title = this.file.getName();
	        } catch (Exception e) {
	            e.printStackTrace();
	            logger.error("解析XLS文档出错："+e.getMessage(),e);
	            throw new ParserException("解析XLS文档出错");
	        }
		
	}

}
