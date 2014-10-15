/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.core.ui.model;

import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.javacoo.cowswing.plugin.core.net.NetManager;
import org.javacoo.cowswing.plugin.core.service.beans.NetClientBean;
import org.springframework.util.CollectionUtils;


/**
 * 网络用户TreeNode
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-11 下午4:13:21
 * @version 1.0
 */
public class KbsNetUserTreeNode extends DefaultMutableTreeNode{
	private static final long serialVersionUID = 1L;
	private boolean explored = false;
    private NetManager netManager;
	public KbsNetUserTreeNode(NetClientBean netClientBean,NetManager netManager) {
		setUserObject(netClientBean);
		this.netManager = netManager;
	}

	public boolean getAllowsChildren() {
		return !getNetClientBean().isLeaf();
	}

	public boolean isLeaf() {
		return getNetClientBean().isLeaf();
	}

	public NetClientBean getNetClientBean() {
		return (NetClientBean) getUserObject();
	}

	public boolean isExplored() {
		return explored;
	}

	public String toString() {
		return getNetClientBean().getUserName();
	}
	
	
	public void explore() {
		if (getNetClientBean().isLeaf()){
			return;
		}
		if (!isExplored()) {
			List<NetClientBean> childList = netManager.getNetUserList();
			if(!CollectionUtils.isEmpty(childList)){
				for(NetClientBean netClientBean : childList){
					add(new KbsNetUserTreeNode(netClientBean,netManager));
				}
			}
			explored = true;
		}
	}
}
