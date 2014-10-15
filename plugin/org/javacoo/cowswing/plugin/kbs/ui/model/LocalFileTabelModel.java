/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.model;

import java.util.List;

import org.javacoo.cowswing.base.utils.FileUtils;
import org.javacoo.cowswing.plugin.kbs.service.beans.LocalFileBean;
import org.javacoo.cowswing.ui.model.AbstractCrawlerTableModel;

/**
 * 本地文件TabelModel
 * 
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2014-7-17 下午8:27:31
 * @version 1.0
 */
public class LocalFileTabelModel extends AbstractCrawlerTableModel<LocalFileBean>{
	private static final long serialVersionUID = 1L;
	public LocalFileTabelModel(List<String> columnNames){
		super(columnNames);
	}
	public LocalFileTabelModel(List<String> columnNames,List<LocalFileBean> dataList){
		super(columnNames,dataList);
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.ui.model.AbstractCrawlerTableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object value = null;
		LocalFileBean entity = dataList.get(rowIndex);
		switch (columnIndex) {
		case 0:
			value = entity.getName();
			break;
		case 1:
			value = entity.getLength()+"("+FileUtils.FormetFileSize(entity.getLength())+")";
			break;
		case 2:
			value = entity.getLastModified();
			break;
		}
		return value;
	}

}
