/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.core.service.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * 网络设置对象
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-9-2 上午9:30:35
 * @version 1.0
 */
public class NetSettingBean {
	/**组播IP*/
	private String ip;
	/**组播端口*/
	private int port;
	/**用户名*/
	private String userName;
	/**组播IP和端口集合*/
	private List<NetSettingBean> listMulIpAndPort = new ArrayList<NetSettingBean>();
	/**组播IP和端口监听集合*/
	private List<NetSettingBean> listListenMulIpAndPort = new ArrayList<NetSettingBean>();
	
	
	/**
	 * 
	 */
	public NetSettingBean() {
		super();
	}
	/**
	 * @param ip
	 * @param port
	 * @param userName
	 */
	public NetSettingBean(String ip, int port, String userName) {
		super();
		this.ip = ip;
		this.port = port;
		this.userName = userName;
	}
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the listMulIpAndPort
	 */
	public List<NetSettingBean> getListMulIpAndPort() {
		return listMulIpAndPort;
	}
	/**
	 * @param listMulIpAndPort the listMulIpAndPort to set
	 */
	public void setListMulIpAndPort(List<NetSettingBean> listMulIpAndPort) {
		this.listMulIpAndPort = listMulIpAndPort;
	}
	/**
	 * @return the listListenMulIpAndPort
	 */
	public List<NetSettingBean> getListListenMulIpAndPort() {
		return listListenMulIpAndPort;
	}
	/**
	 * @param listListenMulIpAndPort the listListenMulIpAndPort to set
	 */
	public void setListListenMulIpAndPort(List<NetSettingBean> listListenMulIpAndPort) {
		this.listListenMulIpAndPort = listListenMulIpAndPort;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + port;
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NetSettingBean other = (NetSettingBean) obj;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (port != other.port)
			return false;
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ip + ":" + port ;
	}
	
	
	

}
