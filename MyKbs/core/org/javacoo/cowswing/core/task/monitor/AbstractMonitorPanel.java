/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.core.task.monitor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import org.javacoo.cowswing.core.event.CowSwingObserver;
import org.javacoo.cowswing.core.event.CowSwingTaskObserver;
import org.javacoo.cowswing.core.event.type.CowSwingTaskEventType;
import org.javacoo.cowswing.ui.view.panel.AbstractTabPanel;

/**
 * 监控面板抽象类
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-7-23 下午3:34:35
 * @version 1.0
 */
public abstract class AbstractMonitorPanel extends AbstractTabPanel{
	private static final long serialVersionUID = 1L;
	/**中部面板*/
	private JPanel centerPanel;
	/**进度条面板*/
	protected JPanel progressPanel;
	/**分隔面板*/
	private JSplitPane splitPanel;
	/**信息显示*/
	private JTextArea taskOutput;
	/**创建一条水平进度条*/
	protected JProgressBar bar = new JProgressBar(JProgressBar.HORIZONTAL);
	public AbstractMonitorPanel(){
		super();
	}
	
	protected Component getCenterPanel() {
		centerPanel = new JPanel(new BorderLayout());
		centerPanel.add(getProgressPanel(),BorderLayout.NORTH);
		centerPanel.add(getProgressDetailPanel(),BorderLayout.CENTER);
		//centerPanel.add(getSplitPanel());
		return centerPanel;
	}
	
	private JSplitPane getSplitPanel(){
		if(splitPanel == null){
			splitPanel = new JSplitPane();
			splitPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
			splitPanel.setOneTouchExpandable(true);
			splitPanel.setTopComponent(getProgressPanel());
			splitPanel.setBottomComponent(getProgressDetailPanel());	
			this.addComponentListener(new ComponentAdapter(){
	            public void componentResized(ComponentEvent e) {
	            	splitPanel.setDividerLocation(0.1);
	            }
	        }); 
		}
		
		return splitPanel;
	}

	public void initOther() {
		progressPanel.removeAll();
		progressPanel.add(bar);
		progressPanel.repaint();
		// 设置在进度条中绘制完成百分比
		bar.setStringPainted(true);
		// 设置进度条的最大值和最小值,
		bar.setMinimum(0);
	}

	/**
	 * 进度条详细面板
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-4-30 下午1:51:50
	 * @version 1.0
	 * @exception 
	 * @return
	 */
	private Component getProgressDetailPanel() {
		taskOutput = new JTextArea();
		taskOutput.setMargin(new Insets(2, 2, 2, 2));
		taskOutput.setEditable(false);
		taskOutput.setAutoscrolls(true);
		// 不显示滚动条
		JScrollPane scrollPane = new JScrollPane(taskOutput,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		return scrollPane;
	}
	/**
	 * 取得销毁进度条事件
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-4-30 下午4:25:04
	 * @version 1.0
	 * @exception 
	 * @return
	 */
	protected abstract CowSwingTaskEventType getDestoryProgressEventType();
	/**
	 * 取得更新进度条事件
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-4-30 下午4:25:19
	 * @version 1.0
	 * @exception 
	 * @return
	 */
	protected abstract CowSwingTaskEventType getUpadteProgressEventType();
    /**
     * 取得监控类型
     * <p>方法说明:</>
     * <li></li>
     * @author DuanYong
     * @since 2013-4-30 下午2:24:51
     * @version 1.0
     * @exception 
     * @return
     */
	protected abstract String getMonitorType();

	/**
	 * 进度条面板
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-4-30 下午1:51:47
	 * @version 1.0
	 * @exception 
	 * @return
	 */
	private Component getProgressPanel(){
		progressPanel = new JPanel();
		GridLayout layout = new GridLayout(1, 1, 1, 1);
		progressPanel.setLayout(layout);
		return new JScrollPane(progressPanel);
	}
	protected void outPutInfo(String str) {
		if (taskOutput.getRows() > 18) {
			taskOutput.remove(18);
		}
		taskOutput.append(str + ".\n");
		// 定位到控件底部
		taskOutput.setCaretPosition(taskOutput.getDocument().getLength());
	}
	protected void addCrawlerListener(){
		CowSwingTaskObserver.getInstance().addCrawlerListener(this);
	}
}
