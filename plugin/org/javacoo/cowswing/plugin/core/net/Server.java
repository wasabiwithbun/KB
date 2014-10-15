/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.core.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.javacoo.cowswing.base.constant.Constant;
import org.javacoo.cowswing.core.event.CowSwingEvent;
import org.javacoo.cowswing.core.event.CowSwingListener;
import org.javacoo.cowswing.core.event.CowSwingObserver;
import org.javacoo.cowswing.core.event.type.CowSwingEventType;
import org.javacoo.cowswing.plugin.core.constant.CoreConstant;
import org.javacoo.cowswing.plugin.core.service.beans.NetClientBean;

/**
 * 服务端
 * <p>
 * 说明:
 * </p>
 * <li></li>
 * 
 * @author DuanYong
 * @since 2013-8-17 下午3:59:10
 * @version 1.0
 */
public class Server implements CowSwingListener {
	protected Logger logger = Logger.getLogger(this.getClass());
	private int port;
	private String ip;
	private ServerSocket serverSocket;
	private List<MsgBean> netSearchDataList;
	private NetManager netManager;
	private boolean flag = true;
	private FolderTransmit folderTransmit = null;
	private FolderTransmit sendFolderTransmit = null;

	/**
	 * @param netSearchDataList
	 */
	public Server(List<MsgBean> netSearchDataList, NetManager netManager) {
		super();
		this.netSearchDataList = netSearchDataList;
		this.netManager = netManager;
		CowSwingObserver.getInstance().addCrawlerListener(this);
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public NetClientBean listen() {
		Runnable listener = new Runnable() {
			@Override
			public void run() {
				Random random = new Random();
				while (true) {
					try {
						port = random.nextInt(65535);
						serverSocket = new ServerSocket(port);
						break;
					} catch (IOException e) {
					}
				}
				NetUserClient userClient = null;
				Socket socket = null;
				while (flag) {
					logger.info("客户端监听服务运行中....");
					try {
						socket = serverSocket.accept();
						userClient = new NetUserClient(socket);
						MsgBean action = userClient.receiveMsg();
						if (CoreConstant.NET_ACTION_TYPE_DOWNLOAD_RETURN
								.equals(action.getAction())) {
							logger.info("收到文件发送请求");
							String folderPath = action.getParams()[0];
							logger.info("收到文件发送请求" + folderPath);
							folderTransmit = new FolderTransmit(socket,
									netManager);
							receiveFolder(folderPath);
						} else if (CoreConstant.NET_ACTION_TYPE_CHAT_DOWNLOAD_RETURN
								.equals(action.getAction())) {
							if (NetManager.isRecving) {
								CowSwingObserver
								.getInstance()
								.notifyEvents(
										new CowSwingEvent(
												this,
												CowSwingEventType.NetDataChangeEvent));
								CowSwingObserver
								.getInstance()
								.notifyEvents(
										new CowSwingEvent(
												this,
												CowSwingEventType.NetChatDownLoadReturnReceiveingEvent));
								continue;
							}
							if (NetManager.isSending) {
								CowSwingObserver
								.getInstance()
								.notifyEvents(
										new CowSwingEvent(
												this,
												CowSwingEventType.NetChatDownLoadReturnSendingEvent));
								continue;
							}
							logger.info("收到聊天文件接收请求");
							action.setSocket(socket);
							CowSwingObserver
							.getInstance()
							.notifyEvents(
									new CowSwingEvent(
											this,
											CowSwingEventType.NetChatFileReceiveingReqEvent,action));
						} else {
							new ReceiveMsg(userClient).start();
						}
					} catch (IOException e) {
						logger.info("客户端监听服务关闭....");
						//e.printStackTrace();
						break;
					}
				}
			}
		};
		new Thread(listener).start();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException ee) {
			ee.printStackTrace();
		}
		return new NetClientBean(getIp(), getPort());
	}

