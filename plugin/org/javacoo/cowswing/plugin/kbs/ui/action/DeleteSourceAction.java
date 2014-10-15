/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.base.utils.FileUtils;
import org.javacoo.cowswing.core.event.CowSwingEvent;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.domain.KnowledgePoint;
import org.javacoo.cowswing.plugin.kbs.event.KbsEventType;
import org.javacoo.cowswing.plugin.kbs.lucene.index.Index;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleSourceBean;
import org.javacoo.cowswing.plugin.kbs.ui.model.KbsModuleSourceTabelModel;
import org.javacoo.cowswing.plugin.kbs.ui.view.panel.KbsModuleSourceListPanel;
import org.springframework.stereotype.Component;

/**
 * 删除资源Action
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-15 下午5:04:49
 * @version 1.0
 */
@Component("deleteSourceAction")
public class DeleteSourceAction  extends AbstractAction{
	private static final long serialVersionUID = 1L;
	/**资源集合面板*/
	@Resource(name="kbsModuleSourceListPanel")
	private KbsModuleSourceListPanel kbsModuleSourceListPanel;
	private JTable moduleSourceTable;
	/**资源model*/
	private KbsModuleSourceTabelModel kbsModuleSourceTabelModel;
	/**索引管理*/
	@Resource(name="indexManager")
    private Index index;
	public DeleteSourceAction(){
		super(LanguageLoader.getString("Kbs.module_source_delete_btn"),ImageLoader.getImageIcon("CrawlerResource.kbs_source_delete"));
		putValue(SHORT_DESCRIPTION, LanguageLoader.getString("Kbs.module_source_delete_btn"));
		this.setEnabled(false);
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		moduleSourceTable = kbsModuleSourceListPanel.getKbsModuleSourceTable();
		if(moduleSourceTable.getSelectedRow() != -1){
			int result = JOptionPane.showConfirmDialog(null, LanguageLoader.getString("Common.deleteConfirm"),LanguageLoader.getString("Common.confirm"), JOptionPane.YES_NO_OPTION); 
			if(result == 0){
				kbsModuleSourceTabelModel = (KbsModuleSourceTabelModel)moduleSourceTable.getModel();
				List<Integer> resourceIdList = new ArrayList<Integer>();
				List<String> filePathList = new ArrayList<String>();
				KbsModuleSourceBean tempKbsModuleSourceBean = null;
				for(Integer selectRow : moduleSourceTable.getSelectedRows()){
					tempKbsModuleSourceBean = kbsModuleSourceTabelModel.getRowObject(selectRow);
					resourceIdList.add(tempKbsModuleSourceBean.getId());
					filePathList.add(tempKbsModuleSourceBean.getFilePath());
				}
				doDeleteByResourceIdList(resourceIdList);
				deleteIndex(resourceIdList);
				deleteFile(filePathList);
			}
		}
	}
	/**
	 * 根据ID集合删除相关数据
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-17 下午4:00:15
	 * @version 1.0
	 * @exception 
	 * @param ResourceIdList
	 */
	public void doDeleteByResourceIdList(List<Integer> resourceIdList){
		//根据内容ID删除内容分页
		KbsModuleSourceBean kbsModuleSourceBean = new KbsModuleSourceBean();
		kbsModuleSourceBean.setIdList(resourceIdList);
		kbsModuleSourceBean.setCowSwingEvent(new CowSwingEvent(this,KbsEventType.KbsModuleSourceDeleteEvent));
		kbsModuleSourceListPanel.getKbsModuleSourceService().delete(kbsModuleSourceBean, KbsConstant.SQLMAP_ID_DELETE_BY_IDS_KBS_MODULE_SOURCE);
	}
	/**
	 * 删除文件
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-23 下午6:23:47
	 * @version 1.0
	 * @exception 
	 * @param filePathList
	 */
	private void deleteFile(List<String> filePathList){
		for(String filePath : filePathList){
			FileUtils.deleteFile(KbsConstant.SYSTEM_ROOT_PATH+KbsConstant.SYSTEM_SEPARATOR+filePath);
		}
	}
	/**
	 * 批量删除索引
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-24 下午2:22:35
	 * @version 1.0
	 * @exception 
	 * @param resourceIdList
	 */
	private void deleteIndex(List<Integer> resourceIdList){
		KnowledgePoint knowledgePoint = null;
		for(Integer id : resourceIdList){
			knowledgePoint = new KnowledgePoint();
			knowledgePoint.setId(id.toString());
			knowledgePoint.setIndexDirName(KbsConstant.DEFAULT_INDEX_MODULE_DIR);
			knowledgePoint.setModule(KbsConstant.SYSTEM_MODULE_SOURCE);
			try {
				index.deleteIndex(knowledgePoint);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
