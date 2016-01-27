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
import org.javacoo.cowswing.core.event.CowSwingEvent;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.domain.KnowledgePoint;
import org.javacoo.cowswing.plugin.kbs.event.KbsEventType;
import org.javacoo.cowswing.plugin.kbs.lucene.index.Index;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleArticleBean;
import org.javacoo.cowswing.plugin.kbs.ui.model.KbsModuleArticleTabelModel;
import org.javacoo.cowswing.plugin.kbs.ui.view.panel.KbsModuleArticleListPanel;
import org.springframework.stereotype.Component;

/**
 * 删除文章Action
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-15 下午5:04:49
 * @version 1.0
 */
@Component("deleteArticleAction")
public class DeleteArticleAction  extends AbstractAction{
	private static final long serialVersionUID = 1L;
	/**文章集合面板*/
	@Resource(name="kbsModuleArticleListPanel")
	private KbsModuleArticleListPanel kbsModuleArticleListPanel;
	private JTable moduleArticleTable;
	/**文章model*/
	private KbsModuleArticleTabelModel kbsModuleArticleTabelModel;
	/**索引管理*/
	@Resource(name="indexManager")
    private Index index;
	public DeleteArticleAction(){
		super(LanguageLoader.getString("Kbs.module_article_delete_btn"),ImageLoader.getImageIcon("CrawlerResource.kbs_article_delete"));
		putValue(SHORT_DESCRIPTION, LanguageLoader.getString("Kbs.module_article_delete_btn"));
		this.setEnabled(false);
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		moduleArticleTable = kbsModuleArticleListPanel.getKbsModuleArticleTable();
		if(moduleArticleTable.getSelectedRow() != -1){
			int result = JOptionPane.showConfirmDialog(null, LanguageLoader.getString("Common.deleteConfirm"),LanguageLoader.getString("Common.confirm"), JOptionPane.YES_NO_OPTION); 
			if(result == 0){
				kbsModuleArticleTabelModel = (KbsModuleArticleTabelModel)moduleArticleTable.getModel();
				List<Integer> idList = new ArrayList<Integer>();
				KbsModuleArticleBean tempKbsModuleArticleBean = null;
				for(Integer selectRow : moduleArticleTable.getSelectedRows()){
					tempKbsModuleArticleBean = kbsModuleArticleTabelModel.getRowObject(selectRow);
					idList.add(tempKbsModuleArticleBean.getId());
				}
				doDeleteByArticleIdList(idList);
				deleteIndex(idList);
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
	public void doDeleteByArticleIdList(List<Integer> idList){
		//根据内容ID删除内容分页
		KbsModuleArticleBean kbsModuleArticleBean = new KbsModuleArticleBean();
		kbsModuleArticleBean.setIdList(idList);
		kbsModuleArticleBean.setCowSwingEvent(new CowSwingEvent(this,KbsEventType.KbsModuleArticleDeleteEvent));
		kbsModuleArticleListPanel.getKbsModuleArticleService().delete(kbsModuleArticleBean, KbsConstant.SQLMAP_ID_DELETE_BY_IDS_KBS_MODULE_ARTICLE);
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
	private void deleteIndex(List<Integer> idList){
		KnowledgePoint knowledgePoint = null;
		for(Integer id : idList){
			knowledgePoint = new KnowledgePoint();
			knowledgePoint.setId(id.toString());
			knowledgePoint.setIndexDirName(KbsConstant.DEFAULT_INDEX_MODULE_DIR);
			knowledgePoint.setModule(KbsConstant.SYSTEM_MODULE_ARTICLE);
			try {
				index.deleteIndex(knowledgePoint);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
