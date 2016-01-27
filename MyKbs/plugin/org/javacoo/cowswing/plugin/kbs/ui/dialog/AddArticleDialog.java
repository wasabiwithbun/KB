/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.dialog;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.tree.TreePath;

import org.apache.commons.lang.StringUtils;
import org.javacoo.cowswing.base.constant.Constant;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.base.service.ICowSwingService;
import org.javacoo.cowswing.base.utils.MsgDialogUtil;
import org.javacoo.cowswing.core.event.CowSwingEvent;
import org.javacoo.cowswing.core.event.CowSwingListener;
import org.javacoo.cowswing.core.event.type.CowSwingEventType;
import org.javacoo.cowswing.plugin.core.constant.CoreConstant;
import org.javacoo.cowswing.plugin.core.net.INetManager;
import org.javacoo.cowswing.plugin.core.net.MsgBean;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.event.KbsEventType;
import org.javacoo.cowswing.plugin.kbs.service.IndexWrap;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleArticleBean;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleArticleCriteria;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsTypeBean;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsTypeCriteria;
import org.javacoo.cowswing.plugin.kbs.ui.model.KbsModuleArticleTabelModel;
import org.javacoo.cowswing.plugin.kbs.ui.model.KbsTypeTreeNode;
import org.javacoo.cowswing.plugin.kbs.ui.view.panel.AddArticlePanel;
import org.javacoo.cowswing.plugin.kbs.ui.view.panel.KbsModuleArticleListPanel;
import org.javacoo.cowswing.plugin.kbs.ui.view.panel.TypeTreePanel;
import org.javacoo.cowswing.ui.view.dialog.AbstractDialog;
import org.springframework.stereotype.Component;

/**
 * 添加文章
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-16 下午6:29:21
 * @version 1.0
 */
