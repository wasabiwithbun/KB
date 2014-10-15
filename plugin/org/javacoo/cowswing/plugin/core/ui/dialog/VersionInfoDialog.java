/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.core.ui.dialog;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.annotation.Resource;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.javacoo.cowswing.base.constant.Constant;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.base.utils.FileUtils;
import org.javacoo.cowswing.base.utils.MsgDialogUtil;
import org.javacoo.cowswing.core.event.CowSwingEvent;
import org.javacoo.cowswing.core.event.CowSwingListener;
import org.javacoo.cowswing.core.event.type.CowSwingEventType;
import org.javacoo.cowswing.main.CowSwingMainFrame;
import org.javacoo.cowswing.plugin.core.constant.CoreConstant;
import org.javacoo.cowswing.plugin.core.net.MsgBean;
import org.javacoo.cowswing.plugin.core.net.NetManager;
import org.javacoo.cowswing.plugin.core.ui.view.panel.VersionInfoPanel;
import org.javacoo.cowswing.ui.view.dialog.AbstractDialog;
import org.springframework.stereotype.Component;

/**
 * 版本信息dialog
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-1-2 下午2:56:48
 * @version 1.0
 */
@Component("versionInfoDialog")
public class VersionInfoDialog extends AbstractDialog implements CowSwingListener{
	private static final long serialVersionUID = 1L;
	protected Logger logger = Logger.getLogger(this.getClass());
	/** 版本信息管理面板 */
	@Resource(name = "versionInfoPanel")
	private VersionInfoPanel versionInfoPanel;
	private MsgBean msgBean;

	private VersionInfoDialog versionInfoDialog;
	/**主窗体*/
	@Resource(name="cowSwingMainFrame")
	private CowSwingMainFrame crawlerMainFrame;
	/** 网络管理服务 */
	@Resource(name = "netManager")
	private NetManager netManager;
	
	public VersionInfoDialog(){
		super(500,300,true);
	}
	@Override
	public JComponent getCenterPane() {
		if (centerPane == null) {
			centerPane = versionInfoPanel;
		}
		return centerPane;
	}
	public JButton getCancelButton(){
		return null;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.javacoo.cowswing.ui.view.dialog.AbstractDialog#
	 * finishButtonActionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	protected void finishButtonActionPerformed(ActionEvent event) {
		msgBean = versionInfoPanel.getData();
		versionInfoDialog = this;
		if(null == msgBean || null == msgBean.getSystemVersionBean()){
			MsgDialogUtil.createMessageDialog(LanguageLoader.getString("Core.version_select_isEmpty"));
		}else{
			int result = JOptionPane.showConfirmDialog(versionInfoDialog, LanguageLoader.getString("Core.version_select_Confirm"),LanguageLoader.getString("Common.confirm"), JOptionPane.YES_NO_OPTION); 
			if(result == 0){
				download();
				versionInfoDialog.setVisible(false);
			}else{
				this.dispose();
			}
		}
	}
	private void download(){
		FileUtils.deleteAllFile(Constant.UPDATE_DIR);
		
		MsgBean downloadMsgBean = new MsgBean();
		downloadMsgBean.setFilePath(Constant.PACKAGE_UPDATE);
		downloadMsgBean.setSavePath(Constant.SYSTEM_ROOT_PATH);
		downloadMsgBean.setAuthor(netManager.getServer().getUserName());
		downloadMsgBean.setAction(CoreConstant.NET_ACTION_TYPE_DOWNLOAD);
		downloadMsgBean.setPort(netManager.getServer().getPort());
		downloadMsgBean.setIp(netManager.getServer().getIp());
		netManager.sendClientData(msgBean.getIp(), msgBean.getPort(), downloadMsgBean);
	}

	protected void initData(String type) {
		fillJTabbedPane();
	}
	public void dispose(){
		super.dispose();
		centerPane = null;
	}
	/**
	 * 填充JTabbedPane值
	 * <p>方法说明:</p>
	 * @auther DuanYong
	 * @since 2012-12-3 下午12:20:32
	 * @return void
	 */
	private void fillJTabbedPane(){
		versionInfoPanel.initData(null);
	}
	public void update(CowSwingEvent event) {
		if (event.getEventType().equals(CowSwingEventType.NetDownLoadSuccess)) {
			String msg = event.getEventObject().toString();
			if(msg.contains(Constant.PACKAGE_UPDATE)){
				versionInfoPanel.setVersion(msgBean.getVersion());
				//创建待更新标志文件
				try {
					FileUtils.createFile(Constant.UPDATE_FILE);
				} catch (IOException e) {
					e.printStackTrace();
					logger.error("创建待更新标志文件失败："+e.getMessage());
				}
				versionInfoDialog.dispose();
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						int result = JOptionPane.showConfirmDialog(
								crawlerMainFrame, LanguageLoader.getString("Core.version_update_complete"),
								LanguageLoader.getString("Common.confirm"),
								JOptionPane.YES_NO_OPTION);
						if (result == 0) {
							crawlerMainFrame.dispose();
						}
					}
				});
			}
		}
	}
}
