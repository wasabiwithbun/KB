/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.action;

import java.awt.event.ActionEvent;

import javax.annotation.Resource;
import javax.swing.AbstractAction;

import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.plugin.kbs.ui.view.panel.KbsSearchPanel;
import org.javacoo.cowswing.ui.view.panel.PageContainer;
import org.springframework.stereotype.Component;

/**
 * 显示搜索面板
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-9 下午7:09:05
 * @version 1.0
 */
@Component("showSearchAction")
public class ShowSearchAction extends AbstractAction{
	private static final long serialVersionUID = 1L;
	@Resource(name="kbsSearchPanel")
    private KbsSearchPanel kbsSearchPanel;
	@Resource(name="pageContainer")
    private PageContainer pageContainer;
	
	public ShowSearchAction(){
		super(LanguageLoader.getString("Kbs.title"),ImageLoader.getImageIcon("CrawlerResource.kbs_search"));
		putValue(SHORT_DESCRIPTION, LanguageLoader.getString("Kbs.title"));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		pageContainer.addPage(kbsSearchPanel, kbsSearchPanel.getPageId());
		kbsSearchPanel.init();
	}
}
