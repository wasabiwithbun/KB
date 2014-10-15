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
import org.javacoo.cowswing.plugin.kbs.ui.dialog.AttributeManageDialog;
import org.springframework.stereotype.Component;

/**
 * 知识属性管理面板
 * 
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-9-29 上午10:33:33
 * @version 1.0
 */
@Component("showAttributeManageAction")
public class ShowAttributeManageAction extends AbstractAction{
	private static final long serialVersionUID = 1L;
	/**主窗体*/
	@Resource(name="cowSwingMainFrame")
	private CowSwingMainFrame crawlerMainFrame;
	/**知识属性管理面板*/
	@Resource(name="attributeManageDialog")
	private AttributeManageDialog attributeManageDialog;
	public ShowAttributeManageAction(){
		super(LanguageLoader.getString("Kbs.attribute_manage"),ImageLoader.getImageIcon("CrawlerResource.kbs_attribute"));
		putValue(SHORT_DESCRIPTION, LanguageLoader.getString("Kbs.attribute_manage"));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		attributeManageDialog.init(crawlerMainFrame, Constant.OPTION_TYPE_ADD, LanguageLoader.getString("Kbs.attribute_manage"));
		attributeManageDialog.setVisible(true);
	}

}
