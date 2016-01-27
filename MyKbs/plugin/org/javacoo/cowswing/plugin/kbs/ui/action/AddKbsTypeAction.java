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
import org.javacoo.cowswing.plugin.kbs.ui.view.panel.TypeTreePanel;
import org.springframework.stereotype.Component;

/**
 * 添加知识分类
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-13 下午2:41:16
 * @version 1.0
 */
@Component("addKbsTypeAction")
public class AddKbsTypeAction extends AbstractAction{
	private static final long serialVersionUID = 1L;
	/**主窗体*/
	@Resource(name="cowSwingMainFrame")
	private CowSwingMainFrame cowSwingMainFrame;
	/**知识分类*/
	@Resource(name="typeTreePanel")
	private TypeTreePanel typeTreePanel;
	public AddKbsTypeAction(){
		super("",ImageLoader.getImageIcon("CrawlerResource.kbs_folder_add"));
		putValue(SHORT_DESCRIPTION, LanguageLoader.getString("Kbs.type_add_btn"));
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		typeTreePanel.getKbsTypeSettingDialog().init(cowSwingMainFrame, Constant.OPTION_TYPE_ADD, LanguageLoader.getString("Kbs.type_setting"));
		typeTreePanel.getKbsTypeSettingDialog().setVisible(true);
		
	}

}
