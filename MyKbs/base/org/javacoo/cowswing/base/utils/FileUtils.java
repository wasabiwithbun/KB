/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.base.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.javacoo.cowswing.base.constant.Constant;

/**
 * 文件操作类
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-4-23 下午4:57:11
 * @version 1.0
 */
public class FileUtils {
	
	/**
	 * 创建文件夹
	 * <p>
	 * 不存在则创建
	 * 
	 * @param path
	 *            文件夹路径
	 */
	public static void createDir(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	/**
	 * 删除文件夹
	 * <p>
	 * 存在则删除
	 * 
	 * @param path
	 *            文件夹路径
	 */
	public static void deleteDir(String path) {
		File dir = new File(path);
		if (dir.exists()) {
			dir.delete();
		}
	}

	/**
	 * 检查文件夹是否不为空
	 * <p>
	 * true:不为空
	 * 
	 * @param path
	 *            文件夹路径
	 * @return true/false
	 */
	public static boolean checkDirIsNotEmpty(String path) {
		File dir = new File(path);
		if (dir.exists()) {
			File[] fileList = dir.listFiles();
			return fileList.length > 0 ? true : false;
		}
		return false;
	}
	/**
	 * 检查文件是否存在
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-1-7 上午10:39:03
	 * @version 1.0
	 * @exception 
	 * @param path
	 * @return
	 */
	public static boolean checkFileIsexists(String path){
		File file = new File(path);
		if (file.exists()) {
			return true;
		}
		return false;
	}

	/**
	 * 递归得到指定文件夹下及子文件内的所有文件
	 * 
	 * @param file
	 *            指定文件夹file对象
	 * @return 文件列表
	 */
	public static List<File> getFileList(List<File> resultList, File file) {
		if (file.isFile()) {
			resultList.add(file);
		} else {
			File[] fileList = file.listFiles();
			for (File f : fileList) {
				getFileList(resultList, f);
			}
		}
		return resultList;
	}

	/**
	 * 创建文件
	 * <p>
	 * 不存在则创建
	 * 
	 * @param path
	 *            文件夹路径
	 */
	public static void createFile(String newFilePath) throws IOException {
		File file = new File(newFilePath);
		if (!file.exists()) {
			file.createNewFile();
		}
	}

	/**
	 * 将文件创建在指定路径下
	 * 
	 * @param file
	 *            原文件
	 * @param newFilePath
	 *            保存路径
	 * @return 错误信息，默认为空
	 */
	public static boolean createFile(File file, String newFilePath) {
		InputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(file);
			byte[] bs = IOUtils.toByteArray(in);
			File newFile = new File(newFilePath);
			if(!newFile.exists()){
				File dir = new File(newFile.getParent());
				if(!dir.exists()){
					dir.mkdirs();
				}
			}
			out = new FileOutputStream(newFile, false);
			// 拷贝数据到新文件
			IOUtils.write(bs, out);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (null != in) {
				try {
					in.close();
					in = null;
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
			if (null != out) {
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 删除文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @return true/false true 成功
	 */
	public static void deleteFile(String filePath) {
		File file = new File(filePath);
		if(file.exists()){
			file.delete();
		}
	}
	/**
	 * 删除所有文件
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-1-3 下午5:37:50
	 * @version 1.0
	 * @exception 
	 * @param filePath
	 */
	public static void deleteAllFile(String filePath){
		File file = new File(filePath);
		if (file.isFile()) {
			file.delete();
		} else {
			File[] fileList = file.listFiles();
			if(null != fileList && fileList.length > 0){
				for (File f : fileList) {
					deleteAllFile(f.getAbsolutePath());
				}
			}
			file.delete();
		}
	}
	/**
	 * 删除文件
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-4-23 下午5:13:23
	 * @version 1.0
	 * @exception 
	 * @param filePath 文件路径
	 * @param deleteParentDirOnEmpty 当父目录为空时是否删除目录
	 */
	public static void deleteFile(String filePath,boolean deleteParentDirOnEmpty) {
		File file = new File(filePath);
		if(file.exists()){
			file.delete();
		}
		if(deleteParentDirOnEmpty ){
			File[] files = file.getParentFile().listFiles();
			if(null == files || files.length == 0){
				file.getParentFile().delete();
			}
		}
	}

	/**
	 * 得到所有索引目录
	 * 
	 */
	public static void getIndexDirs(String indexDir, List<String> dirList) {
		File file = new File(indexDir);
		File nowfile;
		String files[] = null;
		files = file.list();
		if (null != files)
			for (int i = 0; i < files.length; i++) {
				nowfile = new File(indexDir + File.separator + files[i]);
				if (nowfile.isDirectory()) {
					dirList.add(files[i]);
				}
			}
	}

	/**
	 * 得到所有索引文件
	 * 
	 */
	public static void getIndexFiles(String indexDir, List<String> fileList) {
		File file;
		File nowfile;
		String files[] = null;
		file = new File(indexDir);
		files = file.list();
		/**
		 * 如果文件是目录，那么继续找出目录下的文件
		 * 
		 */
		if (null != files)
			for (int i = 0; i < files.length; i++) {
				nowfile = new File(indexDir + File.separator + files[i]);
				if (nowfile.isDirectory()) {
					getIndexFiles(nowfile.getAbsolutePath(), fileList);
				} else {
					fileList.add(files[i]);
				}
			}

	}

	/**
	 * 得到指定目录下系统指定类型的所有文件
	 * 
	 */
	public static void getAllFiles(File dir, List<File> fileList) {
		File files[] = null;
		files = dir.listFiles();
		/**
		 * 如果文件是目录，那么继续找出目录下的文件
		 * 
		 */
		if (null != files)
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					getAllFiles(files[i], fileList);
				} else {
					fileList.add(files[i]);
				}
			}

	}

	/**
	 * 根据文件名取得其文件类型
	 * 
	 * @param fileName
	 *            文件名
	 * @return String 文件类型
	 * @throws Exception
	 */
	public static String getFileTypeString(String fileName) {
		String fileTypeStr = "";

		int p = fileName.lastIndexOf(".");
		fileTypeStr = fileName.substring(p + 1);
		return fileTypeStr;
	}

	/**
	 * 删除目录
	 * 
	 * @param delpath
	 */
	public static boolean delDirectory(String delpath) {
		try {
			File file = new File(delpath);
			if (!file.isDirectory()) {
				if (!file.delete()) {
					return false;
				}
			} else if (file.isDirectory()) {
				File[] filelist = file.listFiles();
				for (File delfile : filelist) {
					if (!delfile.isDirectory()) {
						if (!delfile.delete()) {
							return false;
						}
					} else if (delfile.isDirectory())
						if (!(delDirectory(delfile.getAbsolutePath()))) {
							return false;
						}
				}
				if (!file.delete()) {
					return false;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> readFile(String filePath,String charset){
		List<String> fileContentList = new ArrayList<String>();
		InputStream in = null;
		File file = null;
		try {
			file = new File(filePath);
			in = new FileInputStream(file);
			fileContentList = IOUtils.readLines(in,charset);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
					in = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return fileContentList;
	}
	
	public static boolean writeFile(List<String> writeList,String filePath){
		FileOutputStream out = null;
		try {
			File newFile = new File(filePath);
			out = new FileOutputStream(newFile, false);
			// 拷贝数据到新文件
			IOUtils.writeLines(writeList, null, out,Constant.ENCODING_GBK);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (null != out) {
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}
	public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }
    public static void main(String[] args){
    	try {
			createFile("F:\\p2\\MyKbs\\_update\\update.update");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
}
