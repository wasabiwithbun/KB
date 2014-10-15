package org.javacoo.cowswing.plugin.core.ui.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.annotation.Resource;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.SwingUtilities;

import org.javacoo.cowswing.base.constant.Config;
import org.javacoo.cowswing.base.constant.Constant;
import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.core.event.CowSwingEvent;
import org.javacoo.cowswing.core.event.CowSwingListener;
import org.javacoo.cowswing.core.event.CowSwingObserver;
import org.javacoo.cowswing.core.event.type.CowSwingEventType;
import org.javacoo.cowswing.main.CowSwingMainFrame;
import org.javacoo.cowswing.plugin.core.constant.CoreConstant;
import org.javacoo.cowswing.plugin.core.net.MsgBean;
import org.javacoo.cowswing.plugin.core.net.NetManager;
import org.javacoo.cowswing.plugin.core.ui.dialog.VersionInfoDialog;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;



/**
 * 检查更新
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-1-1 下午6:37:27
 * @version 1.0
 */
@Component("updateAction")
@Scope("prototype")
public class UpdateAction extends AbstractAction implements CowSwingListener{
	private static final long serialVersionUID = 1L;
	private double version = Double.parseDouble(Config.COWSWING_CONFIG_MAP.get(Config.COWSWING_CONFIG_KEY_VERSION).get(Config.COWSWING_CONFIG_KEY_VERSION_VERSION));
	/** 网络管理服务 */
	@Resource(name = "netManager")
	private NetManager netManager;
	/**主窗体*/
	@Resource(name="cowSwingMainFrame")
	private CowSwingMainFrame crawlerMainFrame;
	/**版本更新面板*/
	@Resource(name="versionInfoDialog")
	private VersionInfoDialog versionInfoDialog;
	public UpdateAction(){
		super(LanguageLoader.getString("CrawlerMainFrame.Update"),ImageLoader.getImageIcon("CrawlerResource.update_32"));
		putValue(SHORT_DESCRIPTION, LanguageLoader.getString("CrawlerMainFrame.Update"));
		putValue(ACTION_COMMAND_KEY ,LanguageLoader.getString("CrawlerMainFrame.Update"));
		putValue(MNEMONIC_KEY, KeyEvent.VK_U);
		this.setEnabled(false);
		CowSwingObserver.getInstance().addCrawlerListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		MsgBean msgBean = new MsgBean(
				CoreConstant.NET_ACTION_TYPE_REFRESH, netManager
				.getNetClientBean().getIp(), String.valueOf(netManager
				.getNetClientBean().getPort()));
		msgBean.setIp(netManager.getNetClientBean().getIp());
		msgBean.setPort(netManager.getNetClientBean().getPort());
		msgBean.setVersion(version);
		netManager.sendGroupMsgToAll(msgBean);
		
		versionInfoDialog.init(crawlerMainFrame, Constant.OPTION_TYPE_ADD, LanguageLoader.getString("Core.version_title"));
		versionInfoDialog.setVisible(true);
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
		putValue(Action.SMALL_ICON, ImageLoader.getImageIcon("CrawlerResource.update_16"));
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
}
