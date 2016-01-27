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
import org.javacoo.cowswing.core.cache.UserCacheManager;
import org.javacoo.cowswing.main.CowSwingMainFrame;
import org.javacoo.cowswing.plugin.kbs.ui.view.panel.LocalTreeSearchPanel;
import org.springframework.stereotype.Component;

/**
 * 添加本地文件监听ACTION
 * 
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-7-21 上午11:36:56
 * @version 1.0
 */
@Component("addLocalFileListenerAction")
public class AddLocalFileListenerAction extends AbstractAction{
	private static final long serialVersionUID = 1L;
	/**主窗体*/
	@Resource(name="cowSwingMainFrame")
	private CowSwingMainFrame cowSwingMainFrame;
	/** 用户缓存管理类 */
	protected static UserCacheManager userCacheManager = UserCacheManager.getInstance();
	/**本地文件树*/
	@Resource(name="localTreeSearchPanel")
	private LocalTreeSearchPanel localTreeSearchPanel;
	public AddLocalFileListenerAction(){
		super("",ImageLoader.getImageIcon("CrawlerResource.systemDataBaseAdd"));
		putValue(SHORT_DESCRIPTION, LanguageLoader.getString("Kbs.local_add_local_listener"));
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		localTreeSearchPanel.getAddLocalDirListenerDialog().init(cowSwingMainFrame, Constant.OPTION_TYPE_ADD, LanguageLoader.getString("Kbs.local_add_local_listener"));
		localTreeSearchPanel.getAddLocalDirListenerDialog().setVisible(true);
	}

}
