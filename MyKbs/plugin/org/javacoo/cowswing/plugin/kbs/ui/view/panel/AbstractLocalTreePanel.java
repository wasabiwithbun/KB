/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.view.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.util.Enumeration;

import javax.annotation.Resource;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.base.utils.FileUtils;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsTypeBean;
import org.javacoo.cowswing.plugin.kbs.ui.action.AddLocalFileListenerAction;
import org.javacoo.cowswing.plugin.kbs.ui.action.CollapseKbsTypeAction;
import org.javacoo.cowswing.plugin.kbs.ui.action.DeleteLocalFileListenerAction;
import org.javacoo.cowswing.plugin.kbs.ui.action.ExpandKbsTypeAction;
import org.javacoo.cowswing.plugin.kbs.ui.model.KbsLocalDefaultTreeCellRenderer;
import org.javacoo.cowswing.plugin.kbs.ui.model.KbsLocalTreeNode;

/**
 * 
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-9-23 上午11:18:14
 * @version 1.0
 */
public abstract class AbstractLocalTreePanel extends JPanel implements ITypeTree{
	private static final long serialVersionUID = 1L;
	/** 知识分类树 */
	protected JTree tree;
	protected JScrollPane scrollPane;
	/** 知识分类树model */
	protected DefaultTreeModel treeModel;
	/** 知识分类树SelectionModel */
	protected TreeSelectionModel selectionModel;
	/** 控制面板 */
	protected JPanel controlPanel;
	/**是否初始化*/
	protected boolean hasInit = false;
	/** 折叠知识分类 */
	
	/** 展开知识分类 */
	@Resource(name = "expandKbsTypeAction")
	protected ExpandKbsTypeAction expandKbsTypeAction;
	/** 折叠知识分类 */
	@Resource(name = "collapseKbsTypeAction")
	protected CollapseKbsTypeAction collapseKbsTypeAction;
	/** 展开按钮 */
	protected JButton expandButton;
	/** 折叠按钮 */
	protected JButton collapseButton;
	/**展开深度*/
	private int depth = 0;
	/**
	 * 初始化
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @since 2013-7-13 下午12:42:05
	 * @version 1.0
	 * @exception
	 */
	public void init() {
		if(!hasInit){
			this.setLayout(new BorderLayout());
			initCmp();
			initEvent();
		}
	}
	private void initCmp(){
		tree = new JTree(createRootTreeModel());
		tree.setCellRenderer(new KbsLocalDefaultTreeCellRenderer());
		scrollPane = new JScrollPane(tree);
		treeModel = (DefaultTreeModel) tree.getModel();  
		selectionModel = tree.getSelectionModel();
		selectionModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		collapseKbsTypeAction.setiTypeTreePanel(this);
		expandKbsTypeAction.setiTypeTreePanel(this);
		
		
		expandButton = new JButton(expandKbsTypeAction);
		collapseButton = new JButton(collapseKbsTypeAction);
		doInitCmp();
		//controlPanel.add(expandButton);
		//controlPanel.add(collapseButton);
		this.add(controlPanel, BorderLayout.NORTH);
		this.add(scrollPane, BorderLayout.CENTER);
	}
	/**
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-23 上午11:22:07
	 * @version 1.0
	 * @exception 
	 */
	protected abstract void initEvent();
	/**
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-23 上午11:22:05
	 * @version 1.0
	 * @exception 
	 */
	protected abstract void doInitCmp();
	/**
	 * 展开或者折叠所有节点
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @since 2013-9-23 上午10:57:55
	 * @version 1.0
	 * @exception
	 * @param expandAll
	 *            true 为展开所有,false为折叠所有
	 */
	public void expandTree(boolean expandAll) {
		KbsLocalTreeNode root = (KbsLocalTreeNode) tree.getModel().getRoot();
		expand(tree, new TreePath(root), expandAll);
	}
	/**
	 * 创建根节点
	 * <p>
	 * 方法说明:</>
	 * <li></li>
	 * 
	 * @author DuanYong
	 * @since 2013-7-13 下午1:01:11
	 * @version 1.0
	 * @exception
	 * @return
	 */
	private DefaultTreeModel createRootTreeModel() {
		KbsTypeBean root = new KbsTypeBean();
		root.setLeaf(false);
		root.setParentTypeCode(null);
		root.setTypeCode(KbsConstant.KBS_LOCAL_ROOT_CODE);
		root.setTypeName(LanguageLoader.getString("Kbs.local_root_name"));
		KbsLocalTreeNode rootNode = new KbsLocalTreeNode(root);
		FileSystemView sys = FileSystemView.getFileSystemView();
        File[] files = File.listRoots();
        for(int i = 0; i < files.length; i++) {
        	rootNode.add(createChildNode(KbsConstant.KBS_LOCAL_ROOT_CODE,files[i].toString(),sys.getSystemDisplayName(files[i])+"("+FileUtils.FormetFileSize(files[i].getFreeSpace())+"/"+FileUtils.FormetFileSize(files[i].getTotalSpace())+")"));
        }
		rootNode.explore();
		return new DefaultTreeModel(rootNode);
	}
	/**
	 * 创建子节点
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-25 上午11:11:45
	 * @version 1.0
	 * @exception 
	 * @param parentTypeCode 父分类编码
	 * @param typeCode 分类编码
	 * @param typeName 分类名称
	 * @return 子节点
	 */
	private KbsLocalTreeNode createChildNode(String parentTypeCode,String typeCode,String typeName){
		KbsTypeBean child = new KbsTypeBean();
		child.setLeaf(false);
		child.setParentTypeCode(parentTypeCode);
		child.setTypeCode(typeCode);
		child.setTypeName(typeName);
		return new KbsLocalTreeNode(child);
	}
	/**
	 * 展开
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-23 下午3:50:31
	 * @version 1.0
	 * @exception 
	 * @param tree
	 * @param parent
	 * @param expand
	 */
	private void expand(JTree tree, TreePath parent, boolean expand) {
		if(depth > 50 && expand){
			return;
		}
		KbsLocalTreeNode node = (KbsLocalTreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration e = node.children(); e.hasMoreElements();) {
				KbsLocalTreeNode n = (KbsLocalTreeNode) e.nextElement();
				n.explore();
				TreePath path = parent.pathByAddingChild(n);
				expand(tree, path, expand);
			}
		}
		if(null != parent){
			if (expand) {
				tree.expandPath(parent);
			} else {
				tree.collapsePath(parent);
			}
		}
		if(expand){
			depth ++;
		}
	}
	
	
}
