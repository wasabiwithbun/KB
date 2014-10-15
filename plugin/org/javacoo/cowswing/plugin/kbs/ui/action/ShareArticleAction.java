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
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleArticleBean;
import org.javacoo.cowswing.plugin.kbs.ui.model.KbsModuleArticleTabelModel;
import org.javacoo.cowswing.plugin.kbs.ui.view.panel.KbsModuleArticleListPanel;
import org.springframework.stereotype.Component;

/**
 * 一键分享文章Action
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-15 下午5:04:49
 * @version 1.0
 */
@Component("shareArticleAction")
public class ShareArticleAction  extends AbstractAction{
	private static final long serialVersionUID = 1L;
	/**文章集合面板*/
	@Resource(name="kbsModuleArticleListPanel")
	private KbsModuleArticleListPanel kbsModuleArticleListPanel;
	private JTable moduleArticleTable;
	/**文章model*/
	private KbsModuleArticleTabelModel kbsModuleArticleTabelModel;
	@Resource(name = "netManager")
	private INetManager netManager;
	public ShareArticleAction(){
		super(LanguageLoader.getString("Kbs.module_article_share_btn"),ImageLoader.getImageIcon("CrawlerResource.kbs_article_share"));
		putValue(SHORT_DESCRIPTION, LanguageLoader.getString("Kbs.module_article_delete_btn"));
		this.setEnabled(false);
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		moduleArticleTable = kbsModuleArticleListPanel.getKbsModuleArticleTable();
		if(moduleArticleTable.getSelectedRow() != -1){
			int result = JOptionPane.showConfirmDialog(null, LanguageLoader.getString("Kbs.module_article_share_info"),LanguageLoader.getString("Common.confirm"), JOptionPane.YES_NO_OPTION); 
			if(result == 0){
				kbsModuleArticleTabelModel = (KbsModuleArticleTabelModel)moduleArticleTable.getModel();
				List<Integer> idList = new ArrayList<Integer>();
				KbsModuleArticleBean tempKbsModuleArticleBean = null;
				for(Integer selectRow : moduleArticleTable.getSelectedRows()){
					tempKbsModuleArticleBean = kbsModuleArticleTabelModel.getRowObject(selectRow);
					if(!KbsConstant.SEARCH_PURVIEW_VIEW.equals(tempKbsModuleArticleBean.getPurview())){
						netManager.sendCommonData(populateArticleMsg(tempKbsModuleArticleBean));
						idList.add(tempKbsModuleArticleBean.getId());
					}
				}
				shareByArticleIdList(idList);
			}
		}
	}
	/**
	 * 根据ID集合分享文章
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-17 下午4:00:15
	 * @version 1.0
	 * @exception 
	 * @param ResourceIdList
	 */
	private void shareByArticleIdList(List<Integer> idList){
		//根据内容Id分享
		KbsModuleArticleBean kbsModuleArticleBean = new KbsModuleArticleBean();
		kbsModuleArticleBean.setIdList(idList);
		kbsModuleArticleBean.setPurview(KbsConstant.SEARCH_PURVIEW_VIEW);
		kbsModuleArticleBean.setCowSwingEvent(new CowSwingEvent(this,KbsEventType.KbsModuleArticleDeleteEvent));
		kbsModuleArticleListPanel.getKbsModuleArticleService().update(kbsModuleArticleBean, KbsConstant.SQLMAP_ID_UPDATE_KBS_MODULE_ARTICLE);
	}
	
	/**
	 * 组装文章数据
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-8-31 上午10:08:12
	 * @version 1.0
	 * @exception 
	 * @param articleBean
	 * @return
	 */
	private MsgBean populateArticleMsg(KbsModuleArticleBean articleBean) {
		MsgBean msgBean = new MsgBean();
		msgBean.setId(articleBean.getId().toString());
		msgBean.setAuthor(netManager.getServer().getUserName());
		msgBean.setDate(articleBean.getReleaseDateStr());
		msgBean.setPurview(KbsConstant.SEARCH_PURVIEW_VIEW);
		msgBean.setTitle(articleBean.getTitle());
		msgBean.setModule(KbsConstant.SYSTEM_MODULE_ARTICLE);
		msgBean.setAction(CoreConstant.NET_ACTION_TYPE_ADD);
		msgBean.setPort(netManager.getServer().getPort());
		msgBean.setIp(netManager.getServer().getIp());
		return msgBean;
	}
	
	
}
