/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.core.net;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;
import org.javacoo.cowswing.plugin.core.constant.CoreConstant;


/**
 * 客户端
 * <p>
 * 说明:
 * </p>
 * <li></li>
 * 
 * @author DuanYong
 * @since 2013-8-17 下午6:18:41
 * @version 1.0
 */
public class Client {
	protected Logger logger = Logger.getLogger(this.getClass());
	/**网络客户*/
	private NetUserClient userClient;
	/**IP*/
	private String ip;
	/**端口*/
	private int port;
	/**Socket*/
	private Socket socket;
	
	public Client(String ip,int port){
		this.ip = ip;
		this.port = port;
	}
	
	public boolean connect(){
        try {
           socket = new Socket(ip, port);
           userClient = new NetUserClient(socket);
           userClient.sendMsg(new MsgBean(CoreConstant.NET_ACTION_TYPE_CHATREQ));
          // new ReceiveMsg(userClient).start();
           return true;
       } catch (UnknownHostException e) {
           e.printStackTrace();
           return false;
       } catch (IOException e) {
           e.printStackTrace();
           return false;
       }
   }
   
   private class ReceiveMsg extends Thread{
       private NetUserClient userClient;
     
       public ReceiveMsg(NetUserClient userClient) {
           this.userClient = userClient;
       }

       @Override
       public void run() {
           while(true){
               try {
            	   MsgBean msgBean = userClient.receiveMsg();
            	   if(CoreConstant.NET_ACTION_ARE_YOU_STILL_ALIVE.equals(msgBean.getAction())){
						logger.info("Client收到心跳询问:"+msgBean.toString());
						//netManager.addNetUserToList(new NetClientBean(msgBean.getParams()[0], Integer.valueOf(msgBean.getParams()[1]),msgBean.getParams()[2]));
					}
               } catch (IOException e) {
                   e.printStackTrace();
                   break;
               }
           }
       }
       
   }

   public void stop() {
       try {
    	   userClient = null;
    	   if (socket.isConnected()){
    		   socket.close();
    	   }
       } catch (IOException e) {
           e.printStackTrace();
       }
   }


   public void sendMsg(MsgBean sendMsg) {
       userClient.sendMsg(sendMsg);
   }
   
	
}
