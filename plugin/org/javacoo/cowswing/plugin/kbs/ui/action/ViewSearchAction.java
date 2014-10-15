/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.action;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.swing.AbstractAction;
import javax.swing.JTable;

import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.base.utils.JsonUtils;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.domain.KnowledgePoint;
import org.javacoo.cowswing.plugin.kbs.ui.model.KnowledgePointTabelModel;
import org.javacoo.cowswing.plugin.kbs.ui.view.panel.KbsLocalSearchPanel;
import org.javacoo.cowswing.ui.view.dialog.ViewDialog;
import org.springframework.stereotype.Component;

/**
 * 查看Action
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-15 下午5:04:49
 * @version 1.0
 */
@Component("viewSearchAction")
public class ViewSearchAction  extends AbstractAction{
	private static final long serialVersionUID = 1L;

	/**集合面板*/
	@Resource(name="kbsLocalSearchPanel")
	private KbsLocalSearchPanel kbsLocalSearchPanel;
	private JTable searchTable;
	/**model*/
	private KnowledgePointTabelModel knowledgePointTabelModel;
	/**
	 * 详细信息页面
	 */
	@Resource(name="viewDialog")
	private ViewDialog viewDialog;
	public ViewSearchAction(){
		super(LanguageLoader.getString("Kbs.search_loacl_view"),ImageLoader.getImageIcon("CrawlerResource.kbs_source_view"));
		putValue(SHORT_DESCRIPTION, LanguageLoader.getString("Kbs.module_article_view_btn"));
		this.setEnabled(false);
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			searchTable = kbsLocalSearchPanel.getKnowledgePointTable();
			if(searchTable.getSelectedRow() != -1){
				knowledgePointTabelModel = (KnowledgePointTabelModel)searchTable.getModel();
				KnowledgePoint knowledgePoint = knowledgePointTabelModel.getRowObject(searchTable.getSelectedRow());
				if(KbsConstant.SYSTEM_MODULE_SOURCE.equals(knowledgePoint.getModule())){
					String cmd = "cmd /E:ON /c start ";
					if(KbsConstant.ORIGIN_LOCAL.equals(knowledgePoint.getOrigin())){
						cmd += knowledgePoint.getFilePath();
					}else{
						cmd += KbsConstant.SYSTEM_ROOT_PATH+KbsConstant.SYSTEM_SEPARATOR+knowledgePoint.getFilePath();
					}
					Runtime.getRuntime().exec(cmd);
				}else{
					Map<String,String> valueMap = (Map<String, String>) JsonUtils.formatStringToObject(knowledgePoint.getContent(), Map.class);
					if(!valueMap.isEmpty()){
						viewDialog.showContent(valueMap.get(KbsConstant.KBS_TYPE_CHILD_EDITOR_CODE));
					}else{
						viewDialog.showContent("");
					}
					viewDialog.setVisible(true);
				}
			}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
		
	}

}
