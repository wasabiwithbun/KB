/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.chat.ui.view.frame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;

import javax.annotation.Resource;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.core.event.CowSwingEvent;
import org.javacoo.cowswing.core.event.CowSwingListener;
import org.javacoo.cowswing.core.event.CowSwingObserver;
import org.javacoo.cowswing.core.event.type.CowSwingEventType;
import org.javacoo.cowswing.plugin.chat.ui.view.panel.KbsChatPanel;
import org.javacoo.cowswing.plugin.chat.ui.view.panel.NetUserTreePanel;
import org.javacoo.cowswing.ui.widget.MainFrame;

/**
 * 聊天室窗口
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-9-3 下午6:06:26
 * @version 1.0
 */
@org.springframework.stereotype.Component("kbsChatFrame")
public class KbsChatFrame extends MainFrame  implements CowSwingListener{
	private static final long serialVersionUID = 1L;
	/**分隔面板*/
	private JSplitPane splitPanel;
	/**中部面板*/
	private JPanel centerPanel;
	/**在线用户面板*/
	@Resource(name="netUserTreePanel")
	private NetUserTreePanel netUserTreePanel;
	/**知识聊天室面板*/
	@Resource(name="kbsChatPanel")
    private KbsChatPanel kbsChatPanel;
	private boolean hasInit = false;
	public KbsChatFrame(){
		super("选择左边在线用户进行私聊,选择根节点为群聊");
	}
	public void doInit(){
		if(!hasInit){
			ininFrame();
			BorderLayout layout = new BorderLayout();
			this.setLayout(layout);
			this.add(getCenterPanel(), BorderLayout.CENTER);
			CowSwingObserver.getInstance().addCrawlerListener(this);
			hasInit = true;
		}
	}
	
	private JComponent getCenterPanel() {
		centerPanel = new JPanel(new BorderLayout());
		centerPanel.add(getSplitPanel());
		add(getStatusBar(), BorderLayout.SOUTH);
		return centerPanel;
	
	}
	private JSplitPane getSplitPanel(){
		if(splitPanel == null){
			splitPanel = new JSplitPane();
			splitPanel.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			splitPanel.setOneTouchExpandable(true);
			splitPanel.setLeftComponent(getSortTreePanel());
			splitPanel.setRightComponent(getListPanel());	
			this.addComponentListener(new ComponentAdapter(){
	            public void componentResized(ComponentEvent e) {
	            	splitPanel.setDividerLocation(0.2);
	            }
	        }); 
		}
		
		return splitPanel;
	}
	/**
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-25 上午11:39:57
	 * @version 1.0
	 * @exception 
	 * @return
	 */
	private Component getListPanel() {
		kbsChatPanel.init();
		return kbsChatPanel;
	}
	/**
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-25 上午11:39:52
	 * @version 1.0
	 * @exception 
	 * @return
	 */
	private Component getSortTreePanel() {
		netUserTreePanel.init();
		return netUserTreePanel;
	}

	public void doUpdate(CowSwingEvent event) {
		if(event.getEventType().equals(CowSwingEventType.NetChatDownLoadSuccess) || event.getEventType().equals(CowSwingEventType.NetChatDownLoadBeak)){
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					getCenterStatusBar().setMaximum(100);
					getCenterStatusBar().setValue(1);
				}
			});
		}else if(event.getEventType().equals(CowSwingEventType.NetChatDownLoadIng) || event.getEventType().equals(CowSwingEventType.NetChatSendIng)){
			String downloadRateInfo = event.getEventObject().toString();
			if(StringUtils.isNotBlank(downloadRateInfo) && downloadRateInfo.contains(",")){
				downloadRateInfoArray = downloadRateInfo.split(",");
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						getCenterStatusBar().setMaximum(Integer.valueOf(downloadRateInfoArray[1]));
						getCenterStatusBar().setValue(Integer.valueOf(downloadRateInfoArray[0]));
					}
				});
				
			}
		}
	}

	/**
	 * @return the netUserTreePanel
	 */
	public NetUserTreePanel getNetUserTreePanel() {
		return netUserTreePanel;
	}
	//重写这个方法  
    @Override  
    protected void processWindowEvent(WindowEvent e) {  
        if (e.getID() == WindowEvent.WINDOW_CLOSING){
        	Toolkit.getDefaultToolkit().beep();
            int result = JOptionPane.showConfirmDialog(this, LanguageLoader.getString("CrawlerMainFrame.windowClose"), LanguageLoader.getString("CrawlerMainFrame.windowCloseTitle"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null);  
            if(result == JOptionPane.YES_OPTION){
            	this.setVisible(false);
            	//this.dispose();
            }else{
            	return;//直接返回，阻止默认动作，阻止窗口关闭  
            }  	
        }
        super.processWindowEvent(e); //该语句会执行窗口事件的默认动作(如：隐藏)  
    } 

}
