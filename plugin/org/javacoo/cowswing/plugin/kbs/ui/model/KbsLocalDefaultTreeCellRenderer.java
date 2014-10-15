/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.model;

import java.awt.Component;
import java.util.Map;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.javacoo.cowswing.base.loader.ImageLoader;
import org.javacoo.cowswing.base.utils.JsonUtils;
import org.javacoo.cowswing.core.cache.UserCacheManager;
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
public class KbsLocalDefaultTreeCellRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = 1L;
	/** 用户缓存管理类 */
	protected static UserCacheManager userCacheManager = UserCacheManager.getInstance();
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
		KbsLocalTreeNode node = (KbsLocalTreeNode) value;
        if(KbsConstant.KBS_LOCAL_ROOT_CODE.equals(node.getKbsType().getTypeCode())){
        	this.setIcon(ImageLoader.getImageIcon("CrawlerResource.local_root"));
        }else if(node.getKbsType().getTypeCode().length() == 3){
        	this.setIcon(ImageLoader.getImageIcon("CrawlerResource.navigatorDrive"));
        }else if(!node.getKbsType().isLeaf()){
        	if(hasValue(node.getKbsType().getTypeCode())){
        		this.setIcon(ImageLoader.getImageIcon("CrawlerResource.kbs_folder_feed"));
        	}else{
        		this.setIcon(ImageLoader.getImageIcon("CrawlerResource.kbs_folder"));
        	}
        }else{
        	if(hasValue(node.getKbsType().getTypeCode())){
        		this.setIcon(ImageLoader.getImageIcon("CrawlerResource.kbs_folder_feed"));
        	}else{
        		this.setIcon(ImageLoader.getImageIcon("CrawlerResource.kbs_folder"));
        	}
        }
        
		return this;
	}
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
}
