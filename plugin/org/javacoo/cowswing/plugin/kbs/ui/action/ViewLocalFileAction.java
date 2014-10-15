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
import org.javacoo.cowswing.plugin.kbs.service.beans.LocalFileBean;
import org.javacoo.cowswing.plugin.kbs.ui.model.LocalFileTabelModel;
import org.javacoo.cowswing.plugin.kbs.ui.view.panel.KbsLocalFileListPanel;
import org.springframework.stereotype.Component;

/**
 * 查看本地文件ACTION
 * 
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-7-18 上午10:01:50
 * @version 1.0
 */
@Component("viewLocalFileAction")
public class ViewLocalFileAction  extends AbstractAction{
	private static final long serialVersionUID = 1L;

	/**本地文件列表面板*/
	@Resource(name="kbsLocalFileListPanel")
	private KbsLocalFileListPanel kbsLocalFileListPanel;
	private JTable searchTable;
	/**model*/
	private LocalFileTabelModel localFileTabelModel;
	public ViewLocalFileAction(){
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
			searchTable = kbsLocalFileListPanel.getLocalFileDataTable();
			if(searchTable.getSelectedRow() != -1){
				localFileTabelModel = (LocalFileTabelModel)searchTable.getModel();
				LocalFileBean localFileBean = localFileTabelModel.getRowObject(searchTable.getSelectedRow());
				String comd = "cmd /E:ON /c start "+localFileBean.getPath();
				Runtime.getRuntime().exec(comd);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
		
	}

}
