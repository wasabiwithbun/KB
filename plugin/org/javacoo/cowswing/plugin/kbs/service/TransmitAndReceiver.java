/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.service;

import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Codec;
import javax.media.Control;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Format;
import javax.media.Manager;
import javax.media.NoDataSourceException;
import javax.media.NoPlayerException;
import javax.media.NoProcessorException;
import javax.media.Owned;
import javax.media.Player;
import javax.media.Processor;
import javax.media.RealizeCompleteEvent;
import javax.media.control.BufferControl;
import javax.media.control.FormatControl;
import javax.media.control.QualityControl;
import javax.media.control.TrackControl;
import javax.media.format.AudioFormat;
import javax.media.format.UnsupportedFormatException;
import javax.media.format.VideoFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.protocol.PushBufferDataSource;
import javax.media.protocol.PushBufferStream;
import javax.media.protocol.SourceCloneable;
import javax.media.rtp.InvalidSessionAddressException;
import javax.media.rtp.RTPManager;
import javax.media.rtp.ReceiveStream;
import javax.media.rtp.ReceiveStreamListener;
import javax.media.rtp.SendStream;
import javax.media.rtp.SessionAddress;
import javax.media.rtp.event.ByeEvent;
import javax.media.rtp.event.NewReceiveStreamEvent;
import javax.media.rtp.event.ReceiveStreamEvent;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import jmapps.util.StateHelper;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.javacoo.cowswing.plugin.chat.ui.view.panel.PlayPanel;

/**
 * 发送和接收视频类
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-1-15 上午9:50:59
 * @version 1.0
 */
