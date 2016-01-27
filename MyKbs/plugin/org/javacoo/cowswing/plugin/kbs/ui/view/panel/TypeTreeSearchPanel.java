/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.view.panel;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.annotation.Resource;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.apache.commons.lang.StringUtils;
import org.javacoo.cowswing.base.service.ICowSwingService;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsTypeBean;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsTypeCriteria;
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
@Component("typeTreeSearchPanel")
public class TypeTreeSearchPanel extends AbstractTypeTreePanel {
	private static final long serialVersionUID = 1L;
	@Resource(name = "kbsSearchPanel")
	private KbsSearchPanel kbsSearchPanel;
	/**容器*/
	@Resource(name="pageContainer")
    private PageContainer pageContainer;
	private String key;
	private String[] keys;
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
				KbsTypeTreeNode currentNode =(KbsTypeTreeNode) path.getLastPathComponent();
				boolean nodesAreSelected = (path != null);
				
				pageContainer.addPage(kbsSearchPanel, kbsSearchPanel.getPageId());
				kbsSearchPanel.init();
				//加载资源
				key = currentNode.getKbsType().getTypeCode();
				if(StringUtils.isNotBlank(key) && key.contains(KbsConstant.SYSTEM_LINE_SPITE)){
					keys = key.split(KbsConstant.SYSTEM_LINE_SPITE);
					key = keys[keys.length - 1];
				}
				kbsSearchPanel.getList(key);
			}
		});
		tree.addTreeExpansionListener(new TreeExpansionListener() {
			public void treeCollapsed(TreeExpansionEvent e) {
			}
			public void treeExpanded(TreeExpansionEvent e) {
				TreePath path = e.getPath();
				KbsTypeTreeNode node = (KbsTypeTreeNode) path.getLastPathComponent();
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
	 * @return the kbsTypeService
	 */
	public ICowSwingService<KbsTypeBean, KbsTypeCriteria> getKbsTypeService() {
		return kbsTypeService;
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
