/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.action;

import java.awt.event.ActionEvent;

import javax.annotation.Resource;
import javax.swing.AbstractAction;
import javax.swing.Action;

import org.javacoo.cowswing.base.constant.Constant;
import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.main.CowSwingMainFrame;
import org.javacoo.cowswing.plugin.kbs.ui.dialog.AddSourceDialog;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 添加资源Action
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-16 下午3:31:57
 * @version 1.0
 */
@Component("addSourceAction")
@Scope("prototype")
public class AddSourceAction  extends AbstractAction{
	private static final long serialVersionUID = 1L;
	/**主窗体*/
	@Resource(name="cowSwingMainFrame")
	private CowSwingMainFrame cowSwingMainFrame;
	/**添加资源面板*/
	@Resource(name="addSourceDialog")
	private AddSourceDialog addSourceDialog;
	public AddSourceAction(){
		super("",ImageLoader.getImageIcon("CrawlerResource.kbs_source_add"));
		putValue(SHORT_DESCRIPTION, LanguageLoader.getString("Kbs.module_source_add_btn"));
		this.setEnabled(false);
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		addSourceDialog.init(cowSwingMainFrame, Constant.OPTION_TYPE_ADD, LanguageLoader.getString("Kbs.module_source_add_dialog_title"));
		addSourceDialog.setVisible(true);
	}
	public void initTitle(){
		putValue(Action.NAME, LanguageLoader.getString("Kbs.module_source_add_btn"));
	}
}
