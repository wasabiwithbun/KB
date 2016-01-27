/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.action;

import java.awt.event.ActionEvent;

import javax.annotation.Resource;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;

import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.main.CowSwingMainFrame;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsTypeBean;
import org.javacoo.cowswing.plugin.kbs.ui.model.KbsTypeTreeNode;
import org.javacoo.cowswing.plugin.kbs.ui.view.panel.TypeTreePanel;
import org.springframework.stereotype.Component;

/**
 * 删除知识分类
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-13 下午2:41:16
 * @version 1.0
 */
@Component("deleteKbsTypeAction")
public class DeleteKbsTypeAction extends AbstractAction{
	private static final long serialVersionUID = 1L;
	/**主窗体*/
	@Resource(name="cowSwingMainFrame")
	private CowSwingMainFrame cowSwingMainFrame;
	/**知识分类*/
	@Resource(name="typeTreePanel")
	private TypeTreePanel typeTreePanel;
	public DeleteKbsTypeAction(){
		super("",ImageLoader.getImageIcon("CrawlerResource.kbs_folder_delete"));
		putValue(SHORT_DESCRIPTION, LanguageLoader.getString("Kbs.type_del_btn"));
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		TreePath path = typeTreePanel.getTree().getSelectionPath();
		if (path.getPathCount() == 1) {
			JOptionPane.showMessageDialog(cowSwingMainFrame,LanguageLoader.getString("Kbs.type_setting_del_root_info"));
			return;
		}
		int result = JOptionPane.showConfirmDialog(null, LanguageLoader.getString("Kbs.type_del_confirm_info"),LanguageLoader.getString("Common.deleteConfirm"), JOptionPane.YES_NO_OPTION); 
		if(result == 0){
			KbsTypeTreeNode currNode = (KbsTypeTreeNode) path.getLastPathComponent();
			if (KbsConstant.KBS_TYPE_ROOT_CODE.equals(currNode.getKbsType().getTypeCode())
					|| KbsConstant.KBS_TYPE_CHILD_DEFAULT_CODE.equals(currNode.getKbsType().getTypeCode())
					|| KbsConstant.KBS_TYPE_CHILD_ADDRESS_CODE.equals(currNode.getKbsType().getTypeCode())
					|| KbsConstant.KBS_TYPE_CHILD_BOOKMARK_CODE.equals(currNode.getKbsType().getTypeCode())) {
				return;
			}
			KbsTypeTreeNode parentNode = (KbsTypeTreeNode)currNode.getParent();
			//如果父节点下没有子节点,则更新节点
			if(parentNode.getChildCount() <= 1){
		        KbsTypeBean parentKbsTypeBean = new KbsTypeBean();
		        parentKbsTypeBean.setId(parentNode.getKbsType().getId());
		        parentKbsTypeBean.setLeaf(true);
				typeTreePanel.getKbsTypeService().update(parentKbsTypeBean,KbsConstant.SQLMAP_ID_UPDATE_KBS_TYPE);
		    }
			//删除节点
			KbsTypeBean currKbsTypeBean = new KbsTypeBean();
			currKbsTypeBean.setId(currNode.getKbsType().getId());
			typeTreePanel.getKbsTypeService().delete(currKbsTypeBean,KbsConstant.SQLMAP_ID_DELETE_BY_ID_KBS_TYPE);
			typeTreePanel.deleteNode(currNode);
		}
	}

}
