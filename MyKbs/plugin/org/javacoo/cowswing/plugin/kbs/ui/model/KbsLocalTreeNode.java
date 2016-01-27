/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.model;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.lang.StringUtils;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsTypeBean;


/**
 * 本地文件TreeNode
 * 
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-7-17 上午10:22:42
 * @version 1.0
 */
public class KbsLocalTreeNode extends DefaultMutableTreeNode{
	private static final long serialVersionUID = 1L;
	private boolean explored = false;
	public KbsLocalTreeNode(KbsTypeBean kbsTypeBean) {
		setUserObject(kbsTypeBean);
	}

	public boolean getAllowsChildren() {
		return !getKbsType().isLeaf();
	}

	public boolean isLeaf() {
		return getKbsType().isLeaf();
	}

	public KbsTypeBean getKbsType() {
		return (KbsTypeBean) getUserObject();
	}

	public boolean isExplored() {
		return explored;
	}

	public String toString() {
		return getKbsType().getTypeName();
	}
	

	/**
	 * 取得相对目录
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-17 下午8:58:48
	 * @version 1.0
	 * @exception 
	 * @return
	 */
	public String getFullPath(){
		String typeCode = getKbsType().getTypeCode();
		if(StringUtils.isNotBlank(getKbsType().getTypeCode())){
			if(getKbsType().getTypeCode().contains(KbsConstant.SYSTEM_LINE_SPITE)){
				return getKbsType().getTypeCode().replaceAll(KbsConstant.SYSTEM_LINE_SPITE, KbsConstant.SYSTEM_SLANT_LINE_SPITE)+KbsConstant.SYSTEM_SEPARATOR;
			}else{
				return getKbsType().getTypeCode()+KbsConstant.SYSTEM_SEPARATOR;
			}
		}
		return "";
		
	}
	/**
	 * 取得绝对路径
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-17 下午9:02:56
	 * @version 1.0
	 * @exception 
	 * @return
	 */
	public String getAbsolutePath(){
		return KbsConstant.SYSTEM_ROOT_PATH + KbsConstant.SYSTEM_SEPARATOR + getFullPath();
	}
	public File getFile() {
		return new File(getKbsType().getTypeCode());
	}
	public boolean isDirectory() {
		File file = getFile();
		return file.isDirectory();
	}
	public void explore() {
		if (!isDirectory()){
			return;
		}
		if (!isExplored()) {
			File file = getFile();
			File[] children = file.listFiles();
			File tempFile = null;
			if(null != children){
				for (int i = 0; i < children.length; ++i){
					tempFile = children[i];
					if(tempFile.isDirectory()){
						add(createChildNode(tempFile.getParentFile().getAbsolutePath(),tempFile.getAbsolutePath(),tempFile.getName(),!hasChildDirectory(tempFile)));
					}
				}
			}
			explored = true;
		}
	}
	/**
	 * 检查当前目录是否有子目录
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-7-17 下午5:01:27
	 * @version 1.0
	 * @exception 
	 * @param file
	 * @return
	 */
	private boolean hasChildDirectory(File file){
		if(file.isDirectory()){
			File[] children = file.listFiles();
			if(null != children){
				for (int i = 0; i < children.length; ++i){
					if(children[i].isDirectory()){
						return true;
					}
				}
			}
		}
		return false;
	}
	private KbsLocalTreeNode createChildNode(String parentTypeCode,String typeCode,String typeName,boolean isLeaf){
		KbsTypeBean child = new KbsTypeBean();
		child.setLeaf(isLeaf);
		child.setParentTypeCode(parentTypeCode);
		child.setTypeCode(typeCode);
		child.setTypeName(typeName);
		return new KbsLocalTreeNode(child);
	}
	public static void main(String[] args){
		String typeCode = "default_work";
		String sp = "/";
		System.out.println(KbsConstant.SYSTEM_SEPARATOR);
		System.out.println(typeCode.replaceAll(KbsConstant.SYSTEM_LINE_SPITE, sp));
	}
}
