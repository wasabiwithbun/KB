/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.dialog;

import java.awt.event.ActionEvent;

import javax.annotation.Resource;
import javax.swing.JComponent;
import javax.swing.tree.TreePath;

import org.apache.commons.lang.StringUtils;
import org.javacoo.cowswing.base.constant.Constant;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.base.utils.MsgDialogUtil;
import org.javacoo.cowswing.core.event.CowSwingEvent;
import org.javacoo.cowswing.core.event.CowSwingListener;
import org.javacoo.cowswing.core.event.type.CowSwingEventType;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.event.KbsEventType;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsTypeBean;
import org.javacoo.cowswing.plugin.kbs.ui.model.KbsTypeTreeNode;
import org.javacoo.cowswing.plugin.kbs.ui.view.panel.KbsTypeSettingPanel;
import org.javacoo.cowswing.plugin.kbs.ui.view.panel.TypeTreePanel;
import org.javacoo.cowswing.ui.view.dialog.AbstractDialog;
import org.springframework.stereotype.Component;

/**
 * 设置知识分类
 * <p>
 * 说明:
 * </p>
 * <li></li>
 * 
 * @author DuanYong
 * @since 2013-7-13 下午1:25:28
 * @version 1.0
 */
@Component("kbsTypeSettingDialog")
public class KbsTypeSettingDialog extends AbstractDialog implements
		CowSwingListener {
	private static final long serialVersionUID = 1L;
	/** 设置知识分类设置面板 */
	@Resource(name = "kbsTypeSettingPanel")
	private KbsTypeSettingPanel kbsTypeSettingPanel;
	/**知识分类*/
	@Resource(name="typeTreePanel")
	private TypeTreePanel typeTreePanel;
	/** 知识分类 */
	private KbsTypeBean kbsTypeBean;
	/**类别*/
	private String type;
	/**分类主键*/
	private Integer kbsTypeId;
	/**当前TreeNode*/
	private KbsTypeTreeNode currentNode;
	/**当前父TreeNode*/
	private KbsTypeTreeNode parentNode;
	public KbsTypeSettingDialog(){
		super(350,300,true);
	}
	@Override
	public JComponent getCenterPane() {
		if (centerPane == null) {
			centerPane = kbsTypeSettingPanel;
		}
		return centerPane;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.javacoo.cowswing.ui.view.dialog.AbstractDialog#
	 * finishButtonActionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	protected void finishButtonActionPerformed(ActionEvent event) {
		kbsTypeBean = new KbsTypeBean();
		kbsTypeBean = kbsTypeSettingPanel.getData();
		if(StringUtils.isNotBlank(kbsTypeBean.getTypeName())){
			if(Constant.OPTION_TYPE_ADD == this.type){
				kbsTypeBean.setCowSwingEvent(new CowSwingEvent(this,KbsEventType.KbsTypeChangeEvent));
				kbsTypeBean.setTypeCode(kbsTypeBean.getParentTypeCode()+"_"+kbsTypeBean.getTypeCode());
				typeTreePanel.getKbsTypeService().insert(kbsTypeBean,KbsConstant.SQLMAP_ID_INSERT_KBS_TYPE);
				//设置父节点
				if(currentNode.isLeaf()){
					KbsTypeBean parentKbsTypeBean = new KbsTypeBean();
					parentKbsTypeBean.setId(currentNode.getKbsType().getId());
					parentKbsTypeBean.setLeaf(false);
					typeTreePanel.getKbsTypeService().update(parentKbsTypeBean,KbsConstant.SQLMAP_ID_UPDATE_KBS_TYPE);
				}
				typeTreePanel.insertNode(currentNode, kbsTypeBean);
			}else{
				kbsTypeBean.setId(this.kbsTypeId);
				kbsTypeBean.setCowSwingEvent(new CowSwingEvent(this,KbsEventType.KbsTypeUpdateEvent));
				typeTreePanel.getKbsTypeService().update(kbsTypeBean,KbsConstant.SQLMAP_ID_UPDATE_KBS_TYPE);
				typeTreePanel.updateNode(currentNode, kbsTypeBean);
			}
			this.dispose();
		}else{
			MsgDialogUtil.createMessageDialog(LanguageLoader.getString("Kbs.type_setting_typeName_is_empty"));
		}
		
	}
	
	protected void initData(String type) {
		this.type = type;
		TreePath parentPath = typeTreePanel.getTree().getSelectionPath();
		currentNode = (KbsTypeTreeNode) parentPath.getLastPathComponent();
		System.out.println("currentNode.getFullPath():"+currentNode.getFullPath());
		parentNode = (KbsTypeTreeNode)currentNode.getParent();
		if(Constant.OPTION_TYPE_MODIFY == type){
			kbsTypeBean = currentNode.getKbsType();
			kbsTypeBean.setParentTypeName(parentNode.getKbsType().getTypeName());
			kbsTypeBean.setParentTypeCode(parentNode.getKbsType().getTypeCode());
			this.kbsTypeId = kbsTypeBean.getId();
		}else{
			kbsTypeBean = new KbsTypeBean();
			kbsTypeBean.setParentTypeName(currentNode.getKbsType().getTypeName());
			kbsTypeBean.setParentTypeCode(currentNode.getKbsType().getTypeCode());
		}
		fillJTabbedPane();
	}
	
	public void dispose(){
		super.dispose();
		centerPane = null;
	}
	/**
	 * 填充JTabbedPane值
	 * <p>方法说明:</p>
	 * @auther DuanYong
	 * @since 2012-12-3 下午12:20:32
	 * @return void
	 */
	private void fillJTabbedPane(){
		logger.info("填充JTabbedPane值");
		kbsTypeSettingPanel.initData(kbsTypeBean);
	}

}
