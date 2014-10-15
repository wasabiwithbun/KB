/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.action;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.annotation.Resource;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JTable;

import org.apache.log4j.Logger;
import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.base.utils.JsonUtils;
import org.javacoo.cowswing.plugin.core.constant.CoreConstant;
import org.javacoo.cowswing.plugin.core.net.MsgBean;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.ui.model.KbsNetDataModel;
import org.javacoo.cowswing.plugin.kbs.ui.view.panel.KbsNetSearchPanel;
import org.javacoo.cowswing.ui.view.dialog.ViewDialog;
import org.springframework.stereotype.Component;

/**
 * 查看Action
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-15 下午5:04:49
 * @version 1.0
 */
@Component("viewNetSearchAction")
public class ViewNetSearchAction  extends AbstractAction{
	private static final long serialVersionUID = 1L;
	protected Logger logger = Logger.getLogger(this.getClass());
	/**集合面板*/
	@Resource(name="kbsNetSearchPanel")
	private KbsNetSearchPanel kbsNetSearchPanel;
	private JTable searchTable;
	/**model*/
	private KbsNetDataModel kbsNetDataModel;
	/**
	 * 详细信息页面
	 */
	@Resource(name="viewDialog")
	private ViewDialog viewDialog;
	public ViewNetSearchAction(){
		super(LanguageLoader.getString("Kbs.search_net_view"),ImageLoader.getImageIcon("CrawlerResource.kbs_source_view"));
		putValue(SHORT_DESCRIPTION, LanguageLoader.getString("Kbs.module_article_view_btn"));
		this.setEnabled(false);
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		searchTable = kbsNetSearchPanel.getNetDataTable();
		if(searchTable.getSelectedRow() != -1){
			kbsNetDataModel = (KbsNetDataModel)searchTable.getModel();
			MsgBean msgBean = kbsNetDataModel.getRowObject(searchTable.getSelectedRow());
			if(KbsConstant.SYSTEM_MODULE_SOURCE.equals(msgBean.getModule())){
				logger.info("弹出下载路径选择框");
				JFileChooser chooser = new JFileChooser();
		        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		        int ret = chooser.showOpenDialog(null);
		        if (ret == JFileChooser.OPEN_DIALOG) {
		            File file = chooser.getSelectedFile();
		            String savePath = file.getAbsolutePath();
		            logger.info("文件保存路径："+savePath);
		            MsgBean downloadMsgBean = new MsgBean();
					downloadMsgBean.setFilePath(msgBean.getFilePath());
					downloadMsgBean.setSavePath(savePath);
					downloadMsgBean.setAuthor(kbsNetSearchPanel.getNetManager().getServer().getUserName());
					downloadMsgBean.setAction(CoreConstant.NET_ACTION_TYPE_DOWNLOAD);
					downloadMsgBean.setPort(kbsNetSearchPanel.getNetManager().getServer().getPort());
					downloadMsgBean.setIp(kbsNetSearchPanel.getNetManager().getServer().getIp());
					kbsNetSearchPanel.getNetManager().sendClientData(msgBean.getIp(), msgBean.getPort(), downloadMsgBean);
		            //kbsNetSearchPanel.getNetManager().getServer().receiveFolder(path);
		        }
				
			}else{
				MsgBean viewMsgBean = new MsgBean();
				viewMsgBean.setId(msgBean.getId());
				viewMsgBean.setModule(msgBean.getModule());
				viewMsgBean.setAuthor(kbsNetSearchPanel.getNetManager().getServer().getUserName());
				viewMsgBean.setAction(CoreConstant.NET_ACTION_TYPE_VIEW);
				viewMsgBean.setPort(kbsNetSearchPanel.getNetManager().getServer().getPort());
				viewMsgBean.setIp(kbsNetSearchPanel.getNetManager().getServer().getIp());
				kbsNetSearchPanel.getNetManager().sendClientData(msgBean.getIp(), msgBean.getPort(), viewMsgBean);
				viewDialog.showContent("...");
				viewDialog.setVisible(true);
			}
		}
	}
	/**
	 * 组装下载请求消息
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-8-20 下午1:57:48
	 * @version 1.0
	 * @exception 
	 * @param msgBean
	 * @return
	 */
	private String populateDownloadString(MsgBean msgBean) {
		msgBean.setAuthor(kbsNetSearchPanel.getNetManager().getServer().getUserName());
		msgBean.setAction(CoreConstant.NET_ACTION_TYPE_DOWNLOAD);
		msgBean.setPort(kbsNetSearchPanel.getNetManager().getServer().getPort());
		msgBean.setIp(kbsNetSearchPanel.getNetManager().getServer().getIp());
		return JsonUtils.formatObjectToJsonString(msgBean);
	}
	/**
	 * 组装查看请求
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-8-20 下午1:58:13
	 * @version 1.0
	 * @exception 
	 * @param msgBean
	 * @return
	 */
	private String populateViewString(MsgBean msgBean) {
		msgBean.setAuthor(kbsNetSearchPanel.getNetManager().getServer().getUserName());
		msgBean.setAction(CoreConstant.NET_ACTION_TYPE_VIEW);
		return JsonUtils.formatObjectToJsonString(msgBean);
	}

}
