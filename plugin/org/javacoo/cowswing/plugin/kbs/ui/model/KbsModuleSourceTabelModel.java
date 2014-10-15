/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.model;

import java.util.List;

import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleSourceBean;
import org.javacoo.cowswing.ui.model.AbstractCrawlerTableModel;

/**
 * 资源模块TabelModel
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-15 下午4:29:21
 * @version 1.0
 */
public class KbsModuleSourceTabelModel extends AbstractCrawlerTableModel<KbsModuleSourceBean>{
	private static final long serialVersionUID = 1L;
	public KbsModuleSourceTabelModel(List<String> columnNames){
		super(columnNames);
	}
	public KbsModuleSourceTabelModel(List<String> columnNames,List<KbsModuleSourceBean> dataList){
		super(columnNames,dataList);
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.ui.model.AbstractCrawlerTableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object value = null;
		KbsModuleSourceBean entity = dataList.get(rowIndex);
		switch (columnIndex) {
		case 0:
			value = entity.getId();
			break;
		case 1:
			value = entity.getTitle();
			break;
		case 2:
			value = entity.getFileName();
			break;
		case 3:
			value = entity.getKeyword();
			break;
		case 4:
			value = entity.getTypeName();
			break;
		case 5:
			value = entity.getPurviewStr();
			break;
		case 6:
			value = entity.getUploadDateStr();
			break;
		}
		return value;
	}

}
