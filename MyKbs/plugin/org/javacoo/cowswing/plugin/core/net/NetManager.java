/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.core.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;
import org.javacoo.cowswing.base.constant.Config;
import org.javacoo.cowswing.base.utils.DateUtil;
import org.javacoo.cowswing.core.event.CowSwingEvent;
import org.javacoo.cowswing.core.event.CowSwingListener;
import org.javacoo.cowswing.core.event.CowSwingObserver;
import org.javacoo.cowswing.core.event.type.CowSwingEventType;
import org.javacoo.cowswing.plugin.core.constant.CoreConstant;
import org.javacoo.cowswing.plugin.core.service.beans.NetClientBean;
import org.javacoo.cowswing.plugin.core.service.beans.NetSettingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * 网路管理
 * <p>
 * 说明:
 * </p>
 * <li></li>
 * 
 * @author DuanYong
 * @since 2013-8-12 下午4:08:17
 * @version 1.0
 */
@Component("netManager")
public class NetManager implements INetManager, CowSwingListener {
	protected Logger logger = Logger.getLogger(this.getClass());
	// 使用常量作为本程序的多点广播IP地址
	private static final String BROADCAST_IP = "230.0.0.1";
	// 使用常量作为本程序的多点广播目的的端口
	// DatagramSocket所用的的端口为该端口-1。
	public static final int BROADCAST_PORT = 30000;
	// 休眠时间
	private static final int SLEEP_TIME = 1000;
	private GroupMsgHandler commonMsgHandler = null;
	private List<GroupMsgHandler> listMsgHandler = new CopyOnWriteArrayList<GroupMsgHandler>();
	// 是否分享
	private boolean share = true;
	// 是否接收
	private boolean receive = true;
	public static boolean isSending = false;
	public static boolean isRecving = false;
	// 是否接收
	private boolean conn = true;
	/** 网络数据集合 */
	private List<MsgBean> netDataList = new CopyOnWriteArrayList<MsgBean>();
	/** 网络搜索数据集合 */
	private List<MsgBean> netSearchDataList = new CopyOnWriteArrayList<MsgBean>();
	/** 服务端 */
	private Server server;
	private NetClientBean netClientBean = null;
	private static final String DEFAULT_MUL_IP = "228.5.6.7";
	private static final int DEFAULT_MULPORT = 52254;
	private NetSettingBean netSettingBean;
	/**网络用户总数*/
	private int netUserCount = 0;
	/**网络用户集合*/
	private List<NetClientBean> netUserList = new CopyOnWriteArrayList<NetClientBean>();
	/**版本*/
	private double version = Double.parseDouble(Config.COWSWING_CONFIG_MAP.get(Config.COWSWING_CONFIG_KEY_VERSION).get(Config.COWSWING_CONFIG_KEY_VERSION_VERSION));
	
