/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.core.service.beans;

import org.apache.commons.lang.StringUtils;

/**
 * 客户端IP，端口信息
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-9-2 下午2:05:25
 * @version 1.0
 */
public class NetClientBean {
	/**本地IP*/
	private String ip;
	/**端口*/
	private int port;
	/**用户名*/
	private String userName;
	/** 是否是叶子节点 */
	private boolean isLeaf = true;
	
	/**
	 * @param ip
	 * @param port
	 * @param userName
	 */
	public NetClientBean(String ip, int port, String userName) {
		super();
		this.ip = ip;
		this.port = port;
		this.userName = userName;
	}
	/**
	 * @param ip
	 * @param port
	 */
	public NetClientBean(String ip, int port) {
		super();
		this.ip = ip;
		this.port = port;
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
	 * @return the isLeaf
	 */
	public boolean isLeaf() {
		return isLeaf;
	}
	/**
	 * @param isLeaf the isLeaf to set
	 */
	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
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
		NetClientBean other = (NetClientBean) obj;
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
		if(StringUtils.isNotBlank(ip)){
			return userName+"@"+ip + ":" + port;	
		}
		return "组内群聊";
	}
	
	

}
