/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.action;

import java.awt.event.ActionEvent;
import java.util.Map;

import javax.annotation.Resource;
import javax.swing.AbstractAction;
import javax.swing.JTable;

import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.base.utils.JsonUtils;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleArticleBean;
import org.javacoo.cowswing.plugin.kbs.ui.model.KbsModuleArticleTabelModel;
import org.javacoo.cowswing.plugin.kbs.ui.view.panel.KbsModuleArticleListPanel;
import org.javacoo.cowswing.ui.view.dialog.ViewDialog;
import org.springframework.stereotype.Component;

/**
 * 查看文章Action
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-15 下午5:04:49
 * @version 1.0
 */
@Component("viewArticleAction")
public class ViewArticleAction  extends AbstractAction{
	private static final long serialVersionUID = 1L;
	/**资源集合面板*/
	@Resource(name="kbsModuleArticleListPanel")
	private KbsModuleArticleListPanel kbsModuleArticleListPanel;
	private JTable moduleArticleTable;
	/**资源model*/
	private KbsModuleArticleTabelModel kbsModuleArticleTabelModel;
	/**
	 * 详细信息页面
	 */
	@Resource(name="viewDialog")
	private ViewDialog viewDialog;
	public ViewArticleAction(){
		super(LanguageLoader.getString("Kbs.module_article_view_btn"),ImageLoader.getImageIcon("CrawlerResource.kbs_article_view"));
		putValue(SHORT_DESCRIPTION, LanguageLoader.getString("Kbs.module_source_view_btn"));
		this.setEnabled(false);
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		moduleArticleTable = kbsModuleArticleListPanel.getKbsModuleArticleTable();
		if(moduleArticleTable.getSelectedRow() != -1){
			kbsModuleArticleTabelModel = (KbsModuleArticleTabelModel)moduleArticleTable.getModel();
			KbsModuleArticleBean tempKbsModuleArticleBean = kbsModuleArticleTabelModel.getRowObject(moduleArticleTable.getSelectedRow());
			Map<String,String> valueMap = (Map<String, String>) JsonUtils.formatStringToObject(tempKbsModuleArticleBean.getContent(), Map.class);
			if(!valueMap.isEmpty()){
				viewDialog.showContent(valueMap.get(KbsConstant.KBS_TYPE_CHILD_EDITOR_CODE));
			}else{
				viewDialog.showContent("");
			}
			
			viewDialog.setVisible(true);
		}
		
	}

}
