/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.view.panel;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Map;

import javax.annotation.Resource;
import javax.swing.JButton;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.javacoo.cowswing.base.utils.JsonUtils;
import org.javacoo.cowswing.core.cache.UserCacheManager;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.ui.action.AddLocalFileListenerAction;
import org.javacoo.cowswing.plugin.kbs.ui.action.DeleteLocalFileListenerAction;
import org.javacoo.cowswing.plugin.kbs.ui.action.UpdateLocalFileListenerAction;
import org.javacoo.cowswing.plugin.kbs.ui.dialog.AddLocalDirListenerDialog;
import org.javacoo.cowswing.plugin.kbs.ui.model.KbsLocalTreeNode;
import org.javacoo.cowswing.ui.view.panel.PageContainer;
import org.springframework.stereotype.Component;

/**
 * 本地盘符树panel
 * 
 * <p>
 * 说明:
 * </p>
 * <li></li>
 * 
 * @author DuanYong
 * @since 2014-7-17 上午10:14:56
 * @version 1.0
 */
@Component("localTreeSearchPanel")
public class LocalTreeSearchPanel extends AbstractLocalTreePanel {
	private static final long serialVersionUID = 1L;
	@Resource(name = "kbsLocalFileListPanel")
	private KbsLocalFileListPanel kbsLocalFileListPanel;
	/** 容器 */
	@Resource(name = "pageContainer")
	private PageContainer pageContainer;
	/** 添加本地文件设置 */
	@Resource(name = "addLocalDirListenerDialog")
	private AddLocalDirListenerDialog addLocalDirListenerDialog;
	
	/** 添加本地文件监听 */
	@Resource(name = "addLocalFileListenerAction")
	protected AddLocalFileListenerAction addLocalFileListenerAction;
	/** 删除本地文件监听 */
	@Resource(name = "deleteLocalFileListenerAction")
	protected DeleteLocalFileListenerAction deleteLocalFileListenerAction;

	/** 修改本地文件监听 */
	@Resource(name = "updateLocalFileListenerAction")
	protected UpdateLocalFileListenerAction updateLocalFileListenerAction;
	
	/** 用户缓存管理类 */
	protected static UserCacheManager userCacheManager = UserCacheManager.getInstance();
	/** 添加监听按钮 */
	protected JButton addListenerButton;
	/** 删除监听按钮 */
	protected JButton removeListenerButton;
	/** 修改监听按钮 */
	protected JButton updateListenerButton;
	private String key;

	/**
	 * 初始化组件
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @since 2013-7-13 下午12:41:22
	 * @version 1.0
	 * @exception
	 */
	protected void doInitCmp() {
		addListenerButton = new JButton(addLocalFileListenerAction);
		addListenerButton.setEnabled(false);
		removeListenerButton = new JButton(deleteLocalFileListenerAction);
		removeListenerButton.setEnabled(false);
		updateListenerButton = new JButton(updateLocalFileListenerAction);
		updateListenerButton.setEnabled(false);

		controlPanel.add(addListenerButton);
		controlPanel.add(removeListenerButton);
		controlPanel.add(updateListenerButton);
	}

	/**
	 * 初始化事件
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @since 2013-7-13 下午12:41:35
	 * @version 1.0
	 * @exception
	 */
	protected void initEvent() {
		pageContainer.addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {
			}

			@Override
			public void componentResized(ComponentEvent e) {
				scrollPane.setPreferredSize(new Dimension(getParent()
						.getWidth() - 6, getParent().getHeight() - 35));
				scrollPane.updateUI();
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub

			}
		});
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				TreePath path = e.getNewLeadSelectionPath();
				if (null == path) {
					return;
				}
				KbsLocalTreeNode currentNode = (KbsLocalTreeNode) path
						.getLastPathComponent();
				pageContainer.addPage(kbsLocalFileListPanel,
						kbsLocalFileListPanel.getPageId());
				
				kbsLocalFileListPanel.init();
				// 加载资源
				key = currentNode.getKbsType().getTypeCode();
				kbsLocalFileListPanel.getList(key);
				if(hasValue(key)){
					addListenerButton.setEnabled(false);
					if(equalsValue(key)){
						removeListenerButton.setEnabled(true);
						updateListenerButton.setEnabled(true);
					}else{
						removeListenerButton.setEnabled(false);
						updateListenerButton.setEnabled(false);
					}
				}else{
					addListenerButton.setEnabled(true);
					removeListenerButton.setEnabled(false);	
					updateListenerButton.setEnabled(false);
				}
			}
		});
		tree.addTreeExpansionListener(new TreeExpansionListener() {
			public void treeCollapsed(TreeExpansionEvent e) {
			}

			public void treeExpanded(TreeExpansionEvent e) {
				TreePath path = e.getPath();
				KbsLocalTreeNode node = (KbsLocalTreeNode) path
						.getLastPathComponent();
				if (!node.isExplored()) {
					DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
					node.explore();
					model.nodeStructureChanged(node);
				}
			}
		});


		treeModel.addTreeModelListener(new TreeModelListener() {
			public void treeNodesInserted(TreeModelEvent e) {

			}

			public void treeNodesRemoved(TreeModelEvent e) {

			}

			public void treeNodesChanged(TreeModelEvent e) {

			}

			public void treeStructureChanged(TreeModelEvent e) {
			}
		});
	}
	/**
	 * 存在此目录
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2014-10-14 下午2:58:35
	 * @version 1.0
	 * @exception 
	 * @param key
	 * @return
	 */
    private boolean hasValue(String key){
    	String listenerDirs = userCacheManager.getValue(KbsConstant.KBS_LOCAL_FILE_PROPERTY_KEY_LISTENER_DIRS);
		Object obj = JsonUtils.formatStringToObject(listenerDirs, Map.class);
		Map<String,Map<String,String>> dirSetingMap = null;
		if(null != obj){
			dirSetingMap = (Map<String, Map<String, String>>)obj;
			for(String tempKey : dirSetingMap.keySet()){
				if(key.contains(tempKey)){
					return true;
				}
			}
		}
		return false;
    }
    private boolean equalsValue(String key){
    	String listenerDirs = userCacheManager.getValue(KbsConstant.KBS_LOCAL_FILE_PROPERTY_KEY_LISTENER_DIRS);
		Object obj = JsonUtils.formatStringToObject(listenerDirs, Map.class);
		Map<String,Map<String,String>> dirSetingMap = null;
		if(null != obj){
			dirSetingMap = (Map<String, Map<String, String>>)obj;
			return dirSetingMap.containsKey(key);
		}
		return false;
    }
	/**
	 * @return the tree
	 */
	public JTree getTree() {
		return tree;
	}

	/**
	 * @return the treeModel
	 */
	public DefaultTreeModel getTreeModel() {
		return treeModel;
	}

	/**
	 * @return the addLocalDirListenerDialog
	 */
	public AddLocalDirListenerDialog getAddLocalDirListenerDialog() {
		return addLocalDirListenerDialog;
	}

	/**
	 * @return the addListenerButton
	 */
	public JButton getAddListenerButton() {
		return addListenerButton;
	}

	/**
	 * @return the removeListenerButton
	 */
	public JButton getRemoveListenerButton() {
		return removeListenerButton;
	}

	/**
	 * @return the updateListenerButton
	 */
	public JButton getUpdateListenerButton() {
		return updateListenerButton;
	}
    
}
