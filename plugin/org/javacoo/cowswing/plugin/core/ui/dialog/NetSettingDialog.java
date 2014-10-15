/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.core.ui.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.swing.JButton;
import javax.swing.JComponent;

import org.javacoo.cowswing.base.constant.Constant;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.base.utils.MsgDialogUtil;
import org.javacoo.cowswing.core.event.CowSwingListener;
import org.javacoo.cowswing.plugin.core.net.NetManager;
import org.javacoo.cowswing.plugin.core.service.beans.NetSettingBean;
import org.javacoo.cowswing.plugin.core.ui.view.panel.NetSettingPanel;
import org.javacoo.cowswing.ui.view.dialog.AbstractDialog;
import org.springframework.stereotype.Component;

/**
 * 网络设置
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-16 下午6:29:21
 * @version 1.0
 */
@Component("netSettingDialog")
public class NetSettingDialog extends AbstractDialog implements
		CowSwingListener {
	private static final long serialVersionUID = 1L;
	/** 网络设置面板 */
	@Resource(name = "netSettingPanel")
	private NetSettingPanel netSettingPanel;
	/**网络设置对象*/
	private NetSettingBean netSettingBean;
	/** 网络管理 */
	@Resource(name = "netManager")
	private NetManager netManager;
	public NetSettingDialog(){
		super(500,400,true);
		this.setUndecorated(true);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				checkValue();
				connection();
			}
		});

	}
	@Override
	public JComponent getCenterPane() {
		if (centerPane == null) {
			centerPane = netSettingPanel;
		}
		return centerPane;
	}
	public JButton getCancelButton(){
		return null;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.javacoo.cowswing.ui.view.dialog.AbstractDialog#
	 * finishButtonActionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	protected void finishButtonActionPerformed(ActionEvent event) {
		if(checkValue()){
			connection();
			this.dispose();
		}
	}
	/**
	 * 校验参数
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-12 下午2:20:30
	 * @version 1.0
	 * @exception 
	 * @return
	 */
	private boolean checkValue(){
		netSettingBean = new NetSettingBean();
		netSettingBean = netSettingPanel.getData();
		
		Pattern pattern = Pattern.compile(Constant.NET_IP_REGEX);
		Matcher ipMatcher = pattern.matcher(netSettingBean.getIp());
		if (!ipMatcher.matches()) {
			MsgDialogUtil.createMessageDialog(LanguageLoader.getString("Kbs.net_setting_ip_error"));
			return false;
		}else if(netSettingBean.getPort() < 30000 || netSettingBean.getPort() > 65535){
			MsgDialogUtil.createMessageDialog(LanguageLoader.getString("Kbs.net_setting_port_error"));
			return false;
		}else{
			netManager.setNetSettingBean(netSettingBean);
			return true;
		}
	}	
	/**
	 * 建立连接
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-12 下午2:37:59
	 * @version 1.0
	 * @exception
	 */
	private void connection(){
		try {
			netManager.connectionNetwork();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	protected void initData(String type) {
		netManager.disconnectNetwork();
		fillJTabbedPane();
	}
	public void dispose(){
		super.dispose();
		centerPane = null;
	}
	/**
	 * 填充JTabbedPane值
	 * <p>方法说明:</p>
	 * @auther DuanYong
	 * @since 2012-12-3 下午12:20:32
	 * @return void
	 */
	private void fillJTabbedPane(){
		logger.info("网络连接参数设置");
		netSettingPanel.initData(netSettingBean);
	}
	
	

	
}
