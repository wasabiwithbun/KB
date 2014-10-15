/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.chat.ui.view.panel;

import java.awt.BorderLayout;

import javax.annotation.Resource;
import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.javacoo.cowswing.plugin.kbs.service.TransmitAndReceiver;
import org.springframework.stereotype.Component;

/**
 * 
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-1-9 上午10:04:08
 * @version 1.0
 */
@Component("rtpVideoPanel")
public class RtpVideoPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	protected Logger logger = Logger.getLogger(this.getClass());
	//对话面板
	@Resource(name = "kbsChatPanel")
	private KbsChatPanel kbsChatPanel;
	//播放器面板
	private PlayPanel playPanel;
	//是否停止 默认停止
	private boolean isStop = true;
	//视频,音频发送接收对象
	private TransmitAndReceiver ta;

	public void startVideo(String senderIp) {
		if(isStop){
			try {
				isStop = false;
		    	playPanel = new PlayPanel();
				add(playPanel, BorderLayout.CENTER);
				ta = new TransmitAndReceiver(playPanel,senderIp);
				String msg = ta.start();
				if(StringUtils.isNotBlank(msg)){
					kbsChatPanel.appendChatMsg(msg+".\n");
					ta.close();
				}
				validate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

   /**
    * 关闭视频
    * <p>方法说明:</>
    * <li></li>
    * @author DuanYong
    * @since 2014-1-10 上午10:31:04
    * @version 1.0
    * @exception
    */
   public void cancel(){
	   logger.info("关闭视频");
	   isStop = true;
	   ta.close();
	   playPanel.close();
	   this.remove(playPanel);
	   playPanel = null;
   }

}
