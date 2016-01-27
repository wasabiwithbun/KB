/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.core.net;

import java.util.List;

import org.apache.log4j.Logger;
import org.javacoo.cowswing.base.utils.DateUtil;
import org.javacoo.cowswing.core.event.CowSwingEvent;
import org.javacoo.cowswing.core.event.CowSwingObserver;
import org.javacoo.cowswing.core.event.type.CowSwingEventType;
import org.javacoo.cowswing.plugin.core.constant.CoreConstant;
import org.javacoo.cowswing.plugin.core.service.beans.NetClientBean;
import org.springframework.util.CollectionUtils;

/**
 * 心跳监听
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-9-18 上午10:05:37
 * @version 1.0
 */
public class HeartbeatListener implements Runnable{
	protected Logger logger = Logger.getLogger(this.getClass());
    private NetManager netManager;
    private int scanTime = 5000; 
	public HeartbeatListener(NetManager netManager,int scanTime){
		this.netManager = netManager;
		this.scanTime = scanTime;
	}
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while(true){
			scan();
			try {
				Thread.sleep(scanTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private void scan(){
		List<NetClientBean> netUserList = this.netManager.getNetUserList();
		if(!CollectionUtils.isEmpty(netUserList)){
			for(NetClientBean netClientBean : netUserList){
				if (!this.netManager.getServer().getIp().equals(netClientBean.getIp())
						|| this.netManager.getServer().getPort() != netClientBean.getPort()) {
					try{
						logger.info("开始发送心跳监听："+netClientBean.toString());
						this.netManager.sendClientData(netClientBean.getIp(), netClientBean.getPort(), new MsgBean(CoreConstant.NET_ACTION_ARE_YOU_STILL_ALIVE,this.netManager.getNetClientBean().getIp(),String.valueOf(this.netManager.getNetClientBean().getPort())));
						logger.info("发送心跳监听完成");
					}catch(Exception e){
						logger.info("连接发生异常,此用户已经离线");
						StringBuilder msg = new StringBuilder();
						msg.append("下线提示:").append(netClientBean.getUserName()).append("\n");
			            msg.append("来自:").append(netClientBean.getIp()).append(":").append(netClientBean.getPort()).append("\n");
			            msg.append("时间:").append(DateUtil.dateToStr(DateUtil.getNow(),"yyyy-MM-dd HH:mm:ss")).append("\n");
						this.netManager.getNetUserList().remove(netClientBean);
						 CowSwingObserver.getInstance().notifyEvents(
									new CowSwingEvent(this,
											CowSwingEventType.NetUserOutlineEvent, msg));
					}
				}
			}
		}
	}

}
