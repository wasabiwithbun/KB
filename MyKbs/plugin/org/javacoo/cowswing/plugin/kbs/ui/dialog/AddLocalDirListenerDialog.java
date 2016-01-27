/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.dialog;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;

import org.apache.commons.lang.StringUtils;
import org.javacoo.cowswing.base.constant.Constant;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.base.utils.JsonUtils;
import org.javacoo.cowswing.base.utils.MsgDialogUtil;
import org.javacoo.cowswing.core.cache.UserCacheManager;
import org.javacoo.cowswing.core.event.CowSwingListener;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.service.LocalFileIndexService;
import org.javacoo.cowswing.plugin.kbs.ui.model.KbsLocalTreeNode;
import org.javacoo.cowswing.plugin.kbs.ui.view.panel.AddLocalDirListenerPanel;
import org.javacoo.cowswing.plugin.kbs.ui.view.panel.LocalTreeSearchPanel;
import org.javacoo.cowswing.ui.view.dialog.AbstractDialog;
import org.springframework.stereotype.Component;

/**
 * 添加本地目录监听设置
 * 
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-7-21 下午5:37:16
 * @version 1.0
 */
@Component("addLocalDirListenerDialog")
public class AddLocalDirListenerDialog extends AbstractDialog implements
		CowSwingListener {
	private static final long serialVersionUID = 1L;

	/**本地索引服务*/
	@Resource(name="localFileIndexService")
	private LocalFileIndexService localFileIndexService;
	/**本地文件树*/
	@Resource(name="localTreeSearchPanel")
	private LocalTreeSearchPanel localTreeSearchPanel;
	/** 添加本地目录监听面板 */
	@Resource(name = "addLocalDirListenerPanel")
	private AddLocalDirListenerPanel addLocalDirListenerPanel;
	/** 用户缓存管理类 */
	protected static UserCacheManager userCacheManager = UserCacheManager.getInstance();
	/** 本地目录MAP */
	private Map<String,String> settingMap = new HashMap<String,String>();
	/**当前目录*/
	private String dirPath;
	/**当前类型*/
	private String type;
	public AddLocalDirListenerDialog(){
		super(350,300,true);
	}
	@Override
	public JComponent getCenterPane() {
		if (centerPane == null) {
			centerPane = addLocalDirListenerPanel;
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
		if(Constant.OPTION_TYPE_ADD.equals(type)){
			int result = JOptionPane.showConfirmDialog(null, LanguageLoader.getString("Kbs.local_add_local_listener_info"),LanguageLoader.getString("Common.confirm"), JOptionPane.YES_NO_OPTION); 
			if(result == 1){
				return;
			}
		}
		settingMap = addLocalDirListenerPanel.getData();
		if(null != settingMap && !settingMap.isEmpty()){
			settingMap.put(KbsConstant.KBS_LOCAL_SEETING_SURR_DIR_KEY, dirPath);
			String listenerDirs = userCacheManager.getValue(KbsConstant.KBS_LOCAL_FILE_PROPERTY_KEY_LISTENER_DIRS);
			Object obj = JsonUtils.formatStringToObject(listenerDirs, Map.class);
			Map<String,Map<String,String>> dirSetingMap = null;
			if(null != obj){
				dirSetingMap = (Map<String, Map<String, String>>)obj;
			}
			
			if (StringUtils.isNotBlank(dirPath)) {
				if(dirSetingMap != null && dirSetingMap.containsKey(dirPath) && Constant.OPTION_TYPE_ADD.equals(type)){
					MsgDialogUtil.createMessageDialog(LanguageLoader.getString("Kbs.local_add_listener_setting_is_exist"));
					return;
				}
				try{
					Integer max = Integer.valueOf(settingMap.get(KbsConstant.KBS_LOCAL_SEETING_SURR_MAXFILESIZE_KEY));
					if(max <= 0){
						MsgDialogUtil.createMessageDialog(LanguageLoader.getString("Kbs.local_add_listener_setting_is_maxFileSizeMastNumber"));
						return;
					}
				}catch(Exception e){
					MsgDialogUtil.createMessageDialog(LanguageLoader.getString("Kbs.local_add_listener_setting_is_maxFileSizeMastNumber"));
					return;
				}
				if(dirSetingMap == null){
					dirSetingMap = new HashMap<String,Map<String,String>>();
				}
				dirSetingMap.put(dirPath, settingMap);
				userCacheManager.setValue(KbsConstant.KBS_LOCAL_FILE_PROPERTY_KEY_LISTENER_DIRS, JsonUtils.formatObjectToJsonString(dirSetingMap));
				if(Constant.OPTION_TYPE_ADD.equals(type)){
					localFileIndexService.addListener(dirPath,true);
				}
			}
			this.dispose();
			localTreeSearchPanel.getAddListenerButton().setEnabled(false);
			localTreeSearchPanel.getRemoveListenerButton().setEnabled(true);
			localTreeSearchPanel.getUpdateListenerButton().setEnabled(true);
		}
	}
	
	protected void initData(String type) {
		TreePath parentPath = localTreeSearchPanel.getTree().getSelectionPath();
		KbsLocalTreeNode currentNode = (KbsLocalTreeNode) parentPath.getLastPathComponent();
		this.dirPath = currentNode.getKbsType().getTypeCode();
		this.type = type;
		if(Constant.OPTION_TYPE_MODIFY.equals(type)){
			String listenerDirs = userCacheManager.getValue(KbsConstant.KBS_LOCAL_FILE_PROPERTY_KEY_LISTENER_DIRS);
			Object obj = JsonUtils.formatStringToObject(listenerDirs, Map.class);
			Map<String,Map<String,String>> dirSetingMap = null;
			if(null != obj){
				dirSetingMap = (Map<String, Map<String, String>>)obj;
				if(dirSetingMap.containsKey(this.dirPath) && null != dirSetingMap.get(this.dirPath)){
					settingMap = dirSetingMap.get(this.dirPath);
				}
			}
		}
		settingMap.put(KbsConstant.KBS_LOCAL_SEETING_SURR_DIR_KEY, this.dirPath);
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
		addLocalDirListenerPanel.initData(settingMap);
	}

}
