/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.core.ui.action;

import java.awt.event.ActionEvent;

import javax.annotation.Resource;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.SwingUtilities;

import org.javacoo.cowswing.base.constant.Constant;
import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.core.event.CowSwingEvent;
import org.javacoo.cowswing.core.event.CowSwingListener;
import org.javacoo.cowswing.core.event.CowSwingObserver;
import org.javacoo.cowswing.core.event.type.CowSwingEventType;
import org.javacoo.cowswing.main.CowSwingMainFrame;
import org.javacoo.cowswing.plugin.core.ui.dialog.NetSettingDialog;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 显示网络设置面板
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-9-1 下午4:58:26
 * @version 1.0
 */
@Component("showNetSettingAction")
@Scope("prototype")
public class ShowNetSettingAction extends AbstractAction implements CowSwingListener{
	private static final long serialVersionUID = 1L;
	/**主窗体*/
	@Resource(name="cowSwingMainFrame")
	private CowSwingMainFrame crawlerMainFrame;
	/**网络设置面板*/
	@Resource(name="netSettingDialog")
	private NetSettingDialog netSettingDialog;
	public ShowNetSettingAction(){
		super(LanguageLoader.getString("Kbs.net_setting_title"),ImageLoader.getImageIcon("CrawlerResource.kbs_net_setting"));
		putValue(SHORT_DESCRIPTION, LanguageLoader.getString("Kbs.net_setting_title"));
		this.setEnabled(false);
		CowSwingObserver.getInstance().addCrawlerListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		netSettingDialog.init(crawlerMainFrame, Constant.OPTION_TYPE_ADD, LanguageLoader.getString("Kbs.net_setting_title"));
		netSettingDialog.setVisible(true);
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.core.event.CowSwingListener#update(org.javacoo.cowswing.core.event.CowSwingEvent)
	 */
	@Override
	public void update(CowSwingEvent event) {
		if(CowSwingEventType.NetUserOnlineEvent.equals(event.getEventType())){
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					setEnabled(true);
				}
			});
		}
	}
	/**
	 * 设置小图标
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-26 上午10:50:16
	 * @version 1.0
	 * @exception
	 */
	public void setSmallIcon(){
		putValue(Action.SMALL_ICON, ImageLoader.getImageIcon("CrawlerResource.navigatorSetting"));
	}
	
}
