/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.chat.ui.action;

import java.awt.event.ActionEvent;

import javax.annotation.Resource;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.SwingUtilities;

import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.core.event.CowSwingEvent;
import org.javacoo.cowswing.core.event.CowSwingListener;
import org.javacoo.cowswing.core.event.CowSwingObserver;
import org.javacoo.cowswing.core.event.type.CowSwingEventType;
import org.javacoo.cowswing.plugin.chat.ui.view.frame.KbsChatFrame;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 显示聊天面板
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-9-3 下午5:06:19
 * @version 1.0
 */
@Component("showKbsChatAction")
@Scope("prototype")
public class ShowKbsChatAction extends AbstractAction implements CowSwingListener{
	private static final long serialVersionUID = 1L;
	@Resource(name="kbsChatFrame")
    private KbsChatFrame kbsChatFrame;
	
	public ShowKbsChatAction(){
		super(LanguageLoader.getString("Kbs.chat_title"),ImageLoader.getImageIcon("CrawlerResource.kbs_chat"));
		putValue(SHORT_DESCRIPTION, LanguageLoader.getString("Kbs.chat_title"));
		this.setEnabled(false);
		CowSwingObserver.getInstance().addCrawlerListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		kbsChatFrame.setVisible(true);
		kbsChatFrame.init();
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
		putValue(Action.SMALL_ICON, ImageLoader.getImageIcon("CrawlerResource.kbs_chat_small"));
	}
	
}
