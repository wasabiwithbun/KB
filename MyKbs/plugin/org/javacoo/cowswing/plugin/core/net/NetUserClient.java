/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.core.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import org.javacoo.cowswing.base.utils.JsonUtils;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;

/**
 * 网络用户
 * <p>
 * 说明:
 * </p>
 * <li></li>
 * 
 * @author DuanYong
 * @since 2013-8-17 下午2:44:32
 * @version 1.0
 */
public class NetUserClient {
	/** 用户名 */
	private String userName;
	/** 密码 */
	private String password;
	/** Socket */
	private Socket socket;
	/** 输入流 */
	private DataInputStream in;
	/** 输出流 */
	private DataOutputStream out;

	public NetUserClient(Socket socket) {
		this.socket = socket;
		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public MsgBean receiveMsg() throws IOException{
		String msg = "";
		try{
			int count = in.readInt();
			byte[] bs = new byte[count];
			in.readFully(bs);
			msg = new String(bs, KbsConstant.ENCODING);
		}catch(EOFException e){
			
		}
		return (MsgBean) JsonUtils.formatStringToObject(msg, MsgBean.class);
	}

	public void sendMsg(MsgBean msgBean) {
		try {
			String msg = JsonUtils.formatObjectToJsonString(msgBean);
			byte[] bs = msg.getBytes(KbsConstant.ENCODING);
			out.writeInt(bs.length);
			out.write(bs);
			out.flush();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendByte(byte[] buf, int len) throws IOException {
		out.writeInt(len);
		out.write(buf, 0, len);
		out.flush();
	}

	public void sendLong(long totalLen) throws IOException {
		out.writeLong(totalLen);
		out.flush();
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the socket
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * @param socket
	 *            the socket to set
	 */
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	/**
	 * @return the in
	 */
	public DataInputStream getIn() {
		return in;
	}

	/**
	 * @param in
	 *            the in to set
	 */
	public void setIn(DataInputStream in) {
		this.in = in;
	}

	/**
	 * @return the out
	 */
	public DataOutputStream getOut() {
		return out;
	}

	/**
	 * @param out
	 *            the out to set
	 */
	public void setOut(DataOutputStream out) {
		this.out = out;
	}

}