	public int getPort() {
		return port;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	public String getUserName() {
		if (StringUtils.isNotBlank(netManager.getNetSettingBean()
				.getUserName())) {
			return netManager.getNetSettingBean().getUserName();
		}
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	

	private class ReceiveMsg extends Thread {
		private NetUserClient client;

		public ReceiveMsg(NetUserClient userClient) {
			this.client = userClient;
		}

		@Override
		public void run() {
			while (true) {
				try {
					MsgBean msgBean = client.receiveMsg();
					if (null != msgBean) {
						// 接收搜索返回数据
						if (CoreConstant.NET_ACTION_TYPE_SEARCH_RETURN
								.equals(msgBean.getAction())) {
							logger.info("接收搜索返回数据:" + msgBean.toString());
							netSearchDataList.clear();
							netSearchDataList.addAll(msgBean.getMsgBeanList());
							CowSwingObserver
									.getInstance()
									.notifyEvents(
											new CowSwingEvent(
													this,
													CowSwingEventType.NetDataChangeEvent));
						} else if (CoreConstant.NET_ACTION_TYPE_VIEW
								.equals(msgBean.getAction())) {
							logger.info("收到查看请求:" + msgBean.toString());
							CowSwingObserver.getInstance().notifyEvents(
									new CowSwingEvent(
											this,
											CowSwingEventType.NetViewEvent,
											msgBean));
						} else if (CoreConstant.NET_ACTION_TYPE_VIEW_RETURN
								.equals(msgBean.getAction())) {
							logger.info("接收查看返回数据:" + msgBean.toString());
							CowSwingObserver
									.getInstance()
									.notifyEvents(
											new CowSwingEvent(
													this,
													CowSwingEventType.NetViewReturnEvent,
													msgBean.getContent()));
						} else if (CoreConstant.NET_ACTION_TYPE_DOWNLOAD
								.equals(msgBean.getAction())) {
							logger.info("收到下载请求:" + msgBean.toString()+",路径="+msgBean.getFilePath());
							String filePath = msgBean.getFilePath();
							if(Constant.PACKAGE_UPDATE.equals(filePath)){
								filePath = Constant.UPDATE_DIR;
							}
							sendFolderTransmit = new FolderTransmit(
									msgBean.getSavePath(),
									filePath, msgBean.getIp(),
									msgBean.getPort(), netManager);
							sendFolderTransmit.send();
						} else if (CoreConstant.NET_ACTION_TYPE_PERSONMSG
								.equals(msgBean.getAction())) {
							logger.info("收到组内私聊信息:" + msgBean.toString());
							CowSwingObserver.getInstance().notifyEvents(
									new CowSwingEvent(this,
											CowSwingEventType.NetPersonMsg,
											msgBean.getParams()[0]));
						}else if (CoreConstant.NET_ACTION_TYPE_PERSON_APPLY_VIDEO
								.equals(msgBean.getAction())) {
							logger.info("收到组内个人视频申请信息:" + msgBean.toString());
							CowSwingObserver.getInstance().notifyEvents(
									new CowSwingEvent(this,
											CowSwingEventType.NetPersonApplyVideo,
											msgBean.getParams()));
						}else if (CoreConstant.NET_ACTION_TYPE_PERSON_AGREE_VIDEO
								.equals(msgBean.getAction())) {
							logger.info("收到组内个人视频同意信息:" + msgBean.toString());
							CowSwingObserver.getInstance().notifyEvents(
									new CowSwingEvent(this,
											CowSwingEventType.NetPersonAgreeVideo,
											msgBean.getParams()));
						}else if (CoreConstant.NET_ACTION_TYPE_PERSON_REFUSE_VIDEO
								.equals(msgBean.getAction())) {
							logger.info("收到组内个人视频拒绝信息:" + msgBean.toString());
							CowSwingObserver.getInstance().notifyEvents(
									new CowSwingEvent(this,
											CowSwingEventType.NetPersonRefuseVideo,
											msgBean.getParams()));
						}else if (CoreConstant.NET_ACTION_TYPE_PERSON_CANCEL_VIDEO
								.equals(msgBean.getAction())) {
							logger.info("收到组内个人视频断开信息:" + msgBean.toString());
							CowSwingObserver.getInstance().notifyEvents(
									new CowSwingEvent(this,
											CowSwingEventType.NetPersonCancelVideo,
											msgBean.getParams()));
						}else if (CoreConstant.NET_ACTION_TYPE_PERSON_NO_RESPONSE_VIDEO
								.equals(msgBean.getAction())) {
							logger.info("收到组内个人视频申请无响应信息:" + msgBean.toString());
							CowSwingObserver.getInstance().notifyEvents(
									new CowSwingEvent(this,
											CowSwingEventType.NetPersonNoResponseVideo,
											msgBean.getParams()));
						}else if (CoreConstant.NET_ACTION_TYPE_PERSON_BUSY_VIDEO
								.equals(msgBean.getAction())) {
							logger.info("收到组内个人视频申请忙碌信息:" + msgBean.toString());
							CowSwingObserver.getInstance().notifyEvents(
									new CowSwingEvent(this,
											CowSwingEventType.NetPersonBusyVideo,
											msgBean.getParams()));
						} else if (CoreConstant.NET_ACTION_TYPE_ONLINEBCAK
								.equals(msgBean.getAction())) {
							logger.info("收到上线反馈:" + msgBean.toString());
							netManager.addNetUserToList(new NetClientBean(
									msgBean.getParams()[0], Integer
											.valueOf(msgBean.getParams()[1]),
									msgBean.getParams()[2]));
						} else if (CoreConstant.NET_ACTION_ARE_YOU_STILL_ALIVE
								.equals(msgBean.getAction())) {
							netManager.sendClientData(msgBean.getParams()[0],
									Integer.valueOf(msgBean.getParams()[1]),
									new MsgBean(
											CoreConstant.NET_ACTION_I_AM_ALIVE));
						} else if (CoreConstant.NET_ACTION_I_AM_ALIVE
								.equals(msgBean.getAction())) {
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
					client = null;
					break;
				}
			}
		}

	}

	

	

	public void stop() {
		try {
			flag = false;
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void receiveFolder(String path) {
		folderTransmit.receive(path);
	}

//	public void receiveChatFolder(String path) {
//		chatFolderTransmit.receive(path);
//	}

	public void breakRecvFolder() {
		folderTransmit.breakRecvFolder();
	}

//	public void breakChatRecvFolder() {
//		chatFolderTransmit.breakRecvFolder();
//	}

	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.javacoo.cowswing.core.event.CowSwingListener#update(org.javacoo.cowswing
	 * .core.event.CowSwingEvent)
	 */
	@Override
	public void update(CowSwingEvent event) {
		// TODO Auto-generated method stub

	}

}