public class TransmitAndReceiver {
	protected Logger logger = Logger.getLogger(this.getClass());
	/**视频发送接收端口*/
	private static int VIDEO_PORT = 10000;
	/**视频接收端口*/
	private static int VIDEO_RECEIVER_PORT = 20000;
	/**音频发送接收端口*/
	private static int AUDIO_PORT = 40000;
	/**音频接收端口*/
	private static int AUDIO_RECEIVER_PORT = 50000;
	/**音视频发送对象*/
	private Transmit transmit;
	/**音视频接收对象*/
	private Receiver receiver;
	/**视频,音频播放器容器*/
	private PlayPanel remotePlayerContaner;
	/**目标客户端IP*/
	private String targetIp;
	/**目标客户端端口*/
	private int targetPort;
	private Player localPlayer = null;
	public TransmitAndReceiver(PlayPanel remotePlayerContaner,String targetIp){
		this.remotePlayerContaner = remotePlayerContaner;
		this.targetIp = targetIp;
		init();
	}
	/**
	 * 启动服务
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-1-16 下午2:37:10
	 * @version 1.0
	 * @exception 
	 * @return
	 */
	public String start(){
		String msg = "";
		msg = transmit.sendVideo();
		if(StringUtils.isNotBlank(msg)){
			return msg;
		}
		msg = transmit.sendAudio();
		return msg;
	}
	/**
	 * 关闭对象
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-1-15 上午10:35:11
	 * @version 1.0
	 * @exception
	 */
	public void close(){
		if (transmit != null) {
			transmit.close();
			transmit = null;
		}
		if (receiver != null) {
			receiver.disconnect();
			receiver = null;
		}
	}
	/**
	 * 初始化
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-1-15 上午10:41:12
	 * @version 1.0
	 * @exception
	 */
	private void init(){
		transmit = new Transmit();
		receiver = new Receiver();
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
	class Transmit {
		/**视频CaptureDeviceInfo对象*/
		private CaptureDeviceInfo videoCaptureDeviceInfo = null;
		/**视频Processor对象*/
		private Processor videoProcessor = null;
		/**视频RTPManagerr对象*/
		private RTPManager videoRTPManager = null;
		/**音频CaptureDeviceInfo对象*/
		private CaptureDeviceInfo audioCaptureDeviceInfo = null;
		/**音频Processor对象*/
		private Processor audioProcessor = null;
		/**音频RTPManagerr对象*/
		private RTPManager audioRTPManager = null;
		
		/**
		 * 发送视频
		 * <p>方法说明:</>
		 * <li></li>
		 * @author DuanYong
		 * @since 2014-1-15 上午9:58:10
		 * @version 1.0
		 * @exception
		 */
		private String sendVideo(){
			SessionAddress local;
			SessionAddress target;
			try {
				local = new SessionAddress(InetAddress.getLocalHost(),VIDEO_PORT);
				target = new SessionAddress(InetAddress.getByName(targetIp) ,VIDEO_RECEIVER_PORT);
			}  catch (UnknownHostException e) {
				e.printStackTrace();
				logger.error("IP不存在,请检查配置!"+e.getMessage());
				return "IP不存在,请检查配置!";
			}
			Vector<CaptureDeviceInfo> videoVector = CaptureDeviceManager.getDeviceList(new VideoFormat(null));
			if(null == videoVector || videoVector.size() <= 0){
				logger.error("没有检测到您的视频采集设备!");
				return "没有检测到您的视频采集设备!";
			}
			videoCaptureDeviceInfo = videoVector.get(0);
			try {
				DataSource localDs = Manager.createDataSource(videoCaptureDeviceInfo.getLocator());
				localDs = Manager.createCloneableDataSource(localDs); 
				DataSource cloneDs = ((SourceCloneable) localDs).createClone();  
				
				videoProcessor = Manager.createProcessor(cloneDs);  
				
				//videoProcessor = Manager.createProcessor(videoCaptureDeviceInfo.getLocator());
				StateHelper sh = new StateHelper(videoProcessor);
				doSomeVideoProcess(videoProcessor,sh);
				videoRTPManager = RTPManager.newInstance();
				
				videoRTPManager.initialize(local);
				videoRTPManager.addTarget(target);
                if(null != videoProcessor){
                	logger.info("========="+videoProcessor);
					DataSource ds = videoProcessor.getDataOutput();
					PushBufferDataSource pbds = (PushBufferDataSource)ds;
				    PushBufferStream pbss[] = pbds.getStreams();
				    for(int i=0;i<pbss.length;i++) {
						try {
							SendStream ss = videoRTPManager.createSendStream(ds, i);
							ss.start();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (UnsupportedFormatException e) {
							e.printStackTrace();
						}
				    }
				    videoProcessor.start();
                    //本地播放
                	localPlayer(localDs);
				    return "";
                }
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("网络错误："+e.getMessage());
				return "网络错误!";
			} catch (InvalidSessionAddressException e) {
				e.printStackTrace();
				logger.error("网络错误："+e.getMessage());
				return "网络错误!";
			} catch (NoProcessorException e) {
				e.printStackTrace();
				logger.error("发送视频失败："+e.getMessage());
				return "发送视频失败!";
			} catch (NoDataSourceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "";
		}
		/**
		 * 加载本地播放器
		 * <p>方法说明:</>
		 * <li></li>
		 * @author DuanYong
		 * @since 2014-1-16 下午3:48:16
		 * @version 1.0
		 * @exception 
		 * @param ds
		 */
		private void localPlayer(final DataSource ds){
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					remotePlayerContaner.localPlay(ds);
				}
			});
//			try {  
//				localPlayer = javax.media.Manager.createPlayer(ds);  
//  	            localPlayer.addControllerListener(new ControllerListener() {  
//  	                public void controllerUpdate(ControllerEvent e) {  
//  	  
//  	                    if (e instanceof javax.media.RealizeCompleteEvent) {  
//  	                        final Component comp = localPlayer.getVisualComponent();  
//  	                        SwingUtilities.invokeLater(new Runnable() {
//  	        					public void run() {
//  	        					   if (comp != null) {   
//  	        							comp.setBounds(1, 1, 10,  10);  
//  	     	                        	localPlayerContaner.add(comp);  
//  	     	                        	localPlayerContaner.updateUI();
//  	     	                        }  
//  	        					 localPlayerContaner.validate();  
//  	        					}
//  	        				});
//  	                       
//  	                    }  
//  	                }  
//  	            });  
//  	            localPlayer.start();   
//	        } catch (NoPlayerException e1) {  
//	            e1.printStackTrace();  
//	        } catch (IOException e1) {  
//	            e1.printStackTrace();  
//	        }  
		}
		/**
		 * 发送音频
		 * <p>方法说明:</>
		 * <li></li>
		 * @author DuanYong
		 * @since 2014-1-15 上午9:58:40
		 * @version 1.0
		 * @exception
		 */
		public String sendAudio(){
			SessionAddress local;
			SessionAddress target;
			try {
				local = new SessionAddress(InetAddress.getLocalHost(),AUDIO_PORT);
				target = new SessionAddress(InetAddress.getByName(targetIp) ,AUDIO_RECEIVER_PORT);
			} catch (UnknownHostException e) {
				e.printStackTrace();
				logger.error("IP不存在,请检查配置!"+e.getMessage());
				return "IP不存在,请检查配置!";
			}
			Vector<CaptureDeviceInfo> audioVector = CaptureDeviceManager.getDeviceList(new AudioFormat(null));
			if(null == audioVector || audioVector.size() <= 0){
				logger.error("没有检测到您的音频采集设备!");
				return "没有检测到您的音频采集设备!";
			}
			audioCaptureDeviceInfo = audioVector.get(0);
			
			try {
				audioProcessor = Manager.createProcessor(audioCaptureDeviceInfo.getLocator());
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("网络异常!"+e.getMessage());
			} catch (NoProcessorException e) {
				e.printStackTrace();
				logger.error("发送!音频失败"+e.getMessage());
			}
			StateHelper sh = new StateHelper(audioProcessor);
			doSomeAudioProcess(audioProcessor,sh);
			audioRTPManager = RTPManager.newInstance();
			try {
				audioRTPManager.initialize(local);
				audioRTPManager.addTarget(target);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidSessionAddressException e) {
				e.printStackTrace();
			}
			DataSource ds = audioProcessor.getDataOutput();
			PushBufferDataSource pbds = (PushBufferDataSource)ds;
		    PushBufferStream pbss[] = pbds.getStreams();
		    for(int i=0;i<pbss.length;i++) {
				try {
					SendStream ss = audioRTPManager.createSendStream(ds, i);
					ss.start();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (UnsupportedFormatException e) {
					e.printStackTrace();
				}
		    }
		    audioProcessor.start();
		    return "";
		}
		/**
		 * 视频处理
		 * <p>方法说明:</>
		 * <li></li>
		 * @author DuanYong
		 * @since 2014-1-15 上午9:59:16
		 * @version 1.0
		 * @exception 
		 * @param p
		 * @param sh
		 */
		private void doSomeVideoProcess(Processor p,StateHelper sh) {
			sh.configure(5000);
			p.setContentDescriptor(new ContentDescriptor(ContentDescriptor.RAW_RTP));
			setVideoTrackFormat(p.getTrackControls());		
			sh.realize(5000);
			setJPEGQuality(p, 0.5f);
		}
		/**
		 * 音频处理
		 * <p>方法说明:</>
		 * <li></li>
		 * @author DuanYong
		 * @since 2014-1-15 上午9:59:35
		 * @version 1.0
		 * @exception 
		 * @param p
		 * @param sh
		 */
		private void doSomeAudioProcess(Processor p,StateHelper sh) {
			sh.configure(5000);
			p.setContentDescriptor(new ContentDescriptor(ContentDescriptor.RAW));
			setAudioTrackFormat(p.getTrackControls());		
			sh.realize(5000);
		}
		/**
		 * 关闭视频
		 * <p>方法说明:</>
		 * <li></li>
		 * @author DuanYong
		 * @since 2014-1-14 下午2:16:16
		 * @version 1.0
		 * @exception
		 */
		public void close(){
			closeVideo();
			closeAudio();
		}
		/**
		 * 关闭视频
		 * <p>方法说明:</>
		 * <li></li>
		 * @author DuanYong
		 * @since 2014-1-14 下午2:16:34
		 * @version 1.0
		 * @exception
		 */
		private void closeVideo(){
		    if(videoProcessor!=null) {
		    	videoProcessor.stop();
		    	videoProcessor.close();
		    	videoProcessor = null;
			}
			if(videoRTPManager != null) {
				videoRTPManager.removeTargets("client disconnnected");
				videoRTPManager.dispose();
				videoRTPManager = null;
			}
			if(localPlayer != null){
				localPlayer.stop();
				localPlayer.close();
				localPlayer = null;
			}
		}
		/**
		 * 关闭音频
		 * <p>方法说明:</>
		 * <li></li>
		 * @author DuanYong
		 * @since 2014-1-15 上午10:00:06
		 * @version 1.0
		 * @exception
		 */
		private void closeAudio(){
		    if(audioProcessor!=null) {
		    	audioProcessor.stop();
		    	audioProcessor.close();
			}
			if(audioRTPManager != null) {
				audioRTPManager.removeTargets("client disconnnected");
				audioRTPManager.dispose();
				audioRTPManager = null;
			}
		}
		/**
		 * 设置视频格式
		 * <p>方法说明:</>
		 * <li></li>
		 * @author DuanYong
		 * @since 2014-1-15 上午10:00:34
		 * @version 1.0
		 * @exception 
		 * @param tracks
		 * @return
		 */
		private boolean setVideoTrackFormat(TrackControl[] tracks) {
			if(tracks==null || tracks.length<1)
				return false;
			boolean atLeastOneTrack = false;
			for(TrackControl t:tracks) {
				if(t.isEnabled()) {
					Format[] supported = t.getSupportedFormats();
					Format chosen = null;
					if(supported.length>0) {
						if(supported[0] instanceof VideoFormat)
							chosen = checkForVideoSizes(t.getFormat(),supported[0]);
						else
							chosen = supported[0];					
						t.setFormat(chosen);
						atLeastOneTrack = true;
					} else {
						t.setEnabled(false);
					}
				} else {
					t.setEnabled(false);
				}
			}
			return atLeastOneTrack;
		}	
		/**
		 * 检查格式
		 * <p>方法说明:</>
		 * <li></li>
		 * @author DuanYong
		 * @since 2014-1-15 上午10:00:48
		 * @version 1.0
		 * @exception 
		 * @param original
		 * @param supported
		 * @return
		 */
		private Format checkForVideoSizes(Format original, Format supported) {
		    int width, height;
		    Dimension size = ((VideoFormat)original).getSize();
		    Format jpegFmt = new Format(VideoFormat.JPEG_RTP);
		    Format h263Fmt = new Format(VideoFormat.H263_RTP);
		    logger.info("图像输出大小："+size.toString());
		    if (supported.matches(jpegFmt)) {
		        // For JPEG, make sure width and height are divisible by 8.
		        width = (size.width % 8 == 0 ? size.width :
		                (int)(size.width / 8) * 8);
		        height = (size.height % 8 == 0 ? size.height :
		                (int)(size.height / 8) * 8);
		    } else if (supported.matches(h263Fmt)) {
		        // For H.263, we only support some specific sizes.
		        if (size.width < 128) {
			        width = 128;
			        height = 96;
		        } else if (size.width < 176) {
			        width = 176;
			        height = 144;
		        } else {
			        width = 352;
			        height = 288;
		        }
		    } else {
		    	// We don't know this particular format.  We'll just
		        // leave it alone then.
		        return supported;	        
		    }
		    return (new VideoFormat(null, 
	                new Dimension(width, height), 
	                Format.NOT_SPECIFIED,
	                null,
	                Format.NOT_SPECIFIED)).intersects(supported);
		}
		/**
		 * 设置音频格式
		 * <p>方法说明:</>
		 * <li></li>
		 * @author DuanYong
		 * @since 2014-1-15 上午10:01:03
		 * @version 1.0
		 * @exception 
		 * @param trackControls
		 */
		private void setAudioTrackFormat(TrackControl[] trackControls) {
			boolean ok = false;
		    for(TrackControl t:trackControls){
		    	if(!ok && t instanceof FormatControl) {
		    		if(((FormatControl)t).setFormat(new AudioFormat(AudioFormat.ULAW_RTP,8000,8,1))==null) 
		    			t.setEnabled(false);
		    		 else 
		    			ok = true;	    		
		    	} else {
		    		t.setEnabled(false);
		    	}
		    }
		}
		/**
		 * 图像格式处理
		 * <p>方法说明:</>
		 * <li></li>
		 * @author DuanYong
		 * @since 2014-1-15 上午10:01:19
		 * @version 1.0
		 * @exception 
		 * @param p
		 * @param val
		 */
		private void setJPEGQuality(Processor p, float val) {
			Control cs[] = p.getControls();
		    QualityControl qc = null;
		    VideoFormat jpegFmt = new VideoFormat(VideoFormat.JPEG);
		    // Loop through the controls to find the Quality control for
		    // the JPEG encoder.
		    for (int i = 0; i < cs.length; i++) {
		        if (cs[i] instanceof QualityControl && cs[i] instanceof Owned) {
		        Object owner = ((Owned)cs[i]).getOwner();
		        // Check to see if the owner is a Codec.
		        // Then check for the output format.
		        if (owner instanceof Codec) {
		            Format fmts[] = ((Codec)owner).getSupportedOutputFormats(null);
		            for (int j = 0; j < fmts.length; j++) {
			            if (fmts[j].matches(jpegFmt)) {
			                qc = (QualityControl)cs[i];
			                qc.setQuality(val);	                
			                break;
			            }
		            }
		        }
		        if (qc != null)
		            break;
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
	class Receiver implements ReceiveStreamListener,ControllerListener{
        /**播放器*/
		private Player player;
		/**视频RTPManager对象*/
		private RTPManager videoRTPManager;
		/**音频RTPManager对象*/
		private RTPManager audioRTPManager;
		public Receiver() {
			initVideoReceiver();
			initAudioReceiver();
		}
		/**
		 * 初始化视频接收
		 * <p>方法说明:</>
		 * <li></li>
		 * @author DuanYong
		 * @since 2014-1-15 上午10:03:41
		 * @version 1.0
		 * @exception
		 */
		private void initVideoReceiver(){
			SessionAddress local;
			SessionAddress target;
			try {
				local = new SessionAddress(InetAddress.getLocalHost(),VIDEO_RECEIVER_PORT);
				target = new SessionAddress(InetAddress.getByName(targetIp) ,VIDEO_PORT);
			} catch (UnknownHostException e) {
				e.printStackTrace();
				return;
			}
			videoRTPManager = RTPManager.newInstance();
			videoRTPManager.addReceiveStreamListener(this);
			try {
				videoRTPManager.initialize(local);
				videoRTPManager.addTarget(target);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidSessionAddressException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BufferControl bc = (BufferControl)videoRTPManager.getControl("javax.media.control.BufferControl");
	        if (bc != null)
	            bc.setBufferLength(350);
		}
		/**
		 * 初始化音频接收
		 * <p>方法说明:</>
		 * <li></li>
		 * @author DuanYong
		 * @since 2014-1-15 上午10:03:57
		 * @version 1.0
		 * @exception
		 */
		private void initAudioReceiver(){
			SessionAddress local;
			SessionAddress target;
			try {
				local = new SessionAddress(InetAddress.getLocalHost(),AUDIO_RECEIVER_PORT);
				target = new SessionAddress(InetAddress.getByName(targetIp) ,AUDIO_PORT);
			} catch (UnknownHostException e) {
				e.printStackTrace();
				return;
			}
			audioRTPManager = RTPManager.newInstance();
			audioRTPManager.addReceiveStreamListener(this);
			try {
				audioRTPManager.initialize(local);
				audioRTPManager.addTarget(target);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidSessionAddressException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BufferControl bc = (BufferControl)audioRTPManager.getControl("javax.media.control.BufferControl");
	        if (bc != null)
	            bc.setBufferLength(350);
		}
		

		@Override
		public void update(ReceiveStreamEvent e) {
			logger.info("=========ReceiveStreamEvent事件:"+e.toString());
			if(e instanceof NewReceiveStreamEvent) {
				logger.info("=========收到新的流信息===");
				ReceiveStream rs = ((NewReceiveStreamEvent)e).getReceiveStream();
				final DataSource ds = rs.getDataSource();
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						remotePlayerContaner.remotePlay(ds);
						remotePlayerContaner.updateUI();
					}
				});  
				
//				try {
//					player = Manager.createPlayer(ds);
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				} catch (NoPlayerException e2) {
//					// TODO Auto-generated catch block
//					e2.printStackTrace();
//				}
//				player.addControllerListener(this);
//				player.start();			
			} else if(e instanceof ByeEvent) {
				disconnect();
			}
		}
		public void disconnect() {
			if(player!=null) {
				player.stop();
				player.close();
			}
			if(videoRTPManager!=null) {
				videoRTPManager.removeTargets("closing session");
				videoRTPManager.removeReceiveStreamListener(this);
				videoRTPManager.dispose();
				videoRTPManager = null;
			}
			if(audioRTPManager!=null) {
				audioRTPManager.removeTargets("closing session");
				audioRTPManager.removeReceiveStreamListener(this);
				audioRTPManager.dispose();
				audioRTPManager = null;
			}
		}
		@Override
		public void controllerUpdate(ControllerEvent e) {
			logger.info("=========ControllerEvent事件");
			if(e instanceof RealizeCompleteEvent) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						if(player.getVisualComponent()!=null){
							//remotePlayerContaner.add(player.getVisualComponent(),BorderLayout.CENTER);
							
							logger.info("=========添加视频组件");
						}
						if(player.getControlPanelComponent()!=null){
							//remotePlayerContaner.add(player.getControlPanelComponent(),BorderLayout.SOUTH);
							logger.info("=========添加音频组件");
						}
						remotePlayerContaner.updateUI();
					}
				});
			}
		}
		
	}

}
