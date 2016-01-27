/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.action;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.annotation.Resource;
import javax.swing.AbstractAction;
import javax.swing.JTable;

import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleSourceBean;
import org.javacoo.cowswing.plugin.kbs.ui.model.KbsModuleSourceTabelModel;
import org.javacoo.cowswing.plugin.kbs.ui.view.panel.KbsModuleSourceListPanel;
import org.springframework.stereotype.Component;

/**
 * 查看资源Action
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-15 下午5:04:49
 * @version 1.0
 */
@Component("viewSourceAction")
public class ViewSourceAction  extends AbstractAction{
	private static final long serialVersionUID = 1L;

	/**资源集合面板*/
	@Resource(name="kbsModuleSourceListPanel")
	private KbsModuleSourceListPanel kbsModuleSourceListPanel;
	private JTable moduleSourceTable;
	/**资源model*/
	private KbsModuleSourceTabelModel kbsModuleSourceTabelModel;
	public ViewSourceAction(){
		super(LanguageLoader.getString("Kbs.module_source_view_btn"),ImageLoader.getImageIcon("CrawlerResource.kbs_source_view"));
		putValue(SHORT_DESCRIPTION, LanguageLoader.getString("Kbs.module_article_view_btn"));
		this.setEnabled(false);
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			moduleSourceTable = kbsModuleSourceListPanel.getKbsModuleSourceTable();
			if(moduleSourceTable.getSelectedRow() != -1){
				kbsModuleSourceTabelModel = (KbsModuleSourceTabelModel)moduleSourceTable.getModel();
				KbsModuleSourceBean tempKbsModuleSourceBean = kbsModuleSourceTabelModel.getRowObject(moduleSourceTable.getSelectedRow());
				Runtime.getRuntime().exec("cmd /E:ON /c start "+KbsConstant.SYSTEM_ROOT_PATH+KbsConstant.SYSTEM_SEPARATOR+tempKbsModuleSourceBean.getFilePath());
			}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
		
	}

}
