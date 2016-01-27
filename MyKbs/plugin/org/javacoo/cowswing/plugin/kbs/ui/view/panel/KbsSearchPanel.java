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
 * 知识搜索面板
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-8-12 上午11:19:21
 * @version 1.0
 */
@org.springframework.stereotype.Component("kbsSearchPanel")
public class KbsSearchPanel extends AbstractListPage implements ListSelectionListener{
	private static final long serialVersionUID = 1L;
	/**本地资源列表*/
	@Resource(name="kbsLocalSearchPanel")
    private KbsLocalSearchPanel kbsLocalSearchPanel;
	/**网络资源列表*/
	@Resource(name="kbsNetSearchPanel")
    private KbsNetSearchPanel kbsNetSearchPanel;
	@Override
	protected JComponent getTopPane() {
		super.getTopPane();
		
		return buttonBar;
	}
	@Override
	protected JComponent getCenterPane() {
		JTabbedPane jTabbedPane = new JTabbedPane(JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT);
		jTabbedPane.addTab(LanguageLoader.getString("Kbs.search_loacl"),ImageLoader.getImageIcon("CrawlerResource.kbs_local_search"), kbsLocalSearchPanel);
		kbsLocalSearchPanel.init();
		jTabbedPane.addTab(LanguageLoader.getString("Kbs.search_net"),ImageLoader.getImageIcon("CrawlerResource.kbs_net_search"), kbsNetSearchPanel);
		kbsNetSearchPanel.init();
		return jTabbedPane;
	}
	
	@Override
	public String getPageName() {
		return LanguageLoader.getString("Kbs.search");
	}

	@Override
	public void disposePage() {
		super.disposePage();
	}
	public void getList(String typeCode){
		kbsLocalSearchPanel.getList(typeCode);
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
	 * @return the kbsLocalSearchPanel
	 */
	public KbsLocalSearchPanel getKbsLocalSearchPanel() {
		return kbsLocalSearchPanel;
	}
	/**
	 * @return the kbsNetSearchPanel
	 */
	public KbsNetSearchPanel getKbsNetSearchPanel() {
		return kbsNetSearchPanel;
	}
	
	@Override
	public boolean isDefaultPage() {
		return true;
	}
	
}
