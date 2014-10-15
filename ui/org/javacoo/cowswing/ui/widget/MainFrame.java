package org.javacoo.cowswing.ui.widget;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import org.javacoo.cowswing.base.constant.Config;
import org.javacoo.cowswing.base.constant.Constant;
import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.core.event.CowSwingEvent;
import org.javacoo.cowswing.core.event.CowSwingListener;
import org.javacoo.cowswing.core.event.type.CowSwingEventType;
import org.jdesktop.swingx.JXStatusBar;



/**
 * 窗口基类
 *@author DuanYong
 *@since 2012-11-5上午9:57:20
 *@version 1.0
 */
public abstract class MainFrame extends JFrame implements CowSwingListener{
	private JXStatusBar statusBar;
	
	private JLabel leftStatusBar;

	private JProgressBar centerStatusBar;

	private JLabel rightStatusBar;
	/**消息*/
	protected String msg = "";
	/**下载百分比信息*/
	protected String downloadRateInfo = "";
	/**下载百分比信息数组*/
	protected String[] downloadRateInfoArray;
	private static final long serialVersionUID = 1L;
	
	public MainFrame(){
		super(LanguageLoader.getString("CrawlerMainFrame.title"));
		setSize(Integer.valueOf(Config.COWSWING_CONFIG_MAP.get(Config.COWSWING_CONFIG_KEY_INIT).get(Config.COWSWING_CONFIG_KEY_INIT_FRAME_WIDTH)),Integer.valueOf(Config.COWSWING_CONFIG_MAP.get(Config.COWSWING_CONFIG_KEY_INIT).get(Config.COWSWING_CONFIG_KEY_INIT_FRAME_HEIGHT)));
		
		setIconImage(ImageLoader.getImage("CrawlerResource.logo"));
	} 
	public MainFrame(String title){
		super(title);
		setSize(Constant.FRAME_DEFAULT_WIDTH,Constant.FRAME_DEFAULT_HEIGHT);
		setIconImage(ImageLoader.getImage("CrawlerResource.logo"));
	}
	public MainFrame(String title,Integer width, Integer height){
		super(title);
		setSize(width,height);
		setIconImage(ImageLoader.getImage("CrawlerResource.logo"));
	}
	public MainFrame(String title, Integer width, Integer height, Component owner){
		super(title);
		setSize((null == width) ? Constant.FRAME_DEFAULT_WIDTH : width, (null == height) ? Constant.FRAME_DEFAULT_HEIGHT : height);
		setLocationRelativeTo(owner);
		setIconImage(ImageLoader.getImage("CrawlerResource.logo"));
	}
	/**
	 * 初始化
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-26 上午11:15:27
	 * @version 1.0
	 * @exception
	 */
	public final void init() {
		doInit();
		setVisible(true);
		toFront();
		visibleAfter();
	}
	/**
	 * 执行初始化
	 * <p>方法说明:</>
	 * <li>由子类实现</li>
	 * @author DuanYong
	 * @since 2013-7-26 上午10:54:51
	 * @version 1.0
	 * @exception
	 */
	protected abstract void doInit();
	/**
	 * 显示界面后处理
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-26 上午11:07:58
	 * @version 1.0
	 * @exception
	 */
	private void visibleAfter(){
		getLeftStatusBar().setText(LanguageLoader.getString("CrawlerMainFrame.version") + Config.COWSWING_CONFIG_MAP.get(Config.COWSWING_CONFIG_KEY_VERSION).get(Config.COWSWING_CONFIG_KEY_VERSION_VERSION));
		doVisibleAfter();
	}
	/**
	 * 执行子类实现的显示界面后处理
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-26 上午10:54:47
	 * @version 1.0
	 * @exception
	 */
	protected void doVisibleAfter(){};
	/**
	 * 初始化主界面配置
	 * <p>方法说明:</p>
	 * @auther DuanYong
	 * @since 2012-11-5 上午10:31:49
	 * @return void
	 */
	protected void ininFrame() {
		// 取得屏幕大小
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension oursize = getSize();
		Dimension screensize = kit.getScreenSize();
		// 窗口居中显示
		int x = (screensize.width - oursize.width) / 2;
		int y = (screensize.height - oursize.height) / 2;
		x = Math.max(0, x);
		y = Math.max(0, y);
		setLocation(x, y);
		setResizable(true); 
	}
	protected JXStatusBar getStatusBar() {
		if (statusBar == null) {
			statusBar = new JXStatusBar();
			JXStatusBar.Constraint c = new JXStatusBar.Constraint(JXStatusBar.Constraint.ResizeBehavior.FILL);
			statusBar.add(getLeftStatusBar(), c);
			c = new JXStatusBar.Constraint(JXStatusBar.Constraint.ResizeBehavior.FILL);
			statusBar.add(getCenterStatusBar(), c);
			c = new JXStatusBar.Constraint(JXStatusBar.Constraint.ResizeBehavior.FILL);
			statusBar.add(getRightStatusBar(), c);
		}
		return statusBar;
	}
	public final void update(CowSwingEvent event) {
		if(event.getEventType().equals(CowSwingEventType.ValidDataChangeEvent)){
			msg = event.getEventObject().toString();
			getRightStatusBar().setText(msg);
		}else if(event.getEventType().equals(CowSwingEventType.CacheDataChangeEvent)){
			msg = event.getEventObject().toString();
			getRightStatusBar().setText(msg);
		}else if(event.getEventType().equals(CowSwingEventType.CacheConfigChangeEvent)){
			msg = event.getEventObject().toString();
			getRightStatusBar().setText(msg);
		}
		doUpdate(event);
	}
	protected abstract void doUpdate(CowSwingEvent event);
	
	protected JLabel getLeftStatusBar() {
		if (leftStatusBar == null) {
			leftStatusBar = new JLabel(" ");
		}
		return leftStatusBar;
	}
	
	protected void setLeftStatusBar(String text, String toolTip) {
		getLeftStatusBar().setText(text);
		getLeftStatusBar().setToolTipText(toolTip);
	}
	
	protected JLabel getRightStatusBar() {
		if (rightStatusBar == null) {
			rightStatusBar = new JLabel(" ");
		}
		return rightStatusBar;
	}
	
	protected JProgressBar getCenterStatusBar() {
		if (centerStatusBar == null) {
			centerStatusBar = new ProgressBar(100,false,true,false);
		}
		return centerStatusBar;
	}
	
	protected void setRightStatusBar(String text, String toolTip) {
		getRightStatusBar().setText(text);
		getRightStatusBar().setToolTipText(toolTip);
	}
	
	
	protected void setCenterStatusBar(String text, String toolTip) {
//		getCenterStatusBar().setText(text);
//		getCenterStatusBar().set.setToolTipText(toolTip);
	}
}
