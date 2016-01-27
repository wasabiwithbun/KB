/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.core.net;

import java.util.List;

/**
 * 网络管理
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-8-14 上午8:59:47
 * @version 1.0
 */
public interface INetManager {
	/**
	 * 发送公共数据
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-8-19 下午2:19:10
	 * @version 1.0
	 * @exception 
	 * @param msgBean
	 */
	void sendCommonData(MsgBean msgBean);
	/**
	 * 接收数据
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-8-16 下午4:04:03
	 * @version 1.0
	 * @exception
	 */
	void receiveCommonData();
	/**
	 * 发送单点数据
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-8-20 下午2:23:35
	 * @version 1.0
	 * @exception 
	 * @param ip IP
	 * @param port 端口
	 * @param msgBean 消息
	 */
	void sendClientData(String ip,int port,MsgBean msgBean);
	
	/**
	 * 取得网络数据
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-8-16 下午8:18:53
	 * @version 1.0
	 * @exception 
	 * @return
	 */
	List<MsgBean> getNetDataList();
	/**
	 * 取得搜索数据
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-8-19 下午7:50:51
	 * @version 1.0
	 * @exception 
	 * @return
	 */
	List<MsgBean> getSearchNetDataList();
	
	
	Server getServer();

}
