/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.view.panel;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.annotation.Resource;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.base.service.ICowSwingService;
import org.javacoo.cowswing.base.utils.FileUtils;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsTypeBean;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsTypeCriteria;
import org.javacoo.cowswing.plugin.kbs.ui.action.AddArticleAction;
import org.javacoo.cowswing.plugin.kbs.ui.action.AddKbsTypeAction;
import org.javacoo.cowswing.plugin.kbs.ui.action.AddSourceAction;
import org.javacoo.cowswing.plugin.kbs.ui.action.DeleteKbsTypeAction;
import org.javacoo.cowswing.plugin.kbs.ui.action.UpdateKbsTypeAction;
import org.javacoo.cowswing.plugin.kbs.ui.dialog.KbsTypeSettingDialog;
import org.javacoo.cowswing.plugin.kbs.ui.model.KbsTypeTreeNode;
import org.javacoo.cowswing.ui.view.panel.PageContainer;
import org.springframework.stereotype.Component;

/**
 * 知识分类树panel
 * <p>
 * 说明:
 * </p>
 * <li></li>
 * 
 * @author DuanYong
 * @since 2013-7-11 下午3:50:33
 * @version 1.0
 */
@Component("typeTreePanel")
public class TypeTreePanel extends AbstractTypeTreePanel {
	private static final long serialVersionUID = 1L;
	@Resource(name = "kbsManagerPanel")
	private KbsManagerPanel kbsManagerPanel;
	/**容器*/
	@Resource(name="pageContainer")
    private PageContainer pageContainer;
	/** 知识分类设置 */
	@Resource(name = "kbsTypeSettingDialog")
	private KbsTypeSettingDialog kbsTypeSettingDialog;
	/** 添加知识分类 */
	@Resource(name = "addKbsTypeAction")
	private AddKbsTypeAction addKbsTypeAction;
	/** 修改知识分类 */
	@Resource(name = "updateKbsTypeAction")
	private UpdateKbsTypeAction updateKbsTypeAction;
	/** 删除知识分类 */
	@Resource(name = "deleteKbsTypeAction")
	private DeleteKbsTypeAction deleteKbsTypeAction;
	/**
	 * 添加Action
	 */
	@Resource(name="addArticleAction")
	private AddArticleAction addArticleAction;
	/**
	 * 添加Action
	 */
	@Resource(name="addSourceAction")
	private AddSourceAction addSourceAction;
	
