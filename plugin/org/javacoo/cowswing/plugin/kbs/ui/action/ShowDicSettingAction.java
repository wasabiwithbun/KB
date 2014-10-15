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
import org.javacoo.cowswing.plugin.kbs.ui.dialog.DicSettingDialog;
import org.springframework.stereotype.Component;

/**
 * 显示词典设置面板
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-9-26 下午4:58:26
 * @version 1.0
 */
@Component("showDicSettingAction")
public class ShowDicSettingAction extends AbstractAction{
	private static final long serialVersionUID = 1L;
	/**主窗体*/
	@Resource(name="cowSwingMainFrame")
	private CowSwingMainFrame crawlerMainFrame;
	/**词典设置面板*/
	@Resource(name="dicSettingDialog")
	private DicSettingDialog dicSettingDialog;
	public ShowDicSettingAction(){
		super(LanguageLoader.getString("Kbs.dic_manage"),ImageLoader.getImageIcon("CrawlerResource.kbs_dic_manage"));
		putValue(SHORT_DESCRIPTION, LanguageLoader.getString("Kbs.dic_manage"));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		dicSettingDialog.init(crawlerMainFrame, Constant.OPTION_TYPE_ADD, LanguageLoader.getString("Kbs.dic_manage"));
		dicSettingDialog.setVisible(true);
	}

}
