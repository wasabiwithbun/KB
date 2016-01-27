/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.model;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;

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
public class KbsTypeDefaultTreeCellRenderer extends DefaultTreeCellRenderer {
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
		KbsTypeTreeNode node = (KbsTypeTreeNode) value;
        if(KbsConstant.KBS_TYPE_ROOT_CODE.equals(node.getKbsType().getTypeCode())){
        	this.setIcon(ImageLoader.getImageIcon("CrawlerResource.kbs_type_root"));
        }else if(KbsConstant.KBS_TYPE_CHILD_DEFAULT_CODE.equals(node.getKbsType().getTypeCode())){
        	this.setIcon(ImageLoader.getImageIcon("CrawlerResource.kbs_type"));
        }else if(KbsConstant.KBS_TYPE_CHILD_ADDRESS_CODE.equals(node.getKbsType().getTypeCode())){
        	this.setIcon(ImageLoader.getImageIcon("CrawlerResource.kbs_address"));
        }else if(KbsConstant.KBS_TYPE_CHILD_BOOKMARK_CODE.equals(node.getKbsType().getTypeCode())){
        	this.setIcon(ImageLoader.getImageIcon("CrawlerResource.kbs_bookmark"));
        }else if(node.getKbsType().getTypeCode().startsWith(KbsConstant.KBS_TYPE_CHILD_DEFAULT_CODE)){
        	if(node.getKbsType().isLeaf()){
            	this.setIcon(ImageLoader.getImageIcon("CrawlerResource.kbs_folder_page"));
            }else{
            	this.setIcon(ImageLoader.getImageIcon("CrawlerResource.kbs_folder"));
            }
        }else if(node.getKbsType().getTypeCode().startsWith(KbsConstant.KBS_TYPE_CHILD_ADDRESS_CODE)){
        	if(node.getKbsType().isLeaf()){
            	this.setIcon(ImageLoader.getImageIcon("CrawlerResource.kbs_folder_user"));
            }else{
            	this.setIcon(ImageLoader.getImageIcon("CrawlerResource.kbs_folder"));
            }
        }else if(node.getKbsType().getTypeCode().startsWith(KbsConstant.KBS_TYPE_CHILD_BOOKMARK_CODE)){
        	if(node.getKbsType().isLeaf()){
            	this.setIcon(ImageLoader.getImageIcon("CrawlerResource.kbs_folder_star"));
            }else{
            	this.setIcon(ImageLoader.getImageIcon("CrawlerResource.kbs_folder"));
            }
        }else if(!node.getKbsType().isLeaf()){
        	this.setIcon(ImageLoader.getImageIcon("CrawlerResource.kbs_folder_page"));
        }else{
        	this.setIcon(ImageLoader.getImageIcon("CrawlerResource.kbs_folder"));
        }
        
		return this;
	}
}
