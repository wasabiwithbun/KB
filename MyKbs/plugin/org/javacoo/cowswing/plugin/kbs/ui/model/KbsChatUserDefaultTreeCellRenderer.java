/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.model;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.apache.commons.lang.StringUtils;
import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.plugin.core.ui.model.KbsNetUserTreeNode;

/**
 * 
 * <p>
 * 说明:
 * </p>
 * <li></li>
 * 
 * @author DuanYong
 * @since 2013-9-23 下午3:35:53
 * @version 1.0
 */
public class KbsChatUserDefaultTreeCellRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = 1L;

	/**
	 * 重写父类DefaultTreeCellRenderer的方法
	 */
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		// 执行父类原型操作
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);

		setText(value.toString());

		if (sel) {
			setForeground(getTextSelectionColor());
		} else {
			setForeground(getTextNonSelectionColor());
		}

		// 得到每个节点的TreeNode
		KbsNetUserTreeNode node = (KbsNetUserTreeNode) value;
        if(StringUtils.isBlank(node.getNetClientBean().getIp())){
        	this.setIcon(ImageLoader.getImageIcon("CrawlerResource.kbs_chat_user_root"));
        }else{
        	this.setIcon(ImageLoader.getImageIcon("CrawlerResource.kbs_chat_user"));
        }
        
		return this;
	}
}
