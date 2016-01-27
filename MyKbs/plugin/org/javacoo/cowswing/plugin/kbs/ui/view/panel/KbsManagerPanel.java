/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.view.panel;

import java.awt.Component;

import javax.annotation.Resource;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.core.event.CowSwingEvent;
import org.javacoo.cowswing.ui.view.panel.AbstractListPage;

/**
 * 知识管理面板
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-15 上午11:19:21
 * @version 1.0
 */
@org.springframework.stereotype.Component("kbsManagerPanel")
public class KbsManagerPanel extends AbstractListPage implements ListSelectionListener{
	private static final long serialVersionUID = 1L;
	/**资源列表*/
	@Resource(name="kbsModuleSourceListPanel")
    private KbsModuleSourceListPanel kbsModuleSourceListPanel;
	/**文章列表*/
	@Resource(name="kbsModuleArticleListPanel")
    private KbsModuleArticleListPanel kbsModuleArticleListPanel;
	@Override
	protected JComponent getTopPane() {
		super.getTopPane();
		
		return buttonBar;
	}
	@Override
	protected JComponent getCenterPane() {
		JTabbedPane jTabbedPane = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT);
		jTabbedPane.addTab(LanguageLoader.getString("Kbs.module_article_tab_title"),ImageLoader.getImageIcon("CrawlerResource.kbs_article_view"), kbsModuleArticleListPanel);
		kbsModuleArticleListPanel.init();
		jTabbedPane.addTab(LanguageLoader.getString("Kbs.module_source_tab_title"),ImageLoader.getImageIcon("CrawlerResource.kbs_source_view"), kbsModuleSourceListPanel);
		kbsModuleSourceListPanel.init();
		return jTabbedPane;
	}
	
	@Override
	public String getPageName() {
		return LanguageLoader.getString("Kbs.manager_title");
	}

	@Override
	public void disposePage() {
		super.disposePage();
	}
	
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.core.event.CowSwingListener#update(org.javacoo.cowswing.core.event.CowSwingEvent)
	 */
	@Override
	public void update(CowSwingEvent event) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}
	protected Component getBottomPane() {
		return null;
	}
	/**
	 * @return the kbsModuleSourceListPanel
	 */
	public KbsModuleSourceListPanel getKbsModuleSourceListPanel() {
		return kbsModuleSourceListPanel;
	}
	/**
	 * @return the kbsModuleArticleListPanel
	 */
	public KbsModuleArticleListPanel getKbsModuleArticleListPanel() {
		return kbsModuleArticleListPanel;
	}
	
	
}
