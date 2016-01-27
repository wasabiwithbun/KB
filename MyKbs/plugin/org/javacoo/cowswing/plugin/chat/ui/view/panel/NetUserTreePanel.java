/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.chat.ui.view.panel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.annotation.Resource;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.core.event.CowSwingEvent;
import org.javacoo.cowswing.core.event.CowSwingListener;
import org.javacoo.cowswing.core.event.CowSwingObserver;
import org.javacoo.cowswing.core.event.type.CowSwingEventType;
import org.javacoo.cowswing.plugin.chat.ui.view.frame.KbsMultiVideoFrame;
import org.javacoo.cowswing.plugin.core.net.MsgBean;
import org.javacoo.cowswing.plugin.core.net.NetManager;
import org.javacoo.cowswing.plugin.core.service.beans.NetClientBean;
import org.javacoo.cowswing.plugin.core.ui.model.KbsNetUserTreeNode;
import org.javacoo.cowswing.plugin.kbs.ui.model.KbsChatUserDefaultTreeCellRenderer;
import org.springframework.stereotype.Component;

/**
 * 网络用户树panel
 * <p>
 * 说明:
 * </p>
 * <li></li>
 * 
 * @author DuanYong
 * @since 2013-7-11 下午3:50:33
 * @version 1.0
 */
