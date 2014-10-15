/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.action;

import java.awt.event.ActionEvent;

import javax.annotation.Resource;
import javax.swing.AbstractAction;

import org.javacoo.cowswing.base.constant.Constant;
import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.main.CowSwingMainFrame;
import org.javacoo.cowswing.plugin.kbs.ui.dialog.IndexManageDialog;
import org.springframework.stereotype.Component;

/**
 * 索引管理面板
 * 
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-9-27 下午12:09:25
 * @version 1.0
 */
@Component("showIndexManageAction")
public class ShowIndexManageAction extends AbstractAction{
	private static final long serialVersionUID = 1L;
	/**主窗体*/
	@Resource(name="cowSwingMainFrame")
	private CowSwingMainFrame crawlerMainFrame;
	/**索引管理面板*/
	@Resource(name="indexManageDialog")
	private IndexManageDialog indexManageDialog;
	public ShowIndexManageAction(){
		super(LanguageLoader.getString("Kbs.index_manage"),ImageLoader.getImageIcon("CrawlerResource.kbs_index_manage"));
		putValue(SHORT_DESCRIPTION, LanguageLoader.getString("Kbs.index_manage"));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		indexManageDialog.init(crawlerMainFrame, Constant.OPTION_TYPE_ADD, LanguageLoader.getString("Kbs.index_manage"));
		indexManageDialog.setVisible(true);
	}

}
