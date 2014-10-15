/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.core.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketAddress;

import org.javacoo.cowswing.base.utils.JsonUtils;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;

/**
 * 组播帮助类
 * <p>
 * 说明:
 * </p>
 * <li></li>
 * 
 * @author DuanYong
 * @since 2013-8-12 下午5:10:45
 * @version 1.0
 */
public class GroupMsgHandler {
	//定义广播的IP地址
	private InetAddress groupAddress;
	//定义本程序的MulticastSocket实例
	private MulticastSocket multicastSocket;
	private SocketAddress socketAddress;
	// 定义每个数据报的最大大小为4K
	private static final int DATA_LEN = 4096;
	public GroupMsgHandler(String ip, int port) throws IOException {
		this.groupAddress = InetAddress.getByName(ip);
		this.multicastSocket = new MulticastSocket(port);
		this.multicastSocket.joinGroup(groupAddress);
		this.socketAddress = new InetSocketAddress(groupAddress, port);
	}

	public boolean sendMsg(MsgBean msgBean) {
		byte[] bs = null;
		try {
			String msg = JsonUtils.formatObjectToJsonString(msgBean);
			bs = msg.getBytes(KbsConstant.ENCODING);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		DatagramPacket hi = new DatagramPacket(bs, bs.length);
		hi.setSocketAddress(socketAddress);
		try {
			this.multicastSocket.send(hi);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public MsgBean receiveMsg() {
		MsgBean msgBean  = null;
		String recMsg = "";
		byte[] buf = new byte[DATA_LEN];
		DatagramPacket recv = new DatagramPacket(buf, buf.length);
		try {
			multicastSocket.receive(recv);
			recMsg = new String(recv.getData(), recv.getOffset(),
					recv.getLength(), KbsConstant.ENCODING);
			msgBean = (MsgBean) JsonUtils.formatStringToObject(recMsg, MsgBean.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msgBean;
	}

	public void leaveGroup() {
		try {
			multicastSocket.leaveGroup(groupAddress);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the multicastSocket
	 */
	public MulticastSocket getMulticastSocket() {
		return multicastSocket;
	}

	/**
	 * @param multicastSocket the multicastSocket to set
	 */
	public void setMulticastSocket(MulticastSocket multicastSocket) {
		this.multicastSocket = multicastSocket;
	}
	
	
}
