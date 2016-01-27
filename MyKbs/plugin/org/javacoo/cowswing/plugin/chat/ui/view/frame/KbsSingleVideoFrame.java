/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.chat.ui.view.frame;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.annotation.Resource;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.lang.StringUtils;
import org.javacoo.cowswing.core.event.CowSwingEvent;
import org.javacoo.cowswing.core.event.CowSwingListener;
import org.javacoo.cowswing.core.event.CowSwingObserver;
import org.javacoo.cowswing.plugin.chat.ui.view.panel.KbsChatPanel;
import org.javacoo.cowswing.plugin.chat.ui.view.panel.RtpVideoPanel;
import org.javacoo.cowswing.plugin.core.net.MsgBean;
import org.javacoo.cowswing.plugin.core.net.NetManager;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.service.MultiTransmitAndReceiver;
import org.javacoo.cowswing.ui.widget.MainFrame;
import org.springframework.stereotype.Component;

import sun.util.logging.resources.logging;

/**
 * 单人对话窗口
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-9-3 下午6:06:26
 * @version 1.0
 */
@Component("kbsSingleVideoFrame")
public class KbsSingleVideoFrame extends MainFrame  implements CowSwingListener{
	private static final long serialVersionUID = 1L;

	@Resource(name = "kbsChatPanel")
	private KbsChatPanel kbsChatPanel;
	/** 视频面板 */
	@Resource(name = "rtpVideoPanel")
	private RtpVideoPanel videoPanel;
	private boolean hasInit = false;
	public KbsSingleVideoFrame(){
		super("单人视频",400,350);
	}
	public void doInit(){
		if(!hasInit){
			ininFrame();  
			setResizable(false);
			BorderLayout layout = new BorderLayout();
			this.setLayout(layout);
			this.add(getCenterPanel(), BorderLayout.CENTER);
			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					kbsChatPanel.cancelVideo();
				}
			});   
			CowSwingObserver.getInstance().addCrawlerListener(this);
			hasInit = true;
		}
	}
	
	
	/**
	 * @return the videoPanel
	 */
	public RtpVideoPanel getVideoPanel() {
		return videoPanel;
	}
	/**
	 * @param videoPanel the videoPanel to set
	 */
	public void setVideoPanel(RtpVideoPanel videoPanel) {
		this.videoPanel = videoPanel;
	}
	private JComponent getCenterPanel() {
		return new JScrollPane(videoPanel);
	
	}

	public void doUpdate(CowSwingEvent event) {
		
	}
	

}
