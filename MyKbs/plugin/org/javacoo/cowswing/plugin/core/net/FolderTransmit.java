/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.core.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.javacoo.cowswing.base.utils.DateUtil;
import org.javacoo.cowswing.core.event.CowSwingEvent;
import org.javacoo.cowswing.core.event.CowSwingObserver;
import org.javacoo.cowswing.core.event.type.CowSwingEventType;
import org.javacoo.cowswing.plugin.core.constant.CoreConstant;

/**
 * 文件传输
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-8-19 下午9:18:28
 * @version 1.0
 */
public class FolderTransmit {
	protected Logger logger = Logger.getLogger(this.getClass());
	private String ip;
    private int port;
    private long totalLen = 0L;
    private String folderPath;
    private String filePath;
    private String folderName;
    private NetUserClient sendClient;
    private NetUserClient recvClient;
    private NetManager netManager;
    public FolderTransmit(Socket socket, NetManager netManager){
        this.recvClient = new NetUserClient(socket);
        this.netManager = netManager;
    }
    public FolderTransmit(String folderPath, String ip,int port, NetManager netManager) {
        this.folderPath = folderPath;
        this.netManager = netManager;
        this.ip = ip;
        this.port = port;
        this.folderName = new File(folderPath).getName();
    }
    public FolderTransmit(String folderPath,String filePath, String ip,int port, NetManager netManager) {
        this.folderPath = folderPath;
        this.filePath = filePath;
        this.netManager = netManager;
        this.ip = ip;
        this.port = port;
        this.folderName = new File(filePath).getName();
    }
    private void getFolderTotalLen(String path) {
        this.totalLen = 0L;
        File folder = new File(path);
        getFileLen(folder);
    }
    private void getFileLen(File folder) {
        File[] files = folder.listFiles();
        for (File file : files) {
            if(file.isFile()){
                this.totalLen += file.length();
            }else if(file.isDirectory()){
                getFileLen(file);
            }
        }
    }

