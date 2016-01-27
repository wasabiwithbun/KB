/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.model;

import java.util.List;

import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.plugin.core.net.MsgBean;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.ui.model.AbstractCrawlerTableModel;

/**
 * 网络数据模型
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-8-16 上午9:31:37
 * @version 1.0
 */
public class KbsNetDataModel extends AbstractCrawlerTableModel<MsgBean>{
	private static final long serialVersionUID = 1L;
	public KbsNetDataModel(List<String> columnNames){
		super(columnNames);
	}
	public KbsNetDataModel(List<String> columnNames,List<MsgBean> dataList){
		super(columnNames,dataList);
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.ui.model.AbstractCrawlerTableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object value = null;
		MsgBean entity = dataList.get(rowIndex);
		switch (columnIndex) {
		case 0:
			value = entity.getTitle();
			break;
		case 1:
			value = entity.getAuthor();
			break;
		case 2:
			value = entity.getIp();
			break;
		case 3:
			value = changeType(entity.getModule());
			break;
		case 4:
			value = entity.getDate();
			break;
		}
		return value;
	}
	
	private String changeType(String type){
		return KbsConstant.SYSTEM_MODULE_SOURCE.equals(type) ? LanguageLoader.getString("Kbs.search_source"):LanguageLoader.getString("Kbs.search_article");
	}
	
}
