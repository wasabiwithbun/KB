/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.plugin.kbs.ui.view.panel.ITypeTree;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 折叠知识分类
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-13 下午2:41:16
 * @version 1.0
 */
@Component("collapseKbsTypeAction")
@Scope("prototype")
public class CollapseKbsTypeAction extends AbstractAction{
	private static final long serialVersionUID = 1L;
	/**知识分类树*/
	private ITypeTree iTypeTreePanel;
	public CollapseKbsTypeAction(){
		super("",ImageLoader.getImageIcon("CrawlerResource.kbs_collapse_all"));
		putValue(SHORT_DESCRIPTION, LanguageLoader.getString("Kbs.type_collapse_all_btn"));
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		this.iTypeTreePanel.expandTree(false);
	}
	/**
	 * @return the iTypeTreePanel
	 */
	public ITypeTree getiTypeTreePanel() {
		return iTypeTreePanel;
	}
	/**
	 * @param iTypeTreePanel the iTypeTreePanel to set
	 */
	public void setiTypeTreePanel(ITypeTree iTypeTreePanel) {
		this.iTypeTreePanel = iTypeTreePanel;
	}
	
	

}
