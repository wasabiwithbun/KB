/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.chat.ui.view.panel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;

import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.DataSink;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.Processor;
import javax.media.protocol.DataSource;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.javacoo.cowswing.base.loader.ImageLoader;
/**
 * 
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-1-17 上午9:53:15
 * @version 1.0
 */
public class PlayPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private ImageIcon videoReqIcon = new ImageIcon(ImageLoader.getImage("CrawlerResource.logo_big"));  
    private ImageIcon VideolocalIcon = new ImageIcon(ImageLoader.getImage("CrawlerResource.logo_small"));
    private boolean isViewBigPlaying = false;  
    private boolean isViewSmallPlaying = false;  
    private JPanel viewBigPane;  
    private JPanel viewSmallPane;  
    private JPanel controlPane;  
  
  
    private boolean localPlay = false;  
    private boolean remotePlay = false;  
  
    private DataSource localData;  
    private DataSource remoteData;  
  
    private boolean isViewRun = true;  
    private boolean isShow = true;  
    //  
    private Player localPlayer = null;  
    private Player remotePlayer = null;  
    //  
    private Processor videotapeProcessor = null;  
    private Player videotapePlayer = null;  
    private DataSink videotapeFileWriter;  
  
    public PlayPanel() {  
        this.setLayout(new BorderLayout());  
        // 视图面板  
        viewBigPane = new JPanel() {  
            public void paintComponent(Graphics g) {  
                super.paintComponent(g);  
                if (!isViewBigPlaying) {  
                    g.drawImage(videoReqIcon.getImage(), 1, 1,  
                            videoReqIcon.getIconWidth(),  
                            videoReqIcon.getIconHeight(), null);  
  
                    g.drawRect(getSmallPlayRec().x - 1,  
                            getSmallPlayRec().y - 1,  
                            getSmallPlayRec().width + 1,  
                            getSmallPlayRec().height + 1);  
                } else {  
  
                }  
            }  
        };  
        viewBigPane.setBackground(Color.black);  
        this.add(viewBigPane, BorderLayout.CENTER);  
        viewBigPane.setLayout(null);  
        // ///////////////////////////////  
        viewSmallPane = new JPanel() {  
            public void paintComponent(Graphics g) {  
                super.paintComponent(g);  
                if (!isViewSmallPlaying) {  
                    g.drawImage(VideolocalIcon.getImage(), 0, 0, null);  
                } else {  
  
                }  
            }  
        };  
        viewSmallPane.setBounds(getSmallPlayRec());  
        viewBigPane.add(viewSmallPane);  
        viewSmallPane.setLayout(null);  
  
        controlPane = new JPanel();  
        controlPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        this.add(controlPane, BorderLayout.SOUTH);  
      
    }  
    /**
     * 关闭
     * <p>方法说明:</>
     * <li></li>
     * @author DuanYong
     * @since 2014-1-17 上午10:53:03
     * @version 1.0
     * @exception
     */
    public void close(){
    	 if (localPlayer != null) {  
             localPlayer.stop();  
             localPlayer.close();
             localPlayer = null;
         }  
         if (remotePlayer != null) {  
             remotePlayer.stop();  
             remotePlayer.close();
             remotePlayer = null;
         }  
         if (videotapePlayer != null) {  
             videotapePlayer.stop(); 
             videotapePlayer.close();
             videotapePlayer = null;
         }  
         if (videotapeProcessor != null) {  
             videotapeProcessor.stop();  
             videotapeProcessor.close();
             videotapeProcessor = null;
         }  
         if (videotapeFileWriter != null) {  
             try {  
                 videotapeFileWriter.stop();  
                 videotapeFileWriter.close();  
             } catch (IOException e) { 
            	 e.printStackTrace();
             }  
         }  
    }
  
    public Dimension getMinimumSize() {  
        System.out  
                .println("controlPane.getHeight():" + controlPane.getHeight());  
        return new Dimension(videoReqIcon.getIconWidth() + 2,  
                videoReqIcon.getIconHeight() + controlPane.getHeight());  
    }  
  
    public Dimension getPreferredSize() {  
        System.out  
                .println("controlPane.getHeight():" + controlPane.getHeight());  
        return new Dimension(videoReqIcon.getIconWidth() + 2,  
                videoReqIcon.getIconHeight()  
                        + controlPane.getPreferredSize().height);  
    }  
  
    public void localPlay(DataSource dataSource) {  
        this.setLocalData(dataSource);  
        try {  
            localPlayer = javax.media.Manager.createPlayer(dataSource);  
  
            localPlayer.addControllerListener(new ControllerListener() {  
                public void controllerUpdate(ControllerEvent e) {  
  
                    if (e instanceof javax.media.RealizeCompleteEvent) {  
                        Component comp = null;  
                        comp = localPlayer.getVisualComponent();  
                        if (comp != null) {  
                            // 将可视容器加到窗体上  
                            comp.setBounds(0, 0, VideolocalIcon.getIconWidth(),  
                            		VideolocalIcon.getIconHeight());  
                            viewSmallPane.add(comp);  
                        }  
                        viewBigPane.validate();  
                    }  
                }  
            });  
            localPlayer.start();  
            localPlay = true;  
        } catch (NoPlayerException e1) {  
            e1.printStackTrace();  
        } catch (IOException e1) {  
            e1.printStackTrace();  
        }  
    }  
  
    private Rectangle getSmallPlayRec() {  
        int bigShowWidth = videoReqIcon.getIconWidth();  
        int bigShowHeight = videoReqIcon.getIconHeight();  
        int smallShowWidth = VideolocalIcon.getIconWidth();  
        int smallShowHeight = VideolocalIcon.getIconHeight();  
        return new Rectangle(bigShowWidth - smallShowWidth - 2, bigShowHeight  
                - smallShowHeight - 2, smallShowWidth, smallShowHeight);  
    }  
  
    public void remotePlay(DataSource dataSource) {  
        this.setLocalData(dataSource);  
        remotePlay = true;  
        try {  
            remotePlayer = javax.media.Manager.createPlayer(dataSource);  
  
            remotePlayer.addControllerListener(new ControllerListener() {  
                public void controllerUpdate(ControllerEvent e) {  
  
                    if (e instanceof javax.media.RealizeCompleteEvent) {  
                        Component comp;  
                        if ((comp = remotePlayer.getVisualComponent()) != null) {  
                            // 将可视容器加到窗体上  
                            comp.setBounds(1, 1, videoReqIcon.getIconWidth(),  
                                    videoReqIcon.getIconHeight());  
                            viewBigPane.add(comp);  
                        }  
                        viewBigPane.validate();  
                    }  
                }  
            });  
            remotePlayer.start();  
            remotePlay = true;  
        } catch (NoPlayerException e1) {  
            e1.printStackTrace();  
        } catch (IOException e1) {  
            e1.printStackTrace();  
        }  
    }  
  
    public void closeViewUI() {  
        isShow = false;  
    }  
  
    public boolean isViewRunning() {  
        return isViewRun;  
    }  
  
    public boolean isShowing() {  
        return isShow;  
    }  
  
    public void localReady() {  
        localPlay = true;  
    }  
  
    public void remoteReady() {  
        remotePlay = true;  
    }  
  
    public boolean isRemotePlay() {  
        return remotePlay;  
    }  
  
    public void setRemotePlay(boolean remotePlay) {  
        this.remotePlay = remotePlay;  
    }  
  
    public DataSource getRemoteData() {  
        return remoteData;  
    }  
  
    public void setRemoteData(DataSource remoteData) {  
        this.remoteData = remoteData;  
    }  
  
    public boolean isLocalPlay() {  
        return localPlay;  
    }  
  
    public void setLocalPlay(boolean localPlay) {  
        this.localPlay = localPlay;  
    }  
  
    public DataSource getLocalData() {  
        return localData;  
    }  
  
    public void setLocalData(DataSource localData) {  
        this.localData = localData;  
    }  
}
