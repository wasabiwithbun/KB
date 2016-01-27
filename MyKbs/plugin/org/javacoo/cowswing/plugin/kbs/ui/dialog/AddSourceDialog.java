/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.dialog;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Date;

import javax.annotation.Resource;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.base.service.ICowSwingService;
import org.javacoo.cowswing.base.utils.FileUtils;
import org.javacoo.cowswing.base.utils.MsgDialogUtil;
import org.javacoo.cowswing.core.event.CowSwingEvent;
import org.javacoo.cowswing.core.event.CowSwingListener;
import org.javacoo.cowswing.plugin.core.constant.CoreConstant;
import org.javacoo.cowswing.plugin.core.net.INetManager;
import org.javacoo.cowswing.plugin.core.net.MsgBean;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.event.KbsEventType;
import org.javacoo.cowswing.plugin.kbs.service.IndexWrap;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleSourceBean;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleSourceCriteria;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsTypeBean;
import org.javacoo.cowswing.plugin.kbs.ui.model.KbsTypeTreeNode;
import org.javacoo.cowswing.plugin.kbs.ui.view.panel.AddSourcePanel;
import org.javacoo.cowswing.plugin.kbs.ui.view.panel.TypeTreePanel;
import org.javacoo.cowswing.ui.view.dialog.AbstractDialog;
import org.javacoo.cowswing.ui.view.dialog.WaitingDialog;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * 添加资源
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-16 下午6:29:21
 * @version 1.0
 */
