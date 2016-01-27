/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.model;

import java.util.List;

import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.domain.KnowledgePoint;
import org.javacoo.cowswing.ui.model.AbstractCrawlerTableModel;

/**
 * 知识点TabelModel
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-10 下午3:20:32
 * @version 1.0
 */
public class KnowledgePointTabelModel extends AbstractCrawlerTableModel<KnowledgePoint>{
	private static final long serialVersionUID = 1L;
	public KnowledgePointTabelModel(List<String> columnNames){
		super(columnNames);
	}
	public KnowledgePointTabelModel(List<String> columnNames,List<KnowledgePoint> dataList){
		super(columnNames,dataList);
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.ui.model.AbstractCrawlerTableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object value = null;
		KnowledgePoint entity = dataList.get(rowIndex);
		switch (columnIndex) {
		case 0:
			value = entity.getTitle();
			break;
		case 1:
			value = entity.getTypeDesc();
			break;
		case 2:
			value = translateModule(entity.getModule());
			break;
		case 3:
			value = entity.getShowDate();
			break;
		}
		return value;
	}
	/**
	 * 模块名称转换
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-24 上午10:34:36
	 * @version 1.0
	 * @exception 
	 * @param moduleName
	 * @return
	 */
	private String translateModule(String moduleName){
		if(KbsConstant.SYSTEM_MODULE_SOURCE.equals(moduleName)){
			return LanguageLoader.getString("Kbs.module_source_tab_title");
		}else if(KbsConstant.SYSTEM_MODULE_ARTICLE.equals(moduleName)){
			return LanguageLoader.getString("Kbs.module_article_tab_title");
		}
		return moduleName;
	}

}
