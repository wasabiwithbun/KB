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
import org.javacoo.cowswing.plugin.core.constant.CoreConstant;
import org.javacoo.cowswing.plugin.core.net.MsgBean;
import org.javacoo.cowswing.plugin.core.net.NetManager;
import org.javacoo.cowswing.plugin.kbs.service.MultiTransmitAndReceiver;
import org.javacoo.cowswing.ui.widget.MainFrame;
import org.springframework.stereotype.Component;

/**
 * 聊天室窗口
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-9-3 下午6:06:26
 * @version 1.0
 */
@Component("kbsMultiVideoFrame")
public class KbsMultiVideoFrame extends MainFrame  implements CowSwingListener{
	private static final long serialVersionUID = 1L;
	/**网络管理服务*/
	@Resource(name="netManager")
    private NetManager netManager;
	@Resource(name = "kbsChatPanel")
	private KbsChatPanel kbsChatPanel;
	/**中部面板*/
	private JPanel centerPanel;
	/**播放器集合*/
	private Vector players = null; 
	//视频,音频发送接收对象
	private MultiTransmitAndReceiver ta;
	private boolean hasInit = false;
	//是否停止 默认停止
	private boolean isStop = true;
	public KbsMultiVideoFrame(){
		super("多人视频");
	}
	public void doInit(){
		if(!hasInit){
			ininFrame();
			players = new Vector();   
			BorderLayout layout = new BorderLayout();
			this.setLayout(layout);
			this.add(getCenterPanel(), BorderLayout.CENTER);
			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					if(null != ta){
						ta.close();
						//ta = null;
						netManager.sendCommonData(new MsgBean(CoreConstant.NET_ACTION_TYPE_MULTI_VIDEO_OUT,netManager.getNetClientBean().getUserName()));
					}
					isStop = true;
				}
			});   
			CowSwingObserver.getInstance().addCrawlerListener(this);
			hasInit = true;
		}
	}
	public void startVideo() {
		if(isStop){
			try {
				isStop = false;
				ta = new MultiTransmitAndReceiver(centerPanel,netManager.getNetSettingBean().getIp(),players);
				String msg = ta.start();
				if(StringUtils.isBlank(msg)){
					netManager.sendCommonData(new MsgBean(CoreConstant.NET_ACTION_TYPE_MULTI_VIDEO_IN,netManager.getNetClientBean().getUserName()));
					validate();
				}else{
					kbsChatPanel.appendChatMsg(msg+".\n");
					ta.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private JComponent getCenterPanel() {
		centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		return new JScrollPane(centerPanel);
	
	}

	public void doUpdate(CowSwingEvent event) {
		
	}
	

}
