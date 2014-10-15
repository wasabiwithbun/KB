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
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.domain.KnowledgePoint;
import org.javacoo.cowswing.plugin.kbs.lucene.index.Index;
import org.javacoo.cowswing.plugin.kbs.service.beans.LocalFileBean;
import org.javacoo.cowswing.plugin.kbs.ui.model.LocalFileTabelModel;
import org.javacoo.cowswing.plugin.kbs.ui.view.panel.KbsLocalFileListPanel;
import org.springframework.stereotype.Component;

/**
 * 删除本地文件ACTION
 * 
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-7-21 下午2:47:16
 * @version 1.0
 */
@Component("deleteLocalFileAction")
public class DeleteLocalFileAction  extends AbstractAction{
	private static final long serialVersionUID = 1L;
	/**本地文件集合面板*/
	@Resource(name="kbsLocalFileListPanel")
	private KbsLocalFileListPanel kbsLocalFileListPanel;
	private JTable locaoFileTable;
	/**本地文件model*/
	private LocalFileTabelModel localFileTabelModel;
	/**索引管理*/
	@Resource(name="indexManager")
    private Index index;
	public DeleteLocalFileAction(){
		super(LanguageLoader.getString("Kbs.local_file_delete_btn"),ImageLoader.getImageIcon("CrawlerResource.kbs_source_delete"));
		putValue(SHORT_DESCRIPTION, LanguageLoader.getString("Kbs.local_file_delete_btn"));
		this.setEnabled(false);
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		locaoFileTable = kbsLocalFileListPanel.getLocalFileDataTable();
		if(locaoFileTable.getSelectedRow() != -1){
			int result = JOptionPane.showConfirmDialog(null, LanguageLoader.getString("Common.deleteConfirm"),LanguageLoader.getString("Common.confirm"), JOptionPane.YES_NO_OPTION); 
			if(result == 0){
				localFileTabelModel = (LocalFileTabelModel)locaoFileTable.getModel();
				List<String> resourceIdList = new ArrayList<String>();
				List<String> filePathList = new ArrayList<String>();
				LocalFileBean tempLocalFileBean = null;
				for(Integer selectRow : locaoFileTable.getSelectedRows()){
					tempLocalFileBean = localFileTabelModel.getRowObject(selectRow);
					resourceIdList.add(tempLocalFileBean.getId());
					filePathList.add(tempLocalFileBean.getPath());
				}
//				deleteIndex(resourceIdList);
				deleteFile(filePathList);
				kbsLocalFileListPanel.refresh();
			}
		}
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
			FileUtils.deleteFile(filePath);
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