@Component("netUserTreePanel")
public class NetUserTreePanel extends JPanel implements CowSwingListener{
	protected Logger logger = Logger.getLogger(this.getClass());
	private static final long serialVersionUID = 1L;
	/**网络管理服务*/
	@Resource(name="netManager")
    private NetManager netManager;
	/**聊天面板*/
	@Resource(name = "kbsChatPanel")
	private KbsChatPanel kbsChatPanel;
	/**视频会议面板*/
	@Resource(name = "kbsMultiVideoFrame")
	private KbsMultiVideoFrame kbsMultiVideoFrame;
	/** 网络用户树 */
	private JTree tree;
	private JScrollPane scrollPane;
	/** 网络用户树model */
	private DefaultTreeModel treeModel;
	/** 网络用户树SelectionModel */
	private TreeSelectionModel selectionModel;
	/** 刷新按钮 */
	private JButton refreshButton;
	/** 多人视频按钮 */
	private JButton multiVideoButton;
	/** 视频按钮 */
	private JButton videoButton;
	/** 取消视频按钮 */
	private JButton cancelButton;
	/** 同意视频按钮 */
	private JButton agreeButton;
	/** 拒绝视频按钮 */
	private JButton refuseButton;
	/**根节点*/
	private NetClientBean rootNetClientBean;
	/**选择的节点*/
	private NetClientBean selectNetClientBean;
	/** 控制面板 */
	private JPanel controlPanel;
	private boolean hasInit = false;
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
		CowSwingObserver.getInstance().addCrawlerListener(this);
	}

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
	private void initCmp() {
		tree = new JTree(createRootTreeModel());
		tree.setCellRenderer(new KbsChatUserDefaultTreeCellRenderer());
		scrollPane = new JScrollPane(tree);
		treeModel = (DefaultTreeModel) tree.getModel();

		selectionModel = tree.getSelectionModel();
		selectionModel.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		refreshButton = new JButton(LanguageLoader.getString("Kbs.chat_refresh"),ImageLoader.getImageIcon("CrawlerResource.kbs-refresh"));

		multiVideoButton = new JButton(LanguageLoader.getString("video.btn_multi"),ImageLoader.getImageIcon("CrawlerResource.toolbarImage"));
		//multiVideoButton.setEnabled(false);
		
		videoButton = new JButton(LanguageLoader.getString("video.btn_title"),ImageLoader.getImageIcon("CrawlerResource.toolImageListSetting"));
		videoButton.setEnabled(false);
		
		cancelButton = new JButton(LanguageLoader.getString("video.btn_close"),ImageLoader.getImageIcon("CrawlerResource.toolImageListDelete"));
		cancelButton.setEnabled(false);
		
		agreeButton = new JButton(LanguageLoader.getString("video.btn_agree"),ImageLoader.getImageIcon("CrawlerResource.toolImageListAdd"));
		agreeButton.setEnabled(false);
		
		refuseButton = new JButton(LanguageLoader.getString("video.btn_refuse"),ImageLoader.getImageIcon("CrawlerResource.toolImageListAdd"));
		refuseButton.setEnabled(false);
		
		
		controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(3,2,2,2));
		controlPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		controlPanel.add(refreshButton);
		controlPanel.add(videoButton);
		controlPanel.add(agreeButton);
		controlPanel.add(refuseButton);
		controlPanel.add(cancelButton);
		controlPanel.add(multiVideoButton);
		this.add(controlPanel, BorderLayout.NORTH);
		this.add(scrollPane, BorderLayout.CENTER);
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
	private void initEvent() {
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				TreePath path = e.getNewLeadSelectionPath();
				if(null != path && null != path.getLastPathComponent()){
					KbsNetUserTreeNode currentNode =(KbsNetUserTreeNode) path.getLastPathComponent();
					if(null != currentNode){
						selectNetClientBean = currentNode.getNetClientBean();
						kbsChatPanel.setSelectNetClientBean(currentNode.getNetClientBean());
						if(StringUtils.isNotBlank(selectNetClientBean.getIp()) && !selectNetClientBean.getIp().equals(netManager.getNetClientBean().getIp())){
							videoButton.setEnabled(true);
						}
					}else{
						kbsChatPanel.setSelectNetClientBean(rootNetClientBean);
						videoButton.setEnabled(false);
					}
					kbsChatPanel.setSendBtnState(true);
				}
			}
		});
		tree.addTreeExpansionListener(new TreeExpansionListener() {
			public void treeCollapsed(TreeExpansionEvent e) {
			}
			public void treeExpanded(TreeExpansionEvent e) {
				TreePath path = e.getPath();
				KbsNetUserTreeNode node = (KbsNetUserTreeNode) path.getLastPathComponent();
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
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				logger.info("刷新重新加载在线用户信息");
				kbsChatPanel.refresh();
				videoButton.setEnabled(false);
			}
		});
		videoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				logger.info("请求视频信息");
				kbsChatPanel.applyVideo();
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				logger.info("断开视频信息");
				kbsChatPanel.cancelVideo();
			}
		});
		agreeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				logger.info("同意视频信息");
				kbsChatPanel.agreeVideo();
			}
		});
		refuseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				logger.info("拒绝视频信息");
				kbsChatPanel.refuseVideo();
			}
		});
		multiVideoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				logger.info("开启视频会议");
				kbsMultiVideoFrame.init();
				kbsMultiVideoFrame.startVideo();
			}
		});
		
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
		rootNetClientBean = new NetClientBean("",0,"当前在线用户:"+netManager.getNetUserCount()+"人");
		rootNetClientBean.setLeaf(false);
		KbsNetUserTreeNode rootNode = new KbsNetUserTreeNode(rootNetClientBean, netManager);
		rootNode.explore();
		return new DefaultTreeModel(rootNode);
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

	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.core.event.CowSwingListener#update(org.javacoo.cowswing.core.event.CowSwingEvent)
	 */
	@Override
	public void update(CowSwingEvent event) {
		if (event.getEventType().equals(CowSwingEventType.NetUserOnlineEvent) || event.getEventType().equals(CowSwingEventType.NetUserOutlineEvent)) {
			logger.info("重新加载在线用户信息");
			refresh();
		}else if(CowSwingEventType.NetRefresh.equals(event.getEventType())){
			MsgBean msgBean = (MsgBean)event.getEventObject();
			netManager.addNetUserToList(new NetClientBean(msgBean.getParams()[0], Integer.valueOf(msgBean.getParams()[1]),msgBean.getParams()[2]));
			refresh();
		}
		
	}
	/**
	 * 刷新
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-9-4 上午11:22:19
	 * @version 1.0
	 * @exception
	 */
	private void refresh(){
		tree.setModel(createRootTreeModel());
	}

	/**
	 * @return the videoButton
	 */
	public JButton getVideoButton() {
		return videoButton;
	}

	/**
	 * @return the cancelButton
	 */
	public JButton getCancelButton() {
		return cancelButton;
	}

	/**
	 * @return the agreeButton
	 */
	public JButton getAgreeButton() {
		return agreeButton;
	}

	/**
	 * @return the refuseButton
	 */
	public JButton getRefuseButton() {
		return refuseButton;
	}
	

}
