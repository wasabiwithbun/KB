/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.chat.ui.view.panel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.media.Buffer;
import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.control.FrameGrabbingControl;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;
import javax.media.util.BufferToImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.core.event.CowSwingEvent;
import org.javacoo.cowswing.core.event.CowSwingListener;
import org.javacoo.cowswing.core.event.CowSwingObserver;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-1-9 上午10:04:08
 * @version 1.0
 */
@org.springframework.stereotype.Component("videoPanel")
public class VideoPanel extends JPanel implements CowSwingListener{
	private static final long serialVersionUID = 1L;
	protected Logger logger = Logger.getLogger(this.getClass());
	/**视频发送接收端口*/
	private static int VIDEO_PPRT = 50000;
	// 定义面板
	private JPanel  p2, p3, p4;
	private Icon defaultIcon = new ImageIcon(ImageLoader.getImage("CrawlerResource.logo_big"));
	// 初始时，在接收图像窗口显示一幅静态图片
	private JLabel defaultUserImg = new JLabel(defaultIcon);
	private JLabel defaultSelfImg = new JLabel(defaultIcon);
	/** 控制面板 */
	private JPanel controlPanel;
	// 定义视频图像播放器
	private static Player player = null;
	// 定义音频播放器
	private static Player soundPlayer = null;
	// 获取视频设备
	private CaptureDeviceInfo device = null;
	// 获取音频设备
	private CaptureDeviceInfo soundDevice = null;
	// 媒体定位器
	private MediaLocator locator = null;
	private Image image;
	private Buffer buffer = null;
	private BufferToImage b2i = null;
	// 设置摄像头驱动类型
	private String deviceStr = "vfw:Microsoft WDM Image Capture (Win32):0";
	// 定义播放组件变量
	private Component comV, comVC, comA;
	//是否停止 默认停止
	private boolean isStop = true;
    public VideoPanel(){
    	initCmp();
    	initEvent();
		CowSwingObserver.getInstance().addCrawlerListener(this);
    }
    /**
     * 
     * <p>方法说明:</>
     * <li></li>
     * @author DuanYong
     * @since 2014-1-9 上午10:28:25
     * @version 1.0
     * @exception
     */
    private void initCmp() {
    	p2 = new JPanel(new GridLayout(2, 1));
		p3 = new JPanel(new BorderLayout());
		p4 = new JPanel(new BorderLayout());
		p2.add(p3);
		p2.add(p4);
		
		p3.add(defaultUserImg, BorderLayout.CENTER);
		p4.add(defaultSelfImg, BorderLayout.CENTER);
		
		
		add(p2, BorderLayout.CENTER);
   }

