/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.action;

import java.awt.event.ActionEvent;
import java.util.Map;

import javax.annotation.Resource;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;

import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.base.utils.JsonUtils;
import org.javacoo.cowswing.base.utils.MsgDialogUtil;
import org.javacoo.cowswing.core.cache.UserCacheManager;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.service.LocalFileIndexService;
import org.javacoo.cowswing.plugin.kbs.ui.model.KbsLocalTreeNode;
import org.javacoo.cowswing.plugin.kbs.ui.view.panel.LocalTreeSearchPanel;
import org.springframework.stereotype.Component;

/**
 * 删除本地文件监听ACTION
 * 
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-7-21 下午2:18:55
 * @version 1.0
 */
@Component("deleteLocalFileListenerAction")
public class DeleteLocalFileListenerAction extends AbstractAction{
	private static final long serialVersionUID = 1L;
	/** 用户缓存管理类 */
	protected static UserCacheManager userCacheManager = UserCacheManager.getInstance();
	/**本地文件树*/
	@Resource(name="localTreeSearchPanel")
	private LocalTreeSearchPanel localTreeSearchPanel;
	/**本地索引服务*/
	@Resource(name="localFileIndexService")
	private LocalFileIndexService localFileIndexService;
	public DeleteLocalFileListenerAction(){
		super("",ImageLoader.getImageIcon("CrawlerResource.systemDataBaseDelete"));
		putValue(SHORT_DESCRIPTION, LanguageLoader.getString("Kbs.local_remove_local_listener"));
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		int result = JOptionPane.showConfirmDialog(null, LanguageLoader.getString("Kbs.local_remove_local_listener_info"),LanguageLoader.getString("Common.deleteConfirm"), JOptionPane.YES_NO_OPTION); 
		if(result == 0){
			TreePath path = localTreeSearchPanel.getTree().getSelectionPath();
			KbsLocalTreeNode currentNode = (KbsLocalTreeNode) path.getLastPathComponent();
			String listenerDirs = userCacheManager.getValue(KbsConstant.KBS_LOCAL_FILE_PROPERTY_KEY_LISTENER_DIRS);
			Object obj = JsonUtils.formatStringToObject(listenerDirs, Map.class);
			Map<String,Map<String,String>> dirSetingMap = null;
			if(null != obj){
				dirSetingMap = (Map<String, Map<String, String>>)obj;
				dirSetingMap.remove(currentNode.getKbsType().getTypeCode());
				userCacheManager.setValue(KbsConstant.KBS_LOCAL_FILE_PROPERTY_KEY_LISTENER_DIRS, JsonUtils.formatObjectToJsonString(dirSetingMap));
			}
			localFileIndexService.removeListener(currentNode.getKbsType().getTypeCode(),true);
			//MsgDialogUtil.createMessageDialog(LanguageLoader.getString("Kbs.local_remove_local_listener_succ"));
			//localTreeSearchPanel.getTreeModel().reload();
			localTreeSearchPanel.getAddListenerButton().setEnabled(true);
			localTreeSearchPanel.getRemoveListenerButton().setEnabled(false);
			localTreeSearchPanel.getUpdateListenerButton().setEnabled(false);
			
		}
	}

}
