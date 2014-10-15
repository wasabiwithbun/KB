/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.core.task.monitor;

import java.util.Map;

import javax.swing.SwingUtilities;

import org.javacoo.cowswing.base.constant.Constant;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.core.event.CowSwingEvent;
import org.javacoo.cowswing.core.event.type.CowSwingTaskEventType;
import org.springframework.stereotype.Component;

/**
 * 本地文件建立索引进度监控
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-7-23 下午3:32:23
 * @version 1.0
 */
@Component("taskMonitorPanel")
public class TaskMonitorPanel extends AbstractMonitorPanel{
	private static final long serialVersionUID = 1L;

	public TaskMonitorPanel(){
		super();
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.ui.view.panel.ITabPanel#getTanPanelName()
	 */
	@Override
	public String getTabPanelName() {
		return LanguageLoader.getString("Core.task_monitor_title");
	}
    
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.core.event.CowSwingListener#update(org.javacoo.cowswing.core.event.CowSwingEvent)
	 */
	@Override
	public void update(final CowSwingEvent event) {
		if(CowSwingTaskEventType.TaskTypeIngEvent == event.getEventType() || CowSwingTaskEventType.TaskTypeEndEvent == event.getEventType()){
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					if(null != event.getEventObject() && event.getEventObject() instanceof Map){
						Map<String,Object> evtObj = (Map<String, Object>) event.getEventObject();
						Object totalObj = evtObj.get(Constant.TASK_MONITOR_TOTAL);
						Object numObj = evtObj.get(Constant.TASK_MONITOR_NUM);
						outPutInfo(evtObj.get(Constant.TASK_MONITOR_DESC).toString());
						// 以总任务量作为进度条的最大值
						bar.setMaximum(Integer.valueOf(totalObj.toString()));
						// 以任务的当前完成量设置进度条的value
						bar.setValue(Integer.valueOf(numObj.toString()));
					}
				}
			});	
		}
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.plugin.kbs.ui.view.panel.monitor.AbstractMonitorPanel#getDestoryProgressEventType()
	 */
	@Override
	protected CowSwingTaskEventType getDestoryProgressEventType() {
		// TODO Auto-generated method stub
		return CowSwingTaskEventType.TaskTypeEndEvent;
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.plugin.kbs.ui.view.panel.monitor.AbstractMonitorPanel#getUpadteProgressEventType()
	 */
	@Override
	protected CowSwingTaskEventType getUpadteProgressEventType() {
		// TODO Auto-generated method stub
		return CowSwingTaskEventType.TaskTypeIngEvent;
	}

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.plugin.kbs.ui.view.panel.monitor.AbstractMonitorPanel#getMonitorType()
	 */
	@Override
	protected String getMonitorType() {
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.ui.view.panel.ITabPanel#getTabPanelIndex()
	 */
	@Override
	public int getTabPanelIndex() {
		return 3;
	}
	
	

}
