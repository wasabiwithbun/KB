/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.core.event.CowSwingEvent;
import org.javacoo.cowswing.plugin.core.constant.CoreConstant;
import org.javacoo.cowswing.plugin.core.net.INetManager;
import org.javacoo.cowswing.plugin.core.net.MsgBean;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.event.KbsEventType;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleSourceBean;
import org.javacoo.cowswing.plugin.kbs.ui.model.KbsModuleSourceTabelModel;
import org.javacoo.cowswing.plugin.kbs.ui.view.panel.KbsModuleSourceListPanel;
import org.springframework.stereotype.Component;

/**
 * 分享资源Action
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-15 下午5:04:49
 * @version 1.0
 */
@Component("shareSourceAction")
public class ShareSourceAction  extends AbstractAction{
	private static final long serialVersionUID = 1L;
	/**资源集合面板*/
	@Resource(name="kbsModuleSourceListPanel")
	private KbsModuleSourceListPanel kbsModuleSourceListPanel;
	private JTable moduleSourceTable;
	/**资源model*/
	private KbsModuleSourceTabelModel kbsModuleSourceTabelModel;
	@Resource(name = "netManager")
	private INetManager netManager;
	public ShareSourceAction(){
		super(LanguageLoader.getString("Kbs.module_source_share_btn"),ImageLoader.getImageIcon("CrawlerResource.kbs_share"));
		putValue(SHORT_DESCRIPTION, LanguageLoader.getString("Kbs.module_source_delete_btn"));
		this.setEnabled(false);
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		moduleSourceTable = kbsModuleSourceListPanel.getKbsModuleSourceTable();
		if(moduleSourceTable.getSelectedRow() != -1){
			int result = JOptionPane.showConfirmDialog(null, LanguageLoader.getString("Kbs.module_source_share_info"),LanguageLoader.getString("Common.confirm"), JOptionPane.YES_NO_OPTION); 
			if(result == 0){
				kbsModuleSourceTabelModel = (KbsModuleSourceTabelModel)moduleSourceTable.getModel();
				List<Integer> resourceIdList = new ArrayList<Integer>();
				List<String> filePathList = new ArrayList<String>();
				KbsModuleSourceBean tempKbsModuleSourceBean = null;
				for(Integer selectRow : moduleSourceTable.getSelectedRows()){
					tempKbsModuleSourceBean = kbsModuleSourceTabelModel.getRowObject(selectRow);
					if(!KbsConstant.SEARCH_PURVIEW_DOWNLOAD.equals(tempKbsModuleSourceBean.getPurview())){
						netManager.sendCommonData(populateSourceMsg(tempKbsModuleSourceBean));
						resourceIdList.add(tempKbsModuleSourceBean.getId());
						filePathList.add(tempKbsModuleSourceBean.getFilePath());
					}
				}
				shareByResourceIdList(resourceIdList);
			}
		}
	}
	/**
	 * 根据ID集合分享相关数据
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-17 下午4:00:15
	 * @version 1.0
	 * @exception 
	 * @param ResourceIdList
	 */
	public void shareByResourceIdList(List<Integer> resourceIdList){
		//根据内容ID删除内容分页
		KbsModuleSourceBean kbsModuleSourceBean = new KbsModuleSourceBean();
		kbsModuleSourceBean.setIdList(resourceIdList);
		kbsModuleSourceBean.setPurview(KbsConstant.SEARCH_PURVIEW_DOWNLOAD);
		kbsModuleSourceBean.setCowSwingEvent(new CowSwingEvent(this,KbsEventType.KbsModuleSourceDeleteEvent));
		kbsModuleSourceListPanel.getKbsModuleSourceService().update(kbsModuleSourceBean, KbsConstant.SQLMAP_ID_UPDATE_KBS_MODULE_SOURCE);
	}
	
	
	/**
	 * 组装资源数据
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-8-31 上午10:08:12
	 * @version 1.0
	 * @exception 
	 * @param articleBean
	 * @return
	 */
	private MsgBean populateSourceMsg(KbsModuleSourceBean sourceBean) {
		MsgBean msgBean = new MsgBean();
		msgBean.setId(sourceBean.getId().toString());
		msgBean.setAuthor(netManager.getServer().getUserName());
		msgBean.setDate(sourceBean.getUploadDateStr());
		msgBean.setPurview(KbsConstant.SEARCH_PURVIEW_DOWNLOAD);
		msgBean.setTitle(sourceBean.getTitle());
		msgBean.setModule(KbsConstant.SYSTEM_MODULE_SOURCE);
		msgBean.setAction(CoreConstant.NET_ACTION_TYPE_ADD);
		msgBean.setPort(netManager.getServer().getPort());
		msgBean.setIp(netManager.getServer().getIp());
		return msgBean;
	}

}