	public void startVideo(String senderIp) {
		if(isStop){
			try {
				isStop = false;
				p4.removeAll();
				// 设备初始化
				deviceInit();
				// 在本地播放音频
				speaker();
				new Thread(new Sender(senderIp)).start();
				new Thread(new Receive()).start();
				validate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
    /**
     * 
     * <p>方法说明:</>
     * <li></li>
     * @author DuanYong
     * @since 2014-1-9 上午10:28:28
     * @version 1.0
     * @exception
     */
   private void initEvent() {
	   
   }
   /**
    * 关闭视频
    * <p>方法说明:</>
    * <li></li>
    * @author DuanYong
    * @since 2014-1-10 上午10:31:04
    * @version 1.0
    * @exception
    */
   public void cancel(){
	   logger.info("关闭视频");
		isStop = true;
	    if(player!=null) {
	    	player.stop();
	    	player.close();
		}
	    if(soundPlayer!=null) {
	    	soundPlayer.stop();
	    	soundPlayer.close();
		}
	    p3.removeAll();
	    p4.removeAll();
	    defaultUserImg.setIcon(defaultIcon);
	    defaultSelfImg.setIcon(defaultIcon);
	    p3.add(defaultUserImg, BorderLayout.CENTER);
		p4.add(defaultSelfImg, BorderLayout.CENTER);
		p2.updateUI();
   }
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.core.event.CowSwingListener#update(org.javacoo.cowswing.core.event.CowSwingEvent)
	 */
	@Override
	public void update(CowSwingEvent event) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 设备初始化
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-1-9 上午11:07:10
	 * @version 1.0
	 * @exception 
	 * @throws Exception
	 */
	private void deviceInit() throws Exception{
		// 初始化设备，str为设备驱动
		device = CaptureDeviceManager.getDevice(deviceStr);
		// 确定所需的协议和媒体资源的位置
		locator = device.getLocator();
		try{
			// 调用sethint后Manager会尽力用一个能和轻量级组件混合使用的Renderer来创建播放器
			Manager.setHint(Manager.LIGHTWEIGHT_RENDERER, new Boolean(true));
			// 通过管理器创建播放线程使player达到Realized状态
			player = Manager.createRealizedPlayer(locator);
			player.start();
			// player.getVisualComponent()是一个播放视频媒体组件。
			if ((comV = player.getVisualComponent()) != null){
				p4.add(comV, BorderLayout.CENTER);
			}
			// player.getControlPanelComponent()是显示时间的组件
			if ((comVC = player.getControlPanelComponent()) != null){
				p4.add(comVC, BorderLayout.SOUTH);
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		int new_w = p4.getWidth(); // 输出的图像宽度

		int new_h = p4.getHeight(); // 输出的图像高度
        
		// MediaTracker类跟踪一个Image对象的装载，完成图像加载

		MediaTracker mt = new MediaTracker(this.p4);

		try{

			mt.addImage(image, 0); // 装载图像

			mt.waitForID(0); // 等待图像全部装载

		}catch (Exception e){
			e.printStackTrace();
		}

		// 将图像信息写入缓冲区

		BufferedImage buffImg = new BufferedImage(new_w, new_h,BufferedImage.TYPE_INT_RGB);

		Graphics g = buffImg.createGraphics();

		g.drawImage(image, 0, 0, new_w, new_h, this.p4);

		g.dispose();

	}
	/**
	 * 在本地播放音频
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-1-9 上午11:04:35
	 * @version 1.0
	 * @exception 
	 * @throws Exception
	 */
	private void speaker() throws Exception{
		Vector deviceList = CaptureDeviceManager.getDeviceList(new AudioFormat(AudioFormat.LINEAR, 44100, 16, 2));
		if (deviceList.size() > 0){
			soundDevice = (CaptureDeviceInfo) deviceList.firstElement();
		}else{
			logger.error("找不到音频设备！");
		}
		try{
			soundPlayer = Manager.createRealizedPlayer(soundDevice.getLocator());
			soundPlayer.start();
			if ((comA = soundPlayer.getControlPanelComponent()) != null){
				p3.add(comA, "South");
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 视频发送
	 * 
	 * <p>说明:</p>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-1-9 上午11:20:33
	 * @version 1.0
	 */
	class Sender implements Runnable{
		private String senderIp;
		public Sender(String senderIp){
			this.senderIp = senderIp;
		}
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
				while (true && !isStop){
					try{
						// 捕获要在播放窗口显示的图象帧
						FrameGrabbingControl fgc = (FrameGrabbingControl) player.getControl("javax.media.control.FrameGrabbingControl");
						// 获取当前祯并存入Buffer类
						buffer = fgc.grabFrame();
						b2i = new BufferToImage((VideoFormat) buffer.getFormat());
						image = b2i.createImage(buffer); // 转化为图像
						System.out.println("image=" + image);
						if (null != image) {
							// 创建image图像对象大小的图像缓冲区
							BufferedImage bi = (BufferedImage) createImage(image.getWidth(null), image.getHeight(null));
							// 根据BufferedImage对象创建Graphics2D对象
							Graphics2D g2 = bi.createGraphics();
							g2.drawImage(image, 0, 0, 320, 240, null);
							//g2.drawImage(image, null,null);
							ByteArrayOutputStream output = new ByteArrayOutputStream();
							// 转换成JPEG图像格式
							JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(output);
							JPEGEncodeParam jpeg = encoder.getDefaultJPEGEncodeParam(bi);
							jpeg.setQuality(0.5f, false);
							encoder.setJPEGEncodeParam(jpeg);
							encoder.encode(bi);
							output.close();
							InetAddress address = InetAddress.getByName(senderIp);
							DatagramPacket senderDataPacket = new DatagramPacket(output.toByteArray(), output.size(),address, VIDEO_PPRT);
							DatagramSocket senderDatagramSocket = new DatagramSocket();
							senderDatagramSocket.send(senderDataPacket);
							Thread.sleep(400);
						}
					}catch (Exception e){
						e.printStackTrace();
						logger.error("视频发送失败！");
					}
				
			}
	}
		
	}
   /**
    * 视频接收
    * 
    * <p>说明:</p>
    * <li></li>
    * @author DuanYong
    * @since 2014-1-9 上午11:13:32
    * @version 1.0
    */
	class Receive implements Runnable{
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			DatagramPacket pack = null;
			DatagramSocket maildata = null;
			byte data[] = new byte[320 * 240];
			try{
				// 定义数据包
				pack = new DatagramPacket(data, data.length);
				// 定义数据报接收包
				maildata = new DatagramSocket(VIDEO_PPRT);
			}catch (Exception e) {
				e.printStackTrace();
			}
			while (true && !isStop){
				if (maildata == null) {
					break;
				}else{
					try{// 接收
						maildata.receive(pack);
						ByteArrayInputStream input = new ByteArrayInputStream(data);
						Image reveiveImage = ImageIO.read(input);
						// 在接收图像窗口显示视频图像
						defaultUserImg.setIcon(new ImageIcon(reveiveImage));

						logger.info("对方ＩＰ：" + pack.getAddress() + " 端口：" + pack.getPort());

					}catch (Exception e) {
						e.printStackTrace();
						logger.error("接收图像数据失败！");
					}
				}
			}
			maildata.close();
			maildata = null;
		}
		
	}
    
    

}
