package org.javacoo.cowswing.plugin.kbs.parser;

import java.util.HashMap;
import java.util.Map;

import org.javacoo.cowswing.base.constant.Config;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 文档解析参数配置
 * 
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-9 上午9:31:15
 * @version 1.0
 */
@Component("documentParserConfigration")
public class DocumentParserConfigration {
	/**允许解析的文件类型**/
	private String fileContentType ;
    /**允许解析的文件类型**/
    public String getFileContentType() {
		this.fileContentType = Config.COWSWING_CONFIG_MAP.get(KbsConstant.COWSWING_KBS_CONFIG_KEY_INIT).get(KbsConstant.COWSWING_KBS_CONFIG_KEY_INIT_FILE_CONTENT_TYPE);
		return this.fileContentType;
	}

}