@Component("addSourceDialog")
public class AddSourceDialog extends AbstractDialog implements
		CowSwingListener {
	private static final long serialVersionUID = 1L;
	/**资源模块服务*/
	@Resource(name="kbsModuleSourceService")
    private ICowSwingService<KbsModuleSourceBean, KbsModuleSourceCriteria> kbsModuleSourceService;
	/** 添加资源设置面板 */
	@Resource(name = "addSourcePanel")
	private AddSourcePanel addSourcePanel;
	/**知识分类*/
	@Resource(name="typeTreePanel")
	private TypeTreePanel typeTreePanel;
	/**索引包装类*/
	@Resource(name="indexWrap")
    private IndexWrap indexWrap;
	/**网络管理*/
	@Resource(name = "netManager")
	private INetManager netManager;
	/** 资源模块 */
	private KbsModuleSourceBean kbsModuleSourceBean;
	/** 知识分类 */
	private KbsTypeBean kbsTypeBean;
	/**类别*/
	private String type;
	/**当前TreeNode*/
	private KbsTypeTreeNode currentNode;
	 /**匹配空行正则表达式*/
    private static String EMPT_REGEX = "[\\s| ]*";
	public AddSourceDialog(){
		super(500,400,true);
	}
	@Override
	public JComponent getCenterPane() {
		if (centerPane == null) {
			centerPane = addSourcePanel;
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
		kbsModuleSourceBean = new KbsModuleSourceBean();
		kbsModuleSourceBean = addSourcePanel.getData();
		if(StringUtils.isBlank(kbsModuleSourceBean.getTitle())){
			MsgDialogUtil.createMessageDialog(LanguageLoader.getString("Kbs.module_source_add_error_title_is_empty"));
		}else if(StringUtils.isBlank(kbsModuleSourceBean.getKeyword())){
			MsgDialogUtil.createMessageDialog(LanguageLoader.getString("Kbs.module_source_add_error_keyword_is_empty"));
		}else{
			final AddSourceDialog addSourceDialog = this;
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					//开启线程上传文件及建立索引
					Thread currThread = new Thread(new UploadSource(kbsModuleSourceBean));
					currThread.start();
					Thread waitingThread = new Thread(new WaitingDialog(addSourceDialog,currThread,LanguageLoader.getString("Kbs.module_source_add_uploading")));
					waitingThread.start();
				}
			});
		}
		
		this.dispose();
	}
	class UploadSource implements Runnable{
		private KbsModuleSourceBean sourceBean;
        public UploadSource(KbsModuleSourceBean sourceBean){
        	this.sourceBean = sourceBean;
        }
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			if(!CollectionUtils.isEmpty(this.sourceBean.getFilePathList())){
				KbsModuleSourceBean tempSourceBean = null;
				boolean isSingleFile = this.sourceBean.getFilePathList().size() > 1 ? false : true;
				for(String filePath : this.sourceBean.getFilePathList()){
					tempSourceBean = new KbsModuleSourceBean();
					tempSourceBean.setFilePath(filePath);
					populateSourceBean(tempSourceBean,isSingleFile);
					insert(tempSourceBean);
				}
			}
		}
		/**
		 * 组装资源对象
		 * <p>方法说明:</>
		 * <li></li>
		 * @author DuanYong
		 * @since 2013-7-23 下午3:46:53
		 * @version 1.0
		 * @exception 
		 * @param tempSourceBean
		 */
		private void populateSourceBean(KbsModuleSourceBean tempSourceBean,boolean isSingleFile){
			tempSourceBean.setUploadDate(new Date());
			tempSourceBean.setKeyword(isSingleFile ? this.sourceBean.getKeyword() : this.sourceBean.getKeyword()+getFileName(tempSourceBean.getFilePath()));
			tempSourceBean.setTitle(isSingleFile ? this.sourceBean.getTitle() : getFileName(tempSourceBean.getFilePath()));
			tempSourceBean.setPurview(this.sourceBean.getPurview());
			tempSourceBean.setTypeCode(this.sourceBean.getTypeCode());
			tempSourceBean.setTypeName(this.sourceBean.getTypeName());
			tempSourceBean.setFileName(getFileName(tempSourceBean.getFilePath()));
			tempSourceBean.setDirCode(currentNode.getFullPath());
			tempSourceBean.setCowSwingEvent(new CowSwingEvent(this,KbsEventType.KbsModuleSourceAddEvent));
			tempSourceBean.setFilePath(uploadFile(tempSourceBean.getFilePath()));
		}
		/**
		 * 保存资源
		 * <p>方法说明:</>
		 * <li></li>
		 * @author DuanYong
		 * @since 2013-7-17 上午11:21:28
		 * @version 1.0
		 * @exception 
		 * @param kbsModuleSourceBean
		 */
		private void insert(KbsModuleSourceBean kbsModuleSourceBean){
			int id = kbsModuleSourceService.insert(kbsModuleSourceBean, KbsConstant.SQLMAP_ID_INSERT_KBS_MODULE_SOURCE);
			kbsModuleSourceBean.setId(id);
			indexWrap.buildSourceIndex(kbsModuleSourceBean,true);
			share(kbsModuleSourceBean);
		}
		/**
		 * 分享
		 * <p>方法说明:</>
		 * <li></li>
		 * @author DuanYong
		 * @since 2013-9-1 上午11:42:20
		 * @version 1.0
		 * @exception 
		 * @param kbsModuleSourceBean
		 */
		private void share(KbsModuleSourceBean kbsModuleSourceBean){
			if(KbsConstant.SEARCH_PURVIEW_DOWNLOAD.equals(kbsModuleSourceBean.getPurview())){
				netManager.sendCommonData(populateSourceMsg(kbsModuleSourceBean));
			}
		}
		/**
		 * 上传文件
		 * <p>方法说明:</>
		 * <li></li>
		 * @author DuanYong
		 * @since 2013-7-17 下午9:05:45
		 * @version 1.0
		 * @exception 
		 * @param srcFilePath 源文件路径
		 */
		private String uploadFile(String srcFilePath){
			String destFilePath = currentNode.getAbsolutePath()+ getFileName(srcFilePath);
			File srcFile = new File(srcFilePath);
			FileUtils.createFile(srcFile,destFilePath);
			return currentNode.getFullPath() + getFileName(srcFilePath);
		}
		/**
		 * 取得文件路径中的文件名
		 * <p>方法说明:</>
		 * <li>去掉空格,全部转换小写</li>
		 * @author DuanYong
		 * @since 2013-7-24 上午9:38:19
		 * @version 1.0
		 * @exception 
		 * @param filePath 文件路径
		 * @return 文件名
		 */
		private String getFileName(String filePath){
			String fileName = FilenameUtils.getName(filePath);
			fileName = fileName.replaceAll(EMPT_REGEX, "");
			return fileName.toLowerCase();
		}
	}
	
	
	protected void initData(String type) {
		this.type = type;
		TreePath parentPath = typeTreePanel.getTree().getSelectionPath();
		currentNode = (KbsTypeTreeNode) parentPath.getLastPathComponent();
		kbsTypeBean = currentNode.getKbsType();
		kbsModuleSourceBean = new KbsModuleSourceBean();
		kbsModuleSourceBean.setTypeCode(kbsTypeBean.getTypeCode());
		kbsModuleSourceBean.setTypeName(kbsTypeBean.getTypeName());
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
		addSourcePanel.initData(kbsModuleSourceBean);
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
