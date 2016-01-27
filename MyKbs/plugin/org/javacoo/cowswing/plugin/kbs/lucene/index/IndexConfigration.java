/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.lucene.index;

import org.apache.commons.lang.StringUtils;
import org.javacoo.cowswing.base.constant.Config;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.springframework.stereotype.Component;

/**
 * 索引参数配置
 * <p>说明:</p>
 * <li>定义索引存放目录和其他配置参数</li>
 * @author DuanYong
 * @since 2013-6-30 下午4:11:11
 * @version 1.0
 */
@Component("indexConfigration")
public class IndexConfigration {
	/**根目录*/
	private String rootDir = KbsConstant.SYSTEM_ROOT_PATH;
    /**索引存放目录*/
	private String indexDir = "";
	/**目标文件存放目录*/
	private String docDir = "";
	/**内存存放文档最大数*/
	private long ramMaxSize;
	/**索引文件最大大小*/
	private long maxFileSize;
	/**解析文档类型*/
	private String fileContentType;
	
	public String getIndexDir() {
		//如果未配置索引存放目录,则按照根目录+索引文件存放文件夹名称建立文件夹
		if(!StringUtils.isNotBlank(this.indexDir)){
			checkRootDir();
			return rootDir +KbsConstant.SYSTEM_SEPARATOR+KbsConstant.DEFAULT_INDEX_DIR;
		}
		return indexDir;
	}
	public void setIndexDir(String indexDir) {
		this.indexDir = indexDir;
	}
	public String getDocDir() {
		//如果未配置索引存放目录,则按照根目录+索引文件存放文件夹名称建立文件夹
		if(!StringUtils.isNotBlank(this.docDir)){
			checkRootDir();
			return rootDir +KbsConstant.SYSTEM_SEPARATOR;
		}
		return docDir;
	}
	public void setDocDir(String docDir) {
		this.docDir = docDir;
	}
	public String getRootDir() {
		return rootDir;
	}
	/**
	 * 检查根目录是否配置
	 */
	private void checkRootDir(){
		if(!StringUtils.isNotBlank(this.rootDir)){
			this.rootDir = KbsConstant.SYSTEM_ROOT_PATH;
		}
	}
	public void setRamMaxSize(long ramMaxSize) {
		this.ramMaxSize = ramMaxSize;
	}
	public long getRamMaxSize() {
		this.ramMaxSize = new Long(Config.COWSWING_CONFIG_MAP.get(KbsConstant.COWSWING_KBS_CONFIG_KEY_INIT).get(KbsConstant.COWSWING_KBS_CONFIG_KEY_INIT_RAM_MAX_SIZE));
		return this.ramMaxSize;
	}
	/**
	 * @return the fileContentType
	 */
	public String getFileContentType() {
		this.fileContentType = Config.COWSWING_CONFIG_MAP.get(KbsConstant.COWSWING_KBS_CONFIG_KEY_INIT).get(KbsConstant.COWSWING_KBS_CONFIG_KEY_INIT_FILE_CONTENT_TYPE);
		return fileContentType;
	}
	/**
	 * @param fileContentType the fileContentType to set
	 */
	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}
	/**
	 * @return the maxFileSize
	 */
	public long getMaxFileSize() {
		this.maxFileSize = new Long(Config.COWSWING_CONFIG_MAP.get(KbsConstant.COWSWING_KBS_CONFIG_KEY_INIT).get(KbsConstant.COWSWING_KBS_CONFIG_KEY_INIT_MAX_FILE_SIZE)) * 1024 * 1024;
		return this.maxFileSize;
	}
	/**
	 * @param maxFileSize the maxFileSize to set
	 */
	public void setMaxFileSize(long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}
	
	
}
