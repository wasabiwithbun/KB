/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javacoo.cowswing.base.utils.ImageManager;

import com.sun.awt.AWTUtilities;

/**
 * 快捷菜单导航窗体
 * <p>
 * 说明:
 * </p>
 * <li></li>
 * @author DuanYong
 * @since 2015-11-21下午6:48:31
 * @version 1.0
 */
public class ShortcutFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	protected static final Log logger = LogFactory.getLog(ShortcutFrame.class);
	/** 窗口宽 */
	private int width = 256;
	/** 口高 */
	private int height = 256;
	

	private JPanel imagePanel;

	private MouseAdapter moveWindowListener;
	
	private static ShortcutFrame ui;

	public ShortcutFrame() {
		initListener();
		initComponents();
		registerListener();
		initPosition();
	}
	public static void main(String args[]) {
		ui = new ShortcutFrame();
		ui.setVisible(true);
	}
	
	private void initPosition() {
		// 取得屏幕大小
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension oursize = getSize();
		Dimension screensize = kit.getScreenSize();
		// 窗口居中显示
		int x = (screensize.width - oursize.width) / 2;
		int y = (screensize.height - oursize.height) / 2;
		x = Math.max(0, x);
		y = Math.max(0, y);
		setLocation(screensize.width - oursize.width, screensize.height - oursize.height);
	}

	/**
	 * 初始化组件
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @since 2013-4-20 下午4:27:55
	 * @version 1.0
	 * @exception
	 */
	private void initComponents() {
		//setIconImage(ImageManager.getImageByShortName("crawler_logo.png"));
		setDefaultCloseOperation(2);
		setUndecorated(true);
		AWTUtilities.setWindowOpaque(this, false);
		JPanel centerPane = new JPanel(new BorderLayout());
		setContentPane(centerPane);
		setSize(width, height);
		imagePanel = new JPanel(new BorderLayout());
		imagePanel.setOpaque(false);

		imagePanel.add(createDraggableLabel(ImageManager.getImageIconByShortName("Crawler.png")), "Center");
		centerPane.add(imagePanel, "Center");
		centerPane.setOpaque(false);
	}

	/**
	 * 初始化全局Listener
	 * <p>
	 * 方法说明:</>
	 * <li>初始化MouseAdapter</li>
	 * 
	 * @author DuanYong
	 * @since 2013-4-20 下午4:24:10
	 * @version 1.0
	 * @exception
	 */
	private void initListener() {
		moveWindowListener = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				lastPoint = e.getLocationOnScreen();
			}

			public void mouseDragged(MouseEvent e) {
				Point point = e.getLocationOnScreen();
				int offsetX = point.x - lastPoint.x;
				int offsetY = point.y - lastPoint.y;
				Rectangle bounds = getBounds();
				bounds.x += offsetX;
				bounds.y += offsetY;
				setBounds(bounds);
				lastPoint = point;
			}

			public void mouseEntered(MouseEvent e) {
				
			}

			public void mouseExited(MouseEvent e) {
				
			}

			private Point lastPoint;

		};
	}

	/**
	 * 注册Listener
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @since 2013-4-20 下午4:28:13
	 * @version 1.0
	 * @exception
	 */
	private void registerListener() {
		imagePanel.addMouseListener(moveWindowListener);
		
	}


	
	
	

	/**
	 * 构建可拖动的label
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @since 2013-4-20 下午4:29:31
	 * @version 1.0
	 * @exception
	 * @param icon
	 * @return
	 */
	private JLabel createDraggableLabel(ImageIcon icon) {
		JLabel label = new JLabel(icon);
		label.addMouseListener(moveWindowListener);
		label.addMouseMotionListener(moveWindowListener);
		return label;
	}

	

	


	
	
	
	private void initStyle(JPanel panel){
		Component[] components = panel.getComponents();
		for(Component component : components){
			if(component instanceof JPanel){
				JPanel tempPanel = (JPanel)component;
				initStyle(tempPanel);
			}
			setupComponent(component);
		}
	}
	private void setupComponent(Component c) {
		c.setFont(new Font("微软雅黑", 1, 14));
		c.setForeground(new Color(37, 81, 54));
	}
}
