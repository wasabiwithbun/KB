/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.core.ui.view.panel;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.annotation.Resource;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import org.apache.log4j.Logger;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.core.event.CowSwingEvent;
import org.javacoo.cowswing.core.event.type.CowSwingEventType;
import org.javacoo.cowswing.plugin.core.constant.CoreConstant;
import org.javacoo.cowswing.plugin.core.net.MsgBean;
import org.javacoo.cowswing.plugin.core.net.NetManager;
import org.javacoo.cowswing.plugin.core.service.beans.NetSettingBean;
import org.javacoo.cowswing.ui.listener.IntegerVerifier;
import org.javacoo.cowswing.ui.listener.TextVerifier;
import org.javacoo.cowswing.ui.view.panel.AbstractContentPanel;
import org.springframework.stereotype.Component;


/**
 * 网络设置面板
 * <p>
 * 说明:
 * </p>
 * <li></li>
 * 
 * @author DuanYong
 * @since 2013-7-16 下午5:18:31
 * @version 1.0
 */
@Component("netSettingPanel")
public class NetSettingPanel extends AbstractContentPanel<NetSettingBean> {
	private static final long serialVersionUID = 1L;
	protected Logger logger = Logger.getLogger(this.getClass());
	/** 网络管理 */
	@Resource(name = "netManager")
	private NetManager netManager;
	/** 组播IP标签 */
	private javax.swing.JLabel ipLabel;
	/** 组播IP输入框 */
	private javax.swing.JTextField ipField;
	/** 组播端口标签 */
	private javax.swing.JLabel portLabel;
	/** 组播端口输入框 */
	private javax.swing.JTextField portField;
	/** 用户名标签 */
	private javax.swing.JLabel userNameLabel;
	/** 用户名输入框 */
	private javax.swing.JTextField userNameField;

