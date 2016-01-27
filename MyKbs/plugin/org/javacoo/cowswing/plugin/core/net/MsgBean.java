/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.core.net;

import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import org.javacoo.cowswing.base.constant.Config;
import org.javacoo.cowswing.plugin.core.service.beans.SystemVersionBean;

/**
 * 消息对象
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-8-12 下午7:03:02
 * @version 1.0
 */
public class MsgBean {
	/**主键*/
	private String id;
	/**标题*/
	private String title;
	/**日期*/
	private String date;
	/**作者*/
	private String author;
	/**内容*/
	private String content;
	/**文件路径*/
	private String filePath;
	/**文件保存路径*/
	private String savePath;
	/**权限*/
	private String purview;
	/**模块*/
	private String module;
	/**请求类型*/
	private String action;
	/**IP*/
	private String ip;
	/**版本 */
	private double version;
	/**端口*/
	private int port;
	/**参数集合*/
	private String[] params;
	/**消息集合*/
	private List<MsgBean> msgBeanList;
	/**系统版本信息对象*/
	private SystemVersionBean systemVersionBean;
	/**Socket对象*/
	private Socket socket;
	/**
	 * 
	 */
	public MsgBean() {
		super();
	}

	/**
	 * @param action
	 */
	public MsgBean(String action,String... params) {
		super();
		this.action = action;
		this.params = params;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the purview
	 */
	public String getPurview() {
		return purview;
	}

	/**
	 * @param purview the purview to set
	 */
	public void setPurview(String purview) {
		this.purview = purview;
	}

	/**
	 * @return the module
	 */
	public String getModule() {
		return module;
	}

	/**
	 * @param module the module to set
	 */
	public void setModule(String module) {
		this.module = module;
	}
	
	

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
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
	 * @return the version
	 */
	public double getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(double version) {
		this.version = version;
		if(!Double.isNaN(version)){
			SystemVersionBean systemVersionBean = new SystemVersionBean();
			systemVersionBean.setVersion(Config.COWSWING_CONFIG_MAP.get(Config.COWSWING_CONFIG_KEY_VERSION).get(Config.COWSWING_CONFIG_KEY_VERSION_VERSION));
			systemVersionBean.setAuthor(Config.COWSWING_CONFIG_MAP.get(Config.COWSWING_CONFIG_KEY_VERSION).get(Config.COWSWING_CONFIG_KEY_VERSION_AUTHOR));
			systemVersionBean.setDate(Config.COWSWING_CONFIG_MAP.get(Config.COWSWING_CONFIG_KEY_VERSION).get(Config.COWSWING_CONFIG_KEY_VERSION_DATE));
			systemVersionBean.setInfo(Config.COWSWING_CONFIG_MAP.get(Config.COWSWING_CONFIG_KEY_VERSION).get(Config.COWSWING_CONFIG_KEY_VERSION_INFO));
			systemVersionBean.setUpdateList(Config.COWSWING_CONFIG_MAP.get(Config.COWSWING_CONFIG_KEY_VERSION).get(Config.COWSWING_CONFIG_KEY_VERSION_UPDATE_LIST));
			setSystemVersionBean(systemVersionBean);
		}
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
	 * @return the savePath
	 */
	public String getSavePath() {
		return savePath;
	}

	/**
	 * @param savePath the savePath to set
	 */
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	/**
	 * @return the params
	 */
	public String[] getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(String[] params) {
		this.params = params;
	}

	
	/**
	 * @return the msgBeanList
	 */
	public List<MsgBean> getMsgBeanList() {
		return msgBeanList;
	}

	/**
	 * @param msgBeanList the msgBeanList to set
	 */
	public void setMsgBeanList(List<MsgBean> msgBeanList) {
		this.msgBeanList = msgBeanList;
	}

	/**
	 * @return the systemVersionBean
	 */
	public SystemVersionBean getSystemVersionBean() {
		return systemVersionBean;
	}

	/**
	 * @param systemVersionBean the systemVersionBean to set
	 */
	public void setSystemVersionBean(SystemVersionBean systemVersionBean) {
		this.systemVersionBean = systemVersionBean;
	}
	
	
    
//	/* (non-Javadoc)
//	 * @see java.lang.Object#hashCode()
//	 */
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((author == null) ? 0 : author.hashCode());
//		result = prime * result + ((id == null) ? 0 : id.hashCode());
//		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
//		result = prime * result + ((module == null) ? 0 : module.hashCode());
//		return result;
//	}
//
//	/* (non-Javadoc)
//	 * @see java.lang.Object#equals(java.lang.Object)
//	 */
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		MsgBean other = (MsgBean) obj;
//		if (author == null) {
//			if (other.author != null)
//				return false;
//		} else if (!author.equals(other.author))
//			return false;
//		if (id == null) {
//			if (other.id != null)
//				return false;
//		} else if (!id.equals(other.id))
//			return false;
//		if (ip == null) {
//			if (other.ip != null)
//				return false;
//		} else if (!ip.equals(other.ip))
//			return false;
//		if (module == null) {
//			if (other.module != null)
//				return false;
//		} else if (!module.equals(other.module))
//			return false;
//		return true;
//	}

	/**
	 * @return the socket
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * @param socket the socket to set
	 */
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + ((module == null) ? 0 : module.hashCode());
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
		MsgBean other = (MsgBean) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (module == null) {
			if (other.module != null)
				return false;
		} else if (!module.equals(other.module))
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
		if(null != systemVersionBean){
			return systemVersionBean.toString();
		}
		return "MsgBean [id=" + id + ", title=" + title + ", date=" + date
				+ ", author=" + author + ", content=" + content + ", filePath="
				+ filePath + ", savePath=" + savePath + ", purview=" + purview
				+ ", module=" + module + ", action=" + action + ", ip=" + ip
				+ ", port=" + port + ", version=" + version + ", params=" + Arrays.toString(params)+"]";
	}

	
	
	
}
