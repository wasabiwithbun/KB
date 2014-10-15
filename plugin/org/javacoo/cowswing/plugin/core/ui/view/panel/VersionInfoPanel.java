/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.core.ui.view.panel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;
import org.javacoo.cowswing.base.constant.Config;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.core.event.CowSwingEvent;
import org.javacoo.cowswing.core.event.type.CowSwingEventType;
import org.javacoo.cowswing.plugin.core.net.MsgBean;
import org.javacoo.cowswing.plugin.core.service.beans.SystemVersionBean;
import org.javacoo.cowswing.ui.view.panel.AbstractContentPanel;
import org.springframework.stereotype.Component;

/**
 * 版本信息列表
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-1-2 下午2:45:36
 * @version 1.0
 */
@Component("versionInfoPanel")
public class VersionInfoPanel extends AbstractContentPanel<MsgBean>{
	private static final long serialVersionUID = 1L;
	protected Logger logger = Logger.getLogger(this.getClass());
	/**操作说明标签*/
	private javax.swing.JLabel operDescLabel;
	/**选择的模块类别标签*/
	private javax.swing.JLabel selectFileLabel;
	/**拖拽说明标签*/
	private javax.swing.JLabel dropLabel;
	/**版本信息列表*/
	private JList versionJList;
	/**版本信息Model*/
	private DefaultListModel versionListModel;
	/**版本信息*/
	private double version = Double.parseDouble(Config.COWSWING_CONFIG_MAP.get(Config.COWSWING_CONFIG_KEY_VERSION).get(Config.COWSWING_CONFIG_KEY_VERSION_VERSION));

	/**操作说明标签*/
	private javax.swing.JLabel versionInfoLabel;
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.ui.view.panel.AbstractContentPanel#populateData()
	 */
	@Override
	protected MsgBean populateData() {
		if(null != versionJList.getSelectedValue()){
			return (MsgBean)versionJList.getSelectedValue();
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.ui.view.panel.AbstractContentPanel#initComponents()
	 */
	@Override
	protected void initComponents() {
		operDescLabel = new javax.swing.JLabel();
		operDescLabel.setText(LanguageLoader.getString("Core.version_operDesc"));
		add(operDescLabel);
		operDescLabel.setBounds(20, 15, 80, 15);
		
		
		dropLabel = new javax.swing.JLabel();
		dropLabel.setText(LanguageLoader.getString("Core.version_info"));
		add(dropLabel);
		dropLabel.setBounds(110, 15, 320, 15);
		
		
		selectFileLabel = new javax.swing.JLabel();
		selectFileLabel.setText(LanguageLoader.getString("Core.version_list"));
		add(selectFileLabel);
		selectFileLabel.setBounds(20, 45, 80, 15);
		
		
		versionListModel = new DefaultListModel();
		versionJList = new JList(versionListModel);
		JScrollPane fileListJScrollPane = new JScrollPane(versionJList);
		add(fileListJScrollPane);
		fileListJScrollPane.setBounds(110, 45, 320, 130);
		
		
		versionInfoLabel = new javax.swing.JLabel();
		versionInfoLabel.setText("");
		add(versionInfoLabel);
		versionInfoLabel.setBounds(110, 185, 320, 15);
	}
	protected void initActionListener(){
		versionJList.addMouseListener(new MouseAdapter(){
			 public void mouseClicked(MouseEvent e) {
				 if(null != versionJList.getSelectedValue()){
					 MsgBean msgBean =  (MsgBean)versionJList.getSelectedValue();
						versionInfoLabel.setText(msgBean.getSystemVersionBean().getInfo());
					}
			 }
		});
	}
	public void initData(MsgBean t){
		fillComponentData(t);
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.ui.view.panel.AbstractContentPanel#fillComponentData(java.lang.Object)
	 */
	@Override
	protected void fillComponentData(MsgBean t) {
		versionListModel.clear();
		versionInfoLabel.setText("");
	}
	/**
	 * 更新监听执行事件
	 */
	public void update(CowSwingEvent event) {
		if (event.getEventType().equals(CowSwingEventType.NetRefresh) || event.getEventType().equals(CowSwingEventType.NetUserOnlineInfoEvent)) {
			MsgBean msgBean = (MsgBean)event.getEventObject();
			logger.info("=====================当前版本："+msgBean.toString());
			int result = Double.compare(version, msgBean.getVersion());
			logger.info("=====================版本对比结果："+result);
			//如果当前版本小于其他客户端版本
			if(result < 0){
				if(!versionListModel.contains(msgBean)){
					versionListModel.addElement(msgBean);
				}
			}
		}
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(double version) {
		this.version = version;
	}
	
}