	private Border border = BorderFactory.createLineBorder(Color.GREEN, 1);
	/** 已经监听的播组 */
	private JList listPart;
	/**已经监听的播组Model*/
	private DefaultListModel listPartListModel;
	/** 全部播组 */
	private JList listAll;
	/**全部播组Model*/
	private DefaultListModel listAllListModel;
	/** 刷新 */
	private JButton btnRefresh;
	/** 添加 */
	private JButton btnAdd;
	/** 删除 */
	private JButton btnRemove;
	/** JScrollPane */
	private JScrollPane scrollPanelAll;
	/** JScrollPane */
	private JScrollPane scrollPanelPart;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.javacoo.cowswing.ui.view.panel.AbstractContentPanel#populateData()
	 */
	@Override
	protected NetSettingBean populateData() {
		NetSettingBean netSettingBean = new NetSettingBean();
		netSettingBean.setIp(ipField.getText());
		netSettingBean.setPort(Integer.valueOf(portField.getText()));
		netSettingBean.setUserName(userNameField.getText());
		for(int i = 0;i<listAllListModel.size();i++){
			netSettingBean.getListMulIpAndPort().add((NetSettingBean)listAllListModel.getElementAt(i));
		}
		for(int i = 0;i<listPartListModel.size();i++){
			netSettingBean.getListListenMulIpAndPort().add((NetSettingBean)listPartListModel.getElementAt(i));
		}
		return netSettingBean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.javacoo.cowswing.ui.view.panel.AbstractContentPanel#initComponents()
	 */
	@Override
	protected void initComponents() {

		ipLabel = new javax.swing.JLabel();
		ipLabel.setText(LanguageLoader.getString("Kbs.net_setting_ip"));
		add(ipLabel);
		ipLabel.setBounds(20, 15, 80, 15);

		ipField = new javax.swing.JTextField();
		ipField.setColumns(20);
		ipField.setText("");
		add(ipField);
		ipField.setBounds(110, 15, 360, 21);

		portLabel = new javax.swing.JLabel();
		portLabel.setText(LanguageLoader.getString("Kbs.net_setting_port"));
		add(portLabel);
		portLabel.setBounds(20, 45, 80, 15);

		portField = new javax.swing.JTextField();
		portField.setColumns(20);
		portField.setInputVerifier(new IntegerVerifier(this, false, 30000,
				65535));
		portField.setText("");
		add(portField);
		portField.setBounds(110, 45, 360, 21);

		userNameLabel = new javax.swing.JLabel();
		userNameLabel.setText(LanguageLoader
				.getString("Kbs.net_setting_userName"));
		add(userNameLabel);
		userNameLabel.setBounds(20, 75, 80, 15);

		userNameField = new javax.swing.JTextField();
		userNameField.setColumns(20);
		userNameField.setInputVerifier(new TextVerifier(this, false));
		userNameField.setText("");
		add(userNameField);
		userNameField.setBounds(110, 75, 360, 21);
		
		
		listAllListModel = new DefaultListModel();
		listAll = new JList(listAllListModel);

		scrollPanelAll = new JScrollPane(listAll);
		scrollPanelAll.setBounds(15, 105, 150, 200);
		scrollPanelAll.setBorder(BorderFactory.createTitledBorder(border,
				"网络上所有的组播段"));
		add(scrollPanelAll);

		listPartListModel = new DefaultListModel();
		listPart = new JList(listPartListModel);
		
		scrollPanelPart = new JScrollPane(listPart);
		scrollPanelPart.setBounds(255, 105, 150, 200);
		scrollPanelPart.setBorder(BorderFactory.createTitledBorder(border,
				"要加入监听的组播段"));
		add(scrollPanelPart);
		
		btnRefresh = new JButton();
		btnRefresh.setText("刷新");
		btnRefresh.setBounds(180, 105, 60, 25);
		add(btnRefresh);

		btnAdd = new JButton();
		btnAdd.setText(">>");
		btnAdd.setBounds(180, 135, 60, 25);
		add(btnAdd);

		btnRemove = new JButton();
		btnRemove.setText("<<");
		btnRemove.setBounds(180, 165, 60, 25);
		add(btnRemove);

	}

	/**
	 * 初始化事件
	 * <p>
	 * 方法说明:
	 * </p>
	 * 
	 * @auther DuanYong
	 * @since 2012-11-16 上午11:00:17
	 * @return void
	 */
	protected void initActionListener() {
		btnRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				netManager.getNetSettingBean().getListMulIpAndPort().clear();
				netManager.getCommonMsgHandler().sendMsg(
						new MsgBean(CoreConstant.NET_ACTION_TYPE_LISTEN,
								netManager.getNetClientBean().getIp(), String
										.valueOf(netManager.getNetClientBean()
												.getPort())));
			}
		});

		btnAdd.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				Object[] objs = listAll.getSelectedValues();
				addItemsToListPart(objs);
			}

			private void addItemsToListPart(Object[] objs) {
				List<NetSettingBean> list = netManager.getNetSettingBean()
						.getListListenMulIpAndPort();
				NetSettingBean tempNetSettingBean = null;
				for (Object object : objs) {
					tempNetSettingBean = (NetSettingBean) object;
					if (!list.contains(tempNetSettingBean)) {
						list.add(tempNetSettingBean);
						listPartListModel.addElement(tempNetSettingBean);
					}
				}
			}
		});

		btnRemove.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				Object[] objs = listPart.getSelectedValues();
				removeItemsFromListPart(objs);
			}

			private void removeItemsFromListPart(Object[] objs) {
				List<NetSettingBean> list = netManager.getNetSettingBean()
						.getListListenMulIpAndPort();
				NetSettingBean tempNetSettingBean = null;
				for (Object object : objs) {
					tempNetSettingBean = (NetSettingBean) object;
					if (list.contains(tempNetSettingBean)) {
						list.remove(tempNetSettingBean);
						listPartListModel.removeElement(tempNetSettingBean);
					}
				}
			}
		});
	}

	public void initData(NetSettingBean t) {
		if (t == null) {
			t = new NetSettingBean();
		}
		fillComponentData(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.javacoo.cowswing.ui.view.panel.AbstractContentPanel#fillComponentData
	 * (java.lang.Object)
	 */
	@Override
	protected void fillComponentData(NetSettingBean t) {
		listPartListModel.clear();
		for(NetSettingBean netSettingBean : netManager.getNetSettingBean().getListListenMulIpAndPort()){
			listPartListModel.addElement(netSettingBean);
		}
		netManager.getNetSettingBean().getListMulIpAndPort().clear();
		netManager.getCommonMsgHandler().sendMsg(
				new MsgBean(CoreConstant.NET_ACTION_TYPE_LISTEN, netManager
						.getNetClientBean().getIp(), String.valueOf(netManager
						.getNetClientBean().getPort())));
		ipField.setText(netManager.getNetSettingBean().getIp());
		portField.setText(String.valueOf(netManager.getNetSettingBean().getPort()));
		userNameField.setText(netManager.getNetSettingBean().getUserName());
	}

	/**
	 * 更新监听执行事件
	 */
	public void update(CowSwingEvent event) {
		if (event.getEventType().equals(CowSwingEventType.NetListenReturn)) {
			listAllListModel.clear();
			for(NetSettingBean netSettingBean : netManager.getNetSettingBean().getListMulIpAndPort()){
				listAllListModel.addElement(netSettingBean);
			}
			logger.info("当前播组大小："+listAllListModel.getSize());
		}
	}

}