	/** 删除按钮 */
	private JButton removeButton;
	/** 添加按钮 */
	private JButton addButton;
	/** 修改按钮 */
	private JButton updateButton;
	/** 添加文章按钮 */
	private JButton addArticleButton;
	/** 添加资源按钮 */
	private JButton addSourceButton;
	/**右键菜单*/
	private JPopupMenu popMenu;
	JMenuItem updateKbsTypeItem;
	JMenuItem deleteKbsTypeItem;

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
		removeButton = new JButton(deleteKbsTypeAction);
		addButton = new JButton(addKbsTypeAction);
		updateButton = new JButton(updateKbsTypeAction);
		addArticleButton = new JButton(addArticleAction);
		addArticleButton.registerKeyboardAction(addArticleAction, KeyStroke.getKeyStroke(new Integer('B'), InputEvent.CTRL_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);  
		addSourceButton = new JButton(addSourceAction);
		
		addButton.setEnabled(false);
		updateButton.setEnabled(false);
		removeButton.setEnabled(false);
		addArticleButton.setEnabled(false);
		addSourceButton.setEnabled(false);
		
		controlPanel.add(addButton);
		controlPanel.add(updateButton);
		controlPanel.add(removeButton);
		controlPanel.add(addArticleButton);
		controlPanel.add(addSourceButton);
		
		popMenu = new JPopupMenu();
		JMenuItem addKbsTypeItem = new JMenuItem(LanguageLoader.getString("Kbs.type_add_btn"),ImageLoader.getImageIcon("CrawlerResource.kbs_folder_add"));
		addKbsTypeItem.addActionListener(addKbsTypeAction);
		
        popMenu.add(addKbsTypeItem);
        updateKbsTypeItem = new JMenuItem(LanguageLoader.getString("Kbs.type_update_btn"),ImageLoader.getImageIcon("CrawlerResource.kbs_folder_edit"));
        updateKbsTypeItem.addActionListener(updateKbsTypeAction);
        popMenu.add(updateKbsTypeItem);
        deleteKbsTypeItem = new JMenuItem(LanguageLoader.getString("Kbs.type_del_btn"),ImageLoader.getImageIcon("CrawlerResource.kbs_folder_delete"));
        deleteKbsTypeItem.addActionListener(deleteKbsTypeAction);
        popMenu.add(deleteKbsTypeItem);
        
        popMenu.add(new JSeparator(JSeparator.HORIZONTAL));
        
        JMenuItem addArticleItem = new JMenuItem(LanguageLoader.getString("Kbs.module_article_add_btn"),ImageLoader.getImageIcon("CrawlerResource.kbs_article_add"));
        addArticleItem.addActionListener(addArticleAction);
        popMenu.add(addArticleItem);
        JMenuItem addSourceItem = new JMenuItem(LanguageLoader.getString("Kbs.module_source_add_btn"),ImageLoader.getImageIcon("CrawlerResource.kbs_source_add"));
        addSourceItem.addActionListener(addSourceAction);
        popMenu.add(addSourceItem);
		
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
				scrollPane.setPreferredSize(new Dimension(getParent().getWidth() - 6, getParent().getHeight() - 35));
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
				if(null == path){
					return;
				}
				KbsTypeTreeNode currentNode = (KbsTypeTreeNode) path
						.getLastPathComponent();
				//添加到主界面
				pageContainer.addPage(kbsManagerPanel, kbsManagerPanel.getPageId());
				kbsManagerPanel.init();
				
				boolean nodesAreSelected = (path != null);
				addButton.setEnabled(nodesAreSelected);
				updateButton.setEnabled(nodesAreSelected);
				removeButton.setEnabled(nodesAreSelected);
				addArticleButton.setEnabled(nodesAreSelected);
				addSourceButton.setEnabled(nodesAreSelected);
				
				if (KbsConstant.KBS_TYPE_ROOT_CODE.equals(currentNode.getKbsType().getTypeCode())
						|| KbsConstant.KBS_TYPE_CHILD_DEFAULT_CODE.equals(currentNode.getKbsType().getTypeCode())
						|| KbsConstant.KBS_TYPE_CHILD_ADDRESS_CODE.equals(currentNode.getKbsType().getTypeCode())
						|| KbsConstant.KBS_TYPE_CHILD_BOOKMARK_CODE.equals(currentNode.getKbsType().getTypeCode())) {
					updateButton.setEnabled(false);
					removeButton.setEnabled(false);
					updateKbsTypeItem.setEnabled(false);
					deleteKbsTypeItem.setEnabled(false);
				}else{
					updateButton.setEnabled(true);
					removeButton.setEnabled(true);
					updateKbsTypeItem.setEnabled(true);
					deleteKbsTypeItem.setEnabled(true);
				}

				// 加载资源
				kbsManagerPanel.getKbsModuleSourceListPanel().getList(
						currentNode.getKbsType().getTypeCode());
				// 加载文章
				kbsManagerPanel.getKbsModuleArticleListPanel().getList(
						currentNode.getKbsType().getTypeCode());

				kbsManagerPanel.getKbsModuleSourceListPanel().setButtonEnabled(
						true);
				kbsManagerPanel.getKbsModuleArticleListPanel()
						.setButtonEnabled(true);
				// if(currentNode.getChildCount() == 0){
				// kbsManagerPanel.getKbsModuleSourceListPanel().setButtonEnabled(true);
				// kbsManagerPanel.getKbsModuleArticleListPanel().setButtonEnabled(true);
				// }else{
				// kbsManagerPanel.getKbsModuleSourceListPanel().setButtonEnabled(false);
				// kbsManagerPanel.getKbsModuleArticleListPanel().setButtonEnabled(false);
				// }
			}
		});
		tree.addTreeExpansionListener(new TreeExpansionListener() {
			public void treeCollapsed(TreeExpansionEvent e) {
			}

			public void treeExpanded(TreeExpansionEvent e) {
				TreePath path = e.getPath();
				KbsTypeTreeNode node = (KbsTypeTreeNode) path
						.getLastPathComponent();
				if (!node.isExplored()) {
					DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
					node.explore();
					model.nodeStructureChanged(node);
				}
			}
		});
		tree.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				TreePath path = tree.getPathForLocation(e.getX(), e.getY()); 
		        if (path == null) {
		            return;
		        }
		        tree.setSelectionPath(path);
		        if (e.getButton() == 3) {
		            popMenu.show(tree, e.getX(), e.getY());
		        }
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	/**
	 * 插入新节点
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @since 2013-7-13 下午9:04:27
	 * @version 1.0
	 * @exception
	 * @param parentNode
	 * @param kbsTypeBean
	 */
	public void insertNode(KbsTypeTreeNode parentNode, KbsTypeBean kbsTypeBean) {
		KbsTypeTreeNode newNode = createNode(kbsTypeBean);
		if(parentNode.getChildCount() > 0){
			treeModel.insertNodeInto(newNode, parentNode,
					parentNode.getChildCount());
		}
		parentNode.getKbsType().setLeaf(false);
		tree.scrollPathToVisible(new TreePath(newNode.getPath()));
		// 创建目录
		FileUtils.createDir(KbsConstant.SYSTEM_ROOT_PATH
				+ KbsConstant.SYSTEM_SEPARATOR + newNode.getAbsolutePath());
		tree.updateUI();
	}
	/**
	 * 修改节点
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-25 下午4:01:08
	 * @version 1.0
	 * @exception 
	 * @param currentNode
	 * @param kbsTypeBean
	 */
	public void updateNode(KbsTypeTreeNode currentNode, KbsTypeBean kbsTypeBean){
		currentNode.getKbsType().setTypeName(kbsTypeBean.getTypeName());
		currentNode.getKbsType().setExpandTypeCode(kbsTypeBean.getExpandTypeCode());
		currentNode.getKbsType().setExpandTypeName(kbsTypeBean.getExpandTypeName());
		tree.updateUI();
	}

	/**
	 * 删除目录
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @since 2013-7-17 下午5:50:39
	 * @version 1.0
	 * @exception
	 * @param currNode
	 */
	public void deleteNode(KbsTypeTreeNode currNode) {
		if (null != currNode) {
			// 删除目录
			FileUtils.deleteDir(currNode.getAbsolutePath());
			treeModel.removeNodeFromParent(currNode);
			tree.updateUI();
		}
	}

	

	/**
	 * 创建节点
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @since 2013-7-13 下午1:01:34
	 * @version 1.0
	 * @exception
	 * @param node
	 * @return
	 */
	private KbsTypeTreeNode createNode(KbsTypeBean node) {
		return new KbsTypeTreeNode(node, kbsTypeService);
	}

	/**
	 * @return the kbsTypeService
	 */
	public ICowSwingService<KbsTypeBean, KbsTypeCriteria> getKbsTypeService() {
		return kbsTypeService;
	}

	/**
	 * @return the kbsTypeSettingDialog
	 */
	public KbsTypeSettingDialog getKbsTypeSettingDialog() {
		return kbsTypeSettingDialog;
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

	

}
