/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.chat.ui.view.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.javacoo.cowswing.base.constant.Config;
import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.base.utils.DateUtil;
import org.javacoo.cowswing.core.event.CowSwingEvent;
import org.javacoo.cowswing.core.event.type.CowSwingEventType;
import org.javacoo.cowswing.plugin.chat.net.ChatFolderTransmit;
import org.javacoo.cowswing.plugin.chat.ui.view.frame.KbsChatFrame;
import org.javacoo.cowswing.plugin.chat.ui.view.frame.KbsSingleVideoFrame;
import org.javacoo.cowswing.plugin.core.constant.CoreConstant;
import org.javacoo.cowswing.plugin.core.net.Client;
import org.javacoo.cowswing.plugin.core.net.MsgBean;
import org.javacoo.cowswing.plugin.core.net.NetManager;
import org.javacoo.cowswing.plugin.core.service.beans.NetClientBean;
import org.javacoo.cowswing.ui.view.panel.AbstractListPage;

/**
 * 聊天室面板
 * <p>
 * 说明:
 * </p>
 * <li></li>
 * 
 * @author DuanYong
 * @since 2013-7-15 上午11:19:21
 * @version 1.0
 */
@org.springframework.stereotype.Component("kbsChatPanel")
public class KbsChatPanel extends AbstractListPage implements
		ListSelectionListener {
	protected Logger logger = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	/** KbsChatFrame */
	@Resource(name = "kbsChatFrame")
	private KbsChatFrame kbsChatFrame;
	/** 网络管理服务 */
	@Resource(name = "netManager")
	private NetManager netManager;
	/** 视频面板 */
	@Resource(name = "kbsSingleVideoFrame")
	private KbsSingleVideoFrame kbsSingleVideoFrame;
	private JPanel containerPanel;
	private TextArea txtaChatContent;
	private TextArea txtaSendContent;
	private JPanel scrollPaneChat;
	private JPanel scrollPaneSend;
	private Border border = BorderFactory.createMatteBorder(1, 1, 1, 1,
			Color.BLUE);
	private JButton btnSend;
	private boolean isFirstTime = true;
	private JButton btnFolder;
	private JButton btnRecv;
	private JLabel lblInfo;
	private JRadioButton rbtnTop;
	private JRadioButton rbtnEnter;
	private JRadioButton rbtnGroupRefuse;
	private JButton btnRefresh;
	private JButton btnClear;
	private String senderInfo = "";
	private JRadioButton rbtnImMsg;
	private ChatFolderTransmit chatFolderTransmit;
	private ChatFolderTransmit receiveChatFolderTransmit;
	private NetClientBean selectNetClientBean;
	private Client client;
	private Timer timer = new Timer();
	public static boolean isSending = false;
	public static boolean isRecving = false;
	private KbsChatPanel kbsChatPanel;
	/**中部面板*/
	private JPanel centerPanel;
	/**版本*/
	private double version = Double.parseDouble(Config.COWSWING_CONFIG_MAP.get(Config.COWSWING_CONFIG_KEY_VERSION).get(Config.COWSWING_CONFIG_KEY_VERSION_VERSION));
	/**视频申请信息*/
	private String[] applyVideoParam;
	/**是否同意视频状态 --0 ：无响应*/
	private static final String AGREE_VIDEO_STATE_0 = "0";
	/**是否同意视频状态 --1 ：同意*/
	private static final String AGREE_VIDEO_STATE_1 = "1";
	/**是否同意视频状态 --2：拒绝*/
	private static final String AGREE_VIDEO_STATE_2 = "2";
	/**是否同意视频*/
	private String isAgreeVideo = AGREE_VIDEO_STATE_0;
	/**视频状态 --0 ：空闲状态*/
	private static final String VIDEO_STATE_0 = "0";
	/**视频状态 --1 ：申请中*/
	private static final String VIDEO_STATE_1 = "1";
	/**视频状态 --2 ：视频中*/
	private static final String VIDEO_STATE_2 = "2";
	/**视频状态 --0 ：空闲状态，1：申请中，2：视频中*/
	private String videoState = VIDEO_STATE_0;
	
	@Override
	protected JComponent getTopPane() {
		super.getTopPane();
		return buttonBar;
	}

	@Override
	protected JComponent getCenterPane() {
		centerPanel = new JPanel(new BorderLayout());
		centerPanel.add(getContainerPanel());
		//add(getStatusBar(), BorderLayout.SOUTH);
		return centerPanel;
	}
	private JComponent getContainerPanel() {
		containerPanel = new JPanel(new BorderLayout(1, 2));
		createChatPanel();
		this.add(containerPanel);
		kbsChatPanel = this;
		return containerPanel;
	}

	private void createChatPanel() {
		JPanel chatPanel = new JPanel(new BorderLayout(1, 2));
		chatPanel.add(getScrollPaneChat(), BorderLayout.CENTER);

		JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		radioPanel.add(getRbtnImMsg());
		radioPanel.add(getRbtnTop());
		radioPanel.add(getRbtnEnter());
		radioPanel.add(getRbtnGroupRefuse());
		chatPanel.add(radioPanel, BorderLayout.SOUTH);
		containerPanel.add(chatPanel, BorderLayout.CENTER);

		JPanel sendPanel = new JPanel(new BorderLayout(1, 2));
		sendPanel.add(getScrollPaneSend(), BorderLayout.CENTER);

		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		btnPanel.add(getBtnClear());
		btnPanel.add(getBtnFolder());
		btnPanel.add(getBtnRecv());
		btnPanel.add(getBtnRefresh());
		btnPanel.add(getBtnSend());
		btnPanel.add(getLblInfo());
		sendPanel.add(btnPanel, BorderLayout.SOUTH);
		containerPanel.add(sendPanel, BorderLayout.SOUTH);
	}

	public JButton getBtnRefresh() {
		if (btnRefresh == null) {
			btnRefresh = new JButton();
			btnRefresh.setText("刷新");
			btnRefresh.setIcon(ImageLoader.getImageIcon("CrawlerResource.kbs_chat_msg_refush"));
			btnRefresh.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					refresh();
				}
			});
		}
		return btnRefresh;
	}

	public void refresh() {
		MsgBean msgBean = new MsgBean(
				CoreConstant.NET_ACTION_TYPE_REFRESH, netManager
				.getNetClientBean().getIp(), String.valueOf(netManager
				.getNetClientBean().getPort()));
		msgBean.setIp(netManager.getNetClientBean().getIp());
		msgBean.setPort(netManager.getNetClientBean().getPort());
		msgBean.setVersion(version);
		netManager.sendGroupMsgToAll(msgBean);
	}

	public JRadioButton getRbtnGroupRefuse() {
		if (rbtnGroupRefuse == null) {
			rbtnGroupRefuse = new JRadioButton();
			rbtnGroupRefuse.setText("群发屏蔽");
			rbtnGroupRefuse
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							txtaSendContent.setFocusable(true);
							txtaSendContent.requestFocus();
						}
					});
		}
		return rbtnGroupRefuse;
	}

	private JRadioButton getRbtnEnter() {
		if (rbtnEnter == null) {
			rbtnEnter = new JRadioButton();
			rbtnEnter.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					txtaSendContent.setFocusable(true);
					txtaSendContent.requestFocus();
				}
			});
			rbtnEnter.setText("按Enter发送");
		}
		return rbtnEnter;
	}

	public JRadioButton getRbtnTop() {
		if (rbtnTop == null) {
			rbtnTop = new JRadioButton();
			rbtnTop.setText("窗口顶置");
			rbtnTop.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					kbsChatFrame.setAlwaysOnTop(rbtnTop.isSelected());
				}
			});
			rbtnTop.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					txtaSendContent.setFocusable(true);
					txtaSendContent.requestFocus();
				}
			});
		}
		return rbtnTop;
	}

	public JRadioButton getRbtnImMsg() {
		if (rbtnImMsg == null) {
			rbtnImMsg = new JRadioButton();
			rbtnImMsg.setText("重要信息");
			rbtnImMsg.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					txtaSendContent.setFocusable(true);
					txtaSendContent.requestFocus();
				}
			});
		}
		return rbtnImMsg;
	}

	public JLabel getLblInfo() {
		if (lblInfo == null) {
			lblInfo = new JLabel();
			lblInfo.setForeground(Color.RED);
			lblInfo.setVisible(false);
			lblInfo.setText("完成:  0%");
		}
		return lblInfo;
	}

	public JButton getBtnRecv() {
		if (btnRecv == null) {
			btnRecv = new JButton();
			btnRecv.setVisible(false);
			btnRecv.setIcon(ImageLoader.getImageIcon("CrawlerResource.kbs_chat_msg_put"));
			btnRecv.setText("接收");
			btnRecv.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					String btnText = btnRecv.getText();
					if ("接收".equals(btnText)) {
						btnRecv.setText("断开");
						btnRecv.setIcon(ImageLoader.getImageIcon("CrawlerResource.kbs_chat_msg_cancel"));
						JFileChooser chooser = new JFileChooser();
						chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						int ret = chooser.showOpenDialog(null);
						if (ret == JFileChooser.OPEN_DIALOG) {
							File file = chooser.getSelectedFile();
							String path = file.getAbsolutePath();
							lblInfo.setVisible(true);
							receiveChatFolderTransmit.receive(path);
						}

					} else if ("断开".equals(btnText)) {
						btnRecv.setText("接收");
						btnRecv.setIcon(ImageLoader.getImageIcon("CrawlerResource.kbs_chat_msg_put"));
						receiveChatFolderTransmit.breakRecvFolder();
					} else if ("取消".equals(btnText)) {
						KbsChatPanel.isSending = false;
						btnRecv.setText("接收");
						btnRecv.setIcon(ImageLoader.getImageIcon("CrawlerResource.kbs_chat_msg_put"));
						btnRecv.setVisible(false);
						lblInfo.setVisible(false);
						btnFolder.setEnabled(true);
						if (chatFolderTransmit != null) {
							chatFolderTransmit.stop();
							chatFolderTransmit = null;
						}
					}
				}
			});
		}
		return btnRecv;
	}

	public JButton getBtnFolder() {
		if (btnFolder == null) {
			btnFolder = new JButton();
			btnFolder.setEnabled(false);
			btnFolder.setText("文件");
			btnFolder.setIcon(ImageLoader.getImageIcon("CrawlerResource.kbs_chat_msg_remove"));
			btnFolder.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (KbsChatPanel.isSending) {
						appendChatMsg("一次只能给一个人发送文件,请等待...\n");
						return;
					}
					if (KbsChatPanel.isRecving) {
						appendChatMsg("接收文件的同时不能发送文件,请等待...\n");
						return;
					}
					JFileChooser chooser = new JFileChooser();
					chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
					int ret = chooser.showOpenDialog(null);
					if (ret == JFileChooser.OPEN_DIALOG) {
						String path = chooser.getSelectedFile()
								.getAbsolutePath();
						String folderName = new File(path).getName();
						if (folderName.trim().isEmpty()) {
							// txtaChatContent.append("不能发送整个盘,请选择某一个文件夹.\n\n");
							appendChatMsg("不能发送整个盘,请选择某一个文件夹.\n");
							return;
						}
						txtaSendContent.setText(path);
						txtaSendContent.setEditable(false);
						chatFolderTransmit = new ChatFolderTransmit(path,
								getSelectNetClientBean().getIp(),
								getSelectNetClientBean().getPort(),
								kbsChatPanel);
					}
				}
			});
		}
		return btnFolder;
	}

	public synchronized void appendChatMsg(String msg) {
		String sender = msg.substring(0, msg.indexOf("\n")).trim();
		if (!senderInfo.equals(sender)) {
			senderInfo = sender;
			txtaChatContent.append(msg);
			txtaChatContent.append("\n");
		}
	}

	public JPanel getScrollPaneChat() {
		if (scrollPaneChat == null) {
			scrollPaneChat = new JPanel();
			scrollPaneChat.setBorder(BorderFactory.createTitledBorder(border,
					"聊天内容------------------------------------------------["
							+ DateUtil.formatDate(new Date()) + "]"));
			scrollPaneChat.setLayout(new BorderLayout());
			scrollPaneChat.add(getTxtaChatContent(), BorderLayout.CENTER);

			timer.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					scrollPaneChat.setBorder(BorderFactory.createTitledBorder(
							border,
							"聊天内容------------------------------------------------["
									+ DateUtil.formatDate(new Date()) + "]"));
				}
			}, 1000, 1000);

		}
		return scrollPaneChat;
	}

	public JPanel getScrollPaneSend() {
		if (scrollPaneSend == null) {
			scrollPaneSend = new JPanel();
			scrollPaneSend.setBorder(BorderFactory.createTitledBorder(border,
					"发送消息"));
			scrollPaneSend.setLayout(new BorderLayout());
			scrollPaneSend.add(getTxtaSendContent(), BorderLayout.CENTER);
		}
		return scrollPaneSend;
	}

	public JButton getBtnSend() {
		if (btnSend == null) {
			btnSend = new JButton();
			btnSend.setEnabled(false);
			btnSend.setText("发送");
			btnSend.setIcon(ImageLoader.getImageIcon("CrawlerResource.kbs_chat_msg_addAll"));
			btnSend.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					String sendMsg = txtaSendContent.getText();
					if (sendMsg.trim().isEmpty()) {
						return;
					} else {
						if (chatFolderTransmit != null
								&& !txtaSendContent.isEditable()) {
							if (isToMyself(getSelectNetClientBean())) {
								appendChatMsg("不能给自己发送文件!\n");
								btnClear.doClick();
								return;
							}else if(KbsChatPanel.isRecving){
								appendChatMsg("文件接收中,不能发送文件!\n");
								btnClear.doClick();
								return;
							}else if(KbsChatPanel.isSending){
								appendChatMsg("文件发送中,不能发送文件!\n");
								btnClear.doClick();
								return;
							}
							logger.info("发送文件中。。。");
							chatFolderTransmit.send();
							btnRecv.setText("取消");
							btnRecv.setIcon(ImageLoader.getImageIcon("CrawlerResource.kbs_chat_msg_cancel"));
							btnRecv.setVisible(true);
							lblInfo.setText("等待中...");
							lblInfo.setVisible(true);
							txtaSendContent.setEditable(true);
							txtaSendContent.setText("");
							btnFolder.setEnabled(false);
						} else {
							String say = "";
							if (StringUtils.isNotBlank(getSelectNetClientBean()
									.getIp())) {
								say = buildSendMsg(sendMsg, "私聊信息");
								if (client != null) {
									client.sendMsg(new MsgBean(
											CoreConstant.NET_ACTION_TYPE_PERSONMSG,
											say));
								}
								if (say.startsWith("△imXwc▲")) {
									say = say.substring(7);
								}
								appendChatMsg(say);
							} else {
								if (!rbtnGroupRefuse.isSelected()) {
									say = buildSendMsg(sendMsg, "群发信息");
									netManager.sendGroupMsgToAll(new MsgBean(
											CoreConstant.NET_ACTION_TYPE_COMMONMSG,
													say));
									if (say.startsWith("△imXwc▲")) {
										say = say.substring(7);
									}
									appendChatMsg(say);
								} else {
									return;
								}
							}
							txtaSendContent.setText("");
							txtaSendContent.setFocusable(true);
							txtaSendContent.requestFocus();
						}
					}
				}

				private boolean isToMyself(NetClientBean netClientBean) {
					if (netManager.getNetClientBean().equals(netClientBean)) {
						return true;
					}
					return false;
				}

				private String buildSendMsg(String sendMsg, String msgType) {
					String[] sendMsgs = sendMsg.split("\n");
					sendMsg = "\n";
					for (String msg : sendMsgs) {
						sendMsg += ("\n  " + msg);
					}
					String userName = netManager.getNetSettingBean()
							.getUserName();
					StringBuilder sb = new StringBuilder();
					userName += ("(" + netManager.getNetClientBean().getIp()
							+ ":" + netManager.getNetClientBean().getPort()
							+ ") 【" + msgType + "】");
					String nowTime = getNowTime();
					if (rbtnImMsg.isSelected()) {
						sb.append("△imXwc▲");
						rbtnImMsg.setSelected(false);
					}
					sb.append(userName).append(" 说：  ").append(nowTime)
							.append(sendMsg).append("\n");
					return sb.toString();
				}

				private String getNowTime() {
					Calendar now = Calendar.getInstance();
					String nowTime = paddingTime(now.get(Calendar.HOUR), 0)
							+ ":" + paddingTime(now.get(Calendar.MINUTE), 1)
							+ ":" + paddingTime(now.get(Calendar.SECOND), 2);
					return nowTime;
				}

				private String paddingTime(int value, int type) {
					String ret = "" + value;
					if (value < 10) {
						ret = "0" + value;
					}
					if (ret.equals("00") && type == 0) {
						ret = "12";
					}
					return ret;
				}
			});
		}
		return btnSend;
	}

	public void setSendBtnState(boolean b) {
		btnSend.setEnabled(b);
	}

	public TextArea getTxtaChatContent() {
		if (txtaChatContent == null) {
			txtaChatContent = new TextArea();
			txtaChatContent.setFont(new Font(getFont().getFontName(), getFont()
					.getStyle(), getFont().getSize() + 3));
			txtaChatContent.setEditable(false);
			txtaChatContent.setBackground(Color.WHITE);

			final JPopupMenu popupMenu_1 = new JPopupMenu();
			addPopup(txtaChatContent, popupMenu_1);

			final JMenuItem newItemMenuItem = new JMenuItem();
			newItemMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					if (!txtaChatContent.getText().trim().isEmpty()) {
						// ChatRecordMgr chatRecordMgr = new ChatRecordMgr(
						// txtaChatContent.getText());
						// chatRecordMgr.save();
						txtaChatContent.setText("");
					}
				}
			});
			newItemMenuItem.setText("Save ChatRecord");
			popupMenu_1.add(newItemMenuItem);

			popupMenu_1.addSeparator();

			final JMenuItem newItemMenuItem2 = new JMenuItem();
			newItemMenuItem2.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					// new ChatRecordMgr("").openChatRecord();
				}
			});
			newItemMenuItem2.setText("Open ChatRecord");
			popupMenu_1.add(newItemMenuItem2);

			popupMenu_1.addSeparator();

			final JMenuItem newItemMenuItem3 = new JMenuItem();
			newItemMenuItem3.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					txtaChatContent.setText("");
				}
			});
			newItemMenuItem3.setText("Clear ChatRecord");
			popupMenu_1.add(newItemMenuItem3);

			popupMenu_1.addSeparator();

			final JMenuItem newItemMenuItem4 = new JMenuItem();
			newItemMenuItem4.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					// new ChatRecordMgr("").openMemo();
				}
			});
			newItemMenuItem4.setText("Open MemoRecord");
			popupMenu_1.add(newItemMenuItem4);

			popupMenu_1.addSeparator();

			final JMenuItem newItemMenuItem5 = new JMenuItem();
			newItemMenuItem5.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					// new ChatRecordMgr("").openRecordDir();
				}
			});
			newItemMenuItem5.setText("Open RecordDirectory");
			popupMenu_1.add(newItemMenuItem5);

			txtaChatContent.addMouseWheelListener(new MouseWheelListener() {

				@Override
				public void mouseWheelMoved(MouseWheelEvent e) {
					if (e.isControlDown()) {
						e.consume();
						Font dFont = txtaChatContent.getFont();
						int size = dFont.getSize();
						size -= e.getUnitsToScroll();
						Font font = new Font(dFont.getFontName(), dFont
								.getStyle(), size);
						txtaChatContent.setFont(font);
					}
				}
			});
		}
		return txtaChatContent;
	}

	public TextArea getTxtaSendContent() {
		if (txtaSendContent == null) {
			txtaSendContent = new TextArea();
			txtaSendContent.setFont(new Font(getFont().getFontName(), getFont()
					.getStyle(), getFont().getSize() + 3));

			final JPopupMenu popupMenu_1 = new JPopupMenu();
			addPopup(txtaSendContent, popupMenu_1);

			final JMenuItem newItemMenuItem = new JMenuItem();
			newItemMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					if (!txtaSendContent.getText().trim().isEmpty()) {
						// txtaSendContent.setText(Utils
						// .trimLinesStartByFirstLine(txtaSendContent
						// .getText()));
					}
				}
			});
			newItemMenuItem.setText("Trim Lines Start");
			popupMenu_1.add(newItemMenuItem);

			popupMenu_1.addSeparator();

			final JMenuItem newItemMenuItem_2 = new JMenuItem();
			newItemMenuItem_2.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {
					if (!txtaSendContent.getText().trim().isEmpty()) {
						// new
						// ChatRecordMgr(txtaSendContent.getText()).saveMemo();
						// btnSend.doClick();
					}
				}
			});
			newItemMenuItem_2.setText("Save As Memo");
			popupMenu_1.add(newItemMenuItem_2);

			txtaSendContent.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyPressed(java.awt.event.KeyEvent e) {
					if (rbtnEnter.isSelected()) {
						if (!e.isControlDown()
								&& e.getKeyCode() == KeyEvent.VK_ENTER) {
							btnSend.doClick();
							e.consume();
						}
					} else {
						if (e.isControlDown()
								&& e.getKeyCode() == KeyEvent.VK_ENTER) {
							btnSend.doClick();
							e.consume();
						}
					}

					// 备忘记录信息保存
					if (e.isAltDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
						if (!txtaSendContent.getText().isEmpty()) {

							btnSend.doClick();
						}
						e.consume();
					}
					if (e.getKeyCode() == KeyEvent.VK_F1) {

					} else if (e.getKeyCode() == KeyEvent.VK_F2) {

					} else if (e.getKeyCode() == KeyEvent.VK_F3) {

					} else if (e.getKeyCode() == KeyEvent.VK_F4) {

					}
				}
			});
			txtaSendContent.addMouseWheelListener(new MouseWheelListener() {

				@Override
				public void mouseWheelMoved(MouseWheelEvent e) {
					if (e.isControlDown()) {
						e.consume();
						Font dFont = txtaSendContent.getFont();
						int size = dFont.getSize();
						size -= e.getUnitsToScroll();
						Font font = new Font(dFont.getFontName(), dFont
								.getStyle(), size);
						txtaSendContent.setFont(font);
					}
				}
			});
		}
		return txtaSendContent;
	}

	@Override
	public String getPageName() {
		return LanguageLoader.getString("Kbs.manager_title");
	}

	@Override
	public void disposePage() {
		super.disposePage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.javacoo.cowswing.core.event.CowSwingListener#update(org.javacoo.cowswing
	 * .core.event.CowSwingEvent)
	 */
	@Override
	public void update(CowSwingEvent event) {
		if (CowSwingEventType.NetPersonMsg.equals(event.getEventType())
				|| CowSwingEventType.NetCommonMsg.equals(event.getEventType())) {
			appendChatMsg(event.getEventObject().toString());
		}else if(CowSwingEventType.NetDownLoadIng.equals(event.getEventType())
				|| CowSwingEventType.NetSendIng.equals(event.getEventType())){
			getLblInfo().setText(event.getEventObject().toString());
		}else if(CowSwingEventType.NetPersonApplyVideo.equals(event.getEventType())){
			applyVideoParam = (String[])event.getEventObject();
			//空闲状态才接受视频请求
			if(VIDEO_STATE_0.equals(videoState)){
				appendChatMsg("收到【"+applyVideoParam[2]+"】的视频请求,请在10秒内点击左侧 【同意】 或者 【拒绝】按钮\n");
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						kbsChatFrame.getNetUserTreePanel().getVideoButton().setEnabled(false);
						kbsChatFrame.getNetUserTreePanel().getAgreeButton().setEnabled(true);
						kbsChatFrame.getNetUserTreePanel().getRefuseButton().setEnabled(true);
					}
				});
				
				Timer noticeShowTimer = new Timer();
				// 开启定时器任务,NOTICE_WIN_SHOW_TIME秒后关闭
				noticeShowTimer.schedule(new TimerTask() {
					@Override
					public void run() {
						if(AGREE_VIDEO_STATE_0.equals(isAgreeVideo)){
							appendChatMsg("你未做任何操作,"+applyVideoParam[2]+"视频请求失败\n");;
							noResponse();
						}
					}
				}, 1000*10);
			}else{
				isBusy();
			}
		}else if(CowSwingEventType.NetPersonNoResponseVideo.equals(event.getEventType())){
			appendChatMsg("对方未做任何操作,视频请求失败\n");
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					kbsChatFrame.getNetUserTreePanel().getVideoButton().setEnabled(true);
				}
			});
			videoState = VIDEO_STATE_0;
		}else if(CowSwingEventType.NetPersonBusyVideo.equals(event.getEventType())){
			appendChatMsg("对方正在视频对话中,请稍后再试\n");
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					kbsChatFrame.getNetUserTreePanel().getVideoButton().setEnabled(true);
				}
			});
			videoState = VIDEO_STATE_0;
		}else if(CowSwingEventType.NetPersonRefuseVideo.equals(event.getEventType())){
			appendChatMsg("对方拒绝了您的视频请求\n");
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					kbsChatFrame.getNetUserTreePanel().getVideoButton().setEnabled(true);
				}
			});
			videoState = VIDEO_STATE_0;
		}else if(CowSwingEventType.NetPersonCancelVideo.equals(event.getEventType())){
			appendChatMsg("对方断开了视频链接\n");
			closeVideo();
		}else if(CowSwingEventType.NetPersonAgreeVideo.equals(event.getEventType())){
			appendChatMsg("对方同意了您的视频请求,正在开启视频设备,请稍候...\n");
			//对方同意后，也要将自己的视频信息发送给对方
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					kbsSingleVideoFrame.init();
					kbsSingleVideoFrame.getVideoPanel().startVideo(selectNetClientBean.getIp());
					videoState = VIDEO_STATE_2;
					kbsChatFrame.getNetUserTreePanel().getCancelButton().setEnabled(true);
				}
			});
		}else if(CowSwingEventType.NetMultiVideoIn.equals(event.getEventType())){
			MsgBean msg = (MsgBean)event.getEventObject();
			appendChatMsg("【"+msg.getParams()[0]+"】进入了多人视频...\n");
		}else if(CowSwingEventType.NetMultiVideoOut.equals(event.getEventType())){
			MsgBean msg = (MsgBean)event.getEventObject();
			appendChatMsg("【"+msg.getParams()[0]+"】离开了多人视频...\n");
		}else if(CowSwingEventType.NetChatDownLoadReturnReceiveingEvent.equals(event.getEventType())){
			appendChatMsg("对方接收文件中,请稍候再尝试...\n\n");
		}else if(CowSwingEventType.NetChatDownLoadReturnSendingEvent.equals(event.getEventType())){
			appendChatMsg("对方发送文件中,请稍候再尝试...\n\n");
		}else if(CowSwingEventType.NetChatFileReceiveingReqEvent.equals(event.getEventType())){
			MsgBean action = (MsgBean)event.getEventObject();
			String msg = "【" + action.getParams()[0]
					+ "】 发来文件【" + action.getParams()[1]
					+ "】请接收.\n";
			appendChatMsg(msg);
			getBtnRecv().setVisible(true);
			receiveChatFolderTransmit = new ChatFolderTransmit(action.getSocket(),this);
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event
	 * .ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub

	}

	protected Component getBottomPane() {
		return null;
	}

	public JButton getBtnClear() {
		if (btnClear == null) {
			btnClear = new JButton();
			btnClear.setBounds(new Rectangle(10, 390, 60, 25));
			btnClear.setText("清空");
			btnClear.setIcon(ImageLoader.getImageIcon("CrawlerResource.kbs_chat_msg_delete"));
			btnClear.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					txtaSendContent.setText("");
					txtaSendContent.setEditable(true);
					txtaSendContent.setFocusable(true);
					txtaSendContent.requestFocus();
				}
			});
		}
		return btnClear;
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger())
					showMenu(e);
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger())
					showMenu(e);
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
				e.consume();
			}
		});
	}

	/**
	 * @return the selectNetClientBean
	 */
	public NetClientBean getSelectNetClientBean() {
		return selectNetClientBean;
	}

	/**
	 * @param selectNetClientBean
	 *            the selectNetClientBean to set
	 */
	public void setSelectNetClientBean(NetClientBean selectNetClientBean) {
		this.selectNetClientBean = selectNetClientBean;

		scrollPaneSend.setBorder(BorderFactory.createTitledBorder(border,
				"发送消息---------到---------[" + selectNetClientBean.toString()
						+ "]"));
		if (StringUtils.isNotBlank(this.selectNetClientBean.getIp())) {
			if (client != null) {
				client.stop();
				client = null;
			}
			if (this.selectNetClientBean.equals(selectNetClientBean)) {
				btnFolder.setEnabled(true);
				client = new Client(selectNetClientBean.getIp(),
						selectNetClientBean.getPort());
				client.connect();
			} else {
				btnFolder.setEnabled(false);
				// txtaChatContent.append("此时发送消息只限自己查看(可以保存在聊天记录中)!\n\n");
				appendChatMsg("此时发送消息只限自己查看(可以保存在聊天记录中)!\n");
			}
		} else {
			btnFolder.setEnabled(false);
			if (client != null) {
				client.stop();
				client = null;
			}
		}
		txtaSendContent.setFocusable(true);
		txtaSendContent.requestFocus();
		isFirstTime = false;
	}

	/**
	 * @return the netManager
	 */
	public NetManager getNetManager() {
		return netManager;
	}
	/**
	 * 视频申请
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-1-8 下午3:45:37
	 * @version 1.0
	 * @exception
	 */
	public void applyVideo(){
		if (client != null) {
			videoState = VIDEO_STATE_1;
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					kbsChatFrame.getNetUserTreePanel().getVideoButton().setEnabled(false);
				}
			});
			client.sendMsg(new MsgBean(
					CoreConstant.NET_ACTION_TYPE_PERSON_APPLY_VIDEO,netManager.getNetClientBean().getIp(),String.valueOf(netManager.getNetClientBean().getPort()),netManager.getNetClientBean().getUserName()));
		}
	}
	/**
	 * 取消视频
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-1-10 上午10:31:57
	 * @version 1.0
	 * @exception
	 */
	public void cancelVideo(){
		if(null != applyVideoParam){
			netManager.sendClientData(applyVideoParam[0], Integer.valueOf(applyVideoParam[1]), new MsgBean(CoreConstant.NET_ACTION_TYPE_PERSON_CANCEL_VIDEO));
		}else{
			netManager.sendClientData(selectNetClientBean.getIp(), selectNetClientBean.getPort(), new MsgBean(CoreConstant.NET_ACTION_TYPE_PERSON_CANCEL_VIDEO));
		}
		closeVideo();
	}
	/**
	 * 关闭视频
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-1-10 下午4:48:36
	 * @version 1.0
	 * @exception
	 */
	private void closeVideo(){
		applyVideoParam = null;
		isAgreeVideo = AGREE_VIDEO_STATE_2;
		kbsSingleVideoFrame.getVideoPanel().cancel();
		kbsSingleVideoFrame.setVisible(false);
		videoState = VIDEO_STATE_0;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				kbsChatFrame.getNetUserTreePanel().getVideoButton().setEnabled(true);
				kbsChatFrame.getNetUserTreePanel().getAgreeButton().setEnabled(false);
				kbsChatFrame.getNetUserTreePanel().getRefuseButton().setEnabled(false);
				kbsChatFrame.getNetUserTreePanel().getCancelButton().setEnabled(false);
			}
		});
	}
	/**
	 * 同意视频
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-1-14 上午10:21:04
	 * @version 1.0
	 * @exception
	 */
	public void agreeVideo(){
		isAgreeVideo = AGREE_VIDEO_STATE_1;
		videoState = VIDEO_STATE_2;
		netManager.sendClientData(applyVideoParam[0], Integer.valueOf(applyVideoParam[1]), new MsgBean(CoreConstant.NET_ACTION_TYPE_PERSON_AGREE_VIDEO));
		kbsSingleVideoFrame.init();
		kbsSingleVideoFrame.getVideoPanel().startVideo(applyVideoParam[0]);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				kbsChatFrame.getNetUserTreePanel().getAgreeButton().setEnabled(false);
				kbsChatFrame.getNetUserTreePanel().getRefuseButton().setEnabled(false);
				kbsChatFrame.getNetUserTreePanel().getCancelButton().setEnabled(true);
			}
		});
	}
	public void refuseVideo(){
		isAgreeVideo = AGREE_VIDEO_STATE_2;
		videoState = VIDEO_STATE_0;
		netManager.sendClientData(applyVideoParam[0], Integer.valueOf(applyVideoParam[1]), new MsgBean(CoreConstant.NET_ACTION_TYPE_PERSON_REFUSE_VIDEO));
	}
	/**
	 * 未响应
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-1-10 上午11:16:35
	 * @version 1.0
	 * @exception
	 */
	private void noResponse(){
		videoState = VIDEO_STATE_0;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				kbsChatFrame.getNetUserTreePanel().getVideoButton().setEnabled(true);
				kbsChatFrame.getNetUserTreePanel().getAgreeButton().setEnabled(false);
				kbsChatFrame.getNetUserTreePanel().getRefuseButton().setEnabled(false);
			}
		});
		netManager.sendClientData(applyVideoParam[0], Integer.valueOf(applyVideoParam[1]), new MsgBean(CoreConstant.NET_ACTION_TYPE_PERSON_NO_RESPONSE_VIDEO));
		applyVideoParam = null;
	}
	/**
	 * 视频中
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-1-10 下午2:26:54
	 * @version 1.0
	 * @exception
	 */
	private void isBusy(){
		netManager.sendClientData(applyVideoParam[0], Integer.valueOf(applyVideoParam[1]), new MsgBean(CoreConstant.NET_ACTION_TYPE_PERSON_BUSY_VIDEO));
		applyVideoParam = null;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				kbsChatFrame.getNetUserTreePanel().getVideoButton().setEnabled(true);
			}
		});
		videoState = VIDEO_STATE_0;
	}
	

}
