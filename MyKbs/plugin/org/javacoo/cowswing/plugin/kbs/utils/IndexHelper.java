/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.utils;

import java.io.File;
import java.text.SimpleDateFormat;

import org.apache.commons.io.FilenameUtils;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.domain.KnowledgePoint;

/**
 * 
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-7-29 下午3:57:05
 * @version 1.0
 */
public class IndexHelper {
	/**
	 * 创建知识点
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-7-19 下午2:42:56
	 * @version 1.0
	 * @exception 
	 * @param file
	 * @return
	 */
	public static KnowledgePoint createKnowledgePoint(File file,String rootDir,boolean isBigFile){
		KnowledgePoint knowledgePoint = new KnowledgePoint();
		knowledgePoint.setId(String.valueOf(file.getPath().hashCode()));
		knowledgePoint.setModule(KbsConstant.SYSTEM_MODULE_SOURCE);
		knowledgePoint.setTitle(file.getName());
		knowledgePoint.setKeyWord(FilenameUtils.getBaseName(file.getName()));
		knowledgePoint.setIndexDirName(KbsConstant.DEFAULT_INDEX_MODULE_DIR);
		knowledgePoint.setFileDirs(String.valueOf(rootDir.hashCode()));
		knowledgePoint.setShowDate((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(file.lastModified()));
		knowledgePoint.setFilePath(file.getPath());
		knowledgePoint.setTypeDesc(file.getParent());
		knowledgePoint.setTypeCode(file.getParent());
		knowledgePoint.setOrigin(KbsConstant.ORIGIN_LOCAL);
		knowledgePoint.setPurview(KbsConstant.SEARCH_PURVIEW_DOWNLOAD);
		knowledgePoint.setBigFile(isBigFile);
		return knowledgePoint;
	}
	/**
	 * 创建删除知识点对象
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-8-2 上午11:47:41
	 * @version 1.0
	 * @exception 
	 * @param path
	 * @return
	 */
	public static KnowledgePoint createDeleteKnowledgePoint(String path){
		KnowledgePoint knowledgePoint = new KnowledgePoint();
		knowledgePoint.setId(String.valueOf(path.hashCode()));
		knowledgePoint.setIndexDirName(KbsConstant.DEFAULT_INDEX_MODULE_DIR);
		knowledgePoint.setModule(KbsConstant.SYSTEM_MODULE_SOURCE);
		return knowledgePoint;
	}
}
