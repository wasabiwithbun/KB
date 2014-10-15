/**
 * 如石子一粒,仰高山之巍峨,但不自惭形秽.
 * 若小草一棵,慕白杨之伟岸,却不妄自菲薄.
 */
package org.javacoo.cowswing.plugin.kbs.ui.model;

import java.util.List;

import org.javacoo.cowswing.base.loader.LanguageLoader;
import org.javacoo.cowswing.plugin.kbs.constant.KbsConstant;
import org.javacoo.cowswing.plugin.kbs.service.beans.KbsModuleArticleBean;
import org.javacoo.cowswing.ui.model.AbstractCrawlerTableModel;

/**
 * 文章模块TabelModel
 * <p>说明:</p>
 * <li></li>
 * @author DuanYong
 * @since 2013-7-26 下午5:48:47
 * @version 1.0
 */
public class KbsModuleArticleTabelModel extends AbstractCrawlerTableModel<KbsModuleArticleBean>{
	private static final long serialVersionUID = 1L;
	public KbsModuleArticleTabelModel(List<String> columnNames){
		super(columnNames);
	}
	public KbsModuleArticleTabelModel(List<String> columnNames,List<KbsModuleArticleBean> dataList){
		super(columnNames,dataList);
	}
	/* (non-Javadoc)
	 * @see org.javacoo.cowswing.ui.model.AbstractCrawlerTableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object value = null;
		KbsModuleArticleBean entity = dataList.get(rowIndex);
		switch (columnIndex) {
		case 0:
			value = entity.getId();
			break;
		case 1:
			value = entity.getTitle();
			break;
		case 2:
			value = translateOrigin(entity.getOrigin());
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
			value = entity.getReleaseDateStr();
			break;
		}
		return value;
	}


	/**
	 * 转换来源
	 * <p>方法说明:</>
	 * <li></li>
	 * @author DuanYong
	 * @since 2013-7-28 下午1:20:01
	 * @version 1.0
	 * @exception 
	 * @param origin
	 * @return
	 */
	private String translateOrigin(String origin){
		if(KbsConstant.ORIGIN_PERSON.equals(origin)){
			return LanguageLoader.getString("Kbs.module_article_add_panel_origin_person");
		}else if(KbsConstant.ORIGIN_NETWORK.equals(origin)){
			return LanguageLoader.getString("Kbs.module_article_add_panel_origin_newwork");
		}
		return origin;
	}
}
