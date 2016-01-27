/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;

/**
 * 索引资源帮助类
 * <p>说明:</p>
 * <li>索引资源帮助类</li>
 * @author DuanYong
 * @since 2013-7-3 上午9:39:53
 * @version 1.0
 */
public class IndexResourceUtil {
	/**
	 * 得到指定索引目录路径
	 * @param rootDirPath 索引根目录
	 * @param indexDirName 目录名称
	 * @return
	 */
	public static String getDirPath(String rootDirPath,String indexDirName){
		StringBuilder str = new StringBuilder();
		return str.append(rootDirPath).append(KbsConstant.SYSTEM_SEPARATOR).append(indexDirName).toString();
	}
	/**
	 * 得到索引目录路径
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-9 下午2:49:02
	 * @version 1.0
	 * @exception 
	 * @param indexDirName
	 * @return
	 */
	public static String getIndexDirPath(String indexDirName) {   
        return (new StringBuffer(KbsConstant.SYSTEM_ROOT_PATH).append(KbsConstant.SYSTEM_SEPARATOR).append(indexDirName)).toString();   
    }
	/**
	 * 取得文档绝对路径
	 * 
	 * @param path
	 *            文档相对路径
	 * @return 文档绝对路径
	 */
	public static String getAbsolutePath(String path) {
		return (new StringBuffer(KbsConstant.SYSTEM_ROOT_PATH).append(KbsConstant.SYSTEM_SEPARATOR).append(path)).toString();
	}
	/**
	 * 判断索引目录是否存在
	 * @param INDEX_DIR
	 * @return
	 */
	public static boolean isEmptyIndexDir(File INDEX_DIR){
		return INDEX_DIR.exists() && INDEX_DIR.listFiles().length > 0 ? false : true;
	}
	/**
	 * 从文档全路径取得文档后缀
	 * 
	 * @param docPath
	 *            文档全路径
	 * @return 文档后缀
	 */
	public static String getDocType(String docPath) {
		int p = docPath.lastIndexOf(".");
		return docPath.substring(p + 1);
	}
	/**
	 * 得到所有索引目录
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-8 下午5:36:48
	 * @version 1.0
	 * @exception 
	 * @param indexDir
	 * @param dirList
	 */
	public static List<String> getIndexDirs(String indexDir) {
		List<String> dirList = new ArrayList<String>();
		File file = new File(indexDir);
		File nowfile;
		String files[] = null;
		files = file.list();
		if (null != files){
			for (int i = 0; i < files.length; i++) {
				nowfile = new File(indexDir + File.separator + files[i]);
				if (nowfile.isDirectory()) {
					dirList.add(files[i]);
				}
			}
		}
		return dirList;
	}
}