@Component("addArticleDialog")
public class AddArticleDialog extends AbstractDialog implements
		CowSwingListener {
	private static final long serialVersionUID = 1L;
	/**文章模块服务*/
	@Resource(name="kbsModuleArticleService")
    private ICowSwingService<KbsModuleArticleBean, KbsModuleArticleCriteria> kbsModuleArticleService;
	/** 知识分类服务 */
	@Resource(name = "kbsTypeService")
	protected ICowSwingService<KbsTypeBean, KbsTypeCriteria> kbsTypeService;
	/** 添加文章设置面板 */
	@Resource(name = "addArticlePanel")
	private AddArticlePanel addArticlePanel;
	/**知识分类*/
	@Resource(name="typeTreePanel")
	private TypeTreePanel typeTreePanel;
	/**索引包装类*/
	@Resource(name="indexWrap")
    private IndexWrap indexWrap;
	/**网络管理*/
	@Resource(name = "netManager")
	private INetManager netManager;
	/**
	 * 文章列表
	 */
	@Resource(name="kbsModuleArticleListPanel")
	private KbsModuleArticleListPanel kbsModuleArticleListPanel;
	/** 文章模块 */
	private KbsModuleArticleBean kbsModuleArticleBean;
	/** 知识分类 */
	private KbsTypeBean kbsTypeBean;
	/**类别*/
	private String type;
	/**当前TreeNode*/
	private KbsTypeTreeNode currentNode;

	private Integer id;
	public AddArticleDialog(){
		super(525,450,true);
	}
	@Override
	public JComponent getCenterPane() {
		if (centerPane == null) {
			centerPane = addArticlePanel;
		}
		return centerPane;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.javacoo.cowswing.ui.view.dialog.AbstractDialog#
	 * finishButtonActionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	protected void finishButtonActionPerformed(ActionEvent event) {
		kbsModuleArticleBean = new KbsModuleArticleBean();
		kbsModuleArticleBean = addArticlePanel.getData();
		kbsModuleArticleBean.setReleaseDate(new Date());
		
		if(StringUtils.isBlank(kbsModuleArticleBean.getTitle())){
			MsgDialogUtil.createMessageDialog(LanguageLoader.getString("Kbs.module_source_add_error_title_is_empty"));
		}else if(StringUtils.isBlank(kbsModuleArticleBean.getKeyword())){
			MsgDialogUtil.createMessageDialog(LanguageLoader.getString("Kbs.module_source_add_error_keyword_is_empty"));
		}else{
			if(Constant.OPTION_TYPE_MODIFY == type){
				update(kbsModuleArticleBean);
			}else{
				insert(kbsModuleArticleBean);
			}
		}
		
		this.dispose();
	}
	
	/**
	 * 保存文章
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-17 上午11:21:28
	 * @version 1.0
	 * @exception 
	 * @param kbsModuleArticleBean
	 */
	private void insert(KbsModuleArticleBean kbsModuleArticleBean){
		kbsModuleArticleBean.setCowSwingEvent(new CowSwingEvent(this,KbsEventType.KbsModuleArticleAddEvent));
		int id = kbsModuleArticleService.insert(kbsModuleArticleBean, KbsConstant.SQLMAP_ID_INSERT_KBS_MODULE_ARTICLE);
		kbsModuleArticleBean.setId(id);
		indexWrap.buildArticleIndex(kbsModuleArticleBean,true);
		share(kbsModuleArticleBean);
	}
	/**
	 * 分享
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-1 上午11:45:04
	 * @version 1.0
	 * @exception 
	 * @param kbsModuleArticleBean
	 */
	private void share(KbsModuleArticleBean kbsModuleArticleBean){
		if(KbsConstant.SEARCH_PURVIEW_VIEW.equals(kbsModuleArticleBean.getPurview())){
			netManager.sendCommonData(populateArticleMsg(kbsModuleArticleBean));
		}
	}
	/**
	 * 更新文章
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-31 上午10:41:10
	 * @version 1.0
	 * @exception 
	 * @param kbsModuleArticleBean
	 */
	private void update(KbsModuleArticleBean kbsModuleArticleBean){
		kbsModuleArticleBean.setId(id);
		List<Integer> idList = new ArrayList<Integer>();
		idList.add(id);
		kbsModuleArticleBean.setIdList(idList);
		kbsModuleArticleBean.setCowSwingEvent(new CowSwingEvent(this,KbsEventType.KbsModuleArticleUpdateEvent));
		kbsModuleArticleService.update(kbsModuleArticleBean, KbsConstant.SQLMAP_ID_UPDATE_KBS_MODULE_ARTICLE);
		indexWrap.updateArticleIndex(kbsModuleArticleBean);
		share(kbsModuleArticleBean);
	}
	
	protected void initData(String type) {
		this.type = type;
		JTable articleListTable = kbsModuleArticleListPanel.getKbsModuleArticleTable();
		TreePath parentPath = typeTreePanel.getTree().getSelectionPath();
		currentNode = (KbsTypeTreeNode) parentPath.getLastPathComponent();
		kbsTypeBean = currentNode.getKbsType();
		if(articleListTable.getSelectedRow() != -1 && Constant.OPTION_TYPE_MODIFY == type){
			KbsModuleArticleTabelModel articleTabelModel = (KbsModuleArticleTabelModel)articleListTable.getModel();
			kbsModuleArticleBean = articleTabelModel.getRowObject(articleListTable.getSelectedRow());
			this.id = kbsModuleArticleBean.getId();
		}else{
			kbsModuleArticleBean = new KbsModuleArticleBean();
			kbsModuleArticleBean.setTypeCode(kbsTypeBean.getTypeCode());
			kbsModuleArticleBean.setTypeName(kbsTypeBean.getTypeName());
		}
		if(StringUtils.isNotBlank(kbsTypeBean.getExpandTypeCode())){
			List<KbsTypeBean> childList = getKbsTypeList(kbsTypeBean.getExpandTypeCode());
			kbsModuleArticleBean.setKbsTypeBeanList(childList);
		}
		
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
		logger.info("填充JTabbedPane值");
		addArticlePanel.initData(kbsModuleArticleBean);
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
		msgBean.setPurview(articleBean.getPurview());
		msgBean.setTitle(articleBean.getTitle());
		msgBean.setModule(KbsConstant.SYSTEM_MODULE_ARTICLE);
		if(KbsConstant.SEARCH_PURVIEW_VIEW.equals(kbsModuleArticleBean.getPurview())){
			msgBean.setAction(CoreConstant.NET_ACTION_TYPE_ADD);
		}else{
			msgBean.setAction(CoreConstant.NET_ACTION_TYPE_DEL);
		}
		msgBean.setPort(netManager.getServer().getPort());
		msgBean.setIp(netManager.getServer().getIp());
		return msgBean;
	}
	
	/**
	 * 取得类型列表
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-30 下午5:32:07
	 * @version 1.0
	 * @exception 
	 * @param typeCode
	 * @return
	 */
	private List<KbsTypeBean> getKbsTypeList(String typeCode){
		KbsTypeCriteria q = new KbsTypeCriteria();
		q.setTypeCode(typeCode);
		return kbsTypeService.getList(q, KbsConstant.SQLMAP_ID_GET_LIST_KBS_TYPE);
	}

	
}
