package org.javacoo.cowswing.plugin.kbs.parser.document.impl;

import java.io.FileInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.javacoo.cowswing.plugin.kbs.exception.ParserException;
import org.springframework.stereotype.Component;

/**
 * 类型PPT的文档
 * <p>
 * 解析类型为PPT的文档
 * 
 * @author DuanYong
 * @since 2010-03-27
 * @version 1.0
 */
@Component("ppt")
public class PPTDocumentParserImpl extends AbstractDocumentParser {
	private final static Log logger = LogFactory
			.getLog(PPTDocumentParserImpl.class);

	@Override
	protected void parserDocument(String filePath) throws ParserException {
		if (file.length() == 0) {
			this.content = "";
		}
		try {
			logger.info("解析PPT文档：" + this.file);
			PowerPointExtractor pptExtractor = new PowerPointExtractor(
					new POIFSFileSystem(new FileInputStream(this.file)));
			this.content = pptExtractor.getText(true, true, true, true);
			this.title = this.file.getName();
		} catch (Exception e) {
			logger.error("解析PPT文档出错：" + e.getMessage(), e);
			throw new ParserException("解析PPT文档出错");
		}
	}

}