	/**
	 * 初始化
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-12 下午4:22:43
	 * @version 1.0
	 * @exception
	 */
	public void init(){
		try {
			startCommonListen(getCommonMsgHandler());
			connectionNetwork();
			CowSwingObserver.getInstance().addCrawlerListener(this);
			//new Thread(new HeartbeatListener(this,5000)).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取得公共消息处理器
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @since 2013-8-20 上午9:48:55
	 * @version 1.0
	 * @exception
	 * @return
	 */
	public GroupMsgHandler getCommonMsgHandler() {
		if (commonMsgHandler == null) {
			try {
				commonMsgHandler = new GroupMsgHandler(BROADCAST_IP,
						BROADCAST_PORT);
			} catch (IOException e) {
				e.printStackTrace();
				commonMsgHandler = null;
			}
		}
		return commonMsgHandler;
	}

	/**
	 * 开启公共消息监控
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @since 2013-8-20 上午9:49:18
	 * @version 1.0
	 * @exception
	 * @param commMsgHandler
	 */
	public void startCommonListen(final GroupMsgHandler commMsgHandler) {
		if (commMsgHandler != null) {
			Runnable recThread = new Runnable() {
				@Override
				public void run() {
					MsgBean msgBean = null;
					while (receive) {
						msgBean = receive(commMsgHandler);
						if (null != msgBean) {
							invoke(msgBean);
						}
						try {
							Thread.sleep(SLEEP_TIME);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			};
			new Thread(recThread).start();
		}
	}

	/**
	 * 发送数据
	 * <p>
	 * 方法说明:</>
	 * <li>
	 * 发送公共数据</li>
	 * 
	 * @author DuanYong
	 * @since 2013-8-14 上午8:43:02
	 * @version 1.0
	 * @exception
	 */
	public void sendCommonData(MsgBean msgBean) {
		if(share){
			commonMsgHandler.sendMsg(msgBean);
		}
	}
	/**
	 * 向所有播组发送数据
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-3 上午11:52:19
	 * @version 1.0
	 * @exception 
	 * @param msgBean
	 */
	public void sendGroupMsgToAll(MsgBean msgBean) {
		if (!CollectionUtils.isEmpty(listMsgHandler) && share
				&& null != msgBean) {
			for (GroupMsgHandler commMsgHandler : listMsgHandler) {
				if (commMsgHandler != null) {
					final GroupMsgHandler msgHandler = commMsgHandler;
					final MsgBean sendMsg = msgBean;
					Runnable sendThread = new Runnable() {
						@Override
						public void run() {
							msgHandler.sendMsg(sendMsg);
							logger.info("向所有播组发送数据:" + sendMsg);
						}
					};
					new Thread(sendThread).start();
				}
			}
		}
    }

	

	/**
	 * 接收公共数据
	 */
	public void receiveCommonData() {

	}

	/**
	 * 发送单点数据
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @since 2013-8-20 下午2:02:16
	 * @version 1.0
	 * @exception
	 * @param msgBean
	 */
	public void sendClientData(String ip, int port, MsgBean msgBean) {
		if(!this.server.getIp().equals(ip)
				|| this.server.getPort() != port) {
			Client sendClient = new Client(ip, port);
			sendClient.connect();
			sendClient.sendMsg(msgBean);
			if (sendClient != null) {
				sendClient.stop();
				sendClient = null;
			}
		}
	}

	/**
	 * 根据不同ACTION执行相应的请求
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @since 2013-8-17 上午11:07:46
	 * @version 1.0
	 * @exception
	 * @param msgBean
	 */
	private void invoke(MsgBean msgBean) {
		if (CoreConstant.NET_ACTION_TYPE_ADD.equals(msgBean.getAction())) {
			if (!server.getIp().equals(msgBean.getIp())
					|| server.getPort() != msgBean.getPort()) {
				netDataList.add(msgBean);
				logger.info("成功添加数据:" + msgBean.getTitle());
				CowSwingObserver.getInstance().notifyEvents(
						new CowSwingEvent(this,
								CowSwingEventType.NetDataChangeEvent));
			}
		} else if (CoreConstant.NET_ACTION_TYPE_DEL.equals(msgBean.getAction())) {
			netDataList.remove(msgBean);
			logger.info("成功删除数据:" + msgBean.getTitle());
			CowSwingObserver.getInstance().notifyEvents(
					new CowSwingEvent(this,
							CowSwingEventType.NetDataChangeEvent));
		} else if (CoreConstant.NET_ACTION_TYPE_SEARCH.equals(msgBean
				.getAction())) {
			if (!server.getIp().equals(msgBean.getIp())
					|| server.getPort() != msgBean.getPort()) {
				logger.info("开始搜索数据:" + msgBean.getTitle());
				CowSwingObserver.getInstance().notifyEvents(
							new CowSwingEvent(this,
									CowSwingEventType.NetSearceEvent, msgBean));
			}
		} else if (CoreConstant.NET_ACTION_TYPE_ONLINE.equals(msgBean
				.getAction())) {
			logger.info("上线:" + msgBean.getAuthor());
			StringBuilder msg = new StringBuilder();
            msg.append("上线提示:").append(msgBean.getAuthor()).append("\n");
            msg.append("来自:").append(msgBean.getIp()).append(":").append(msgBean.getPort()).append("\n");
            msg.append("时间:").append(DateUtil.dateToStr(DateUtil.getNow(),"yyyy-MM-dd HH:mm:ss")).append("\n");
            //添加网络用户到集合 
            addNetUserToList(new NetClientBean(msgBean.getIp(), msgBean.getPort(),msgBean.getAuthor()));
            //收到上线通知的同时，也要告诉对方我也在线
            MsgBean onlineRebackBean = new MsgBean(CoreConstant.NET_ACTION_TYPE_ONLINEBCAK,getNetClientBean().getIp(),String.valueOf(getNetClientBean().getPort()),getNetClientBean().getUserName());
            onlineRebackBean.setIp(getNetClientBean().getIp());
            onlineRebackBean.setPort(getNetClientBean().getPort());
            onlineRebackBean.setVersion(version);
            sendClientData(msgBean.getIp(),msgBean.getPort(),onlineRebackBean);
            logger.info("当前在线人数:" + getNetUserCount());
            CowSwingObserver.getInstance().notifyEvents(
					new CowSwingEvent(this,
							CowSwingEventType.NetUserOnlineEvent, msg));
            CowSwingObserver.getInstance().notifyEvents(
					new CowSwingEvent(this,
							CowSwingEventType.NetUserOnlineInfoEvent, msgBean));
            
		} else if (CoreConstant.NET_ACTION_TYPE_OUTLINE.equals(msgBean
				.getAction())) {
			logger.info("下线:" + msgBean.getAuthor());
			StringBuilder msg = new StringBuilder();
			msg.append("下线提示:").append(msgBean.getAuthor()).append("\n");
            msg.append("来自:").append(msgBean.getIp()).append(":").append(msgBean.getPort()).append("\n");
            msg.append("时间:").append(DateUtil.dateToStr(DateUtil.getNow(),"yyyy-MM-dd HH:mm:ss")).append("\n");
            //删除网络用户到集合 
            getNetUserList().remove(new NetClientBean(msgBean.getIp(), msgBean.getPort(),msgBean.getAuthor()));
            logger.info("当前在线人数:" + getNetUserCount());
            CowSwingObserver.getInstance().notifyEvents(
					new CowSwingEvent(this,
							CowSwingEventType.NetUserOutlineEvent, msg));
		} else if (CoreConstant.NET_ACTION_TYPE_DOWNLOAD.equals(msgBean
				.getAction())) {
			logger.info("开始搜索数据:" + msgBean.getTitle());
		}else if (CoreConstant.NET_ACTION_TYPE_LISTEN.equals(msgBean.getAction())) {
			logger.info("收到监听请求,发送本机IP,本机端口,播组IP,播组端口,网络用户名");
			commonMsgHandler.sendMsg(new MsgBean(CoreConstant.NET_ACTION_TYPE_LISTENRETURN,getNetClientBean().getIp(),String.valueOf(getNetClientBean().getPort()),getNetSettingBean().getIp(),String.valueOf(getNetSettingBean().getPort()),getNetSettingBean().getUserName()));
		}else if (CoreConstant.NET_ACTION_TYPE_LISTENRETURN.equals(msgBean.getAction())) {
			logger.info("收到监听请求返回:" + msgBean.toString());
			logger.info("本机客户端信息:" + getNetClientBean().toString());
			if(!getNetClientBean().getIp().equals(msgBean.getParams()[0]) || getNetClientBean().getPort() != Integer.valueOf(msgBean.getParams()[1])){
				NetSettingBean tempNetSettingBean = new NetSettingBean(msgBean.getParams()[2],Integer.valueOf(msgBean.getParams()[3]),msgBean.getParams()[4]);
				if(!getNetSettingBean().getListMulIpAndPort().contains(tempNetSettingBean)){
					logger.info("添加监听返回信息:" + msgBean.toString());
					getNetSettingBean().getListMulIpAndPort().add(tempNetSettingBean);
					CowSwingObserver.getInstance().notifyEvents(
							new CowSwingEvent(this,
									CowSwingEventType.NetListenReturn));
				}
			}
		}else if (CoreConstant.NET_ACTION_TYPE_MULTI_VIDEO_IN.equals(msgBean.getAction())) {
			logger.info("收到多人视频进入信息:" + msgBean.toString());
			CowSwingObserver.getInstance().notifyEvents(
					new CowSwingEvent(this,
							CowSwingEventType.NetMultiVideoIn,msgBean));
		}else if (CoreConstant.NET_ACTION_TYPE_MULTI_VIDEO_OUT.equals(msgBean.getAction())) {
			logger.info("收到多人视频退出信息:" + msgBean.toString());
			CowSwingObserver.getInstance().notifyEvents(
					new CowSwingEvent(this,
							CowSwingEventType.NetMultiVideoOut,msgBean));
		}

	}
	

	/**
	 * 取得网络数据
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @since 2013-8-16 下午8:18:53
	 * @version 1.0
	 * @exception
	 * @return
	 */
	public List<MsgBean> getNetDataList() {
		return netDataList;
	}

	/**
	 * 取得搜索数据
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @since 2013-8-19 下午7:50:51
	 * @version 1.0
	 * @exception
	 * @return
	 */
	public List<MsgBean> getSearchNetDataList() {
		return netSearchDataList;
	}

	/**
	 * 接收数据
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @since 2013-8-15 下午9:05:39
	 * @version 1.0
	 * @exception
	 * @param commMsgHandler
	 * @return
	 */
	private MsgBean receive(GroupMsgHandler commMsgHandler) {
		return commMsgHandler.receiveMsg();
	}

	/**
	 * 建立连接
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-8-17 下午4:22:01
	 * @version 1.0
	 * @exception
	 * @throws IOException
	 */
	public void connectionNetwork() throws IOException {
		server = new Server(netSearchDataList, this);
		netClientBean = server.listen();
		netClientBean.setUserName(getNetSettingBean().getUserName());
		if (!getNetSettingBean().getListListenMulIpAndPort().contains(getNetSettingBean())) {
			getNetSettingBean().getListListenMulIpAndPort().add(0, getNetSettingBean());
		} else {
			getNetSettingBean().getListListenMulIpAndPort().remove(getNetSettingBean());
			getNetSettingBean().getListListenMulIpAndPort().add(0, getNetSettingBean());
		}
		conn = true;
		if(!CollectionUtils.isEmpty(getNetSettingBean().getListListenMulIpAndPort())){
			for (NetSettingBean netSettingBean : getNetSettingBean().getListListenMulIpAndPort()) {
				 GroupMsgHandler msgHandler = createGroupMsgHandler(netSettingBean.getIp(), netSettingBean.getPort());
				 listMsgHandler.add(msgHandler);
			 }	
		}
		onLine();
	}
	/**
	 * 创建群组
	 * <p>方法说明:</>
	 * <li>开启线程，监听</li>
	 * @author DuanYong
	 * @since 2013-9-17 上午10:38:02
	 * @version 1.0
	 * @exception 
	 * @param ip
	 * @param port
	 * @return
	 * @throws IOException
	 */
	private GroupMsgHandler createGroupMsgHandler(String ip,
            int port) throws IOException {
        final GroupMsgHandler msgHandler = new GroupMsgHandler(ip,
                port);
        Runnable recThread = new Runnable() {
            @Override
            public void run() {
                while (conn) {
                	MsgBean recMsg = msgHandler.receiveMsg();
                	logger.info("收到组内消息："+recMsg.toString());
                	if(CoreConstant.NET_ACTION_TYPE_COMMONMSG.equals(recMsg.getAction())){
                		logger.info("收到组内公共消息："+recMsg.getParams()[0]);
                		CowSwingObserver.getInstance().notifyEvents(
								new CowSwingEvent(this,
										CowSwingEventType.NetCommonMsg,recMsg.getParams()[0]));
                	}else if(CoreConstant.NET_ACTION_TYPE_REFRESH.equals(recMsg.getAction())){
                		MsgBean msgBean = new MsgBean(CoreConstant.NET_ACTION_TYPE_REBACK,getNetClientBean().getIp(),String.valueOf(getNetClientBean().getPort()),getNetClientBean().getUserName());
                		msgBean.setIp(getNetClientBean().getIp());
                		msgBean.setPort(getNetClientBean().getPort());
                		msgBean.setVersion(version);
                		msgHandler.sendMsg(msgBean);
                	}else if(CoreConstant.NET_ACTION_TYPE_REBACK.equals(recMsg.getAction())){
						logger.info("收到刷新返回信息:"+recMsg.toString());
						CowSwingObserver.getInstance().notifyEvents(
								new CowSwingEvent(this,
										CowSwingEventType.NetRefresh,recMsg));
					}
                }
            }
        };
        new Thread(recThread).start();
        return msgHandler;
    }

	/**
	 * 上线
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @since 2013-8-20 上午10:01:49
	 * @version 1.0
	 * @exception
	 */
	private void onLine() {
		if(null != server){
			sendCommonData(populateOnOurLineMsg(CoreConstant.NET_ACTION_TYPE_ONLINE));
		}
	}

	/**
	 * 下线
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @since 2013-8-20 上午10:01:57
	 * @version 1.0
	 * @exception
	 */
	private void outLine() {
        if(null != server){
    		sendCommonData(populateOnOurLineMsg(CoreConstant.NET_ACTION_TYPE_OUTLINE));
        }
		
	}

	/**
	 * 组装上线下线消息
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @since 2013-8-20 上午10:01:32
	 * @version 1.0
	 * @exception
	 * @param action
	 * @return
	 */
	private MsgBean populateOnOurLineMsg(String action) {
		MsgBean msgBean = new MsgBean();
		msgBean.setAuthor(server.getUserName());
		msgBean.setAction(action);
		msgBean.setPort(server.getPort());
		msgBean.setIp(server.getIp());
		msgBean.setVersion(version);
		return msgBean;
	}
    /**
     * 断开连接
     * <p>方法说明:</>
     * <li></li>
     * @author DuanYong
     * @since 2013-9-3 上午11:03:28
     * @version 1.0
     * @exception
     */
	public void disconnectNetwork() {
		outLine();
		conn = false;
		for (GroupMsgHandler handler : listMsgHandler) {
			handler.leaveGroup();
			handler = null;
		}
		listMsgHandler.clear();
		getNetSettingBean().getListListenMulIpAndPort().remove(getNetSettingBean());
		server.stop();
		server = null;
	}

	/**
	 * @return the server
	 */
	public Server getServer() {
		return server;
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
		if (event.getEventType()
				.equals(CowSwingEventType.SystemExitChangeEvent)) {
			outLine();
		}
	}

	/**
	 * @return the netSettingBean
	 */
	public NetSettingBean getNetSettingBean() {
		if(null == this.netSettingBean){
			try {
				this.netSettingBean = new NetSettingBean(DEFAULT_MUL_IP,DEFAULT_MULPORT,InetAddress.getLocalHost().getHostName());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		return this.netSettingBean;
	}

	/**
	 * @param netSettingBean the netSettingBean to set
	 */
	public void setNetSettingBean(NetSettingBean netSettingBean) {
		this.netSettingBean = netSettingBean;
	}

	/**
	 * @return the netClientBean
	 */
	public NetClientBean getNetClientBean() {
		return netClientBean;
	}

	/**
	 * @param netClientBean the netClientBean to set
	 */
	public void setNetClientBean(NetClientBean netClientBean) {
		this.netClientBean = netClientBean;
	}

	/**
	 * @return the netUserCount
	 */
	public int getNetUserCount() {
		netUserCount = netUserList.size();
		return netUserCount;
	}

	/**
	 * @return the netUserList
	 */
	public List<NetClientBean> getNetUserList() {
		return netUserList;
	}
	
	public void addNetUserToList(NetClientBean netClientBean){
		if(!netUserList.contains(netClientBean)){
			netUserList.add(netClientBean);
		}
	}

	/**
	 * @param netUserList the netUserList to set
	 */
	public void setNetUserList(List<NetClientBean> netUserList) {
		this.netUserList = netUserList;
	}

	/**
	 * @return the netSearchDataList
	 */
	public List<MsgBean> getNetSearchDataList() {
		return netSearchDataList;
	}
	
	
    
}
