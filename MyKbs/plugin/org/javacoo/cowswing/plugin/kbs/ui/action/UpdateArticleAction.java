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
import org.javacoo.cowswing.plugin.kbs.ui.dialog.AddArticleDialog;
import org.springframework.stereotype.Component;

/**
 * 修改资源Action
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-16 下午3:31:57
 * @version 1.0
 */
@Component("updateArticleAction")
public class UpdateArticleAction  extends AbstractAction{
	private static final long serialVersionUID = 1L;
	/**主窗体*/
	@Resource(name="cowSwingMainFrame")
	private CowSwingMainFrame cowSwingMainFrame;
	/**添加资源面板*/
	@Resource(name="addArticleDialog")
	private AddArticleDialog addArticleDialog;
	public UpdateArticleAction(){
		super(LanguageLoader.getString("Kbs.module_article_edit_btn"),ImageLoader.getImageIcon("CrawlerResource.kbs_article_edit"));
		putValue(SHORT_DESCRIPTION, LanguageLoader.getString("Kbs.module_article_edit_btn"));
		this.setEnabled(false);
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		addArticleDialog.init(cowSwingMainFrame, Constant.OPTION_TYPE_MODIFY, LanguageLoader.getString("Kbs.module_article_add_dialog_title"));
		addArticleDialog.setVisible(true);
		
	}

}
