/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.model;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.commons.lang.StringUtils;
import org.javacoo.cowswing.base.service.ICowSwingService;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsTypeBean;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsTypeCriteria;
import org.springframework.util.CollectionUtils;


/**
 * 知识分类TreeNode
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-11 下午4:13:21
 * @version 1.0
 */
public class KbsTypeTreeNode extends DefaultMutableTreeNode{
	private static final long serialVersionUID = 1L;
	private boolean explored = false;
    private ICowSwingService<KbsTypeBean, KbsTypeCriteria> kbsTypeService;
	public KbsTypeTreeNode(KbsTypeBean kbsTypeBean,ICowSwingService<KbsTypeBean, KbsTypeCriteria> kbsTypeService) {
		setUserObject(kbsTypeBean);
		this.kbsTypeService = kbsTypeService;
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

	
	public void explore() {
		if (getKbsType().isLeaf()){
			return;
		}
		if (!isExplored()) {
			KbsTypeCriteria q = new KbsTypeCriteria();
			q.setTypeCode(getKbsType().getTypeCode());
			List<KbsTypeBean> childList = kbsTypeService.getList(q, KbsConstant.SQLMAP_ID_GET_LIST_KBS_TYPE);
			if(!CollectionUtils.isEmpty(childList)){
				for(KbsTypeBean kbsTypeBean : childList){
					add(new KbsTypeTreeNode(kbsTypeBean,kbsTypeService));
				}
			}
			explored = true;
		}
	}
	
	public static void main(String[] args){
		String typeCode = "default_work";
		String sp = "/";
		System.out.println(KbsConstant.SYSTEM_SEPARATOR);
		System.out.println(typeCode.replaceAll(KbsConstant.SYSTEM_LINE_SPITE, sp));
	}
}