    public void stop() {
        try {
            isStopSend = true;
            sendClient.getSocket().shutdownOutput();
            sendClient.getSocket().shutdownInput();
            sendClient.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String getSpeed(long time, long totalLen){
        String speed = (totalLen * 1000D) / (1024D * 1024D * time) + "";
        int indexP = speed.indexOf(".");
        if(indexP != -1){
            speed = speed.substring(0, indexP + 3);
        }
        return (speed + " MB/S");
    }
    private String getUseTime(long time){
        String useTime = "";
        if (time / 1000D / 60D >= 1) {
            useTime = time / 1000 / 60 + " 分钟";
        }else{
            if(time / 1000 == 0){
                useTime = "1 秒钟";
            }else{
                useTime = time / 1000 + " 秒钟";
            }
        }
        return useTime;
    }
    private static final int BUF_LEN = 102400;
    private boolean isStopSend = false;
    public void send() {
        Runnable conn = new Runnable() {
            
            @Override
            public void run() {
                sendRunnable();
            }
            
            private void sendRunnable(){
            	logger.info("发送下载数据开始");
                if(connect()){
                    Runnable sendRu = new Runnable() {
                        private String rootPath = null;
                        private long haveSendLen = 0L;
                        @Override
                        public void run() {
                            long beginTime = System.currentTimeMillis();
                            
                            File folder = new File(filePath);
                            
                            if(folder.isFile()){
                                totalLen = folder.length();
                                try {
                                    sendClient.sendLong(totalLen);
                                    sendClient.sendMsg(new MsgBean(CoreConstant.NET_ACTION_TYPE_DOWNLOAD_ONLYFILE,folder.getName()));
                                    sendFile(folder);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                rootPath = folder.getAbsolutePath();
                                getFolderTotalLen(folderPath);
                                try {
                                    sendClient.sendLong(totalLen);
                                    sendFolder(folder);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    return;
                                }
                            }
                           
                            sendClient.sendMsg(new MsgBean(CoreConstant.NET_ACTION_TYPE_DOWNLOAD_ENDFOLDERT));
                            netManager.isSending = false;
                            
                            long endTime = System.currentTimeMillis();
                            
                            if(!isStopSend){
                                String speed = getSpeed(endTime - beginTime,
                                        totalLen);
                                String useTime = getUseTime(endTime - beginTime);
                                StringBuilder msg = new StringBuilder();
                                msg.append("文件【").append(folderName).append("】发送完毕\n");
                                msg.append("目的地:").append(ip).append(":").append(port).append("\n");
                                msg.append("传送用时:").append(useTime).append(",速度:").append(speed).append("\n");
                                msg.append("完成时间:").append(DateUtil.dateToStr(DateUtil.getNow(),"yyyy-MM-dd HH:mm:ss")).append("\n");
                                CowSwingObserver.getInstance().notifyEvents(
    									new CowSwingEvent(this,
    											CowSwingEventType.NetDownLoadSuccess,msg));
                                logger.info(msg);
                            }else{
                                isStopSend = false;
                                String msg = "对方终止了文件【" + folderName + "】的传送!\n";
                                CowSwingObserver.getInstance().notifyEvents(
    									new CowSwingEvent(this,
    											CowSwingEventType.NetDownLoadBeak,msg));
                            	logger.info(msg);
                            }
                        }

                        private void sendFolder(File folder) {
                            String path = folder.getAbsolutePath();
                            int index = rootPath.length() - folderName.length();
                            String fPath = path.substring(index);
                            sendClient.sendMsg(new MsgBean(CoreConstant.NET_ACTION_TYPE_DOWNLOAD_BEGINFOLDERT,fPath));
                            File[] files = folder.listFiles();
                            List<File> listFile = new ArrayList<File>();
                            List<File> listFolder = new ArrayList<File>();
                            for (File file : files) {
                                if(file.isFile()){
                                    listFile.add(file);
                                }else if(file.isDirectory()){
                                    listFolder.add(file);
                                }
                            }
                            for (File file : listFile) {
                                sendFile(file);
                            }
                            for (File file : listFolder) {
                                sendFolder(file);
                            }
                        }

                        private boolean sendFile(File file) {
                            sendClient.sendMsg(new MsgBean(CoreConstant.NET_ACTION_TYPE_DOWNLOAD_BEGINFILET,file.getName()));
                            FileInputStream fis = null;
                            try {
                                fis = new FileInputStream(file);
                                byte[] buf = new byte[BUF_LEN];
                                int len = fis.read(buf);
                                while (len != -1) {
                                    haveSendLen += len;
                                    setTransferRate(haveSendLen, totalLen);
                                    
                                    sendClient.sendByte(buf, len);
                                    len = fis.read(buf);
                                }
                                sendClient.getOut().writeInt(len);
                                fis.close();
                                return true;
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                                isStopSend = true;
                                if(fis != null){
                                    try {
                                        fis.close();
                                        file = null;
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }
                            return false;
                        }
                        
                        private void setTransferRate(long haveRecvLen, long folderLen) {
                            long rate = ((haveRecvLen * 100) / folderLen);
                            logger.info("完成:  " + rate +"%");
                        }
                    };
                    netManager.isSending = true;
                    new Thread(sendRu).start();
                }
            }
        };
        new Thread(conn).start();
    }
    
    private boolean connect() {
        try {
        	logger.info("开始连接客户端：ip="+ip+",port="+port);
            Socket socket = new Socket(ip, port);
            sendClient = new NetUserClient(socket);
            sendClient.sendMsg(new MsgBean(CoreConstant.NET_ACTION_TYPE_DOWNLOAD_RETURN,this.folderPath));
            logger.info("发送下载数据请求：ip="+ip+",port="+port);
            MsgBean retMsgBean = sendClient.receiveMsg();
            logger.info("开始发送数据"+retMsgBean.getAction());
            if(CoreConstant.NET_ACTION_TYPE_DOWNLOAD_AGREE.equals(retMsgBean.getAction())){
                Runnable recvCMD = new Runnable() {
                	MsgBean runMsgBean = null;
                    @Override
                    public void run() {
                        while(true){
                            try {
                            	runMsgBean = sendClient.receiveMsg();
                                if(CoreConstant.NET_ACTION_TYPE_DOWNLOAD_BREAK.equals(runMsgBean.getAction())){
                                    isStopSend = true;
                                    break;
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                break;
                            }
                        }
                    }
                };
                new Thread(recvCMD).start();
                return true;
            }else{
            	logger.info(retMsgBean.toString());
                return false;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
   
    private boolean isFirstTime = true;
    private boolean isStopRecv = false;
    public void receive(final String path) {
    	logger.info("开始接收数据到：folderPath="+path);
        recvClient.sendMsg(new MsgBean(CoreConstant.NET_ACTION_TYPE_DOWNLOAD_AGREE));
        netManager.isRecving = true;
        Runnable recv = new Runnable() {
            private String folderPath = "";
            @Override
            public void run() {
                long beginTime = System.currentTimeMillis();
                
                long haveRecvLen = 0;// 已经接收的文件长度
                // get folder total length
                long folderLen = 0;
                try {
                    folderLen = recvClient.getIn().readLong();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                
                while(true){
                    FileOutputStream fos = null;
                    try {
                    	MsgBean recvCmd = recvClient.receiveMsg();
                        if(CoreConstant.NET_ACTION_TYPE_DOWNLOAD_BEGINFOLDERT.equals(recvCmd.getAction())){
                            String subFolder = recvCmd.getParams()[0];
                            if(isFirstTime){
                                folderName = subFolder;
                                isFirstTime = false;
                            }
                            if(path.endsWith(File.separator)){
                                folderPath = path + subFolder;
                            }else{
                                folderPath = path + File.separator + subFolder;
                            }
                            File file = new File(folderPath);
                            file.mkdirs();
                        }else if(CoreConstant.NET_ACTION_TYPE_DOWNLOAD_BEGINFILET.equals(recvCmd.getAction())){
                            String fileName = recvCmd.getParams()[0];
                            String filePath = folderPath + File.separator + fileName;
                            fos = new FileOutputStream(filePath);
                            
                            byte[] bs = new byte[BUF_LEN];
                            int count = recvClient.getIn().readInt();
                            while(count != -1){
                                recvClient.getIn().readFully(bs, 0, count);
                                fos.write(bs, 0, count);
                                fos.flush();
                                haveRecvLen += count;
                                setTransferRate(haveRecvLen, folderLen);
                                count = recvClient.getIn().readInt();
                            }
                            fos.close();
                        }else if(CoreConstant.NET_ACTION_TYPE_DOWNLOAD_ENDFOLDERT.equals(recvCmd.getAction())){
                            long endTime = System.currentTimeMillis();
                            
                            String speed = getSpeed(endTime - beginTime, folderLen);
                            String useTime = getUseTime(endTime - beginTime);
                            StringBuilder msg = new StringBuilder();
                            msg.append("文件【").append(folderName).append("】接收完毕\n");
                            msg.append("来源地:").append(recvClient.getSocket().getInetAddress()).append(":").append(recvClient.getSocket().getPort()).append("\n");
                            msg.append("传送用时:").append(useTime).append(",速度:").append(speed).append("\n");
                            msg.append("完成时间:").append(DateUtil.dateToStr(DateUtil.getNow(),"yyyy-MM-dd HH:mm:ss")).append("\n");
                            CowSwingObserver.getInstance().notifyEvents(
									new CowSwingEvent(this,
											CowSwingEventType.NetDownLoadSuccess,msg));
                            logger.info(msg);
                            break;
                        }else if(CoreConstant.NET_ACTION_TYPE_DOWNLOAD_ONLYFILE.equals(recvCmd.getAction())){
                            if(path.endsWith(File.separator)){
                                folderPath = path.substring(0, path.length()-1);
                            }else{
                                folderPath = path;
                            }
                            new File(folderPath).mkdirs();
                            folderName = recvCmd.getParams()[0];
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        if(!isStopRecv){
                        	String msg = "对方终止了文件【" + folderName + "】的传送!\n";
                            CowSwingObserver.getInstance().notifyEvents(
									new CowSwingEvent(this,
											CowSwingEventType.NetDownLoadBeak,msg));
                        	logger.info(msg);
                        }else{
                            isStopRecv = false;
                        }
                        if(fos != null){
                            try {
                                fos.close();
                                fos = null;
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                        break;
                    }
                }
                netManager.isRecving = false;
            }
            private void setTransferRate(long haveRecvLen, long folderLen) {
                double val = ((double)haveRecvLen * 100) / ((double)folderLen);
                int rate = (int)val;
                CowSwingObserver.getInstance().notifyEvents(
						new CowSwingEvent(this,
								CowSwingEventType.NetDownloadRate,haveRecvLen+","+folderLen));
                String msg = "完成:  " + rate +"%";
                CowSwingObserver.getInstance().notifyEvents(
						new CowSwingEvent(this,
								CowSwingEventType.NetDownLoadIng,msg));
                logger.info(msg);
            }
        };
        new Thread(recv).start();
    }

    public void breakRecvFolder() {
        try {
            recvClient.sendMsg(new MsgBean(CoreConstant.NET_ACTION_TYPE_DOWNLOAD_BREAK));
            isStopRecv = true;
            recvClient.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args){
    	File folder = new File("D:\\MyKbs\\update\\[电影天堂www.dy2018.com]生死倒数BD中英双字.rmvb");
    	System.out.println(folder.length());
    }
    
}
