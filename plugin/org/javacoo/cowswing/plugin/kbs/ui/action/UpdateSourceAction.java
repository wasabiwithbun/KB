/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.springframework.stereotype.Component;

/**
 * 修改资源Action
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-16 下午3:31:57
 * @version 1.0
 */
@Component("updateSourceAction")
public class UpdateSourceAction  extends AbstractAction{
	private static final long serialVersionUID = 1L;
	public UpdateSourceAction(){
		super(LanguageLoader.getString("Kbs.module_source_edit_btn"),ImageLoader.getImageIcon("CrawlerResource.kbs_source_edit"));
		putValue(SHORT_DESCRIPTION, LanguageLoader.getString("Kbs.module_source_edit_btn"));
		this.setEnabled(false);
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
